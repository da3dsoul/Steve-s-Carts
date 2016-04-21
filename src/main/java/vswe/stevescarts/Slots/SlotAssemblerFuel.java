package vswe.stevescarts.Slots;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class SlotAssemblerFuel extends SlotAssembler
{
    public SlotAssemblerFuel(TileEntityCartAssembler assembler, int i, int j, int k)
    {
        super(assembler, i, j, k, 0, true, 0);
    }

    public void validate() {}

    public void invalidate() {}

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() == Items.coal ? true : (!this.getAssembler().isCombustionFuelValid() ? false : !FluidContainerRegistry.isFilledContainer(itemstack) && TileEntityFurnace.getItemBurnTime(itemstack) > 0);
    }

    public int getFuelLevel(ItemStack itemstack)
    {
        return this.isItemValid(itemstack) ? (int)((float)TileEntityFurnace.getItemBurnTime(itemstack) * 0.25F) : 0;
    }

    public boolean shouldUpdatePlaceholder()
    {
        return false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 64;
    }
}
