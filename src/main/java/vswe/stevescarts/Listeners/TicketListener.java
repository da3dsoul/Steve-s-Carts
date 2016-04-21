package vswe.stevescarts.Listeners;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;

public class TicketListener implements LoadingCallback
{
    public TicketListener()
    {
        ForgeChunkManager.setForcedChunkLoadingCallback(StevesCarts.instance, this);
    }

    public void ticketsLoaded(List<Ticket> tickets, World world)
    {
        Iterator i$ = tickets.iterator();

        while (i$.hasNext())
        {
            Ticket ticket = (Ticket)i$.next();
            Entity entity = ticket.getEntity();

            if (entity instanceof MinecartModular)
            {
                MinecartModular cart = (MinecartModular)entity;
                cart.loadChunks(ticket);
            }
        }
    }
}
