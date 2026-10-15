package com.seigmannen.mineeverything.settings;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.EnumSet;
import java.util.Set;

/**
 * Reads and changes the mining settings and decides whether a block may be mined with this mod's rules.
 * The server keeps the settings in the world's save (MiningSettingsData). The client can't read that,
 * so the server sends it a copy (MiningSettingsPayload), which is kept here. Both sides must agree
 * on how fast a block breaks.
 */
public final class MiningSettings {
	/** The copy the client got from the server, or null (everything on) until the server sends one. */
	private static volatile Set<MiningSetting> clientEnabled = null;

	private MiningSettings() {
	}

	private static MiningSettingsData getData(MinecraftServer server) {
		return server.getDataStorage().computeIfAbsent(MiningSettingsData.TYPE);
	}

	/**
	 * Runs once per world: the first time it is opened with this version (no settings file yet).
	 * A test version of the mod stored the settings as gamerules (mineeverything:mine_*). Those gamerules
	 * no longer exist, so Minecraft skips them when it reads the world, but logs an error every time while
	 * they are still in the file. Saving the gamerules again writes the file without them; all other
	 * gamerules are kept. The old values are not copied: everything starts on, like in a new world.
	 */
	public static void cleanUpOldGameRules(MinecraftServer server) {
		SavedDataStorage storage = server.getDataStorage();
		if (storage.get(MiningSettingsData.TYPE) != null) {
			return;
		}
		MiningSettingsData data = new MiningSettingsData();
		data.setDirty();
		storage.set(MiningSettingsData.TYPE, data);

		GameRuleMap gameRules = storage.get(GameRuleMap.TYPE);
		if (gameRules != null) {
			gameRules.setDirty();
		}
	}

	public static boolean isEnabled(MinecraftServer server, MiningSetting setting) {
		return getData(server).isEnabled(setting);
	}

	public static boolean isEnabled(Level level, MiningSetting setting) {
		if (level instanceof ServerLevel serverLevel) {
			return isEnabled(serverLevel.getServer(), setting);
		}
		Set<MiningSetting> enabled = clientEnabled;
		return enabled == null || enabled.contains(setting);
	}

	public static Set<MiningSetting> getEnabled(MinecraftServer server) {
		Set<MiningSetting> enabled = EnumSet.noneOf(MiningSetting.class);
		for (MiningSetting setting : MiningSetting.values()) {
			if (isEnabled(server, setting)) {
				enabled.add(setting);
			}
		}
		return enabled;
	}

	/** Changes a setting, saves it with the world and sends the new settings to every player. */
	public static void set(MinecraftServer server, MiningSetting setting, boolean enabled) {
		getData(server).set(setting, enabled);
		MiningSettingsPayload payload = createPayload(server);
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ServerPlayNetworking.send(player, payload);
		}
	}

	public static MiningSettingsPayload createPayload(MinecraftServer server) {
		return new MiningSettingsPayload(getEnabled(server));
	}

	public static void setClientEnabled(Set<MiningSetting> enabled) {
		clientEnabled = Set.copyOf(enabled);
	}

	public static void resetClient() {
		clientEnabled = null;
	}

	/**
	 * Whether the settings allow this mod's rules for this block at this position.
	 * Each setting only affects its own block. Blocks with no setting (added to the tag by a datapack)
	 * are always allowed.
	 */
	public static boolean isAllowed(Level level, BlockState state, BlockPos pos) {
		if (state.is(Blocks.BEDROCK)) {
			return isEnabled(level, MiningSetting.BEDROCK)
					&& (!isOverworldBottomLayer(level, pos) || isEnabled(level, MiningSetting.BOTTOM_LAYER));
		}
		if (state.is(Blocks.REINFORCED_DEEPSLATE)) {
			return isEnabled(level, MiningSetting.REINFORCED_DEEPSLATE);
		}
		if (state.is(Blocks.BUDDING_AMETHYST)) {
			return isEnabled(level, MiningSetting.BUDDING_AMETHYST);
		}
		if (state.is(Blocks.SPAWNER)) {
			return isEnabled(level, MiningSetting.SPAWNER);
		}
		return true;
	}

	/** The lowest layer of the Overworld (y = -64). */
	private static boolean isOverworldBottomLayer(Level level, BlockPos pos) {
		return level.dimension() == Level.OVERWORLD && pos.getY() == level.getMinY();
	}
}
