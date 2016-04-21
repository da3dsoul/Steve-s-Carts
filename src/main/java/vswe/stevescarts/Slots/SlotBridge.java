package vswe.stevescarts.Slots;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;

public class SlotBridge extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotBridge(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return isBridgeMaterial(itemstack);
    }

    public static boolean isBridgeMaterial(ItemStack itemstack)
    {
        Block b = Block.getBlockFromItem(itemstack.getItem());
        return b.isOpaqueCube();
    }

    public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TRANSFER_TYPE type)
    {
        return this.isItemValid(item) && type != TransferHandler.TRANSFER_TYPE.OTHER;
    }
}
