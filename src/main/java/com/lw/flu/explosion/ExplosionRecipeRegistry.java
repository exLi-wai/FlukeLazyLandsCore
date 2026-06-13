package com.lw.flu.explosion;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class ExplosionRecipeRegistry {
    private static final Map<ResourceLocation, ExplosionRecipe> RECIPES = new LinkedHashMap<>();

    public static void clear() {
        RECIPES.clear();
    }

    public static ExplosionRecipe add(ResourceLocation id, ItemStack input, List<ExplosionDrop> drops) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Explosion recipe " + id + " must have a non-empty input");
        }

        if (drops.isEmpty()) {
            throw new IllegalArgumentException("Explosion recipe " + id + " must have at least one output");
        }

        var recipe = new ExplosionRecipe(id, input, drops);
        RECIPES.put(id, recipe);
        return recipe;
    }

    public static Optional<ExplosionRecipe> find(ItemStack stack) {
        return RECIPES.values().stream().filter(recipe -> recipe.matches(stack)).findFirst();
    }

    public static Collection<ExplosionRecipe> recipes() {
        return List.copyOf(RECIPES.values());
    }

    public static ResourceLocation defaultId(ItemStack input) {
        var key = BuiltInRegistries.ITEM.getKey(input.getItem());
        return ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "explosion/" + key.getPath());
    }
}
