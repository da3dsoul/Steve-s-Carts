package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCakeDynamite;

public class ModuleCakeServerDynamite extends ModuleCakeServer
{
    private int dynamiteCount;

    private int getMaxDynamiteCount()
    {
        return Math.min(StevesCarts.instance.maxDynamites, 25);
    }

    public ModuleCakeServerDynamite(MinecartModular cart)
    {
        super(cart);
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotCakeDynamite(this.getCart(), slotId, 8 + x * 18, 38 + y * 18);
    }

    public boolean dropOnDeath()
    {
        return this.dynamiteCount == 0;
    }

    public void onDeath()
    {
        if (this.dynamiteCount > 0)
        {
            this.explode();
        }
    }

    private void explode()
    {
        this.getCart().worldObj.createExplosion((Entity)null, this.getCart().posX, this.getCart().posY, this.getCart().posZ, (float)this.dynamiteCount * 0.8F, true);
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            ItemStack item = this.getStack(0);

            if (item != null && item.getItem().equals(ModItems.component) && item.getItemDamage() == 6 && this.dynamiteCount < this.getMaxDynamiteCount())
            {
                int count = Math.min(this.getMaxDynamiteCount() - this.dynamiteCount, item.stackSize);
                this.dynamiteCount += count;
                item.stackSize -= count;

                if (item.stackSize == 0)
                {
                    this.setStack(0, (ItemStack)null);
                }
            }
        }
    }

    public boolean onInteractFirst(EntityPlayer entityplayer)
    {
        if (this.dynamiteCount > 0)
        {
            this.explode();
            this.getCart().setDead();
            return true;
        }
        else
        {
            return super.onInteractFirst(entityplayer);
        }
    }
}
