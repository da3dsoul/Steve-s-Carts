package vswe.stevescarts.Modules.Addons;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleCrafterAdv extends ModuleCrafter
{
    public ModuleCrafterAdv(MinecartModular cart)
    {
        super(cart);
    }

    protected boolean canUseAdvancedFeatures()
    {
        return true;
    }
}
