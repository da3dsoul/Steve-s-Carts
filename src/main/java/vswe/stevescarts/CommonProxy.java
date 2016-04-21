package vswe.stevescarts;

import cpw.mods.fml.common.network.IGuiHandler;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.TileEntities.TileEntityBase;

public class CommonProxy implements IGuiHandler
{
    public void renderInit() {}

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 0)
        {
            MinecartModular tileentity = this.getCart(x, world);

            if (tileentity != null)
            {
                return tileentity.getGui(player);
            }
        }
        else
        {
            TileEntity tileentity1 = world.getTileEntity(x, y, z);

            if (tileentity1 != null && tileentity1 instanceof TileEntityBase)
            {
                return ((TileEntityBase)tileentity1).getGui(player.inventory);
            }
        }

        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 0)
        {
            MinecartModular tileentity = this.getCart(x, world);

            if (tileentity != null)
            {
                return tileentity.getCon(player.inventory);
            }
        }
        else
        {
            TileEntity tileentity1 = world.getTileEntity(x, y, z);

            if (tileentity1 != null && tileentity1 instanceof TileEntityBase)
            {
                return ((TileEntityBase)tileentity1).getContainer(player.inventory);
            }
        }

        return null;
    }

    private MinecartModular getCart(int ID, World world)
    {
        Iterator i$ = world.loadedEntityList.iterator();
        Object e;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            e = i$.next();
        }
        while (!(e instanceof Entity) || ((Entity)e).getEntityId() != ID || !(e instanceof MinecartModular));

        return (MinecartModular)e;
    }

    public World getClientWorld()
    {
        return null;
    }

    public void soundInit() {}
}
