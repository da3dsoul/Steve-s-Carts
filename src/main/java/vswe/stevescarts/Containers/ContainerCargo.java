package vswe.stevescarts.Containers;

import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Slots.SlotCargo;
import vswe.stevescarts.TileEntities.TileEntityCargo;

public class ContainerCargo extends ContainerManager
{
    public short lastTarget;

    public ContainerCargo(IInventory invPlayer, TileEntityCargo cargo)
    {
        super(cargo);
        cargo.cargoSlots = new ArrayList();
        cargo.lastLayout = -1;

        for (int i = 0; i < 60; ++i)
        {
            SlotCargo slot = new SlotCargo(cargo, i);
            this.addSlotToContainer(slot);
            cargo.cargoSlots.add(slot);
        }

        this.addPlayer(invPlayer);
    }

    protected int offsetX()
    {
        return 73;
    }
}
