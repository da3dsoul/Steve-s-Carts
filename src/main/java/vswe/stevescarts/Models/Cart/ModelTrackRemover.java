package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelTrackRemover extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/removerModel.png");

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    public ModelTrackRemover()
    {
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        this.AddRenderer(base);
        base.addBox(-5.0F, -5.0F, -0.5F, 10, 10, 1, 0.0F);
        base.setRotationPoint(0.0F, -5.5F, -0.0F);
        base.rotateAngleX = ((float)Math.PI / 2F);
        ModelRenderer pipe = new ModelRenderer(this, 0, 11);
        this.AddRenderer(pipe);
        pipe.addBox(-2.5F, -2.5F, -2.5F, 6, 5, 5, 0.0F);
        pipe.setRotationPoint(0.0F, -9.5F, -0.0F);
        pipe.rotateAngleZ = ((float)Math.PI / 2F);
        ModelRenderer pipe2 = new ModelRenderer(this, 0, 21);
        pipe.addChild(pipe2);
        this.fixSize(pipe2);
        pipe2.addBox(-2.5F, -2.5F, -2.5F, 19, 5, 5, 0.0F);
        pipe2.setRotationPoint(0.005F, -0.005F, -0.005F);
        pipe2.rotateAngleZ = -((float)Math.PI / 2F);
        ModelRenderer pipe3 = new ModelRenderer(this, 22, 0);
        pipe2.addChild(pipe3);
        this.fixSize(pipe3);
        pipe3.addBox(-2.5F, -2.5F, -2.5F, 14, 5, 5, 0.0F);
        pipe3.setRotationPoint(14.005F, -0.005F, 0.005F);
        pipe3.rotateAngleZ = ((float)Math.PI / 2F);
        ModelRenderer end = new ModelRenderer(this, 0, 31);
        pipe3.addChild(end);
        this.fixSize(end);
        end.addBox(-7.0F, -11.0F, -0.5F, 14, 14, 1, 0.0F);
        end.setRotationPoint(12.0F, 0.0F, -0.0F);
        end.rotateAngleY = ((float)Math.PI / 2F);
    }
}
