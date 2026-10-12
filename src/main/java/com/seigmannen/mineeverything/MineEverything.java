package com.seigmannen.mineeverything;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineEverything implements ModInitializer {
	public static final String MOD_ID = "mineeverything";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// What a block drops is decided by its loot table (data/minecraft/loot_table/blocks/...).
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			// Vanilla already takes 1 durability for mining a block, so we take the rest.
			// hurtAndBreak rolls Unbreaking for every point and does nothing in creative.
			ItemStack pickaxe = player.getMainHandItem();
			if (state.is(NetheriteMining.NETHERITE_MINEABLE) && pickaxe.is(Items.NETHERITE_PICKAXE)) {
				pickaxe.hurtAndBreak(NetheriteMining.DURABILITY_PER_BLOCK - 1, player, EquipmentSlot.MAINHAND);
			}
		});

		LOGGER.info("Mine (Almost) Everything loaded");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
