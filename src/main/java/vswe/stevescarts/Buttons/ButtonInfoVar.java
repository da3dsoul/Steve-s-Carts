package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonInfoVar extends ButtonAssembly
{
    protected boolean increase;

    public ButtonInfoVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean increase)
    {
        super(module, loc);
        this.increase = increase;
    }

    public String toString()
    {
        return this.increase ? "Next variable" : "Previous variable";
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
            while (ComputerTask.isInfo(task.getType()) && !task.isInfoEmpty());

            return false;
        }
        else
        {
            return false;
        }
    }

    public int texture()
    {
        return this.increase ? 30 : 31;
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

            if (this.increase && task.getInfoVarIndex() < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
        }
        while (this.increase || task.getInfoVarIndex() <= -1);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setInfoVar(task.getInfoVarIndex() + (this.increase ? 1 : -1));
        }
    }
}
