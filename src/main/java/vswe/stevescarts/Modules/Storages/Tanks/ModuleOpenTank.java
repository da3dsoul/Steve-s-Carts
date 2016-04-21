package vswe.stevescarts.Modules.Storages.Tanks;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleOpenTank extends ModuleTank
{
    int cooldown = 0;

    public ModuleOpenTank(MinecartModular cart)
    {
        super(cart);
    }

    protected int getTankSize()
    {
        return 7000;
    }

    public void update()
    {
        super.update();

        if (this.cooldown > 0)
        {
            --this.cooldown;
        }
        else
        {
            this.cooldown = 20;

            if (this.getCart().worldObj.isRaining() && this.getCart().worldObj.canBlockSeeTheSky(this.getCart().x(), this.getCart().y() + 1, this.getCart().z()) && this.getCart().worldObj.getPrecipitationHeight(this.getCart().x(), this.getCart().z()) < this.getCart().y() + 1)
            {
                this.fill(new FluidStack(FluidRegistry.WATER, this.getCart().worldObj.getBiomeGenForCoords(this.getCart().x(), this.getCart().z()).getEnableSnow() ? 2 : 5), true);
            }
        }
    }
}
