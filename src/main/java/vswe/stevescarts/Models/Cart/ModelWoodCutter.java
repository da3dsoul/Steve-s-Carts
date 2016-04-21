package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutter;

@SideOnly(Side.CLIENT)
public class ModelWoodCutter extends ModelCartbase
{
    private ModelRenderer[] anchors;
    private ResourceLocation resource;

    public ResourceLocation getResource(ModuleBase module)
    {
        return this.resource;
    }

    protected int getTextureWidth()
    {
        return 16;
    }

    protected int getTextureHeight()
    {
        return 8;
    }

    public ModelWoodCutter(ResourceLocation resource)
    {
        this.resource = resource;
        this.anchors = new ModelRenderer[5];

        for (int i = -2; i <= 2; ++i)
        {
            ModelRenderer anchor = new ModelRenderer(this);
            this.anchors[i + 2] = anchor;
            this.AddRenderer(anchor);
            ModelRenderer main = new ModelRenderer(this, 0, 0);
            anchor.addChild(main);
            this.fixSize(main);
            main.addBox(-3.5F, -1.5F, -0.5F, 7, 3, 1, 0.0F);
            main.setRotationPoint(-13.0F, 0.0F, (float)(i * 2));
            ModelRenderer tip = new ModelRenderer(this, 0, 4);
            main.addChild(tip);
            this.fixSize(tip);
            tip.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
            tip.setRotationPoint(-4.0F, 0.0F, 0.0F);
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        float commonAngle = module == null ? 0.0F : ((ModuleWoodcutter)module).getCutterAngle();

        for (int i = 0; i < this.anchors.length; ++i)
        {
            float specificAngle;

            if (i % 2 == 0)
            {
                specificAngle = (float)Math.sin((double)commonAngle);
            }
            else
            {
                specificAngle = (float)Math.cos((double)commonAngle);
            }

            this.anchors[i].rotationPointX = specificAngle * 1.25F;
        }
    }
}
