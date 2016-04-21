package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Recharger extends RechargerBase
{
    protected int amount;

    public Recharger(int amount)
    {
        this.amount = amount;
    }

    protected int getAmount(TileEntityUpgrade upgrade)
    {
        return this.amount;
    }

    protected boolean canGenerate(TileEntityUpgrade upgrade)
    {
        return true;
    }

    public String getName()
    {
        return Localization.UPGRADES.GENERATOR.translate(new String[] {String.valueOf(this.amount), String.valueOf(this.amount)});
    }
}
