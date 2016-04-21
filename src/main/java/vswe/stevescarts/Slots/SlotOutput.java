package vswe.stevescarts.Slots;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class SlotOutput extends SlotAssembler
{
    public SlotOutput(TileEntityCartAssembler assembler, int i, int j, int k)
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
        if (!this.getAssembler().getIsAssembling() && itemstack.getItem() == ModItems.carts)
        {
            NBTTagCompound info = itemstack.getTagCompound();

            if (info != null && info.hasKey("maxTime"))
            {
                return true;
            }
        }

        return false;
    }

    public boolean shouldUpdatePlaceholder()
    {
        return false;
    }
}
