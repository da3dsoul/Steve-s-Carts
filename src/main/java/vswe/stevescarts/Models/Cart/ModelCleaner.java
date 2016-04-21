package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelCleaner extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/cleanerModel.png");

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
        return 32;
    }

    public ModelCleaner()
    {
        ModelRenderer box = new ModelRenderer(this, 0, 0);
        this.AddRenderer(box);
        box.addBox(-4.0F, -3.0F, -4.0F, 8, 6, 8, 0.0F);
        box.setRotationPoint(4.0F, -0.0F, -0.0F);
        ModelRenderer connectiontube;

        for (int tube = 0; tube < 2; ++tube)
        {
            connectiontube = new ModelRenderer(this, 0, 14);
            this.AddRenderer(connectiontube);
            connectiontube.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
            connectiontube.setRotationPoint(4.0F, -0.0F, -5.0F * (float)(tube * 2 - 1));
        }

        ModelRenderer var6 = new ModelRenderer(this, 0, 14);
        this.AddRenderer(var6);
        var6.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
        var6.setRotationPoint(-1.0F, 0.0F, 0.0F);
        var6.rotateAngleY = ((float)Math.PI / 2F);

        for (int var7 = 0; var7 < 2; ++var7)
        {
            ModelRenderer i = new ModelRenderer(this, 0, 14);
            this.AddRenderer(i);
            i.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
            i.setRotationPoint(-7.0F, -0.0F, -3.0F * (float)(var7 * 2 - 1));
            i.rotateAngleY = ((float)Math.PI / 2F);
        }

        connectiontube = new ModelRenderer(this, 0, 20);
        this.AddRenderer(connectiontube);
        connectiontube.addBox(-5.0F, -5.0F, -1.0F, 10, 4, 4, 0.0F);
        connectiontube.setRotationPoint(-5.0F, 3.0F, 0.0F);
        connectiontube.rotateAngleY = ((float)Math.PI / 2F);

        for (int var8 = 0; var8 < 2; ++var8)
        {
            ModelRenderer externaltube = new ModelRenderer(this, 0, 14);
            this.AddRenderer(externaltube);
            externaltube.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
            externaltube.setRotationPoint(-10.95F, -0.0F, -3.05F * (float)(var8 * 2 - 1));
            externaltube.rotateAngleY = ((float)Math.PI / 2F);
        }
    }
}
