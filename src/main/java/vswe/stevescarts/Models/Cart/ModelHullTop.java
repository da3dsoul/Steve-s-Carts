package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelHullTop extends ModelCartbase
{
    private ResourceLocation resource;
    private boolean useColors;

    public ResourceLocation getResource(ModuleBase module)
    {
        return this.resource;
    }

    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelHullTop(ResourceLocation resource)
    {
        this(resource, true);
    }

    public ModelHullTop(ResourceLocation resource, boolean useColors)
    {
        this.resource = resource;
        this.useColors = useColors;
        ModelRenderer top = new ModelRenderer(this, 0, 0);
        this.AddRenderer(top);
        top.addBox(-8.0F, -6.0F, -1.0F, 16, 12, 2, 0.0F);
        top.setRotationPoint(0.0F, -4.0F, 0.0F);
        top.rotateAngleX = -((float)Math.PI / 2F);
    }

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        if (this.useColors && module != null)
        {
            float[] color = module.getCart().getColor();
            GL11.glColor4f(color[0], color[1], color[2], 1.0F);
        }

        super.render(render, module, yaw, pitch, roll, mult, partialtime);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
