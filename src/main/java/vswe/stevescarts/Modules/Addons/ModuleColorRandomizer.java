package vswe.stevescarts.Modules.Addons;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ModuleColorRandomizer extends ModuleAddon
{
    private int[] button = new int[] {10, 26, 16, 16};
    private int cooldown;
    private boolean hover;
    private Random random = new Random();

    public ModuleColorRandomizer(MinecartModular cart)
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
        return 100;
    }

    public int guiHeight()
    {
        return 50;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/color_randomizer.png");
        float[] color = this.getColor();
        GL11.glColor4f(color[0], color[1], color[2], 1.0F);
        this.drawImage(gui, 50, 20, 0, 16, 28, 28);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.inRect(x, y, this.button))
        {
            this.drawImage(gui, 10, 26, 32, 0, 16, 16);
        }
        else
        {
            this.drawImage(gui, 10, 26, 16, 0, 16, 16);
        }

        this.drawImage(gui, 10, 26, 0, 0, 16, 16);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        if (this.inRect(x, y, this.button))
        {
            String randomizeString = Localization.MODULES.ADDONS.BUTTON_RANDOMIZE.translate(new String[0]);
            this.drawStringOnMouseOver(gui, randomizeString, x, y, this.button);
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && this.inRect(x, y, this.button))
        {
            this.sendPacket(0);
        }
    }

    public void activatedByRail(int x, int y, int z, boolean active)
    {
        if (active && this.cooldown == 0)
        {
            this.randomizeColor();
            this.cooldown = 5;
        }
    }

    public void update()
    {
        if (this.cooldown > 0)
        {
            --this.cooldown;
        }
    }

    private void randomizeColor()
    {
        int red = this.random.nextInt(256);
        int green = this.random.nextInt(256);
        int blue = this.random.nextInt(256);
        this.setColorVal(0, (byte)red);
        this.setColorVal(1, (byte)green);
        this.setColorVal(2, (byte)blue);
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
        if (id == 0)
        {
            this.randomizeColor();
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
