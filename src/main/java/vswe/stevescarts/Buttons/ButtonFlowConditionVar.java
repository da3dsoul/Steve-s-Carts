package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowConditionVar extends ButtonFlowCondition
{
    protected boolean increase;

    public ButtonFlowConditionVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean increase)
    {
        super(module, loc);
        this.increase = increase;
    }

    public String toString()
    {
        return this.increase ? "Next variable" : "Previous variable";
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

            if (this.increase && this.getIndex(task) < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
        }
        while (this.increase || this.getIndex(task) <= -1);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            this.setIndex(task, this.getIndex(task) + (this.increase ? 1 : -1));
        }
    }

    protected int getIndex(ComputerTask task)
    {
        return task.getFlowConditionVarIndex();
    }

    protected void setIndex(ComputerTask task, int val)
    {
        task.setFlowConditionVar(val);
    }
}
