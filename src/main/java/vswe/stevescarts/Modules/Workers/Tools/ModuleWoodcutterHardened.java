package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Items.ModItems;

public class ModuleWoodcutterHardened extends ModuleWoodcutter
{
    public ModuleWoodcutterHardened(MinecartModular cart)
    {
        super(cart);
    }

    public int getPercentageDropChance()
    {
        return 100;
    }

    public int getMaxDurability()
    {
        return 640000;
    }

    public String getRepairItemName()
    {
        return ComponentTypes.REINFORCED_METAL.getLocalizedName();
    }

    public int getRepairItemUnits(ItemStack item)
    {
        return item != null && item.getItem() == ModItems.component && item.getItemDamage() == 22 ? 320000 : 0;
    }

    public int getRepairSpeed()
    {
        return 400;
    }
}
