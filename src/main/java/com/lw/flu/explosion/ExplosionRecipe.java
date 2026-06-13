package com.lw.flu.explosion;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ExplosionRecipe(ResourceLocation id, ItemStack input, List<ExplosionDrop> drops) {
    public ExplosionRecipe {
        input = input.copy();
        drops = List.copyOf(drops);
    }

    public boolean matches(ItemStack stack) {
        return ItemStack.isSameItemSameComponents(input, stack);
    }

    public int inputCount() {
        return Math.max(1, input.getCount());
    }

    public ItemStack inputStack() {
        return input.copy();
    }

    public ResourceLocation inputId() {
        return BuiltInRegistries.ITEM.getKey(input.getItem());
    }
}
