package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;

public class TimeFlatRemoved extends TimeFlat
{
    public TimeFlatRemoved(int ticks)
    {
        super(ticks);
    }

    public String getName()
    {
        return Localization.UPGRADES.FLAT_REMOVED.translate(new String[] {(this.getSeconds() >= 0 ? "+" : "") + this.getSeconds(), String.valueOf(this.getSeconds())});
    }
}
