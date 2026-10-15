package com.seigmannen.mineeverything;

import com.seigmannen.mineeverything.settings.MaeCommand;
import com.seigmannen.mineeverything.settings.MiningAllowedCondition;
import com.seigmannen.mineeverything.settings.MiningSettings;
import com.seigmannen.mineeverything.settings.MiningSettingsPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineEverything implements ModInitializer {
	public static final String MOD_ID = "mineeverything";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		registerSettings();

		// What a block drops is decided by its loot table (data/minecraft/loot_table/blocks/...).
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			// Vanilla already takes 1 durability for mining a block, so we take the rest.
			// hurtAndBreak rolls Unbreaking for every point and does nothing in creative.
			ItemStack pickaxe = player.getMainHandItem();
			if (state.is(NetheriteMining.NETHERITE_MINEABLE) && pickaxe.is(Items.NETHERITE_PICKAXE)
					&& MiningSettings.isAllowed(level, state, pos)) {
				pickaxe.hurtAndBreak(NetheriteMining.DURABILITY_PER_BLOCK - 1, player, EquipmentSlot.MAINHAND);
			}
		});

		LOGGER.info("Mine (Almost) Everything loaded");
	}

	/** The per-world settings: the /mae command, the loot condition, and keeping the clients' copy up to date. */
	private static void registerSettings() {
		Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, id("mining_allowed"), MiningAllowedCondition.MAP_CODEC);
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> MaeCommand.register(dispatcher));

		PayloadTypeRegistry.clientboundPlay().register(MiningSettingsPayload.TYPE, MiningSettingsPayload.CODEC);
		// /mae sends changes itself (MiningSettings.set); this sends the settings to players as they join.
		ServerPlayConnectionEvents.JOIN.register((listener, sender, server) ->
				sender.sendPacket(MiningSettings.createPayload(server)));
		ServerLifecycleEvents.SERVER_STARTED.register(MiningSettings::cleanUpOldGameRules);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
