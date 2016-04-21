package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarSecondVar extends ButtonVarVar
{
    public ButtonVarSecondVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean increase)
    {
        super(module, loc, increase);
    }

    protected int getIndex(ComputerTask task)
    {
        return task.getVarSecondVarIndex();
    }

    protected void setIndex(ComputerTask task, int val)
    {
        task.setVarSecondVar(val);
    }

    protected String getName()
    {
        return "second";
    }

    protected boolean isVarVisible(ComputerTask task)
    {
        return task.getVarUseSecondVar();
    }

    protected boolean isSecondValue()
    {
        return true;
    }
}
