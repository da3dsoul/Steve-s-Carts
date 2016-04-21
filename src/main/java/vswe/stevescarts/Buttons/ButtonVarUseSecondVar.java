package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarUseSecondVar extends ButtonVarUseVar
{
    public ButtonVarUseSecondVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean use)
    {
        super(module, loc, use);
    }

    protected boolean getUseVar(ComputerTask task)
    {
        return task.getVarUseSecondVar();
    }

    protected void setUseVar(ComputerTask task, boolean val)
    {
        task.setVarUseSecondVar(val);
    }

    protected String getName()
    {
        return "second";
    }

    protected boolean isSecondValue()
    {
        return true;
    }
}
