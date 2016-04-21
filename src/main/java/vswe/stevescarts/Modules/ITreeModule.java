package vswe.stevescarts.Modules;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ITreeModule
{
    boolean isLeaves(Block var1, int var2, int var3, int var4);

    boolean isWood(Block var1, int var2, int var3, int var4);

    boolean isSapling(ItemStack var1);
}
