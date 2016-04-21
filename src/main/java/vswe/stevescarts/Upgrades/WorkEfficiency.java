package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;

public class WorkEfficiency extends BaseEffect
{
    private float efficiency;

    public WorkEfficiency(float efficiency)
    {
        this.efficiency = efficiency;
    }

    public String getName()
    {
        return Localization.UPGRADES.EFFICIENCY.translate(new String[] {(this.getPercentage() >= 0 ? "+" : "") + this.getPercentage()});
    }

    private int getPercentage()
    {
        return (int)(this.efficiency * 100.0F);
    }

    public float getEfficiency()
    {
        return this.efficiency;
    }
}
