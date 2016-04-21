package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleCakeServer;

@SideOnly(Side.CLIENT)
public class ModelCake extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/cakeModel.png");
    private ModelRenderer[] cakes = new ModelRenderer[6];

    protected int getTextureHeight()
    {
        return 256;
    }

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    public ModelCake()
    {
        for (int i = 0; i < this.cakes.length; ++i)
        {
            this.cakes[i] = this.createCake(6 - i);
        }
    }

    private ModelRenderer createCake(int slices)
    {
        ModelRenderer cake = new ModelRenderer(this, 0, 22 * (6 - slices));
        this.AddRenderer(cake);
        cake.addBox(-7.0F, -4.0F, -7.0F, 2 * slices + (slices == 6 ? 2 : 1), 8, 14, 0.0F);
        cake.setRotationPoint(0.0F, -9.0F, 0.0F);
        return cake;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        int count;

        if (module != null)
        {
            count = ((ModuleCakeServer)module).getRenderSliceCount();
        }
        else
        {
            count = 6;
        }

        for (int i = 0; i < this.cakes.length; ++i)
        {
            this.cakes[i].isHidden = 6 - i != count;
        }
    }
}
