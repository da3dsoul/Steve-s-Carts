package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public abstract class ModelCartbase extends ModelBase
{
    protected final byte cartLength = 20;
    protected final byte cartHeight = 8;
    protected final byte cartWidth = 16;
    protected final byte cartOnGround = 4;
    private ArrayList<ModelRenderer> renderers = new ArrayList();

    @SideOnly(Side.CLIENT)
    public abstract ResourceLocation getResource(ModuleBase var1);

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        ResourceLocation resource = this.getResource(module);

        if (resource != null)
        {
            ResourceHelper.bindResource(resource);
            this.applyEffects(module, yaw, pitch, roll, partialtime);
            this.do_render(mult);
        }
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll, float partialtime)
    {
        this.applyEffects(module, yaw, pitch, roll);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll) {}

    protected void AddRenderer(ModelRenderer renderer)
    {
        this.renderers.add(this.fixSize(renderer));
    }

    public ModelRenderer fixSize(ModelRenderer renderer)
    {
        return renderer.setTextureSize(this.getTextureWidth(), this.getTextureHeight());
    }

    protected int getTextureWidth()
    {
        return 64;
    }

    protected int getTextureHeight()
    {
        return 64;
    }

    public float extraMult()
    {
        return 1.0F;
    }

    protected void do_render(float mult)
    {
        Iterator i$ = this.renderers.iterator();

        while (i$.hasNext())
        {
            ModelRenderer renderer = (ModelRenderer)i$.next();
            renderer.render(mult * this.extraMult());
        }
    }
}
