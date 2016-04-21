package vswe.stevescarts.Modules;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ICropModule
{
    boolean isSeedValid(ItemStack var1);

    Block getCropFromSeed(ItemStack var1);

    boolean isReadyToHarvest(int var1, int var2, int var3);
}
