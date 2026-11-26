package net.star.isitwaxed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(IsItWaxedClient.MOD_ID)
@Mod.EventBusSubscriber(modid = IsItWaxedClient.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IsItWaxedClient {
    public static final String MOD_ID = "isitwaxed";
    private static final ResourceLocation OVERLAY_TEXTURE =
            new ResourceLocation(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay.png");
    private static final ResourceLocation OVERLAY_FRAME_TEXTURE =
            new ResourceLocation(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay_frame.png");

    public IsItWaxedClient() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {}

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            event.register(item, (guiGraphics, font, stack, x, y) -> {
                if (isWaxedByName(stack)) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    // Flower
                    if (ClientConfig.OVERLAY_TYPE.get() == 1) {
                        guiGraphics.pose().pushPose();
                        guiGraphics.pose().translate(0, 0, 200.0F);
                        switch (ClientConfig.OVERLAY_CORNER.get()) {
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
                        guiGraphics.pose().popPose();
                    }

                    // Outline
                    if (ClientConfig.OVERLAY_TYPE.get() == 2) {
                        guiGraphics.pose().pushPose();
                        guiGraphics.setColor(1f, 1f, 0f, 1f);
                        guiGraphics.renderFakeItem(stack, x, y);
                        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                        guiGraphics.pose().popPose();
                    }

                    // Frame
                    if (ClientConfig.OVERLAY_TYPE.get() == 3) {
                        guiGraphics.pose().pushPose();
                        guiGraphics.blit(OVERLAY_FRAME_TEXTURE, x, y, 0, 0, 16, 16, 16, 16);
                        guiGraphics.pose().popPose();
                    }

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