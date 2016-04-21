package vswe.stevescarts.Helpers;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import vswe.stevescarts.Modules.Addons.ModuleCrafter;

public class CraftingDummy extends InventoryCrafting
{
    private int inventoryWidth = 3;
    private ModuleCrafter module;

    public CraftingDummy(ModuleCrafter module)
    {
        super((Container)null, 3, 3);
        this.module = module;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : this.module.getStack(par1);
    }

    /**
     * Returns the itemstack in the slot specified (Top left is 0, 0). Args: row, column
     */
    public ItemStack getStackInRowAndColumn(int par1, int par2)
    {
        if (par1 >= 0 && par1 < this.inventoryWidth)
        {
            int k = par1 + par2 * this.inventoryWidth;
            return this.getStackInSlot(k);
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        return null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {}

    public void update()
    {
        this.module.setStack(9, this.getResult());
    }

    public ItemStack getResult()
    {
        return CraftingManager.getInstance().findMatchingRecipe(this, this.module.getCart().worldObj);
    }

    public IRecipe getRecipe()
    {
        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); ++i)
        {
            IRecipe irecipe = (IRecipe)CraftingManager.getInstance().getRecipeList().get(i);

            if (irecipe.matches(this, this.module.getCart().worldObj))
            {
                return irecipe;
            }
        }

        return null;
    }
}
