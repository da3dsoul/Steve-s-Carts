package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Renders.RendererMinecart;

@SideOnly(Side.CLIENT)
public class ModelFrontTank extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/tankModelFront.png");

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

    public ModelFrontTank()
    {
        for (int i = 0; i < 2; ++i)
        {
            ModelRenderer tankside = new ModelRenderer(this, 0, 15);
            this.AddRenderer(tankside);
            tankside.addBox(-4.0F, -3.0F, -0.5F, 8, 6, 1, 0.0F);
            tankside.setRotationPoint(-14.0F, 0.0F, -6.5F + (float)(i * 13));
            ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
            this.AddRenderer(tanktopbot);
            tanktopbot.addBox(-4.0F, -7.0F, -0.5F, 8, 14, 1, 0.0F);
            tanktopbot.setRotationPoint(-14.0F, 3.5F - (float)(i * 7), 0.0F);
            tanktopbot.rotateAngleX = ((float)Math.PI / 2F);
            ModelRenderer tankfrontback = new ModelRenderer(this, 0, 22);
            this.AddRenderer(tankfrontback);
            tankfrontback.addBox(-6.0F, -3.0F, -0.5F, 12, 6, 1, 0.0F);
            tankfrontback.setRotationPoint(-17.5F + (float)(i * 7), 0.0F, 0.0F);
            tankfrontback.rotateAngleY = ((float)Math.PI / 2F);
        }
    }

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        super.render(render, module, yaw, pitch, roll, mult, partialtime);

        if (render != null && module != null)
        {
            FluidStack liquid = ((ModuleTank)module).getFluid();

            if (liquid != null)
            {
                ((RendererMinecart)render).renderLiquidCuboid(liquid, ((ModuleTank)module).getCapacity(), -14.0F, 0.0F, 0.0F, 6.0F, 6.0F, 12.0F, mult);
            }
        }
    }
}
