package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;

public class ModuleShield extends ModuleAddon implements IActivatorModule
{
    private boolean shield = true;
    private float shieldDistance = 18.0F;
    private float shieldAngle;
    private int[] buttonRect = new int[] {20, 20, 24, 12};

    public ModuleShield(MinecartModular cart)
    {
        super(cart);
    }

    protected boolean shieldSetting()
    {
        return this.getShieldStatus();
    }

    public float getShieldDistance()
    {
        return this.shieldDistance;
    }

    public float getShieldAngle()
    {
        return this.shieldAngle;
    }

    public boolean hasShield()
    {
        return this.shield;
    }

    public void update()
    {
        super.update();

        if (this.hasShield() && !this.getCart().hasFuelForModule() && !this.getCart().worldObj.isRemote)
        {
            this.setShieldStatus(false);
        }

        if (this.shield)
        {
            this.getCart().extinguish();
        }

        if (!this.getShieldStatus() && this.shieldDistance > 0.0F)
        {
            this.shieldDistance -= 0.25F;

            if (this.shieldDistance <= 0.0F)
            {
                this.shield = false;
            }
        }
        else if (this.getShieldStatus() && this.shieldDistance < 18.0F)
        {
            this.shieldDistance += 0.25F;
            this.shield = true;
        }

        if (this.shield)
        {
            this.shieldAngle = (float)((double)(this.shieldAngle + 0.125F) % 314.1592653589793D);
        }
    }

    @Override
    public boolean receiveDamage()
    {
        return !this.hasShield();
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
        return 75;
    }

    public int guiHeight()
    {
        return 35;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public void setShieldStatus(boolean val)
    {
        if (!this.isPlaceholder())
        {
            this.updateDw(0, (byte)(val ? 1 : 0));
        }
    }

    private boolean getShieldStatus()
    {
        return this.isPlaceholder() ? this.getSimInfo().getShieldActive() : this.getDw(0) != 0;
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/shield.png");
        int imageID = this.getShieldStatus() ? 1 : 0;
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

    private String getStateName()
    {
        return Localization.MODULES.ADDONS.SHIELD.translate(new String[] {this.getShieldStatus() ? "1" : "0"});
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
            this.updateDw(0, this.getShieldStatus() ? 0 : 1);
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
        this.addDw(0, 0);
    }

    public int getConsumption(boolean isMoving)
    {
        return this.hasShield() ? 20 : super.getConsumption(isMoving);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setBoolean(this.generateNBTName("Shield", id), this.getShieldStatus());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setShieldStatus(tagCompound.getBoolean(this.generateNBTName("Shield", id)));
    }

    public void doActivate(int id)
    {
        this.setShieldStatus(true);
    }

    public void doDeActivate(int id)
    {
        this.setShieldStatus(false);
    }

    public boolean isActive(int id)
    {
        return this.getShieldStatus();
    }
}
