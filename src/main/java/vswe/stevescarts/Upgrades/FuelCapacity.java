package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;

public class FuelCapacity extends BaseEffect
{
    private int capacity;

    public FuelCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public String getName()
    {
        return Localization.UPGRADES.FUEL_CAPACITY.translate(new String[] {(this.capacity >= 0 ? "+" : "") + this.capacity});
    }

    public int getFuelCapacity()
    {
        return this.capacity;
    }
}
