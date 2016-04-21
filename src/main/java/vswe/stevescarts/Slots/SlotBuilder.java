package vswe.stevescarts.Slots;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBuilder extends SlotBase
{
    public SlotBuilder(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return Block.getBlockFromItem(itemstack.getItem()) instanceof BlockRailBase;
    }
}
