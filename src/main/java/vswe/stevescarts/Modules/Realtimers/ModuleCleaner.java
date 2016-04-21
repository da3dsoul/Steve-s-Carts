package vswe.stevescarts.Modules.Realtimers;

import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleCleaner extends ModuleBase
{
    public ModuleCleaner(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            if (this.getCart().hasFuel())
            {
                this.suck();
            }

            this.clean();
        }
    }

    private double calculatemotion(double dif)
    {
        return dif > -0.5D && dif < 0.5D ? 0.0D : 1.0D / (dif * 15.0D);
    }

    private void suck()
    {
        List list = this.getCart().worldObj.getEntitiesWithinAABBExcludingEntity(this.getCart(), this.getCart().boundingBox.expand(3.0D, 1.0D, 3.0D));

        for (int e = 0; e < list.size(); ++e)
        {
            if (list.get(e) instanceof EntityItem)
            {
                EntityItem eItem = (EntityItem)list.get(e);

                if (eItem.delayBeforeCanPickup <= 10)
                {
                    double difX = this.getCart().posX - eItem.posX;
                    double difY = this.getCart().posY - eItem.posY;
                    double difZ = this.getCart().posZ - eItem.posZ;
                    eItem.motionX += this.calculatemotion(difX);
                    eItem.motionY += this.calculatemotion(difY);
                    eItem.motionZ += this.calculatemotion(difZ);
                }
            }
        }
    }

    private void clean()
    {
        List list = this.getCart().worldObj.getEntitiesWithinAABBExcludingEntity(this.getCart(), this.getCart().boundingBox.expand(1.0D, 0.5D, 1.0D));

        for (int e = 0; e < list.size(); ++e)
        {
            if (list.get(e) instanceof EntityItem)
            {
                EntityItem eItem = (EntityItem)list.get(e);

                if (eItem.delayBeforeCanPickup <= 10 && !eItem.isDead)
                {
                    int iItem = eItem.getEntityItem().stackSize;
                    this.getCart().addItemToChest(eItem.getEntityItem());

                    if (iItem != eItem.getEntityItem().stackSize)
                    {
                        this.getCart().worldObj.playSoundAtEntity(this.getCart(), "random.pop", 0.2F, ((this.getCart().rand.nextFloat() - this.getCart().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

                        if (eItem.getEntityItem().stackSize <= 0)
                        {
                            eItem.setDead();
                        }
                    }
                    else if (this.failPickup(eItem.getEntityItem()))
                    {
                        eItem.setDead();
                    }
                }
            }
            else if (list.get(e) instanceof EntityArrow)
            {
                EntityArrow var5 = (EntityArrow)list.get(e);

                if (Math.pow(var5.motionX, 2.0D) + Math.pow(var5.motionY, 2.0D) + Math.pow(var5.motionZ, 2.0D) < 0.2D && var5.arrowShake <= 0 && !var5.isDead)
                {
                    var5.arrowShake = 3;
                    ItemStack var6 = new ItemStack(Items.arrow, 1);
                    this.getCart().addItemToChest(var6);

                    if (var6.stackSize <= 0)
                    {
                        this.getCart().worldObj.playSoundAtEntity(this.getCart(), "random.pop", 0.2F, ((this.getCart().rand.nextFloat() - this.getCart().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        var5.setDead();
                    }
                    else if (this.failPickup(var6))
                    {
                        var5.setDead();
                    }
                }
            }
        }
    }

    private boolean failPickup(ItemStack item)
    {
        int x = this.normalize(this.getCart().pushZ);
        int z = this.normalize(this.getCart().pushX);

        if (x == 0 && z == 0)
        {
            return false;
        }
        else
        {
            if (this.getCart().worldObj.isRemote)
            {
                ;
            }

            EntityItem entityitem = new EntityItem(this.getCart().worldObj, this.getCart().posX, this.getCart().posY, this.getCart().posZ, item.copy());
            entityitem.delayBeforeCanPickup = 35;
            entityitem.motionX = (double)((float)x / 3.0F);
            entityitem.motionY = 0.15000000596046448D;
            entityitem.motionZ = (double)((float)z / 3.0F);
            this.getCart().worldObj.spawnEntityInWorld(entityitem);
            return true;
        }
    }

    private int normalize(double val)
    {
        return val == 0.0D ? 0 : (val > 0.0D ? 1 : -1);
    }
}
