package net.star.isitwaxed;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(IsItWaxed.MOD_ID)
public class IsItWaxed {
    public static final String MOD_ID = "isitwaxed";

   public IsItWaxed(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
