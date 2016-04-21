package vswe.stevescarts.Modules.Addons.Plants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public class ModulePlantSize extends ModuleAddon
{
    private int size = 1;
    private int[] boxrect = new int[] {10, 18, 44, 44};

    public ModulePlantSize(MinecartModular cart)
    {
        super(cart);
    }

    public int getSize()
    {
        return this.size;
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
        return 80;
    }

    public int guiHeight()
    {
        return 70;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ADDONS.PLANTER_RANGE.translate(new String[0]), 8, 6, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/plantsize.png");
        int srcX = (this.size - 1) % 5 * 44;
        int srcY = ((this.size - 1) / 5 + 1) * 44;
        this.drawImage(gui, this.boxrect, srcX, srcY);

        if (this.inRect(x, y, this.boxrect))
        {
            this.drawImage(gui, this.boxrect, 0, 0);
        }
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.SAPLING_AMOUNT.translate(new String[0]) + ": " + this.size + "x" + this.size, x, y, this.boxrect);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if ((button == 0 || button == 1) && this.inRect(x, y, this.boxrect))
        {
            this.sendPacket(0, (byte)button);
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            if (data[0] == 1)
            {
                --this.size;

                if (this.size < 1)
                {
                    this.size = 7;
                }
            }
            else
            {
                ++this.size;

                if (this.size > 7)
                {
                    this.size = 1;
                }
            }
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.size);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.size = data;
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("size", id), (byte)this.size);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.size = tagCompound.getByte(this.generateNBTName("size", id));
    }
}
