package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public abstract class ModelEngineBase extends ModelCartbase
{
    protected ModelRenderer anchor = new ModelRenderer(this, 0, 0);

    public ModelEngineBase()
    {
        this.AddRenderer(this.anchor);
        this.anchor.setRotationPoint(10.5F, 0.5F, -0.0F);
        this.anchor.rotateAngleY = -((float)Math.PI / 2F);
    }
}
