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
public class ModelSideTanks extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/tanksModel.png");

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelSideTanks()
    {
        for (int i = 0; i < 2; ++i)
        {
            ModelRenderer tankback;
            ModelRenderer tube1;

            for (int tankfront = 0; tankfront < 2; ++tankfront)
            {
                tankback = new ModelRenderer(this, 0, 0);
                this.AddRenderer(tankback);
                tankback.addBox(-6.0F, -3.0F, -0.5F, 12, 6, 1, 0.0F);
                tankback.setRotationPoint(-2.0F, -0.5F, -10.5F + (float)(i * 22) + -3.0F + (float)(tankfront * 5));
                tube1 = new ModelRenderer(this, 0, 7);
                this.AddRenderer(tube1);
                tube1.addBox(-6.0F, -2.0F, -0.5F, 12, 4, 1, 0.0F);
                tube1.setRotationPoint(-2.0F, -3.0F + (float)(tankfront * 5), -11.0F + (float)(i * 22));
                tube1.rotateAngleX = ((float)Math.PI / 2F);
            }

            ModelRenderer var7 = new ModelRenderer(this, 26, 0);
            this.AddRenderer(var7);
            var7.addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, 0.0F);
            var7.setRotationPoint(-7.5F, -0.5F, -11.0F + (float)(i * 22));
            var7.rotateAngleY = ((float)Math.PI / 2F);
            tankback = new ModelRenderer(this, 36, 0);
            this.AddRenderer(tankback);
            tankback.addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, 0.0F);
            tankback.setRotationPoint(4.5F, -0.5F, -11.0F + (float)(i * 22));
            tankback.rotateAngleY = ((float)Math.PI / 2F);
            tube1 = new ModelRenderer(this, 26, 5);
            this.AddRenderer(tube1);
            tube1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
            tube1.setRotationPoint(5.5F, -0.5F, -11.0F + (float)(i * 22));
            ModelRenderer tube2 = new ModelRenderer(this, 26, 5);
            this.AddRenderer(tube2);
            tube2.addBox(-2.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
            tube2.setRotationPoint(7.5F, -0.5F, -10.0F + (float)(i * 20));
            tube2.rotateAngleY = ((float)Math.PI / 2F);
            ModelRenderer connection = new ModelRenderer(this, 36, 0);
            this.AddRenderer(connection);
            connection.addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, 0.0F);
            connection.setRotationPoint(7.5F, -0.5F, -8.5F + (float)(i * 17));
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
                ((RendererMinecart)render).renderLiquidCuboid(liquid, ((ModuleTank)module).getCapacity(), -2.0F, -0.5F, -11.0F, 10.0F, 4.0F, 4.0F, mult);
                ((RendererMinecart)render).renderLiquidCuboid(liquid, ((ModuleTank)module).getCapacity(), -2.0F, -0.5F, 11.0F, 10.0F, 4.0F, 4.0F, mult);
            }
        }
    }
}
