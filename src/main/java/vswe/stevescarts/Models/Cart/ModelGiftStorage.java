package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;

@SideOnly(Side.CLIENT)
public class ModelGiftStorage extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/giftStorageModel.png");
    ModelRenderer lid1 = this.AddChest(false);
    ModelRenderer lid2 = this.AddChest(true);

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureHeight()
    {
        return 64;
    }

    private ModelRenderer AddChest(boolean opposite)
    {
        ModelRenderer chestAnchor = new ModelRenderer(this);
        this.AddRenderer(chestAnchor);
        byte offsetY = 0;

        if (opposite)
        {
            chestAnchor.rotateAngleY = (float)Math.PI;
            offsetY = 21;
        }

        ModelRenderer base = new ModelRenderer(this, 0, 7 + offsetY);
        this.fixSize(base);
        chestAnchor.addChild(base);
        base.addBox(8.0F, 3.0F, 2.0F, 16, 6, 4, 0.0F);
        base.setRotationPoint(-16.0F, -5.5F, -14.0F);
        ModelRenderer lid = new ModelRenderer(this, 0, offsetY);
        this.fixSize(lid);
        chestAnchor.addChild(lid);
        lid.addBox(8.0F, -3.0F, -4.0F, 16, 3, 4, 0.0F);
        lid.setRotationPoint(-16.0F, -1.5F, -8.0F);
        ModelRenderer lock = new ModelRenderer(this, 0, 17 + offsetY);
        this.fixSize(lock);
        lid.addChild(lock);
        lock.addBox(1.0F, 1.5F, 0.5F, 2, 3, 1, 0.0F);
        lock.setRotationPoint(14.0F, -3.0F, -5.5F);
        return lid;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.lid1.rotateAngleX = module == null ? 0.0F : -((ModuleChest)module).getChestAngle();
        this.lid2.rotateAngleX = module == null ? 0.0F : -((ModuleChest)module).getChestAngle();
    }
}
