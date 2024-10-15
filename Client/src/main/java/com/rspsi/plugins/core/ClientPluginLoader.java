package com.rspsi.plugins.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientPluginLoader {

    private static int count = 0;

    private static final Path path = Path.of("plugins" + File.separator + "active");
    public static final Set<ClientPlugin> pluginSet = new HashSet<>();

    public static void loadPlugins() throws MalformedURLException {
        final File file = path.toFile();
        File[] plugins = file.listFiles((dir, name) -> name.endsWith(".jar"));

        if (plugins == null || plugins.length == 0) {
            log.info("No plugins found.");
            return;
        }

        final URL[] urls = new URL[plugins.length];
        for (int index = 0; index < plugins.length; index++) {
            urls[index] = plugins[index].toURI().toURL();
            log.info("Loading plugin: {}", plugins[index].getName());
        }

        Arrays.stream(urls)
                .filter(Objects::nonNull)
                .parallel()
                .forEach(url -> {
                    final URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{url});
                    final ServiceLoader<ClientPlugin> serviceLoader = ServiceLoader.load(ClientPlugin.class, urlClassLoader);
                    for (final ClientPlugin plugin : serviceLoader) {
                        plugin.initializePlugin();
                        pluginSet.add(plugin);
                        count++;
                    }
                });
    }

}
