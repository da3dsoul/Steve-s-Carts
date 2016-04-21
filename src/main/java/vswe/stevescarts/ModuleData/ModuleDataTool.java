package vswe.stevescarts.ModuleData;

import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleDataTool extends ModuleData
{
    private boolean unbreakable;

    public ModuleDataTool(int id, String name, Class <? extends ModuleBase > moduleClass, int modularCost, boolean unbreakable)
    {
        super(id, name, moduleClass, modularCost);
        this.useExtraData((byte)100);
        this.unbreakable = unbreakable;
    }

    public String getModuleInfoText(byte b)
    {
        return this.unbreakable ? Localization.MODULE_INFO.TOOL_UNBREAKABLE.translate(new String[0]) : Localization.MODULE_INFO.TOOL_DURABILITY.translate(new String[] {String.valueOf(b)});
    }

    public String getCartInfoText(String name, byte b)
    {
        return this.unbreakable ? super.getCartInfoText(name, b) : name + " [" + b + "%]";
    }
}
