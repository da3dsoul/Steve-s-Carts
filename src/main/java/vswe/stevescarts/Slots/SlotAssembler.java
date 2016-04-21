package vswe.stevescarts.Slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class SlotAssembler extends Slot
{
    private int groupID;
    private int x;
    private int y;
    private TileEntityCartAssembler assembler;
    private int openingAnimation;
    private int id;
    private boolean isValid;
    private boolean useLarge;
    private boolean reloadOnUpdate;

    public SlotAssembler(TileEntityCartAssembler assembler, int i, int j, int k, int groupID, boolean useLarge, int id)
    {
        super(assembler, i, j, k);
        this.assembler = assembler;
        this.useLarge = useLarge;
        this.groupID = groupID;
        this.x = j;
        this.y = k;
        this.isValid = true;
        this.id = id;
    }

    public boolean useLargeInterface()
    {
        return this.useLarge;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack != null && this.isValid && ModuleData.isValidModuleItem(this.groupID, itemstack) && (!this.getHasStack() || this.getStack().stackSize > 0 && itemstack.stackSize > 0);
    }

    public void invalidate()
    {
        this.isValid = false;
        this.invalidationCheck();
    }

    public void validate()
    {
        this.isValid = true;
    }

    public boolean isValid()
    {
        return this.isValid;
    }

    private void invalidationCheck()
    {
        this.xDisplayPosition = -3000;
        this.yDisplayPosition = -3000;

        if (this.openingAnimation > 8)
        {
            this.openingAnimation = 8;
        }
    }

    public void update()
    {
        if (!this.assembler.getWorldObj().isRemote)
        {
            if (!this.isValid() && this.getHasStack())
            {
                this.assembler.puke(this.getStack());
                this.putStack((ItemStack)null);
            }
        }
        else if (this.isValid())
        {
            if (this.openingAnimation == 8)
            {
                this.xDisplayPosition = this.x;
                this.yDisplayPosition = this.y;
                ++this.openingAnimation;
            }
            else if (this.openingAnimation < 8)
            {
                ++this.openingAnimation;
            }
        }
        else if (this.openingAnimation > 0)
        {
            --this.openingAnimation;
        }
        else
        {
            this.openingAnimation = this.id * -3;
        }
    }

    public int getAnimationTick()
    {
        return this.openingAnimation;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public TileEntityCartAssembler getAssembler()
    {
        return this.assembler;
    }

    public boolean shouldUpdatePlaceholder()
    {
        return true;
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged()
    {
        super.onSlotChanged();

        if (this.shouldUpdatePlaceholder())
        {
            this.assembler.updatePlaceholder();
        }
        else
        {
            this.assembler.isErrorListOutdated = true;
        }
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(EntityPlayer player)
    {
        return this.getStack() != null && this.getStack().stackSize > 0;
    }
}
