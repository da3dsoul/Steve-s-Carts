package vswe.stevescarts.Modules.Addons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

import java.util.Iterator;

public class ModuleBuilderIntelligence extends ModuleAddon
{
    private ModuleDrill drill;
    private int guiW = -1;
    private int guiH = -1;
    private short[] isDisabled;
    private boolean clickedState;
    private boolean clicked;
    private int lastId;

    public ModuleBuilderIntelligence(MinecartModular cart)
    {
        super(cart);
    }

    public void preInit()
    {
        super.preInit();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleDrill)
            {
                this.drill = (ModuleDrill)module;
            }
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

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    private int getDrillWidth()
    {
        return this.drill == null ? 0 : this.drill.getAreaWidth();
    }

    public int guiWidth()
    {
        if (this.guiW == -1)
        {
            this.guiW = Math.max(15 + this.getDrillWidth() * 10 + 5, 93);
        }

        return this.guiW;
    }

    public int guiHeight()
    {
        if (this.guiH == -1)
        {
            this.guiH = 35;
        }

        return this.guiH;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/intelligence.png");
        int w = this.getDrillWidth();

        for (int i = 0; i < w; ++i)
        {
            int[] rect = this.getSettingRect(i);
            int srcX = 0;
            byte srcY = 0;
            this.drawImage(gui, rect, srcX, srcY);

            if (this.isActive(i))
            {
                srcX = 0;
                srcY = 8;
                this.drawImage(gui, rect, srcX, srcY);
            }

            srcX = this.inRect(x, y, rect) ? 8 : 0;
            srcY = 16;
            this.drawImage(gui, rect, srcX, srcY);

        }
    }

    private void initDisabledData()
    {
        if (this.isDisabled == null)
        {
            this.isDisabled = new short[this.getDrillWidth()];
        }
    }

    public boolean isActive(int id)
    {
        this.initDisabledData();
        return this.isLocked(id) ? true : (this.isDisabled[id]) == 0;
    }

    private boolean isLocked(int id)
    {
        int x = id % this.getDrillWidth();
        return x == (this.getDrillWidth() - 1) / 2;
    }

    private void swapActiveness(int id)
    {
        this.initDisabledData();

        if (!this.isLocked(id))
        {
            this.isDisabled[id] = (short)(this.isDisabled[id] ^ 1);
        }
    }

    private int[] getSettingRect(int x)
    {
        return new int[] {15 + x * 10, 20, 8, 8};
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        int w = this.getDrillWidth();

        for (int i = 0; i < w; ++i)
        {
            int[] rect = this.getSettingRect(i);
            String str = this.isLocked(i) ? Localization.MODULES.ADDONS.LOCKED.translate(new String[0]) : Localization.MODULES.ADDONS.CHANGE_INTELLIGENCE.translate(new String[0]) + "\n" + Localization.MODULES.ADDONS.CURRENT_INTELLIGENCE.translate(new String[] {this.isActive(i) ? "0" : "1"});
            this.drawStringOnMouseOver(gui, str, x, y, rect);
        }

    }

    public int numberOfGuiData()
    {
        return 9;
    }

    protected void checkGuiData(Object[] info)
    {
        if (this.isDisabled != null)
        {
            for (int i = 0; i < this.isDisabled.length; ++i)
            {
                this.updateGuiData(info, i, this.isDisabled[i]);
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        this.initDisabledData();

        if (id >= 0 && id < this.isDisabled.length)
        {
            this.isDisabled[id] = data;
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.swapActiveness(data[0]);
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        this.initDisabledData();

        for (int i = 0; i < this.isDisabled.length; ++i)
        {
            tagCompound.setShort(this.generateNBTName("isDisabled" + i, id), this.isDisabled[i]);
        }
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.initDisabledData();

        for (int i = 0; i < this.isDisabled.length; ++i)
        {
            this.isDisabled[i] = tagCompound.getShort(this.generateNBTName("isDisabled" + i, id));
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (button == -1 && this.clicked)
        {
            int w = this.getDrillWidth();

            for (int i = 0; i < w; ++i)
            {
                if (this.lastId != i && this.isActive(i) == this.clickedState)
                {
                    int[] rect = this.getSettingRect(i);

                    if (this.inRect(x, y, rect))
                    {
                        this.lastId = w + i;
                        this.sendPacket(0, (byte)(i));
                        return;
                    }
                }
            }
        }

        if (button == 0)
        {
            this.clicked = false;
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            int w = this.getDrillWidth();

            for (int i = 0; i < w; ++i)
            {
                int[] rect = this.getSettingRect(i);

                if (this.inRect(x, y, rect))
                {
                    this.clicked = true;
                    this.clickedState = this.isActive(i);
                    this.lastId = i;
                    this.sendPacket(0, (byte)(i));
                    return;
                }
            }
        }
    }
}
