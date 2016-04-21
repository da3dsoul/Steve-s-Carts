package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleAdvControl;

@SideOnly(Side.CLIENT)
public class ModelWheel extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/wheelModel.png");
    private ModelRenderer anchor = new ModelRenderer(this);

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

    public float extraMult()
    {
        return 0.65F;
    }

    public ModelWheel()
    {
        this.AddRenderer(this.anchor);
        this.anchor.setRotationPoint(-10.0F, -5.0F, 0.0F);
        ModelRenderer top = new ModelRenderer(this, 0, 0);
        this.anchor.addChild(top);
        this.fixSize(top);
        top.addBox(-4.5F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
        top.setRotationPoint(0.0F, -6.0F, 0.0F);
        top.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer topleft = new ModelRenderer(this, 0, 4);
        this.anchor.addChild(topleft);
        this.fixSize(topleft);
        topleft.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        topleft.setRotationPoint(0.0F, -4.0F, -5.5F);
        topleft.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer topright = new ModelRenderer(this, 0, 4);
        this.anchor.addChild(topright);
        this.fixSize(topright);
        topright.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        topright.setRotationPoint(0.0F, -4.0F, 5.5F);
        topright.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer left = new ModelRenderer(this, 0, 12);
        this.anchor.addChild(left);
        this.fixSize(left);
        left.addBox(-1.0F, -2.5F, -1.0F, 2, 5, 2, 0.0F);
        left.setRotationPoint(0.0F, -0.5F, -7.5F);
        left.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer right = new ModelRenderer(this, 0, 12);
        this.anchor.addChild(right);
        this.fixSize(right);
        right.addBox(-1.0F, -2.5F, -1.0F, 2, 5, 2, 0.0F);
        right.setRotationPoint(0.0F, -0.5F, 7.5F);
        right.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer bottomleft = new ModelRenderer(this, 0, 4);
        this.anchor.addChild(bottomleft);
        this.fixSize(bottomleft);
        bottomleft.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        bottomleft.setRotationPoint(0.0F, 3.0F, -5.5F);
        bottomleft.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer bottomright = new ModelRenderer(this, 0, 4);
        this.anchor.addChild(bottomright);
        this.fixSize(bottomright);
        bottomright.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        bottomright.setRotationPoint(0.0F, 3.0F, 5.5F);
        bottomright.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer bottominnerleft = new ModelRenderer(this, 0, 4);
        this.anchor.addChild(bottominnerleft);
        this.fixSize(bottominnerleft);
        bottominnerleft.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        bottominnerleft.setRotationPoint(0.0F, 5.0F, -3.5F);
        bottominnerleft.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer bottominnerright = new ModelRenderer(this, 0, 4);
        this.anchor.addChild(bottominnerright);
        this.fixSize(bottominnerright);
        bottominnerright.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        bottominnerright.setRotationPoint(0.0F, 5.0F, 3.5F);
        bottominnerright.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer bottom = new ModelRenderer(this, 0, 8);
        this.anchor.addChild(bottom);
        this.fixSize(bottom);
        bottom.addBox(-2.5F, -1.0F, -1.0F, 5, 2, 2, 0.0F);
        bottom.setRotationPoint(0.0F, 7.0F, 0.0F);
        bottom.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer middlebottom = new ModelRenderer(this, 0, 19);
        this.anchor.addChild(middlebottom);
        this.fixSize(middlebottom);
        middlebottom.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        middlebottom.setRotationPoint(0.5F, 3.5F, 0.0F);
        middlebottom.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer middle = new ModelRenderer(this, 0, 25);
        this.anchor.addChild(middle);
        this.fixSize(middle);
        middle.addBox(-1.5F, -1.0F, -0.5F, 3, 2, 1, 0.0F);
        middle.setRotationPoint(0.5F, 0.0F, 0.0F);
        middle.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer middleleft = new ModelRenderer(this, 0, 25);
        this.anchor.addChild(middleleft);
        this.fixSize(middleleft);
        middleleft.addBox(-1.5F, -1.0F, -0.5F, 3, 2, 1, 0.0F);
        middleleft.setRotationPoint(0.5F, -1.0F, -3.0F);
        middleleft.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer middleright = new ModelRenderer(this, 0, 25);
        this.anchor.addChild(middleright);
        this.fixSize(middleright);
        middleright.addBox(-1.5F, -1.0F, -0.5F, 3, 2, 1, 0.0F);
        middleright.setRotationPoint(0.5F, -1.0F, 3.0F);
        middleright.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer innerleft = new ModelRenderer(this, 0, 28);
        this.anchor.addChild(innerleft);
        this.fixSize(innerleft);
        innerleft.addBox(-1.5F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        innerleft.setRotationPoint(0.5F, -1.5F, -5.0F);
        innerleft.rotateAngleY = -((float)Math.PI / 2F);
        ModelRenderer innerright = new ModelRenderer(this, 0, 28);
        this.anchor.addChild(innerright);
        this.fixSize(innerright);
        innerright.addBox(-1.5F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        innerright.setRotationPoint(0.5F, -1.5F, 6.0F);
        innerright.rotateAngleY = -((float)Math.PI / 2F);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.anchor.rotateAngleX = module == null ? 0.0F : ((ModuleAdvControl)module).getWheelAngle();
    }
}
