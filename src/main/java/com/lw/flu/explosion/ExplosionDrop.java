package com.lw.flu.explosion;

import net.minecraft.world.item.ItemStack;

public record ExplosionDrop(ItemStack stack, double chance) {
    public ExplosionDrop {
        stack = stack.copy();
        chance = Math.clamp(chance, 0.0D, 1.0D);
    }

    public ItemStack copyStack() {
        return stack.copy();
    }
}
