package vswe.stevescarts.Slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;

public class SlotCartCrafterResult extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotCartCrafterResult(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

    public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TRANSFER_TYPE type)
    {
        return false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 0;
    }
}
