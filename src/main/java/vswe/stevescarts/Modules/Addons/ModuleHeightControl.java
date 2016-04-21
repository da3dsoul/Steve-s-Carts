package vswe.stevescarts.Modules.Addons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.HeightControlOre;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ModuleHeightControl extends ModuleAddon
{
    private int levelNumberBoxX = 8;
    private int levelNumberBoxY = 18;
    private int[] arrowUp = new int[] {9, 36, 17, 9};
    private int[] arrowMiddle = new int[] {9, 46, 17, 6};
    private int[] arrowDown = new int[] {9, 53, 17, 9};
    private int oreMapX = 40;
    private int oreMapY = 18;

    public ModuleHeightControl(MinecartModular cart)
    {
        super(cart);
    }

    public boolean hasSlots()
    {
        return false;
    }

    public boolean hasGui()
    {
        return true;
    }

    public int guiWidth()
    {
        return Math.max(100, this.oreMapX + 5 + HeightControlOre.ores.size() * 4);
    }

    public int guiHeight()
    {
        return 65;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
        String s = String.valueOf(this.getYTarget());
        int x = this.levelNumberBoxX + 6;
        int color = 16777215;

        if (this.getYTarget() >= 100)
        {
            x -= 4;
        }
        else if (this.getYTarget() < 10)
        {
            x += 3;

            if (this.getYTarget() < 5)
            {
                color = 16711680;
            }
        }

        this.drawString(gui, s, x, this.levelNumberBoxY + 5, color);
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/heightcontrol.png");
        this.drawImage(gui, this.levelNumberBoxX, this.levelNumberBoxY, 4, 36, 21, 15);
        this.drawImage(gui, this.arrowUp, 4, 12);
        this.drawImage(gui, this.arrowMiddle, 4, 21);
        this.drawImage(gui, this.arrowDown, 4, 27);
        int pos;

        for (pos = 0; pos < HeightControlOre.ores.size(); ++pos)
        {
            HeightControlOre ore = (HeightControlOre)HeightControlOre.ores.get(pos);

            for (int j = 0; j < 11; ++j)
            {
                int altitude = this.getYTarget() - j + 5;
                boolean empty = ore.spanLowest > altitude || altitude > ore.spanHighest;
                boolean high = ore.bestLowest <= altitude && altitude <= ore.bestHighest;
                int srcY;
                int srcX;

                if (empty)
                {
                    srcY = 0;
                    srcX = 0;
                }
                else
                {
                    srcX = ore.srcX;
                    srcY = ore.srcY;

                    if (high)
                    {
                        srcY += 4;
                    }
                }

                this.drawImage(gui, this.oreMapX + pos * 4, this.oreMapY + j * 4, srcX, srcY, 4, 4);
            }
        }

        if (this.getYTarget() != (int)this.getCart().posY)
        {
            this.drawMarker(gui, 5, false);
        }

        pos = this.getYTarget() + 5 - (int)this.getCart().posY;

        if (pos >= 0 && pos < 11)
        {
            this.drawMarker(gui, pos, true);
        }
    }

    private void drawMarker(GuiMinecart gui, int pos, boolean isTargetLevel)
    {
        byte srcX = 4;
        int srcY = isTargetLevel ? 6 : 0;
        this.drawImage(gui, this.oreMapX - 1, this.oreMapY + pos * 4 - 1, srcX, srcY, 1, 6);

        for (int i = 0; i < HeightControlOre.ores.size(); ++i)
        {
            this.drawImage(gui, this.oreMapX + i * 4, this.oreMapY + pos * 4 - 1, srcX + 1, srcY, 4, 6);
        }

        this.drawImage(gui, this.oreMapX + HeightControlOre.ores.size() * 4, this.oreMapY + pos * 4 - 1, srcX + 5, srcY, 1, 6);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            byte packetData = 0;

            if (this.inRect(x, y, this.arrowMiddle))
            {
                packetData = (byte)(packetData | 1);
            }
            else
            {
                if (!this.inRect(x, y, this.arrowUp))
                {
                    if (!this.inRect(x, y, this.arrowDown))
                    {
                        return;
                    }

                    packetData = (byte)(packetData | 2);
                }

                if (GuiMinecart.isShiftKeyDown())
                {
                    packetData = (byte)(packetData | 4);
                }
            }

            this.sendPacket(0, packetData);
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            byte info = data[0];

            if ((info & 1) != 0)
            {
                this.setYTarget((int)this.getCart().posY);
            }
            else
            {
                byte mult;

                if ((info & 2) == 0)
                {
                    mult = 1;
                }
                else
                {
                    mult = -1;
                }

                byte dif;

                if ((info & 4) == 0)
                {
                    dif = 1;
                }
                else
                {
                    dif = 10;
                }

                int targetY = this.getYTarget();
                targetY += mult * dif;

                if (targetY < 0)
                {
                    targetY = 0;
                }
                else if (targetY > 255)
                {
                    targetY = 255;
                }

                this.setYTarget(targetY);
            }
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, (int)this.getCart().posY);
    }

    public void setYTarget(int val)
    {
        this.updateDw(0, val);
    }

    public int getYTarget()
    {
        if (this.isPlaceholder())
        {
            return 64;
        }
        else
        {
            int data = this.getDw(0);

            if (data < 0)
            {
                data += 256;
            }

            return data;
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.generateNBTName("Height", id), (short)this.getYTarget());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setYTarget(tagCompound.getShort(this.generateNBTName("Height", id)));
    }
}
