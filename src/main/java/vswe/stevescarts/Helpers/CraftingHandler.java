package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Items.ModItems;

public class CraftingHandler
{
    public CraftingHandler()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event)
    {
        this.onCrafting(event.player, event.crafting, event.craftMatrix);
    }

    private void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
    {
        if (item.getItem() == ModItems.component || item.getItem() == ModItems.modules)
        {
            for (int i = 0; i < craftMatrix.getSizeInventory(); ++i)
            {
                ItemStack sItem = craftMatrix.getStackInSlot(i);

                if (sItem != null && sItem.getItem().getContainerItem() != null)
                {
                    craftMatrix.setInventorySlotContents(i, (ItemStack)null);
                }
            }
        }
    }
}
