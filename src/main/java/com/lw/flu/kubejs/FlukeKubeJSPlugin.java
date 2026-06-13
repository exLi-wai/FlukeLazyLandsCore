package com.lw.flu.kubejs;

import com.lw.flu.explosion.ExplosionRecipeRegistry;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class FlukeKubeJSPlugin implements KubeJSPlugin {
    public static final EventGroup GROUP = EventGroup.of("FlukeEvents");

    public static final EventHandler EXPLOSION_RECIPES = GROUP.startup("explosionRecipes", () -> ExplosionRecipesKubeEvent.class);

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        registry.add("FlukeExplosionRecipes", ExplosionRecipeRegistry.class);
    }

    @Override
    public void afterScriptsLoaded(dev.latvian.mods.kubejs.script.ScriptManager manager) {
        if (manager.scriptType.isStartup()) {
            ExplosionRecipeRegistry.clear();
            EXPLOSION_RECIPES.post(manager.scriptType, new ExplosionRecipesKubeEvent());
        }
    }
}
