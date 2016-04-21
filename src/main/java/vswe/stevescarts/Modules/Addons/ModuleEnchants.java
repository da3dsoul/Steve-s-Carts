package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.*;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotEnchantment;
import vswe.stevescarts.StevesCarts;

public class ModuleEnchants extends ModuleAddon
{
    private EnchantmentData[] enchants = new EnchantmentData[3];
    private ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes = new ArrayList();

    public ModuleEnchants(MinecartModular cart)
    {
        super(cart);
    }

    public int getFortuneLevel()
    {
        return this.useSilkTouch() ? 0 : this.getEnchantLevel(EnchantmentInfo.fortune);
    }

    public boolean useSilkTouch()
    {
        return false;
    }

    public int getUnbreakingLevel()
    {
        return this.getEnchantLevel(EnchantmentInfo.unbreaking);
    }

    public int getEfficiencyLevel()
    {
        return this.getEnchantLevel(EnchantmentInfo.efficiency);
    }

    public int getPowerLevel()
    {
        return this.getEnchantLevel(EnchantmentInfo.power);
    }

    public int getPunchLevel()
    {
        return this.getEnchantLevel(EnchantmentInfo.punch);
    }

    public boolean useFlame()
    {
        return this.getEnchantLevel(EnchantmentInfo.flame) > 0;
    }

    public boolean useInfinity()
    {
        return this.getEnchantLevel(EnchantmentInfo.infinity) > 0;
    }

    public boolean hasGui()
    {
        return true;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    protected int getInventoryWidth()
    {
        return 1;
    }

    protected int getInventoryHeight()
    {
        return 3;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotEnchantment(this.getCart(), this.enabledTypes, slotId, 8, 14 + y * 20);
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (this.getStack(i) != null && this.getStack(i).stackSize > 0)
                {
                    int stacksize = this.getStack(i).stackSize;
                    this.enchants[i] = EnchantmentInfo.addBook(this.enabledTypes, this.enchants[i], this.getStack(i));

                    if (this.getStack(i).stackSize != stacksize)
                    {
                        boolean valid = true;

                        for (int j = 0; j < 3; ++j)
                        {
                            if (i != j && this.enchants[i] != null && this.enchants[j] != null && this.enchants[i].getEnchantment() == this.enchants[j].getEnchantment())
                            {
                                this.enchants[i] = null;
                                ++this.getStack(i).stackSize;
                                valid = false;
                                break;
                            }
                        }

                        if (valid && this.getStack(i).stackSize <= 0)
                        {
                            this.setStack(i, (ItemStack)null);
                        }
                    }
                }
            }
        }
    }

    public void damageEnchant(EnchantmentInfo.ENCHANTMENT_TYPE type, int dmg)
    {
        for (int i = 0; i < 3; ++i)
        {
            if (this.enchants[i] != null && this.enchants[i].getEnchantment().getType() == type)
            {
                this.enchants[i].damageEnchant(dmg);

                if (this.enchants[i].getValue() <= 0)
                {
                    this.enchants[i] = null;
                }
            }
        }
    }

    private int getEnchantLevel(EnchantmentInfo info)
    {
        if (info != null)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (this.enchants[i] != null && this.enchants[i].getEnchantment() == info)
                {
                    return this.enchants[i].getLevel();
                }
            }
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/enchant.png");

        for (int i = 0; i < 3; ++i)
        {
            int[] box = this.getBoxRect(i);

            if (this.inRect(x, y, box))
            {
                this.drawImage(gui, box, 65, 0);
            }
            else
            {
                this.drawImage(gui, box, 0, 0);
            }

            EnchantmentData data = this.enchants[i];

            if (data != null)
            {
                int maxlevel = data.getMaxLevel();
                long value = data.getValue();


                for (int j = 0; j < maxlevel; ++j)
                {
                    int m = (int)Math.round(((double)j / ((double)maxlevel - 1)) * data.getEnchantment().getEnchantment().getMaxLevel());
                    int[] bar = this.getBarRect(i, j, maxlevel);

                    if (j != maxlevel - 1)
                    {
                        this.drawImage(gui, bar[0] + bar[2], bar[1], 61 + m, 1, 1, bar[3]);
                    }

                    long levelmaxvalue = data.getEnchantment().getValue(j + 1);

                    if (value > 0)
                    {
                        float mult = (float)value / (float)levelmaxvalue;

                        if (mult > 1.0F)
                        {
                            mult = 1.0F;
                        }

                        bar[2] = (int)((float)bar[2] * mult);
                        this.drawImage(gui, bar, 1, 13 + 11 * m);
                    }

                    value -= levelmaxvalue;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        for (int i = 0; i < 3; ++i)
        {
            EnchantmentData data = this.enchants[i];
            String str;

            if (data != null)
            {
                str = data.getInfoText();
            }
            else
            {
                str = Localization.MODULES.ADDONS.ENCHANT_INSTRUCTION.translate(new String[0]);
            }

            this.drawStringOnMouseOver(gui, str, x, y, this.getBoxRect(i));
        }
    }

    private int[] getBoxRect(int id)
    {
        return new int[] {40, 17 + id * 20, 61, 12};
    }

    private int[] getBarRect(int id, int barid, int maxlevel)
    {
        int width = (59 - (maxlevel - 1)) / maxlevel;
        return new int[] {41 + (width + 1) * barid, 18 + id * 20, width, 10};
    }

    public int numberOfGuiData()
    {
        return 18;
    }
    /*
    2305847408333946881
    8192 << 48
    1024 << 32
    16384 << 16
    1

     */

    protected void checkGuiData(Object[] info)
    {
        for (int i = 0; i < 3; ++i)
        {
            EnchantmentData data = this.enchants[i];

            if (data == null)
            {
                this.updateGuiData(info, i * numberOfGuiData() / 3 + 0, (short) - 1);
            }
            else
            {
                this.updateGuiData(info, i * (numberOfGuiData() / 3) + 0, (short)data.getEnchantment().getEnchantment().effectId);
                this.updateGuiData(info, i * (numberOfGuiData() / 3) + 1, (short)(data.getMaxLevel()));
                this.updateGuiData(info, i * (numberOfGuiData() / 3) + 2, (short)(data.getValue() & 65535));
                this.updateGuiData(info, i * (numberOfGuiData() / 3) + 3, (short)(data.getValue() >>> 16));
                this.updateGuiData(info, i * (numberOfGuiData() / 3) + 4, (short)(data.getValue() >>> 32));
                this.updateGuiData(info, i * (numberOfGuiData() / 3) + 5, (short)(data.getValue() >>> 48));
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        long datalong = data;

        if (data < 0)
        {
            datalong = data + 65536;
        }

        int enchantId = id / (numberOfGuiData() / 3);
        id %= (numberOfGuiData() / 3);

        if (id == 0)
        {
            if (data == -1)
            {
                this.enchants[enchantId] = null;
            }
            else
            {
                this.enchants[enchantId] = EnchantmentInfo.createDataFromEffectId(this.enchants[enchantId], data);
            }
        }
        else if (this.enchants[enchantId] != null)
        {
            if (id == 1)
            {
                this.enchants[enchantId].setMaxLevel(data);
            } else if (id == 2)
            {
                this.enchants[enchantId].setValue((this.enchants[enchantId].getValue() & 0xFFFFFFFFFFFF0000L) | (datalong));
            } else if (id == 3)
            {
                this.enchants[enchantId].setValue((this.enchants[enchantId].getValue() & 0xFFFFFFFF0000FFFFL) | (datalong << 16));
            } else if (id == 4)
            {
                this.enchants[enchantId].setValue((this.enchants[enchantId].getValue() & 0xFFFF0000FFFFFFFFL) | (datalong << 32));
            } else if (id == 5)
            {
                this.enchants[enchantId].setValue((this.enchants[enchantId].getValue() & 0x0000FFFFFFFFFFFFL) | (datalong << 48));
            }
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);

        for (int i = 0; i < 3; ++i)
        {
            if (this.enchants[i] == null)
            {
                tagCompound.setShort(this.generateNBTName("EffectId" + i, id), (short) - 1);
            }
            else
            {
                tagCompound.setShort(this.generateNBTName("EffectId" + i, id), (short)this.enchants[i].getEnchantment().getEnchantment().effectId);
                tagCompound.setLong(this.generateNBTName("ValueLong" + i, id), this.enchants[i].getValue());
                tagCompound.setShort(this.generateNBTName("maxLevel" + i, id), (short)this.enchants[i].getMaxLevel());
            }
        }
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);

        for (int i = 0; i < 3; ++i)
        {
            short effect = tagCompound.getShort(this.generateNBTName("EffectId" + i, id));

            if (effect == -1)
            {
                this.enchants[i] = null;
            }
            else
            {
                this.enchants[i] = EnchantmentInfo.createDataFromEffectId(this.enchants[i], effect);

                if (this.enchants[i] != null)
                {
                    long value = 0;
                    if(tagCompound.hasKey(this.generateNBTName("ValueLong" + i, id))) {
                        value = tagCompound.getLong(this.generateNBTName("ValueLong" + i, id));
                    } else if(tagCompound.hasKey(this.generateNBTName("Value" + i, id))) {
                        value = tagCompound.getInteger(this.generateNBTName("Value" + i, id));
                    }

                    this.enchants[i].setValue(value);
                    this.enchants[i].setMaxLevel(tagCompound.getShort(this.generateNBTName("maxLevel" + i, id)));
                }
            }
        }
    }

    public int guiWidth()
    {
        return 110;
    }

    public void addType(EnchantmentInfo.ENCHANTMENT_TYPE type)
    {
        this.enabledTypes.add(type);
    }
}
