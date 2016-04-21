package vswe.stevescarts.Blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityDistributor;

public class BlockDistributor extends BlockContainerBase
{
    private IIcon purpleIcon;
    private IIcon orangeIcon;
    private IIcon redIcon;
    private IIcon blueIcon;
    private IIcon greenIcon;
    private IIcon yellowIcon;

    public BlockDistributor()
    {
        super(Material.rock);
        this.setCreativeTab(StevesCarts.tabsSC2Blocks);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return side == 0 ? this.purpleIcon : (side == 1 ? this.orangeIcon : (side == 2 ? this.yellowIcon : (side == 3 ? this.blueIcon : (side == 4 ? this.greenIcon : this.redIcon))));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.purpleIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_distributor").append("_purple").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.orangeIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_distributor").append("_orange").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.redIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_distributor").append("_red").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.blueIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_distributor").append("_blue").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.greenIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_distributor").append("_green").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.yellowIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_distributor").append("_yellow").toString());
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        if (entityplayer.isSneaking())
        {
            return false;
        }
        else if (world.isRemote)
        {
            return true;
        }
        else
        {
            FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 5, world, i, j, k);
            return true;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityDistributor();
    }
}
