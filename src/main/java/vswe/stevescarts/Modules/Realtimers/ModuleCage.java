package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleCage extends ModuleBase implements IActivatorModule
{
    private int[] autoRect = new int[] {15, 20, 24, 12};
    private int[] manualRect;
    private ModuleCage.EntityNearestTarget sorter;
    private int cooldown;
    private boolean disablePickup;

    public ModuleCage(MinecartModular cart)
    {
        super(cart);
        this.manualRect = new int[] {this.autoRect[0] + this.autoRect[2] + 5, this.autoRect[1], this.autoRect[2], this.autoRect[3]};
        this.sorter = new ModuleCage.EntityNearestTarget(this.getCart());
        this.cooldown = 0;
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
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/cage.png");
        this.drawButton(gui, x, y, this.autoRect, this.disablePickup ? 2 : 3);
        this.drawButton(gui, x, y, this.manualRect, this.isCageEmpty() ? 0 : 1);
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

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAGE_AUTO.translate(new String[] {this.disablePickup ? "0" : "1"}), x, y, this.autoRect);
        this.drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAGE.translate(new String[] {this.isCageEmpty() ? "0" : "1"}), x, y, this.manualRect);
    }

    private boolean isCageEmpty()
    {
        return this.getCart().riddenByEntity == null;
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            if (this.inRect(x, y, this.autoRect))
            {
                this.sendPacket(0);
            }
            else if (this.inRect(x, y, this.manualRect))
            {
                this.sendPacket(1);
            }
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.disablePickup = !this.disablePickup;
        }
        else if (id == 1)
        {
            if (!this.isCageEmpty())
            {
                this.manualDrop();
            }
            else
            {
                this.manualPickUp();
            }
        }
    }

    public int numberOfPackets()
    {
        return 2;
    }

    public void update()
    {
        super.update();

        if (this.cooldown > 0)
        {
            --this.cooldown;
        }
        else if (!this.disablePickup)
        {
            this.pickUpCreature(2);
            this.cooldown = 20;
        }
    }

    private void manualDrop()
    {
        if (!this.isCageEmpty())
        {
            this.getCart().riddenByEntity.mountEntity((Entity)null);
            this.cooldown = 20;
        }
    }

    private void manualPickUp()
    {
        this.pickUpCreature(5);
    }

    private void pickUpCreature(int searchDistance)
    {
        if (!this.getCart().worldObj.isRemote && this.isCageEmpty())
        {
            List entities = this.getCart().worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getCart().boundingBox.expand((double)searchDistance, 4.0D, (double)searchDistance));
            Collections.sort(entities, this.sorter);
            Iterator itt = entities.iterator();
            EntityLivingBase target;

            do
            {
                if (!itt.hasNext())
                {
                    return;
                }

                target = (EntityLivingBase)itt.next();
            }
            while (target instanceof EntityPlayer || target instanceof EntityIronGolem || target instanceof EntityDragon || target instanceof EntitySlime || target instanceof EntityWaterMob || target instanceof EntityWither || target instanceof EntityEnderman || target instanceof EntitySpider && !(target instanceof EntityCaveSpider) || target instanceof EntityGiantZombie || target instanceof EntityFlying || target instanceof EntitySkeleton && ((EntitySkeleton)target).getSkeletonType() == 1 || target.ridingEntity != null);

            target.mountEntity(this.getCart());
        }
    }

    public float mountedOffset(Entity rider)
    {
        return rider instanceof EntityBat ? 0.5F : (!(rider instanceof EntityZombie) && !(rider instanceof EntitySkeleton) ? super.mountedOffset(rider) : -0.75F);
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)((byte)(this.disablePickup ? 1 : 0)));
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.disablePickup = data != 0;
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setBoolean(this.generateNBTName("disablePickup", id), this.disablePickup);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.disablePickup = tagCompound.getBoolean(this.generateNBTName("disablePickup", id));
    }

    public boolean isActive(int id)
    {
        return id == 0 ? !this.disablePickup : !this.isCageEmpty();
    }

    public void doActivate(int id)
    {
        if (id == 0)
        {
            this.disablePickup = false;
        }
        else
        {
            this.manualPickUp();
        }
    }

    public void doDeActivate(int id)
    {
        if (id == 0)
        {
            this.disablePickup = true;
        }
        else
        {
            this.manualDrop();
        }
    }

    private static class EntityNearestTarget implements Comparator
    {
        private Entity entity;

        public EntityNearestTarget(Entity entity)
        {
            this.entity = entity;
        }

        public int compareDistanceSq(Entity entity1, Entity entity2)
        {
            double distance1 = this.entity.getDistanceSqToEntity(entity1);
            double distance2 = this.entity.getDistanceSqToEntity(entity2);
            return distance1 < distance2 ? -1 : (distance1 > distance2 ? 1 : 0);
        }

        public int compare(Object obj1, Object obj2)
        {
            return this.compareDistanceSq((Entity)obj1, (Entity)obj2);
        }
    }
}
