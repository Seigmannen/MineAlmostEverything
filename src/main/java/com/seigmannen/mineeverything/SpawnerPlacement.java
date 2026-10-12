package com.seigmannen.mineeverything;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * Rules for placing a spawner item, so a spawner mined with Silk Touch keeps its mob type.
 * For safety we only read the mob type (entity ID) from the item and ignore all other data.
 */
public final class SpawnerPlacement {
	private SpawnerPlacement() {
	}

	/** Operators placing a spawner with full block entity data get vanilla behaviour (all data is loaded). */
	public static boolean usesVanillaBehaviour(Player player, ItemStack stack) {
		return player != null && player.canUseGameMasterBlocks() && stack.has(DataComponents.BLOCK_ENTITY_DATA);
	}

	/**
	 * Sets the mob type on a newly placed spawner, if the item has an allowed one.
	 * Returns true if the spawner was changed; otherwise the spawner stays empty.
	 */
	public static boolean applyMobType(Level level, BlockPos pos, SpawnerBlockEntity spawner, ItemStack stack) {
		Optional<EntityType<?>> type = readEntityId(stack)
				.map(Identifier::tryParse)
				.flatMap(BuiltInRegistries.ENTITY_TYPE::getOptional)
				.filter(SpawnerPlacement::hasVanillaSpawnEgg);
		if (type.isEmpty()) {
			return false;
		}

		spawner.setEntityId(type.get(), level.getRandom());
		// Tell nearby players' clients about the new mob type.
		BlockState state = level.getBlockState(pos);
		level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
		return true;
	}

	/**
	 * Reads only SpawnData.entity.id. Our loot table stores it in custom_data;
	 * spawner items from other places (like Creative pick block) store it in block_entity_data.
	 */
	private static Optional<String> readEntityId(ItemStack stack) {
		Optional<String> fromBlockEntityData = Optional.empty();
		TypedEntityData<BlockEntityType<?>> blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
		if (blockEntityData != null && blockEntityData.type() == BlockEntityTypes.MOB_SPAWNER) {
			fromBlockEntityData = getSpawnDataEntityId(blockEntityData.copyTagWithoutId());
		}
		return fromBlockEntityData.or(() ->
				getSpawnDataEntityId(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()));
	}

	private static Optional<String> getSpawnDataEntityId(CompoundTag tag) {
		return tag.getCompoundOrEmpty("SpawnData").getCompoundOrEmpty("entity").getString("id");
	}

	/** Only mobs that have a spawn egg in vanilla are allowed (no command block minecarts and such). */
	private static boolean hasVanillaSpawnEgg(EntityType<?> type) {
		return BuiltInRegistries.ITEM.componentLookup()
				.findMatching(DataComponents.ENTITY_DATA, data -> data.type() == type)
				.anyMatch(item -> item.value() instanceof SpawnEggItem
						&& BuiltInRegistries.ITEM.getKey(item.value()).getNamespace().equals(Identifier.DEFAULT_NAMESPACE));
	}
}
