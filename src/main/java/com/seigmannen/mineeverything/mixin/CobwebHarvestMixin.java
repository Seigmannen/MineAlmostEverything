package com.seigmannen.mineeverything.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.seigmannen.mineeverything.CobwebMining;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * When a player breaks a block, the server asks hasCorrectToolForDrops to decide whether the block drops anything.
 * We only change that answer here, not the one used for the mining speed (BlockBehaviour.getDestroyProgress),
 * so a Silk Touch pickaxe makes a cobweb drop itself without breaking it faster.
 */
@Mixin(ServerPlayerGameMode.class)
public class CobwebHarvestMixin {
	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;hasCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean mineEverything$silkTouchPickaxeHarvestsCobweb(ServerPlayer player, BlockState state, Operation<Boolean> original) {
		return original.call(player, state) || CobwebMining.canHarvest(player.level(), state, player.getMainHandItem());
	}
}
