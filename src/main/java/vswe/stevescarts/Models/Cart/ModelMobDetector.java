package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;

@SideOnly(Side.CLIENT)
public class ModelMobDetector extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/mobDetectorModel.png");
    ModelRenderer base = new ModelRenderer(this, 0, 0);

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelMobDetector()
    {
        this.AddRenderer(this.base);
        this.base.addBox(-1.0F, -2.0F, -1.0F, 2, 4, 2, 0.0F);
        this.base.setRotationPoint(0.0F, -14.0F, -0.0F);
        ModelRenderer body = new ModelRenderer(this, 0, 8);
        this.base.addChild(body);
        this.fixSize(body);
        body.addBox(-2.5F, -1.5F, -0.5F, 5, 3, 1, 0.0F);
        body.setRotationPoint(0.0F, -1.5F, -1.5F);
        int receiver;
        ModelRenderer dot;

        for (receiver = 0; receiver < 2; ++receiver)
        {
            dot = new ModelRenderer(this, 0, 13);
            body.addChild(dot);
            this.fixSize(dot);
            dot.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
            dot.setRotationPoint(0.0F, 2.0F * (float)(receiver * 2 - 1), -1.0F);
        }

        for (receiver = 0; receiver < 2; ++receiver)
        {
            dot = new ModelRenderer(this, 12, 13);
            body.addChild(dot);
            this.fixSize(dot);
            dot.addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
            dot.setRotationPoint(3.0F * (float)(receiver * 2 - 1), 0.0F, -1.0F);
            dot.rotateAngleZ = ((float)Math.PI / 2F);
        }

        ModelRenderer var4 = new ModelRenderer(this, 8, 0);
        body.addChild(var4);
        this.fixSize(var4);
        var4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        var4.setRotationPoint(0.0F, 0.0F, -1.0F);
        var4.rotateAngleY = ((float)Math.PI / 2F);
        dot = new ModelRenderer(this, 8, 2);
        body.addChild(dot);
        this.fixSize(dot);
        dot.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        dot.setRotationPoint(0.0F, 0.0F, -2.0F);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.base.rotateAngleY = module == null ? 0.0F : ((ModuleShooterAdv)module).getDetectorAngle() + yaw;
    }
}
