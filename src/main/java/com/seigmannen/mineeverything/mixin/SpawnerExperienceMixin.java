package com.seigmannen.mineeverything.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.SpawnerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Vanilla spawners always drop XP when mined, even with Silk Touch.
 * Ores run their XP through the tool's enchantments, and Silk Touch sets block XP to 0
 * (data/minecraft/enchantment/silk_touch.json). We do the same for spawners,
 * so you get either the spawner (from its loot table) or the XP, never both.
 */
@Mixin(SpawnerBlock.class)
public class SpawnerExperienceMixin {
	@ModifyArg(method = "spawnAfterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SpawnerBlock;popExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;I)V"), index = 2)
	private int mineEverything$applyToolToExperience(int experience, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) ItemStack tool) {
		return EnchantmentHelper.processBlockExperience(level, tool, experience);
	}
}
