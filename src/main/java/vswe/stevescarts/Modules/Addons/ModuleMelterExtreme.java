package vswe.stevescarts.Modules.Addons;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleMelterExtreme extends ModuleMelter
{
    public ModuleMelterExtreme(MinecartModular cart)
    {
        super(cart);
    }

    protected boolean melt(Block b, int x, int y, int z)
    {
        if (!super.melt(b, x, y, z))
        {
            if (b == Blocks.snow)
            {
                this.getCart().worldObj.setBlockToAir(x, y, z);
                return true;
            }

            if (b == Blocks.ice)
            {
                this.getCart().worldObj.setBlock(x, y, z, Blocks.water);
                return true;
            }
        }

        return false;
    }
}
