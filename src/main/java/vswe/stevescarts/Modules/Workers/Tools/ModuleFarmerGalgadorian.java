package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleFarmerGalgadorian extends ModuleFarmer
{
    public ModuleFarmerGalgadorian(MinecartModular cart)
    {
        super(cart);
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

    public int getRange()
    {
        return 2;
    }
}
