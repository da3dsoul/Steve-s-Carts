package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerLiquid;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.ITankHolder;
import vswe.stevescarts.Helpers.ManagerTransfer;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiLiquid;
import vswe.stevescarts.Slots.SlotLiquidFilter;
import vswe.stevescarts.Slots.SlotLiquidManagerInput;
import vswe.stevescarts.Slots.SlotLiquidOutput;

public class TileEntityLiquid extends TileEntityManager implements IFluidHandler, ITankHolder, ISidedInventory
{
    Tank[] tanks = new Tank[4];
    private int tick;
    private static final int[] topSlots = new int[] {0, 3, 6, 9};
    private static final int[] botSlots = new int[] {1, 4, 7, 10};
    private static final int[] sideSlots = new int[0];

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiLiquid(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerLiquid(inv, this);
    }

    public TileEntityLiquid()
    {
        for (int i = 0; i < 4; ++i)
        {
            this.tanks[i] = new Tank(this, 32000, i);
        }
    }

    public Tank[] getTanks()
    {
        return this.tanks;
    }

    public void updateEntity()
    {
        super.updateEntity();

        if (this.tick-- <= 0)
        {
            this.tick = 5;

            if (!this.worldObj.isRemote)
            {
                for (int i = 0; i < 4; ++i)
                {
                    this.tanks[i].containerTransfer();
                }
            }
        }
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int amount = 0;

        if (resource != null && resource.amount > 0)
        {
            FluidStack fluid = resource.copy();

            for (int i = 0; i < 4; ++i)
            {
                int tempAmount = this.tanks[i].fill(fluid, doFill, this.worldObj.isRemote);
                amount += tempAmount;
                fluid.amount -= tempAmount;

                if (fluid.amount <= 0)
                {
                    break;
                }
            }
        }

        return amount;
    }

    public int fill(int tankIndex, FluidStack resource, boolean doFill)
    {
        return tankIndex >= 0 && tankIndex < 4 ? this.tanks[tankIndex].fill(resource, doFill, this.worldObj.isRemote) : 0;
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

        for (int i = 0; i < 4; ++i)
        {
            FluidStack temp = this.tanks[i].drain(maxDrain, false, this.worldObj.isRemote);

            if (temp != null && (ret == null || ret.isFluidEqual(temp)))
            {
                temp = this.tanks[i].drain(maxDrain, doDrain, this.worldObj.isRemote);

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

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 12;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.fluidmanager";
    }

    public ItemStack getInputContainer(int tankid)
    {
        return this.getStackInSlot(tankid * 3);
    }

    public void clearInputContainer(int tankid)
    {
        this.setInventorySlotContents(tankid * 3, (ItemStack)null);
    }

    public void addToOutputContainer(int tankid, ItemStack item)
    {
        TransferHandler.TransferItem(item, this, tankid * 3 + 1, tankid * 3 + 1, new ContainerLiquid((IInventory)null, this), Slot.class, (Class)null, -1);
    }

    public void onFluidUpdated(int tankid)
    {
        //this.onInventoryChanged();
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(int tankid, GuiBase gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY)
    {
        gui.drawIcon(icon, gui.getGuiLeft() + targetX, gui.getGuiTop() + targetY, (float)sizeX / 16.0F, (float)sizeY / 16.0F, (float)srcX / 16.0F, (float)srcY / 16.0F);
    }

    protected boolean isTargetValid(ManagerTransfer transfer)
    {
        return true;
    }

    protected boolean doTransfer(ManagerTransfer transfer)
    {
        int maximumToTransfer = this.hasMaxAmount(transfer.getSetting()) ? Math.min(this.getMaxAmount(transfer.getSetting()) - transfer.getWorkload(), 1000) : 1000;
        boolean sucess = false;

        if (this.toCart[transfer.getSetting()])
        {
            for (int cartTanks = 0; cartTanks < this.tanks.length; ++cartTanks)
            {
                int i$ = this.fillTank(transfer.getCart(), cartTanks, transfer.getSetting(), maximumToTransfer, false);

                if (i$ > 0)
                {
                    this.fillTank(transfer.getCart(), cartTanks, transfer.getSetting(), i$, true);
                    sucess = true;

                    if (this.hasMaxAmount(transfer.getSetting()))
                    {
                        transfer.setWorkload(transfer.getWorkload() + i$);
                    }

                    break;
                }
            }
        }
        else
        {
            ArrayList var8 = transfer.getCart().getTanks();
            Iterator var9 = var8.iterator();

            while (var9.hasNext())
            {
                IFluidTank cartTank = (IFluidTank)var9.next();
                int drain = this.drainTank(cartTank, transfer.getSetting(), maximumToTransfer, false);

                if (drain > 0)
                {
                    this.drainTank(cartTank, transfer.getSetting(), drain, true);
                    sucess = true;

                    if (this.hasMaxAmount(transfer.getSetting()))
                    {
                        transfer.setWorkload(transfer.getWorkload() + drain);
                    }

                    break;
                }
            }
        }

        if (sucess && this.hasMaxAmount(transfer.getSetting()) && transfer.getWorkload() == this.getMaxAmount(transfer.getSetting()))
        {
            transfer.setLowestSetting(transfer.getSetting() + 1);
        }

        return sucess;
    }

    private int fillTank(MinecartModular cart, int tankId, int sideId, int fillAmount, boolean doFill)
    {
        if (this.isTankValid(tankId, sideId))
        {
            FluidStack fluidToFill = this.tanks[tankId].drain(fillAmount, doFill);

            if (fluidToFill == null)
            {
                return 0;
            }

            fillAmount = fluidToFill.amount;

            if (this.isFluidValid(sideId, fluidToFill))
            {
                ArrayList cartTanks = cart.getTanks();
                Iterator i$ = cartTanks.iterator();

                do
                {
                    if (!i$.hasNext())
                    {
                        return fillAmount - fluidToFill.amount;
                    }

                    IFluidTank cartTank = (IFluidTank)i$.next();
                    fluidToFill.amount -= cartTank.fill(fluidToFill, doFill);
                }
                while (fluidToFill.amount > 0);

                return fillAmount;
            }
        }

        return 0;
    }

    private int drainTank(IFluidTank cartTank, int sideId, int drainAmount, boolean doDrain)
    {
        FluidStack drainedFluid = cartTank.drain(drainAmount, doDrain);

        if (drainedFluid == null)
        {
            return 0;
        }
        else
        {
            drainAmount = drainedFluid.amount;

            if (this.isFluidValid(sideId, drainedFluid))
            {
                for (int i = 0; i < this.tanks.length; ++i)
                {
                    Tank tank = this.tanks[i];

                    if (this.isTankValid(i, sideId))
                    {
                        drainedFluid.amount -= tank.fill(drainedFluid, doDrain);

                        if (drainedFluid.amount <= 0)
                        {
                            return drainAmount;
                        }
                    }
                }

                return drainAmount - drainedFluid.amount;
            }
            else
            {
                return 0;
            }
        }
    }

    private boolean isTankValid(int tankId, int sideId)
    {
        return (this.layoutType != 1 || tankId == sideId) && (this.layoutType != 2 || this.color[sideId] == this.color[tankId]);
    }

    private boolean isFluidValid(int sideId, FluidStack fluid)
    {
        ItemStack filter = this.getStackInSlot(sideId * 3 + 2);
        FluidStack filterFluid = FluidContainerRegistry.getFluidForFilledItem(filter);
        return filterFluid == null || filterFluid.isFluidEqual(fluid);
    }

    public int getMaxAmount(int id)
    {
        return (int)(this.getMaxAmountBuckets(id) * 1000.0F);
    }

    public float getMaxAmountBuckets(int id)
    {
        switch (this.getAmountId(id))
        {
            case 1:
                return 0.25F;

            case 2:
                return 0.5F;

            case 3:
                return 0.75F;

            case 4:
                return 1.0F;

            case 5:
                return 2.0F;

            case 6:
                return 3.0F;

            case 7:
                return 5.0F;

            case 8:
                return 7.5F;

            case 9:
                return 10.0F;

            case 10:
                return 15.0F;

            default:
                return 0.0F;
        }
    }

    public boolean hasMaxAmount(int id)
    {
        return this.getAmountId(id) != 0;
    }

    public int getAmountCount()
    {
        return 11;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        for (int i = 0; i < 4; ++i)
        {
            this.tanks[i].setFluid(FluidStack.loadFluidStackFromNBT(nbttagcompound.getCompoundTag("Fluid" + i)));
        }

        this.setWorkload(nbttagcompound.getShort("workload"));
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        for (int i = 0; i < 4; ++i)
        {
            if (this.tanks[i].getFluid() != null)
            {
                NBTTagCompound compound = new NBTTagCompound();
                this.tanks[i].getFluid().writeToNBT(compound);
                nbttagcompound.setTag("Fluid" + i, compound);
            }
        }

        nbttagcompound.setShort("workload", (short)this.getWorkload());
    }

    public void checkGuiData(ContainerManager conManager, ICrafting crafting, boolean isNew)
    {
        super.checkGuiData(conManager, crafting, isNew);
        ContainerLiquid con = (ContainerLiquid)conManager;

        for (int i = 0; i < 4; ++i)
        {
            boolean changed = false;
            int id = 4 + i * 4;
            int amount1 = 4 + i * 4 + 1;
            int amount2 = 4 + i * 4 + 2;
            int meta = 4 + i * 4 + 3;

            if ((isNew || con.oldLiquids[i] != null) && this.tanks[i].getFluid() == null)
            {
                this.updateGuiData(con, crafting, id, (short) - 1);
                changed = true;
            }
            else if (this.tanks[i].getFluid() != null)
            {
                if (!isNew && con.oldLiquids[i] != null)
                {
                    if (con.oldLiquids[i].fluidID != this.tanks[i].getFluid().fluidID)
                    {
                        this.updateGuiData(con, crafting, id, (short)this.tanks[i].getFluid().fluidID);
                        changed = true;
                    }

                    if (con.oldLiquids[i].amount != this.tanks[i].getFluid().amount)
                    {
                        this.updateGuiData(con, crafting, amount1, this.getShortFromInt(true, this.tanks[i].getFluid().amount));
                        this.updateGuiData(con, crafting, amount2, this.getShortFromInt(false, this.tanks[i].getFluid().amount));
                        changed = true;
                    }
                }
                else
                {
                    this.updateGuiData(con, crafting, id, (short)this.tanks[i].getFluid().fluidID);
                    this.updateGuiData(con, crafting, amount1, this.getShortFromInt(true, this.tanks[i].getFluid().amount));
                    this.updateGuiData(con, crafting, amount2, this.getShortFromInt(false, this.tanks[i].getFluid().amount));
                    changed = true;
                }
            }

            if (changed)
            {
                if (this.tanks[i].getFluid() == null)
                {
                    con.oldLiquids[i] = null;
                }
                else
                {
                    con.oldLiquids[i] = this.tanks[i].getFluid().copy();
                }
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id > 3)
        {
            id -= 4;
            int tankid = id / 4;
            int contentid = id % 4;

            if (contentid == 0)
            {
                if (data == -1)
                {
                    this.tanks[tankid].setFluid((FluidStack)null);
                }
                else if (this.tanks[tankid].getFluid() == null)
                {
                    this.tanks[tankid].setFluid(new FluidStack(data, 0));
                }
            }
            else if (this.tanks[tankid].getFluid() != null)
            {
                this.tanks[tankid].getFluid().amount = this.getIntFromShort(contentid == 1, this.tanks[tankid].getFluid().amount, data);
            }
        }
        else
        {
            super.receiveGuiData(id, data);
        }
    }

    private boolean isInput(int id)
    {
        return id % 3 == 0;
    }

    private boolean isOutput(int id)
    {
        return id % 3 == 1;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {
        return this.isInput(slotId) ? SlotLiquidManagerInput.isItemStackValid(item, this, -1) : (this.isOutput(slotId) ? SlotLiquidOutput.isItemStackValid(item) : SlotLiquidFilter.isItemStackValid(item));
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 1 ? topSlots : (side == 0 ? botSlots : sideSlots);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        return side == 1 && this.isInput(slot) && this.isItemValidForSlot(slot, item);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return side == 0 && this.isOutput(slot);
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
        FluidTankInfo[] info = new FluidTankInfo[this.tanks.length];

        for (int i = 0; i < this.tanks.length; ++i)
        {
            info[i] = new FluidTankInfo(this.tanks[i].getFluid(), this.tanks[i].getCapacity());
        }

        return info;
    }

	@Override
	public boolean hasCustomInventoryName() {return false;}
}
