package vswe.stevescarts.Slots;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;

public interface ISpecialItemTransferValidator
{
    boolean isItemValidForTransfer(ItemStack var1, TransferHandler.TRANSFER_TYPE var2);
}
