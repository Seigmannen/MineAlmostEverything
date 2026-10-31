package com.seigmannen.mineeverything;

import com.seigmannen.mineeverything.settings.MiningSetting;
import com.seigmannen.mineeverything.settings.MiningSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * End portal frames are mined like bedrock (they are in the mineeverything:netherite_mineable tag).
 * This class handles the one extra rule: when a frame of an active portal is removed, the portal goes too.
 */
public final class EndPortalFrameMining {
	/** A vanilla portal is 3 × 3. The limit only stops a strange build from making us search forever. */
	private static final int MAX_PORTAL_BLOCKS = 64;

	private EndPortalFrameMining() {
	}

	/**
	 * Called after a frame was removed from the world, by anyone or anything (players, machines, commands).
	 * The frames of a working portal face inwards, so the block in front of the frame is part of the portal.
	 * From there we remove every connected end portal block on the same layer. Does nothing when the setting is off.
	 */
	public static void removePortal(ServerLevel level, BlockPos framePos, BlockState frameState) {
		if (!MiningSettings.isEnabled(level, MiningSetting.END_PORTAL_FRAME)) {
			return;
		}

		BlockPos start = framePos.relative(frameState.getValue(EndPortalFrameBlock.FACING));
		Set<BlockPos> portal = new HashSet<>();
		Queue<BlockPos> toCheck = new ArrayDeque<>();
		toCheck.add(start);
		while (!toCheck.isEmpty() && portal.size() < MAX_PORTAL_BLOCKS) {
			BlockPos pos = toCheck.remove();
			if (portal.contains(pos) || !level.getBlockState(pos).is(Blocks.END_PORTAL)) {
				continue;
			}
			portal.add(pos);
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				toCheck.add(pos.relative(direction));
			}
		}

		for (BlockPos pos : portal) {
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
		}
	}
}
