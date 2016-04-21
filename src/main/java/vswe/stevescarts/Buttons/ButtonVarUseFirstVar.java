package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarUseFirstVar extends ButtonVarUseVar
{
    public ButtonVarUseFirstVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean use)
    {
        super(module, loc, use);
    }

    protected boolean getUseVar(ComputerTask task)
    {
        return task.getVarUseFirstVar();
    }

    protected void setUseVar(ComputerTask task, boolean val)
    {
        task.setVarUseFirstVar(val);
    }

    protected String getName()
    {
        return "first";
    }
}
