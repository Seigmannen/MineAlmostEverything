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
	OVERWORLD_BOTTOM("overworld_bottom"),
	/** Bedrock on the lowest layer of the Nether (y = 0). Only matters while BEDROCK is on. */
	NETHER_BOTTOM("nether_bottom"),
	/** Bedrock on the top layer of the Nether roof (y = 127). Only matters while BEDROCK is on. */
	NETHER_ROOF("nether_roof"),
	END_PORTAL_FRAME("end_portal_frame"),
	COBWEB("cobweb");

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
