package vswe.stevescarts.Modules.Engines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public abstract class ModuleEngine extends ModuleBase
{
    private int fuel;
    protected int[] priorityButton;

    public ModuleEngine(MinecartModular cart)
    {
        super(cart);
        this.initPriorityButton();
    }

    protected void initPriorityButton()
    {
        this.priorityButton = new int[] {78, 7, 16, 16};
    }

    public void update()
    {
        super.update();
        this.loadFuel();
    }

    public boolean hasFuel(int comsumption)
    {
        return this.getFuelLevel() >= comsumption && !this.isDisabled();
    }

    public int getFuelLevel()
    {
        return this.fuel;
    }

    public void setFuelLevel(int val)
    {
        this.fuel = val;
    }

    protected boolean isDisabled()
    {
        return this.getPriority() >= 3 || this.getPriority() < 0;
    }

    public int getPriority()
    {
        if (this.isPlaceholder())
        {
            return 0;
        }
        else
        {
            byte temp = this.getDw(0);

            if (temp < 0 || temp > 3)
            {
                temp = 3;
            }

            return temp;
        }
    }

    private void setPriority(int data)
    {
        if (data < 0)
        {
            data = 0;
        }
        else if (data > 3)
        {
            data = 3;
        }

        this.updateDw(0, data);
    }

    public void consumeFuel(int comsumption)
    {
        this.setFuelLevel(this.getFuelLevel() - comsumption);
    }

    protected abstract void loadFuel();

    public void smoke() {}

    public abstract int getTotalFuel();

    public abstract float[] getGuiBarColor();

    public boolean hasGui()
    {
        return true;
    }

    public int guiWidth()
    {
        return 100;
    }

    public int guiHeight()
    {
        return 50;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/engine.png");
        int sourceX = 16 * this.getPriority();
        byte sourceY = 0;

        if (this.inRect(x, y, this.priorityButton))
        {
            sourceY = 16;
        }

        this.drawImage(gui, this.priorityButton, sourceX, sourceY);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, this.getPriorityText(), x, y, this.priorityButton);
    }

    private String getPriorityText()
    {
        return this.isDisabled() ? Localization.MODULES.ENGINES.ENGINE_DISABLED.translate(new String[0]) : Localization.MODULES.ENGINES.ENGINE_PRIORITY.translate(new String[] {String.valueOf(this.getPriority())});
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.inRect(x, y, this.priorityButton) && (button == 0 || button == 1))
        {
            this.sendPacket(0, (byte)button);
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            int prio = this.getPriority();
            prio += data[0] == 0 ? 1 : -1;
            prio %= 4;

            if (prio < 0)
            {
                prio += 4;
            }

            this.setPriority(prio);
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Priority", id), (byte)this.getPriority());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setPriority(tagCompound.getByte(this.generateNBTName("Priority", id)));
    }
}
