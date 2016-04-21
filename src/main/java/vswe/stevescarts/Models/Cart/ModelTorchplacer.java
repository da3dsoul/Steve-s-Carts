package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleTorch;

@SideOnly(Side.CLIENT)
public class ModelTorchplacer extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/torchModel.png");
    ModelRenderer[] torches1 = this.createSide(false);
    ModelRenderer[] torches2 = this.createSide(true);

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

    private ModelRenderer[] createSide(boolean opposite)
    {
        ModelRenderer anchor = new ModelRenderer(this, 0, 0);
        this.AddRenderer(anchor);

        if (opposite)
        {
            anchor.rotateAngleY = (float)Math.PI;
        }

        ModelRenderer base = new ModelRenderer(this, 0, 0);
        anchor.addChild(base);
        this.fixSize(base);
        base.addBox(-7.0F, -2.0F, -1.0F, 14, 4, 2, 0.0F);
        base.setRotationPoint(0.0F, -2.0F, -9.0F);
        ModelRenderer[] torches = new ModelRenderer[3];

        for (int i = -1; i <= 1; ++i)
        {
            ModelRenderer torchHolder = new ModelRenderer(this, 0, 6);
            base.addChild(torchHolder);
            this.fixSize(torchHolder);
            torchHolder.addBox(-1.0F, -1.0F, -0.5F, 2, 2, 1, 0.0F);
            torchHolder.setRotationPoint((float)(i * 4), 0.0F, -1.5F);
            ModelRenderer torch = new ModelRenderer(this, 0, 9);
            torches[i + 1] = torch;
            torchHolder.addChild(torch);
            this.fixSize(torch);
            torch.addBox(-1.0F, -5.0F, -1.0F, 2, 10, 2, 0.0F);
            torch.setRotationPoint(0.0F, 0.0F, -1.5F);
        }

        return torches;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        int torches = module == null ? 7 : ((ModuleTorch)module).getTorches();

        for (int i = 0; i < 3; ++i)
        {
            boolean isTorch = (torches & 1 << i) != 0;
            this.torches1[i].isHidden = !isTorch;
            this.torches2[2 - i].isHidden = !isTorch;
        }
    }
}
