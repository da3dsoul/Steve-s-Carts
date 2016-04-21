package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;

public class SlotIncinerator extends SlotBase implements ISpecialSlotValidator
{
    public SlotIncinerator(IInventory inv, int id, int x, int y)
    {
        super(inv, id, x, y);
    }

    public boolean isSlotValid()
    {
        return false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }
}
