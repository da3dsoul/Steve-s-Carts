package vswe.stevescarts.Modules.Hull;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleStandard extends ModuleHull
{
    public ModuleStandard(MinecartModular cart)
    {
        super(cart);
    }

    public int getConsumption(boolean isMoving)
    {
        return !isMoving ? super.getConsumption(false) : 1;
    }
}
