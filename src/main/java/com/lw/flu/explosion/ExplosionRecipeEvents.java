package com.lw.flu.explosion;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
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

        for (var entity : event.getAffectedEntities()) {
            if (!(entity instanceof ItemEntity itemEntity) || !itemEntity.isAlive()) {
                continue;
            }

            var recipe = ExplosionRecipeRegistry.find(itemEntity.getItem()).orElse(null);
            if (recipe == null) {
                continue;
            }

            var inputStack = itemEntity.getItem();
            var conversions = inputStack.getCount() / recipe.inputCount();
            if (conversions <= 0) {
                continue;
            }

            var remainder = inputStack.getCount() % recipe.inputCount();
            itemEntity.discard();
            var position = itemEntity.position();

            if (remainder > 0) {
                var remainderStack = inputStack.copyWithCount(remainder);
                level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, remainderStack));
            }

            for (int i = 0; i < conversions; i++) {
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
}
