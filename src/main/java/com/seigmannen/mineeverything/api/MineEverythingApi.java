package com.seigmannen.mineeverything.api;

import com.seigmannen.mineeverything.NetheriteMining;
import com.seigmannen.mineeverything.settings.MiningSetting;
import com.seigmannen.mineeverything.settings.MiningSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The public API for other mods. Only uses plain Minecraft and Java types, so it stays the same
 * even if the mod's own code changes. Works on both the server and the client
 * (the client uses the copy of the settings the server sends it).
 */
public final class MineEverythingApi {
	private MineEverythingApi() {
	}

	/**
	 * Whether a /mae setting is on in this world. The setting names are the same as in /mae:
	 * "bedrock", "reinforced_deepslate", "budding_amethyst", "spawner",
	 * "overworld_bottom", "nether_bottom", "nether_roof", "end_portal_frame" and "cobweb".
	 * An unknown name returns false.
	 */
	public static boolean isEnabled(Level level, String setting) {
		MiningSetting miningSetting = MiningSetting.byName(setting);
		return miningSetting != null && MiningSettings.isEnabled(level, miningSetting);
	}

	/**
	 * Whether the settings allow this mod's rules for this block at this position. Checks the block's own
	 * setting, and for bedrock also "overworld_bottom" (y = -64 in the Overworld), "nether_bottom"
	 * (y = 0 in the Nether) or "nether_roof" (y = 127 in the Nether) when it is on one of those layers.
	 * Blocks without a setting return true.
	 */
	public static boolean isAllowed(Level level, BlockState state, BlockPos pos) {
		return MiningSettings.isAllowed(level, state, pos);
	}

	/**
	 * Whether this tool can mine this block with this mod's rules for blocks that are unbreakable in vanilla
	 * (like bedrock). True when the block is in the mineeverything:netherite_mineable tag, the tool is a
	 * netherite pickaxe, and the settings allow it (including the bedrock layer settings). Uses the same rules as a player.
	 */
	public static boolean canMineUnbreakable(Level level, BlockState state, BlockPos pos, ItemStack tool) {
		return NetheriteMining.canMine(level, state, pos, tool);
	}
}
