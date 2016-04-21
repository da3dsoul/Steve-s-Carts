package vswe.stevescarts.Slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.TileEntities.TileEntityLiquid;

public class SlotLiquidManagerInput extends SlotBase
{
    private TileEntityLiquid manager;
    private int tankid;

    public SlotLiquidManagerInput(TileEntityLiquid manager, int tankid, int i, int j, int k)
    {
        super(manager, i, j, k);
        this.manager = manager;
        this.tankid = tankid;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return isItemStackValid(itemstack, this.manager, this.tankid);
    }

    public static boolean isItemStackValid(ItemStack itemstack, TileEntityLiquid manager, int tankid)
    {
        if (tankid >= 0 && tankid < 4)
        {
            Tank tank = manager.getTanks()[tankid];
            return FluidContainerRegistry.isEmptyContainer(itemstack) && tank.getFluid() != null || FluidContainerRegistry.isFilledContainer(itemstack) && (tank.getFluid() == null || tank.getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemstack)));
        }
        else
        {
            return FluidContainerRegistry.isContainer(itemstack);
        }
    }
}
