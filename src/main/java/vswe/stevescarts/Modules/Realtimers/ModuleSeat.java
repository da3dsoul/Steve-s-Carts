package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleSeat extends ModuleBase
{
    private int[] buttonRect = new int[] {20, 20, 24, 12};
    private boolean relative;
    private float chairAngle;

    public ModuleSeat(MinecartModular cart)
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
        return 55;
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
        ResourceHelper.bindResource("/gui/chair.png");
        int imageID = this.getState();
        byte borderID = 0;

        if (this.inRect(x, y, this.buttonRect))
        {
            if (imageID == 0)
            {
                borderID = 2;
            }
            else
            {
                borderID = 1;
            }
        }

        this.drawImage(gui, this.buttonRect, 0, this.buttonRect[3] * borderID);
        int srcY = this.buttonRect[3] * 3 + imageID * (this.buttonRect[3] - 2);
        this.drawImage(gui, this.buttonRect[0] + 1, this.buttonRect[1] + 1, 0, srcY, this.buttonRect[2] - 2, this.buttonRect[3] - 2);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, this.getStateName(), x, y, this.buttonRect);
    }

    private int getState()
    {
        return this.getCart().riddenByEntity == null ? 1 : (this.getCart().riddenByEntity == this.getClientPlayer() ? 2 : 0);
    }

    private String getStateName()
    {
        return Localization.MODULES.ATTACHMENTS.SEAT_MESSAGE.translate(new String[] {String.valueOf(this.getState())});
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
        if (id == 0 && player != null)
        {
            if (this.getCart().riddenByEntity == null)
            {
                player.mountEntity(this.getCart());
            }
            else if (this.getCart().riddenByEntity == player)
            {
                player.mountEntity((Entity)null);
            }
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public void update()
    {
        super.update();

        if (this.getCart().riddenByEntity != null)
        {
            this.relative = false;
            this.chairAngle = (float)(Math.PI + Math.PI * (double)this.getCart().riddenByEntity.rotationYaw / 180.0D);
        }
        else
        {
            this.relative = true;
            this.chairAngle = ((float)Math.PI / 2F);
        }
    }

    public float getChairAngle()
    {
        return this.chairAngle;
    }

    public boolean useRelativeRender()
    {
        return this.relative;
    }

    public float mountedOffset(Entity rider)
    {
        return -0.1F;
    }
}
