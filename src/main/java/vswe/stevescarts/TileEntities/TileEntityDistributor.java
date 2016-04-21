package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerDistributor;
import vswe.stevescarts.Helpers.DistributorSetting;
import vswe.stevescarts.Helpers.DistributorSide;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiDistributor;

public class TileEntityDistributor extends TileEntityBase implements IInventory, ISidedInventory, IFluidHandler
{
    private ArrayList<DistributorSide> sides = new ArrayList();
    private boolean dirty = true;
    private boolean dirty2 = true;
    private TileEntityManager[] inventories;
    public boolean hasTop;
    public boolean hasBot;

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiDistributor(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerDistributor(inv, this);
    }

    public ArrayList<DistributorSide> getSides()
    {
        return this.sides;
    }

    public TileEntityDistributor()
    {
        this.sides.add(new DistributorSide(0, Localization.GUI.DISTRIBUTOR.SIDE_ORANGE, ForgeDirection.UP));
        this.sides.add(new DistributorSide(1, Localization.GUI.DISTRIBUTOR.SIDE_PURPLE, ForgeDirection.DOWN));
        this.sides.add(new DistributorSide(2, Localization.GUI.DISTRIBUTOR.SIDE_YELLOW, ForgeDirection.NORTH));
        this.sides.add(new DistributorSide(3, Localization.GUI.DISTRIBUTOR.SIDE_GREEN, ForgeDirection.WEST));
        this.sides.add(new DistributorSide(4, Localization.GUI.DISTRIBUTOR.SIDE_BLUE, ForgeDirection.SOUTH));
        this.sides.add(new DistributorSide(5, Localization.GUI.DISTRIBUTOR.SIDE_RED, ForgeDirection.EAST));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        Iterator i$ = this.getSides().iterator();

        while (i$.hasNext())
        {
            DistributorSide side = (DistributorSide)i$.next();
            side.setData(nbttagcompound.getInteger("Side" + side.getId()));
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        Iterator i$ = this.getSides().iterator();

        while (i$.hasNext())
        {
            DistributorSide side = (DistributorSide)i$.next();
            nbttagcompound.setInteger("Side" + side.getId(), side.getData());
        }
    }

    public void updateEntity()
    {
        this.dirty = true;
        this.dirty2 = true;
    }

    protected void sendPacket(int id)
    {
        this.sendPacket(id, new byte[0]);
    }

    protected void sendPacket(int id, byte data)
    {
        this.sendPacket(id, new byte[] {data});
    }

    public void sendPacket(int id, byte[] data)
    {
        PacketHandler.sendPacket(id, data);
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0 || id == 1)
        {
            byte settingId = data[0];
            byte sideId = data[1];

            if (settingId >= 0 && settingId < DistributorSetting.settings.size() && sideId >= 0 && sideId < this.getSides().size())
            {
                if (id == 0)
                {
                    ((DistributorSide)this.getSides().get(sideId)).set(settingId);
                }
                else
                {
                    ((DistributorSide)this.getSides().get(sideId)).reset(settingId);
                }
            }
        }
    }

    public void initGuiData(Container con, ICrafting crafting) {}

    public void checkGuiData(Container con, ICrafting crafting)
    {
        ContainerDistributor condist = (ContainerDistributor)con;

        for (int i = 0; i < this.getSides().size(); ++i)
        {
            DistributorSide side = (DistributorSide)this.getSides().get(i);

            if (side.getLowShortData() != ((Short)condist.cachedValues.get(i * 2)).shortValue())
            {
                this.updateGuiData(con, crafting, i * 2, side.getLowShortData());
                condist.cachedValues.set(i * 2, Short.valueOf(side.getLowShortData()));
            }

            if (side.getHighShortData() != ((Short)condist.cachedValues.get(i * 2 + 1)).shortValue())
            {
                this.updateGuiData(con, crafting, i * 2 + 1, side.getHighShortData());
                condist.cachedValues.set(i * 2 + 1, Short.valueOf(side.getHighShortData()));
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        int sideId = id / 2;
        boolean isHigh = id % 2 == 1;
        DistributorSide side = (DistributorSide)this.getSides().get(sideId);

        if (isHigh)
        {
            side.setHighShortData(data);
        }
        else
        {
            side.setLowShortData(data);
        }
    }

    public TileEntityManager[] getInventories()
    {
        if (this.dirty)
        {
            this.generateInventories();
            this.dirty = false;
        }

        return this.inventories;
    }

    private void generateInventories()
    {
        TileEntityManager bot = this.generateManager(-1);
        TileEntityManager top = this.generateManager(1);
        this.hasTop = top != null;
        this.hasBot = bot != null;
        this.inventories = this.populateManagers(top, bot, this.hasTop, this.hasBot);
    }

    private TileEntityManager[] populateManagers(TileEntityManager topElement, TileEntityManager botElement, boolean hasTopElement, boolean hasBotElement)
    {
        return !hasTopElement && !hasBotElement ? new TileEntityManager[0] : (!hasBotElement ? new TileEntityManager[] {topElement}: (!hasTopElement ? new TileEntityManager[] {botElement}: new TileEntityManager[] {botElement, topElement}));
    }

    private TileEntityManager generateManager(int y)
    {
        TileEntity TE = this.worldObj.getTileEntity(this.xCoord, this.yCoord + y, this.zCoord);
        return TE != null && TE instanceof TileEntityManager ? (TileEntityManager)TE : null;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    private int translateSlotId(int slot)
    {
        return slot % 60;
    }

    private TileEntityManager getManagerFromSlotId(int slot)
    {
        TileEntityManager[] invs = this.getInventories();
        int id = slot / 60;

        if (!this.hasTop || !this.hasBot)
        {
            id = 0;
        }

        return id >= 0 && id < invs.length ? invs[id] : null;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 120;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot)
    {
        TileEntityManager manager = this.getManagerFromSlotId(slot);
        return manager != null ? manager.getStackInSlot(this.translateSlotId(slot)) : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int count)
    {
        TileEntityManager manager = this.getManagerFromSlotId(slot);
        return manager != null ? manager.decrStackSize(this.translateSlotId(slot), count) : null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        TileEntityManager manager = this.getManagerFromSlotId(slot);

        if (manager != null)
        {
            manager.setInventorySlotContents(this.translateSlotId(slot), itemstack);
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.cargodistributor";
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return false;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void closeInventory() {}

    public void openInventory() {}

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        TileEntityManager manager = this.getManagerFromSlotId(slot);
        return manager != null ? manager.getStackInSlotOnClosing(this.translateSlotId(slot)) : null;
    }

    private boolean isChunkValid(DistributorSide side, TileEntityManager manager, int chunkId, boolean top)
    {
        Iterator i$ = DistributorSetting.settings.iterator();
        DistributorSetting setting;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            setting = (DistributorSetting)i$.next();
        }
        while (!setting.isEnabled(this) || !side.isSet(setting.getId()) || !setting.isValid(manager, chunkId, top));

        return true;
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        IFluidTank[] tanks = this.getTanks(from);
        int amount = 0;
        IFluidTank[] arr$ = tanks;
        int len$ = tanks.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            IFluidTank tank = arr$[i$];
            amount += tank.fill(resource, doFill);
        }

        return amount;
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.drain(from, (FluidStack)null, maxDrain, doDrain);
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return this.drain(from, resource, resource == null ? 0 : resource.amount, doDrain);
    }

    private FluidStack drain(ForgeDirection from, FluidStack resource, int maxDrain, boolean doDrain)
    {
        FluidStack ret = resource;

        if (resource != null)
        {
            ret = resource.copy();
            ret.amount = 0;
        }

        IFluidTank[] tanks = this.getTanks(from);
        IFluidTank[] arr$ = tanks;
        int len$ = tanks.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            IFluidTank tank = arr$[i$];
            FluidStack temp = null;
            temp = tank.drain(maxDrain, doDrain);

            if (temp != null && (ret == null || ret.isFluidEqual(temp)))
            {
                if (ret == null)
                {
                    ret = temp;
                }
                else
                {
                    ret.amount += temp.amount;
                }

                maxDrain -= temp.amount;

                if (maxDrain <= 0)
                {
                    break;
                }
            }
        }

        return ret != null && ret.amount == 0 ? null : ret;
    }

    private IFluidTank[] getTanks(ForgeDirection direction)
    {
        TileEntityManager[] invs = this.getInventories();

        if (invs.length > 0)
        {
            Iterator i$ = this.getSides().iterator();

            while (i$.hasNext())
            {
                DistributorSide side = (DistributorSide)i$.next();

                if (side.getSide() == direction)
                {
                    ArrayList tanks = new ArrayList();

                    if (this.hasTop && this.hasBot)
                    {
                        this.populateTanks(tanks, side, invs[0], false);
                        this.populateTanks(tanks, side, invs[1], true);
                    }
                    else if (this.hasTop)
                    {
                        this.populateTanks(tanks, side, invs[0], true);
                    }
                    else if (this.hasBot)
                    {
                        this.populateTanks(tanks, side, invs[0], false);
                    }

                    return (IFluidTank[])tanks.toArray(new IFluidTank[tanks.size()]);
                }
            }
        }

        return new IFluidTank[0];
    }

    private void populateTanks(ArrayList<IFluidTank> tanks, DistributorSide side, TileEntityManager manager, boolean top)
    {
        if (manager != null && manager instanceof TileEntityLiquid)
        {
            TileEntityLiquid fluid = (TileEntityLiquid)manager;
            Tank[] managerTanks = fluid.getTanks();

            for (int i = 0; i < 4; ++i)
            {
                if (this.isChunkValid(side, manager, i, top) && !tanks.contains(managerTanks[i]))
                {
                    tanks.add(managerTanks[i]);
                }
            }
        }
    }

    private void populateSlots(ArrayList<Integer> slotchunks, DistributorSide side, TileEntityManager manager, boolean top)
    {
        if (manager != null && manager instanceof TileEntityCargo)
        {
            for (int i = 0; i < 4; ++i)
            {
                if (this.isChunkValid(side, manager, i, top))
                {
                    int chunkid = i + (top ? 4 : 0);

                    if (!slotchunks.contains(Integer.valueOf(chunkid)))
                    {
                        slotchunks.add(Integer.valueOf(chunkid));
                    }
                }
            }
        }
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int direction)
    {
        TileEntityManager[] invs = this.getInventories();

        if (invs.length > 0)
        {
            Iterator i$ = this.getSides().iterator();

            while (i$.hasNext())
            {
                DistributorSide side = (DistributorSide)i$.next();

                if (side.getIntSide() == direction)
                {
                    ArrayList slotchunks = new ArrayList();

                    if (this.hasTop && this.hasBot)
                    {
                        this.populateSlots(slotchunks, side, invs[0], false);
                        this.populateSlots(slotchunks, side, invs[1], true);
                    }
                    else if (this.hasTop)
                    {
                        this.populateSlots(slotchunks, side, invs[0], true);
                    }
                    else if (this.hasBot)
                    {
                        this.populateSlots(slotchunks, side, invs[0], false);
                    }

                    int[] ret = new int[slotchunks.size() * 15];
                    int id = 0;
                    Iterator i$1 = slotchunks.iterator();

                    while (i$1.hasNext())
                    {
                        int chunkid = ((Integer)i$1.next()).intValue();

                        for (int i = 0; i < 15; ++i)
                        {
                            ret[id] = chunkid * 15 + i;
                            ++id;
                        }
                    }

                    return ret;
                }
            }
        }

        return new int[0];
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        return true;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {
        return true;
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        IFluidTank[] tanks = this.getTanks(from);
        FluidTankInfo[] infos = new FluidTankInfo[tanks.length];

        for (int i = 0; i < infos.length; ++i)
        {
            infos[i] = new FluidTankInfo(tanks[i].getFluid(), tanks[i].getCapacity());
        }

        return infos;
    }
    
    @Override
	public boolean hasCustomInventoryName() {return false;}
}
