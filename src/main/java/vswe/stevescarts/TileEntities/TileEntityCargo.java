package vswe.stevescarts.TileEntities;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerCargo;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.CargoItemSelection;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ManagerTransfer;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiCargo;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.Slots.ISlotExplosions;
import vswe.stevescarts.Slots.SlotArrow;
import vswe.stevescarts.Slots.SlotBridge;
import vswe.stevescarts.Slots.SlotBuilder;
import vswe.stevescarts.Slots.SlotCake;
import vswe.stevescarts.Slots.SlotCargo;
import vswe.stevescarts.Slots.SlotChest;
import vswe.stevescarts.Slots.SlotFertilizer;
import vswe.stevescarts.Slots.SlotFirework;
import vswe.stevescarts.Slots.SlotFuel;
import vswe.stevescarts.Slots.SlotMilker;
import vswe.stevescarts.Slots.SlotSapling;
import vswe.stevescarts.Slots.SlotSeed;
import vswe.stevescarts.Slots.SlotTorch;
import vswe.stevescarts.StevesCarts;

public class TileEntityCargo extends TileEntityManager implements IEnergyReceiver
{
    public static ArrayList<CargoItemSelection> itemSelections;
    public int[] target = new int[] {0, 0, 0, 0};
    public ArrayList<SlotCargo> cargoSlots;
    public int lastLayout = -1;
    private ManagerTransfer latestTransferToBeUsed;

    private int energy;

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiCargo(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerCargo(inv, this);
    }

    public static void loadSelectionSettings()
    {
        itemSelections = new ArrayList();
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ALL, Slot.class, new ItemStack(ModItems.carts, 1, 0)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ENGINE, SlotFuel.class, new ItemStack(ModItems.modules, 1, 0)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_RAILER, SlotBuilder.class, new ItemStack(ModItems.modules, 1, 10)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_STORAGE, SlotChest.class, new ItemStack(Blocks.chest, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_TORCHES, SlotTorch.class, new ItemStack(Blocks.torch, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_EXPLOSIVES, ISlotExplosions.class, ComponentTypes.DYNAMITE.getItemStack()));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ARROWS, SlotArrow.class, new ItemStack(Items.arrow, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_BRIDGE, SlotBridge.class, new ItemStack(Blocks.brick_block, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_SEEDS, SlotSeed.class, new ItemStack(Items.wheat_seeds, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_FERTILIZER, SlotFertilizer.class, new ItemStack(Items.dye, 1, 15)));
        itemSelections.add(new CargoItemSelection((Localization.GUI.CARGO)null, (Class)null, (ItemStack)null));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_SAPLINGS, SlotSapling.class, new ItemStack(Blocks.sapling, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_FIREWORK, SlotFirework.class, new ItemStack(Items.fireworks, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_BUCKETS, SlotMilker.class, new ItemStack(Items.bucket, 1)));
        itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_CAKES, SlotCake.class, new ItemStack(Items.cake, 1)));
    }

    @Override
    public boolean exchangeItems(ManagerTransfer transfer) {
        if(energy > 0 && transfer.getCart().getMaxEnergyStored(ForgeDirection.UNKNOWN) > 0) {
            int temp = transfer.getCart().receiveEnergy(ForgeDirection.UNKNOWN, energy < 1000 ? energy : 1000, false);
            energy -= temp;
        }
        return super.exchangeItems(transfer) || (energy > 0 && transfer.getCart().getMaxEnergyStored(ForgeDirection.UNKNOWN) > 0 && transfer.getCart().getEnergyStored(ForgeDirection.UNKNOWN) < transfer.getCart().getMaxEnergyStored(ForgeDirection.UNKNOWN));
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 60;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.cargomanager";
    }

    protected void updateLayout()
    {
        if (this.cargoSlots != null && this.lastLayout != this.layoutType)
        {
            Iterator i$ = this.cargoSlots.iterator();

            while (i$.hasNext())
            {
                SlotCargo slot = (SlotCargo)i$.next();
                slot.updatePosition();
            }

            this.lastLayout = this.layoutType;
        }
    }

    protected boolean isTargetValid(ManagerTransfer transfer)
    {
        return this.target[transfer.getSetting()] >= 0 && this.target[transfer.getSetting()] < itemSelections.size();
    }

    protected void receiveClickData(int packetid, int id, int dif)
    {
        if (packetid == 1)
        {
            this.target[id] += dif;

            if (this.target[id] >= itemSelections.size())
            {
                this.target[id] = 0;
            }
            else if (this.target[id] < 0)
            {
                this.target[id] = itemSelections.size() - 1;
            }

            if (this.color[id] - 1 == this.getSide())
            {
                this.reset();
            }

            if (((CargoItemSelection)itemSelections.get(this.target[id])).getValidSlot() == null && dif != 0)
            {
                this.receiveClickData(packetid, id, dif);
            }
        }
    }

    public void checkGuiData(ContainerManager conManager, ICrafting crafting, boolean isNew)
    {
        super.checkGuiData(conManager, crafting, isNew);
        ContainerCargo con = (ContainerCargo)conManager;
        short targetShort = 0;

        for (int i = 0; i < 4; ++i)
        {
            targetShort = (short)(targetShort | (this.target[i] & 15) << i * 4);
        }

        if (isNew || con.lastTarget != targetShort)
        {
            this.updateGuiData(con, crafting, 2, targetShort);
            con.lastTarget = targetShort;
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 2)
        {
            for (int i = 0; i < 4; ++i)
            {
                this.target[i] = (data & 15 << i * 4) >> i * 4;
            }
        }
        else
        {
            super.receiveGuiData(id, data);
        }
    }

    public int getAmount(int id)
    {
        int val = this.getAmountId(id);

        switch (val)
        {
            case 1:
                return 1;

            case 2:
                return 3;

            case 3:
                return 8;

            case 4:
                return 16;

            case 5:
                return 32;

            case 6:
                return 64;

            case 7:
                return 1;

            case 8:
                return 2;

            case 9:
                return 3;

            case 10:
                return 5;

            default:
                return 0;
        }
    }

    public int getAmountType(int id)
    {
        int val = this.getAmountId(id);
        return val == 0 ? 0 : (val <= 6 ? 1 : 2);
    }

    public int getAmountCount()
    {
        return 11;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.setWorkload(nbttagcompound.getByte("workload"));

        for (int i = 0; i < 4; ++i)
        {
            this.target[i] = nbttagcompound.getByte("target" + i);
        }

        energy = nbttagcompound.getInteger("energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("workload", (byte)this.getWorkload());

        for (int i = 0; i < 4; ++i)
        {
            nbttagcompound.setByte("target" + i, (byte)this.target[i]);
        }

        nbttagcompound.setInteger("energy", energy);
    }

    protected boolean doTransfer(ManagerTransfer transfer)
    {
        Class slotCart = ((CargoItemSelection)itemSelections.get(this.target[transfer.getSetting()])).getValidSlot();

        if (slotCart == null)
        {
            transfer.setLowestSetting(transfer.getSetting() + 1);
            return true;
        }
        else
        {
            Class slotCargo = SlotCargo.class;
            Object fromInv;
            Object fromCont;
            Class fromValid;
            Object toInv;
            Object toCont;
            Class toValid;

            if (this.toCart[transfer.getSetting()])
            {
                fromInv = this;
                fromCont = new ContainerCargo((IInventory)null, this);
                fromValid = slotCargo;
                toInv = transfer.getCart();
                toCont = transfer.getCart().getCon((InventoryPlayer)null);
                toValid = slotCart;
            }
            else
            {
                fromInv = transfer.getCart();
                fromCont = transfer.getCart().getCon((InventoryPlayer)null);
                fromValid = slotCart;
                toInv = this;
                toCont = new ContainerCargo((IInventory)null, this);
                toValid = slotCargo;
            }

            this.latestTransferToBeUsed = transfer;

            for (int i = 0; i < ((IInventory)fromInv).getSizeInventory(); ++i)
            {
                if (TransferHandler.isSlotOfType(((Container)fromCont).getSlot(i), fromValid) && ((IInventory)fromInv).getStackInSlot(i) != null)
                {
                    ItemStack iStack = ((IInventory)fromInv).getStackInSlot(i);
                    int stacksize = iStack.stackSize;
                    int maxNumber;

                    if (this.getAmountType(transfer.getSetting()) == 1)
                    {
                        maxNumber = this.getAmount(transfer.getSetting()) - transfer.getWorkload();
                    }
                    else
                    {
                        maxNumber = -1;
                    }

                    TransferHandler.TransferItem(iStack, (IInventory)toInv, (Container)toCont, toValid, maxNumber, TransferHandler.TRANSFER_TYPE.MANAGER);

                    if (iStack.stackSize != stacksize)
                    {
                        if (this.getAmountType(transfer.getSetting()) == 1)
                        {
                            transfer.setWorkload(transfer.getWorkload() + stacksize - iStack.stackSize);
                        }
                        else if (this.getAmountType(transfer.getSetting()) == 2)
                        {
                            transfer.setWorkload(transfer.getWorkload() + 1);
                        }

                        //this.onInventoryChanged();
                        transfer.getCart().onInventoryChanged();

                        if (iStack.stackSize == 0)
                        {
                            ((IInventory)fromInv).setInventorySlotContents(i, (ItemStack)null);
                        }

                        if (transfer.getWorkload() >= this.getAmount(transfer.getSetting()) && this.getAmountType(transfer.getSetting()) != 0)
                        {
                            transfer.setLowestSetting(transfer.getSetting() + 1);
                        }

                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {
        return true;
    }

    public ManagerTransfer getCurrentTransferForSlots()
    {
        return this.latestTransferToBeUsed;
    }
    
    @Override
	public boolean hasCustomInventoryName() {return false;}

    /**
     * Add energy to an IEnergyReceiver, internal distribution is left entirely to the IEnergyReceiver.
     *
     * @param from       Orientation the energy is received from.
     * @param maxReceive Maximum amount of energy to receive.
     * @param simulate   If TRUE, the charge will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) received.
     */
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if(energy + maxReceive > getMaxEnergyStored(from)) {
            maxReceive = getMaxEnergyStored(from) - energy;
        }
        if(!simulate) energy += maxReceive;
        return maxReceive;
    }

    /**
     * Returns the amount of energy currently stored.
     *
     * @param from
     */
    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energy;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     *
     * @param from
     */
    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return StevesCarts.instance.maxCargoManagerEnergy;
    }

    /**
     * Returns TRUE if the TileEntity can connect on a given side.
     *
     * @param from
     */
    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }
}
