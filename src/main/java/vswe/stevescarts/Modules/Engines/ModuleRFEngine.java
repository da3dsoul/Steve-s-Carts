package vswe.stevescarts.Modules.Engines;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ModuleRFEngine extends ModuleEngine implements IEnergyReceiver
{
    public ModuleRFEngine(MinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected void loadFuel() {
        int amount;

        Fluid rf = FluidRegistry.getFluid("redstone");
        if(rf == null) return;

        while (this.getFuelLevel() <= 10000)
        {
            amount = this.getCart().drain(rf, 250, false);

            if (amount < 250)
            {
                break;
            }

            this.getCart().drain(rf, amount, true);
            this.setFuelLevel(this.getFuelLevel() + amount);
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ENGINES.RF.translate(new String[0]), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_POWER.translate(new String[0]);

        if (this.getFuelLevel() > 0)
        {
            strfuel = Localization.MODULES.ENGINES.POWER.translate(new String[] {String.valueOf(this.getFuelLevel())});
        }

        this.drawString(gui, strfuel, 8, 42, 4210752);
    }

    public int getTotalFuel()
    {
        return getFuelLevel();
    }

    public float[] getGuiBarColor()
    {
        return new float[] {1.0F, 1.0F, 0.0F};
    }

    public boolean hasSlots()
    {
        return false;
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.getFuelLevel());
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.setFuelLevel(data);

            if (this.getFuelLevel() < 0)
            {
                this.setFuelLevel(this.getFuelLevel() + 65536);
            }
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setShort(this.generateNBTName("Fuel", id), (short) this.getFuelLevel());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.setFuelLevel(tagCompound.getShort(this.generateNBTName("Fuel", id)));

        if (this.getFuelLevel() < 0)
        {
            this.setFuelLevel(this.getFuelLevel() + 65536);
        }
    }

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
        int fuel = getFuelLevel();
        if(fuel + maxReceive > getMaxEnergyStored(from)) maxReceive = getMaxEnergyStored(from) - fuel;
        if(!simulate) {
            setFuelLevel(fuel + maxReceive);
        }
        return maxReceive;
    }

    /**
     * Returns the amount of energy currently stored.
     *
     * @param from
     */
    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getFuelLevel();
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     *
     * @param from
     */
    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return 50000;
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
