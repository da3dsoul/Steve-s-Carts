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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;

public class BlockUpgrade extends BlockContainerBase
{
    public BlockUpgrade()
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
        return AssemblerUpgrade.getStandardIcon();
    }

    public void registerBlockIcons(IIconRegister register) {}

    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    private int getUpgradeId(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityUpgrade)
        {
            TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
            return upgrade.getType();
        }
        else
        {
            return 0;
        }
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World world, int x, int y, int z)
    {
        return this.getUpgradeId(world, x, y, z);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {}

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return side != -1 && this.getUpgradeId(world, x, y, z) == 13;
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        ((BlockCartAssembler)ModBlocks.CART_ASSEMBLER.getBlock()).addUpgrade(world, x, y, z);
    }

    /**
     * Called when the block is attempted to be harvested
     */
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode)
        {
            world.setBlockMetadataWithNotify(x, y, z, 1, 0);
        }

        super.onBlockHarvested(world, x, y, z, meta, player);
    }

    public void breakBlock(World world, int x, int y, int z, Block id, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityUpgrade)
        {
            TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;

            if (upgrade.getUpgrade() != null)
            {
                upgrade.getUpgrade().removed(upgrade);
            }

            if (meta != 1)
            {
                this.dropBlockAsItem(world, x, y, z, new ItemStack(ModItems.upgrades, 1, this.getUpgradeId(world, x, y, z)));
            }

            if (upgrade.hasInventory())
            {
                for (int var8 = 0; var8 < upgrade.getSizeInventory(); ++var8)
                {
                    ItemStack var9 = upgrade.getStackInSlotOnClosing(var8);

                    if (var9 != null)
                    {
                        float var10 = world.rand.nextFloat() * 0.8F + 0.1F;
                        float var11 = world.rand.nextFloat() * 0.8F + 0.1F;
                        EntityItem var14;

                        for (float var12 = world.rand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; world.spawnEntityInWorld(var14))
                        {
                            int var13 = world.rand.nextInt(21) + 10;

                            if (var13 > var9.stackSize)
                            {
                                var13 = var9.stackSize;
                            }

                            var9.stackSize -= var13;
                            var14 = new EntityItem(world, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
                            float var15 = 0.05F;
                            var14.motionX = (double)((float)world.rand.nextGaussian() * var15);
                            var14.motionY = (double)((float)world.rand.nextGaussian() * var15 + 0.2F);
                            var14.motionZ = (double)((float)world.rand.nextGaussian() * var15);

                            if (var9.hasTagCompound())
                            {
                                var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                            }
                        }
                    }
                }
            }
        }

        super.breakBlock(world, x, y, z, id, meta);
        ((BlockCartAssembler)ModBlocks.CART_ASSEMBLER.getBlock()).removeUpgrade(world, x, y, z);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return !this.renderAsNormalBlock() && StevesCarts.instance.blockRenderer != null ? StevesCarts.instance.blockRenderer.getRenderId() : 0;
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        this.setUpgradeBounds(par1IBlockAccess, par2, par3, par4);
    }

    public final int setUpgradeBounds(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileEntityUpgrade)
        {
            TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
            TileEntityCartAssembler master = upgrade.getMaster();
            float margin = 0.1875F;
            float width = 0.125F;

            if (master == null)
            {
                this.setIdleBlockBounds();
                return 0;
            }

            if (master.yCoord < y)
            {
                this.setBlockBounds(margin, 0.0F, margin, 1.0F - margin, width, 1.0F - margin);
                return 0;
            }

            if (master.yCoord > y)
            {
                this.setBlockBounds(margin, 1.0F - width, margin, 1.0F - margin, 1.0F, 1.0F - margin);
                return 1;
            }

            if (master.xCoord < x)
            {
                this.setBlockBounds(0.0F, margin, margin, width, 1.0F - margin, 1.0F - margin);
                return 3;
            }

            if (master.xCoord > x)
            {
                this.setBlockBounds(1.0F - width, margin, margin, 1.0F, 1.0F - margin, 1.0F - margin);
                return 5;
            }

            if (master.zCoord < z)
            {
                this.setBlockBounds(margin, margin, 0.0F, 1.0F - margin, 1.0F - margin, width);
                return 4;
            }

            if (master.zCoord > z)
            {
                this.setBlockBounds(margin, margin, 1.0F - width, 1.0F - margin, 1.0F - margin, 1.0F);
                return 2;
            }
        }

        return -1;
    }

    public void setIdleBlockBounds()
    {
        float margin = 0.1875F;
        float width = 0.125F;
        this.setBlockBounds(margin, width, margin, 1.0F - margin, 1.0F - width, 1.0F - margin);
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
        else
        {
            TileEntity tile = world.getTileEntity(i, j, k);

            if (tile != null && tile instanceof TileEntityUpgrade)
            {
                TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;

                if (upgrade.getMaster() == null)
                {
                    return false;
                }

                if (world.isRemote)
                {
                    return true;
                }

                if (upgrade.getUpgrade().useStandardInterface())
                {
                    FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 3, world, upgrade.getMaster().xCoord, upgrade.getMaster().yCoord, upgrade.getMaster().zCoord);
                    return true;
                }

                FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 7, world, i, j, k);
            }

            return true;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityUpgrade();
    }
}
