package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowForStartInteger extends ButtonFlowForInteger
{
    public ButtonFlowForStartInteger(ModuleComputer module, ButtonBase.LOCATION loc, int dif)
    {
        super(module, loc, dif);
    }

    protected String getName()
    {
        return "start";
    }

    protected boolean isVarVisible(ComputerTask task)
    {
        return task.getFlowForUseStartVar();
    }

    protected int getInteger(ComputerTask task)
    {
        return task.getFlowForStartInteger();
    }

    protected void setInteger(ComputerTask task, int val)
    {
        task.setFlowForStartInteger(val);
    }
}
