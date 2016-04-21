package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSideChests extends ModuleChest
{
    public ModuleSideChests(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 5;
    }

    protected int getInventoryHeight()
    {
        return 3;
    }
}
