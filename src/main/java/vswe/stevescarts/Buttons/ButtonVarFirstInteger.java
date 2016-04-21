package vswe.stevescarts.Buttons;

import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarFirstInteger extends ButtonVarInteger
{
    public ButtonVarFirstInteger(ModuleComputer module, ButtonBase.LOCATION loc, int dif)
    {
        super(module, loc, dif);
    }

    protected String getName()
    {
        return "first";
    }

    protected boolean isVarVisible(ComputerTask task)
    {
        return task.getVarUseFirstVar();
    }

    protected int getInteger(ComputerTask task)
    {
        return task.getVarFirstInteger();
    }

    protected void setInteger(ComputerTask task, int val)
    {
        task.setVarFirstInteger(val);
    }
}
