package vswe.stevescarts.Listeners;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import vswe.stevescarts.Helpers.ComponentTypes;

public class MobDeathListener
{
    public MobDeathListener()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityLivingDeath(LivingDeathEvent event)
    {
        EntityLivingBase monster = event.entityLiving;

        if (!monster.worldObj.isRemote && event.source.getDamageType().equals("player"))
        {
            if (monster instanceof EntityMob && Math.random() < 0.1D)
            {
                this.dropItem(monster, ComponentTypes.STOLEN_PRESENT.getItemStack());
            }

            if (monster instanceof EntityBlaze && Math.random() < 0.12D)
            {
                this.dropItem(monster, ComponentTypes.RED_WRAPPING_PAPER.getItemStack());
            }
        }
    }

    private void dropItem(EntityLivingBase monster, ItemStack item)
    {
        EntityItem obj = new EntityItem(monster.worldObj, monster.posX, monster.posY, monster.posZ, item);
        obj.motionX = monster.worldObj.rand.nextGaussian() * 0.05000000074505806D;
        obj.motionY = monster.worldObj.rand.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
        obj.motionZ = monster.worldObj.rand.nextGaussian() * 0.05000000074505806D;
        monster.worldObj.spawnEntityInWorld(obj);
    }
}
