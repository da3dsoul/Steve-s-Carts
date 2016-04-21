package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.block.BlockRailBase;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleRocket extends ModuleBase
{
    private boolean flying;
    private int landDirX;
    private int landDirZ;
    private double flyX;
    private double flyZ;
    private float yaw;
    private boolean isLanding;
    private double landY;
    private double groundY;

    public ModuleRocket(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        if (!this.isPlaceholder())
        {
            if (this.getCart().worldObj.isRemote)
            {
                if (!this.flying && this.getDw(0) != 0)
                {
                    this.takeOff();
                }
                else if (!this.isLanding && this.getDw(0) > 1)
                {
                    this.land();
                }
                else if (this.flying && this.isLanding && this.getDw(0) == 0)
                {
                    this.done();
                }
            }

            if (this.flying)
            {
                this.getCart().motionX = this.isLanding ? (double)((float)this.landDirX * 0.05F) : 0.0D;
                this.getCart().motionY = this.isLanding ? 0.0D : 0.1D;
                this.getCart().motionZ = this.isLanding ? (double)((float)this.landDirZ * 0.05F) : 0.0D;
                MinecartModular var10000;

                if (this.isLanding && this.landDirX != 0)
                {
                    var10000 = this.getCart();
                    var10000.posX += this.getCart().motionX;
                }
                else
                {
                    this.getCart().posX = this.flyX;
                }

                if (this.isLanding && this.landDirZ != 0)
                {
                    var10000 = this.getCart();
                    var10000.posZ += this.getCart().motionZ;
                }
                else
                {
                    this.getCart().posZ = this.flyZ;
                }

                this.getCart().rotationYaw = this.yaw;
                this.getCart().rotationPitch = 0.0F;

                if (this.isLanding)
                {
                    this.getCart().posY = this.landY;

                    if (BlockRailBase.func_150049_b_(this.getCart().worldObj, this.getCart().x(), this.getCart().y(), this.getCart().z()))
                    {
                        this.done();
                        this.updateDw(0, 0);
                    }
                }

                if (!this.isLanding && this.getCart().posY - this.groundY > 2.0D && BlockRailBase.func_150049_b_(this.getCart().worldObj, this.getCart().x() + this.landDirX, this.getCart().y(), this.getCart().z() + this.landDirZ))
                {
                    this.land();
                    this.updateDw(0, 2);
                }
            }
        }
    }

    public void activatedByRail(int x, int y, int z, boolean active)
    {
        if (active)
        {
            this.takeOff();
            this.updateDw(0, 1);
        }
    }

    private void takeOff()
    {
        this.flying = true;
        this.getCart().setCanUseRail(false);
        this.flyX = this.getCart().posX;
        this.flyZ = this.getCart().posZ;
        this.yaw = this.getCart().rotationYaw;
        this.groundY = this.getCart().posY;

        if (Math.abs(this.getCart().motionX) > Math.abs(this.getCart().motionZ))
        {
            this.landDirX = this.getCart().motionX > 0.0D ? 1 : -1;
        }
        else
        {
            this.landDirZ = this.getCart().motionZ > 0.0D ? 1 : -1;
        }
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    private void land()
    {
        this.isLanding = true;
        this.landY = this.getCart().posY;
        this.getCart().setCanUseRail(true);
    }

    private void done()
    {
        this.flying = false;
        this.isLanding = false;
        this.landDirX = 0;
        this.landDirZ = 0;
    }
}
