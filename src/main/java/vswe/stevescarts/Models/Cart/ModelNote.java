package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelNote extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/noteModel.png");

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelNote()
    {
        this.AddSpeaker(false);
        this.AddSpeaker(true);
    }

    private void AddSpeaker(boolean opposite)
    {
        ModelRenderer noteAnchor = new ModelRenderer(this);
        this.AddRenderer(noteAnchor);
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        this.fixSize(base);
        noteAnchor.addChild(base);
        base.addBox(8.0F, 6.0F, 6.0F, 16, 12, 12, 0.0F);
        base.setRotationPoint(-16.0F, -13.5F, -12.0F + 14.0F * (float)(opposite ? 1 : -1));
    }
}
