package vswe.stevescarts.Slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotFertilizer extends SlotBase
{
    public SlotFertilizer(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() == Items.bone || itemstack.getItem() == Items.dye && itemstack.getItemDamage() == 15;
    }
}
