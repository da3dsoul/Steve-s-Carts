package vswe.stevescarts.Modules.Hull;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleGalgadorian extends ModuleHull
{
    public ModuleGalgadorian(MinecartModular cart)
    {
        super(cart);
    }

    public int getConsumption(boolean isMoving)
    {
        return !isMoving ? super.getConsumption(false) : 9;
    }
}
