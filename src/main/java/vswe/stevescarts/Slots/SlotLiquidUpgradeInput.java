package vswe.stevescarts.Slots;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class SlotLiquidUpgradeInput extends SlotLiquidInput
{
    private TileEntityUpgrade upgrade;

    public SlotLiquidUpgradeInput(TileEntityUpgrade upgrade, Tank tank, int maxsize, int i, int j, int k)
    {
        super(upgrade, tank, maxsize, i, j, k);
        this.upgrade = upgrade;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return super.getSlotStackLimit();
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return super.isItemValid(itemstack);
    }
}
