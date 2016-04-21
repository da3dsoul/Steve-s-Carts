package vswe.stevescarts.Helpers;

public class EnchantmentData
{
    private EnchantmentInfo type;
    private long value;
    private int maxLevel = 0;

    public EnchantmentData(EnchantmentInfo type)
    {
        this.type = type;
        this.value = 0;
    }

    public int getMaxLevel() {
        //return 46;
        return Math.max(this.type.getEnchantment().getMaxLevel(), maxLevel);
    }

    public void setMaxLevel(int level) {
        maxLevel = level;
    }

    public long getValue()
    {
        return this.value;
    }

    public void setValue(long val)
    {
        this.value = val;
    }

    public EnchantmentInfo getEnchantment()
    {
        return this.type;
    }

    public void setEnchantment(EnchantmentInfo info)
    {
        this.type = info;
    }

    public void damageEnchant(long dmg)
    {
        this.damageEnchantLevel(dmg, this.getValue(), 1);
    }

    private boolean damageEnchantLevel(long dmg, long value, int level)
    {
        if (level <= getMaxLevel() && value > 0)
        {
            long levelvalue = this.getEnchantment().getValue(level);

            if (!this.damageEnchantLevel(dmg, value - levelvalue, level + 1))
            {
                long dmgdealt = dmg * (int)Math.pow(2.0D, (double)(level - 1));

                if (dmgdealt > value)
                {
                    dmgdealt = value;
                }

                this.setValue(this.getValue() - dmgdealt);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int getLevel()
    {
        long value = this.getValue();

        for (int i = 0; i < getMaxLevel(); ++i)
        {
            if (value <= 0)
            {
                return i;
            }

            value -= this.getEnchantment().getValue(i + 1);
        }

        return getMaxLevel();
    }

    public String getInfoText()
    {
        long value = this.getValue();
        int percentage = 0;

        for (int var5 = 1; var5 <= getMaxLevel(); ++var5)
        {
            if (value > 0)
            {
                long levelvalue = this.getEnchantment().getValue(var5);
                percentage = Math.round(100 * (float) value / (float)levelvalue);
                value -= levelvalue;

                if (value < 0)
                {
                    break;
                }
            }
        }

        return "\u00a7E" + this.getEnchantment().getEnchantment().getTranslatedName(this.getLevel()) + "\n" + percentage + "% left of this tier";
    }
}
