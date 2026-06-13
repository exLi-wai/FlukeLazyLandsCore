package com.lw.flu.kubejs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lw.flu.explosion.ExplosionDrop;
import com.lw.flu.explosion.ExplosionRecipe;
import com.lw.flu.explosion.ExplosionRecipeRegistry;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ExplosionRecipesKubeEvent implements KubeStartupEvent {
    public ExplosionRecipe add(Object input, Object outputs) {
        return add(null, input, outputs);
    }

    public ExplosionRecipe add(String id, Object input, Object outputs) {
        var stack = parseStack(input);
        var recipeId = id == null || id.isBlank()
                ? ExplosionRecipeRegistry.defaultId(stack)
                : ResourceLocation.parse(id);

        return ExplosionRecipeRegistry.add(recipeId, stack, parseDrops(outputs));
    }

    public ExplosionRecipe add(Map<?, ?> recipe) {
        var id = stringValue(recipe.get("id"));
        var input = firstPresent(recipe, "input", "item", "stack");
        var outputs = firstPresent(recipe, "outputs", "output", "drops");
        return add(id, input, outputs);
    }

    private static Object firstPresent(Map<?, ?> map, String... keys) {
        for (var key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }

        throw new IllegalArgumentException("Missing required key: " + String.join("/", keys));
    }

    private static List<ExplosionDrop> parseDrops(Object value) {
        var drops = new ArrayList<ExplosionDrop>();

        if (value instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                drops.add(parseDrop(element));
            }
        } else {
            drops.add(parseDrop(value));
        }

        return drops;
    }

    private static ExplosionDrop parseDrop(Object value) {
        if (value instanceof Map<?, ?> map) {
            var item = firstPresent(map, "item", "stack", "output");
            var count = intValue(map.containsKey("count") ? map.get("count") : 1);
            var chance = doubleValue(map.containsKey("chance") ? map.get("chance") : 1.0D);
            var stack = parseStack(item).copyWithCount(count);
            return new ExplosionDrop(stack, chance);
        }

        return new ExplosionDrop(parseStack(value), 1.0D);
    }

    private static ItemStack parseStack(Object value) {
        if (value instanceof ItemStack stack) {
            return stack.copy();
        }

        var string = String.valueOf(value);
        var parts = string.split(" ", 2);
        var count = 1;
        var itemId = string;

        if (parts.length == 2 && parts[0].chars().allMatch(Character::isDigit)) {
            count = Math.max(1, Integer.parseInt(parts[0]));
            itemId = parts[1];
        }

        var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
        return new ItemStack(item, count);
    }

    private static String stringValue(Object value) {
        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }

    private static int intValue(Object value) {
        if (value instanceof Number number) {
            return Math.max(1, number.intValue());
        }

        return Math.max(1, Integer.parseInt(String.valueOf(value)));
    }

    private static double doubleValue(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }

        return Double.parseDouble(String.valueOf(value));
    }
}
