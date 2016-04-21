package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;

@SideOnly(Side.CLIENT)
public class ModelGun extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/gunModel.png");
    ModelRenderer[] guns;

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureWidth()
    {
        return 32;
    }

    protected int getTextureHeight()
    {
        return 8;
    }

    public ModelGun() {}

    public ModelGun(ArrayList<Integer> pipes)
    {
        this.guns = new ModelRenderer[pipes.size()];

        for (int i = 0; i < pipes.size(); ++i)
        {
            float angle = (float)(new int[] {3, 4, 5, 2, -1, 6, 1, 0, 7})[((Integer)pipes.get(i)).intValue()];
            angle *= ((float)Math.PI / 4F);
            ModelRenderer gunAnchorAnchor = new ModelRenderer(this);
            this.AddRenderer(gunAnchorAnchor);
            gunAnchorAnchor.rotateAngleY = angle;
            this.guns[i] = this.createGun(gunAnchorAnchor);
        }
    }

    protected ModelRenderer createGun(ModelRenderer parent)
    {
        ModelRenderer gunAnchor = new ModelRenderer(this);
        parent.addChild(gunAnchor);
        gunAnchor.setRotationPoint(2.5F, 0.0F, 0.0F);
        ModelRenderer gun = new ModelRenderer(this, 0, 16);
        this.fixSize(gun);
        gunAnchor.addChild(gun);
        gun.addBox(-1.5F, -2.5F, -1.5F, 7, 3, 3, 0.0F);
        gun.setRotationPoint(0.0F, -9.0F, 0.0F);
        return gun;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        for (int i = 0; i < this.guns.length; ++i)
        {
            this.guns[i].rotateAngleZ = module == null ? 0.0F : ((ModuleShooter)module).getPipeRotation(i);
        }
    }
}
