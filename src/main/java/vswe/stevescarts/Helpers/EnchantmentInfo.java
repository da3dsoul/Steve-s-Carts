package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class EnchantmentInfo
{
    private Enchantment enchantment;
    private int rank1Value;
    private EnchantmentInfo.ENCHANTMENT_TYPE type;
    public static ArrayList<EnchantmentInfo> enchants = new ArrayList();
    public static EnchantmentInfo fortune = new EnchantmentInfo(Enchantment.fortune, EnchantmentInfo.ENCHANTMENT_TYPE.TOOL, 50000);
    public static EnchantmentInfo efficiency = new EnchantmentInfo(Enchantment.efficiency, EnchantmentInfo.ENCHANTMENT_TYPE.TOOL, 50000);
    public static EnchantmentInfo unbreaking = new EnchantmentInfo(Enchantment.unbreaking, EnchantmentInfo.ENCHANTMENT_TYPE.TOOL, 64000);
    public static EnchantmentInfo power = new EnchantmentInfo(Enchantment.power, EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER, 750);
    public static EnchantmentInfo punch = new EnchantmentInfo(Enchantment.punch, EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER, 1000);
    public static EnchantmentInfo flame = new EnchantmentInfo(Enchantment.flame, EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER, 1000);
    public static EnchantmentInfo infinity = new EnchantmentInfo(Enchantment.infinity, EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER, 500);

    public EnchantmentInfo(Enchantment enchantment, EnchantmentInfo.ENCHANTMENT_TYPE type, int rank1Value)
    {
        this.enchantment = enchantment;
        this.rank1Value = rank1Value;
        this.type = type;
        enchants.add(this);
    }

    public Enchantment getEnchantment()
    {
        return this.enchantment;
    }

    public long getMaxValue(int maxLevel) {
        long max = 0;

        for (int i = 0; i < Math.max(this.getEnchantment().getMaxLevel(), maxLevel); ++i)
        {
            max += this.getValue(i + 1);
        }

        return max;
    }

    public long getMaxValue()
    {
        return getMaxValue(0);
    }

    public long getValue(int level)
    {
        return (long)Math.pow(2.0D, (double)(level - 1)) * this.rank1Value;
    }

    public static boolean isItemValid(ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes, ItemStack itemstack)
    {
        if (itemstack != null && itemstack.getItem() == Items.enchanted_book)
        {
            Iterator i$ = enchants.iterator();

            while (i$.hasNext())
            {
                EnchantmentInfo info = (EnchantmentInfo)i$.next();
                boolean isValid = false;
                Iterator level = enabledTypes.iterator();

                while (level.hasNext())
                {
                    EnchantmentInfo.ENCHANTMENT_TYPE type = (EnchantmentInfo.ENCHANTMENT_TYPE)level.next();

                    if (info.type == type)
                    {
                        isValid = true;
                    }
                }

                if (isValid)
                {
                    int level1 = getEnchantmentLevel(info.getEnchantment().effectId, itemstack);

                    if (level1 > 0)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static EnchantmentData addBook(ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, ItemStack itemstack)
    {
        if (itemstack != null && itemstack.getItem() == Items.enchanted_book)
        {
            EnchantmentInfo info;

            if (data == null)
            {
                for (Iterator i$ = enchants.iterator(); i$.hasNext(); data = addEnchantment(enabledTypes, data, itemstack, info))
                {
                    info = (EnchantmentInfo)i$.next();
                }
            }
            else
            {
                addEnchantment(enabledTypes, data, itemstack, data.getEnchantment());
            }
        }

        return data;
    }

    private static EnchantmentData addEnchantment(ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, ItemStack itemstack, EnchantmentInfo info)
    {
        boolean isValid = false;
        Iterator level = enabledTypes.iterator();

        while (level.hasNext())
        {
            EnchantmentInfo.ENCHANTMENT_TYPE newValue = (EnchantmentInfo.ENCHANTMENT_TYPE)level.next();

            if (info.type == newValue)
            {
                isValid = true;
            }
        }

        if (isValid)
        {
            int var7 = getEnchantmentLevel(info.getEnchantment().effectId, itemstack);

            if (var7 > 0)
            {
                if (data == null)
                {
                    data = new EnchantmentData(info);
                }

                if(var7 > data.getMaxLevel()) {
                    data.setMaxLevel(var7);
                }

                long var8 = data.getEnchantment().getValue(var7) + data.getValue();

                if (var8 <= data.getEnchantment().getMaxValue(var7))
                {
                    data.setValue(var8);
                    --itemstack.stackSize;
                }
            }
        }

        return data;
    }

    private static int getEnchantmentLevel(int par0, ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return 0;
        }
        else
        {
            NBTTagList nbttaglist = Items.enchanted_book.func_92110_g(par1ItemStack);

            if (nbttaglist == null)
            {
                return 0;
            }
            else
            {
                for (int j = 0; j < nbttaglist.tagCount(); ++j)
                {
                    short short1 = nbttaglist.getCompoundTagAt(j).getShort("id");
                    short short2 = nbttaglist.getCompoundTagAt(j).getShort("lvl");

                    if (short1 == par0)
                    {
                        return short2;
                    }
                }

                return 0;
            }
        }
    }

    public static EnchantmentData createDataFromEffectId(EnchantmentData data, short id)
    {
        Iterator i$ = enchants.iterator();

        while (i$.hasNext())
        {
            EnchantmentInfo info = (EnchantmentInfo)i$.next();

            if (info.getEnchantment().effectId == id)
            {
                if (data == null)
                {
                    data = new EnchantmentData(info);
                }
                else
                {
                    data.setEnchantment(info);
                }

                break;
            }
        }

        return data;
    }

    public EnchantmentInfo.ENCHANTMENT_TYPE getType()
    {
        return this.type;
    }

    public static enum ENCHANTMENT_TYPE
    {
        TOOL("TOOL", 0),
        SHOOTER("SHOOTER", 1);

        private static final EnchantmentInfo.ENCHANTMENT_TYPE[] $VALUES = new EnchantmentInfo.ENCHANTMENT_TYPE[]{TOOL, SHOOTER};

        private ENCHANTMENT_TYPE(String var1, int var2) {}
    }
}
