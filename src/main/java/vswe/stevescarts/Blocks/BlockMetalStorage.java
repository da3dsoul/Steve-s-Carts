package vswe.stevescarts.Blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Items.ModItems;

public class BlockMetalStorage extends Block implements IBlockBase
{
    private String unlocalizedName;

    public BlockMetalStorage()
    {
        super(Material.iron);
        this.setCreativeTab(StevesCarts.tabsSC2Blocks);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        meta %= ModItems.storages.icons.length;
        return ModItems.storages.icons[meta];
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {}

    /**
     * Returns the unlocalized name of the block with "tile." appended to the front.
     */
    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public void setUnlocalizedName(String name)
    {
        this.unlocalizedName = name;
    }
}
