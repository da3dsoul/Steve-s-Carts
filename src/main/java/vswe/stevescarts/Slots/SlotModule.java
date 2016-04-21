package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Items.ModItems;

public class SlotModule extends Slot
{
    public SlotModule(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() == ModItems.modules;
    }
}
