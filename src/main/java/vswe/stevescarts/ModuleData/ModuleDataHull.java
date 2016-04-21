package vswe.stevescarts.ModuleData;

import java.util.List;
import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleDataHull extends ModuleData
{
    private int modularCapacity;
    private int engineMaxCount;
    private int addonMaxCount;
    private int complexityMax;

    public ModuleDataHull(int id, String name, Class <? extends ModuleBase > moduleClass)
    {
        super(id, name, moduleClass, 0);
    }

    public ModuleDataHull setCapacity(int val)
    {
        this.modularCapacity = val;
        return this;
    }

    public ModuleDataHull setEngineMax(int val)
    {
        this.engineMaxCount = val;
        return this;
    }

    public ModuleDataHull setAddonMax(int val)
    {
        this.addonMaxCount = val;
        return this;
    }

    public ModuleDataHull setComplexityMax(int val)
    {
        this.complexityMax = val;
        return this;
    }

    public int getEngineMax()
    {
        return this.engineMaxCount;
    }

    public int getAddonMax()
    {
        return this.addonMaxCount;
    }

    public int getCapacity()
    {
        return this.modularCapacity;
    }

    public int getComplexityMax()
    {
        return this.complexityMax;
    }

    public void addSpecificInformation(List list)
    {
        list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.MODULAR_CAPACITY.translate(new String[] {String.valueOf(this.modularCapacity)}));
        list.add(ColorHelper.PURPLE + Localization.MODULE_INFO.COMPLEXITY_CAP.translate(new String[] {String.valueOf(this.complexityMax)}));
        list.add(ColorHelper.ORANGE + Localization.MODULE_INFO.MAX_ENGINES.translate(new String[] {String.valueOf(this.engineMaxCount)}));
        list.add(ColorHelper.GREEN + Localization.MODULE_INFO.MAX_ADDONS.translate(new String[] {String.valueOf(this.addonMaxCount)}));
    }
}
