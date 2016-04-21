package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.Modules.Engines.ModuleCoalBase;

public class SlotFuel extends SlotBase
{
    public SlotFuel(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return this.getItemBurnTime(itemstack) > 0;
    }

    private boolean isValid(ItemStack itemstack)
    {
        return !FluidContainerRegistry.isFilledContainer(itemstack);
    }

    private int getItemBurnTime(ItemStack itemstack)
    {
        return this.isValid(itemstack) ? TileEntityFurnace.getItemBurnTime(itemstack) : 0;
    }

    public static int getItemBurnTime(ModuleCoalBase engine, ItemStack itemstack)
    {
        return (int)((double)TileEntityFurnace.getItemBurnTime(itemstack) * engine.getFuelMultiplier());
    }
}
