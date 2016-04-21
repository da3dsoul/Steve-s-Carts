package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotBase extends Slot
{
    private int x;
    private int y;

    public SlotBase(IInventory inventory, int i, int j, int k)
    {
        super(inventory, i, j, k);
        this.x = j;
        this.y = k;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public boolean containsValidItem()
    {
        return this.getStack() != null && this.isItemValid(this.getStack());
    }
}
