package vswe.stevescarts.Buttons;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonTask extends ButtonAssembly
{
    private int id;

    public ButtonTask(ModuleComputer module, ButtonBase.LOCATION loc, int id)
    {
        super(module, loc);
        this.id = id;
    }

    public String toString()
    {
        ComputerTask task = this.getTask();
        return task == null ? "Something went wrong" : task.toString();
    }

    public boolean isVisible()
    {
        return super.isVisible() && this.getTask() != null;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public int borderID()
    {
        ComputerTask task = this.getTask();

        if (task != null)
        {
            boolean selected = task.getIsActivated();
            boolean running = false;

            if (this.module instanceof ModuleComputer)
            {
                ComputerProg program = ((ModuleComputer)this.module).getActiveProgram();

                if (program != null)
                {
                    running = program.getActiveId() == this.id;
                }
            }

            if (running && selected)
            {
                return 2;
            }

            if (running)
            {
                return 1;
            }

            if (selected)
            {
                return 0;
            }
        }

        return super.borderID();
    }

    public int ColorCode()
    {
        ComputerTask task = this.getTask();
        return task != null ? task.getType() : 0;
    }

    public int texture()
    {
        ComputerTask task = this.getTask();
        return task != null ? task.getImage() : super.texture();
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        ComputerTask task = this.getTask();

        if (!ctrlKey && this.module instanceof ModuleComputer)
        {
            ComputerProg program = ((ModuleComputer)this.module).getCurrentProg();

            if (program != null)
            {
                Iterator i$ = program.getTasks().iterator();

                while (i$.hasNext())
                {
                    ComputerTask t = (ComputerTask)i$.next();

                    if (t != task)
                    {
                        t.setIsActivated(false);
                    }
                }
            }
        }

        task.setIsActivated(!task.getIsActivated());
    }

    private ComputerTask getTask()
    {
        ComputerProg program = ((ModuleComputer)this.module).getCurrentProg();

        if (program != null)
        {
            ArrayList tasks = program.getTasks();
            return this.id >= 0 && this.id < tasks.size() ? (ComputerTask)tasks.get(this.id) : null;
        }
        else
        {
            return null;
        }
    }
}
