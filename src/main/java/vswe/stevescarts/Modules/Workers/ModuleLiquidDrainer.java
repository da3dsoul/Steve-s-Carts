package vswe.stevescarts.Modules.Workers;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ModuleLiquidDrainer extends ModuleWorker
{
    public ModuleLiquidDrainer(MinecartModular cart)
    {
        super(cart);
    }

    public byte getWorkPriority()
    {
        return (byte)0;
    }

    public boolean work()
    {
        return false;
    }

    public void handleLiquid(ModuleDrill drill, int x, int y, int z)
    {
        BlockCoord here = new BlockCoord(x, y, z);
        ArrayList checked = new ArrayList();
        float result = this.drainAt(drill, checked, here, 0);

        if (result > 0 && this.doPreWork())
        {
            drill.kill();
            this.startWorking((int)(result));
        }
        else
        {
            this.stopWorking();
        }
    }

    public boolean preventAutoShutdown()
    {
        return true;
    }

    private float drainAt(ModuleDrill drill, ArrayList<BlockCoord> checked, BlockCoord here, int buckets)
    {
        float drained = 0;
        Block b = this.getCart().worldObj.getBlock(here.getX(), here.getY(), here.getZ());

        if (!this.isLiquid(b))
        {
            return 0;
        }
        else
        {
            int meta = this.getCart().worldObj.getBlockMetadata(here.getX(), here.getY(), here.getZ());
            FluidStack liquid = this.getFluidStack(b, here.getX(), here.getY(), here.getZ(), !this.doPreWork());
            int y;

            if (liquid != null)
            {
                if (this.doPreWork())
                {
                    liquid.amount += buckets * 1000;
                }

                y = this.getCart().fill(liquid, false);

                if (y == liquid.amount)
                {
                    boolean x = meta == 0;

                    if (!this.doPreWork())
                    {
                        if (x)
                        {
                            this.getCart().fill(liquid, true);
                        }

                        this.getCart().worldObj.setBlockToAir(here.getX(), here.getY(), here.getZ());
                    }

                    drained += x ? 0.25 : 0;
                    buckets += x ? 1 : 0;
                }
            }

            checked.add(here);

            if (here.getHorizontalDistToCartSquared(this.getCart()) < 400.0D)
            {
                for (y = 1; y >= 0; --y)
                {
                    for (int var13 = -1; var13 <= 1; ++var13)
                    {
                        for (int z = -1; z <= 1; ++z)
                        {
                            if (Math.abs(var13) + Math.abs(y) + Math.abs(z) == 1)
                            {
                                BlockCoord next = new BlockCoord(here.getX() + var13, here.getY() + y, here.getZ() + z);

                                if (!checked.contains(next))
                                {
                                    drained += this.drainAt(drill, checked, next, buckets);
                                }
                            }
                        }
                    }
                }
            }

            return drained;
        }
    }

    private boolean isLiquid(Block b)
    {
        boolean isWater = b == Blocks.water || b == Blocks.flowing_water || b == Blocks.ice;
        boolean isLava = b == Blocks.lava || b == Blocks.flowing_lava;
        boolean isOther = b != null && (b instanceof IFluidBlock || b instanceof BlockLiquid);
        return isWater || isLava || isOther;
    }

    private FluidStack getFluidStack(Block b, int x, int y, int z, boolean doDrain)
    {
        if (b != Blocks.water && b != Blocks.flowing_water)
        {
            if (b != Blocks.lava && b != Blocks.flowing_lava)
            {
                return drainBlock(b, getCart().worldObj, x, y, z, doDrain);
            }
            else
            {
                return new FluidStack(FluidRegistry.LAVA, 1000);
            }
        }
        else
        {
            return new FluidStack(FluidRegistry.WATER, 1000);
        }
    }

    public static FluidStack drainBlock(Block block, World world, int x, int y, int z, boolean doDrain) {
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        if(fluid != null && FluidRegistry.isFluidRegistered(fluid)) {
            int meta = world.getBlockMetadata(x, y, z);
            if(meta != 0) {
                return null;
            } else {
                if(doDrain) {
                    world.setBlockToAir(x, y, z);
                }

                return new FluidStack(fluid, 1000);
            }
        } else {
            return null;
        }
    }
}
