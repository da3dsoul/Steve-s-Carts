package vswe.stevescarts.Containers;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Slots.SlotLiquidFilter;
import vswe.stevescarts.Slots.SlotLiquidManagerInput;
import vswe.stevescarts.Slots.SlotLiquidOutput;
import vswe.stevescarts.TileEntities.TileEntityLiquid;

public class ContainerLiquid extends ContainerManager
{
    public FluidStack[] oldLiquids = new FluidStack[4];

    public ContainerLiquid(IInventory invPlayer, TileEntityLiquid liquid)
    {
        super(liquid);

        for (int i = 0; i < 4; ++i)
        {
            int x = i % 2;
            int y = i / 2;
            this.addSlotToContainer(new SlotLiquidManagerInput(liquid, i, i * 3, x == 0 ? 6 : 208, y == 0 ? 17 : 80));
            this.addSlotToContainer(new SlotLiquidOutput(liquid, i * 3 + 1, x == 0 ? 6 : 208, y == 0 ? 42 : 105));
            this.addSlotToContainer(new SlotLiquidFilter(liquid, i * 3 + 2, x == 0 ? 66 : 148, y == 0 ? 12 : 110));
        }

        this.addPlayer(invPlayer);
    }

    protected int offsetX()
    {
        return 35;
    }
}
