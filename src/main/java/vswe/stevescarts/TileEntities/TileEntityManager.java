package vswe.stevescarts.TileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.ManagerTransfer;
import vswe.stevescarts.Helpers.NBTHelper;

public abstract class TileEntityManager extends TileEntityBase implements IInventory
{
    private ManagerTransfer standardTransferHandler = new ManagerTransfer();
    private ItemStack[] cargoItemStacks = new ItemStack[this.getSizeInventory()];
    public int layoutType;
    public int moveTime = 0;
    public boolean[] toCart = new boolean[] {true, true, true, true};
    public boolean[] doReturn = new boolean[] {false, false, false, false};
    public int[] amount = new int[] {0, 0, 0, 0};
    public int[] color = new int[] {1, 2, 3, 4};

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return this.cargoItemStacks[i];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.cargoItemStacks[i] != null)
        {
            ItemStack itemstack1;

            if (this.cargoItemStacks[i].stackSize <= j)
            {
                itemstack1 = this.cargoItemStacks[i];
                this.cargoItemStacks[i] = null;
                //this.onInventoryChanged();
                return itemstack1;
            }
            else
            {
                itemstack1 = this.cargoItemStacks[i].splitStack(j);

                if (this.cargoItemStacks[i].stackSize == 0)
                {
                    this.cargoItemStacks[i] = null;
                }

                //this.onInventoryChanged();
                return itemstack1;
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
        this.cargoItemStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }

        //this.onInventoryChanged();
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.cargomanager";
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return false;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", NBTHelper.COMPOUND.getId());
        this.cargoItemStacks = new ItemStack[this.getSizeInventory()];

        for (int temp = 0; temp < nbttaglist.tagCount(); ++temp)
        {
            NBTTagCompound temp2 = nbttaglist.getCompoundTagAt(temp);
            byte i = temp2.getByte("Slot");

            if (i >= 0 && i < this.cargoItemStacks.length)
            {
                this.cargoItemStacks[i] = ItemStack.loadItemStackFromNBT(temp2);
            }
        }

        this.moveTime = nbttagcompound.getByte("movetime");
        this.setLowestSetting(nbttagcompound.getByte("lowestNumber"));
        this.layoutType = nbttagcompound.getByte("layout");
        byte var6 = nbttagcompound.getByte("tocart");
        byte var7 = nbttagcompound.getByte("doReturn");

        for (int var8 = 0; var8 < 4; ++var8)
        {
            this.amount[var8] = nbttagcompound.getByte("amount" + var8);
            this.color[var8] = nbttagcompound.getByte("color" + var8);

            if (this.color[var8] == 0)
            {
                this.color[var8] = var8 + 1;
            }

            this.toCart[var8] = (var6 & 1 << var8) != 0;
            this.doReturn[var8] = (var7 & 1 << var8) != 0;
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("movetime", (byte)this.moveTime);
        nbttagcompound.setByte("lowestNumber", (byte)this.getLowestSetting());
        nbttagcompound.setByte("layout", (byte)this.layoutType);
        byte temp = 0;
        byte temp2 = 0;

        for (int nbttaglist = 0; nbttaglist < 4; ++nbttaglist)
        {
            nbttagcompound.setByte("amount" + nbttaglist, (byte)this.amount[nbttaglist]);
            nbttagcompound.setByte("color" + nbttaglist, (byte)this.color[nbttaglist]);

            if (this.toCart[nbttaglist])
            {
                temp = (byte)(temp | 1 << nbttaglist);
            }

            if (this.doReturn[nbttaglist])
            {
                temp2 = (byte)(temp2 | 1 << nbttaglist);
            }
        }

        nbttagcompound.setByte("tocart", temp);
        nbttagcompound.setByte("doReturn", temp2);
        NBTTagList var7 = new NBTTagList();

        for (int i = 0; i < this.cargoItemStacks.length; ++i)
        {
            if (this.cargoItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.cargoItemStacks[i].writeToNBT(nbttagcompound1);
                var7.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", var7);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public MinecartModular getCart()
    {
        return this.standardTransferHandler.getCart();
    }

    public void setCart(MinecartModular cart)
    {
        this.standardTransferHandler.setCart(cart);
    }

    public int getSetting()
    {
        return this.standardTransferHandler.getSetting();
    }

    public void setSetting(int val)
    {
        this.standardTransferHandler.setSetting(val);
    }

    public int getSide()
    {
        return this.standardTransferHandler.getSide();
    }

    public void setSide(int val)
    {
        this.standardTransferHandler.setSide(val);
    }

    public int getLastSetting()
    {
        return this.standardTransferHandler.getLastSetting();
    }

    public void setLastSetting(int val)
    {
        this.standardTransferHandler.setLastSetting(val);
    }

    public int getLowestSetting()
    {
        return this.standardTransferHandler.getLowestSetting();
    }

    public void setLowestSetting(int val)
    {
        this.standardTransferHandler.setLowestSetting(val);
    }

    public int getWorkload()
    {
        return this.standardTransferHandler.getWorkload();
    }

    public void setWorkload(int val)
    {
        this.standardTransferHandler.setWorkload(val);
    }

    public void updateEntity()
    {
        if (this.worldObj.isRemote)
        {
            this.updateLayout();
        }
        else if (this.getCart() != null && !this.getCart().isDead && this.getSide() >= 0 && this.getSide() <= 3 && this.getCart().isDisabled())
        {
            ++this.moveTime;

            if (this.moveTime >= 24)
            {
                this.moveTime = 0;

                if (!this.exchangeItems(this.standardTransferHandler))
                {
                    this.getCart().releaseCart();

                    if (this.doReturn[this.getSide()])
                    {
                        this.getCart().turnback();
                    }
                    this.getCart().returningHome = false;

                    this.standardTransferHandler.reset();
                }
            }
        }
        else
        {
            this.standardTransferHandler.reset();
        }
    }

    public boolean exchangeItems(ManagerTransfer transfer)
    {
        transfer.setSetting(transfer.getLowestSetting());

        while (true)
        {
            if (transfer.getSetting() >= 4)
            {
                return false;
            }

            if (this.color[transfer.getSetting()] - 1 == transfer.getSide())
            {
                transfer.setLowestSetting(transfer.getSetting());

                if (transfer.getLastSetting() != transfer.getSetting())
                {
                    transfer.setWorkload(0);
                    transfer.setLastSetting(transfer.getSetting());
                    return true;
                }

                if (this.toCart[transfer.getSetting()])
                {
                    if (!transfer.getToCartEnabled())
                    {
                        break;
                    }
                }
                else if (!transfer.getFromCartEnabled())
                {
                    break;
                }

                if (!this.isTargetValid(transfer))
                {
                    break;
                }

                if (this.doTransfer(transfer))
                {
                    return true;
                }
            }

            transfer.setSetting(transfer.getSetting() + 1);
        }

        transfer.setLowestSetting(transfer.getSetting() + 1);
        return true;
    }

    public void sendPacket(int id)
    {
        this.sendPacket(id, new byte[0]);
    }

    public void sendPacket(int id, byte data)
    {
        this.sendPacket(id, new byte[] {data});
    }

    public void sendPacket(int id, byte[] data)
    {
        PacketHandler.sendPacket(id, data);
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        byte railsAndDifferenceCombined;

        if (id == 0)
        {
            railsAndDifferenceCombined = data[0];
            this.toCart[railsAndDifferenceCombined] = !this.toCart[railsAndDifferenceCombined];

            if (this.color[railsAndDifferenceCombined] - 1 == this.getSide())
            {
                this.reset();
            }
        }
        else if (id == 4)
        {
            railsAndDifferenceCombined = data[0];

            if (this.color[railsAndDifferenceCombined] != 5)
            {
                this.doReturn[this.color[railsAndDifferenceCombined] - 1] = !this.doReturn[this.color[railsAndDifferenceCombined] - 1];
            }
        }
        else if (id == 5)
        {
            railsAndDifferenceCombined = data[0];
            this.layoutType += railsAndDifferenceCombined;

            if (this.layoutType > 2)
            {
                this.layoutType = 0;
            }
            else if (this.layoutType < 0)
            {
                this.layoutType = 2;
            }

            this.reset();
        }
        else
        {
            railsAndDifferenceCombined = data[0];
            int railID = railsAndDifferenceCombined & 3;
            int k = (railsAndDifferenceCombined & 4) >> 2;
            byte difference;

            if (k == 0)
            {
                difference = 1;
            }
            else
            {
                difference = -1;
            }

            if (id == 2)
            {
                this.amount[railID] += difference;

                if (this.amount[railID] >= this.getAmountCount())
                {
                    this.amount[railID] = 0;
                }
                else if (this.amount[railID] < 0)
                {
                    this.amount[railID] = this.getAmountCount() - 1;
                }

                if (this.color[railID] - 1 == this.getSide())
                {
                    this.reset();
                }
            }
            else if (id == 3)
            {
                if (this.color[railID] != 5)
                {
                    boolean willStillExist = false;

                    for (int side = 0; side < 4; ++side)
                    {
                        if (side != railID && this.color[railID] == this.color[side])
                        {
                            willStillExist = true;
                            break;
                        }
                    }

                    if (!willStillExist)
                    {
                        this.doReturn[this.color[railID] - 1] = false;
                    }
                }

                this.color[railID] += difference;

                if (this.color[railID] > 5)
                {
                    this.color[railID] = 1;
                }
                else if (this.color[railID] < 1)
                {
                    this.color[railID] = 5;
                }

                if (this.color[railID] - 1 == this.getSide())
                {
                    this.reset();
                }
            }
            else
            {
                this.receiveClickData(id, railID, difference);
            }
        }
    }

    public void initGuiData(Container con, ICrafting crafting)
    {
        this.checkGuiData((ContainerManager)con, crafting, true);
    }

    public void checkGuiData(Container con, ICrafting crafting)
    {
        this.checkGuiData((ContainerManager)con, crafting, false);
    }

    public void checkGuiData(ContainerManager con, ICrafting crafting, boolean isNew)
    {
        short header = (short)(this.moveTime & 31);
        header = (short)(header | (this.layoutType & 3) << 5);
        int colorShort;

        for (colorShort = 0; colorShort < 4; ++colorShort)
        {
            header = (short)(header | (this.toCart[colorShort] ? 1 : 0) << 7 + colorShort);
        }

        for (colorShort = 0; colorShort < 4; ++colorShort)
        {
            header = (short)(header | (this.doReturn[colorShort] ? 1 : 0) << 11 + colorShort);
        }

        if (isNew || con.lastHeader != header)
        {
            this.updateGuiData(con, crafting, 0, header);
            con.lastHeader = header;
        }

        short var8 = 0;

        for (int amountShort = 0; amountShort < 4; ++amountShort)
        {
            var8 = (short)(var8 | (this.color[amountShort] & 7) << amountShort * 3);
        }

        var8 = (short)(var8 | (this.getLastSetting() & 7) << 12);

        if (isNew || con.lastColor != var8)
        {
            this.updateGuiData(con, crafting, 1, var8);
            con.lastColor = var8;
        }

        short var9 = 0;

        for (int i = 0; i < 4; ++i)
        {
            var9 = (short)(var9 | (this.amount[i] & 15) << i * 4);
        }

        if (isNew || con.lastAmount != var9)
        {
            this.updateGuiData(con, crafting, 3, var9);
            con.lastAmount = var9;
        }
    }

    public void receiveGuiData(int id, short data)
    {
        int i;

        if (id == 0)
        {
            this.moveTime = data & 31;
            this.layoutType = (data & 96) >> 5;
            this.updateLayout();

            for (i = 0; i < 4; ++i)
            {
                this.toCart[i] = (data & 1 << 7 + i) != 0;
            }

            for (i = 0; i < 4; ++i)
            {
                this.doReturn[i] = (data & 1 << 11 + i) != 0;
            }
        }
        else if (id == 1)
        {
            for (i = 0; i < 4; ++i)
            {
                this.color[i] = (data & 7 << i * 3) >> i * 3;
            }

            this.setLastSetting((data & 28672) >> 12);
        }
        else if (id == 3)
        {
            for (i = 0; i < 4; ++i)
            {
                this.amount[i] = (data & 15 << i * 4) >> i * 4;
            }
        }
    }

    public int moveProgressScaled(int i)
    {
        return this.moveTime * i / 24;
    }

    public void closeInventory() {}

    public void openInventory() {}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.cargoItemStacks[par1] != null)
        {
            ItemStack var2 = this.cargoItemStacks[par1];
            this.cargoItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    protected void updateLayout() {}

    protected void receiveClickData(int packetid, int id, int dif) {}

    protected abstract boolean isTargetValid(ManagerTransfer var1);

    protected abstract boolean doTransfer(ManagerTransfer var1);

    public abstract int getAmountCount();

    protected void reset()
    {
        this.moveTime = 0;
        this.setWorkload(0);
    }

    protected int getAmountId(int id)
    {
        return this.amount[id];
    }
}
