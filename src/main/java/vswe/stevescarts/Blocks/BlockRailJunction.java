package vswe.stevescarts.Blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;

public class BlockRailJunction extends BlockSpecialRailBase
{
    private IIcon normalIcon;
    private IIcon cornerIcon;

    public BlockRailJunction()
    {
        super(false);
        this.setCreativeTab(StevesCarts.tabsSC2Blocks);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return meta >= 6 ? this.cornerIcon : this.normalIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.normalIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("junction_rail").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.cornerIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("junction_rail").append("_corner").toString());
    }

    public boolean canMakeSlopes(IBlockAccess world, int i, int j, int k)
    {
        return false;
    }

    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int i, int j, int k)
    {
        if (cart instanceof MinecartModular)
        {
            MinecartModular modularCart = (MinecartModular)cart;
            int meta = modularCart.getRailMeta(i, j, k);

            if (meta != -1)
            {
                return meta;
            }
        }

        return world.getBlockMetadata(i, j, k);
    }
}
