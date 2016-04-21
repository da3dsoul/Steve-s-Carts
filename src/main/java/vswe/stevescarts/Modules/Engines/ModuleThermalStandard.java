package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleThermalStandard extends ModuleThermalBase
{
    public ModuleThermalStandard(MinecartModular cart)
    {
        super(cart);
    }

    protected int getEfficiency()
    {
        return 25;
    }

    protected int getCoolantEfficiency()
    {
        return 0;
    }
}
