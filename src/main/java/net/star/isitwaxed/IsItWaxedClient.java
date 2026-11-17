package net.star.isitwaxed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = IsItWaxedClient.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = IsItWaxedClient.MOD_ID, value = Dist.CLIENT)
public class IsItWaxedClient {
    public static final String MOD_ID = "isitwaxed";

    public IsItWaxedClient(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }


    private static final ResourceLocation OVERLAY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay.png");

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            event.register(item, (guiGraphics, font, stack, x, y) -> {
                if (isWaxedByName(stack)) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    if (ClientConfig.OVERLAY_TYPE.getAsInt() == 1) {
                        guiGraphics.pose().pushPose();
                        guiGraphics.pose().translate(0, 0, 200.0F);
                        switch (ClientConfig.OVERLAY_CORNER.getAsInt()) {
                            case 1:
                                guiGraphics.blit(OVERLAY_TEXTURE, x + 1, y + 1, 0, 0, 3, 3, 3, 3);
                                break;
                            case 2:
                                guiGraphics.blit(OVERLAY_TEXTURE, x + 12, y + 1, 0, 0, 3, 3, 3, 3);
                                break;
                            case 3:
                                guiGraphics.blit(OVERLAY_TEXTURE, x + 1, y + 12, 0, 0, 3, 3, 3, 3);
                                break;
                            case 4:
                                guiGraphics.blit(OVERLAY_TEXTURE, x + 12, y + 12, 0, 0, 3, 3, 3, 3);
                                break;
                        }
                    }

                    if (ClientConfig.OVERLAY_TYPE.getAsInt() == 2) {
                        guiGraphics.pose().pushPose();
                        guiGraphics.pose().translate(1.6f - x, 1.6f, 0);
                        guiGraphics.pose().scale(1.1f,1.1f,1.1f);
                        guiGraphics.setColor(1f, 1f, 0f, 1f);
                        guiGraphics.renderFakeItem(stack, x, y);

                        //Render back
                        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                    }

                    guiGraphics.pose().popPose();
                    RenderSystem.disableBlend();
                }

                return false;
            });
        }
    }

    public static boolean isWaxedByName(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String path = id.getPath();

        return path.startsWith("waxed_") || path.contains("_waxed_") || path.contains("_waxed");
    }
}
