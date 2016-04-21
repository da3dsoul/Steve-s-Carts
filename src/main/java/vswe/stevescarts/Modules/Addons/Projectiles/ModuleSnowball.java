package vswe.stevescarts.Modules.Addons.Projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSnowball extends ModuleProjectile
{
    public ModuleSnowball(MinecartModular cart)
    {
        super(cart);
    }

    public boolean isValidProjectile(ItemStack item)
    {
        return item.getItem() == Items.snowball;
    }

    public Entity createProjectile(Entity target, ItemStack item)
    {
        return new EntitySnowball(this.getCart().worldObj);
    }
}
