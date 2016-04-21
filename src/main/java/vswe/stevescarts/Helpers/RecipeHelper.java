package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vswe.stevescarts.Items.ModItems;

public final class RecipeHelper
{
    public static void addRecipe(ItemStack item, Object[][] recipe)
    {
        if (recipe != null && item != null)
        {
            if (item.getItem() == ModItems.component && !ModItems.component.isValid(item))
            {
                return;
            }

            ArrayList usedItems = new ArrayList();
            String chars = "ABCDEFGHI";
            String[] parts = new String[recipe.length];
            boolean isOreDict = false;
            boolean isSpecial = false;
            ItemStack[] items = new ItemStack[recipe.length * recipe[0].length];
            int i;

            for (int finalRecipe = 0; finalRecipe < recipe.length; ++finalRecipe)
            {
                parts[finalRecipe] = "";

                for (i = 0; i < recipe[finalRecipe].length; ++i)
                {
                    Object obj = recipe[finalRecipe][i];
                    boolean valid = true;

                    if (obj instanceof Item)
                    {
                        obj = new ItemStack((Item)obj, 1);
                    }
                    else if (obj instanceof Block)
                    {
                        obj = new ItemStack((Block)obj, 1);
                    }
                    else if (obj instanceof ItemStack)
                    {
                        obj = ((ItemStack)obj).copy();
                        ((ItemStack)obj).stackSize = 1;

                        if (obj != null && ((ItemStack)obj).getItem() instanceof ItemEnchantedBook)
                        {
                            isSpecial = true;
                        }
                    }
                    else if (obj instanceof String)
                    {
                        isOreDict = true;
                    }
                    else
                    {
                        valid = false;
                    }

                    if (obj instanceof ItemStack)
                    {
                        items[i + finalRecipe * recipe[finalRecipe].length] = (ItemStack)obj;
                    }

                    char myChar;

                    if (valid)
                    {
                        int ind = -1;

                        for (int k = 0; k < usedItems.size(); ++k)
                        {
                            if (usedItems.get(k) instanceof ItemStack && obj instanceof ItemStack && ((ItemStack)usedItems.get(k)).isItemEqual((ItemStack)obj) || usedItems.get(k) instanceof String && obj instanceof String && ((String)usedItems.get(k)).equals((String)obj))
                            {
                                ind = k;
                                break;
                            }
                        }

                        if (ind == -1)
                        {
                            usedItems.add(obj);
                            ind = usedItems.size() - 1;
                        }

                        myChar = chars.charAt(ind);
                    }
                    else
                    {
                        myChar = 32;
                    }

                    parts[finalRecipe] = parts[finalRecipe] + myChar;
                }
            }

            Object[] var15 = new Object[parts.length + usedItems.size() * 2];
            System.arraycopy(parts, 0, var15, 0, parts.length);

            for (i = 0; i < usedItems.size(); ++i)
            {
                var15[parts.length + i * 2] = Character.valueOf(chars.charAt(i));
                var15[parts.length + i * 2 + 1] = usedItems.get(i);
            }

            if (isSpecial)
            {
                GameRegistry.addRecipe(new ShapedRecipes2(recipe[0].length, recipe.length, items, item));
            }
            else if (isOreDict)
            {
                GameRegistry.addRecipe(new ShapedOreRecipe(item, var15));
            }
            else
            {
                GameRegistry.addRecipe(item, var15);
            }
        }
    }
}
