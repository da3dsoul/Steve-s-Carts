package vswe.stevescarts.Modules.Hull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Carts.MinecartModular;

public class ModulePig extends ModuleHull
{
    private int oinkTimer = this.getRandomTimer();

    public ModulePig(MinecartModular cart)
    {
        super(cart);
    }

    private void oink()
    {
        this.getCart().worldObj.playSoundAtEntity(this.getCart(), "mob.pig.say", 1.0F, (this.getCart().rand.nextFloat() - this.getCart().rand.nextFloat()) * 0.2F + 1.0F);
    }

    private int getRandomTimer()
    {
        return this.oinkTimer = this.getCart().rand.nextInt(900) + 300;
    }

    public void update()
    {
        if (this.oinkTimer <= 0)
        {
            this.oink();
            this.oinkTimer = this.getRandomTimer();
        }
        else
        {
            --this.oinkTimer;
        }
    }

    private ItemStack getHelmet()
    {
        Entity rider = this.getCart().riddenByEntity;
        return rider != null && rider instanceof EntityLivingBase ? ((EntityLivingBase)rider).getEquipmentInSlot(4) : null;
    }

    public boolean hasHelment()
    {
        ItemStack item = this.getHelmet();
        return item != null && item.getItem() instanceof ItemArmor && ((ItemArmor)item.getItem()).armorType == 0;
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getHelmetResource(boolean isOverlay)
    {
        if (this.hasHelment())
        {
            ItemStack item = this.getHelmet();
            return (ItemArmor)item.getItem() == null ? null : RenderBiped.getArmorResource((AbstractClientPlayer)null, item, 0, isOverlay ? "overlay" : (String)null);
        }
        else
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean hasHelmetColor(boolean isOverlay)
    {
        return this.getHelmetColor(isOverlay) != -1;
    }

    @SideOnly(Side.CLIENT)
    public int getHelmetColor(boolean isOverlay)
    {
        if (this.hasHelment())
        {
            ItemStack item = this.getHelmet();
            return ((ItemArmor)item.getItem()).getColorFromItemStack(item, isOverlay ? 1 : 0);
        }
        else
        {
            return -1;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean getHelmetMultiRender()
    {
        if (this.hasHelment())
        {
            ItemStack item = this.getHelmet();
            return ((ItemArmor)item.getItem()).requiresMultipleRenderPasses();
        }
        else
        {
            return false;
        }
    }

    public int getConsumption(boolean isMoving)
    {
        return !isMoving ? super.getConsumption(isMoving) : 1;
    }
}
