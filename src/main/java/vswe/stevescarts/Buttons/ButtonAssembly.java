package vswe.stevescarts.Buttons;

import vswe.stevescarts.Modules.Workers.ModuleComputer;

public abstract class ButtonAssembly extends ButtonBase
{
    public ButtonAssembly(ModuleComputer module, ButtonBase.LOCATION loc)
    {
        super(module, loc);
    }

    public boolean isVisible()
    {
        return !((ModuleComputer)this.module).isWriting();
    }
}
