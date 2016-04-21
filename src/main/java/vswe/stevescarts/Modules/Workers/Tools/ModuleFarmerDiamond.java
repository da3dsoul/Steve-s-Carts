package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModuleFarmerDiamond extends ModuleFarmer
{
    public ModuleFarmerDiamond(MinecartModular cart)
    {
        super(cart);
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
        return item != null && item.getItem() == Items.diamond ? 150000 : 0;
    }

    public boolean useDurability()
    {
        return true;
    }

    public int getRepairSpeed()
    {
        return 500;
    }

    public int getRange()
    {
        return 1;
    }
}
