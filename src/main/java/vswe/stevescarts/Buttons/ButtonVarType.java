package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarType extends ButtonAssembly
{
    private int typeId;

    public ButtonVarType(ModuleComputer module, ButtonBase.LOCATION loc, int id)
    {
        super(module, loc);
        this.typeId = id;
    }

    public String toString()
    {
        return "Change to " + ComputerTask.getVarTypeName(this.typeId);
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
            while (ComputerTask.isVar(task.getType()));

            return false;
        }
        else
        {
            return false;
        }
    }

    public int texture()
    {
        return ComputerTask.getVarImage(this.typeId);
    }

    public int ColorCode()
    {
        return 2;
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
        while (task.getVarType() == this.typeId);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setVarType(this.typeId);
        }
    }
}
