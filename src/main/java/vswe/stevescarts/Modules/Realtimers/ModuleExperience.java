package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleExperience extends ModuleBase
{
    private static final int MAX_EXPERIENCE_AMOUNT = 1500;
    private int experienceAmount;

    public ModuleExperience(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            List list = this.getCart().worldObj.getEntitiesWithinAABBExcludingEntity(this.getCart(), this.getCart().boundingBox.expand(3.0D, 1.0D, 3.0D));

            for (int e = 0; e < list.size(); ++e)
            {
                if (list.get(e) instanceof EntityXPOrb)
                {
                    this.experienceAmount += ((EntityXPOrb)list.get(e)).getXpValue();

                    if (this.experienceAmount > 1500)
                    {
                        this.experienceAmount = 1500;
                    }
                    else
                    {
                        ((Entity)list.get(e)).setDead();
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE_LEVEL.translate(new String[] {String.valueOf(this.experienceAmount), String.valueOf(1500)}) + "\n" + Localization.MODULES.ATTACHMENTS.EXPERIENCE_EXTRACT.translate(new String[0]) + "\n" + Localization.MODULES.ATTACHMENTS.EXPERIENCE_PLAYER_LEVEL.translate(new String[] {String.valueOf(this.getClientPlayer().experienceLevel)}), x, y, this.getContainerRect());
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.experienceAmount);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.experienceAmount = data;
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE.translate(new String[0]), 8, 6, 4210752);
    }

    private int[] getContainerRect()
    {
        return new int[] {10, 15, 26, 65};
    }

    private int[] getContentRect(float part)
    {
        int[] cont = this.getContainerRect();
        int normalHeight = cont[3] - 4;
        int currentHeight = (int)((float)normalHeight * part);
        return new int[] {cont[0] + 2, cont[1] + 2 + normalHeight - currentHeight, cont[2] - 4, currentHeight, normalHeight};
    }

    private void drawContent(GuiMinecart gui, int x, int y, int id)
    {
        int lowerLevel = id * 1500 / 3;
        int currentLevel = this.experienceAmount - lowerLevel;
        float part = 3.0F * (float)currentLevel / 1500.0F;

        if (part > 1.0F)
        {
            part = 1.0F;
        }

        int[] content = this.getContentRect(part);
        this.drawImage(gui, content, 4 + content[2] * (id + 1), content[4] - content[3]);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/experience.png");

        for (int i = 0; i < 3; ++i)
        {
            this.drawContent(gui, x, y, i);
        }

        this.drawImage(gui, this.getContainerRect(), 0, this.inRect(x, y, this.getContainerRect()) ? 65 : 0);
    }

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.inRect(x, y, this.getContainerRect()))
        {
            this.sendPacket(0);
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

    public int guiWidth()
    {
        return 70;
    }

    public int guiHeight()
    {
        return 84;
    }

    protected int numberOfPackets()
    {
        return 1;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            player.addExperience(Math.min(this.experienceAmount, 50));
            this.experienceAmount -= Math.min(this.experienceAmount, 50);
        }
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.experienceAmount = tagCompound.getShort(this.generateNBTName("Experience", id));
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.generateNBTName("Experience", id), (short)this.experienceAmount);
    }
}
