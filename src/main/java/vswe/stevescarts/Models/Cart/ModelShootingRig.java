package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelShootingRig extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/rigModel.png");

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelShootingRig()
    {
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        this.AddRenderer(base);
        base.addBox(-7.0F, -0.5F, -3.0F, 14, 1, 6, 0.0F);
        base.setRotationPoint(0.0F, -5.5F, -0.0F);
        base.rotateAngleY = ((float)Math.PI / 2F);
        ModelRenderer pillar = new ModelRenderer(this, 0, 7);
        this.AddRenderer(pillar);
        pillar.addBox(-2.0F, -2.5F, -2.0F, 4, 5, 4, 0.0F);
        pillar.setRotationPoint(0.0F, -8.0F, -0.0F);
        ModelRenderer top = new ModelRenderer(this, 16, 7);
        this.AddRenderer(top);
        top.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 6, 0.0F);
        top.setRotationPoint(0.0F, -11.0F, -0.0F);
    }
}
