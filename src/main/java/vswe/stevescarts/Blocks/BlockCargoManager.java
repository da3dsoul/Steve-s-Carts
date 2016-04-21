package vswe.stevescarts.Blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityCargo;

public class BlockCargoManager extends BlockContainerBase
{
    private IIcon topIcon;
    private IIcon botIcon;
    private IIcon redIcon;
    private IIcon blueIcon;
    private IIcon greenIcon;
    private IIcon yellowIcon;

    public BlockCargoManager()
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
        return side == 0 ? this.botIcon : (side == 1 ? this.topIcon : (side == 2 ? this.yellowIcon : (side == 3 ? this.blueIcon : (side == 4 ? this.greenIcon : this.redIcon))));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.topIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_manager").append("_top").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.botIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_manager").append("_bot").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.redIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_manager").append("_red").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.blueIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_manager").append("_blue").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.greenIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_manager").append("_green").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.yellowIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cargo_manager").append("_yellow").toString());
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        TileEntityCargo var7 = (TileEntityCargo)par1World.getTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    float var11 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = par1World.rand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14))
                    {
                        int var13 = par1World.rand.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
                        float var15 = 0.05F;
                        var14.motionX = (double)((float)par1World.rand.nextGaussian() * var15);
                        var14.motionY = (double)((float)par1World.rand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)par1World.rand.nextGaussian() * var15);

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
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
            FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 1, world, i, j, k);
            return true;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityCargo();
    }
}
