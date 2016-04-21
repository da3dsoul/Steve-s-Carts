package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;

@SideOnly(Side.CLIENT)
public class ModelTopChest extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/topChestModel.png");
    ModelRenderer lid = this.AddChest();

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    private ModelRenderer AddChest()
    {
        ModelRenderer chestAnchor = new ModelRenderer(this);
        this.AddRenderer(chestAnchor);
        chestAnchor.rotateAngleY = ((float)Math.PI * 3F / 2F);
        chestAnchor.setRotationPoint(-2.5F, -3.0F, 2.0F);
        ModelRenderer base = new ModelRenderer(this, 0, 19);
        this.fixSize(base);
        chestAnchor.addChild(base);
        base.addBox(6.0F, 2.0F, 8.0F, 12, 4, 16, 0.0F);
        base.setRotationPoint(-14.0F, -5.5F, -18.5F);
        ModelRenderer lid = new ModelRenderer(this, 0, 0);
        this.fixSize(lid);
        chestAnchor.addChild(lid);
        lid.addBox(6.0F, -3.0F, -16.0F, 12, 3, 16, 0.0F);
        lid.setRotationPoint(-14.0F, -2.5F, 5.5F);
        ModelRenderer lock = new ModelRenderer(this, 0, 39);
        this.fixSize(lock);
        lid.addChild(lock);
        lock.addBox(1.0F, 1.5F, 0.5F, 2, 3, 1, 0.0F);
        lock.setRotationPoint(10.0F, -3.0F, -17.5F);
        return lid;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.lid.rotateAngleX = module == null ? 0.0F : -((ModuleChest)module).getChestAngle();
    }
}
