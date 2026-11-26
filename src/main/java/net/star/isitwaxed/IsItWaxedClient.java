package net.star.isitwaxed;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = IsItWaxedClient.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = IsItWaxedClient.MOD_ID, value = Dist.CLIENT)
public class IsItWaxedClient {

    public static final String MOD_ID = "isitwaxed";
    private static final DecorativeItems decorativeItems = new DecorativeItems();
    private static final Identifier OVERLAY_TEXTURE =
            Identifier.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay.png");
    private static final Identifier OVERLAY_FRAME_TEXTURE =
            Identifier.fromNamespaceAndPath(IsItWaxedClient.MOD_ID, "textures/gui/waxed_overlay_frame.png");

    public IsItWaxedClient(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.addListener(IsItWaxedClient::renderScreenEvent);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        getItems();
    }
    private static final ThreadLocal<Boolean> REENTERING_DECORATOR = ThreadLocal.withInitial(() -> false);

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            event.register(item, (extractor, font, stack, x, y) -> {
                if (REENTERING_DECORATOR.get()) {
                    return false;
                }

                if (isWaxedByName(stack)) {
                    // Flower
                    if (ClientConfig.OVERLAY_TYPE.get() == 1) {
                        extractor.pose().pushMatrix();
                        switch (ClientConfig.OVERLAY_CORNER.get()) {
                            case 1 -> extractor.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, x + 1, y + 1, 0, 0, 3, 3, 3, 3);
                            case 2 -> extractor.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, x + 12, y + 1, 0, 0, 3, 3, 3, 3);
                            case 3 -> extractor.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, x + 1, y + 12, 0, 0, 3, 3, 3, 3);
                            case 4 -> extractor.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, x + 12, y + 12, 0, 0, 3, 3, 3, 3);
                        }
                        extractor.pose().popMatrix();
                    }

                    // Outline
                    if (ClientConfig.OVERLAY_TYPE.get() == 2) {
                        extractor.fill(x, y, x + 16, y + 16, 0x80FFFF00);

                        REENTERING_DECORATOR.set(true);
                        try {
                            extractor.item(stack, x, y);
                            extractor.itemDecorations(font, stack, x, y);
                        } finally {
                            REENTERING_DECORATOR.set(false);
                        }
                    }

                    // Frame
                    if (ClientConfig.OVERLAY_TYPE.get() == 3) {
                        extractor.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_FRAME_TEXTURE, x, y, 0, 0, 16, 16, 16, 16);
                    }
                }

                return false;
            });
        }
    }

    public static void renderScreenEvent(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof ConfigurationScreen.ConfigurationSectionScreen configurationScreen) {
            if (configurationScreen.getTitle().getString().toLowerCase().contains("is it waxed")) {
                decorativeItems.renderPreviewDecorativeItems(event.getScreen(), event.getGuiGraphics());
            }
        }
    }

    private static void getItems() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (isWaxedByName(item)) {
                decorativeItems.add(item);
            }
        }
    }

    public static boolean isWaxedByName(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        String path = id.getPath();

        return path.startsWith("waxed_") || path.contains("_waxed_") || path.contains("_waxed");
    }

    public static boolean isWaxedByName(ItemStack stack) {
        return isWaxedByName(stack.getItem());
    }
}