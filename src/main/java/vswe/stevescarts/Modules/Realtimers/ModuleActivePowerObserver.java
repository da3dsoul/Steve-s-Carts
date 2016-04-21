package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.ModuleAddon;
import vswe.stevescarts.Modules.Engines.ModuleEngine;

public class ModuleActivePowerObserver extends ModuleAddon
{
    private short[] areaData = new short[4];
    private short[] powerLevel = new short[4];
    private int currentEngine = -1;



    public ModuleActivePowerObserver(MinecartModular cart)
    {
        super(cart);
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
        return 190;
    }

    public int guiHeight()
    {
        return 150;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);

        for (int i = 0; i < 4; ++i)
        {
            int[] rect = this.getPowerRect(i);
            this.drawString(gui, this.powerLevel[i] + Localization.MODULES.ADDONS.K.translate(new String[0]), rect, 4210752);
        }
    }

    private boolean removeOnPickup()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        int i;

        for (i = 0; i < this.getCart().getEngines().size(); ++i)
        {
            if (!this.removeOnPickup() || this.currentEngine != i)
            {
                this.drawEngine(gui, i, this.getEngineRect(i));
            }
        }

        ResourceHelper.bindResource("/gui/observer.png");

        for (i = 0; i < 4; ++i)
        {
            int[] rect = this.getAreaRect(i);
            this.drawImage(gui, rect, 18, 22 * i);

            if (this.inRect(x, y, rect))
            {
                this.drawImage(gui, rect, 18, 22 * (i + 4));
            }

            int count = 0;

            for (int j = 0; j < this.getCart().getEngines().size(); ++j)
            {
                if ((this.areaData[i] & 1 << j) != 0)
                {
                    this.drawEngine(gui, j, this.getEngineRectInArea(i, count));
                    ++count;
                }
            }

            ResourceHelper.bindResource("/gui/observer.png");
            rect = this.getPowerRect(i);

            if (this.isAreaActive(i))
            {
                this.drawImage(gui, rect, 122, 0);
            }
            else
            {
                this.drawImage(gui, rect, 122 + rect[2], 0);
            }

            if (this.inRect(x, y, rect))
            {
                this.drawImage(gui, rect, 122 + rect[2] * 2, 0);
            }
        }

        if (this.currentEngine != -1)
        {
            this.drawEngine(gui, this.currentEngine, this.getEngineRectMouse(x, y + this.getCart().getRealScrollY()));
        }
    }

    private void drawEngine(GuiMinecart gui, int id, int[] rect)
    {
        ModuleEngine engine = (ModuleEngine)this.getCart().getEngines().get(id);
        ResourceHelper.bindResourcePath("/atlas/items.png");
        this.drawImage(gui, engine.getData().getIcon(), rect, 0, 0);
    }

    private int[] getAreaRect(int id)
    {
        return new int[] {10, 40 + 25 * id, 104, 22};
    }

    private int[] getEngineRect(int id)
    {
        return new int[] {11 + id * 20, 21, 16, 16};
    }

    private int[] getEngineRectMouse(int x, int y)
    {
        return new int[] {x - 8, y - 8, 16, 16};
    }

    private int[] getEngineRectInArea(int areaid, int number)
    {
        int[] area = this.getAreaRect(areaid);
        return new int[] {area[0] + 4 + number * 20, area[1] + 3, 16, 16};
    }

    private int[] getPowerRect(int areaid)
    {
        int[] area = this.getAreaRect(areaid);
        return new int[] {area[0] + area[2] + 10, area[1] + 2, 35, 18};
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        int i;

        for (i = 0; i < this.getCart().getEngines().size(); ++i)
        {
            if (!this.removeOnPickup() || this.currentEngine != i)
            {
                ModuleEngine count = (ModuleEngine)this.getCart().getEngines().get(i);
                this.drawStringOnMouseOver(gui, count.getData().getName() + "\n" + Localization.MODULES.ADDONS.OBSERVER_INSTRUCTION.translate(new String[0]), x, y, this.getEngineRect(i));
            }
        }

        for (i = 0; i < 4; ++i)
        {
            int var8 = 0;

            for (int j = 0; j < this.getCart().getEngines().size(); ++j)
            {
                if ((this.areaData[i] & 1 << j) != 0)
                {
                    ModuleEngine engine = (ModuleEngine)this.getCart().getEngines().get(j);
                    this.drawStringOnMouseOver(gui, engine.getData().getName() + "\n" + Localization.MODULES.ADDONS.OBSERVER_REMOVE.translate(new String[0]), x, y, this.getEngineRectInArea(i, var8));
                    ++var8;
                }
            }

            if (this.currentEngine != -1)
            {
                this.drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.OBSERVER_DROP.translate(new String[0]), x, y, this.getAreaRect(i));
            }

            this.drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.OBSERVER_CHANGE.translate(new String[0]) + "\n" + Localization.MODULES.ADDONS.OBSERVER_CHANGE_10.translate(new String[0]), x, y, this.getPowerRect(i));
        }
    }

    public int numberOfGuiData()
    {
        return 8;
    }

    protected void checkGuiData(Object[] info)
    {
        int i;

        for (i = 0; i < 4; ++i)
        {
            this.updateGuiData(info, i, this.areaData[i]);
        }

        for (i = 0; i < 4; ++i)
        {
            this.updateGuiData(info, i + 4, this.powerLevel[i]);
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id >= 0 && id < 4)
        {
            this.areaData[id] = data;
        }
        else if (id >= 4 && id < 8)
        {
            this.powerLevel[id - 4] = data;
        }
    }

    public int numberOfPackets()
    {
        return 3;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        byte area;
        byte button;

        if (id == 0)
        {
            area = data[0];
            button = data[1];
            this.areaData[area] = (short)(this.areaData[area] | 1 << button);
        }
        else if (id == 1)
        {
            area = data[0];
            button = data[1];
            this.areaData[area] = (short)(this.areaData[area] & ~(1 << button));
        }
        else if (id == 2)
        {
            area = data[0];
            int button1 = data[1] & 1;
            boolean shift = (data[1] & 2) != 0;
            int change = button1 == 0 ? 1 : -1;

            if (shift)
            {
                change *= 10;
            }

            short value = this.powerLevel[area];
            value = (short)(value + change);

            if (value < 0)
            {
                value = 0;
            }
            else if (value > 999)
            {
                value = 999;
            }

            this.powerLevel[area] = value;
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (button != -1)
        {
            if (button == 0)
            {
                for (int i = 0; i < 4; ++i)
                {
                    int[] rect = this.getAreaRect(i);

                    if (this.inRect(x, y, rect))
                    {
                        this.sendPacket(0, new byte[] {(byte)i, (byte)this.currentEngine});
                        break;
                    }
                }
            }

            this.currentEngine = -1;
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        int i;
        int[] count;

        for (i = 0; i < 4; ++i)
        {
            count = this.getPowerRect(i);

            if (this.inRect(x, y, count))
            {
                this.sendPacket(2, new byte[] {(byte)i, (byte)(button | (GuiScreen.isShiftKeyDown() ? 2 : 0))});
                break;
            }
        }

        if (button == 0)
        {
            for (i = 0; i < this.getCart().getEngines().size(); ++i)
            {
                count = this.getEngineRect(i);

                if (this.inRect(x, y, count))
                {
                    this.currentEngine = i;
                    break;
                }
            }
        }
        else if (button == 1)
        {
            for (i = 0; i < 4; ++i)
            {
                int var9 = 0;

                for (int j = 0; j < this.getCart().getEngines().size(); ++j)
                {
                    if ((this.areaData[i] & 1 << j) != 0)
                    {
                        int[] rect = this.getEngineRectInArea(i, var9);

                        if (this.inRect(x, y, rect))
                        {
                            this.sendPacket(1, new byte[] {(byte)i, (byte)j});
                            break;
                        }

                        ++var9;
                    }
                }
            }
        }
    }

    public boolean isAreaActive(int area)
    {
        int power = 0;

        for (int i = 0; i < this.getCart().getEngines().size(); ++i)
        {
            ModuleEngine engine = (ModuleEngine)this.getCart().getEngines().get(i);

            if ((this.areaData[area] & 1 << i) != 0)
            {
                power += engine.getTotalFuel();
            }
        }

        return power > this.powerLevel[area] * 1000;
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        for (int i = 0; i < 4; ++i)
        {
            tagCompound.setShort(this.generateNBTName("AreaData" + i, id), this.areaData[i]);
            tagCompound.setShort(this.generateNBTName("PowerLevel" + i, id), this.powerLevel[i]);
        }

    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        for (int i = 0; i < 4; ++i)
        {
            this.areaData[i] = tagCompound.getShort(this.generateNBTName("AreaData" + i, id));
            this.powerLevel[i] = tagCompound.getShort(this.generateNBTName("PowerLevel" + i, id));
        }
    }

    public void update()
    {
        super.update();
        boolean b = false;
        for(int i = 0; i < 4; i++) {
            boolean c = false;
            for(int j = 0; j < this.getCart().getEngines().size(); j++) {
                if((this.areaData[i] & 1 << j) != 0) c = true;
            }
            if(c && !isAreaActive(i)) {
                b = true;
                break;
            }
        }
        if(b) {
            this.turnback();
        }


    }
}
