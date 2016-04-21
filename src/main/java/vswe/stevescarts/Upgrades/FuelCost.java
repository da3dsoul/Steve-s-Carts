package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;

public class FuelCost extends BaseEffect
{
    private float cost;

    public FuelCost(float cost)
    {
        this.cost = cost;
    }

    public String getName()
    {
        return Localization.UPGRADES.FUEL_COST.translate(new String[] {(this.getPercentage() >= 0 ? "+" : "") + this.getPercentage()});
    }

    private int getPercentage()
    {
        return (int)(this.cost * 100.0F);
    }

    public float getCost()
    {
        return this.cost;
    }
}
