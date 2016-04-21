package vswe.stevescarts.Modules.Addons.Plants;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ICropModule;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public class ModuleNetherwart extends ModuleAddon implements ICropModule
{
    public ModuleNetherwart(MinecartModular cart)
    {
        super(cart);
    }

    public boolean isSeedValid(ItemStack seed)
    {
        return seed.getItem() == Items.nether_wart;
    }

    public Block getCropFromSeed(ItemStack seed)
    {
        return Blocks.nether_wart;
    }

    public boolean isReadyToHarvest(int x, int y, int z)
    {
        Block b = this.getCart().worldObj.getBlock(x, y, z);
        int m = this.getCart().worldObj.getBlockMetadata(x, y, z);
        return b == Blocks.nether_wart && m == 3;
    }
}
