package vswe.stevescarts.Modules.Storages.Tanks;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleFrontTank extends ModuleTank
{
    public ModuleFrontTank(MinecartModular cart)
    {
        super(cart);
    }

    protected int getTankSize()
    {
        return 8000;
    }
}
