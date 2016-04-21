package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class SlotHelmet extends SlotBase
{
    public SlotHelmet(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == 0;
    }
}
