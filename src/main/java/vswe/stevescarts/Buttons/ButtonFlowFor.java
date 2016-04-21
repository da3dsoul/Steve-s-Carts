package vswe.stevescarts.Buttons;

import java.util.Iterator;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public abstract class ButtonFlowFor extends ButtonAssembly
{
    public ButtonFlowFor(ModuleComputer module, ButtonBase.LOCATION loc)
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
            while (task.isFlowFor());

            return false;
        }
        else
        {
            return false;
        }
    }
}
