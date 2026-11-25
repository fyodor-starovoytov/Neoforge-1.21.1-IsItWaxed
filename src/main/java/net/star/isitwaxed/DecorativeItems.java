package net.star.isitwaxed;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class DecorativeItems extends ArrayList<Item> {

    public ItemStack getItemStack(int index) {
        return new ItemStack(this.get(index));
    }

}
