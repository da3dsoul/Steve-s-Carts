package vswe.stevescarts.Modules.Addons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ModuleColorizer extends ModuleAddon
{
    private int markerOffsetX = 10;
    private int scrollWidth = 64;
    private int markerMoving = -1;

    public ModuleColorizer(MinecartModular cart)
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

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int guiWidth()
    {
        return 125;
    }

    public int guiHeight()
    {
        return 75;
    }

    private int[] getMovableMarker(int i)
    {
        return new int[] {this.markerOffsetX + (int)((float)this.scrollWidth * ((float)this.getColorVal(i) / 255.0F)) - 2, 17 + i * 20, 4, 13};
    }

    private int[] getArea(int i)
    {
        return new int[] {this.markerOffsetX, 20 + i * 20, this.scrollWidth, 7};
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/color.png");

        for (int color = 0; color < 3; ++color)
        {
            this.drawMarker(gui, x, y, color);
        }

        float[] var5 = this.getColor();
        GL11.glColor4f(var5[0], var5[1], var5[2], 1.0F);
        this.drawImage(gui, this.scrollWidth + 25, 29, 4, 7, 28, 28);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        String[] colorNames = new String[] {Localization.MODULES.ADDONS.COLOR_RED.translate(new String[0]), Localization.MODULES.ADDONS.COLOR_GREEN.translate(new String[0]), Localization.MODULES.ADDONS.COLOR_BLUE.translate(new String[0])};

        for (int i = 0; i < 3; ++i)
        {
            this.drawStringOnMouseOver(gui, colorNames[i] + ": " + this.getColorVal(i), x, y, this.getArea(i));
        }
    }

    private void drawMarker(GuiMinecart gui, int x, int y, int id)
    {
        float[] colorArea = new float[3];
        float[] colorMarker = new float[3];

        for (int i = 0; i < 3; ++i)
        {
            if (i == id)
            {
                colorArea[i] = 0.7F;
                colorMarker[i] = 1.0F;
            }
            else
            {
                colorArea[i] = 0.2F;
                colorMarker[i] = 0.0F;
            }
        }

        GL11.glColor4f(colorArea[0], colorArea[1], colorArea[2], 1.0F);
        this.drawImage(gui, this.getArea(id), 0, 0);
        GL11.glColor4f(colorMarker[0], colorMarker[1], colorMarker[2], 1.0F);
        this.drawImage(gui, this.getMovableMarker(id), 0, 7);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (this.inRect(x, y, this.getMovableMarker(i)))
                {
                    this.markerMoving = i;
                }
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (this.markerMoving != -1)
        {
            int tempColor = (int)((float)(x - this.markerOffsetX) / ((float)this.scrollWidth / 255.0F));

            if (tempColor < 0)
            {
                tempColor = 0;
            }
            else if (tempColor > 255)
            {
                tempColor = 255;
            }

            this.sendPacket(this.markerMoving, (byte)tempColor);
        }

        if (button != -1)
        {
            this.markerMoving = -1;
        }
    }

    public int numberOfDataWatchers()
    {
        return 3;
    }

    public void initDw()
    {
        this.addDw(0, 255);
        this.addDw(1, 255);
        this.addDw(2, 255);
    }

    public int numberOfPackets()
    {
        return 3;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id >= 0 && id < 3)
        {
            this.setColorVal(id, data[0]);
        }
    }

    public int getColorVal(int i)
    {
        if (this.isPlaceholder())
        {
            return 255;
        }
        else
        {
            int tempVal = this.getDw(i);

            if (tempVal < 0)
            {
                tempVal += 256;
            }

            return tempVal;
        }
    }

    public void setColorVal(int id, int val)
    {
        this.updateDw(id, val);
    }

    private float getColorComponent(int i)
    {
        return (float)this.getColorVal(i) / 255.0F;
    }

    public float[] getColor()
    {
        return new float[] {this.getColorComponent(0), this.getColorComponent(1), this.getColorComponent(2)};
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Red", id), (byte)this.getColorVal(0));
        tagCompound.setByte(this.generateNBTName("Green", id), (byte)this.getColorVal(1));
        tagCompound.setByte(this.generateNBTName("Blue", id), (byte)this.getColorVal(2));
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setColorVal(0, tagCompound.getByte(this.generateNBTName("Red", id)));
        this.setColorVal(1, tagCompound.getByte(this.generateNBTName("Green", id)));
        this.setColorVal(2, tagCompound.getByte(this.generateNBTName("Blue", id)));
    }
}
