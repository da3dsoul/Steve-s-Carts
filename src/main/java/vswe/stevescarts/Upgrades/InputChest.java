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
import vswe.stevescarts.ModuleData.ModuleDataHull;
import vswe.stevescarts.Slots.SlotAssemblerFuel;
import vswe.stevescarts.Slots.SlotModule;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class InputChest extends SimpleInventoryEffect
{
    public InputChest(int inventoryWidth, int inventoryHeight)
    {
        super(inventoryWidth, inventoryHeight);
    }

    public String getName()
    {
        return Localization.UPGRADES.INPUT_CHEST.translate(new String[] {String.valueOf(this.getInventorySize())});
    }

    public void init(TileEntityUpgrade upgrade)
    {
        upgrade.getCompound().setByte("TransferCooldown", (byte)0);
    }

    public Class <? extends Slot > getSlot(int i)
    {
        return SlotModule.class;
    }

    public void update(TileEntityUpgrade upgrade)
    {
        if (!upgrade.getWorldObj().isRemote && upgrade.getMaster() != null)
        {
            NBTTagCompound comp = upgrade.getCompound();

            if (comp.getByte("TransferCooldown") != 0)
            {
                comp.setByte("TransferCooldown", (byte)(comp.getByte("TransferCooldown") - 1));
            }
            else
            {
                comp.setByte("TransferCooldown", (byte)20);

                for (int slotId = 0; slotId < upgrade.getUpgrade().getInventorySize(); ++slotId)
                {
                    ItemStack itemstack = upgrade.getStackInSlot(slotId);

                    if (itemstack != null)
                    {
                        ModuleData module = ModItems.modules.getModuleData(itemstack);

                        if (module != null && this.isValidForBluePrint(upgrade.getMaster(), module) && !this.willInvalidate(upgrade.getMaster(), module))
                        {
                            int stackSize = itemstack.stackSize;
                            TransferHandler.TransferItem(itemstack, upgrade.getMaster(), new ContainerCartAssembler((IInventory)null, upgrade.getMaster()), Slot.class, SlotAssemblerFuel.class, 1);

                            if (itemstack.stackSize == 0)
                            {
                                upgrade.setInventorySlotContents(slotId, (ItemStack)null);
                            }

                            if (stackSize != itemstack.stackSize)
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean willInvalidate(TileEntityCartAssembler assembler, ModuleData module)
    {
        ModuleDataHull hull = assembler.getHullModule();

        if (hull == null)
        {
            return false;
        }
        else
        {
            ArrayList modules = assembler.getNonHullModules();
            modules.add(module);
            return ModuleData.checkForErrors(hull, modules) != null;
        }
    }

    private boolean isValidForBluePrint(TileEntityCartAssembler assembler, ModuleData module)
    {
        Iterator i$ = assembler.getUpgradeTiles().iterator();

        while (i$.hasNext())
        {
            TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();
            Iterator i$1 = tile.getUpgrade().getEffects().iterator();

            while (i$1.hasNext())
            {
                BaseEffect effect = (BaseEffect)i$1.next();

                if (effect instanceof Blueprint)
                {
                    return ((Blueprint)effect).isValidForBluePrint(tile, assembler.getModules(true), module);
                }
            }
        }

        return true;
    }
}
