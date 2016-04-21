package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;

public class ModuleInvisible extends ModuleAddon implements IActivatorModule
{
    private int[] buttonRect = new int[] {20, 20, 24, 12};

    public ModuleInvisible(MinecartModular cart)
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
        return 90;
    }

    public int guiHeight()
    {
        return 35;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/invis.png");
        int imageID = this.isVisible() ? 1 : 0;
        byte borderID = 0;

        if (this.inRect(x, y, this.buttonRect))
        {
            borderID = 1;
        }

        this.drawImage(gui, this.buttonRect, 0, this.buttonRect[3] * borderID);
        int srcY = this.buttonRect[3] * 2 + imageID * (this.buttonRect[3] - 2);
        this.drawImage(gui, this.buttonRect[0] + 1, this.buttonRect[1] + 1, 0, srcY, this.buttonRect[2] - 2, this.buttonRect[3] - 2);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, this.getStateName(), x, y, this.buttonRect);
    }

    public void update()
    {
        super.update();

        if (!this.isVisible() && !this.getCart().hasFuelForModule() && !this.getCart().worldObj.isRemote)
        {
            this.setIsVisible(true);
        }
    }

    private boolean isVisible()
    {
        return this.isPlaceholder() ? !this.getSimInfo().getInvisActive() : this.getDw(0) != 0;
    }

    private String getStateName()
    {
        return Localization.MODULES.ADDONS.INVISIBILITY.translate(new String[] {this.isVisible() ? "0" : "1"});
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && this.inRect(x, y, this.buttonRect))
        {
            this.sendPacket(0);
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.setIsVisible(!this.isVisible());
        }
    }

    public void setIsVisible(boolean val)
    {
        this.updateDw(0, val ? 1 : 0);
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public boolean shouldCartRender()
    {
        return this.isVisible();
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 1);
    }

    public int getConsumption(boolean isMoving)
    {
        return this.isVisible() ? super.getConsumption(isMoving) : 3;
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setBoolean(this.generateNBTName("Invis", id), !this.isVisible());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setIsVisible(!tagCompound.getBoolean(this.generateNBTName("Invis", id)));
    }

    public void doActivate(int id)
    {
        this.setIsVisible(false);
    }

    public void doDeActivate(int id)
    {
        this.setIsVisible(true);
    }

    public boolean isActive(int id)
    {
        return !this.isVisible();
    }
}
