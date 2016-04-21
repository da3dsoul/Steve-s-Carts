package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Modules.ILeverModule;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelLever extends ModelCartbase
{
    ModelRenderer lever;
    ResourceLocation resource;

    public ResourceLocation getResource(ModuleBase module)
    {
        return this.resource;
    }

    protected int getTextureWidth()
    {
        return 32;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelLever(ResourceLocation resource)
    {
        this.resource = resource;
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        this.AddRenderer(base);
        base.addBox(-2.5F, -1.5F, -0.5F, 5, 3, 1, 0.0F);
        base.setRotationPoint(0.0F, 2.0F, 8.5F);
        this.lever = new ModelRenderer(this, 0, 4);
        base.addChild(this.lever);
        this.fixSize(this.lever);
        this.lever.addBox(-0.5F, -12.0F, -0.5F, 1, 11, 1, 0.0F);
        this.lever.setRotationPoint(0.0F, 0.0F, 0.0F);
        ModelRenderer handle = new ModelRenderer(this, 4, 4);
        this.lever.addChild(handle);
        this.fixSize(handle);
        handle.addBox(-1.0F, -13.0F, -1.0F, 2, 2, 2, 0.0F);
        handle.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.lever.rotateAngleZ = module == null ? 0.0F : 0.3926991F - ((ILeverModule)module).getLeverState() * (float)Math.PI / 4.0F;
    }
}
