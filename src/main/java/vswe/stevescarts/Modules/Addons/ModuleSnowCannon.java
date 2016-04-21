package vswe.stevescarts.Modules.Addons;

import net.minecraft.init.Blocks;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSnowCannon extends ModuleAddon
{
    private int tick;

    public ModuleSnowCannon(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            if (this.getCart().hasFuel())
            {
                if (this.tick >= this.getInterval())
                {
                    this.tick = 0;
                    this.generateSnow();
                }
                else
                {
                    ++this.tick;
                }
            }
        }
    }

    protected int getInterval()
    {
        return 70;
    }

    protected int getBlocksOnSide()
    {
        return 7;
    }

    protected int getBlocksFromLevel()
    {
        return 1;
    }

    private void generateSnow()
    {
        for (int x = -this.getBlocksOnSide(); x <= this.getBlocksOnSide(); ++x)
        {
            for (int z = -this.getBlocksOnSide(); z <= this.getBlocksOnSide(); ++z)
            {
                for (int y = -this.getBlocksFromLevel(); y <= this.getBlocksFromLevel(); ++y)
                {
                    int x1 = this.getCart().x() + x;
                    int y1 = this.getCart().y() + y;
                    int z1 = this.getCart().z() + z;

                    if (this.countsAsAir(x1, y1, z1) && this.getCart().worldObj.getBiomeGenForCoords(x1, z1).getFloatTemperature(x1, y1, z1) <= 1.0F && Blocks.snow.canPlaceBlockAt(this.getCart().worldObj, x1, y1, z1))
                    {
                        this.getCart().worldObj.setBlock(x1, y1, z1, Blocks.snow);
                    }
                }
            }
        }
    }
}
