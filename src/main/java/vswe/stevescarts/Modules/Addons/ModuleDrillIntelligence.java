package vswe.stevescarts.Modules.Addons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ModuleDrillIntelligence extends ModuleAddon
{
    private ModuleDrill drill;
    private boolean hasHeightController;
    private int guiW = -1;
    private int guiH = -1;
    private short[] isDisabled;
    private boolean clickedState;
    private boolean clicked;
    private int lastId;

    public ModuleDrillIntelligence(MinecartModular cart)
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
            else if (module instanceof ModuleHeightControl)
            {
                this.hasHeightController = true;
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

    private int getDrillHeight()
    {
        return this.drill == null ? 0 : this.drill.getAreaHeight() + (this.hasHeightController ? 2 : 0);
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
            this.guiH = 20 + this.getDrillHeight() * 10 + 5;
        }

        return this.guiH;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/intelligence.png");
        int w = this.getDrillWidth();
        int h = this.getDrillHeight();

        for (int i = 0; i < w; ++i)
        {
            for (int j = 0; j < h; ++j)
            {
                int[] rect = this.getSettingRect(i, j);
                int srcX = this.hasHeightController && (j == 0 || j == h - 1) ? 8 : 0;
                byte srcY = 0;
                this.drawImage(gui, rect, srcX, srcY);

                if (this.isActive(j * w + i))
                {
                    srcX = this.isLocked(j * w + i) ? 8 : 0;
                    srcY = 8;
                    this.drawImage(gui, rect, srcX, srcY);
                }

                srcX = this.inRect(x, y, rect) ? 8 : 0;
                srcY = 16;
                this.drawImage(gui, rect, srcX, srcY);
            }
        }
    }

    private void initDisabledData()
    {
        if (this.isDisabled == null)
        {
            this.isDisabled = new short[(int)Math.ceil((double)((float)(this.getDrillWidth() * this.getDrillHeight()) / 16.0F))];
        }
    }

    public boolean isActive(int x, int y, int offset, boolean direction)
    {
        y = this.getDrillHeight() - 1 - y;

        if (this.hasHeightController)
        {
            y -= offset;
        }

        if (!direction)
        {
            x = this.getDrillWidth() - 1 - x;
        }

        return this.isActive(y * this.getDrillWidth() + x);
    }

    private boolean isActive(int id)
    {
        this.initDisabledData();
        return this.isLocked(id) ? true : (this.isDisabled[id / 16] & 1 << id % 16) == 0;
    }

    private boolean isLocked(int id)
    {
        int x = id % this.getDrillWidth();
        int y = id / this.getDrillWidth();
        return y != this.getDrillHeight() - 1 && (!this.hasHeightController || y != this.getDrillHeight() - 2) ? false : x == (this.getDrillWidth() - 1) / 2;
    }

    private void swapActiveness(int id)
    {
        this.initDisabledData();

        if (!this.isLocked(id))
        {
            this.isDisabled[id / 16] = (short)(this.isDisabled[id / 16] ^ 1 << id % 16);
        }
    }

    private int[] getSettingRect(int x, int y)
    {
        return new int[] {15 + x * 10, 20 + y * 10, 8, 8};
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        int w = this.getDrillWidth();
        int h = this.getDrillHeight();

        for (int i = 0; i < w; ++i)
        {
            for (int j = 0; j < h; ++j)
            {
                int[] rect = this.getSettingRect(i, j);
                String str = this.isLocked(j * w + i) ? Localization.MODULES.ADDONS.LOCKED.translate(new String[0]) : Localization.MODULES.ADDONS.CHANGE_INTELLIGENCE.translate(new String[0]) + "\n" + Localization.MODULES.ADDONS.CURRENT_INTELLIGENCE.translate(new String[] {this.isActive(j * w + i) ? "0" : "1"});
                this.drawStringOnMouseOver(gui, str, x, y, rect);
            }
        }
    }

    public int numberOfGuiData()
    {
        byte maxDrillWidth = 9;
        byte maxDrillHeight = 9;
        return (int)Math.ceil((double)((float)(maxDrillWidth * (maxDrillHeight + 2)) / 16.0F));
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
            int h = this.getDrillHeight();

            for (int i = 0; i < w; ++i)
            {
                for (int j = 0; j < h; ++j)
                {
                    if (this.lastId != j * w + i && this.isActive(j * w + i) == this.clickedState)
                    {
                        int[] rect = this.getSettingRect(i, j);

                        if (this.inRect(x, y, rect))
                        {
                            this.lastId = j * w + i;
                            this.sendPacket(0, (byte)(j * w + i));
                            return;
                        }
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
            int h = this.getDrillHeight();

            for (int i = 0; i < w; ++i)
            {
                for (int j = 0; j < h; ++j)
                {
                    int[] rect = this.getSettingRect(i, j);

                    if (this.inRect(x, y, rect))
                    {
                        this.clicked = true;
                        this.clickedState = this.isActive(j * w + i);
                        this.lastId = j * w + i;
                        this.sendPacket(0, (byte)(j * w + i));
                        return;
                    }
                }
            }
        }
    }
}
