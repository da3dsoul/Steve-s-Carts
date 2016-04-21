package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModuleDrillIron extends ModuleDrill
{
    public ModuleDrillIron(MinecartModular cart)
    {
        super(cart);
    }

    public int blocksOnTop()
    {
        return 3;
    }

    public int blocksOnSide()
    {
        return 1;
    }

    protected float getTimeMult()
    {
        return 40.0F;
    }

    public int getMaxDurability()
    {
        return 50000;
    }

    public String getRepairItemName()
    {
        return Localization.MODULES.TOOLS.IRON.translate(new String[0]);
    }

    public int getRepairItemUnits(ItemStack item)
    {
        return item != null && item.getItem() == Items.iron_ingot ? 20000 : 0;
    }

    public int getRepairSpeed()
    {
        return 50;
    }

    public boolean useDurability()
    {
        return true;
    }
}
