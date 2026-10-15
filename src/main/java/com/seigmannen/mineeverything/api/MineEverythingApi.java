package com.seigmannen.mineeverything.api;

import com.seigmannen.mineeverything.settings.MiningSetting;
import com.seigmannen.mineeverything.settings.MiningSettings;
import net.minecraft.core.BlockPos;
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
	 * "bedrock", "reinforced_deepslate", "budding_amethyst", "spawner" and "bottom_layer".
	 * An unknown name returns false.
	 */
	public static boolean isEnabled(Level level, String setting) {
		MiningSetting miningSetting = MiningSetting.byName(setting);
		return miningSetting != null && MiningSettings.isEnabled(level, miningSetting);
	}

	/**
	 * Whether the settings allow this mod's rules for this block at this position. Checks the block's own
	 * setting, and for bedrock also "bottom_layer" when it is on the lowest layer of the Overworld.
	 * Blocks without a setting return true.
	 */
	public static boolean isAllowed(Level level, BlockState state, BlockPos pos) {
		return MiningSettings.isAllowed(level, state, pos);
	}
}
