package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Engines.ModuleSolarTop;

@SideOnly(Side.CLIENT)
public abstract class ModelSolarPanel extends ModelCartbase
{
    ModelRenderer moving;

    protected ModelRenderer createMovingHolder(int x, int y)
    {
        ModelRenderer moving = new ModelRenderer(this, x, y);
        this.moving = moving;
        this.AddRenderer(moving);
        return moving;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.moving.rotationPointY = module == null ? -4.0F : ((ModuleSolarTop)module).getMovingLevel();
    }
}
