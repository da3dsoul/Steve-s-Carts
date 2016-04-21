package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Engines.ModuleSolarCompact;

@SideOnly(Side.CLIENT)
public class ModelCompactSolarPanel extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/panelModelSideActive.png");
    private static ResourceLocation texture2 = ResourceHelper.getResource("/models/panelModelSideIdle.png");
    ModelRenderer[][] models = new ModelRenderer[2][];

    public ResourceLocation getResource(ModuleBase module)
    {
        return module != null && ((ModuleSolarCompact)module).getLight() == 15 ? texture : texture2;
    }

    protected int getTextureWidth()
    {
        return 64;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelCompactSolarPanel()
    {
        this.models[0] = this.createSide(false);
        this.models[1] = this.createSide(true);
    }

    private ModelRenderer[] createSide(boolean opposite)
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
        base.addBox(-7.0F, -6.0F, -1.5F, 14, 6, 3, 0.0F);
        base.setRotationPoint(0.0F, 2.0F, -9.0F);
        ModelRenderer panelarminner = new ModelRenderer(this, 34, 0);
        anchor.addChild(panelarminner);
        this.fixSize(panelarminner);
        panelarminner.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F);
        panelarminner.setRotationPoint(0.0F, -1.0F, 0.0F);
        ModelRenderer panelarmouter = new ModelRenderer(this, 34, 0);
        panelarminner.addChild(panelarmouter);
        this.fixSize(panelarmouter);
        panelarmouter.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 4, 0.0F);
        panelarmouter.setRotationPoint(0.001F, 0.001F, 0.001F);
        ModelRenderer panelBase = new ModelRenderer(this, 0, 9);
        panelarmouter.addChild(panelBase);
        this.fixSize(panelBase);
        panelBase.addBox(-5.5F, -2.0F, -1.0F, 11, 4, 2, 0.0F);
        panelBase.setRotationPoint(0.0F, 0.0F, -2.8F);
        ModelRenderer panelTop = this.createPanel(panelBase, 10, 4, -0.497F, 0, 15);
        ModelRenderer panelBot = this.createPanel(panelBase, 10, 4, -0.494F, 22, 15);
        ModelRenderer panelLeft = this.createPanel(panelBase, 6, 4, -0.491F, 0, 20);
        ModelRenderer panelRight = this.createPanel(panelBase, 6, 4, -0.488F, 14, 20);
        ModelRenderer panelTopLeft = this.createPanel(panelLeft, 6, 4, 0.002F, 0, 25);
        ModelRenderer panelBotLeft = this.createPanel(panelLeft, 6, 4, 0.001F, 28, 25);
        ModelRenderer panelTopRight = this.createPanel(panelRight, 6, 4, 0.002F, 14, 25);
        ModelRenderer panelBotRight = this.createPanel(panelRight, 6, 4, 0.001F, 42, 25);
        return new ModelRenderer[] {panelBase, panelTop, panelBot, panelLeft, panelRight, panelTopLeft, panelTopRight, panelBotLeft, panelBotRight, panelarmouter, panelarminner};
    }

    private ModelRenderer createPanel(ModelRenderer parent, int width, int height, float offset, int textureOffsetX, int textureOffsetY)
    {
        ModelRenderer panel = new ModelRenderer(this, textureOffsetX, textureOffsetY);
        parent.addChild(panel);
        this.fixSize(panel);
        panel.addBox((float)(-width / 2), (float)(-height / 2), -0.5F, width, height, 1, 0.0F);
        panel.setRotationPoint(0.0F, 0.0F, offset);
        return panel;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        if (module == null)
        {
            for (int solar = 0; solar < 2; ++solar)
            {
                ModelRenderer[] i = this.models[solar];
                i[9].rotationPointZ = 0.6F;
                i[10].rotationPointZ = -8.1F;
                i[1].rotationPointY = -0.1F;
                i[2].rotationPointY = 0.1F;
                i[3].rotationPointX = -2.01F;
                i[4].rotationPointX = 2.01F;
                i[5].rotationPointY = i[6].rotationPointY = -0.1F;
                i[7].rotationPointY = i[8].rotationPointY = 0.1F;
                i[9].rotateAngleX = 0.0F;
            }
        }
        else
        {
            ModuleSolarCompact var8 = (ModuleSolarCompact)module;

            for (int var9 = 0; var9 < 2; ++var9)
            {
                ModelRenderer[] models = this.models[var9];
                models[9].rotationPointZ = 1.0F - var8.getExtractionDist();
                models[10].rotationPointZ = -7.7F - var8.getInnerExtraction();
                models[1].rotationPointY = -var8.getTopBotExtractionDist();
                models[2].rotationPointY = var8.getTopBotExtractionDist();
                models[3].rotationPointX = -2.0F - var8.getLeftRightExtractionDist();
                models[4].rotationPointX = 2.0F + var8.getLeftRightExtractionDist();
                models[5].rotationPointY = models[6].rotationPointY = -var8.getCornerExtractionDist();
                models[7].rotationPointY = models[8].rotationPointY = var8.getCornerExtractionDist();
                models[9].rotateAngleX = -var8.getPanelAngle();
            }
        }
    }
}
