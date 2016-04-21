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
public class ModelTopTank extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/tankModelTop.png");
    private static ResourceLocation textureOpen = ResourceHelper.getResource("/models/tankModelTopOpen.png");
    private boolean open;

    public ResourceLocation getResource(ModuleBase module)
    {
        return this.open ? textureOpen : texture;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelTopTank(boolean open)
    {
        this.open = open;

        for (int i = 0; i < 2; ++i)
        {
            ModelRenderer tankside = new ModelRenderer(this, 0, 13);
            this.AddRenderer(tankside);
            tankside.addBox(-8.0F, -2.5F, -0.5F, 16, 5, 1, 0.0F);
            tankside.setRotationPoint(0.0F, -8.5F, -5.5F + (float)(i * 11));
            ModelRenderer tankfrontback;

            if (!open || i == 0)
            {
                tankfrontback = new ModelRenderer(this, 0, 0);
                this.AddRenderer(tankfrontback);
                tankfrontback.addBox(-8.0F, -6.0F, -0.5F, 16, 12, 1, 0.0F);
                tankfrontback.setRotationPoint(0.0F, -5.5F - (float)(i * 6), 0.0F);
                tankfrontback.rotateAngleX = ((float)Math.PI / 2F);
            }

            tankfrontback = new ModelRenderer(this, 0, 19);
            this.AddRenderer(tankfrontback);
            tankfrontback.addBox(-5.0F, -2.5F, -0.5F, 10, 5, 1, 0.0F);
            tankfrontback.setRotationPoint(-7.5F + (float)(i * 15), -8.5F, 0.0F);
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
                ((RendererMinecart)render).renderLiquidCuboid(liquid, ((ModuleTank)module).getCapacity(), 0.0F, this.open ? -7.25F : -8.5F, 0.0F, 14.0F, this.open ? 2.5F : 5.0F, 10.0F, mult);
            }
        }
    }
}
