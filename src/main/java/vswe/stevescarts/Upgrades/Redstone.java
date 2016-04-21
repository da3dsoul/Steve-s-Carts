package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Redstone extends BaseEffect
{
    public String getName()
    {
        return Localization.UPGRADES.REDSTONE.translate(new String[0]);
    }

    public void update(TileEntityUpgrade upgrade)
    {
        if (upgrade.getWorldObj().isBlockIndirectlyGettingPowered(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord) && upgrade.getMaster() != null)
        {
            upgrade.getMaster().doAssemble();
        }
    }
}
