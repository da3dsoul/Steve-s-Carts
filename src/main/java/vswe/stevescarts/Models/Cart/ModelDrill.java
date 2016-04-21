package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

@SideOnly(Side.CLIENT)
public class ModelDrill extends ModelCartbase
{
    private ModelRenderer drillAnchor;
    private ResourceLocation resource;

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

    public ModelDrill(ResourceLocation resource)
    {
        this.resource = resource;
        this.drillAnchor = new ModelRenderer(this);
        this.AddRenderer(this.drillAnchor);
        this.drillAnchor.rotateAngleY = ((float)Math.PI * 3F / 2F);
        int srcY = 0;

        for (int i = 0; i < 6; ++i)
        {
            ModelRenderer drill = this.fixSize(new ModelRenderer(this, 0, srcY));
            this.drillAnchor.addChild(drill);
            drill.addBox(-3.0F + (float)i * 0.5F, -3.0F + (float)i * 0.5F, (float)i, 6 - i, 6 - i, 1, 0.0F);
            drill.setRotationPoint(0.0F, 0.0F, 11.0F);
            srcY += 7 - i;
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        Object drill;

        for (Iterator i$ = this.drillAnchor.childModels.iterator(); i$.hasNext(); ((ModelRenderer)drill).rotateAngleZ = module == null ? 0.0F : ((ModuleDrill)module).getDrillRotation())
        {
            drill = i$.next();
        }
    }
}
