package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleInternalStorage extends ModuleChest
{
    public ModuleInternalStorage(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 3;
    }

    protected int getInventoryHeight()
    {
        return 3;
    }

    protected boolean hasVisualChest()
    {
        return false;
    }

    public int guiWidth()
    {
        return super.guiWidth() + 30;
    }
}
