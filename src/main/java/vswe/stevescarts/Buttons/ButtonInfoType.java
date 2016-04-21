package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonInfoType extends ButtonAssembly
{
    private int typeId;

    public ButtonInfoType(ModuleComputer module, ButtonBase.LOCATION loc, int id)
    {
        super(module, loc);
        this.typeId = id;
    }

    public String toString()
    {
        return "Change to " + ComputerTask.getInfoTypeName(this.typeId);
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
            while (ComputerTask.isInfo(task.getType()));

            return false;
        }
        else
        {
            return false;
        }
    }

    public int texture()
    {
        return ComputerTask.getInfoImage(this.typeId);
    }

    public int ColorCode()
    {
        return 4;
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
        while (task.getInfoType() == this.typeId);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setInfoType(this.typeId);
        }
    }
}
