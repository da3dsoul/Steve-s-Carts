package vswe.stevescarts.Containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.TileEntities.TileEntityBase;

public abstract class ContainerBase extends Container
{
    public abstract IInventory getMyInventory();

    public abstract TileEntityBase getTileEntity();

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
        if (this.getMyInventory() == null)
        {
            return null;
        }
        else
        {
            ItemStack itemstack = null;
            Slot slot = (Slot)this.inventorySlots.get(i);

            if (slot != null && slot.getHasStack())
            {
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();

                if (i < this.getMyInventory().getSizeInventory())
                {
                    if (!this.mergeItemStack(itemstack1, this.getMyInventory().getSizeInventory() + 28, this.getMyInventory().getSizeInventory() + 36, false) && !this.mergeItemStack(itemstack1, this.getMyInventory().getSizeInventory(), this.getMyInventory().getSizeInventory() + 28, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(itemstack1, 0, this.getMyInventory().getSizeInventory(), false))
                {
                    return null;
                }

                if (itemstack1.stackSize == 0)
                {
                    slot.putStack((ItemStack)null);
                }
                else
                {
                    slot.onSlotChanged();
                }

                if (itemstack1.stackSize == itemstack.stackSize)
                {
                    return null;
                }

                slot.onPickupFromSlot(player, itemstack1);
            }

            return itemstack;
        }
    }

    /**
     * merges provided ItemStack with the first avaliable one in the container/player inventory
     */
    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        if (this.getMyInventory() == null)
        {
            return false;
        }
        else
        {
            boolean var5 = false;
            int var6 = par2;

            if (par4)
            {
                var6 = par3 - 1;
            }

            Slot var7;
            ItemStack var8;
            int stackSize;

            if (par1ItemStack.isStackable())
            {
                while (par1ItemStack.stackSize > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2))
                {
                    var7 = (Slot)this.inventorySlots.get(var6);
                    var8 = var7.getStack();

                    if (var8 != null && var8.stackSize > 0 && var8.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, var8))
                    {
                        stackSize = var8.stackSize + par1ItemStack.stackSize;
                        int newItem = Math.min(par1ItemStack.getMaxStackSize(), var7.getSlotStackLimit());

                        if (stackSize <= newItem)
                        {
                            par1ItemStack.stackSize = 0;
                            var8.stackSize = stackSize;
                            var7.onSlotChanged();
                            var5 = true;
                        }
                        else if (var8.stackSize < newItem)
                        {
                            par1ItemStack.stackSize -= newItem - var8.stackSize;
                            var8.stackSize = newItem;
                            var7.onSlotChanged();
                            var5 = true;
                        }
                    }

                    if (par4)
                    {
                        --var6;
                    }
                    else
                    {
                        ++var6;
                    }
                }
            }

            if (par1ItemStack.stackSize > 0)
            {
                if (par4)
                {
                    var6 = par3 - 1;
                }
                else
                {
                    var6 = par2;
                }

                while (!par4 && var6 < par3 || par4 && var6 >= par2)
                {
                    var7 = (Slot)this.inventorySlots.get(var6);
                    var8 = var7.getStack();

                    if (var8 == null && TransferHandler.isItemValidForTransfer(var7, par1ItemStack, TransferHandler.TRANSFER_TYPE.SHIFT))
                    {
                        stackSize = Math.min(var7.getSlotStackLimit(), par1ItemStack.stackSize);
                        ItemStack var11 = par1ItemStack.copy();
                        var11.stackSize = stackSize;
                        par1ItemStack.stackSize -= stackSize;
                        var7.putStack(var11);
                        var7.onSlotChanged();
                        var5 = par1ItemStack.stackSize == 0;
                        break;
                    }

                    if (par4)
                    {
                        --var6;
                    }
                    else
                    {
                        ++var6;
                    }
                }
            }

            return var5;
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.getTileEntity() != null && this.getTileEntity().isUseableByPlayer(entityplayer);
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);

        if (this.getTileEntity() != null)
        {
            this.getTileEntity().initGuiData(this, par1ICrafting);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        par2 &= 65535;

        if (this.getTileEntity() != null)
        {
            this.getTileEntity().receiveGuiData(par1, (short)par2);
        }
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (this.getTileEntity() != null)
        {
            Iterator var1 = this.crafters.iterator();

            while (var1.hasNext())
            {
                ICrafting var2 = (ICrafting)var1.next();
                this.getTileEntity().checkGuiData(this, var2);
            }
        }
    }
}
