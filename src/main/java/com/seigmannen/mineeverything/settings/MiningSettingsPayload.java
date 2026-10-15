package com.seigmannen.mineeverything.settings;

import com.seigmannen.mineeverything.MineEverything;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.EnumSet;
import java.util.Set;

/**
 * Server to client: which mining settings are on. Sent when a player joins and whenever a setting changes.
 * Sent as a number where bit N is the setting with ordinal N.
 */
public record MiningSettingsPayload(Set<MiningSetting> enabled) implements CustomPacketPayload {
	public static final Type<MiningSettingsPayload> TYPE = new Type<>(MineEverything.id("mining_settings"));

	public static final StreamCodec<ByteBuf, MiningSettingsPayload> CODEC =
			ByteBufCodecs.VAR_INT.map(MiningSettingsPayload::fromBits, MiningSettingsPayload::toBits);

	@Override
	public Type<MiningSettingsPayload> type() {
		return TYPE;
	}

	private static MiningSettingsPayload fromBits(int bits) {
		Set<MiningSetting> enabled = EnumSet.noneOf(MiningSetting.class);
		for (MiningSetting setting : MiningSetting.values()) {
			if ((bits & (1 << setting.ordinal())) != 0) {
				enabled.add(setting);
			}
		}
		return new MiningSettingsPayload(enabled);
	}

	private static int toBits(MiningSettingsPayload payload) {
		int bits = 0;
		for (MiningSetting setting : payload.enabled) {
			bits |= 1 << setting.ordinal();
		}
		return bits;
	}
}
