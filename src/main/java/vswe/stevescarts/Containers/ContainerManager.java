package vswe.stevescarts.Containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.TileEntities.TileEntityBase;
import vswe.stevescarts.TileEntities.TileEntityManager;

public abstract class ContainerManager extends ContainerBase
{
    private TileEntityManager manager;
    public short lastHeader;
    public short lastColor;
    public short lastAmount;

    public IInventory getMyInventory()
    {
        return this.manager;
    }

    public TileEntityBase getTileEntity()
    {
        return this.manager;
    }

    public ContainerManager(TileEntityManager manager)
    {
        this.manager = manager;
    }

    protected void addPlayer(IInventory invPlayer)
    {
        int l;

        for (l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(invPlayer, j1 + l * 9 + 9, j1 * 18 + this.offsetX(), 104 + l * 18 + 36));
            }
        }

        for (l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(invPlayer, l, l * 18 + this.offsetX(), 198));
        }
    }

    protected abstract int offsetX();
}
