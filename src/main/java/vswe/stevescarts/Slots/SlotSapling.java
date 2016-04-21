package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutter;

public class SlotSapling extends SlotBase
{
    private ModuleWoodcutter module;

    public SlotSapling(IInventory iinventory, ModuleWoodcutter module, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.module = module;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return this.module.isSaplingHandler(itemstack);
    }
}
