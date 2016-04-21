package vswe.stevescarts.Modules.Engines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;

public abstract class ModuleSolarBase extends ModuleEngine
{
    private int light;
    private boolean maxLight;
    private int panelCoolDown;
    private boolean down = true;
    private boolean upState;

    public ModuleSolarBase(MinecartModular cart)
    {
        super(cart);
    }

    public boolean hasSlots()
    {
        return false;
    }

    public void update()
    {
        super.update();
        this.updateSolarModel();
    }

    protected void loadFuel()
    {
        this.updateLight();
        this.updateDataForModel();
        this.chargeSolar();
    }

    public int getTotalFuel()
    {
        return this.getFuelLevel();
    }

    public float[] getGuiBarColor()
    {
        return new float[] {1.0F, 1.0F, 0.0F};
    }

    private void updateLight()
    {
        this.light = this.getCart().worldObj.getBlockLightValue(this.getCart().x(), this.getCart().y(), this.getCart().z());

        if (this.light == 15 && !this.getCart().worldObj.canBlockSeeTheSky(this.getCart().x(), this.getCart().y() + 1, this.getCart().z()))
        {
            this.light = 14;
        }
    }

    private void updateDataForModel()
    {
        if (this.isPlaceholder())
        {
            this.light = this.getSimInfo().getMaxLight() ? 15 : 14;
        }
        else if (this.getCart().worldObj.isRemote)
        {
            this.light = this.getDw(1);
        }
        else
        {
            this.updateDw(1, (byte)this.light);
        }

        this.maxLight = this.light == 15;

        if (!this.upState && this.light == 15)
        {
            this.light = 14;
        }
    }

    private void chargeSolar()
    {
        if (this.light == 15 && this.getCart().worldObj.rand.nextInt(8) < 4)
        {
            this.setFuelLevel(this.getFuelLevel() + this.getGenSpeed());

            if (this.getFuelLevel() > this.getMaxCapacity())
            {
                this.setFuelLevel(this.getMaxCapacity());
            }
        }
    }

    public int getLight()
    {
        return this.light;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ENGINES.SOLAR.translate(new String[0]), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_POWER.translate(new String[0]);

        if (this.getFuelLevel() > 0)
        {
            strfuel = Localization.MODULES.ENGINES.POWER.translate(new String[] {String.valueOf(this.getFuelLevel())});
        }

        this.drawString(gui, strfuel, 8, 42, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        super.drawBackground(gui, x, y);
        ResourceHelper.bindResource("/gui/solar.png");
        int lightWidth = this.light * 3;

        if (this.light == 15)
        {
            lightWidth += 2;
        }

        this.drawImage(gui, 9, 20, 0, 0, 54, 18);
        this.drawImage(gui, 15, 21, 0, 18, lightWidth, 16);
    }

    public int numberOfDataWatchers()
    {
        return super.numberOfDataWatchers() + 2;
    }

    public void initDw()
    {
        super.initDw();
        this.addDw(1, 0);
        this.addDw(2, 0);
    }

    protected boolean isGoingDown()
    {
        return this.down;
    }

    public void updateSolarModel()
    {
        if (this.getCart().worldObj.isRemote)
        {
            this.updateDataForModel();
        }

        this.panelCoolDown += this.maxLight ? 1 : -1;

        if (this.down && this.panelCoolDown < 0)
        {
            this.panelCoolDown = 0;
        }
        else if (!this.down && this.panelCoolDown > 0)
        {
            this.panelCoolDown = 0;
        }
        else if (Math.abs(this.panelCoolDown) > 20)
        {
            this.panelCoolDown = 0;
            this.down = !this.down;
        }

        this.upState = this.updatePanels();

        if (!this.getCart().worldObj.isRemote)
        {
            this.updateDw(2, this.upState ? 1 : 0);
        }
    }

    public int numberOfGuiData()
    {
        return 2;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)(this.getFuelLevel() & 65535));
        this.updateGuiData(info, 1, (short)(this.getFuelLevel() >> 16 & 65535));
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            int dataint = data;

            if (data < 0)
            {
                dataint = data + 65536;
            }

            this.setFuelLevel(this.getFuelLevel() & -65536 | dataint);
        }
        else if (id == 1)
        {
            this.setFuelLevel(this.getFuelLevel() & 65535 | data << 16);
        }
    }

    protected abstract int getMaxCapacity();

    protected abstract int getGenSpeed();

    protected abstract boolean updatePanels();

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setInteger(this.generateNBTName("Fuel", id), this.getFuelLevel());
        tagCompound.setBoolean(this.generateNBTName("Up", id), this.upState);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.setFuelLevel(tagCompound.getInteger(this.generateNBTName("Fuel", id)));
        this.upState = tagCompound.getBoolean(this.generateNBTName("Up", id));
    }
}
