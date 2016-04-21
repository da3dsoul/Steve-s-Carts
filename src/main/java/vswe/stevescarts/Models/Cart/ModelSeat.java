package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleSeat;

@SideOnly(Side.CLIENT)
public class ModelSeat extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/chairModel.png");
    ModelRenderer sit = new ModelRenderer(this, 0, 0);

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

    public ModelSeat()
    {
        this.AddRenderer(this.sit);
        this.sit.addBox(-4.0F, -2.0F, -2.0F, 8, 4, 4, 0.0F);
        this.sit.setRotationPoint(0.0F, 1.0F, 0.0F);
        ModelRenderer back = new ModelRenderer(this, 0, 8);
        this.sit.addChild(back);
        this.fixSize(back);
        back.addBox(-4.0F, -2.0F, -1.0F, 8, 12, 2, 0.0F);
        back.setRotationPoint(0.0F, -8.0F, 3.0F);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.sit.rotateAngleY = module == null ? ((float)Math.PI / 2F) : ((ModuleSeat)module).getChairAngle() + (((ModuleSeat)module).useRelativeRender() ? 0.0F : yaw);
    }
}
