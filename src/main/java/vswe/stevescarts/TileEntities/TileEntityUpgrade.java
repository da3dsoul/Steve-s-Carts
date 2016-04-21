package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.ITankHolder;
import vswe.stevescarts.Helpers.NBTHelper;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiUpgrade;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;
import vswe.stevescarts.Upgrades.InterfaceEffect;
import vswe.stevescarts.Upgrades.InventoryEffect;

public class TileEntityUpgrade extends TileEntityBase implements IInventory, ISidedInventory, IFluidHandler, IFluidTank, ITankHolder
{
    public Tank tank;
    private TileEntityCartAssembler master;
    private int type;
    private boolean initialized;
    private NBTTagCompound comp;
    ItemStack[] inventoryStacks;
    private int[] slotsForSide;

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiUpgrade(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerUpgrade(inv, this);
    }

    public void setMaster(TileEntityCartAssembler master)
    {
        if (this.worldObj.isRemote && this.master != master)
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        this.master = master;
    }

    public TileEntityCartAssembler getMaster()
    {
        return this.master;
    }

    public void setType(int type)
    {
        this.type = type;

        if (!this.initialized)
        {
            this.initialized = true;
            AssemblerUpgrade upgrade = this.getUpgrade();

            if (upgrade != null)
            {
                this.comp = new NBTTagCompound();
                this.slotsForSide = new int[upgrade.getInventorySize()];
                upgrade.init(this);

                if (upgrade.getInventorySize() > 0)
                {
                    this.inventoryStacks = new ItemStack[upgrade.getInventorySize()];

                    for (int i = 0; i < this.slotsForSide.length; this.slotsForSide[i] = i++)
                    {
                        ;
                    }
                }
            }
            else
            {
                this.inventoryStacks = null;
            }
        }
    }

    public int getType()
    {
        return this.type;
    }

    public NBTTagCompound getCompound()
    {
        return this.comp;
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }

    public AssemblerUpgrade getUpgrade()
    {
        return AssemblerUpgrade.getUpgrade(this.type);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getTexture(boolean outside)
    {
        return this.getUpgrade() == null ? null : (outside ? this.getUpgrade().getMainTexture() : this.getUpgrade().getSideTexture());
    }

    public boolean hasInventory()
    {
        return this.inventoryStacks != null;
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        this.setType(tagCompound.getByte("Type"));
        NBTTagList items = tagCompound.getTagList("Items", NBTHelper.COMPOUND.getId());

        for (int upgrade = 0; upgrade < items.tagCount(); ++upgrade)
        {
            NBTTagCompound item = items.getCompoundTagAt(upgrade);
            int slot = item.getByte("Slot") & 255;
            ItemStack iStack = ItemStack.loadItemStackFromNBT(item);

            if (slot >= 0 && slot < this.getSizeInventory())
            {
                this.setInventorySlotContents(slot, iStack);
            }
        }

        AssemblerUpgrade var7 = this.getUpgrade();

        if (var7 != null)
        {
            var7.load(this, tagCompound);
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        NBTTagList items = new NBTTagList();

        if (this.inventoryStacks != null)
        {
            for (int upgrade = 0; upgrade < this.inventoryStacks.length; ++upgrade)
            {
                ItemStack iStack = this.inventoryStacks[upgrade];

                if (iStack != null)
                {
                    NBTTagCompound item = new NBTTagCompound();
                    item.setByte("Slot", (byte)upgrade);
                    iStack.writeToNBT(item);
                    items.appendTag(item);
                }
            }
        }

        tagCompound.setTag("Items", items);
        tagCompound.setByte("Type", (byte)this.type);
        AssemblerUpgrade var6 = this.getUpgrade();

        if (var6 != null)
        {
            var6.save(this, tagCompound);
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void updateEntity()
    {
        if (this.getUpgrade() != null && this.getMaster() != null)
        {
            this.getUpgrade().update(this);
        }
    }

    public void initGuiData(Container con, ICrafting crafting)
    {
        if (this.getUpgrade() != null)
        {
            InterfaceEffect gui = this.getUpgrade().getInterfaceEffect();

            if (gui != null)
            {
                gui.checkGuiData(this, (ContainerUpgrade)con, crafting, true);
            }
        }
    }

    public void checkGuiData(Container con, ICrafting crafting)
    {
        if (this.getUpgrade() != null)
        {
            InterfaceEffect gui = this.getUpgrade().getInterfaceEffect();

            if (gui != null)
            {
                gui.checkGuiData(this, (ContainerUpgrade)con, crafting, false);
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (this.getUpgrade() != null)
        {
            InterfaceEffect gui = this.getUpgrade().getInterfaceEffect();

            if (gui != null)
            {
                gui.receiveGuiData(this, id, data);
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.inventoryStacks == null ? (this.master == null ? 0 : this.master.getSizeInventory()) : this.inventoryStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return this.inventoryStacks == null ? (this.master == null ? null : this.master.getStackInSlot(i)) : (i >= 0 && i < this.getSizeInventory() ? this.inventoryStacks[i] : null);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.inventoryStacks == null)
        {
            return this.master == null ? null : this.master.decrStackSize(i, j);
        }
        else if (i >= 0 && i < this.getSizeInventory())
        {
            if (this.inventoryStacks[i] != null)
            {
                ItemStack itemstack1;

                if (this.inventoryStacks[i].stackSize <= j)
                {
                    itemstack1 = this.inventoryStacks[i];
                    this.inventoryStacks[i] = null;
                    this.onInventoryChanged();
                    return itemstack1;
                }
                else
                {
                    itemstack1 = this.inventoryStacks[i].splitStack(j);

                    if (this.inventoryStacks[i].stackSize == 0)
                    {
                        this.inventoryStacks[i] = null;
                    }

                    this.onInventoryChanged();
                    return itemstack1;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (this.inventoryStacks == null)
        {
            if (this.master != null)
            {
                this.master.setInventorySlotContents(i, itemstack);
            }
        }
        else
        {
            if (i < 0 || i >= this.getSizeInventory())
            {
                return;
            }

            this.inventoryStacks[i] = itemstack;

            if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
            {
                itemstack.stackSize = this.getInventoryStackLimit();
            }

            this.onInventoryChanged();
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.assemblerupgrade";
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
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.inventoryStacks == null)
        {
            return this.master == null ? null : this.master.getStackInSlotOnClosing(i);
        }
        else
        {
            ItemStack item = this.getStackInSlot(i);

            if (item != null)
            {
                this.setInventorySlotContents(i, (ItemStack)null);
                return item;
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        if (this.getUpgrade() != null)
        {
            InventoryEffect inv = this.getUpgrade().getInventoryEffect();

            if (inv != null)
            {
                inv.onInventoryChanged(this);
            }
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if (this.getUpgrade() != null)
        {
            InventoryEffect inv = this.getUpgrade().getInventoryEffect();

            if (inv != null)
            {
                return inv.isItemValid(slot, item);
            }
        }

        return this.getMaster() != null ? this.getMaster().isItemValidForSlot(slot, item) : false;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (this.getUpgrade() != null)
        {
            InventoryEffect inv = this.getUpgrade().getInventoryEffect();

            if (inv != null)
            {
                return this.slotsForSide;
            }
        }

        return this.getMaster() != null ? this.getMaster().getAccessibleSlotsFromSide(side) : new int[0];
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        if (this.getUpgrade() != null)
        {
            InventoryEffect inv = this.getUpgrade().getInventoryEffect();

            if (inv != null)
            {
                return this.isItemValidForSlot(slot, item);
            }
        }

        return this.getMaster() != null ? this.getMaster().canInsertItem(slot, item, side) : false;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        if (this.getUpgrade() != null)
        {
            InventoryEffect inv = this.getUpgrade().getInventoryEffect();

            if (inv != null)
            {
                return true;
            }
        }

        return this.getMaster() != null ? this.getMaster().canExtractItem(slot, item, side) : false;
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return this.fill(resource, doFill);
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return resource != null && resource.isFluidEqual(this.getFluid()) ? this.drain(from, resource.amount, doDrain) : null;
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.drain(maxDrain, doDrain);
    }

    public FluidStack getFluid()
    {
        return this.tank == null ? null : this.tank.getFluid();
    }

    public int getCapacity()
    {
        return this.tank == null ? 0 : this.tank.getCapacity();
    }

    public int fill(FluidStack resource, boolean doFill)
    {
        if (this.tank == null)
        {
            return 0;
        }
        else
        {
            int result = this.tank.fill(resource, doFill);
            return result;
        }
    }

    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (this.tank == null)
        {
            return null;
        }
        else
        {
            FluidStack result = this.tank.drain(maxDrain, doDrain);
            return result;
        }
    }

    public ItemStack getInputContainer(int tankid)
    {
        return this.getStackInSlot(0);
    }

    public void clearInputContainer(int tankid)
    {
        this.setInventorySlotContents(0, (ItemStack)null);
    }

    public void addToOutputContainer(int tankid, ItemStack item)
    {
        TransferHandler.TransferItem(item, this, 1, 1, new ContainerUpgrade((IInventory)null, this), Slot.class, (Class)null, -1);
    }

    public void onFluidUpdated(int tankid) {}

    @SideOnly(Side.CLIENT)
    public void drawImage(int tankid, GuiBase gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY)
    {
        gui.drawIcon(icon, gui.getGuiLeft() + targetX, gui.getGuiTop() + targetY, (float)sizeX / 16.0F, (float)sizeY / 16.0F, (float)srcX / 16.0F, (float)srcY / 16.0F);
    }

    public int getFluidAmount()
    {
        return this.tank == null ? 0 : this.tank.getFluidAmount();
    }

    public FluidTankInfo getInfo()
    {
        return this.tank == null ? null : this.tank.getInfo();
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
        return new FluidTankInfo[] {this.getInfo()};
    }

	@Override
	public boolean hasCustomInventoryName() {return false;}
}
