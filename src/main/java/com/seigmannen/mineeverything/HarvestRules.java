package com.seigmannen.mineeverything;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * All of this mod's "correct tool for drops" rules in one place: cases where vanilla says a tool gets no drops
 * from a block, but this mod says it does. Players (CorrectToolHarvestMixin) and other mods (MineEverythingApi)
 * both ask this class, so they always get the same answer. Add new rules here, and both get them.
 * Only used for the drops, never for the mining speed.
 */
public final class HarvestRules {
	private HarvestRules() {
	}

	/** True when one of this mod's rules (with its /mae setting on) makes this tool the correct tool for drops. */
	public static boolean canHarvest(Level level, BlockState state, BlockPos pos, ItemStack tool) {
		return CobwebMining.canHarvest(level, state, tool);
	}
}
