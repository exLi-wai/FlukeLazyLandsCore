package com.lw.flu;

import com.lw.flu.explosion.ExplosionRecipeEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(FlukeLazyLandsCore.MODID)
public class FlukeLazyLandsCore {
    public static final String MODID = "flukelazylandscore";

    public FlukeLazyLandsCore(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(ExplosionRecipeEvents.class);
    }
}
