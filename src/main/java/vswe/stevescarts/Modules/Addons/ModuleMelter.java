package vswe.stevescarts.Modules.Addons;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleMelter extends ModuleAddon
{
    private int tick;

    public ModuleMelter(MinecartModular cart)
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
                    this.melt();
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

    private void melt()
    {
        for (int x = -this.getBlocksOnSide(); x <= this.getBlocksOnSide(); ++x)
        {
            for (int z = -this.getBlocksOnSide(); z <= this.getBlocksOnSide(); ++z)
            {
                for (int y = -this.getBlocksFromLevel(); y <= this.getBlocksFromLevel(); ++y)
                {
                    Block b = this.getCart().worldObj.getBlock(x + this.getCart().x(), y + this.getCart().y(), z + this.getCart().z());
                    this.melt(b, x + this.getCart().x(), y + this.getCart().y(), z + this.getCart().z());
                }
            }
        }
    }

    protected boolean melt(Block b, int x, int y, int z)
    {
        if (b == Blocks.snow)
        {
            this.getCart().worldObj.setBlockToAir(x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }
}
