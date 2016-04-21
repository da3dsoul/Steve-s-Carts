package vswe.stevescarts.Items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.StorageBlock;

public class ItemBlockStorage extends ItemBlock
{
    public static StorageBlock[] blocks;
    public IIcon[] icons;

    public static void init()
    {
        blocks = new StorageBlock[] {new StorageBlock("Reinforced Metal Block", ComponentTypes.REINFORCED_METAL.getItemStack()), new StorageBlock("Galgadorian Block", ComponentTypes.GALGADORIAN_METAL.getItemStack()), new StorageBlock("Enhanced Galgadorian Block", ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack())};
    }

    public static void loadRecipes()
    {
        for (int i = 0; i < blocks.length; ++i)
        {
            blocks[i].loadRecipe(i);
        }
    }

    public ItemBlockStorage(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(StevesCarts.tabsSC2Blocks);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int dmg)
    {
        dmg %= this.icons.length;
        return this.icons[dmg];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        this.icons = new IIcon[blocks.length];

        for (int i = 0; i < this.icons.length; ++i)
        {
            IIcon[] var10000 = this.icons;
            StringBuilder var10003 = new StringBuilder();
            StevesCarts.instance.getClass();
            var10000[i] = register.registerIcon(var10003.append("stevescarts").append(":").append(blocks[i].getName().replace(":", "").replace(" ", "_").toLowerCase()).toString());
        }
    }

    public String getName(ItemStack item)
    {
        if (item == null)
        {
            return "Unknown";
        }
        else
        {
            int dmg = item.getItemDamage();
            dmg %= blocks.length;
            return blocks[dmg].getName();
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack item)
    {
        if (item != null)
        {
            StringBuilder var10000 = (new StringBuilder()).append("item.");
            StevesCarts var10001 = StevesCarts.instance;
            return var10000.append("SC2:").append("BlockStorage").append(item.getItemDamage()).toString();
        }
        else
        {
            return "item.unknown";
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * This returns the sub items
     */
    public void getSubItems(Item item, CreativeTabs tab, List items)
    {
        for (int i = 0; i < blocks.length; ++i)
        {
            items.add(new ItemStack(item, 1, i));
        }
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int dmg)
    {
        return dmg;
    }
}
