package vswe.stevescarts.Modules.Workers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;

public abstract class ModuleWorker extends ModuleBase
{
    private boolean preWork = true;
    private boolean shouldDie;

    public ModuleWorker(MinecartModular cart)
    {
        super(cart);
    }

    public abstract byte getWorkPriority();

    public abstract boolean work();

    protected void startWorking(int time)
    {
        this.getCart().setWorkingTime(time);
        this.preWork = false;
        this.getCart().setWorker(this);
    }

    public void stopWorking()
    {
        if (this.getCart().getWorker() == this)
        {
            this.preWork = true;
            this.getCart().setWorker((ModuleWorker)null);
        }
    }

    public boolean preventAutoShutdown()
    {
        return false;
    }

    public void kill()
    {
        this.shouldDie = true;
    }

    public boolean isDead()
    {
        return this.shouldDie;
    }

    public void revive()
    {
        this.shouldDie = false;
    }

    protected boolean doPreWork()
    {
        return this.preWork;
    }
    
    protected void setPreWork(boolean state)
    {
    	this.preWork = state;
    }

    public Vec3 getLastblock()
    {
        return this.getNextblock(false);
    }

    public Vec3 getNextblock()
    {
        return this.getNextblock(true);
    }

    private Vec3 getNextblock(boolean flag)
    {
        int i = this.getCart().x();
        int j = this.getCart().y();
        int k = this.getCart().z();

        if (BlockRailBase.func_150049_b_(this.getCart().worldObj, i, j - 1, k))
        {
            --j;
        }

        Block b = this.getCart().worldObj.getBlock(i, j, k);

        if (BlockRailBase.func_150051_a(b))
        {
            int meta = ((BlockRailBase)b).getBasicRailMetadata(this.getCart().worldObj, this.getCart(), i, j, k);

            if (meta >= 2 && meta <= 5)
            {
                ++j;
            }

            int[][] logic = MinecartModular.railDirectionCoordinates[meta];
            double pX = this.getCart().pushX;
            double pZ = this.getCart().pushZ;
            boolean xDir = pX > 0.0D && logic[0][0] > 0 || pX == 0.0D || logic[0][0] == 0 || pX < 0.0D && logic[0][0] < 0;
            boolean zDir = pZ > 0.0D && logic[0][2] > 0 || pZ == 0.0D || logic[0][2] == 0 || pZ < 0.0D && logic[0][2] < 0;
            int dir = (xDir && zDir) == flag ? 0 : 1;
            return Vec3.createVectorHelper((double)(i + logic[dir][0]), (double)(j + logic[dir][1]), (double)(k + logic[dir][2]));
        }
        else
        {
            return Vec3.createVectorHelper((double)i, (double)j, (double)k);
        }
    }

    public float getMaxSpeed()
    {
        return !this.doPreWork() ? 0.0F : super.getMaxSpeed();
    }

    protected boolean isValidForTrack(int i, int j, int k, boolean flag)
    {
        boolean result = this.countsAsAir(i, j, k) && (!flag || World.doesBlockHaveSolidTopSurface(this.getCart().worldObj, i, j - 1, k));

        if (result)
        {
            int coordX = i - (this.getCart().x() - i);
            int coordZ = k - (this.getCart().z() - k);
            Block block = this.getCart().worldObj.getBlock(coordX, j, coordZ);
            boolean isWater = block == Blocks.water || block == Blocks.flowing_water || block == Blocks.ice;
            boolean isLava = block == Blocks.lava || block == Blocks.flowing_lava;
            boolean isOther = block != null && block instanceof IFluidBlock;
            boolean isLiquid = isWater || isLava || isOther;
            result = !isLiquid;
        }

        return result;
    }
}
