package com.rspsi.plugins.core;

import com.jagex.Client;
import com.jagex.net.ResourceResponse;

import java.util.concurrent.atomic.AtomicBoolean;

public interface ClientPlugin {
	void initializePlugin();
	void onGameLoaded(Client client) throws Exception;
	default void onResourceDelivered(ResourceResponse resource) {
		
	}
}
