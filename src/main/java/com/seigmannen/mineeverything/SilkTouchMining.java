package com.seigmannen.mineeverything;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Budding amethyst and spawners drop themselves only when mined with Silk Touch on a pickaxe from
 * the item tag data/mineeverything/tags/item/silk_touch_pickaxes.json (and their setting is on).
 * Their loot tables check the same thing. Mining speed is not changed, so it stays like vanilla.
 */
public final class SilkTouchMining {
	/** Pickaxes that can get budding amethyst and spawners with Silk Touch. */
	public static final TagKey<Item> SILK_TOUCH_PICKAXES = TagKey.create(Registries.ITEM, MineEverything.id("silk_touch_pickaxes"));

	private SilkTouchMining() {
	}

	/** True when this tool makes budding amethyst or a spawner drop itself (the setting is checked separately). */
	public static boolean isSilkTouchPickaxe(Level level, ItemStack tool) {
		Holder<Enchantment> silkTouch = level.registryAccess().getOrThrow(Enchantments.SILK_TOUCH);
		return tool.is(SILK_TOUCH_PICKAXES) && EnchantmentHelper.getItemEnchantmentLevel(silkTouch, tool) > 0;
	}
}
