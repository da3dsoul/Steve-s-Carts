package vswe.stevescarts.Modules.Storages.Tanks;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleAdvancedTank extends ModuleTank
{
    public ModuleAdvancedTank(MinecartModular cart)
    {
        super(cart);
    }

    protected int getTankSize()
    {
        return 32000;
    }
}
