package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public class ModelPumpkinHull extends ModelHull
{
    private ResourceLocation resourceactive;
    private ResourceLocation resourceidle;

    public ResourceLocation getResource(ModuleBase module)
    {
        return module != null && !this.isActive(module) ? this.resourceidle : this.resourceactive;
    }

    public ModelPumpkinHull(ResourceLocation resourceactive, ResourceLocation resourceidle)
    {
        super(resourceactive);
        this.resourceactive = resourceactive;
        this.resourceidle = resourceidle;
    }

    private boolean isActive(ModuleBase module)
    {
        long time = module.getCart().worldObj.getWorldInfo().getWorldTime() % 24000L;
        return time >= 12000L && time <= 18000L;
    }
}
