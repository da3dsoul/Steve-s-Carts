package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEasterEgg extends EntityEgg
{
    public EntityEasterEgg(World world)
    {
        super(world);
    }

    public EntityEasterEgg(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public EntityEasterEgg(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition data)
    {
        if (data.entityHit != null)
        {
            data.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.worldObj.isRemote)
        {
            if (this.rand.nextInt(8) == 0)
            {
                if (this.rand.nextInt(32) == 0)
                {
                    EntityPig j = new EntityPig(this.worldObj);
                    j.setGrowingAge(-24000);
                    j.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    this.worldObj.spawnEntityInWorld(j);
                }
                else
                {
                    EntityChicken var6 = new EntityChicken(this.worldObj);
                    var6.setGrowingAge(-24000);
                    var6.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    this.worldObj.spawnEntityInWorld(var6);
                }
            }
            else
            {
                ArrayList var7 = GiftItem.generateItems(this.rand, GiftItem.EasterList, 25 + this.rand.nextInt(300), 1);
                Iterator i$ = var7.iterator();

                while (i$.hasNext())
                {
                    ItemStack item = (ItemStack)i$.next();
                    EntityItem eItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, item);
                    eItem.motionX = this.rand.nextGaussian() * 0.05000000074505806D;
                    eItem.motionY = this.rand.nextGaussian() * 0.25D;
                    eItem.motionZ = this.rand.nextGaussian() * 0.05000000074505806D;
                    this.worldObj.spawnEntityInWorld(eItem);
                }
            }
        }

        for (int var8 = 0; var8 < 8; ++var8)
        {
            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}
