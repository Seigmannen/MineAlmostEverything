package com.seigmannen.mineeverything.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.seigmannen.mineeverything.NetheriteMining;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Some blocks (like bedrock) are made with "no loot table" in vanilla, so Minecraft never even looks
 * for a loot table file. For blocks in the mineeverything:netherite_mineable tag we use the normal
 * file path instead (for bedrock: data/minecraft/loot_table/blocks/bedrock.json).
 * Vanilla keeps the loot table in the "drops" field, which is read in two places:
 * getLootTable() and getDrops(). We change both, so drops always go through the loot table system.
 */
@Mixin(BlockBehaviour.class)
public class NetheriteMineableLootTableMixin {
	@Inject(method = "getLootTable", at = @At("RETURN"), cancellable = true)
	private void mineEverything$useDefaultLootTable(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> cir) {
		cir.setReturnValue(mineEverything$withDefaultLootTable(cir.getReturnValue()));
	}

	/** getDrops() reads the "drops" field directly instead of calling getLootTable(), so we change that read too. */
	@ModifyExpressionValue(method = "getDrops", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour;drops:Ljava/util/Optional;"))
	private Optional<ResourceKey<LootTable>> mineEverything$useDefaultLootTableForDrops(Optional<ResourceKey<LootTable>> drops) {
		return mineEverything$withDefaultLootTable(drops);
	}

	private Optional<ResourceKey<LootTable>> mineEverything$withDefaultLootTable(Optional<ResourceKey<LootTable>> drops) {
		if (drops.isPresent()) {
			return drops;
		}

		Block block = (Block) (Object) this;
		if (block.defaultBlockState().is(NetheriteMining.NETHERITE_MINEABLE)) {
			return Optional.of(ResourceKey.create(Registries.LOOT_TABLE,
					BuiltInRegistries.BLOCK.getKey(block).withPrefix("blocks/")));
		}
		return drops;
	}
}
