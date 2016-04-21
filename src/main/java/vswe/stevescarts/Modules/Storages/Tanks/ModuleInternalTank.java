package vswe.stevescarts.Modules.Storages.Tanks;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleInternalTank extends ModuleTank
{
    public ModuleInternalTank(MinecartModular cart)
    {
        super(cart);
    }

    protected int getTankSize()
    {
        return 4000;
    }

    public boolean hasVisualTank()
    {
        return false;
    }
}
