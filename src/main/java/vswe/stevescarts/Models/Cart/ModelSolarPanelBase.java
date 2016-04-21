package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelSolarPanelBase extends ModelSolarPanel
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/panelModelBase.png");

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

    public ModelSolarPanelBase()
    {
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        this.AddRenderer(base);
        base.addBox(-1.0F, -5.0F, -1.0F, 2, 10, 2, 0.0F);
        base.setRotationPoint(0.0F, -4.5F, 0.0F);
        ModelRenderer moving = this.createMovingHolder(8, 0);
        moving.addBox(-2.0F, -3.5F, -2.0F, 4, 7, 4, 0.0F);
        ModelRenderer top = new ModelRenderer(this, 0, 12);
        this.fixSize(top);
        moving.addChild(top);
        top.addBox(-6.0F, -1.5F, -2.0F, 12, 3, 4, 0.0F);
        top.setRotationPoint(0.0F, -5.0F, 0.0F);
    }
}
