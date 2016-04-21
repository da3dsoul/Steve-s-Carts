package vswe.stevescarts.Upgrades;

import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public abstract class RechargerBase extends BaseEffect
{
    public void update(TileEntityUpgrade upgrade)
    {
        if (!upgrade.getWorldObj().isRemote && this.canGenerate(upgrade))
        {
            NBTTagCompound comp = upgrade.getCompound();

            if (comp.getShort("GenerateCooldown") >= 1200 / this.getAmount(upgrade))
            {
                comp.setShort("GenerateCooldown", (short)0);
                upgrade.getMaster().increaseFuel(1);
            }
            else
            {
                comp.setShort("GenerateCooldown", (short)(comp.getShort("GenerateCooldown") + 1));
            }
        }
    }

    protected abstract boolean canGenerate(TileEntityUpgrade var1);

    protected abstract int getAmount(TileEntityUpgrade var1);

    public void init(TileEntityUpgrade upgrade)
    {
        upgrade.getCompound().setShort("GenerateCooldown", (short)0);
    }
}
