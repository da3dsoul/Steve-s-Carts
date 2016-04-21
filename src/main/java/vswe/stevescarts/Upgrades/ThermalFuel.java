package vswe.stevescarts.Upgrades;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class ThermalFuel extends TankEffect
{
    public static final int LAVA_EFFICIENCY = 3;

    public int getTankSize()
    {
        return 12000;
    }

    public String getName()
    {
        return Localization.UPGRADES.THERMAL.translate(new String[0]);
    }

    public void update(TileEntityUpgrade upgrade)
    {
        super.update(upgrade);

        if (!upgrade.getWorldObj().isRemote && upgrade.getMaster() != null && upgrade.getFluid() != null && upgrade.getFluid().getFluid().equals(FluidRegistry.LAVA))
        {
            int fuelspace = upgrade.getMaster().getMaxFuelLevel() - upgrade.getMaster().getFuelLevel();
            int unitspace = Math.min(fuelspace / 3, 200);

            if (unitspace > 100)
            {
                FluidStack drain = upgrade.drain(unitspace, false);

                if (drain != null && drain.amount > 0)
                {
                    upgrade.getMaster().setFuelLevel(upgrade.getMaster().getFuelLevel() + drain.amount * 3);
                    upgrade.drain(unitspace, true);
                }
            }
        }
    }
}
