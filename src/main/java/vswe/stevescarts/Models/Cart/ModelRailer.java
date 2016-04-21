package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleRailer;

@SideOnly(Side.CLIENT)
public class ModelRailer extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/builderModel.png");
    private ModelRenderer[] rails;

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

    public ModelRailer(int railCount)
    {
        this.rails = new ModelRenderer[railCount];

        for (int r = 0; r < this.rails.length; ++r)
        {
            ModelRenderer railAnchor = new ModelRenderer(this);
            this.AddRenderer(railAnchor);
            this.rails[r] = railAnchor;
            railAnchor.setRotationPoint(0.0F, (float)(-r), 0.0F);
            ModelRenderer rail1 = new ModelRenderer(this, 18, 0);
            this.fixSize(rail1);
            railAnchor.addChild(rail1);
            rail1.addBox(1.0F, 8.0F, 0.5F, 2, 16, 1, 0.0F);
            rail1.setRotationPoint(-16.0F, -6.5F, -7.0F);
            rail1.rotateAngleZ = ((float)Math.PI * 3F / 2F);
            rail1.rotateAngleY = ((float)Math.PI * 3F / 2F);
            ModelRenderer rail2 = new ModelRenderer(this, 24, 0);
            this.fixSize(rail2);
            railAnchor.addChild(rail2);
            rail2.addBox(1.0F, 8.0F, 0.5F, 2, 16, 1, 0.0F);
            rail2.setRotationPoint(-16.0F, -6.5F, 3.0F);
            rail2.rotateAngleZ = ((float)Math.PI * 3F / 2F);
            rail2.rotateAngleY = ((float)Math.PI * 3F / 2F);

            for (int i = 0; i < 4; ++i)
            {
                ModelRenderer railbedMiddle = new ModelRenderer(this, 0, 0);
                this.fixSize(railbedMiddle);
                railAnchor.addChild(railbedMiddle);
                railbedMiddle.addBox(4.0F, 1.0F, 0.5F, 8, 2, 1, 0.0F);
                railbedMiddle.setRotationPoint(-8.0F + (float)(i * 4), -6.5F, -8.0F);
                railbedMiddle.rotateAngleZ = ((float)Math.PI * 3F / 2F);
                railbedMiddle.rotateAngleY = ((float)Math.PI * 3F / 2F);
                ModelRenderer railbedSide1 = new ModelRenderer(this, 0, 3);
                this.fixSize(railbedSide1);
                railAnchor.addChild(railbedSide1);
                railbedSide1.addBox(0.5F, 1.0F, 0.5F, 1, 2, 1, 0.0F);
                railbedSide1.setRotationPoint(-8.0F + (float)(i * 4), -6.5F, -7.5F);
                railbedSide1.rotateAngleZ = ((float)Math.PI * 3F / 2F);
                railbedSide1.rotateAngleY = ((float)Math.PI * 3F / 2F);
                ModelRenderer railbedSide2 = new ModelRenderer(this, 0, 3);
                this.fixSize(railbedSide2);
                railAnchor.addChild(railbedSide2);
                railbedSide2.addBox(0.5F, 1.0F, 0.5F, 1, 2, 1, 0.0F);
                railbedSide2.setRotationPoint(-8.0F + (float)(i * 4), -6.5F, 5.5F);
                railbedSide2.rotateAngleZ = ((float)Math.PI * 3F / 2F);
                railbedSide2.rotateAngleY = ((float)Math.PI * 3F / 2F);
            }
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        int valid = module == null ? 1 : ((ModuleRailer)module).getRails();

        for (int i = 0; i < this.rails.length; ++i)
        {
            this.rails[i].rotateAngleY = module == null ? 0.0F : ((ModuleRailer)module).getRailAngle(i);
            this.rails[i].isHidden = i >= valid;
        }
    }
}
