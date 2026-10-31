package com.seigmannen.mineeverything.mixin;

import com.seigmannen.mineeverything.EndPortalFrameMining;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla calls affectNeighborsAfterRemoval on the server after a block is replaced by a different block
 * (with neighbour updates on, which is the case for mining, Level.destroyBlock and /setblock).
 * End portal frames don't override it, so we hook the base method and only act for frames.
 */
@Mixin(BlockBehaviour.class)
public class EndPortalFrameRemovalMixin {
	@Inject(method = "affectNeighborsAfterRemoval", at = @At("HEAD"))
	private void mineEverything$removeEndPortal(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston, CallbackInfo ci) {
		if (state.is(Blocks.END_PORTAL_FRAME)) {
			EndPortalFrameMining.removePortal(level, pos, state);
		}
	}
}
