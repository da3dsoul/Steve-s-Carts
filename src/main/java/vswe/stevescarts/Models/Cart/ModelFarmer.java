package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;

@SideOnly(Side.CLIENT)
public class ModelFarmer extends ModelCartbase
{
    private ModelRenderer mainAnchor;
    private ModelRenderer anchor;
    private ModelRenderer[] outers;
    private ResourceLocation resource;

    public ResourceLocation getResource(ModuleBase module)
    {
        return this.resource;
    }

    protected int getTextureWidth()
    {
        return 128;
    }

    public float extraMult()
    {
        return 0.5F;
    }

    public ModelFarmer(ResourceLocation resource)
    {
        this.resource = resource;
        this.mainAnchor = new ModelRenderer(this);
        this.AddRenderer(this.mainAnchor);
        this.mainAnchor.setRotationPoint(-18.0F, 4.0F, 0.0F);

        for (int mainarm = -1; mainarm <= 1; mainarm += 2)
        {
            ModelRenderer start = new ModelRenderer(this, 26, 23);
            this.mainAnchor.addChild(start);
            this.fixSize(start);
            start.addBox(-1.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F);
            start.setRotationPoint(0.0F, 0.0F, (float)(mainarm * 17));
        }

        ModelRenderer var10 = new ModelRenderer(this, 0, 37);
        this.mainAnchor.addChild(var10);
        this.fixSize(var10);
        var10.addBox(-30.0F, -2.0F, -2.0F, 60, 4, 4, 0.0F);
        var10.setRotationPoint(8.0F, 0.0F, 0.0F);
        var10.rotateAngleY = ((float)Math.PI / 2F);

        for (int var11 = -1; var11 <= 1; var11 += 2)
        {
            ModelRenderer end = new ModelRenderer(this, 26, 27);
            this.mainAnchor.addChild(end);
            this.fixSize(end);
            end.addBox(-2.5F, -2.5F, -1.0F, 5, 5, 2, 0.0F);
            end.setRotationPoint(8.0F, 0.0F, (float)(var11 * 30));
            ModelRenderer i = new ModelRenderer(this, 26, 17);
            this.mainAnchor.addChild(i);
            this.fixSize(i);
            i.addBox(-1.0F, -2.0F, -1.0F, 16, 4, 2, 0.0F);
            i.setRotationPoint(8.0F, 0.0F, (float)(var11 * 32));
        }

        this.anchor = new ModelRenderer(this);
        this.mainAnchor.addChild(this.anchor);
        this.anchor.setRotationPoint(22.0F, 0.0F, 0.0F);
        float var12 = -1.5F;
        float var13 = 1.5F;
        ModelRenderer outer;
        ModelRenderer var16;

        for (float var14 = -1.5F; var14 <= 1.5F; ++var14)
        {
            int nailAnchor;

            for (nailAnchor = 0; nailAnchor < 6; ++nailAnchor)
            {
                outer = new ModelRenderer(this, 0, 0);
                this.anchor.addChild(outer);
                this.fixSize(outer);
                outer.addBox(-5.0F, -8.8F, -1.0F, 10, 4, 2, 0.0F);
                outer.setRotationPoint(0.0F, 0.0F, var14 * 20.0F + (float)(nailAnchor % 2) * 0.005F);
                outer.rotateAngleZ = (float)nailAnchor * ((float)Math.PI * 2F) / 6.0F;
            }

            if (var14 != var12 && var14 != var13)
            {
                for (nailAnchor = 0; nailAnchor < 3; ++nailAnchor)
                {
                    outer = new ModelRenderer(this, 26, 12);
                    this.anchor.addChild(outer);
                    this.fixSize(outer);
                    outer.addBox(-1.0F, -2.0F, -0.5F, 8, 4, 1, 0.0F);
                    outer.setRotationPoint(0.0F, 0.0F, var14 * 20.0F);
                    outer.rotateAngleZ = ((float)nailAnchor + 0.25F) * ((float)Math.PI * 2F) / 3.0F;
                }
            }
            else
            {
                var16 = new ModelRenderer(this, 0, 12);
                this.anchor.addChild(var16);
                this.fixSize(var16);
                var16.addBox(-6.0F, -6.0F, -0.5F, 12, 12, 1, 0.0F);
                var16.setRotationPoint(0.0F, 0.0F, var14 * 20.0F);
            }
        }

        int var15;

        for (var15 = 0; var15 < 6; ++var15)
        {
            var16 = new ModelRenderer(this, 0, 6);
            this.anchor.addChild(var16);
            this.fixSize(var16);
            var16.addBox(-30.0F, -1.7F, -1.0F, 60, 2, 2, 0.0F);
            var16.setRotationPoint(0.0F, 0.0F, (float)(var15 % 2) * 0.005F);
            var16.rotateAngleX = (float)var15 * ((float)Math.PI * 2F) / 6.0F;
            var16.rotateAngleY = ((float)Math.PI / 2F);
        }

        this.outers = new ModelRenderer[6];

        for (var15 = 0; var15 < 6; ++var15)
        {
            var16 = new ModelRenderer(this);
            this.anchor.addChild(var16);
            var16.rotateAngleX = this.nailRot(var15);
            var16.rotateAngleY = ((float)Math.PI / 2F);
            outer = new ModelRenderer(this, 0, 10);
            var16.addChild(outer);
            this.fixSize(outer);
            outer.addBox(-30.0F, -0.5F, -0.5F, 60, 1, 1, 0.0F);
            outer.setRotationPoint(0.0F, -8.8F, 0.0F);
            outer.rotateAngleX = (float)Math.PI;
            this.outers[var15] = outer;

            for (int j = -13; j <= 13; ++j)
            {
                if (Math.abs(j) > 6 || Math.abs(j) < 4)
                {
                    ModelRenderer nail = new ModelRenderer(this, 44, 13);
                    outer.addChild(nail);
                    this.fixSize(nail);
                    nail.addBox(-0.5F, -1.5F, -0.5F, 1, 3, 1, 0.0F);
                    nail.setRotationPoint((float)(j * 2), -2.0F, 0.0F);
                }
            }
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.mainAnchor.rotateAngleZ = module == null ? 3.926991F : -((ModuleFarmer)module).getRigAngle();
        float farmAngle = module == null ? 0.0F : ((ModuleFarmer)module).getFarmAngle();
        this.anchor.rotateAngleZ = -farmAngle;

        for (int i = 0; i < 6; ++i)
        {
            this.outers[i].rotateAngleX = farmAngle + this.nailRot(i);
        }
    }

    private float nailRot(int i)
    {
        return ((float)i + 0.5F) * ((float)Math.PI * 2F) / 6.0F;
    }
}
