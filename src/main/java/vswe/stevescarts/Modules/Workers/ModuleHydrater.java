package vswe.stevescarts.Modules.Workers;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;

public class ModuleHydrater extends ModuleWorker
{
    private int range = 1;

    public ModuleHydrater(MinecartModular cart)
    {
        super(cart);
    }

    public byte getWorkPriority()
    {
        return (byte)82;
    }

    public void init()
    {
        super.init();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleFarmer)
            {
                this.range = ((ModuleFarmer)module).getExternalRange();
                break;
            }
        }
    }

    public boolean work()
    {
        Vec3 next = this.getNextblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;

        for (int i = -this.range; i <= this.range; ++i)
        {
            for (int j = -this.range; j <= this.range; ++j)
            {
                int coordX = x + i;
                int coordY = y - 1;
                int coordZ = z + j;

                if (this.hydrate(coordX, coordY, coordZ))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hydrate(int x, int y, int z)
    {
        Block b = this.getCart().worldObj.getBlock(x, y, z);
        int m = this.getCart().worldObj.getBlockMetadata(x, y, z);

        if (b == Blocks.farmland && m != 7)
        {
            int waterCost = 7 - m;
            waterCost = this.getCart().drain(FluidRegistry.WATER, waterCost, false);

            if (waterCost > 0)
            {
                if (this.doPreWork())
                {
                    this.startWorking(2 + waterCost);
                    return true;
                }

                this.stopWorking();
                this.getCart().drain(FluidRegistry.WATER, waterCost, true);
                this.getCart().worldObj.setBlockMetadataWithNotify(x, y, z, m + waterCost, 3);
            }
        }

        return false;
    }
}
