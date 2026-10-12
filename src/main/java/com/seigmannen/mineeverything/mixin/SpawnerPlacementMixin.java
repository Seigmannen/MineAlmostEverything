package com.seigmannen.mineeverything.mixin;

import com.seigmannen.mineeverything.SpawnerPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla ignores spawner data when a non-operator places a spawner (onlyOpCanSetNbt).
 * For spawners only, we instead read just the mob type and set it on the new spawner.
 * Runs on the server only, like vanilla's own code here.
 */
@Mixin(BlockItem.class)
public class SpawnerPlacementMixin {
	@Inject(method = "updateCustomBlockEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	private static void mineEverything$placeSpawner(Level level, Player player, BlockPos pos, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (level.isClientSide() || !(level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner)) {
			return;
		}
		if (SpawnerPlacement.usesVanillaBehaviour(player, stack)) {
			return;
		}
		cir.setReturnValue(SpawnerPlacement.applyMobType(level, pos, spawner, stack));
	}
}
