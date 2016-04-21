package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleShield;

@SideOnly(Side.CLIENT)
public class ModelShield extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/shieldModel.png");
    private ModelRenderer[][] shieldAnchors;
    private ModelRenderer[][] shields = new ModelRenderer[4][5];

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureWidth()
    {
        return 8;
    }

    protected int getTextureHeight()
    {
        return 4;
    }

    public ModelShield()
    {
        this.shieldAnchors = new ModelRenderer[this.shields.length][this.shields[0].length];

        for (int i = 0; i < this.shields.length; ++i)
        {
            for (int j = 0; j < this.shields[i].length; ++j)
            {
                this.shieldAnchors[i][j] = new ModelRenderer(this);
                this.AddRenderer(this.shieldAnchors[i][j]);
                this.shields[i][j] = new ModelRenderer(this, 0, 0);
                this.fixSize(this.shields[i][j]);
                this.shieldAnchors[i][j].addChild(this.shields[i][j]);
                this.shields[i][j].addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
                this.shields[i][j].setRotationPoint(0.0F, 0.0F, 0.0F);
            }
        }
    }

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        if (render == null || module == null || ((ModuleShield)module).hasShield())
        {
            super.render(render, module, yaw, pitch, roll, mult, partialtime);
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        float shieldAngle = module == null ? 0.0F : ((ModuleShield)module).getShieldAngle();
        float shieldDistance = module == null ? 18.0F : ((ModuleShield)module).getShieldDistance();

        for (int i = 0; i < this.shields.length; ++i)
        {
            for (int j = 0; j < this.shields[i].length; ++j)
            {
                float a = shieldAngle + ((float)Math.PI * 2F) * ((float)j / (float)this.shields[i].length + (float)i / (float)this.shields.length);
                a = (float)((double)a % 314.1592653589793D);
                this.shieldAnchors[i][j].rotateAngleY = a;
                this.shields[i][j].rotationPointY = ((float)Math.sin((double)(a / 5.0F)) * 3.0F + ((float)i - (float)(this.shields.length - 1) / 2.0F) * 5.0F - 5.0F) * shieldDistance / 18.0F;
                this.shields[i][j].rotationPointZ = shieldDistance;
            }
        }
    }
}
