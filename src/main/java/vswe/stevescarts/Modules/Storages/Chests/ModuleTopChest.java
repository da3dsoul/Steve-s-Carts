package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleTopChest extends ModuleChest
{
    public ModuleTopChest(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 6;
    }

    protected int getInventoryHeight()
    {
        return 3;
    }
}
