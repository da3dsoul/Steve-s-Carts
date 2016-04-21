package vswe.stevescarts.Blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
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
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class BlockCartAssembler extends BlockContainerBase
{
    private IIcon topIcon;
    private IIcon botIcon;
    private IIcon[] sideIcons;

    public BlockCartAssembler()
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
        return side == 0 ? this.botIcon : (side == 1 ? this.topIcon : this.sideIcons[side - 2]);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.topIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cart_assembler").append("_top").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.botIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("cart_assembler").append("_bot").toString());
        this.sideIcons = new IIcon[4];

        for (int i = 1; i <= 4; ++i)
        {
            IIcon[] var10000 = this.sideIcons;
            int var10001 = i - 1;
            StringBuilder var10003 = new StringBuilder();
            StevesCarts.instance.getClass();
            var10000[var10001] = register.registerIcon(var10003.append("stevescarts").append(":").append("cart_assembler").append("_side_").append(i).toString());
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        if (entityplayer.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntityCartAssembler assembler = (TileEntityCartAssembler)world.getTileEntity(x, y, z);

            if (assembler != null)
            {
                if (!world.isRemote)
                {
                    FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 3, world, x, y, z);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public void updateMultiBlock(World world, int x, int y, int z)
    {
        TileEntityCartAssembler master = (TileEntityCartAssembler)world.getTileEntity(x, y, z);

        if (master != null)
        {
            master.clearUpgrades();
        }

        this.checkForUpgrades(world, x, y, z);

        if (!world.isRemote)
        {
            PacketHandler.sendBlockInfoToClients(world, new byte[0], x, y, z);
        }

        if (master != null)
        {
            master.onUpgradeUpdate();
        }
    }

    private void checkForUpgrades(World world, int x, int y, int z)
    {
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                for (int k = -1; k <= 1; ++k)
                {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) == 1)
                    {
                        this.checkForUpgrade(world, x + i, y + j, z + k);
                    }
                }
            }
        }
    }

    private TileEntityCartAssembler checkForUpgrade(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityUpgrade)
        {
            TileEntityUpgrade upgrade = (TileEntityUpgrade)tile;
            ArrayList masters = this.getMasters(world, x, y, z);

            if (masters.size() == 1)
            {
                TileEntityCartAssembler i$1 = (TileEntityCartAssembler)masters.get(0);
                i$1.addUpgrade(upgrade);
                upgrade.setMaster(i$1);
                return i$1;
            }

            Iterator i$ = masters.iterator();

            while (i$.hasNext())
            {
                TileEntityCartAssembler master = (TileEntityCartAssembler)i$.next();
                master.removeUpgrade(upgrade);
            }

            upgrade.setMaster((TileEntityCartAssembler)null);
        }

        return null;
    }

    private ArrayList<TileEntityCartAssembler> getMasters(World world, int x, int y, int z)
    {
        ArrayList masters = new ArrayList();

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                for (int k = -1; k <= 1; ++k)
                {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) == 1)
                    {
                        TileEntityCartAssembler temp = this.getMaster(world, x + i, y + j, z + k);

                        if (temp != null)
                        {
                            masters.add(temp);
                        }
                    }
                }
            }
        }

        return masters;
    }

    private TileEntityCartAssembler getValidMaster(World world, int x, int y, int z)
    {
        TileEntityCartAssembler master = null;

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                for (int k = -1; k <= 1; ++k)
                {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) == 1)
                    {
                        TileEntityCartAssembler temp = this.getMaster(world, x + i, y + j, z + k);

                        if (temp != null)
                        {
                            if (master != null)
                            {
                                return null;
                            }

                            master = temp;
                        }
                    }
                }
            }
        }

        return master;
    }

    private TileEntityCartAssembler getMaster(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityCartAssembler)
        {
            TileEntityCartAssembler master = (TileEntityCartAssembler)tile;

            if (!master.isDead)
            {
                return master;
            }
        }

        return null;
    }

    public void addUpgrade(World world, int x, int y, int z)
    {
        TileEntityCartAssembler master = this.getValidMaster(world, x, y, z);

        if (master != null)
        {
            this.updateMultiBlock(world, master.xCoord, master.yCoord, master.zCoord);
        }
    }

    public void removeUpgrade(World world, int x, int y, int z)
    {
        TileEntityCartAssembler master = this.getValidMaster(world, x, y, z);

        if (master != null)
        {
            this.updateMultiBlock(world, master.xCoord, master.yCoord, master.zCoord);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int var2)
    {
        return new TileEntityCartAssembler();
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        this.updateMultiBlock(world, x, y, z);
    }

    public void breakBlock(World world, int x, int y, int z, Block unknown1, int unknown2)
    {
        TileEntityCartAssembler var7 = (TileEntityCartAssembler)world.getTileEntity(x, y, z);
        var7.isDead = true;
        this.updateMultiBlock(world, x, y, z);

        if (var7 != null)
        {
            for (int outputItem = 0; outputItem < var7.getSizeInventory(); ++outputItem)
            {
                ItemStack eItem = var7.getStackInSlotOnClosing(outputItem);

                if (eItem != null)
                {
                    float var10 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float var11 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = world.rand.nextFloat() * 0.8F + 0.1F; eItem.stackSize > 0; world.spawnEntityInWorld(var14))
                    {
                        int var13 = world.rand.nextInt(21) + 10;

                        if (var13 > eItem.stackSize)
                        {
                            var13 = eItem.stackSize;
                        }

                        eItem.stackSize -= var13;
                        var14 = new EntityItem(world, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(eItem.getItem(), var13, eItem.getItemDamage()));
                        float var15 = 0.05F;
                        var14.motionX = (double)((float)world.rand.nextGaussian() * var15);
                        var14.motionY = (double)((float)world.rand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)world.rand.nextGaussian() * var15);

                        if (eItem.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)eItem.getTagCompound().copy());
                        }
                    }
                }
            }

            ItemStack var16 = var7.getOutputOnInterupt();

            if (var16 != null)
            {
                EntityItem var17 = new EntityItem(world, (double)x + 0.20000000298023224D, (double)y + 0.20000000298023224D, (double)((float)z + 0.2F), var16);
                var17.motionX = (double)((float)world.rand.nextGaussian() * 0.05F);
                var17.motionY = (double)((float)world.rand.nextGaussian() * 0.25F);
                var17.motionZ = (double)((float)world.rand.nextGaussian() * 0.05F);

                if (var16.hasTagCompound())
                {
                    var17.getEntityItem().setTagCompound((NBTTagCompound)var16.getTagCompound().copy());
                }

                world.spawnEntityInWorld(var17);
            }
        }

        super.breakBlock(world, x, y, z, unknown1, unknown2);
    }
}
