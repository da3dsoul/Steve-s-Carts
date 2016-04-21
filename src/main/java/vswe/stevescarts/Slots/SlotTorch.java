package vswe.stevescarts.Slots;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class SlotTorch extends SlotBase
{
    public SlotTorch(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
    	Block b = Block.getBlockFromItem(itemstack.getItem());
        try {
            if(b != null) {
                if(b.getLightValue(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0), 0, 0, 0) > 0) {
                    return true;
                }
                String name = null;

                try
                {
                    name = itemstack.getUnlocalizedName() == null ? "" : ("" + StatCollector.translateToFallback(itemstack.getItem().getUnlocalizedNameInefficiently(itemstack) + ".name")).trim();
                }catch(NullPointerException e) {};

                if(name == null) return false;

                if(name.toLowerCase().contains("lamp") || name.toLowerCase().contains("light") || name.toLowerCase().contains("lantern"))
                {
                    return true;
                }
            }
        } catch (Throwable t) {}
        return (b != null && b.getLightValue() > 0);
    }
}
