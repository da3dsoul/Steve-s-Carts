package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotExplosion;

public class ModuleDynamite extends ModuleBase
{
    private boolean markerMoving;
    private int fuseStartX = super.guiWidth() + 5;
    private int fuseStartY = 27;
    private final int maxFuseLength = 150;

    public ModuleDynamite(MinecartModular cart)
    {
        super(cart);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.EXPLOSIVES.translate(new String[0]), 8, 6, 4210752);
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotExplosion(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    public boolean hasGui()
    {
        return true;
    }

    protected int getInventoryWidth()
    {
        return 1;
    }

    public void activatedByRail(int x, int y, int z, boolean active)
    {
        if (active && this.getFuse() == 0)
        {
            this.prime();
        }
    }

    public void update()
    {
        super.update();

        if (this.isPlaceholder())
        {
            if (this.getFuse() == 0 && this.getSimInfo().getShouldExplode())
            {
                this.setFuse(1);
            }
            else if (this.getFuse() != 0 && !this.getSimInfo().getShouldExplode())
            {
                this.setFuse(0);
            }
        }

        if (this.getFuse() > 0)
        {
            this.setFuse(this.getFuse() + 1);

            if (this.getFuse() == this.getFuseLength())
            {
                this.explode();

                if (!this.isPlaceholder())
                {
                    this.getCart().setDead();
                }
            }
        }
    }

    public int guiWidth()
    {
        return super.guiWidth() + 136;
    }

    private int[] getMovableMarker()
    {
        return new int[] {this.fuseStartX + (int)(105.0F * (1.0F - (float)this.getFuseLength() / 150.0F)), this.fuseStartY, 4, 10};
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/explosions.png");
        this.drawImage(gui, this.fuseStartX, this.fuseStartY + 3, 12, 0, 105, 4);
        this.drawImage(gui, this.fuseStartX + 105, this.fuseStartY - 4, 0, 10, 16, 16);
        this.drawImage(gui, this.fuseStartX + (int)(105.0F * (1.0F - (float)(this.getFuseLength() - this.getFuse()) / 150.0F)), this.fuseStartY, this.isPrimed() ? 8 : 4, 0, 4, 10);
        this.drawImage(gui, this.getMovableMarker(), 0, 0);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && this.getFuse() == 0 && this.inRect(x, y, this.getMovableMarker()))
        {
            this.markerMoving = true;
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (this.getFuse() != 0)
        {
            this.markerMoving = false;
        }
        else if (this.markerMoving)
        {
            int tempfuse = 150 - (int)((float)(x - this.fuseStartX) / 0.7F);

            if (tempfuse < 2)
            {
                tempfuse = 2;
            }
            else if (tempfuse > 150)
            {
                tempfuse = 150;
            }

            this.sendPacket(0, (byte)tempfuse);
        }

        if (button != -1)
        {
            this.markerMoving = false;
        }
    }

    private boolean isPrimed()
    {
        return this.getFuse() / 5 % 2 == 0 && this.getFuse() != 0;
    }

    private void explode()
    {
        if (this.isPlaceholder())
        {
            this.setFuse(1);
        }
        else
        {
            float f = this.explosionSize();
            this.setStack(0, (ItemStack)null);
            this.getCart().worldObj.createExplosion((Entity)null, this.getCart().posX, this.getCart().posY, this.getCart().posZ, f, true);
        }
    }

    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        this.createExplosives();
    }

    public boolean dropOnDeath()
    {
        return this.getFuse() == 0;
    }

    public void onDeath()
    {
        if (this.getFuse() > 0 && this.getFuse() < this.getFuseLength())
        {
            this.explode();
        }
    }

    public float explosionSize()
    {
        return this.isPlaceholder() ? this.getSimInfo().getExplosionSize() / 2.5F : (float)this.getDw(2) / 2.5F;
    }

    public void createExplosives()
    {
        if (!this.isPlaceholder() && !this.getCart().worldObj.isRemote)
        {
            int f = 8;

            if (ComponentTypes.DYNAMITE.isStackOfType(this.getStack(0)))
            {
                f += this.getStack(0).stackSize * 2;
            }

            this.updateDw(2, (byte)f);
        }
    }

    public int numberOfDataWatchers()
    {
        return 3;
    }

    public void initDw()
    {
        this.addDw(0, 0);
        this.addDw(1, 70);
        this.addDw(2, 8);
    }

    public int getFuse()
    {
        if (this.isPlaceholder())
        {
            return this.getSimInfo().fuse;
        }
        else
        {
            byte val = this.getDw(0);
            return val < 0 ? val + 256 : val;
        }
    }

    private void setFuse(int val)
    {
        if (this.isPlaceholder())
        {
            this.getSimInfo().fuse = val;
        }
        else
        {
            this.updateDw(0, (byte)val);
        }
    }

    public void setFuseLength(int val)
    {
        if (val > 150)
        {
            val = 150;
        }

        this.updateDw(1, (byte)val);
    }

    public int getFuseLength()
    {
        if (this.isPlaceholder())
        {
            return this.getSimInfo().getFuseLength();
        }
        else
        {
            byte val = this.getDw(1);
            return val < 0 ? val + 256 : val;
        }
    }

    public void prime()
    {
        this.setFuse(1);
    }

    protected int getMaxFuse()
    {
        return 150;
    }

    public int numberOfPackets()
    {
        return 1;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.setFuseLength(data[0]);
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.generateNBTName("FuseLength", id), (short)this.getFuseLength());
        tagCompound.setShort(this.generateNBTName("Fuse", id), (short)this.getFuse());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setFuseLength(tagCompound.getShort(this.generateNBTName("FuseLength", id)));
        this.setFuse(tagCompound.getShort(this.generateNBTName("Fuse", id)));
        this.createExplosives();
    }
}
