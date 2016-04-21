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
public class ModelAdvancedTank extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/tankModelLarge.png");

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    public ModelAdvancedTank()
    {
        for (int i = 0; i < 2; ++i)
        {
            ModelRenderer tankside = new ModelRenderer(this, 0, 13);
            this.AddRenderer(tankside);
            tankside.addBox(-8.0F, -6.5F, -0.5F, 16, 13, 1, 0.0F);
            tankside.setRotationPoint(0.0F, -4.5F, -5.5F + (float)(i * 11));
            ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
            this.AddRenderer(tanktopbot);
            tanktopbot.addBox(-8.0F, -6.0F, -0.5F, 16, 12, 1, 0.0F);
            tanktopbot.setRotationPoint(0.0F, 2.5F - (float)(i * 14), 0.0F);
            tanktopbot.rotateAngleX = ((float)Math.PI / 2F);
            ModelRenderer tankfrontback = new ModelRenderer(this, 0, 27);
            this.AddRenderer(tankfrontback);
            tankfrontback.addBox(-5.0F, -6.5F, -0.5F, 10, 13, 1, 0.0F);
            tankfrontback.setRotationPoint(-7.5F + (float)(i * 15), -4.5F, 0.0F);
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
                ((RendererMinecart)render).renderLiquidCuboid(liquid, ((ModuleTank)module).getCapacity(), 0.0F, -4.5F, 0.0F, 14.0F, 13.0F, 10.0F, mult);
            }
        }
    }
}
