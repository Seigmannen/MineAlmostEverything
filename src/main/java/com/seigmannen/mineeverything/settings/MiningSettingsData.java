package com.seigmannen.mineeverything.settings;

import com.mojang.serialization.Codec;
import com.seigmannen.mineeverything.MineEverything;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The mining settings of one world, saved in the world folder as data/mineeverything/settings.dat.
 * A setting that isn't in the file is on, so worlds from before this file existed start with everything on.
 * Names in the file that we don't know are ignored.
 */
public final class MiningSettingsData extends SavedData {
	private static final Codec<MiningSettingsData> CODEC = Codec.unboundedMap(Codec.STRING, Codec.BOOL)
			.xmap(MiningSettingsData::fromMap, MiningSettingsData::toMap);

	/** No data fixer: the format is just names and true/false. */
	public static final SavedDataType<MiningSettingsData> TYPE =
			new SavedDataType<>(MineEverything.id("settings"), MiningSettingsData::new, CODEC, null);

	private final Map<MiningSetting, Boolean> values = new EnumMap<>(MiningSetting.class);

	public boolean isEnabled(MiningSetting setting) {
		return values.getOrDefault(setting, true);
	}

	public void set(MiningSetting setting, boolean enabled) {
		values.put(setting, enabled);
		setDirty();
	}

	private static MiningSettingsData fromMap(Map<String, Boolean> map) {
		MiningSettingsData data = new MiningSettingsData();
		map.forEach((name, enabled) -> {
			MiningSetting setting = MiningSetting.byName(name);
			if (setting != null) {
				data.values.put(setting, enabled);
			}
		});
		return data;
	}

	private static Map<String, Boolean> toMap(MiningSettingsData data) {
		Map<String, Boolean> map = new LinkedHashMap<>();
		for (MiningSetting setting : MiningSetting.values()) {
			map.put(setting.getName(), data.isEnabled(setting));
		}
		return map;
	}
}
