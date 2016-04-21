package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Items.ModItems;

public class ModuleDrillHardened extends ModuleDrill
{
    public ModuleDrillHardened(MinecartModular cart)
    {
        super(cart);
    }

    public int blocksOnTop()
    {
        return 5;
    }

    public int blocksOnSide()
    {
        return 2;
    }

    protected float getTimeMult()
    {
        return 4.0F;
    }

    public int getMaxDurability()
    {
        return 1000000;
    }

    public String getRepairItemName()
    {
        return ComponentTypes.REINFORCED_METAL.getLocalizedName();
    }

    public int getRepairItemUnits(ItemStack item)
    {
        return item != null && item.getItem() == ModItems.component && item.getItemDamage() == 22 ? 450000 : 0;
    }

    public int getRepairSpeed()
    {
        return 200;
    }

    public boolean useDurability()
    {
        return true;
    }
}
