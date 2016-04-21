package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleThermalAdvanced extends ModuleThermalBase
{
    public ModuleThermalAdvanced(MinecartModular cart)
    {
        super(cart);
    }

    protected int getEfficiency()
    {
        return 60;
    }

    protected int getCoolantEfficiency()
    {
        return 90;
    }
}
