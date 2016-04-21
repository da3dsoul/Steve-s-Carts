package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;

@SideOnly(Side.CLIENT)
public class ModelSniperRifle extends ModelGun
{
    ModelRenderer anchor = new ModelRenderer(this);
    ModelRenderer gun;

    public ModelSniperRifle()
    {
        this.AddRenderer(this.anchor);
        this.gun = this.createGun(this.anchor);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.gun.rotateAngleZ = module == null ? 0.0F : ((ModuleShooterAdv)module).getPipeRotation(0);
        this.anchor.rotateAngleY = module == null ? 0.0F : (float)Math.PI + ((ModuleShooterAdv)module).getRifleDirection() + yaw;
    }
}
