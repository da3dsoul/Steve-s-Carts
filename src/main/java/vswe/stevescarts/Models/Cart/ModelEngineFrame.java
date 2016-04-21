package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelEngineFrame extends ModelEngineBase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/engineModelFrame.png");

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
        return 8;
    }

    public ModelEngineFrame()
    {
        ModelRenderer left = new ModelRenderer(this, 0, 0);
        this.anchor.addChild(left);
        this.fixSize(left);
        left.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        left.setRotationPoint(-4.0F, 0.0F, 0.0F);
        ModelRenderer right = new ModelRenderer(this, 0, 0);
        this.anchor.addChild(right);
        this.fixSize(right);
        right.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        right.setRotationPoint(4.0F, 0.0F, 0.0F);
        ModelRenderer top = new ModelRenderer(this, 4, 0);
        this.anchor.addChild(top);
        this.fixSize(top);
        top.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1, 0.0F);
        top.setRotationPoint(0.0F, -3.0F, 0.0F);
        top.rotateAngleZ = ((float)Math.PI / 2F);
        ModelRenderer bot = new ModelRenderer(this, 4, 0);
        this.anchor.addChild(bot);
        this.fixSize(bot);
        bot.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1, 0.0F);
        bot.setRotationPoint(0.0F, 2.0F, 0.0F);
        bot.rotateAngleZ = ((float)Math.PI / 2F);
    }
}
