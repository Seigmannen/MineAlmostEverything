package com.seigmannen.mineeverything.mixin;

import com.seigmannen.mineeverything.NetheriteMining;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For blocks in the mineeverything:netherite_mineable tag (like bedrock, which vanilla says is unbreakable)
 * we return our own progress when a netherite pickaxe is used.
 * This code runs on both client and server, so they agree on when the block breaks.
 */
@Mixin(BlockBehaviour.class)
public class BedrockDestroyProgressMixin {
	@Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
	private void mineEverything$mineWithNetherite(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		if (NetheriteMining.canMine(state, player, pos)) {
			cir.setReturnValue(NetheriteMining.getProgressPerTick(player));
		}
	}
}
