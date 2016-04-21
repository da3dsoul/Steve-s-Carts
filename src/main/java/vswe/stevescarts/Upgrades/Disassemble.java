package vswe.stevescarts.Upgrades;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Containers.ContainerCartAssembler;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Slots.SlotCart;
import vswe.stevescarts.Slots.SlotModule;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Disassemble extends InventoryEffect
{
    public int getInventorySize()
    {
        return 31;
    }

    public int getSlotX(int id)
    {
        return id == 0 ? 178 : 38 + (id - 1) % 10 * 18;
    }

    public int getSlotY(int id)
    {
        int y;

        if (id == 0)
        {
            y = 0;
        }
        else
        {
            y = (id - 1) / 10 + 2;
        }

        return 8 + y * 18;
    }

    public Class <? extends Slot > getSlot(int i)
    {
        return i == 0 ? SlotCart.class : SlotModule.class;
    }

    public void load(TileEntityUpgrade upgrade, NBTTagCompound compound)
    {
        this.setLastCart(upgrade, upgrade.getStackInSlot(0));
    }

    public String getName()
    {
        return Localization.UPGRADES.DISASSEMBLE.translate(new String[0]);
    }

    public void onInventoryChanged(TileEntityUpgrade upgrade)
    {
        ItemStack cart = upgrade.getStackInSlot(0);

        if (!this.updateCart(upgrade, cart))
        {
            boolean needsToPuke = true;

            for (int i = 1; i < this.getInventorySize(); ++i)
            {
                if (upgrade.getStackInSlot(i) == null)
                {
                    ItemStack item = upgrade.getStackInSlot(0);
                    upgrade.setInventorySlotContents(0, (ItemStack)null);
                    upgrade.setInventorySlotContents(i, item);
                    needsToPuke = false;
                    break;
                }
            }

            if (needsToPuke)
            {
                if (!upgrade.getWorldObj().isRemote)
                {
                    upgrade.getMaster().puke(upgrade.getStackInSlot(0).copy());
                }

                upgrade.setInventorySlotContents(0, (ItemStack)null);
            }
        }
    }

    public void removed(TileEntityUpgrade upgrade)
    {
        this.updateCart(upgrade, (ItemStack)null);
    }

    private void resetMaster(TileEntityCartAssembler master, boolean full)
    {
        for (int i = 0; i < master.getSizeInventory() - master.nonModularSlots(); ++i)
        {
            if (master.getStackInSlot(i) != null)
            {
                if (master.getStackInSlot(i).stackSize <= 0)
                {
                    master.setInventorySlotContents(i, (ItemStack)null);
                }
                else if (full)
                {
                    if (!master.getWorldObj().isRemote)
                    {
                        master.puke(master.getStackInSlot(i).copy());
                    }

                    master.setInventorySlotContents(i, (ItemStack)null);
                }
            }
        }
    }

    private void setLastCart(TileEntityUpgrade upgrade, ItemStack cart)
    {
        if (cart == null)
        {
            upgrade.getCompound().setShort("id", (short)0);
        }
        else
        {
            cart.writeToNBT(upgrade.getCompound());
        }
    }

    private ItemStack getLastCart(TileEntityUpgrade upgrade)
    {
        return ItemStack.loadItemStackFromNBT(upgrade.getCompound());
    }

    private boolean updateCart(TileEntityUpgrade upgrade, ItemStack cart)
    {
        if (upgrade.getMaster() != null)
        {
            if (cart != null && cart.getItem() == ModItems.carts && cart.getTagCompound() != null && !cart.getTagCompound().hasKey("maxTime"))
            {
                ItemStack last = this.getLastCart(upgrade);
                this.setLastCart(upgrade, cart);
                int result = this.canDisassemble(upgrade);
                boolean reset = false;

                if (result > 0 && last != null && !ItemStack.areItemStacksEqual(cart, last))
                {
                    result = 2;
                    reset = true;
                }

                if (result != 2)
                {
                    return result == 1 && upgrade.getMaster().getStackInSlot(0) != null;
                }

                if (reset)
                {
                    this.resetMaster(upgrade.getMaster(), true);
                }

                boolean addedHull = false;
                ArrayList modules = ModuleData.getModularItems(cart);
                Iterator i$ = modules.iterator();

                while (i$.hasNext())
                {
                    ItemStack item = (ItemStack)i$.next();
                    item.stackSize = 0;
                    TransferHandler.TransferItem(item, upgrade.getMaster(), new ContainerCartAssembler((IInventory)null, upgrade.getMaster()), 1);

                    if (!addedHull)
                    {
                        addedHull = true;
                        upgrade.getMaster().updateSlots();
                    }
                }
            }
            else
            {
                this.resetMaster(upgrade.getMaster(), false);
                this.setLastCart(upgrade, (ItemStack)null);

                if (cart != null)
                {
                    upgrade.getMaster().puke(cart);
                    upgrade.setInventorySlotContents(0, (ItemStack)null);
                }
            }
        }

        return true;
    }

    public int canDisassemble(TileEntityUpgrade upgrade)
    {
        int disassembleCount = 0;
        Iterator i = upgrade.getMaster().getEffects().iterator();

        while (i.hasNext())
        {
            BaseEffect effect = (BaseEffect)i.next();

            if (effect instanceof Disassemble)
            {
                ++disassembleCount;
            }
        }

        if (disassembleCount != 1)
        {
            return 0;
        }
        else
        {
            int var5;

            for (var5 = 0; var5 < upgrade.getMaster().getSizeInventory() - upgrade.getMaster().nonModularSlots(); ++var5)
            {
                if (upgrade.getMaster().getStackInSlot(var5) != null && upgrade.getMaster().getStackInSlot(var5).stackSize <= 0)
                {
                    return 1;
                }
            }

            for (var5 = 0; var5 < upgrade.getMaster().getSizeInventory() - upgrade.getMaster().nonModularSlots(); ++var5)
            {
                if (upgrade.getMaster().getStackInSlot(var5) != null)
                {
                    return 0;
                }
            }

            return 2;
        }
    }
}
