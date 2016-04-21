package vswe.stevescarts.Modules.Addons.Projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public abstract class ModuleProjectile extends ModuleAddon
{
    public ModuleProjectile(MinecartModular cart)
    {
        super(cart);
    }

    public abstract boolean isValidProjectile(ItemStack var1);

    public abstract Entity createProjectile(Entity var1, ItemStack var2);
}
