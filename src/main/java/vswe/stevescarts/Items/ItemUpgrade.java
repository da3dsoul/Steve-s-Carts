package vswe.stevescarts.Items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;

public class ItemUpgrade extends ItemBlock
{
    public ItemUpgrade(Block block)
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
        AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(dmg);
        return upgrade != null ? upgrade.getIcon() : null;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        Iterator i$ = AssemblerUpgrade.getUpgradesList().iterator();

        while (i$.hasNext())
        {
            AssemblerUpgrade upgrade = (AssemblerUpgrade)i$.next();
            upgrade.createIcon(register);
        }

        AssemblerUpgrade.initSides(register);
    }

    public String getName(ItemStack item)
    {
        AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(item.getItemDamage());
        return upgrade != null ? upgrade.getName() : "Unknown";
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack item)
    {
        AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(item.getItemDamage());
        return upgrade != null ? "item.SC2:" + upgrade.getRawName() : "item.unknown";
    }

    @SideOnly(Side.CLIENT)

    /**
     * This returns the sub items
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        Iterator i$ = AssemblerUpgrade.getUpgradesList().iterator();

        while (i$.hasNext())
        {
            AssemblerUpgrade upgrade = (AssemblerUpgrade)i$.next();
            ItemStack iStack = new ItemStack(par1, 1, upgrade.getId());
            par3List.add(iStack);
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
        {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile != null && tile instanceof TileEntityUpgrade)
            {
                TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
                upgrade.setType(stack.getItemDamage());
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        AssemblerUpgrade upgrade = AssemblerUpgrade.getUpgrade(par1ItemStack.getItemDamage());

        if (upgrade != null)
        {
            Iterator i$ = upgrade.getEffects().iterator();

            while (i$.hasNext())
            {
                BaseEffect effect = (BaseEffect)i$.next();
                par3List.add(effect.getName());
            }
        }
    }
}
