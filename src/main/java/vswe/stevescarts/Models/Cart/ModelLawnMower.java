package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleFlowerRemover;

@SideOnly(Side.CLIENT)
public class ModelLawnMower extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/lawnmowerModel.png");
    private ArrayList<ModelRenderer> bladepins = new ArrayList();

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureWidth()
    {
        return 64;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelLawnMower()
    {
        this.createSide(false);
        this.createSide(true);
    }

    private void createSide(boolean opposite)
    {
        ModelRenderer anchor = new ModelRenderer(this, 0, 0);
        this.AddRenderer(anchor);

        if (opposite)
        {
            anchor.rotateAngleY = (float)Math.PI;
        }

        ModelRenderer base = new ModelRenderer(this, 0, 0);
        anchor.addChild(base);
        this.fixSize(base);
        base.addBox(-11.5F, -3.0F, -1.0F, 23, 6, 2, 0.0F);
        base.setRotationPoint(0.0F, -1.5F, -9.0F);

        for (int i = 0; i < 2; ++i)
        {
            ModelRenderer arm = new ModelRenderer(this, 0, 8);
            base.addChild(arm);
            this.fixSize(arm);
            arm.addBox(-8.0F, -1.5F, -1.5F, 16, 3, 3, 0.0F);
            arm.setRotationPoint(-8.25F + (float)i * 16.5F, 0.0F, -8.0F);
            arm.rotateAngleY = ((float)Math.PI / 2F);
            ModelRenderer arm2 = new ModelRenderer(this, 0, 14);
            arm.addChild(arm2);
            this.fixSize(arm2);
            arm2.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
            arm2.setRotationPoint(6.5F, 3.0F, 0.0F);
            arm2.rotateAngleZ = ((float)Math.PI / 2F);
            ModelRenderer bladepin = new ModelRenderer(this, 0, 20);
            arm2.addChild(bladepin);
            this.fixSize(bladepin);
            bladepin.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
            bladepin.setRotationPoint(2.5F, 0.0F, 0.0F);
            ModelRenderer bladeanchor = new ModelRenderer(this, 0, 0);
            bladepin.addChild(bladeanchor);
            bladeanchor.rotateAngleY = ((float)Math.PI / 2F);

            for (int j = 0; j < 4; ++j)
            {
                ModelRenderer blade = new ModelRenderer(this, 0, 22);
                bladeanchor.addChild(blade);
                this.fixSize(blade);
                blade.addBox(-1.5F, -1.5F, -0.5F, 8, 3, 1, 0.0F);
                blade.setRotationPoint(0.0F, 0.0F, (float)j * 0.01F);
                blade.rotateAngleZ = ((float)Math.PI / 2F) * ((float)j + (float)i * 0.5F);
                ModelRenderer bladetip = new ModelRenderer(this, 0, 26);
                blade.addChild(bladetip);
                this.fixSize(bladetip);
                bladetip.addBox(6.5F, -1.0F, -0.5F, 6, 2, 1, 0.0F);
                bladetip.setRotationPoint(0.0F, 0.0F, 0.005F);
            }

            this.bladepins.add(bladepin);
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll, float partialtime)
    {
        float angle = module == null ? 0.0F : ((ModuleFlowerRemover)module).getBladeAngle() + partialtime * ((ModuleFlowerRemover)module).getBladeSpindSpeed();

        for (int i = 0; i < this.bladepins.size(); ++i)
        {
            ModelRenderer bladepin = (ModelRenderer)this.bladepins.get(i);

            if (i % 2 == 0)
            {
                bladepin.rotateAngleX = angle;
            }
            else
            {
                bladepin.rotateAngleX = -angle;
            }
        }
    }
}
