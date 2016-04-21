package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Hull.ModulePig;

@SideOnly(Side.CLIENT)
public class ModelPigHelmet extends ModelCartbase
{
    private boolean isOverlay;

    public ResourceLocation getResource(ModuleBase module)
    {
        ModulePig pig = (ModulePig)module;
        return pig.getHelmetResource(this.isOverlay);
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelPigHelmet(boolean isOverlay)
    {
        this.isOverlay = isOverlay;
        ModelRenderer Headwear = new ModelRenderer(this, 0, 0);
        this.AddRenderer(Headwear);
        Headwear.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        Headwear.setRotationPoint(-12.2F + (isOverlay ? 0.2F : 0.0F), -5.4F, 0.0F);
        Headwear.rotateAngleY = ((float)Math.PI / 2F);
    }

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        if (render != null && module != null)
        {
            ModulePig pig = (ModulePig)module;

            if (pig.hasHelment() && (!this.isOverlay || pig.getHelmetMultiRender()))
            {
                float sizemult = 1.09375F + (this.isOverlay ? 0.020833334F : 0.0F);
                GL11.glScalef(sizemult, sizemult, sizemult);

                if (pig.hasHelmetColor(this.isOverlay))
                {
                    int color = pig.getHelmetColor(this.isOverlay);
                    float red = (float)(color >> 16 & 255) / 255.0F;
                    float green = (float)(color >> 8 & 255) / 255.0F;
                    float blue = (float)(color & 255) / 255.0F;
                    GL11.glColor3f(red, green, blue);
                }

                super.render(render, module, yaw, pitch, roll, mult, partialtime);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                GL11.glScalef(1.0F / sizemult, 1.0F / sizemult, 1.0F / sizemult);
            }
        }
    }
}
