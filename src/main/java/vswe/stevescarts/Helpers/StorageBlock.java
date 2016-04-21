package vswe.stevescarts.Helpers;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Blocks.ModBlocks;

public class StorageBlock
{
    private String name;
    private ItemStack item;

    public StorageBlock(String name, ItemStack item)
    {
        this.name = name;
        this.item = item.copy();
        this.item.stackSize = 9;
    }

    public String getName()
    {
        return this.name;
    }

    public void loadRecipe(int i)
    {
        ItemStack block = new ItemStack(ModBlocks.STORAGE.getBlock(), 1, i);
        RecipeHelper.addRecipe(block, new Object[][] {{this.item, this.item, this.item}, {this.item, this.item, this.item}, {this.item, this.item, this.item}});
        RecipeHelper.addRecipe(this.item, new Object[][] {{block}});
    }
}
