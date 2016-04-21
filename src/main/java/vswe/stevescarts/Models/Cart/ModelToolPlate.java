package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelToolPlate extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/toolPlateModel.png");

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
        return 8;
    }

    public ModelToolPlate()
    {
        ModelRenderer drillBase = new ModelRenderer(this, 0, 0);
        this.AddRenderer(drillBase);
        drillBase.addBox(-5.0F, -7.0F, -2.0F, 10, 6, 1, 0.0F);
        drillBase.setRotationPoint(-9.0F, 4.0F, 0.0F);
        drillBase.rotateAngleY = ((float)Math.PI / 2F);
    }
}
