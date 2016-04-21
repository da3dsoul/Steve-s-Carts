package vswe.stevescarts.Helpers;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Slots.ISpecialItemTransferValidator;
import vswe.stevescarts.Slots.ISpecialSlotValidator;

public class TransferHandler
{
    public static boolean isSlotOfType(Slot slot, Class slotType)
    {
        if (slot instanceof ISpecialSlotValidator)
        {
            ISpecialSlotValidator specSlot = (ISpecialSlotValidator)slot;
            return specSlot.isSlotValid();
        }
        else
        {
            return slotType.isInstance(slot);
        }
    }

    public static boolean isItemValidForTransfer(Slot slot, ItemStack item, TransferHandler.TRANSFER_TYPE type)
    {
        if (slot instanceof ISpecialItemTransferValidator)
        {
            ISpecialItemTransferValidator specSlot = (ISpecialItemTransferValidator)slot;
            return specSlot.isItemValidForTransfer(item, type);
        }
        else
        {
            return slot.isItemValid(item);
        }
    }

    public static void TransferItem(ItemStack iStack, IInventory inv, Container cont, int maxItems)
    {
        TransferItem(iStack, inv, cont, Slot.class, (Class)null, maxItems);
    }

    public static void TransferItem(ItemStack iStack, IInventory inv, Container cont, Class validSlot, int maxItems, TransferHandler.TRANSFER_TYPE type)
    {
        TransferItem(iStack, inv, 0, inv.getSizeInventory() - 1, cont, validSlot, (Class)null, maxItems, type, false);
    }

    public static void TransferItem(ItemStack iStack, IInventory inv, Container cont, Class validSlot, Class invalidSlot, int maxItems)
    {
        TransferItem(iStack, inv, 0, inv.getSizeInventory() - 1, cont, validSlot, invalidSlot, maxItems);
    }

    public static void TransferItem(ItemStack iStack, IInventory inv, int start, int end, Container cont, Class validSlot, Class invalidSlot, int maxItems)
    {
        TransferItem(iStack, inv, start, end, cont, validSlot, invalidSlot, maxItems, TransferHandler.TRANSFER_TYPE.OTHER, false);
    }

    public static void TransferItem(ItemStack iStack, IInventory inv, int start, int end, Container cont, Class validSlot, Class invalidSlot, int maxItems, TransferHandler.TRANSFER_TYPE type, boolean fake)
    {
        start = Math.max(0, start);
        end = Math.min(inv.getSizeInventory() - 1, end);
        int startEmpty = start;
        int startOccupied = start;

        while (true)
        {
            int pos = -1;
            int existingItem = startEmpty;

            while (true)
            {
                if (existingItem <= end)
                {
                    if (!isSlotOfType(cont.getSlot(existingItem), validSlot) || invalidSlot != null && isSlotOfType(cont.getSlot(existingItem), invalidSlot) || inv.getStackInSlot(existingItem) == null || inv.getStackInSlot(existingItem).getItem() != iStack.getItem() || !inv.getStackInSlot(existingItem).isStackable() || inv.getStackInSlot(existingItem).stackSize >= inv.getStackInSlot(existingItem).getMaxStackSize() || inv.getStackInSlot(existingItem).stackSize >= cont.getSlot(existingItem).getSlotStackLimit() || inv.getStackInSlot(existingItem).stackSize <= 0 || iStack.stackSize <= 0 || inv.getStackInSlot(existingItem).getHasSubtypes() && inv.getStackInSlot(existingItem).getItemDamage() != iStack.getItemDamage() || inv.getStackInSlot(existingItem).getTagCompound() != null && !inv.getStackInSlot(existingItem).getTagCompound().equals(iStack.getTagCompound()))
                    {
                        ++existingItem;
                        continue;
                    }

                    pos = existingItem;
                    startEmpty = existingItem + 1;
                }

                if (pos == -1)
                {
                    for (existingItem = startOccupied; existingItem <= end; ++existingItem)
                    {
                        if (isSlotOfType(cont.getSlot(existingItem), validSlot) && (invalidSlot == null || !isSlotOfType(cont.getSlot(existingItem), invalidSlot)))
                        {
                            Slot stackSize = cont.getSlot(existingItem);

                            if (isItemValidForTransfer(stackSize, iStack, type) && inv.getStackInSlot(existingItem) == null)
                            {
                                pos = existingItem;
                                startOccupied = existingItem + 1;
                                break;
                            }
                        }
                    }
                }

                if (pos != -1)
                {
                    ItemStack var16 = null;

                    if (inv.getStackInSlot(pos) == null)
                    {
                        ItemStack var17 = iStack.copy();
                        var17.stackSize = 0;

                        if (!fake)
                        {
                            inv.setInventorySlotContents(pos, var17);
                        }

                        var16 = var17;
                    }
                    else
                    {
                        var16 = inv.getStackInSlot(pos);
                    }

                    int var18 = iStack.stackSize;

                    if (var18 > var16.getMaxStackSize() - var16.stackSize)
                    {
                        var18 = var16.getMaxStackSize() - var16.stackSize;
                    }

                    if (var18 > cont.getSlot(pos).getSlotStackLimit() - var16.stackSize)
                    {
                        var18 = cont.getSlot(pos).getSlotStackLimit() - var16.stackSize;
                    }

                    boolean killMe = false;

                    if (maxItems != -1)
                    {
                        if (var18 > maxItems)
                        {
                            var18 = maxItems;
                            killMe = true;
                        }

                        maxItems -= var18;
                    }

                    if (var18 <= 0)
                    {
                        pos = -1;
                    }
                    else
                    {
                        iStack.stackSize -= var18;

                        if (!fake)
                        {
                            ItemStack var10000 = inv.getStackInSlot(pos);
                            var10000.stackSize += var18;
                        }

                        if (iStack.stackSize == 0 || killMe || maxItems == 0)
                        {
                            pos = -1;
                        }
                    }
                }

                if (pos == -1)
                {
                    if (!fake)
                    {
                        //inv.onInventoryChanged();
                    }

                    return;
                }

                break;
            }
        }
    }

    public static enum TRANSFER_TYPE
    {
        SHIFT("SHIFT", 0),
        MANAGER("MANAGER", 1),
        OTHER("OTHER", 2);

        private static final TransferHandler.TRANSFER_TYPE[] $VALUES = new TransferHandler.TRANSFER_TYPE[]{SHIFT, MANAGER, OTHER};

        private TRANSFER_TYPE(String var1, int var2) {}
    }
}
