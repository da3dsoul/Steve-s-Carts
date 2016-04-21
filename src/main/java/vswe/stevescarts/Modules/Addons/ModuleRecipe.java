package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CargoItemSelection;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotChest;
import vswe.stevescarts.TileEntities.TileEntityCargo;

public abstract class ModuleRecipe extends ModuleAddon
{
    private int target = 3;
    protected boolean dirty = true;
    protected ArrayList<SlotBase> inputSlots;
    protected ArrayList<SlotBase> outputSlots = new ArrayList();
    protected ArrayList<SlotBase> allTheSlots = new ArrayList();
    private int maxItemCount = 1;
    private int mode;

    public ModuleRecipe(MinecartModular cart)
    {
        super(cart);
    }

    protected abstract int getLimitStartX();

    protected abstract int getLimitStartY();

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        if (this.canUseAdvancedFeatures())
        {
            int[] area = this.getArea();
            ResourceHelper.bindResource("/gui/recipe.png");
            this.drawImage(gui, area[0] - 2, area[1] - 2, 0, 0, 20, 20);

            if (this.mode == 1)
            {
                for (int i = 0; i < 3; ++i)
                {
                    this.drawControlRect(gui, x, y, i);
                }
            }
            else
            {
                this.drawControlRect(gui, x, y, 1);
            }
        }
    }

    private void drawControlRect(GuiMinecart gui, int x, int y, int i)
    {
        int v = i * 11;
        int[] rect = this.getControlRect(i);
        this.drawImage(gui, rect, 20 + (this.inRect(x, y, rect) ? 22 : 0), v);
    }

    private int[] getControlRect(int i)
    {
        return new int[] {this.getLimitStartX(), this.getLimitStartY() + 12 * i, 22, 11};
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        if (this.canUseAdvancedFeatures())
        {
            String str;

            switch (this.mode)
            {
                case 0:
                    str = "Inf";
                    break;

                case 1:
                    str = String.valueOf(this.maxItemCount);
                    break;

                default:
                    str = "X";
            }

            this.drawString(gui, str, this.getControlRect(1), 4210752);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackgroundItems(GuiMinecart gui, int x, int y)
    {
        if (this.canUseAdvancedFeatures())
        {
            ItemStack icon;

            if (this.isTargetInvalid())
            {
                icon = new ItemStack(Items.minecart, 1);
            }
            else
            {
                icon = ((CargoItemSelection)TileEntityCargo.itemSelections.get(this.target)).getIcon();
            }

            int[] area = this.getArea();
            this.drawItemInInterface(gui, icon, area[0], area[1]);
        }
    }

    private boolean isTargetInvalid()
    {
        return this.target < 0 || this.target >= TileEntityCargo.itemSelections.size() || ((CargoItemSelection)TileEntityCargo.itemSelections.get(this.target)).getValidSlot() == null;
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        if (this.canUseAdvancedFeatures())
        {
            String str = Localization.MODULES.ADDONS.RECIPE_OUTPUT.translate(new String[0]) + "\n" + Localization.MODULES.ADDONS.CURRENT.translate(new String[0]) + ": ";

            if (this.isTargetInvalid())
            {
                str = str + Localization.MODULES.ADDONS.INVALID_OUTPUT.translate(new String[0]);
            }
            else
            {
                str = str + ((CargoItemSelection)TileEntityCargo.itemSelections.get(this.target)).getName();
            }

            this.drawStringOnMouseOver(gui, str, x, y, this.getArea());

            for (int i = 0; i < 3; ++i)
            {
                if (i == 1)
                {
                    str = Localization.MODULES.ADDONS.RECIPE_MODE.translate(new String[0]) + "\n" + Localization.MODULES.ADDONS.CURRENT.translate(new String[0]) + ": ";

                    switch (this.mode)
                    {
                        case 0:
                            str = str + Localization.MODULES.ADDONS.RECIPE_NO_LIMIT.translate(new String[0]);
                            break;

                        case 1:
                            str = str + Localization.MODULES.ADDONS.RECIPE_LIMIT.translate(new String[0]);
                            break;

                        default:
                            str = str + Localization.MODULES.ADDONS.RECIPE_DISABLED.translate(new String[0]);
                    }
                }
                else if (this.mode != 1)
                {
                    str = null;
                }
                else
                {
                    str = Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT.translate(new String[] {i == 0 ? "0" : "1"}) + "\n" + Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT_10.translate(new String[0]) + "\n" + Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT_64.translate(new String[0]);
                }

                if (str != null)
                {
                    this.drawStringOnMouseOver(gui, str, x, y, this.getControlRect(i));
                }
            }
        }
    }

    protected abstract int[] getArea();

    public int numberOfGuiData()
    {
        return this.canUseAdvancedFeatures() ? 3 : 0;
    }

    protected void checkGuiData(Object[] info)
    {
        if (this.canUseAdvancedFeatures())
        {
            this.updateGuiData(info, 0, (short)this.target);
            this.updateGuiData(info, 1, (short)this.mode);
            this.updateGuiData(info, 2, (short)this.maxItemCount);
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (this.canUseAdvancedFeatures())
        {
            if (id == 0)
            {
                this.target = data;
            }
            else if (id == 1)
            {
                this.mode = data;
            }
            else if (id == 2)
            {
                this.maxItemCount = data;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.canUseAdvancedFeatures())
        {
            if (this.inRect(x, y, this.getArea()))
            {
                this.sendPacket(0, (byte)button);
            }

            for (int i = 0; i < 3; ++i)
            {
                if ((this.mode == 1 || i == 1) && this.inRect(x, y, this.getControlRect(i)))
                {
                    if (i == 1)
                    {
                        this.sendPacket(1, (byte)button);
                    }
                    else
                    {
                        byte encodedData = (byte)(i == 0 ? 0 : 1);

                        if (GuiScreen.isCtrlKeyDown())
                        {
                            encodedData = (byte)(encodedData | 2);
                        }
                        else if (GuiScreen.isShiftKeyDown())
                        {
                            encodedData = (byte)(encodedData | 4);
                        }

                        this.sendPacket(2, encodedData);
                    }

                    break;
                }
            }
        }
    }

    protected int numberOfPackets()
    {
        return this.canUseAdvancedFeatures() ? 3 : 0;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (this.canUseAdvancedFeatures())
        {
            if (id == 0)
            {
                this.dirty = true;
                this.changeTarget(data[0] == 0);
            }
            else if (id == 1)
            {
                if (data[0] == 0)
                {
                    if (++this.mode > 2)
                    {
                        this.mode = 0;
                    }
                }
                else if (--this.mode < 0)
                {
                    this.mode = 2;
                }
            }
            else if (id == 2)
            {
                int dif = (data[0] & 1) == 0 ? 1 : -1;

                if ((data[0] & 2) != 0)
                {
                    dif *= 64;
                }
                else if ((data[0] & 4) != 0)
                {
                    dif *= 10;
                }

                this.maxItemCount = Math.min(Math.max(1, this.maxItemCount + dif), 999);
            }
        }
    }

    private void changeTarget(boolean up)
    {
        if (!up)
        {
            if (--this.target < 0)
            {
                this.target = TileEntityCargo.itemSelections.size() - 1;
            }
        }
        else if (++this.target >= TileEntityCargo.itemSelections.size())
        {
            this.target = 0;
        }

        if (this.isTargetInvalid())
        {
            this.changeTarget(up);
        }
    }

    protected abstract boolean canUseAdvancedFeatures();

    protected Class getValidSlot()
    {
        return this.isTargetInvalid() ? null : ((CargoItemSelection)TileEntityCargo.itemSelections.get(this.target)).getValidSlot();
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        if (this.canUseAdvancedFeatures())
        {
            this.target = tagCompound.getByte(this.generateNBTName("Target", id));
            this.mode = tagCompound.getByte(this.generateNBTName("Mode", id));
            this.maxItemCount = tagCompound.getShort(this.generateNBTName("MaxItems", id));
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        if (this.canUseAdvancedFeatures())
        {
            tagCompound.setByte(this.generateNBTName("Target", id), (byte)this.target);
            tagCompound.setByte(this.generateNBTName("Mode", id), (byte)this.mode);
            tagCompound.setShort(this.generateNBTName("MaxItems", id), (short)this.maxItemCount);
        }
    }

    protected void prepareLists()
    {
        if (this.inputSlots == null)
        {
            this.inputSlots = new ArrayList();
            Iterator validSlot = this.getCart().getModules().iterator();

            while (validSlot.hasNext())
            {
                ModuleBase i$ = (ModuleBase)validSlot.next();

                if (i$.getSlots() != null)
                {
                    Iterator module = i$.getSlots().iterator();

                    while (module.hasNext())
                    {
                        SlotBase i$1 = (SlotBase)module.next();

                        if (i$1 instanceof SlotChest)
                        {
                            this.inputSlots.add(i$1);
                        }
                    }
                }
            }
        }

        if (this.dirty)
        {
            this.allTheSlots.clear();
            this.outputSlots.clear();
            Class validSlot1 = this.getValidSlot();
            Iterator i$2 = this.getCart().getModules().iterator();

            while (i$2.hasNext())
            {
                ModuleBase module1 = (ModuleBase)i$2.next();

                if (module1.getSlots() != null)
                {
                    Iterator i$3 = module1.getSlots().iterator();

                    while (i$3.hasNext())
                    {
                        SlotBase slot = (SlotBase)i$3.next();

                        if (validSlot1.isInstance(slot))
                        {
                            this.outputSlots.add(slot);
                            this.allTheSlots.add(slot);
                        }
                        else if (slot instanceof SlotChest)
                        {
                            this.allTheSlots.add(slot);
                        }
                    }
                }
            }

            this.dirty = false;
        }
    }

    protected boolean canCraftMoreOfResult(ItemStack result)
    {
        if (this.mode == 0)
        {
            return true;
        }
        else if (this.mode == 2)
        {
            return false;
        }
        else
        {
            int count = 0;

            for (int i = 0; i < this.outputSlots.size(); ++i)
            {
                ItemStack item = ((SlotBase)this.outputSlots.get(i)).getStack();

                if (item != null && item.isItemEqual(result) && ItemStack.areItemStackTagsEqual(item, result))
                {
                    count += item.stackSize;

                    if (count >= this.maxItemCount)
                    {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
