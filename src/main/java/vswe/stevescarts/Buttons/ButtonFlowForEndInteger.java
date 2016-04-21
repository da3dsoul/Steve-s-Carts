package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowForEndInteger extends ButtonFlowForInteger
{
    public ButtonFlowForEndInteger(ModuleComputer module, ButtonBase.LOCATION loc, int dif)
    {
        super(module, loc, dif);
    }

    protected String getName()
    {
        return "end";
    }

    protected boolean isVarVisible(ComputerTask task)
    {
        return task.getFlowForUseEndVar();
    }

    protected int getInteger(ComputerTask task)
    {
        return task.getFlowForEndInteger();
    }

    protected void setInteger(ComputerTask task, int val)
    {
        task.setFlowForEndInteger(val);
    }
}
