package vswe.stevescarts.Listeners;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import vswe.stevescarts.Helpers.TradeHandler;
import vswe.stevescarts.Items.ModItems;

public class MobInteractListener
{
    public MobInteractListener()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        Entity target = event.target;

        if (target instanceof EntityVillager)
        {
            EntityVillager villager = (EntityVillager)target;

            if (villager.getProfession() != TradeHandler.santaId)
            {
                ItemStack item = player.getCurrentEquippedItem();

                if (item != null && item.getItem() == ModItems.component && item.getItemDamage() == 53)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        --item.stackSize;
                    }

                    if (!player.worldObj.isRemote)
                    {
                        villager.setProfession(TradeHandler.santaId);
                        MerchantRecipeList list = villager.getRecipes(player);
                        list.clear();
                        VillagerRegistry.manageVillagerTrades(list, villager, villager.getProfession(), new Random());
                    }

                    if (item.stackSize <= 0 && !player.capabilities.isCreativeMode)
                    {
                        player.destroyCurrentEquippedItem();
                    }

                    event.setCanceled(true);
                }
            }
        }
    }
}
