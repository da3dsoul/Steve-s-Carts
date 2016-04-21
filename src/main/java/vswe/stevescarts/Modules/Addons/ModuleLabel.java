package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.LabelInformation;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Engines.ModuleEngine;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotChest;

public class ModuleLabel extends ModuleAddon
{
    private ArrayList<LabelInformation> labels = new ArrayList();
    private int delay = 0;
    private ArrayList<SlotBase> storageSlots;
    private ModuleTool tool;

    public ModuleLabel(MinecartModular cart)
    {
        super(cart);
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.NAME)
        {
            public String getLabel()
            {
                return ModuleLabel.this.getCart().getCartName();
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.DISTANCE)
        {
            public String getLabel()
            {
                return Localization.MODULES.ADDONS.DISTANCE_LONG.translate(new String[] {String.valueOf((int)ModuleLabel.this.getCart().getDistanceToEntity(ModuleLabel.this.getClientPlayer()))});
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.POSITION)
        {
            public String getLabel()
            {
                return Localization.MODULES.ADDONS.POSITION_LONG.translate(new String[] {String.valueOf(ModuleLabel.this.getCart().x()), String.valueOf(ModuleLabel.this.getCart().y()), String.valueOf(ModuleLabel.this.getCart().z())});
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.FUEL)
        {
            public String getLabel()
            {
                int seconds = ModuleLabel.this.getIntDw(1);

                if (seconds == -1)
                {
                    return Localization.MODULES.ADDONS.FUEL_NO_CONSUMPTION.translate(new String[0]);
                }
                else
                {
                    int minutes = seconds / 60;
                    seconds -= minutes * 60;
                    int hours = minutes / 60;
                    minutes -= hours * 60;
                    return String.format(Localization.MODULES.ADDONS.FUEL_LONG.translate(new String[0]) + ": %02d:%02d:%02d", new Object[] {Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
                }
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.STORAGE)
        {
            public String getLabel()
            {
                int used = ModuleLabel.this.getDw(2);

                if (used < 0)
                {
                    used += 256;
                }

                return ModuleLabel.this.storageSlots == null ? "" : Localization.MODULES.ADDONS.STORAGE.translate(new String[0]) + ": " + used + "/" + ModuleLabel.this.storageSlots.size() + (ModuleLabel.this.storageSlots.size() == 0 ? "" : "[" + (int)(100.0F * (float)used / (float)ModuleLabel.this.storageSlots.size()) + "%]");
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.RETURNING_HOME)
        {
            public String getLabel()
            {
                return Localization.MODULES.ADDONS.RETURNING_HOME.translate(new String[0]) + ": " + (ModuleLabel.this.getDw(3) == 1);
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.BLOCKED)
        {
            public String getLabel()
            {
                return Localization.MODULES.ADDONS.BLOCKED.translate(new String[0]) + ": " + (ModuleLabel.this.getDw(4) == 1);
            }
        });
        this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.CURRENT_TOOL)
        {
            public String getLabel()
            {
                return Localization.MODULES.ADDONS.CURRENT_TOOL.translate(new String[0]) + ": " + (ModuleLabel.this.getStringDw(5));
            }
        });
    }

    public void preInit()
    {
        if (this.getCart().getModules() != null)
        {
            Iterator i$ = this.getCart().getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase moduleBase = (ModuleBase)i$.next();

                if (moduleBase instanceof ModuleTool)
                {
                    this.tool = (ModuleTool)moduleBase;
                    this.labels.add(new LabelInformation(Localization.MODULES.ADDONS.DURABILITY)
                    {
                        public String getLabel()
                        {
                            if (ModuleLabel.this.tool.useDurability())
                            {
                                int data = ModuleLabel.this.getIntDw(3);
                                return data == 0 ? Localization.MODULES.ADDONS.BROKEN.translate(new String[0]) : (data > 0 ? Localization.MODULES.ADDONS.DURABILITY.translate(new String[0]) + ": " + data + " / " + ModuleLabel.this.tool.getMaxDurability() + " [" + 100 * data / ModuleLabel.this.tool.getMaxDurability() + "%]" : (data == -1 ? "" : (data == -2 ? Localization.MODULES.ADDONS.NOT_BROKEN.translate(new String[0]) : Localization.MODULES.ADDONS.REPAIR.translate(new String[0]) + " [" + -(data + 3) + "%]")));
                            }
                            else
                            {
                                return Localization.MODULES.ADDONS.UNBREAKABLE.translate(new String[0]);
                            }
                        }
                    });
                    break;
                }
            }
        }
    }

    public void init()
    {
        this.storageSlots = new ArrayList();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module.getSlots() != null)
            {
                Iterator i$1 = module.getSlots().iterator();

                while (i$1.hasNext())
                {
                    SlotBase slot = (SlotBase)i$1.next();

                    if (slot instanceof SlotChest)
                    {
                        this.storageSlots.add(slot);
                    }
                }
            }
        }
    }

    private boolean hasTool()
    {
        return this.tool != null;
    }

    private boolean hasToolWithDurability()
    {
        return this.hasTool() && this.tool.useDurability();
    }

    public void addToLabel(ArrayList<String> label)
    {
        for (int i = 0; i < this.labels.size(); ++i)
        {
            if (this.isActive(i))
            {
                label.add(((LabelInformation)this.labels.get(i)).getLabel());
            }
        }
    }

    private int[] getBoxArea(int i)
    {
        return new int[] {10, 17 + i * 12, 8, 8};
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/label.png");

        for (int i = 0; i < this.labels.size(); ++i)
        {
            int[] rect = this.getBoxArea(i);
            this.drawImage(gui, rect, this.isActive(i) ? 8 : 0, 0);
            this.drawImage(gui, rect, this.inRect(x, y, rect) ? 8 : 0, 8);
        }
    }

    private boolean isActive(int i)
    {
        return !this.isPlaceholder() && (this.getDw(0) & 1 << i) != 0;
    }

    private void toggleActive(int i)
    {
        this.updateDw(0, this.getDw(0) ^ 1 << i);
    }

    public int numberOfDataWatchers()
    {
        int count = 5;

        if (this.hasToolWithDurability())
        {
            ++count;
        }

        return count;
    }

    public void initDw()
    {
        this.addDw(0, 0);
        this.addIntDw(1, 0);
        this.addDw(2, 0);

        if (this.hasToolWithDurability())
        {
            this.addIntDw(6, -1);
        }

        this.addDw(3, 0);
        this.addDw(4, 0);
        this.addStringDw(5,"");
    }

    public void update()
    {
        if (!this.isPlaceholder() && !this.getCart().worldObj.isRemote)
        {
            if(this.getDw(3) == 1 != this.getCart().returningHome) {
                this.updateDw(3, (byte)(this.getCart().returningHome?1:0));
            }
            if(this.getDw(4) == 1 != this.getCart().isCollidedForward) {
                this.updateDw(4, (byte)(this.getCart().isCollidedForward?1:0));
            }
            if(!this.getStringDw(5).equals(this.getCart().currentTool)) {
                this.updateStringDw(5, this.getCart().currentTool);
            }

            if (this.delay <= 0)
            {
                int data;
                Iterator i$;

                if (this.isActive(3))
                {
                    data = 0;
                    i$ = this.getCart().getEngines().iterator();

                    while (i$.hasNext())
                    {
                        ModuleEngine slot = (ModuleEngine)i$.next();

                        if (slot.getPriority() != 3)
                        {
                            data += slot.getTotalFuel();
                        }
                    }

                    if (data != 0)
                    {
                        int var4 = this.getCart().getConsumption();

                        if (var4 == 0)
                        {
                            data = -1;
                        }
                        else
                        {
                            data /= var4 * 20;
                        }
                    }

                    this.updateIntDw(1, data);
                }

                if (this.isActive(4))
                {
                    data = 0;
                    i$ = this.storageSlots.iterator();

                    while (i$.hasNext())
                    {
                        SlotBase var5 = (SlotBase)i$.next();

                        if (var5.getHasStack())
                        {
                            ++data;
                        }
                    }

                    this.updateDw(2, (byte)data);
                }

                if (this.hasToolWithDurability())
                {
                    if (this.isActive(5))
                    {
                        if (this.tool.isRepairing())
                        {
                            if (this.tool.isActuallyRepairing())
                            {
                                this.updateIntDw(3, -3 - this.tool.getRepairPercentage());
                            }
                            else
                            {
                                this.updateIntDw(3, -2);
                            }
                        }
                        else
                        {
                            this.updateIntDw(3, this.tool.getCurrentDurability());
                        }
                    }
                    else if (this.getIntDw(3) != -1)
                    {
                        this.updateIntDw(3, -1);
                    }
                }

                this.delay = 20;
            }
            else if (this.delay > 0)
            {
                --this.delay;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        for (int i = 0; i < this.labels.size(); ++i)
        {
            int[] rect = this.getBoxArea(i);

            if (this.inRect(x, y, rect))
            {
                this.sendPacket(0, (byte)i);
                break;
            }
        }
    }

    protected int numberOfPackets()
    {
        return 1;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.toggleActive(data[0]);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ADDONS.LABELS.translate(new String[0]), 8, 6, 4210752);

        for (int i = 0; i < this.labels.size(); ++i)
        {
            int[] rect = this.getBoxArea(i);
            this.drawString(gui, ((LabelInformation)this.labels.get(i)).getName(), rect[0] + 12, rect[1] + 1, 4210752);
        }
    }

    public boolean hasGui()
    {
        return true;
    }

    public boolean hasSlots()
    {
        return false;
    }

    public int guiWidth()
    {
        return 92;
    }

    public int guiHeight()
    {
        return 149;
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.updateDw(0, tagCompound.getByte(this.generateNBTName("Active", id)));
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Active", id), this.getDw(0));
    }
}
