package net.star.isitwaxed;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class DecorativeItems extends ArrayList<Item> {

    public ItemStack getItemStack(int index) {
        return new ItemStack(this.get(index));
    }

    public void renderPreviewDecorativeItems(Screen screen, GuiGraphics guiGraphics){

        int x = screen.width / 4;
        int y = screen.height / 3;
        int z = 1;

        for (Item item : this) {

            if (y > screen.height / 8 * 7){
                return;
            }

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
