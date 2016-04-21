package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleWoodcutterGalgadorian extends ModuleWoodcutter
{
    public ModuleWoodcutterGalgadorian(MinecartModular cart)
    {
        super(cart);
    }

    public int getPercentageDropChance()
    {
        return 125;
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
