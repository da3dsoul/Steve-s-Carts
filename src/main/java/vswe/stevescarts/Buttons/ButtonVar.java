package vswe.stevescarts.Buttons;

import java.util.Iterator;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public abstract class ButtonVar extends ButtonAssembly
{
    public ButtonVar(ModuleComputer module, ButtonBase.LOCATION loc)
    {
        super(module, loc);
    }

    public boolean isVisible()
    {
        if (!super.isVisible())
        {
            return false;
        }
        else if (((ModuleComputer)this.module).getSelectedTasks() != null && ((ModuleComputer)this.module).getSelectedTasks().size() > 0)
        {
            Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();
            ComputerTask task;

            do
            {
                if (!i$.hasNext())
                {
                    return true;
                }

                task = (ComputerTask)i$.next();
            }
            while (ComputerTask.isVar(task.getType()) && !task.isVarEmpty() && (!this.isSecondValue() || task.hasTwoValues()));

            return false;
        }
        else
        {
            return false;
        }
    }

    protected boolean isSecondValue()
    {
        return false;
    }
}
