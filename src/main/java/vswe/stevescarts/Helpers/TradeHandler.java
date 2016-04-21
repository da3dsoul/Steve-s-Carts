package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import java.util.Random;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import vswe.stevescarts.Items.ModItems;

public class TradeHandler implements IVillageTradeHandler
{
    public static int santaId = 523;

    public TradeHandler()
    {
        VillagerRegistry.instance().registerVillagerId(santaId);
        VillagerRegistry.instance().registerVillageTradeHandler(santaId, this);
    }

    public void registerSkin()
    {
        VillagerRegistry.instance().registerVillagerSkin(santaId, ResourceHelper.getResource("/models/santa.png"));
    }

    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        recipeList.add(new MerchantRecipe(new ItemStack(ModItems.component, 3, 50), new ItemStack(ModItems.component, 1, 51)));
    }
}
