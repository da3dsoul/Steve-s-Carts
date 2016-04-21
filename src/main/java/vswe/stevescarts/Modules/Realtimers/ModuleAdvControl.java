package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ILeverModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Engines.ModuleEngine;

public class ModuleAdvControl extends ModuleBase implements ILeverModule
{
    private byte[] engineInformation;
    private int tripPacketTimer;
    private int enginePacketTimer;
    private byte keyinformation;
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean first = true;
    private int speedChangeCooldown;
    private boolean lastBackKey;
    private double odo;
    private double trip;
    private int[] buttonRect = new int[] {15, 20, 24, 12};

    public ModuleAdvControl(MinecartModular cart)
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

    @SideOnly(Side.CLIENT)
    public void renderOverlay(Minecraft minecraft)
    {
        ResourceHelper.bindResource("/gui/drive.png");
        int enginesEndAt;
        int speedGraphicHeight;

        if (this.engineInformation != null)
        {
            for (enginesEndAt = 0; enginesEndAt < this.getCart().getEngines().size(); ++enginesEndAt)
            {
                this.drawImage(5, enginesEndAt * 15, 0, 0, 66, 15);
                speedGraphicHeight = this.engineInformation[enginesEndAt * 2] & 63;
                int itemRenderer = this.engineInformation[enginesEndAt * 2 + 1] & 63;
                ModuleEngine engine = (ModuleEngine)this.getCart().getEngines().get(enginesEndAt);
                float[] rgb = engine.getGuiBarColor();
                GL11.glColor4f(rgb[0], rgb[1], rgb[2], 1.0F);
                this.drawImage(7, enginesEndAt * 15 + 2, 66, 0, speedGraphicHeight, 5);
                this.drawImage(7, enginesEndAt * 15 + 2 + 6, 66, 6, itemRenderer, 5);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawImage(5, enginesEndAt * 15, 66 + engine.getPriority() * 7, 11, 7, 15);
            }
        }

        enginesEndAt = this.getCart().getEngines().size() * 15;
        this.drawImage(5, enginesEndAt, 0, 15, 32, 32);

        if (minecraft.gameSettings.keyBindForward.getIsKeyPressed())
        {
            this.drawImage(15, enginesEndAt + 5, 42, 20, 12, 6);
        }
        else if (minecraft.gameSettings.keyBindLeft.getIsKeyPressed())
        {
            this.drawImage(7, enginesEndAt + 13, 34, 28, 6, 12);
        }
        else if (minecraft.gameSettings.keyBindRight.getIsKeyPressed())
        {
            this.drawImage(29, enginesEndAt + 13, 56, 28, 6, 12);
        }

        speedGraphicHeight = this.getSpeedSetting() * 2;
        this.drawImage(14, enginesEndAt + 13 + 12 - speedGraphicHeight, 41, 40 - speedGraphicHeight, 14, speedGraphicHeight);
        this.drawImage(0, 0, 0, 67, 5, 130);
        this.drawImage(1, 1 + (256 - this.getCart().y()) / 2, 5, 67, 5, 1);
        this.drawImage(5, enginesEndAt + 32, 0, 47, 32, 20);
        this.drawImage(5, enginesEndAt + 52, 0, 47, 32, 20);
        this.drawImage(5, enginesEndAt + 72, 0, 47, 32, 20);
        minecraft.fontRenderer.drawString(Localization.MODULES.ATTACHMENTS.ODO.translate(new String[0]), 7, enginesEndAt + 52 + 2, 4210752);
        minecraft.fontRenderer.drawString(this.distToString(this.odo), 7, enginesEndAt + 52 + 11, 4210752);
        minecraft.fontRenderer.drawString(Localization.MODULES.ATTACHMENTS.TRIP.translate(new String[0]), 7, enginesEndAt + 52 + 22, 4210752);
        minecraft.fontRenderer.drawString(this.distToString(this.trip), 7, enginesEndAt + 52 + 31, 4210752);
        RenderItem var7 = new RenderItem();
        var7.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, new ItemStack(Items.clock, 1), 5, enginesEndAt + 32 + 3);
        var7.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, new ItemStack(Items.compass, 1), 21, enginesEndAt + 32 + 3);
    }

    private String distToString(double dist)
    {
        int i;

        for (i = 0; dist >= 1000.0D; ++i)
        {
            dist /= 1000.0D;
        }

        byte val;

        if (dist >= 100.0D)
        {
            val = 1;
        }
        else if (dist >= 10.0D)
        {
            val = 10;
        }
        else
        {
            val = 100;
        }

        double d = (double)Math.round(dist * (double)val) / (double)val;
        String s;

        if (d == (double)((int)d))
        {
            s = String.valueOf((int)d);
        }
        else
        {
            s = String.valueOf(d);
        }

        while (s.length() < (s.indexOf(46) != -1 ? 4 : 3))
        {
            if (s.indexOf(46) != -1)
            {
                s = s + "0";
            }
            else
            {
                s = s + ".0";
            }
        }

        s = s + Localization.MODULES.ATTACHMENTS.DISTANCES.translate(new String[] {String.valueOf(i)});
        return s;
    }

    public ModuleBase.RAILDIRECTION getSpecialRailDirection(int x, int y, int z)
    {
        return this.isForwardKeyDown() ? ModuleBase.RAILDIRECTION.FORWARD : (this.isLeftKeyDown() ? ModuleBase.RAILDIRECTION.LEFT : (this.isRightKeyDown() ? ModuleBase.RAILDIRECTION.RIGHT : ModuleBase.RAILDIRECTION.DEFAULT));
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.engineInformation = data;
        }
        else if (id == 1)
        {
            if (this.getCart().riddenByEntity != null && this.getCart().riddenByEntity instanceof EntityPlayer && this.getCart().riddenByEntity == player)
            {
                this.keyinformation = data[0];
                this.getCart().resetRailDirection();
            }
        }
        else if (id == 2)
        {
            int intOdo = 0;
            int intTrip = 0;

            for (int i = 0; i < 4; ++i)
            {
                int temp = data[i];

                if (temp < 0)
                {
                    temp += 256;
                }

                intOdo |= temp << i * 8;
                temp = data[i + 4];

                if (temp < 0)
                {
                    temp += 256;
                }

                intTrip |= temp << i * 8;
            }

            this.odo = (double)intOdo;
            this.trip = (double)intTrip;
        }
        else if (id == 3)
        {
            this.trip = 0.0D;
            this.tripPacketTimer = 0;
        }
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote && this.getCart().riddenByEntity != null && this.getCart().riddenByEntity instanceof EntityPlayer)
        {
            if (this.enginePacketTimer == 0)
            {
                this.sendEnginePacket((EntityPlayer)this.getCart().riddenByEntity);
                this.enginePacketTimer = 15;
            }
            else
            {
                --this.enginePacketTimer;
            }

            if (this.tripPacketTimer == 0)
            {
                this.sendTripPacket((EntityPlayer)this.getCart().riddenByEntity);
                this.tripPacketTimer = 500;
            }
            else
            {
                --this.tripPacketTimer;
            }
        }
        else
        {
            this.enginePacketTimer = 0;
            this.tripPacketTimer = 0;
        }

        if (this.getCart().worldObj.isRemote)
        {
            this.encodeKeys();
        }

        if (!this.lastBackKey && this.isBackKeyDown())
        {
            this.turnback();
        }

        this.lastBackKey = this.isBackKeyDown();

        if (!this.getCart().worldObj.isRemote)
        {
            if (this.speedChangeCooldown == 0)
            {
                if (!this.isJumpKeyDown() || !this.isSneakKeyDown())
                {
                    if (this.isJumpKeyDown())
                    {
                        this.setSpeedSetting(this.getSpeedSetting() + 1);
                        this.speedChangeCooldown = 8;
                    }
                    else if (this.isSneakKeyDown())
                    {
                        this.setSpeedSetting(this.getSpeedSetting() - 1);
                        this.speedChangeCooldown = 8;
                    }
                    else
                    {
                        this.speedChangeCooldown = 0;
                    }
                }
            }
            else
            {
                --this.speedChangeCooldown;
            }

            if (this.isForwardKeyDown() && this.isLeftKeyDown() && this.isRightKeyDown() && this.getCart().riddenByEntity != null && this.getCart().riddenByEntity instanceof EntityPlayer)
            {
                this.getCart().riddenByEntity.mountEntity(this.getCart());
                this.keyinformation = 0;
            }
        }

        double x = this.getCart().posX - this.lastPosX;
        double y = this.getCart().posY - this.lastPosY;
        double z = this.getCart().posZ - this.lastPosZ;
        this.lastPosX = this.getCart().posX;
        this.lastPosY = this.getCart().posY;
        this.lastPosZ = this.getCart().posZ;
        double dist = Math.sqrt(x * x + y * y + z * z);

        if (!this.first)
        {
            this.odo += dist;
            this.trip += dist;
        }
        else
        {
            this.first = false;
        }
    }

    public double getPushFactor()
    {
        switch (this.getSpeedSetting())
        {
            case 1:
                return 0.01D;

            case 2:
                return 0.03D;

            case 3:
                return 0.05D;

            case 4:
                return 0.07D;

            case 5:
                return 0.09D;

            case 6:
                return 0.11D;

            default:
                return super.getPushFactor();
        }
    }

    private void encodeKeys()
    {
        if (this.getCart().riddenByEntity != null && this.getCart().riddenByEntity instanceof EntityPlayer && this.getCart().riddenByEntity == this.getClientPlayer())
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            byte oldVal = this.keyinformation;
            this.keyinformation = 0;
            this.keyinformation |= (byte)((minecraft.gameSettings.keyBindForward.getIsKeyPressed() ? 1 : 0) << 0);
            this.keyinformation |= (byte)((minecraft.gameSettings.keyBindLeft.getIsKeyPressed() ? 1 : 0) << 1);
            this.keyinformation |= (byte)((minecraft.gameSettings.keyBindRight.getIsKeyPressed() ? 1 : 0) << 2);
            this.keyinformation |= (byte)((minecraft.gameSettings.keyBindBack.getIsKeyPressed() ? 1 : 0) << 3);
            this.keyinformation |= (byte)((minecraft.gameSettings.keyBindJump.getIsKeyPressed() ? 1 : 0) << 4);
            this.keyinformation |= (byte)((minecraft.gameSettings.keyBindSneak.getIsKeyPressed() ? 1 : 0) << 5);

            if (oldVal != this.keyinformation)
            {
                PacketHandler.sendPacket(this.getCart(), 1 + this.getPacketStart(), new byte[] {this.keyinformation});
            }
        }
    }

    private boolean isForwardKeyDown()
    {
        return (this.keyinformation & 1) != 0;
    }

    private boolean isLeftKeyDown()
    {
        return (this.keyinformation & 2) != 0;
    }

    private boolean isRightKeyDown()
    {
        return (this.keyinformation & 4) != 0;
    }

    private boolean isBackKeyDown()
    {
        return (this.keyinformation & 8) != 0;
    }

    private boolean isJumpKeyDown()
    {
        return (this.keyinformation & 16) != 0;
    }

    private boolean isSneakKeyDown()
    {
        return (this.keyinformation & 32) != 0;
    }

    private void sendTripPacket(EntityPlayer player)
    {
        byte[] data = new byte[8];
        int intOdo = (int)this.odo;
        int intTrip = (int)this.trip;

        for (int i = 0; i < 4; ++i)
        {
            data[i] = (byte)((intOdo & 255 << i * 8) >> i * 8);
            data[i + 4] = (byte)((intTrip & 255 << i * 8) >> i * 8);
        }

        this.sendPacket(2, data, player);
    }

    private void sendEnginePacket(EntityPlayer player)
    {
        int engineCount = this.getCart().getEngines().size();
        byte[] data = new byte[engineCount * 2];

        for (int i = 0; i < this.getCart().getEngines().size(); ++i)
        {
            ModuleEngine engine = (ModuleEngine)this.getCart().getEngines().get(i);
            int totalfuel = engine.getTotalFuel();
            short fuelInTopBar = 20000;
            byte maxBarLength = 62;
            float percentage = (float)(totalfuel % fuelInTopBar) / (float)fuelInTopBar;
            int upperBarLength = (int)((float)maxBarLength * percentage);
            int lowerBarLength = totalfuel / fuelInTopBar;

            if (lowerBarLength > maxBarLength)
            {
                lowerBarLength = maxBarLength;
            }

            data[i * 2] = (byte)(upperBarLength & 63);
            data[i * 2 + 1] = (byte)(lowerBarLength & 63);
        }

        this.sendPacket(0, data, player);
    }

    public int numberOfPackets()
    {
        return 4;
    }

    private void setSpeedSetting(int val)
    {
        if (val >= 0 && val <= 6)
        {
            this.updateDw(0, val);
        }
    }

    private int getSpeedSetting()
    {
        return this.isPlaceholder() ? 1 : this.getDw(0);
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public boolean stopEngines()
    {
        return this.getSpeedSetting() == 0;
    }

    public int getConsumption(boolean isMoving)
    {
        if (!isMoving)
        {
            return super.getConsumption(isMoving);
        }
        else
        {
            switch (this.getSpeedSetting())
            {
                case 4:
                    return 1;

                case 5:
                    return 3;

                case 6:
                    return 5;

                default:
                    return super.getConsumption(isMoving);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/advlever.png");

        if (this.inRect(x, y, this.buttonRect))
        {
            this.drawImage(gui, this.buttonRect, 0, this.buttonRect[3]);
        }
        else
        {
            this.drawImage(gui, this.buttonRect, 0, 0);
        }
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CONTROL_RESET.translate(new String[0]), x, y, this.buttonRect);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && this.inRect(x, y, this.buttonRect))
        {
            this.sendPacket(3);
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.CONTROL_SYSTEM.translate(new String[0]), 8, 6, 4210752);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Speed", id), (byte)this.getSpeedSetting());
        tagCompound.setDouble(this.generateNBTName("ODO", id), this.odo);
        tagCompound.setDouble(this.generateNBTName("TRIP", id), this.trip);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setSpeedSetting(tagCompound.getByte(this.generateNBTName("Speed", id)));
        this.odo = tagCompound.getDouble(this.generateNBTName("ODO", id));
        this.trip = tagCompound.getDouble(this.generateNBTName("TRIP", id));
    }

    public float getWheelAngle()
    {
        if (!this.isForwardKeyDown())
        {
            if (this.isLeftKeyDown())
            {
                return 0.3926991F;
            }

            if (this.isRightKeyDown())
            {
                return -0.3926991F;
            }
        }

        return 0.0F;
    }

    public float getLeverState()
    {
        return this.isPlaceholder() ? 0.0F : (float)this.getSpeedSetting() / 6.0F;
    }

    public void postUpdate()
    {
        if (this.getCart().worldObj.isRemote && this.getCart().riddenByEntity != null && this.getCart().riddenByEntity instanceof EntityPlayer && this.getCart().riddenByEntity == this.getClientPlayer())
        {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }
}
