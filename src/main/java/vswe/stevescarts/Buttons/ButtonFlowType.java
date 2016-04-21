package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowType extends ButtonAssembly
{
    private int typeId;

    public ButtonFlowType(ModuleComputer module, ButtonBase.LOCATION loc, int id)
    {
        super(module, loc);
        this.typeId = id;
    }

    public String toString()
    {
        return "Change to " + ComputerTask.getFlowTypeName(this.typeId);
    }

    public boolean isVisible()
    {
        if (!super.isVisible())
        {
            return false;
        }
        else if (this.module instanceof ModuleComputer && ((ModuleComputer)this.module).getSelectedTasks() != null && ((ModuleComputer)this.module).getSelectedTasks().size() > 0)
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
            while (ComputerTask.isFlow(task.getType()));

            return false;
        }
        else
        {
            return false;
        }
    }

    public int texture()
    {
        return ComputerTask.getFlowImage(this.typeId);
    }

    public int ColorCode()
    {
        return 1;
    }

    public boolean isEnabled()
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();
        ComputerTask task;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            task = (ComputerTask)i$.next();
        }
        while (task.getFlowType() == this.typeId);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setFlowType(this.typeId);
        }
    }
}
