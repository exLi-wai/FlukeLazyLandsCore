package com.lw.flu.explosion;

import java.util.ArrayList;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

public final class ExplosionRecipeEvents {
    private ExplosionRecipeEvents() {
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        var random = level.random;
        var conversions = new ArrayList<PendingConversion>();

        for (var entity : event.getAffectedEntities()) {
            if (!(entity instanceof ItemEntity itemEntity) || !itemEntity.isAlive()) {
                continue;
            }

            var recipe = ExplosionRecipeRegistry.find(itemEntity.getItem()).orElse(null);
            if (recipe == null) {
                continue;
            }

            var inputStack = itemEntity.getItem();
            var conversionCount = inputStack.getCount() / recipe.inputCount();
            if (conversionCount <= 0) {
                continue;
            }

            var remainder = inputStack.getCount() % recipe.inputCount();
            conversions.add(new PendingConversion(itemEntity, itemEntity.position(), recipe, conversionCount, remainder));
        }

        if (conversions.isEmpty()) {
            return;
        }

        event.getAffectedEntities().removeIf(entity -> conversions.stream().anyMatch(conversion -> conversion.itemEntity() == entity));

        for (var conversion : conversions) {
            var itemEntity = conversion.itemEntity();
            var position = conversion.position();
            var recipe = conversion.recipe();

            itemEntity.discard();

            if (conversion.remainder() > 0) {
                var remainderStack = itemEntity.getItem().copyWithCount(conversion.remainder());
                level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, remainderStack));
            }

            for (int i = 0; i < conversion.conversions(); i++) {
                for (var drop : recipe.drops()) {
                    if (random.nextDouble() > drop.chance()) {
                        continue;
                    }

                    var stack = drop.copyStack();
                    if (!stack.isEmpty()) {
                        var dropEntity = new ItemEntity(level, position.x, position.y, position.z, stack);
                        level.addFreshEntity(dropEntity);
                    }
                }
            }
        }
    }

    private record PendingConversion(ItemEntity itemEntity, Vec3 position, ExplosionRecipe recipe, int conversions, int remainder) {
    }
}
