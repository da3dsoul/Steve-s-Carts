package vswe.stevescarts.Upgrades;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public abstract class InventoryEffect extends InterfaceEffect
{
    protected ArrayList<Slot> slots = new ArrayList();

    public Class <? extends Slot > getSlot(int id)
    {
        return Slot.class;
    }

    public void onInventoryChanged(TileEntityUpgrade upgrade) {}

    public abstract int getInventorySize();

    public abstract int getSlotX(int var1);

    public abstract int getSlotY(int var1);

    public void addSlot(Slot slot)
    {
        this.slots.add(slot);
    }

    public void clear()
    {
        this.slots.clear();
    }

    public boolean isItemValid(int slotId, ItemStack item)
    {
        return slotId >= 0 && slotId < this.slots.size() ? ((Slot)this.slots.get(slotId)).isItemValid(item) : false;
    }

    public Slot createSlot(TileEntityUpgrade upgrade, int id)
    {
        try
        {
            Class e = this.getSlot(id);
            Constructor slotConstructor = e.getConstructor(new Class[] {IInventory.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
            Object slotObject = slotConstructor.newInstance(new Object[] {upgrade, Integer.valueOf(id), Integer.valueOf(this.getSlotX(id)), Integer.valueOf(this.getSlotY(id))});
            return (Slot)slotObject;
        }
        catch (Exception var6)
        {
            System.out.println("Failed to create slot! More info below.");
            var6.printStackTrace();
            return null;
        }
    }
}
