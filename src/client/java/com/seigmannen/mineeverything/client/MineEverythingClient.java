package com.seigmannen.mineeverything.client;

import com.seigmannen.mineeverything.settings.MiningSettings;
import com.seigmannen.mineeverything.settings.MiningSettingsPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MineEverythingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// The settings are saved on the server, so the server sends them to the client. They decide how fast
		// a block breaks, which the client has to know too.
		ClientPlayNetworking.registerGlobalReceiver(MiningSettingsPayload.TYPE,
				(payload, context) -> MiningSettings.setClientEnabled(payload.enabled()));
		// Don't carry one world's settings over to the next.
		ClientPlayConnectionEvents.DISCONNECT.register((listener, client) -> MiningSettings.resetClient());
	}
}
