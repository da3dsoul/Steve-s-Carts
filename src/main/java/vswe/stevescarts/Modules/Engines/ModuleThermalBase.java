package vswe.stevescarts.Modules.Engines;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;

public abstract class ModuleThermalBase extends ModuleEngine
{
    private short coolantLevel;
    private static final int RELOAD_LIQUID_SIZE = 1;

    public ModuleThermalBase(MinecartModular cart)
    {
        super(cart);
    }

    private int getCoolantLevel()
    {
        return this.coolantLevel;
    }

    private void setCoolantLevel(int val)
    {
        this.coolantLevel = (short)val;
    }

    protected void initPriorityButton()
    {
        this.priorityButton = new int[] {72, 17, 16, 16};
    }

    protected abstract int getEfficiency();

    protected abstract int getCoolantEfficiency();

    private boolean requiresCoolant()
    {
        return this.getCoolantEfficiency() > 0;
    }

    public int guiHeight()
    {
        return 40;
    }

    public boolean hasFuel(int consumption)
    {
        return !super.hasFuel(consumption) ? false : !this.requiresCoolant() || this.getCoolantLevel() >= consumption;
    }

    public void consumeFuel(int consumption)
    {
        super.consumeFuel(consumption);
        this.setCoolantLevel(this.getCoolantLevel() - consumption);
    }

    protected void loadFuel()
    {
        int consumption = this.getCart().getConsumption(true) * 2;
        int amount;

        while (this.getFuelLevel() <= consumption)
        {
            amount = this.getCart().drain(FluidRegistry.LAVA, 1, false);

            if (amount <= 0)
            {
                break;
            }

            this.getCart().drain(FluidRegistry.LAVA, amount, true);
            this.setFuelLevel(this.getFuelLevel() + amount * this.getEfficiency());
        }

        while (this.requiresCoolant() && this.getCoolantLevel() <= consumption)
        {
            amount = this.getCart().drain(FluidRegistry.WATER, 1, false);

            if (amount <= 0)
            {
                break;
            }

            this.getCart().drain(FluidRegistry.WATER, amount, true);
            this.setCoolantLevel(this.getCoolantLevel() + amount * this.getCoolantEfficiency());
        }
    }

    public int getTotalFuel()
    {
        int totalfuel = this.getFuelLevel() + this.getCart().drain(FluidRegistry.LAVA, Integer.MAX_VALUE, false) * this.getEfficiency();

        if (this.requiresCoolant())
        {
            int totalcoolant = this.getCoolantLevel() + this.getCart().drain(FluidRegistry.WATER, Integer.MAX_VALUE, false) * this.getCoolantEfficiency();
            return Math.min(totalcoolant, totalfuel);
        }
        else
        {
            return totalfuel;
        }
    }

    public float[] getGuiBarColor()
    {
        return new float[] {1.0F, 0.0F, 0.0F};
    }

    public void smoke() {}

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ENGINES.THERMAL.translate(new String[0]), 8, 6, 4210752);
        int consumption = this.getCart().getConsumption();

        if (consumption == 0)
        {
            consumption = 1;
        }

        String str;

        if (this.getFuelLevel() >= consumption && (!this.requiresCoolant() || this.getCoolantLevel() >= consumption))
        {
            str = Localization.MODULES.ENGINES.POWERED.translate(new String[0]);
        }
        else if (this.getFuelLevel() >= consumption)
        {
            str = Localization.MODULES.ENGINES.NO_WATER.translate(new String[0]);
        }
        else
        {
            str = Localization.MODULES.ENGINES.NO_LAVA.translate(new String[0]);
        }

        this.drawString(gui, str, 8, 22, 4210752);
    }

    public boolean hasSlots()
    {
        return false;
    }

    public int numberOfGuiData()
    {
        return 2;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.getFuelLevel());

        if (this.requiresCoolant())
        {
            this.updateGuiData(info, 1, (short)this.getCoolantLevel());
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.setFuelLevel(data);
        }
        else if (id == 1)
        {
            this.setCoolantLevel(data);
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setShort(this.generateNBTName("Fuel", id), (short)this.getFuelLevel());

        if (this.requiresCoolant())
        {
            tagCompound.setShort(this.generateNBTName("Coolant", id), (short)this.getCoolantLevel());
        }
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.setFuelLevel(tagCompound.getShort(this.generateNBTName("Fuel", id)));

        if (this.requiresCoolant())
        {
            this.setCoolantLevel(tagCompound.getShort(this.generateNBTName("Coolant", id)));
        }
    }
}
