package vswe.stevescarts.Listeners;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Items.ModItems;

public class PlayerSleepListener
{
    public PlayerSleepListener()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void tickEnd(PlayerTickEvent event)
    {
        if (event.side == Side.SERVER)
        {
            EntityPlayer player = event.player;

            if (StevesCarts.isChristmas && player.isPlayerFullyAsleep())
            {
                for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
                {
                    ItemStack item = player.inventory.getStackInSlot(i);

                    if (item != null && item.getItem() == ModItems.component && item.getItemDamage() == 56)
                    {
                        item.setItemDamage(item.getItemDamage() + 1);
                    }
                }
            }
        }
    }
}
