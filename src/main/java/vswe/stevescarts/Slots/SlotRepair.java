package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;

public class SlotRepair extends SlotBase implements ISpecialItemTransferValidator
{
    private ModuleTool tool;

    public SlotRepair(ModuleTool tool, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.tool = tool;
    }

    public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TRANSFER_TYPE type)
    {
        return false;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return this.tool.isValidRepairMaterial(itemstack);
    }
}
