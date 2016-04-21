package vswe.stevescarts.Containers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Helpers.DistributorSide;
import vswe.stevescarts.TileEntities.TileEntityBase;
import vswe.stevescarts.TileEntities.TileEntityDistributor;

public class ContainerDistributor extends ContainerBase
{
    private TileEntityDistributor distributor;
    public ArrayList<Short> cachedValues;

    public IInventory getMyInventory()
    {
        return null;
    }

    public TileEntityBase getTileEntity()
    {
        return this.distributor;
    }

    public ContainerDistributor(IInventory invPlayer, TileEntityDistributor distributor)
    {
        this.distributor = distributor;
        this.cachedValues = new ArrayList();
        Iterator i$ = distributor.getSides().iterator();

        while (i$.hasNext())
        {
            DistributorSide side = (DistributorSide)i$.next();
            this.cachedValues.add(Short.valueOf((short)0));
            this.cachedValues.add(Short.valueOf((short)0));
        }
    }
}
