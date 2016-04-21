package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelCage extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/cageModel.png");
    private int cageHeight = 26;

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

    public ModelCage(boolean isPlaceholder)
    {
        if (isPlaceholder)
        {
            this.cageHeight = 14;
        }

        float z;

        for (z = -9.0F; z <= 9.0F; z += 6.0F)
        {
            if (Math.abs(z) != 9.0F)
            {
                this.createBar(z, 7.0F);
                this.createBar(z, -7.0F);
            }

            this.createTopBarShort(z);
        }

        for (z = -7.0F; z <= 7.0F; z += 4.6666665F)
        {
            this.createBar(9.0F, z);
            this.createBar(-9.0F, z);
            this.createTopBarLong(z);
        }
    }

    private void createBar(float offsetX, float offsetZ)
    {
        ModelRenderer bar = new ModelRenderer(this, 0, 0);
        this.AddRenderer(bar);
        bar.addBox(-0.5F, (float)(-this.cageHeight) / 2.0F, -0.5F, 1, this.cageHeight, 1, 0.0F);
        bar.setRotationPoint(offsetX, (float)(-this.cageHeight) / 2.0F - 4.0F, offsetZ);
    }

    private void createTopBarLong(float offsetZ)
    {
        ModelRenderer bar = new ModelRenderer(this, 0, 0);
        this.AddRenderer(bar);
        bar.addBox(-0.5F, -9.5F, -0.5F, 1, 19, 1, 0.0F);
        bar.setRotationPoint(0.005F, (float)(-this.cageHeight) - 4.005F, offsetZ + 0.005F);
        bar.rotateAngleZ = ((float)Math.PI / 2F);
    }

    private void createTopBarShort(float offsetX)
    {
        ModelRenderer bar = new ModelRenderer(this, 0, 0);
        this.AddRenderer(bar);
        bar.addBox(-0.5F, -7.5F, -0.5F, 1, 15, 1, 0.0F);
        bar.setRotationPoint(offsetX - 0.005F, (float)(-this.cageHeight - 4) + 0.005F, -0.005F);
        bar.rotateAngleX = ((float)Math.PI / 2F);
    }
}
