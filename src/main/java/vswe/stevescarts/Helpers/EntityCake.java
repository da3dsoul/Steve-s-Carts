package vswe.stevescarts.Helpers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityCake extends EntityEgg
{
    public EntityCake(World world)
    {
        super(world);
    }

    public EntityCake(World world, EntityLiving thrower)
    {
        super(world, thrower);
    }

    public EntityCake(World world, double x, double y, double z)
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

            if (data.entityHit instanceof EntityPlayer)
            {
                EntityPlayer j = (EntityPlayer)data.entityHit;
                j.getFoodStats().addStats(14, 0.7F);
            }
        }
        else if (this.worldObj.isAirBlock((int)this.posX, (int)this.posY, (int)this.posZ) && this.worldObj.isSideSolid((int)this.posX, (int)this.posY - 1, (int)this.posZ, ForgeDirection.UP))
        {
            this.worldObj.setBlock((int)this.posX, (int)this.posY, (int)this.posZ, Blocks.cake);
        }

        for (int var3 = 0; var3 < 8; ++var3)
        {
            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}
