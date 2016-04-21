package vswe.stevescarts.Containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.TileEntities.TileEntityBase;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.InventoryEffect;

public class ContainerUpgrade extends ContainerBase
{
    private TileEntityUpgrade upgrade;
    public Object olddata;

    public IInventory getMyInventory()
    {
        return this.upgrade;
    }

    public TileEntityBase getTileEntity()
    {
        return this.upgrade;
    }

    public ContainerUpgrade(IInventory invPlayer, TileEntityUpgrade upgrade)
    {
        this.upgrade = upgrade;

        if (upgrade.getUpgrade() != null && upgrade.getUpgrade().getInventoryEffect() != null)
        {
            InventoryEffect inventory = upgrade.getUpgrade().getInventoryEffect();
            inventory.clear();
            int j;

            for (j = 0; j < inventory.getInventorySize(); ++j)
            {
                Slot k = inventory.createSlot(upgrade, j);
                this.addSlotToContainer(k);
                inventory.addSlot(k);
            }

            for (j = 0; j < 3; ++j)
            {
                for (int var6 = 0; var6 < 9; ++var6)
                {
                    this.addSlotToContainer(new Slot(invPlayer, var6 + j * 9 + 9, this.offsetX() + var6 * 18, j * 18 + this.offsetY()));
                }
            }

            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j, this.offsetX() + j * 18, 58 + this.offsetY()));
            }
        }
    }

    protected int offsetX()
    {
        return 48;
    }

    protected int offsetY()
    {
        return 108;
    }
}
