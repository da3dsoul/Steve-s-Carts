package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabSC2 extends CreativeTabs
{
    private ItemStack item;

    public CreativeTabSC2(String label)
    {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        return this.item;
    }

    public void setIcon(ItemStack item)
    {
        this.item = item;
    }

    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return null;
    }
}
