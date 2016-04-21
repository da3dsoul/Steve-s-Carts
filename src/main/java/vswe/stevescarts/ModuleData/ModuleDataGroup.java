package vswe.stevescarts.ModuleData;

import java.util.ArrayList;
import java.util.Iterator;
import vswe.stevescarts.Helpers.Localization;

public class ModuleDataGroup
{
    private Localization.MODULE_INFO name;
    private ArrayList<ModuleData> modules;
    private int count;

    public ModuleDataGroup(Localization.MODULE_INFO name)
    {
        this.name = name;
        this.count = 1;
        this.modules = new ArrayList();
    }

    public String getName()
    {
        return this.name.translate(new String[] {String.valueOf(this.getCount())});
    }

    public ArrayList<ModuleData> getModules()
    {
        return this.modules;
    }

    public int getCount()
    {
        return this.count;
    }

    public ModuleDataGroup add(ModuleData module)
    {
        this.modules.add(module);
        return this;
    }

    public ModuleDataGroup setCount(int count)
    {
        this.count = count;
        return this;
    }

    public ModuleDataGroup copy()
    {
        ModuleDataGroup newObj = (new ModuleDataGroup(this.name)).setCount(this.getCount());
        Iterator i$ = this.getModules().iterator();

        while (i$.hasNext())
        {
            ModuleData obj = (ModuleData)i$.next();
            newObj.add(obj);
        }

        return newObj;
    }

    public ModuleDataGroup copy(int count)
    {
        ModuleDataGroup newObj = (new ModuleDataGroup(this.name)).setCount(count);
        Iterator i$ = this.getModules().iterator();

        while (i$.hasNext())
        {
            ModuleData obj = (ModuleData)i$.next();
            newObj.add(obj);
        }

        return newObj;
    }

    public String getCountName()
    {
        switch (this.count)
        {
            case 1:
                return Localization.MODULE_INFO.MODULE_COUNT_1.translate(new String[0]);

            case 2:
                return Localization.MODULE_INFO.MODULE_COUNT_2.translate(new String[0]);

            case 3:
                return Localization.MODULE_INFO.MODULE_COUNT_3.translate(new String[0]);

            default:
                return "???";
        }
    }

    public static ModuleDataGroup getCombinedGroup(Localization.MODULE_INFO name, ModuleDataGroup group1, ModuleDataGroup group2)
    {
        ModuleDataGroup newgroup = group1.copy();
        newgroup.add(group2);
        newgroup.name = name;
        return newgroup;
    }

    public void add(ModuleDataGroup group)
    {
        Iterator i$ = group.getModules().iterator();

        while (i$.hasNext())
        {
            ModuleData obj = (ModuleData)i$.next();
            this.add(obj);
        }
    }
}
