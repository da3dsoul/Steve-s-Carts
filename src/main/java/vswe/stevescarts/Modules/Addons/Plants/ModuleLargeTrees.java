package vswe.stevescarts.Modules.Addons.Plants;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.Addons.ModuleAddon;
import vswe.stevescarts.Modules.ITreeSizeModule;

public class ModuleLargeTrees extends ModuleAddon implements ITreeSizeModule {
    public ModuleLargeTrees(MinecartModular cart)
    {
        super(cart);
    }
    @Override
    public int getMaxHorizontalDistance() {
        return 9216;
    }
}
