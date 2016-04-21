package vswe.stevescarts.Modules.Realtimers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleMobdetector;

public class ModuleShooterAdv extends ModuleShooter
{
    private ArrayList<ModuleMobdetector> detectors;
    private ModuleShooterAdv.EntityNearestTarget sorter = new ModuleShooterAdv.EntityNearestTarget(this.getCart());
    private float detectorAngle;

    public ModuleShooterAdv(MinecartModular cart)
    {
        super(cart);
    }

    public void preInit()
    {
        super.preInit();
        this.detectors = new ArrayList();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleMobdetector)
            {
                this.detectors.add((ModuleMobdetector)module);
            }
        }
    }

    protected void generatePipes(ArrayList<Integer> list)
    {
        list.add(Integer.valueOf(1));
    }

    protected int guiExtraWidth()
    {
        return 100;
    }

    protected int guiRequiredHeight()
    {
        return 10 + 10 * this.detectors.size();
    }

    private int[] getSelectionBox(int id)
    {
        return new int[] {90, id * 10 + (this.guiHeight() - 10 * this.detectors.size()) / 2, 8, 8};
    }

    protected void generateInterfaceRegions() {}

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.SHOOTER.translate(new String[0]), 8, 6, 4210752);

        for (int i = 0; i < this.detectors.size(); ++i)
        {
            int[] box = this.getSelectionBox(i);
            this.drawString(gui, ((ModuleMobdetector)this.detectors.get(i)).getName(), box[0] + 12, box[1], 4210752);
        }
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/mobdetector.png");

        for (int i = 0; i < this.detectors.size(); ++i)
        {
            int srcX = this.isOptionActive(i) ? 0 : 8;
            int srcY = this.inRect(x, y, this.getSelectionBox(i)) ? 8 : 0;
            this.drawImage(gui, this.getSelectionBox(i), srcX, srcY);
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            for (int i = 0; i < this.detectors.size(); ++i)
            {
                if (this.inRect(x, y, this.getSelectionBox(i)))
                {
                    this.sendPacket(0, (byte)i);
                    break;
                }
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button) {}

    public int numberOfGuiData()
    {
        return 0;
    }

    protected void checkGuiData(Object[] info) {}

    protected void Shoot()
    {
        this.setTimeToNext(15);

        if (this.getCart().hasFuel())
        {
            Entity target = this.getTarget();

            if (target != null)
            {
                if (this.hasProjectileItem())
                {
                    this.shootAtTarget(target);
                }
                else
                {
                    this.getCart().worldObj.playAuxSFX(1001, (int)this.getCart().posX, (int)this.getCart().posY, (int)this.getCart().posZ, 0);
                }
            }
        }
    }

    private void shootAtTarget(Entity target)
    {
        if (target != null)
        {
            Entity projectile = this.getProjectile(target, this.getProjectileItem(true));
            projectile.posY = this.getCart().posY + (double)this.getCart().getEyeHeight() - 0.10000000149011612D;
            double disX = target.posX - this.getCart().posX;
            double disY = target.posY + (double)target.getEyeHeight() - 0.699999988079071D - projectile.posY;
            double disZ = target.posZ - this.getCart().posZ;
            double dis = (double)MathHelper.sqrt_double(disX * disX + disZ * disZ);

            if (dis >= 1.0E-7D)
            {
                float theta = (float)(Math.atan2(disZ, disX) * 180.0D / Math.PI) - 90.0F;
                float phi = (float)(-(Math.atan2(disY, dis) * 180.0D / Math.PI));
                this.setRifleDirection((float)Math.atan2(disZ, disX));
                double disPX = disX / dis;
                double disPZ = disZ / dis;
                projectile.setLocationAndAngles(this.getCart().posX + disPX * 1.5D, projectile.posY, this.getCart().posZ + disPZ * 1.5D, theta, phi);
                projectile.yOffset = 0.0F;
                float disD5 = (float)dis * 0.2F;
                this.setHeading(projectile, disX, disY + (double)disD5, disZ, 1.6F, 0.0F);
            }

            this.getCart().worldObj.playSoundAtEntity(this.getCart(), "random.bow", 1.0F, 1.0F / (this.getCart().rand.nextFloat() * 0.4F + 0.8F));
            this.setProjectileDamage(projectile);
            this.setProjectileOnFire(projectile);
            this.setProjectileKnockback(projectile);
            this.getCart().worldObj.spawnEntityInWorld(projectile);
            this.damageEnchant();
        }
    }

    protected int getTargetDistance()
    {
        return 16;
    }

    private Entity getTarget()
    {
        List entities = this.getCart().worldObj.getEntitiesWithinAABB(Entity.class, this.getCart().boundingBox.expand((double)this.getTargetDistance(), 4.0D, (double)this.getTargetDistance()));
        Collections.sort(entities, this.sorter);
        Iterator itt = entities.iterator();

        while (itt.hasNext())
        {
            Entity target = (Entity)itt.next();

            if (target != this.getCart() && this.canSee(target))
            {
                for (int i = 0; i < this.detectors.size(); ++i)
                {
                    if (this.isOptionActive(i))
                    {
                        ModuleMobdetector detector = (ModuleMobdetector)this.detectors.get(i);

                        if (detector.isValidTarget(target))
                        {
                            return target;
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean canSee(Entity target)
    {
        return target == null ? false : this.getCart().worldObj.rayTraceBlocks(Vec3.createVectorHelper(this.getCart().posX, this.getCart().posY + (double)this.getCart().getEyeHeight(), this.getCart().posZ), Vec3.createVectorHelper(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ)) == null;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.switchOption(data[0]);
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public int numberOfDataWatchers()
    {
        return 2;
    }

    public void initDw()
    {
        this.addDw(0, 0);
        this.addDw(1, 0);
    }

    private void switchOption(int id)
    {
        byte val = this.getDw(0);
        val = (byte)(val ^ 1 << id);
        this.updateDw(0, val);
    }

    public void setOptions(byte val)
    {
        this.updateDw(0, val);
    }

    public byte selectedOptions()
    {
        return this.getDw(0);
    }

    private boolean isOptionActive(int id)
    {
        return (this.selectedOptions() & 1 << id) != 0;
    }

    protected boolean isPipeActive(int id)
    {
        return this.isPlaceholder() ? this.getSimInfo().getIsPipeActive() : this.selectedOptions() != 0;
    }

    public float getDetectorAngle()
    {
        return this.detectorAngle;
    }

    public void update()
    {
        super.update();

        if (this.isPipeActive(0))
        {
            this.detectorAngle = (float)((double)(this.detectorAngle + 0.1F) % (Math.PI * 2D));
        }
    }

    private void setRifleDirection(float val)
    {
        val /= ((float)Math.PI * 2F);
        val *= 256.0F;
        val %= 256.0F;

        if (val < 0.0F)
        {
            val += 256.0F;
        }

        this.updateDw(1, (byte)((int)val));
    }

    public float getRifleDirection()
    {
        float val;

        if (this.isPlaceholder())
        {
            val = 0.0F;
        }
        else
        {
            val = (float)this.getDw(1);
        }

        val /= 256.0F;
        val *= ((float)Math.PI * 2F);
        return val;
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Options", id), this.selectedOptions());
        this.saveTick(tagCompound, id);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setOptions(tagCompound.getByte(this.generateNBTName("Options", id)));
        this.loadTick(tagCompound, id);
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
