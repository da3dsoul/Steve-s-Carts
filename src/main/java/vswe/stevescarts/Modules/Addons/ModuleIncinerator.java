package vswe.stevescarts.Modules.Addons;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotIncinerator;

public class ModuleIncinerator extends ModuleAddon
{
    public ModuleIncinerator(MinecartModular cart)
    {
        super(cart);
    }

    public void incinerate(ItemStack item)
    {
        if (this.isItemValid(item))
        {
            if (this.getIncinerationCost() != 0)
            {
                int amount = item.stackSize * this.getIncinerationCost();
                amount = this.getCart().drain(FluidRegistry.LAVA, amount, false);
                int incinerated = amount / this.getIncinerationCost();
                this.getCart().drain(FluidRegistry.LAVA, incinerated * this.getIncinerationCost(), true);
                item.stackSize -= incinerated;
            }
            else
            {
                item.stackSize = 0;
            }
        }
    }

    protected int getIncinerationCost()
    {
        return 3;
    }

    protected boolean isItemValid(ItemStack item)
    {
        if (item != null)
        {
            for (int i = 0; i < this.getInventorySize(); ++i)
            {
                if (this.getStack(i) != null && item.isItemEqual(this.getStack(i)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasGui()
    {
        return true;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    protected int getInventoryWidth()
    {
        return 4;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotIncinerator(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }
}
