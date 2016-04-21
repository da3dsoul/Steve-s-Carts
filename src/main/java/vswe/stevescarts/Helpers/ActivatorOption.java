package vswe.stevescarts.Helpers;

import vswe.stevescarts.Modules.ModuleBase;

public class ActivatorOption
{
    private Class <? extends ModuleBase > module;
    private int id;
    private Localization.GUI.TOGGLER name;
    private int option;

    public ActivatorOption(Localization.GUI.TOGGLER name, Class <? extends ModuleBase > module, int id)
    {
        this.name = name;
        this.module = module;
        this.id = id;
    }

    public ActivatorOption(Localization.GUI.TOGGLER name, Class <? extends ModuleBase > module)
    {
        this(name, module, 0);
    }

    public Class <? extends ModuleBase > getModule()
    {
        return this.module;
    }

    public String getName()
    {
        return this.name.translate(new String[0]);
    }

    public int getOption()
    {
        return this.option;
    }

    public int getId()
    {
        return this.id;
    }

    public void setOption(int val)
    {
        this.option = val;
    }

    public void changeOption(boolean dif)
    {
        if (dif)
        {
            if (++this.option > 5)
            {
                this.option = 0;
            }
        }
        else if (--this.option < 0)
        {
            this.option = 5;
        }
    }

    public boolean isDisabled()
    {
        return this.option == 0;
    }

    public boolean shouldActivate(boolean isOrange)
    {
        return this.option == 2 || this.option == 4 && !isOrange || this.option == 5 && isOrange;
    }

    public boolean shouldDeactivate(boolean isOrange)
    {
        return this.option == 1 || this.option == 4 && isOrange || this.option == 5 && !isOrange;
    }

    public boolean shouldToggle()
    {
        return this.option == 3;
    }

    public String getInfo()
    {
        return this.isDisabled() ? Localization.GUI.TOGGLER.SETTING_DISABLED.translate(new String[0]) : "\u00a76" + Localization.GUI.TOGGLER.SETTING_ORANGE.translate(new String[0]) + ": " + (this.shouldActivate(true) ? "\u00a72" + Localization.GUI.TOGGLER.STATE_ACTIVATE.translate(new String[0]) : (this.shouldDeactivate(true) ? "\u00a74" + Localization.GUI.TOGGLER.STATE_DEACTIVATE.translate(new String[0]) : "\u00a7E" + Localization.GUI.TOGGLER.STATE_TOGGLE.translate(new String[0]))) + "\n" + "\u00a71" + Localization.GUI.TOGGLER.SETTING_BLUE.translate(new String[0]) + ": " + (this.shouldActivate(false) ? "\u00a72" + Localization.GUI.TOGGLER.STATE_ACTIVATE.translate(new String[0]) : (this.shouldDeactivate(false) ? "\u00a74" + Localization.GUI.TOGGLER.STATE_DEACTIVATE.translate(new String[0]) : "\u00a7E" + Localization.GUI.TOGGLER.STATE_TOGGLE.translate(new String[0])));
    }
}
