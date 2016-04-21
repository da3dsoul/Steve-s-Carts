package vswe.stevescarts.Modules.Hull;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleReinforced extends ModuleHull
{
    public ModuleReinforced(MinecartModular cart)
    {
        super(cart);
    }

    public int getConsumption(boolean isMoving)
    {
        return !isMoving ? super.getConsumption(false) : 3;
    }
}
