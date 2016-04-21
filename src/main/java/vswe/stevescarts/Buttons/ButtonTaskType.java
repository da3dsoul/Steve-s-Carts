package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonTaskType extends ButtonAssembly
{
    private int typeId;

    public ButtonTaskType(ModuleComputer module, ButtonBase.LOCATION loc, int id)
    {
        super(module, loc);
        this.typeId = id;
    }

    public String toString()
    {
        return this.haveTasks() ? "Change to " + ComputerTask.getTypeName(this.typeId) : "Add " + ComputerTask.getTypeName(this.typeId) + " task";
    }

    public boolean isVisible()
    {
        return super.isVisible();
    }

    public int texture()
    {
        return this.typeId < 4 ? this.typeId * 2 + (this.haveTasks() ? 1 : 0) : (this.typeId == 4 ? 66 + (this.haveTasks() ? 1 : 0) : this.typeId * 2 + (this.haveTasks() ? 1 : 0) - 2);
    }

    public boolean isEnabled()
    {
        if (this.module instanceof ModuleComputer && ((ModuleComputer)this.module).getCurrentProg() != null)
        {
            if (this.haveTasks())
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
                while (task.getType() == this.typeId);

                return true;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean haveTasks()
    {
        return ((ModuleComputer)this.module).getSelectedTasks().size() > 0;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        ComputerTask task;

        if (this.haveTasks())
        {
            Iterator program = ((ModuleComputer)this.module).getSelectedTasks().iterator();

            while (program.hasNext())
            {
                task = (ComputerTask)program.next();
                task.setType(this.typeId);
            }
        }
        else
        {
            ComputerProg program1 = ((ModuleComputer)this.module).getCurrentProg();

            if (program1 != null)
            {
                task = new ComputerTask((ModuleComputer)this.module, program1);
                task.setType(this.typeId);
                program1.getTasks().add(task);
            }
        }
    }
}
