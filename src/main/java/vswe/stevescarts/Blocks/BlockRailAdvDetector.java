package vswe.stevescarts.Blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.TileEntities.TileEntityActivator;
import vswe.stevescarts.TileEntities.TileEntityDetector;
import vswe.stevescarts.TileEntities.TileEntityManager;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;
import vswe.stevescarts.Upgrades.Disassemble;
import vswe.stevescarts.Upgrades.Transposer;

public class BlockRailAdvDetector extends BlockSpecialRailBase
{
    private IIcon normalIcon;
    private IIcon cornerIcon;

    public BlockRailAdvDetector()
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
        this.normalIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("advanced_detector_rail").toString());
        var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.cornerIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("advanced_detector_rail").append("_corner").toString());
    }

    public boolean canMakeSlopes(IBlockAccess world, int i, int j, int k)
    {
        return false;
    }

    public void onMinecartPass(World world, EntityMinecart Minecart, int x, int y, int z)
    {
        if (!world.isRemote && Minecart instanceof MinecartModular)
        {
            MinecartModular cart = (MinecartModular)Minecart;

            if (world.getBlock(x, y - 1, z) == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y - 1, z)).canInteractWithCart())
            {
                TileEntity var23 = world.getTileEntity(x, y - 1, z);

                if (var23 != null && var23 instanceof TileEntityDetector)
                {
                    TileEntityDetector var25 = (TileEntityDetector)var23;
                    var25.handleCart(cart);
                }
            }
            else if (this.isCartReadyForAction(cart, x, y, z))
            {
                int side = 0;

                for (int receivesPower = -1; receivesPower <= 1; ++receivesPower)
                {
                    for (int j = -1; j <= 1; ++j)
                    {
                        if (Math.abs(receivesPower) != Math.abs(j))
                        {
                            Block block = world.getBlock(x + receivesPower, y, z + j);
                            TileEntity tileentity;

                            if (block == ModBlocks.CARGO_MANAGER.getBlock() || block == ModBlocks.LIQUID_MANAGER.getBlock())
                            {
                                tileentity = world.getTileEntity(x + receivesPower, y, z + j);

                                if (tileentity != null && tileentity instanceof TileEntityManager)
                                {
                                    TileEntityManager var27 = (TileEntityManager)tileentity;

                                    if (var27.getCart() == null)
                                    {
                                        var27.setCart(cart);
                                        var27.setSide(side);
                                    }
                                }

                                return;
                            }

                            if (block == ModBlocks.MODULE_TOGGLER.getBlock())
                            {
                                tileentity = world.getTileEntity(x + receivesPower, y, z + j);

                                if (tileentity != null && tileentity instanceof TileEntityActivator)
                                {
                                    TileEntityActivator upgrade = (TileEntityActivator)tileentity;
                                    boolean i$ = false;

                                    if (cart.temppushX == 0.0D == (cart.temppushZ == 0.0D))
                                    {
                                        continue;
                                    }

                                    if (receivesPower == 0)
                                    {
                                        if (j == -1)
                                        {
                                            i$ = cart.temppushX < 0.0D;
                                        }
                                        else
                                        {
                                            i$ = cart.temppushX > 0.0D;
                                        }
                                    }
                                    else if (j == 0)
                                    {
                                        if (receivesPower == -1)
                                        {
                                            i$ = cart.temppushZ > 0.0D;
                                        }
                                        else
                                        {
                                            i$ = cart.temppushZ < 0.0D;
                                        }
                                    }

                                    boolean effect = false;
                                    upgrade.handleCart(cart, i$);
                                    cart.releaseCart();
                                }

                                return;
                            }
                            else
                            {
                                if (block == ModBlocks.UPGRADE.getBlock())
                                {
                                    tileentity = world.getTileEntity(x + receivesPower, y, z + j);
                                    TileEntityUpgrade var26 = (TileEntityUpgrade)tileentity;

                                    if (var26 != null && var26.getUpgrade() != null)
                                    {
                                        Iterator var28 = var26.getUpgrade().getEffects().iterator();

                                        while (var28.hasNext())
                                        {
                                            BaseEffect var29 = (BaseEffect)var28.next();

                                            if (var29 instanceof Transposer)
                                            {
                                                Transposer transposer = (Transposer)var29;

                                                if (var26.getMaster() != null)
                                                {
                                                    Iterator i$1 = var26.getMaster().getUpgradeTiles().iterator();

                                                    while (i$1.hasNext())
                                                    {
                                                        TileEntityUpgrade tile = (TileEntityUpgrade)i$1.next();

                                                        if (tile.getUpgrade() != null)
                                                        {
                                                            Iterator i$2 = tile.getUpgrade().getEffects().iterator();

                                                            while (i$2.hasNext())
                                                            {
                                                                BaseEffect effect2 = (BaseEffect)i$2.next();

                                                                if (effect2 instanceof Disassemble)
                                                                {
                                                                    Disassemble disassembler = (Disassemble)effect2;

                                                                    if (tile.getStackInSlot(0) == null)
                                                                    {
                                                                        tile.setInventorySlotContents(0, ModuleData.createModularCart(cart));
                                                                        var26.getMaster().managerInteract(cart, false);

                                                                        for (int p = 0; p < cart.getSizeInventory(); ++p)
                                                                        {
                                                                            ItemStack item = cart.getStackInSlotOnClosing(p);

                                                                            if (item != null)
                                                                            {
                                                                                var26.getMaster().puke(item);
                                                                            }
                                                                        }

                                                                        cart.setDead();
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                ++side;
                            }
                        }
                    }
                }

                boolean var24 = world.isBlockIndirectlyGettingPowered(x, y, z);

                if (var24)
                {
                    cart.releaseCart();
                }
            }
        }
    }

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlock(x, y - 1, z) == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromMeta(world.getBlockMetadata(x, y - 1, z)).canInteractWithCart())
        {
            return false;
        }
        else
        {
            for (int i = -1; i <= 1; ++i)
            {
                for (int j = -1; j <= 1; ++j)
                {
                    if (Math.abs(i) != Math.abs(j))
                    {
                        Block block = world.getBlock(x + i, y, z + j);

                        if (block == ModBlocks.CARGO_MANAGER.getBlock() || block == ModBlocks.LIQUID_MANAGER.getBlock() || block == ModBlocks.MODULE_TOGGLER.getBlock())
                        {
                            return false;
                        }

                        if (block == ModBlocks.UPGRADE.getBlock())
                        {
                            TileEntity tileentity = world.getTileEntity(x + i, y, z + j);
                            TileEntityUpgrade upgrade = (TileEntityUpgrade)tileentity;

                            if (upgrade != null && upgrade.getUpgrade() != null)
                            {
                                Iterator i$ = upgrade.getUpgrade().getEffects().iterator();

                                while (i$.hasNext())
                                {
                                    BaseEffect effect = (BaseEffect)i$.next();

                                    if (effect instanceof Transposer && upgrade.getMaster() != null)
                                    {
                                        Iterator i$1 = upgrade.getMaster().getUpgradeTiles().iterator();

                                        while (i$1.hasNext())
                                        {
                                            TileEntityUpgrade tile = (TileEntityUpgrade)i$1.next();

                                            if (tile.getUpgrade() != null)
                                            {
                                                Iterator i$2 = tile.getUpgrade().getEffects().iterator();

                                                while (i$2.hasNext())
                                                {
                                                    BaseEffect effect2 = (BaseEffect)i$2.next();

                                                    if (effect2 instanceof Disassemble)
                                                    {
                                                        return false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean isCartReadyForAction(MinecartModular cart, int x, int y, int z)
    {
        return cart.disabledX != x && cart.disabledY != y && cart.disabledZ != z ? false : cart.isDisabled();
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        return world.getBlock(i, j - 1, k) == ModBlocks.DETECTOR_UNIT.getBlock() ? ModBlocks.DETECTOR_UNIT.getBlock().onBlockActivated(world, i, j - 1, k, entityplayer, par6, par7, par8, par9) : false;
    }

    public void refreshState(World world, int x, int y, int z, boolean flag)
    {
        (new BlockRailBase.Rail(world, x, y, z)).func_150655_a(flag, false);
    }
}
