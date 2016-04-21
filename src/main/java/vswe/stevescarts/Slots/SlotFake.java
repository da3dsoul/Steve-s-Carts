package vswe.stevescarts.Slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;

public abstract class SlotFake extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotFake(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 0;
    }

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);

        if (par2ItemStack != null && par1EntityPlayer != null && par1EntityPlayer.inventory != null)
        {
            par1EntityPlayer.inventory.setItemStack((ItemStack)null);
        }
    }

    public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TRANSFER_TYPE type)
    {
        return false;
    }
}
