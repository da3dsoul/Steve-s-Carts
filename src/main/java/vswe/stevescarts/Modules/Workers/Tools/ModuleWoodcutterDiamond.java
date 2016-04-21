package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModuleWoodcutterDiamond extends ModuleWoodcutter
{
    public ModuleWoodcutterDiamond(MinecartModular cart)
    {
        super(cart);
    }

    public int getPercentageDropChance()
    {
        return 80;
    }

    public int getMaxDurability()
    {
        return 320000;
    }

    public String getRepairItemName()
    {
        return Localization.MODULES.TOOLS.DIAMONDS.translate(new String[0]);
    }

    public int getRepairItemUnits(ItemStack item)
    {
        return item != null && item.getItem() == Items.diamond ? 160000 : 0;
    }

    public int getRepairSpeed()
    {
        return 150;
    }
}
