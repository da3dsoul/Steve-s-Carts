package vswe.stevescarts.Buttons;

import java.util.Iterator;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowConditionSecondVar extends ButtonFlowConditionVar
{
    public ButtonFlowConditionSecondVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean increase)
    {
        super(module, loc, increase);
    }

    public boolean isVisible()
    {
        if (((ModuleComputer)this.module).getSelectedTasks() != null)
        {
            Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

            while (i$.hasNext())
            {
                ComputerTask task = (ComputerTask)i$.next();

                if (!task.getFlowConditionUseSecondVar())
                {
                    return false;
                }
            }
        }

        return super.isVisible();
    }

    protected int getIndex(ComputerTask task)
    {
        return task.getFlowConditionSecondVarIndex();
    }

    protected void setIndex(ComputerTask task, int val)
    {
        task.setFlowConditionSecondVar(val);
    }
}
