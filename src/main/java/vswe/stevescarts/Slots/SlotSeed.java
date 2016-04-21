package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;

public class SlotSeed extends SlotBase
{
    private ModuleFarmer module;

    public SlotSeed(IInventory iinventory, ModuleFarmer module, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.module = module;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return this.module.isSeedValidHandler(itemstack);
    }
}
