package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModuleDrillDiamond extends ModuleDrill
{
    public ModuleDrillDiamond(MinecartModular cart)
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
        return 8.0F;
    }

    public int getMaxDurability()
    {
        return 300000;
    }

    public String getRepairItemName()
    {
        return Localization.MODULES.TOOLS.DIAMONDS.translate(new String[0]);
    }

    public int getRepairItemUnits(ItemStack item)
    {
        return item != null && item.getItem() == Items.diamond ? 100000 : 0;
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
