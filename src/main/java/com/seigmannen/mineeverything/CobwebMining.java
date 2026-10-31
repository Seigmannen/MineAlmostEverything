package com.seigmannen.mineeverything;

import com.seigmannen.mineeverything.settings.MiningSetting;
import com.seigmannen.mineeverything.settings.MiningSettings;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Vanilla's cobweb loot table already drops the cobweb with Silk Touch, but cobwebs only drop anything
 * when broken with the "correct tool" (shears or a sword), so a pickaxe gets nothing.
 * We let any pickaxe (the minecraft:pickaxes tag) with Silk Touch count as the correct tool for cobwebs.
 * This is only used for the drops, never for the mining speed, so cobwebs break as fast as in vanilla.
 */
public final class CobwebMining {
	private CobwebMining() {
	}

	/** True when breaking this block with this tool should run the loot table even though vanilla says no. */
	public static boolean canHarvest(Level level, BlockState state, ItemStack tool) {
		return state.is(Blocks.COBWEB) && tool.is(ItemTags.PICKAXES) && SilkTouchMining.hasSilkTouch(level, tool)
				&& MiningSettings.isEnabled(level, MiningSetting.COBWEB);
	}
}
