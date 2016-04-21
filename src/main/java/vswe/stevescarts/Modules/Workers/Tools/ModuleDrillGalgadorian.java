package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleDrillGalgadorian extends ModuleDrill
{
    public ModuleDrillGalgadorian(MinecartModular cart)
    {
        super(cart);
    }

    public int blocksOnTop()
    {
        return 9;
    }

    public int blocksOnSide()
    {
        return 4;
    }

    protected float getTimeMult()
    {
        return 0.0F;
    }

    public int getMaxDurability()
    {
        return 1;
    }

    public String getRepairItemName()
    {
        return null;
    }

    public int getRepairItemUnits(ItemStack item)
    {
        return 0;
    }

    public boolean useDurability()
    {
        return false;
    }

    public int getRepairSpeed()
    {
        return 1;
    }
}
