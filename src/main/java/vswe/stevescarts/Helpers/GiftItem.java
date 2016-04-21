package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.ModuleData.ModuleData;

public class GiftItem
{
    private int chanceWeight;
    private int costPerItem;
    private ItemStack item;
    private boolean fixedSize;
    public static ArrayList<GiftItem> ChristmasList;
    public static ArrayList<GiftItem> EasterList;

    public GiftItem(ItemStack item, int costPerItem, int chanceWeight)
    {
        this.item = item;
        this.chanceWeight = chanceWeight;
        this.costPerItem = costPerItem;
    }

    public GiftItem(Block block, int costPerItem, int chanceWeight)
    {
        this(new ItemStack(block, 1), costPerItem, chanceWeight);
    }

    public GiftItem(Item item, int costPerItem, int chanceWeight)
    {
        this(new ItemStack(item, 1), costPerItem, chanceWeight);
    }

    public static void init()
    {
        ChristmasList = new ArrayList();
        ChristmasList.add(new GiftItem(new ItemStack(Blocks.dirt, 32), 25, 200000));
        ChristmasList.add(new GiftItem(new ItemStack(Blocks.stone, 16), 50, 100000));
        ChristmasList.add(new GiftItem(new ItemStack(Items.coal, 8), 50, 50000));
        ChristmasList.add(new GiftItem(new ItemStack(Items.redstone, 2), 75, 22000));
        ChristmasList.add(new GiftItem(new ItemStack(Items.iron_ingot, 4), 75, 25000));
        ChristmasList.add(new GiftItem(Items.gold_ingot, 80, 17000));
        ChristmasList.add(new GiftItem(Items.diamond, 250, 5000));
        addModuleGifts(ChristmasList);
        EasterList = new ArrayList();
        addModuleGifts(EasterList);
    }

    public static void addModuleGifts(ArrayList<GiftItem> gifts)
    {
        Iterator i$ = ModuleData.getList().values().iterator();

        while (i$.hasNext())
        {
            ModuleData module = (ModuleData)i$.next();

            if (module.getIsValid() && !module.getIsLocked() && module.getHasRecipe() && module.getCost() > 0)
            {
                GiftItem item = new GiftItem(new ItemStack(ModItems.modules, 1, module.getID()), module.getCost() * 20, (int)Math.pow((double)(151 - module.getCost()), 2.0D));
                item.fixedSize = true;
                gifts.add(item);
            }
        }
    }

    public static ArrayList<ItemStack> generateItems(Random rand, ArrayList<GiftItem> gifts, int value, int maxTries)
    {
        int totalChanceWeight = 0;
        GiftItem tries;

        for (Iterator items = gifts.iterator(); items.hasNext(); totalChanceWeight += tries.chanceWeight)
        {
            tries = (GiftItem)items.next();
        }

        ArrayList var13 = new ArrayList();

        if (totalChanceWeight == 0)
        {
            return var13;
        }
        else
        {
            for (int var14 = 0; value > 0 && var14 < maxTries; ++var14)
            {
                int chance = rand.nextInt(totalChanceWeight);
                GiftItem gift;

                for (Iterator i$ = gifts.iterator(); i$.hasNext(); chance -= gift.chanceWeight)
                {
                    gift = (GiftItem)i$.next();

                    if (chance < gift.chanceWeight)
                    {
                        int maxSetSize = value / gift.costPerItem;

                        if (maxSetSize * gift.item.stackSize > gift.item.getItem().getItemStackLimit(gift.item))
                        {
                            maxSetSize = gift.item.getItem().getItemStackLimit(gift.item) / gift.item.stackSize;
                        }

                        if (maxSetSize > 0)
                        {
                            int setSize = 1;

                            if (!gift.fixedSize)
                            {
                                for (int item = 1; item < maxSetSize; ++item)
                                {
                                    if (rand.nextBoolean())
                                    {
                                        ++setSize;
                                    }
                                }
                            }

                            ItemStack var15 = gift.item.copy();
                            var15.stackSize *= setSize;
                            var13.add(var15);
                            value -= setSize * gift.costPerItem;
                        }

                        break;
                    }
                }
            }

            return var13;
        }
    }
}
