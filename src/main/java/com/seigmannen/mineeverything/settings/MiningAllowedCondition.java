package com.seigmannen.mineeverything.settings;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

/**
 * Loot table condition {"type": "mineeverything:mining_allowed"}: passes when the mining settings
 * allow the broken block at its position (see MiningSettings.isAllowed). Our block loot tables use it,
 * so a block whose setting is off drops nothing, no matter who or what broke it.
 */
public final class MiningAllowedCondition implements LootItemCondition {
	public static final MiningAllowedCondition INSTANCE = new MiningAllowedCondition();
	public static final MapCodec<MiningAllowedCondition> MAP_CODEC = MapCodec.unit(INSTANCE);

	private MiningAllowedCondition() {
	}

	@Override
	public MapCodec<MiningAllowedCondition> codec() {
		return MAP_CODEC;
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Set.of(LootContextParams.BLOCK_STATE, LootContextParams.ORIGIN);
	}

	@Override
	public boolean test(LootContext context) {
		BlockState state = context.getOptional(LootContextParams.BLOCK_STATE);
		Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
		if (state == null || origin == null) {
			return false;
		}
		return MiningSettings.isAllowed(context.getLevel(), state, BlockPos.containing(origin));
	}
}
