package vswe.stevescarts.Modules.Storages.Tanks;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSideTanks extends ModuleTank
{
    public ModuleSideTanks(MinecartModular cart)
    {
        super(cart);
    }

    protected int getTankSize()
    {
        return 8000;
    }
}
