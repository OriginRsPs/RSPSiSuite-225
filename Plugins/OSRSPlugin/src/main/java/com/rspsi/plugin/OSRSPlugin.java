package com.rspsi.plugin;

import com.displee.cache.index.Index;

import com.displee.cache.index.archive.Archive;
import com.jagex.Cache;
import com.jagex.Client;
import com.jagex.cache.loader.anim.AnimationDefinitionLoader;
import com.jagex.cache.loader.anim.FrameBaseLoader;
import com.jagex.cache.loader.anim.FrameLoader;
import com.jagex.cache.loader.anim.GraphicLoader;
import com.jagex.cache.loader.config.RSAreaLoader;
import com.jagex.cache.loader.config.VariableBitLoader;
import com.jagex.cache.loader.floor.FloorDefinitionLoader;
import com.jagex.cache.loader.map.MapIndexLoader;
import com.jagex.cache.loader.object.ObjectDefinitionLoader;
import com.jagex.cache.loader.textures.TextureLoader;
import com.jagex.net.ResourceResponse;
import com.rspsi.cache.CacheFileType;
import com.rspsi.plugin.loader.AnimationDefinitionLoaderOSRS;
import com.rspsi.plugin.loader.FloorDefinitionLoaderOSRS;
import com.rspsi.plugin.loader.FrameBaseLoaderOSRS;
import com.rspsi.plugin.loader.FrameLoaderOSRS;
import com.rspsi.plugin.loader.GraphicLoaderOSRS;
import com.rspsi.plugin.loader.MapIndexLoaderOSRS;
import com.rspsi.plugin.loader.ObjectDefinitionLoaderOSRS;
import com.rspsi.plugin.loader.RSAreaLoaderOSRS;
import com.rspsi.plugin.loader.TextureLoaderOSRS;
import com.rspsi.plugin.loader.VarbitLoaderOSRS;
import com.rspsi.plugins.core.ClientPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public class OSRSPlugin implements ClientPlugin {

    private FrameLoaderOSRS frameLoader;
    private FloorDefinitionLoaderOSRS floorLoader;
    private ObjectDefinitionLoaderOSRS objLoader;
    private AnimationDefinitionLoaderOSRS animDefLoader;
    private GraphicLoaderOSRS graphicLoader;
    private VarbitLoaderOSRS varbitLoader;
    private MapIndexLoaderOSRS mapIndexLoader;
    private TextureLoaderOSRS textureLoader;
    private FrameBaseLoaderOSRS skeletonLoader;
    private RSAreaLoaderOSRS areaLoader;

    @Override
    public void initializePlugin() {
        objLoader = new ObjectDefinitionLoaderOSRS();
        floorLoader = new FloorDefinitionLoaderOSRS();
        frameLoader = new FrameLoaderOSRS();
        animDefLoader = new AnimationDefinitionLoaderOSRS();

        mapIndexLoader = new MapIndexLoaderOSRS();
        textureLoader = new TextureLoaderOSRS();
        skeletonLoader = new FrameBaseLoaderOSRS();
        graphicLoader = new GraphicLoaderOSRS();
        varbitLoader = new VarbitLoaderOSRS();
        areaLoader = new RSAreaLoaderOSRS();

        MapIndexLoader.instance = mapIndexLoader;
        GraphicLoader.instance = graphicLoader;
        VariableBitLoader.instance = varbitLoader;
        FrameLoader.instance = frameLoader;
        ObjectDefinitionLoader.instance = objLoader;
        FloorDefinitionLoader.instance = floorLoader;
        FrameBaseLoader.instance = skeletonLoader;
        TextureLoader.instance = textureLoader;
        AnimationDefinitionLoader.instance = animDefLoader;
        RSAreaLoader.instance = areaLoader;
    }

    @Override
    public void onGameLoaded(Client client) {

        frameLoader.init(5000);

        final Cache cache = client.getCache();
        if (cache == null) {
            throw new NullPointerException("Invalid cache");
        }

        final Index configIndex = client.getCache().getFile(CacheFileType.CONFIG);

        if (configIndex == null)
            throw new NullPointerException("Invalid configuration index");

        final Archive overlay = configIndex.archive(4);
        if (overlay == null)
            throw new NullPointerException("Invalid overlay archive");

        floorLoader.initOverlays(overlay);

        final Archive underlays = configIndex.archive(1);
        if (underlays == null)
            throw new NullPointerException("Invalid underlay archive");

        floorLoader.initUnderlays(underlays);

        final Archive obj = configIndex.archive(6);
        if (obj == null)
            throw new NullPointerException("Invalid objects archive");

        objLoader.init(obj);

        final Archive anim = configIndex.archive(12);
        if (anim == null)
            throw new NullPointerException("Invalid animation archive");

        animDefLoader.init(anim);

        final Archive gfx = configIndex.archive(13);
        if (gfx == null)
            throw new NullPointerException("Invalid graphics archive");

        graphicLoader.init(gfx);

        final Archive varbit = configIndex.archive(14);
        if (varbit == null)
            throw new NullPointerException("Invalid varbits archive");
        varbitLoader.init(varbit);

        final Archive area = configIndex.archive(35);
        if (area == null)
            throw new NullPointerException("Invalid areas archive");

        areaLoader.init(area);

        objLoader.renameMapFunctions(areaLoader);

        Index skeletonIndex = client.getCache().getFile(CacheFileType.SKELETON);

        skeletonLoader.init(skeletonIndex);

        Index mapIndex = client.getCache().getFile(CacheFileType.MAP);
        mapIndexLoader.init(mapIndex);

        Index textureIndex = client.getCache().getFile(CacheFileType.TEXTURE);
        Index spriteIndex = client.getCache().getFile(CacheFileType.SPRITE);
        textureLoader.init(textureIndex.archive(0), spriteIndex);
    }

    @Override
    public void onResourceDelivered(ResourceResponse arg0) {
        // TODO Auto-generated method stub

    }

}
