package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Engines.ModuleSolarTop;

@SideOnly(Side.CLIENT)
public class ModelSolarPanelHeads extends ModelSolarPanel
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/panelModelActive.png");
    private static ResourceLocation texture2 = ResourceHelper.getResource("/models/panelModelIdle.png");
    ArrayList<ModelRenderer> panels = new ArrayList();

    public ResourceLocation getResource(ModuleBase module)
    {
        return module != null && ((ModuleSolarTop)module).getLight() == 15 ? texture : texture2;
    }

    protected int getTextureWidth()
    {
        return 32;
    }

    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelSolarPanelHeads(int panelCount)
    {
        ModelRenderer moving = this.createMovingHolder(0, 0);

        for (int i = 0; i < panelCount; ++i)
        {
            this.createPanel(moving, i);
        }
    }

    private void createPanel(ModelRenderer base, int index)
    {
        float rotation;
        float f;

        switch (index)
        {
            case 0:
                rotation = 0.0F;
                f = -1.5F;
                break;

            case 1:
                rotation = (float)Math.PI;
                f = -1.5F;
                break;

            case 2:
                rotation = ((float)Math.PI * 3F / 2F);
                f = -6.0F;
                break;

            case 3:
                rotation = ((float)Math.PI / 2F);
                f = -6.0F;
                break;

            default:
                return;
        }

        this.createPanel(base, rotation, f);
    }

    private void createPanel(ModelRenderer base, float rotation, float f)
    {
        ModelRenderer panel = new ModelRenderer(this, 0, 0);
        this.fixSize(panel);
        base.addChild(panel);
        panel.addBox(-6.0F, 0.0F, -2.0F, 12, 13, 2, 0.0F);
        panel.setRotationPoint((float)Math.sin((double)rotation) * f, -5.0F, (float)Math.cos((double)rotation) * f);
        panel.rotateAngleY = rotation;
        this.panels.add(panel);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        super.applyEffects(module, yaw, pitch, roll);
        ModelRenderer panel;

        for (Iterator i$ = this.panels.iterator(); i$.hasNext(); panel.rotateAngleX = module == null ? 0.0F : -((ModuleSolarTop)module).getInnerRotation())
        {
            panel = (ModelRenderer)i$.next();
        }
    }
}
