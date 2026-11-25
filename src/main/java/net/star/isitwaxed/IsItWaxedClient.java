package net.star.isitwaxed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
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
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = IsItWaxedClient.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = IsItWaxedClient.MOD_ID, value = Dist.CLIENT)
public class IsItWaxedClient {
    public static final String MOD_ID = "isitwaxed";
    private static final DecorativeItems decorativeItems = new DecorativeItems();
    private static final ResourceLocation OVERLAY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay.png");
    private static final ResourceLocation OVERLAY_FRAME_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay_frame.png");


    public IsItWaxedClient(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        getItems();
    }

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
                        guiGraphics.setColor(1f, 1f, 0f, 1f);
                        guiGraphics.renderFakeItem(stack, x, y);

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
    public static void renderScreenEvent(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof ConfigurationScreen.ConfigurationSectionScreen configurationScreen) {
            if (configurationScreen.getTitle().getString().toLowerCase().contains("is it waxed")) {

                Screen screen = event.getScreen();

                int x = screen.width / 4;
                int y = screen.height / 2;
                int z = 1;

                for (Item item : decorativeItems) {

                    if (y > screen.height / 8 * 7){
                        return;
                    }

                    GuiGraphics guiGraphics = event.getGuiGraphics();
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(x, y, z);

                    ItemStack toRender = new ItemStack(item);
                    guiGraphics.renderItem(toRender, 0, 0);

                    guiGraphics.renderItemDecorations(Minecraft.getInstance().font, toRender, 0, 0);
                    guiGraphics.pose().popPose();

                    x += 18;

                    if (x > screen.width / 4 * 3) {
                        x = screen.width / 4;
                        y += 18;
                    }

                }


            }
        }
    }

    private static void getItems() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (isWaxedByName(new ItemStack(item))) {
                decorativeItems.add(item);
            }
        }
    }

    public static boolean isWaxedByName(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String path = id.getPath();

        return path.startsWith("waxed_") || path.contains("_waxed_") || path.contains("_waxed");
    }
}
