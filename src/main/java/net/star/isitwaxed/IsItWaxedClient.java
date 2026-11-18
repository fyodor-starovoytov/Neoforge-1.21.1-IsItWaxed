package net.star.isitwaxed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = IsItWaxedClient.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = IsItWaxedClient.MOD_ID, value = Dist.CLIENT)
public class IsItWaxedClient {
    public static final String MOD_ID = "isitwaxed";
    private static final ItemStack configStack = new ItemStack(Items.WAXED_COPPER_BLOCK);

    public IsItWaxedClient(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }


    private static final ResourceLocation OVERLAY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay.png");
    private static final ResourceLocation OVERLAY_FRAME_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay_frame.png");


    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            event.register(item, (guiGraphics, font, stack, x, y) -> {
                if (isWaxedByName(stack)) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    //Flower
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
                        guiGraphics.pose().popPose();
                    }

                    //Outline
                    if (ClientConfig.OVERLAY_TYPE.getAsInt() == 2) {
                        guiGraphics.pose().pushPose();

                        guiGraphics.pose().translate(x - 1.6, y - 1.6, -10);
                        guiGraphics.pose().scale(1.1f,1.1f,1.1f);
                        guiGraphics.setColor(1f, 1f, 0f, 1f);
                        guiGraphics.renderFakeItem(stack, 0, 0);
                        //Render back
                        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                        guiGraphics.pose().popPose();
                    }

                    //Frame
                    if (ClientConfig.OVERLAY_TYPE.getAsInt() == 3) {
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

    @SubscribeEvent
    public static void renderScreenEvent(ScreenEvent.Render.Post event){
        if (event.getScreen() instanceof ConfigurationScreen.ConfigurationSectionScreen configurationScreen){
            if (configurationScreen.getTitle().getString().toLowerCase().contains("is it waxed")){
                GuiGraphics guiGraphics = event.getGuiGraphics();
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(100, 100, 100);
                guiGraphics.pose().scale(1, 1, 1);

                guiGraphics.renderItem(configStack, 0,0);
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, configStack, 0, 0);
                guiGraphics.pose().popPose();
            }
        }
    }

    public static boolean isWaxedByName(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String path = id.getPath();

        return path.startsWith("waxed_") || path.contains("_waxed_") || path.contains("_waxed");
    }
}
