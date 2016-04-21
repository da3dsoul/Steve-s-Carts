package vswe.stevescarts.Containers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Helpers.ActivatorOption;
import vswe.stevescarts.TileEntities.TileEntityActivator;
import vswe.stevescarts.TileEntities.TileEntityBase;

public class ContainerActivator extends ContainerBase
{
    private TileEntityActivator activator;
    public ArrayList<Integer> lastOptions;

    public IInventory getMyInventory()
    {
        return null;
    }

    public TileEntityBase getTileEntity()
    {
        return this.activator;
    }

    public ContainerActivator(IInventory invPlayer, TileEntityActivator activator)
    {
        this.activator = activator;
        this.lastOptions = new ArrayList();
        Iterator i$ = activator.getOptions().iterator();

        while (i$.hasNext())
        {
            ActivatorOption option = (ActivatorOption)i$.next();
            this.lastOptions.add(Integer.valueOf(option.getOption()));
        }
    }
}
