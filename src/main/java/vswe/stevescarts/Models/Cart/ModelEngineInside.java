package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Engines.ModuleCoalBase;

@SideOnly(Side.CLIENT)
public class ModelEngineInside extends ModelEngineBase
{
    private static ResourceLocation[] textures = new ResourceLocation[5];

    public ResourceLocation getResource(ModuleBase module)
    {
        int index = module == null ? 0 : ((ModuleCoalBase)module).getFireIndex();
        return textures[index];
    }

    protected int getTextureWidth()
    {
        return 8;
    }

    protected int getTextureHeight()
    {
        return 4;
    }

    public ModelEngineInside()
    {
        ModelRenderer back = new ModelRenderer(this, 0, 0);
        this.anchor.addChild(back);
        this.fixSize(back);
        back.addBox(-3.5F, -2.0F, 0.0F, 7, 4, 0, 0.0F);
        back.setRotationPoint(0.0F, -0.5F, 0.3F);
    }

    static
    {
        textures[0] = ResourceHelper.getResource("/models/engineModelBack.png");

        for (int i = 1; i < textures.length; ++i)
        {
            textures[i] = ResourceHelper.getResource("/models/engineModelFire" + i + ".png");
        }
    }
}
