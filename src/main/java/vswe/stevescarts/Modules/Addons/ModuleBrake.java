package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ILeverModule;

public class ModuleBrake extends ModuleAddon implements ILeverModule
{
    private int[] startstopRect = new int[] {15, 20, 24, 12};
    private int[] turnbackRect;

    public ModuleBrake(MinecartModular cart)
    {
        super(cart);
        this.turnbackRect = new int[] {this.startstopRect[0] + this.startstopRect[2] + 5, this.startstopRect[1], this.startstopRect[2], this.startstopRect[3]};
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
        return 35;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ADDONS.CONTROL_LEVER.translate(new String[0]), 8, 6, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/lever.png");
        this.drawButton(gui, x, y, this.startstopRect, this.isForceStopping() ? 2 : 1);
        this.drawButton(gui, x, y, this.turnbackRect, 0);
    }

    private void drawButton(GuiMinecart gui, int x, int y, int[] coords, int imageID)
    {
        if (this.inRect(x, y, coords))
        {
            this.drawImage(gui, coords, 0, coords[3]);
        }
        else
        {
            this.drawImage(gui, coords, 0, 0);
        }

        int srcY = coords[3] * 2 + imageID * (coords[3] - 2);
        this.drawImage(gui, coords[0] + 1, coords[1] + 1, 0, srcY, coords[2] - 2, coords[3] - 2);
    }

    public boolean stopEngines()
    {
        return this.isForceStopping();
    }

    private boolean isForceStopping()
    {
        return this.isPlaceholder() ? this.getSimInfo().getBrakeActive() : this.getDw(0) != 0;
    }

    private void setForceStopping(boolean val)
    {
        this.updateDw(0, (byte)(val ? 1 : 0));
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, this.isForceStopping() ? Localization.MODULES.ADDONS.LEVER_START.translate(new String[0]) : Localization.MODULES.ADDONS.LEVER_STOP.translate(new String[0]), x, y, this.startstopRect);
        this.drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.LEVER_TURN.translate(new String[0]), x, y, this.turnbackRect);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            if (this.inRect(x, y, this.startstopRect))
            {
                this.sendPacket(0);
            }
            else if (this.inRect(x, y, this.turnbackRect))
            {
                this.sendPacket(1);
            }
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.setForceStopping(!this.isForceStopping());
        }
        else if (id == 1)
        {
            this.turnback();
        }
    }

    public int numberOfPackets()
    {
        return 2;
    }

    public float getLeverState()
    {
        return this.isForceStopping() ? 0.0F : 1.0F;
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setBoolean(this.generateNBTName("ForceStop", id), this.isForceStopping());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setForceStopping(tagCompound.getBoolean(this.generateNBTName("ForceStop", id)));
    }
}
