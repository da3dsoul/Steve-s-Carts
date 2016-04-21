package vswe.stevescarts.Modules.Storages.Tanks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Helpers.ITankHolder;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Storages.ModuleStorage;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotLiquidInput;
import vswe.stevescarts.Slots.SlotLiquidOutput;

public abstract class ModuleTank extends ModuleStorage implements IFluidTank, ITankHolder
{
    protected Tank tank = new Tank(this, this.getTankSize(), 0);
    private int tick;
    protected int[] tankBounds = new int[] {35, 20, 36, 51};

    public ModuleTank(MinecartModular cart)
    {
        super(cart);
    }

    protected abstract int getTankSize();

    public boolean hasGui()
    {
        return true;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return (SlotBase)(y == 0 ? new SlotLiquidInput(this.getCart(), this.tank, -1, slotId, 8 + x * 18, 24 + y * 24) : new SlotLiquidOutput(this.getCart(), slotId, 8 + x * 18, 24 + y * 24));
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int getInventoryWidth()
    {
        return 1;
    }

    public int getInventoryHeight()
    {
        return 2;
    }

    public int guiWidth()
    {
        return 100;
    }

    public int guiHeight()
    {
        return 80;
    }

    public boolean hasVisualTank()
    {
        return true;
    }

    public void update()
    {
        super.update();

        if (this.tick-- <= 0)
        {
            this.tick = 5;

            if (!this.getCart().worldObj.isRemote)
            {
                this.tank.containerTransfer();
            }
            else if (!this.isPlaceholder())
            {
                if (this.getShortDw(0) == -1)
                {
                    this.tank.setFluid((FluidStack)null);
                }
                else
                {
                    this.tank.setFluid(new FluidStack(this.getShortDw(0), this.getIntDw(1)));
                }
            }
        }
    }

    public ItemStack getInputContainer(int tankid)
    {
        return this.getStack(0);
    }

    public void clearInputContainer(int tankid)
    {
        this.setStack(0, (ItemStack)null);
    }

    public void addToOutputContainer(int tankid, ItemStack item)
    {
        this.addStack(1, item);
    }

    public void onFluidUpdated(int tankid)
    {
        if (!this.getCart().worldObj.isRemote)
        {
            this.updateDw();
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(int tankid, GuiBase gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY)
    {
        this.drawImage((GuiMinecart)gui, icon, targetX, targetY, srcX, srcY, sizeX, sizeY);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        this.tank.drawFluid(gui, this.tankBounds[0], this.tankBounds[1]);
        ResourceHelper.bindResource("/gui/tank.png");
        this.drawImage(gui, this.tankBounds, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, this.getTankInfo(), x, y, this.tankBounds);
    }

    protected String getTankInfo()
    {
        String str = this.tank.getMouseOver();

        if (this.tank.isLocked())
        {
            str = str + "\n\n" + ColorHelper.GREEN + Localization.MODULES.TANKS.LOCKED.translate(new String[0]) + "\n" + Localization.MODULES.TANKS.UNLOCK.translate(new String[0]);
        }
        else if (this.tank.getFluid() != null)
        {
            str = str + "\n\n" + Localization.MODULES.TANKS.LOCK.translate(new String[0]);
        }

        return str;
    }

    public FluidStack getFluid()
    {
        return this.tank.getFluid() == null ? null : this.tank.getFluid().copy();
    }

    public int getCapacity()
    {
        return this.getTankSize();
    }

    public int fill(FluidStack resource, boolean doFill)
    {
        return this.tank.fill(resource, doFill, this.getCart().worldObj.isRemote);
    }

    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        return this.tank.drain(maxDrain, doDrain, this.getCart().worldObj.isRemote);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        if (this.tank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.tank.getFluid().writeToNBT(compound);
            tagCompound.setTag(this.generateNBTName("Fluid", id), compound);
        }

        tagCompound.setBoolean(this.generateNBTName("Locked", id), this.tank.isLocked());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.tank.setFluid(FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag(this.generateNBTName("Fluid", id))));
        this.tank.setLocked(tagCompound.getBoolean(this.generateNBTName("Locked", id)));
        this.updateDw();
    }

    public int numberOfDataWatchers()
    {
        return 2;
    }

    protected void updateDw()
    {
        this.updateShortDw(0, this.tank.getFluid() == null ? -1 : this.tank.getFluid().fluidID);
        this.updateIntDw(1, this.tank.getFluid() == null ? -1 : this.tank.getFluid().amount);
    }

    public void initDw()
    {
        this.addShortDw(0, this.tank.getFluid() == null ? -1 : this.tank.getFluid().fluidID);
        this.addIntDw(1, this.tank.getFluid() == null ? -1 : this.tank.getFluid().amount);
    }

    public float getFluidRenderHeight()
    {
        return this.tank.getFluid() == null ? 0.0F : (float)this.tank.getFluid().amount / (float)this.getTankSize();
    }

    public boolean isCompletelyFilled()
    {
        return this.getFluid() != null && this.getFluid().amount >= this.getTankSize();
    }

    public boolean isCompletelyEmpty()
    {
        return this.getFluid() == null || this.getFluid().amount == 0;
    }

    public int getFluidAmount()
    {
        return this.getFluid() == null ? 0 : this.getFluid().amount;
    }

    public FluidTankInfo getInfo()
    {
        return new FluidTankInfo(this.getFluid(), this.getCapacity());
    }

    protected int numberOfPackets()
    {
        return 1;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0 && (this.getFluid() != null || this.tank.isLocked()))
        {
            this.tank.setLocked(!this.tank.isLocked());

            if (!this.tank.isLocked() && this.tank.getFluid() != null && this.tank.getFluid().amount <= 0)
            {
                this.tank.setFluid((FluidStack)null);
                this.updateDw();
            }
        }
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)(this.tank.isLocked() ? 1 : 0));
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.tank.setLocked(data != 0);
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.inRect(x, y, this.tankBounds))
        {
            byte data = (byte)button;

            if (GuiScreen.isShiftKeyDown())
            {
                data = (byte)(data | 2);
            }

            this.sendPacket(0, data);
        }
    }
}
