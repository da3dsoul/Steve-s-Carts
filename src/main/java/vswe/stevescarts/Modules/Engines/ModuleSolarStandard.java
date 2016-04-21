package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSolarStandard extends ModuleSolarTop
{
    public ModuleSolarStandard(MinecartModular cart)
    {
        super(cart);
    }

    protected int getPanelCount()
    {
        return 4;
    }

    protected int getMaxCapacity()
    {
        return 800000;
    }

    protected int getGenSpeed()
    {
        return 5;
    }
}
