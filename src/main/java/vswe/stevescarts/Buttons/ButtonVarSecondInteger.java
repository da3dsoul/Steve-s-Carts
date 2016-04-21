package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarSecondInteger extends ButtonVarInteger
{
    public ButtonVarSecondInteger(ModuleComputer module, ButtonBase.LOCATION loc, int dif)
    {
        super(module, loc, dif);
    }

    protected String getName()
    {
        return "second";
    }

    protected boolean isVarVisible(ComputerTask task)
    {
        return task.getVarUseSecondVar();
    }

    protected int getInteger(ComputerTask task)
    {
        return task.getVarSecondInteger();
    }

    protected void setInteger(ComputerTask task, int val)
    {
        task.setVarSecondInteger(val);
    }

    protected boolean isSecondValue()
    {
        return true;
    }
}
