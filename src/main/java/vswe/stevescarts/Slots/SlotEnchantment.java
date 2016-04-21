package vswe.stevescarts.Slots;

import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.EnchantmentInfo;

public class SlotEnchantment extends SlotBase
{
    private ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes;

    public SlotEnchantment(IInventory iinventory, ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.enabledTypes = enabledTypes;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return EnchantmentInfo.isItemValid(this.enabledTypes, itemstack);
    }
}
