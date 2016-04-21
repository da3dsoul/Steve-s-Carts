package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleBridge;

@SideOnly(Side.CLIENT)
public class ModelBridge extends ModelCartbase
{
    private static ResourceLocation normal = ResourceHelper.getResource("/models/aiModelNormal.png");
    private static ResourceLocation down = ResourceHelper.getResource("/models/aiModelDown.png");
    private static ResourceLocation up = ResourceHelper.getResource("/models/aiModelUp.png");
    private static ResourceLocation normalWarning = ResourceHelper.getResource("/models/aiModelNormalWarning.png");
    private static ResourceLocation downWarning = ResourceHelper.getResource("/models/aiModelDownWarning.png");
    private static ResourceLocation upWarning = ResourceHelper.getResource("/models/aiModelUpWarning.png");
    private ModelRenderer drillAnchor;

    public ResourceLocation getResource(ModuleBase module)
    {
        if (module == null)
        {
            return normal;
        }
        else
        {
            boolean needBridge = ((ModuleBridge)module).needBridge();
            Vec3 next = ((ModuleBridge)module).getNextblock();
            int y = (int)next.yCoord;
            int yDif = module.getCart().getYTarget() - y;
            return needBridge ? (yDif > 0 ? upWarning : (yDif < 0 ? downWarning : normalWarning)) : (yDif > 0 ? up : (yDif < 0 ? down : normal));
        }
    }

    protected int getTextureWidth()
    {
        return 8;
    }

    protected int getTextureHeight()
    {
        return 8;
    }

    public ModelBridge()
    {
        ModelRenderer side1 = new ModelRenderer(this, 0, 0);
        this.AddRenderer(side1);
        side1.addBox(1.0F, 3.0F, 0.5F, 2, 6, 1, 0.0F);
        side1.setRotationPoint(-11.5F, -6.0F, 8.0F);
        side1.rotateAngleY = ((float)Math.PI / 2F);
        ModelRenderer side2 = new ModelRenderer(this, 0, 0);
        this.AddRenderer(side2);
        side2.addBox(1.0F, 3.0F, 0.5F, 2, 6, 1, 0.0F);
        side2.setRotationPoint(-11.5F, -6.0F, -4.0F);
        side2.rotateAngleY = ((float)Math.PI / 2F);
    }
}
