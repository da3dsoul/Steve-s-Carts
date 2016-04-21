package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.Helpers.Tank;

public class SlotLiquidInput extends SlotBase
{
    private Tank tank;
    private int maxsize;

    public SlotLiquidInput(IInventory iinventory, Tank tank, int maxsize, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.tank = tank;
        this.maxsize = maxsize;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return this.maxsize != -1 ? this.maxsize : Math.min(8, this.tank.getCapacity() / 1000);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return FluidContainerRegistry.isEmptyContainer(itemstack) && this.tank.getFluid() != null || FluidContainerRegistry.isFilledContainer(itemstack) && (this.tank.getFluid() == null || this.tank.getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemstack)));
    }
}
