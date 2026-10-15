package com.seigmannen.mineeverything.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.seigmannen.mineeverything.SilkTouchMining;
import com.seigmannen.mineeverything.settings.MiningSetting;
import com.seigmannen.mineeverything.settings.MiningSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SpawnerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Vanilla spawners always drop XP when mined, even with Silk Touch.
 * When the spawner drops itself (setting on, Silk Touch on a pickaxe from SilkTouchMining.SILK_TOUCH_PICKAXES,
 * the same check as its loot table) it gives no XP, so you get either the spawner or the XP, never both.
 * In every other case the XP is like vanilla.
 */
@Mixin(SpawnerBlock.class)
public class SpawnerExperienceMixin {
	@ModifyArg(method = "spawnAfterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SpawnerBlock;popExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;I)V"), index = 2)
	private int mineEverything$noExperienceWhenDropped(int experience, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) ItemStack tool) {
		if (MiningSettings.isEnabled(level, MiningSetting.SPAWNER) && SilkTouchMining.isSilkTouchPickaxe(level, tool)) {
			return 0;
		}
		return experience;
	}
}
