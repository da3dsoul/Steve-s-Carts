package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Solar extends RechargerBase
{
    protected int getAmount(TileEntityUpgrade upgrade)
    {
        return upgrade.yCoord > upgrade.getMaster().yCoord ? 400 : (upgrade.yCoord < upgrade.getMaster().yCoord ? 0 : 240);
    }

    protected boolean canGenerate(TileEntityUpgrade upgrade)
    {
        return upgrade.getWorldObj().getBlockLightValue(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord) == 15 && upgrade.getWorldObj().canBlockSeeTheSky(upgrade.xCoord, upgrade.yCoord + 1, upgrade.zCoord);
    }

    public String getName()
    {
        return Localization.UPGRADES.SOLAR.translate(new String[0]);
    }
}
