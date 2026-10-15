package com.seigmannen.mineeverything.settings;

/**
 * The on/off settings for what this mod lets you mine. They are saved per world
 * (MiningSettingsData) and changed with /mae. All settings are on by default.
 */
public enum MiningSetting {
	BEDROCK("bedrock"),
	REINFORCED_DEEPSLATE("reinforced_deepslate"),
	BUDDING_AMETHYST("budding_amethyst"),
	SPAWNER("spawner"),
	/** Bedrock on the lowest layer of the Overworld (y = -64). Only matters while BEDROCK is on. */
	BOTTOM_LAYER("bottom_layer");

	private final String name;

	MiningSetting(String name) {
		this.name = name;
	}

	/** The name used in /mae and in the save file, like "bedrock". */
	public String getName() {
		return name;
	}

	public static MiningSetting byName(String name) {
		for (MiningSetting setting : values()) {
			if (setting.name.equals(name)) {
				return setting;
			}
		}
		return null;
	}
}
