package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonLabelId extends ButtonAssembly
{
    private boolean increase;

    public ButtonLabelId(ModuleComputer module, ButtonBase.LOCATION loc, boolean increase)
    {
        super(module, loc);
        this.increase = increase;
    }

    public String toString()
    {
        return this.increase ? "Increase ID" : "Decrease ID";
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
            while (task.isFlowLabel() || task.isFlowGoto());

            return false;
        }
        else
        {
            return false;
        }
    }

    public int texture()
    {
        return this.increase ? 23 : 24;
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
        while ((!this.increase || task.getFlowLabelId() >= 31) && (this.increase || task.getFlowLabelId() <= 0));

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setFlowLabelId(task.getFlowLabelId() + (this.increase ? 1 : -1));
        }
    }
}
