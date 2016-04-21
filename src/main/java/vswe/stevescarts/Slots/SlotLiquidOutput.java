package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.Helpers.TransferHandler;

public class SlotLiquidOutput extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotLiquidOutput(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return isItemStackValid(itemstack);
    }

    public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TRANSFER_TYPE type)
    {
        return type == TransferHandler.TRANSFER_TYPE.OTHER && FluidContainerRegistry.isContainer(item);
    }

    public static boolean isItemStackValid(ItemStack itemstack)
    {
        return false;
    }
}
