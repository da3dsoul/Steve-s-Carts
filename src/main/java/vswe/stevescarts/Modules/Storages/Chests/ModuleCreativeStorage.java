package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotChest;

public class ModuleCreativeStorage extends ModuleChest
{
    public ModuleCreativeStorage(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 9;
    }

    protected int getInventoryHeight()
    {
        return 27;
    }

    protected boolean hasVisualChest()
    {
        return false;
    }

    public int guiWidth()
    {
        return super.guiWidth() + 30;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotChest(this.getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

}
