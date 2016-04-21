package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelHull extends ModelCartbase
{
    private ResourceLocation resource;

    public ResourceLocation getResource(ModuleBase module)
    {
        return this.resource;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelHull(ResourceLocation resource)
    {
        this.resource = resource;
        ModelRenderer bot = new ModelRenderer(this, 0, 0);
        ModelRenderer front = new ModelRenderer(this, 0, 18);
        ModelRenderer left = new ModelRenderer(this, 0, 18);
        ModelRenderer right = new ModelRenderer(this, 0, 18);
        ModelRenderer back = new ModelRenderer(this, 0, 18);
        this.AddRenderer(bot);
        this.AddRenderer(front);
        this.AddRenderer(left);
        this.AddRenderer(right);
        this.AddRenderer(back);
        bot.addBox(-10.0F, -8.0F, -1.0F, 20, 16, 2, 0.0F);
        bot.setRotationPoint(0.0F, 4.0F, 0.0F);
        front.addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        front.setRotationPoint(-9.0F, 4.0F, 0.0F);
        left.addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        left.setRotationPoint(0.0F, 4.0F, -7.0F);
        right.addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        right.setRotationPoint(0.0F, 4.0F, 7.0F);
        back.addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        back.setRotationPoint(9.0F, 4.0F, 0.0F);
        bot.rotateAngleX = ((float)Math.PI / 2F);
        front.rotateAngleY = ((float)Math.PI * 3F / 2F);
        left.rotateAngleY = (float)Math.PI;
        back.rotateAngleY = ((float)Math.PI / 2F);
    }

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        if (module != null)
        {
            float[] color = module.getCart().getColor();
            GL11.glColor4f(color[0], color[1], color[2], 1.0F);
        }

        super.render(render, module, yaw, pitch, roll, mult, partialtime);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
