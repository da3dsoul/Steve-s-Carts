package vswe.stevescarts.Modules.Workers.Tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.EnchantmentInfo;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleEnchants;
import vswe.stevescarts.Modules.Workers.ModuleWorker;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotRepair;

public abstract class ModuleTool extends ModuleWorker
{
    private int currentDurability = this.getMaxDurability();
    private int remainingRepairUnits;
    private int maximumRepairUnits = 1;
    protected ModuleEnchants enchanter;
    private int[] durabilityRect = new int[] {10, 15, 52, 8};

    public ModuleTool(MinecartModular cart)
    {
        super(cart);
    }

    public abstract int getMaxDurability();

    public abstract String getRepairItemName();

    public abstract int getRepairItemUnits(ItemStack var1);

    public abstract int getRepairSpeed();

    public abstract boolean useDurability();

    public void init()
    {
        super.init();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleEnchants)
            {
                this.enchanter = (ModuleEnchants)module;
                this.enchanter.addType(EnchantmentInfo.ENCHANTMENT_TYPE.TOOL);
                break;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/tool.png");
        this.drawBox(gui, 0, 0, 1.0F);
        this.drawBox(gui, 0, 8, this.useDurability() ? (float)this.currentDurability / (float)this.getMaxDurability() : 1.0F);
        this.drawBox(gui, 0, 16, (float)this.remainingRepairUnits / (float)this.maximumRepairUnits);

        if (this.inRect(x, y, this.durabilityRect))
        {
            this.drawBox(gui, 0, 24, 1.0F);
        }
    }

    private void drawBox(GuiMinecart gui, int u, int v, float mult)
    {
        int w = (int)((float)this.durabilityRect[2] * mult);

        if (w > 0)
        {
            this.drawImage(gui, this.durabilityRect[0], this.durabilityRect[1], u, v, w, this.durabilityRect[3]);
        }
    }

    public boolean isValidRepairMaterial(ItemStack item)
    {
        return this.getRepairItemUnits(item) > 0;
    }

    public boolean hasGui()
    {
        return true;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotRepair(this, this.getCart(), slotId, 76, 8);
    }

    protected int getInventoryWidth()
    {
        return 1;
    }

    public int guiWidth()
    {
        return 100;
    }

    public int guiHeight()
    {
        return 50;
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        String str;

        if (this.useDurability())
        {
            str = Localization.MODULES.TOOLS.DURABILITY.translate(new String[0]) + ": " + this.currentDurability + "/" + this.getMaxDurability();

            if (this.isBroken())
            {
                str = str + " [" + Localization.MODULES.TOOLS.BROKEN.translate(new String[0]) + "]";
            }
            else
            {
                str = str + " [" + 100 * this.currentDurability / this.getMaxDurability() + "%]";
            }

            str = str + "\n";

            if (this.isRepairing())
            {
                if (this.isActuallyRepairing())
                {
                    str = str + " [" + this.getRepairPercentage() + "%]";
                }
                else
                {
                    str = str + Localization.MODULES.TOOLS.DECENT.translate(new String[0]);
                }
            }
            else
            {
                str = str + Localization.MODULES.TOOLS.INSTRUCTION.translate(new String[] {this.getRepairItemName()});
            }
        }
        else
        {
            str = Localization.MODULES.TOOLS.UNBREAKABLE.translate(new String[0]);

            if (this.isRepairing() && !this.isActuallyRepairing())
            {
                str = str + " " + Localization.MODULES.TOOLS.UNBREAKABLE_REPAIR.translate(new String[0]);
            }
        }

        this.drawStringOnMouseOver(gui, str, x, y, this.durabilityRect);
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote && this.useDurability())
        {
            if (this.isActuallyRepairing())
            {
                byte units = 1;
                this.remainingRepairUnits -= units;
                this.currentDurability += units * this.getRepairSpeed();

                if (this.currentDurability > this.getMaxDurability())
                {
                    this.currentDurability = this.getMaxDurability();
                }
            }

            if (!this.isActuallyRepairing())
            {
                int var2 = this.getRepairItemUnits(this.getStack(0));

                if (var2 > 0 && var2 <= this.getMaxDurability() - this.currentDurability)
                {
                    this.maximumRepairUnits = this.remainingRepairUnits = var2 / this.getRepairSpeed();
                    --this.getStack(0).stackSize;

                    if (this.getStack(0).stackSize <= 0)
                    {
                        this.setStack(0, (ItemStack)null);
                    }
                }
            }
        }
    }

    public boolean stopEngines()
    {
        return this.isRepairing();
    }

    public boolean isRepairing()
    {
        return this.getStack(0) != null || this.isActuallyRepairing();
    }

    public boolean isActuallyRepairing()
    {
        return this.remainingRepairUnits > 0;
    }

    public boolean isBroken()
    {
        return this.currentDurability == 0 && this.useDurability();
    }

    public void damageTool(int val)
    {
        int unbreaking = this.enchanter != null ? this.enchanter.getUnbreakingLevel() : 0;

        if (this.getCart().rand.nextInt(100) < 100 / (unbreaking + 1))
        {
            this.currentDurability -= val;

            if (this.currentDurability < 0)
            {
                this.currentDurability = 0;
            }
        }

        if (this.enchanter != null)
        {
            this.enchanter.damageEnchant(EnchantmentInfo.ENCHANTMENT_TYPE.TOOL, val);
        }
    }

    public int numberOfGuiData()
    {
        return 4;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)(this.currentDurability & 65535));
        this.updateGuiData(info, 1, (short)(this.currentDurability >> 16 & 65535));
        this.updateGuiData(info, 2, (short)this.remainingRepairUnits);
        this.updateGuiData(info, 3, (short)this.maximumRepairUnits);
    }

    public void receiveGuiData(int id, short data)
    {
        int dataint = data;

        if (data < 0)
        {
            dataint = data + 65536;
        }

        if (id == 0)
        {
            this.currentDurability = this.currentDurability & -65536 | dataint;
        }
        else if (id == 1)
        {
            this.currentDurability = this.currentDurability & 65535 | dataint << 16;
        }
        else if (id == 2)
        {
            this.remainingRepairUnits = data;
        }
        else if (id == 3)
        {
            this.maximumRepairUnits = data;
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setInteger(this.generateNBTName("Durability", id), this.currentDurability);
        tagCompound.setShort(this.generateNBTName("Repair", id), (short)this.remainingRepairUnits);
        tagCompound.setShort(this.generateNBTName("MaxRepair", id), (short)this.maximumRepairUnits);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.currentDurability = tagCompound.getInteger(this.generateNBTName("Durability", id));
        this.remainingRepairUnits = tagCompound.getShort(this.generateNBTName("Repair", id));
        this.maximumRepairUnits = tagCompound.getShort(this.generateNBTName("MaxRepair", id));
    }

    public boolean hasExtraData()
    {
        return true;
    }

    public byte getExtraData()
    {
        return (byte)(100 * this.currentDurability / this.getMaxDurability());
    }

    public void setExtraData(byte b)
    {
        this.currentDurability = b * this.getMaxDurability() / 100;
    }

    public boolean shouldSilkTouch(Block b, int x, int y, int z, int m)
    {
        boolean doSilkTouch = false;

        try
        {
            if (this.enchanter != null && this.enchanter.useSilkTouch() && b.canSilkHarvest(this.getCart().worldObj, (EntityPlayer)null, x, y, z, m))
            {
                return true;
            }
        }
        catch (Exception var8)
        {
            ;
        }

        return false;
    }

    public ItemStack getSilkTouchedItem(Block b, int m)
    {
        byte droppedMeta = 0;
        ItemStack stack = new ItemStack(b, 1, droppedMeta);
        return stack.getItem() != null && stack.getItem().getHasSubtypes() ? new ItemStack(b, 1, m) : stack;
    }

    public int getCurrentDurability()
    {
        return this.currentDurability;
    }

    public int getRepairPercentage()
    {
        return 100 - 100 * this.remainingRepairUnits / this.maximumRepairUnits;
    }
}
