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
import vswe.stevescarts.TileEntities.TileEntityActivator;

public class BlockActivator extends BlockContainerBase
{
    private IIcon topIcon;
    private IIcon botIcon;
    private IIcon sideIcon;

    public BlockActivator()
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
        return side == 0 ? this.botIcon : (side == 1 ? this.topIcon : this.sideIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.topIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("module_toggler").append("_top").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.botIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("module_toggler").append("_bot").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.sideIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("module_toggler").append("_side").toString());
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
            FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 4, world, i, j, k);
            return true;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityActivator();
    }
}
