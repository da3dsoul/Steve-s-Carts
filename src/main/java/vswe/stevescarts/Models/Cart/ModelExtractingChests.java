package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Chests.ModuleExtractingChests;

@SideOnly(Side.CLIENT)
public class ModelExtractingChests extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/codeSideChestsModel.png");
    ModelRenderer lid1;
    ModelRenderer lid2;
    ModelRenderer base1;
    ModelRenderer base2;

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    public ModelExtractingChests()
    {
        ModelRenderer[] temp = this.AddChest(false);
        this.base1 = temp[0];
        this.lid1 = temp[1];
        temp = this.AddChest(true);
        this.base2 = temp[0];
        this.lid2 = temp[1];
    }

    private ModelRenderer[] AddChest(boolean opposite)
    {
        ModelRenderer chestAnchor = new ModelRenderer(this);
        this.AddRenderer(chestAnchor);

        if (opposite)
        {
            chestAnchor.rotateAngleY = (float)Math.PI;
        }

        ModelRenderer base = new ModelRenderer(this, 0, 17);
        this.fixSize(base);
        chestAnchor.addChild(base);
        base.addBox(8.0F, 3.0F, 2.0F, 16, 6, 14, 0.0F);
        base.setRotationPoint(-16.0F, -5.5F, -14.0F);
        ModelRenderer lid = new ModelRenderer(this, 0, 0);
        this.fixSize(lid);
        chestAnchor.addChild(lid);
        lid.addBox(8.0F, -3.0F, -14.0F, 16, 3, 14, 0.0F);
        lid.setRotationPoint(-16.0F, -1.5F, 2.0F);
        ModelRenderer lock = new ModelRenderer(this, 0, 37);
        this.fixSize(lock);
        lid.addChild(lock);
        lock.addBox(1.0F, 1.5F, 0.5F, 2, 3, 1, 0.0F);
        lock.setRotationPoint(14.0F, -3.0F, -15.5F);
        return new ModelRenderer[] {base, lid};
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        if (module == null)
        {
            this.lid1.rotateAngleX = 0.0F;
            this.lid2.rotateAngleX = 0.0F;
            this.lid1.rotationPointZ = 2.0F;
            this.lid2.rotationPointZ = 2.0F;
            this.base1.rotationPointZ = -14.0F;
            this.base2.rotationPointZ = -14.0F;
        }
        else
        {
            ModuleExtractingChests chest = (ModuleExtractingChests)module;
            this.lid1.rotateAngleX = -chest.getChestAngle();
            this.lid2.rotateAngleX = -chest.getChestAngle();
            this.lid1.rotationPointZ = chest.getChestOffset() + 16.0F;
            this.lid2.rotationPointZ = chest.getChestOffset() + 16.0F;
            this.base1.rotationPointZ = chest.getChestOffset();
            this.base2.rotationPointZ = chest.getChestOffset();
        }
    }
}
