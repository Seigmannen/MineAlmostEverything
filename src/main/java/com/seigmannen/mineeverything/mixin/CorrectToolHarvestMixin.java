package com.seigmannen.mineeverything.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.seigmannen.mineeverything.HarvestRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * When a player breaks a block, the server asks hasCorrectToolForDrops to decide whether the block drops anything.
 * We add this mod's rules from HarvestRules to that answer (for example a Silk Touch pickaxe on a cobweb).
 * We only change that answer here, not the one used for the mining speed (BlockBehaviour.getDestroyProgress),
 * so blocks don't break any faster.
 */
@Mixin(ServerPlayerGameMode.class)
public class CorrectToolHarvestMixin {
	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;hasCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean mineEverything$applyHarvestRules(ServerPlayer player, BlockState state, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos) {
		return original.call(player, state) || HarvestRules.canHarvest(player.level(), state, pos, player.getMainHandItem());
	}
}
