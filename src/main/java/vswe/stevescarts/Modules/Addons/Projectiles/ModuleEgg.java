package vswe.stevescarts.Modules.Addons.Projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleEgg extends ModuleProjectile
{
    public ModuleEgg(MinecartModular cart)
    {
        super(cart);
    }

    public boolean isValidProjectile(ItemStack item)
    {
        return item.getItem() == Items.egg;
    }

    public Entity createProjectile(Entity target, ItemStack item)
    {
        return new EntityEgg(this.getCart().worldObj);
    }
}
