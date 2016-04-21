package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSolarBasic extends ModuleSolarTop
{
    public ModuleSolarBasic(MinecartModular cart)
    {
        super(cart);
    }

    protected int getPanelCount()
    {
        return 2;
    }

    protected int getMaxCapacity()
    {
        return 100000;
    }

    protected int getGenSpeed()
    {
        return 2;
    }
}
