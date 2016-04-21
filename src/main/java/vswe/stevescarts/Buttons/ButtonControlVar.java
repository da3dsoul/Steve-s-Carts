package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonControlVar extends ButtonControl
{
    protected boolean increase;

    public ButtonControlVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean increase)
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
        if (((ModuleComputer)this.module).getSelectedTasks() != null)
        {
            Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

            while (i$.hasNext())
            {
                ComputerTask task = (ComputerTask)i$.next();

                if (!task.getControlUseVar())
                {
                    return false;
                }
            }
        }

        return super.isVisible();
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

            if (this.increase && task.getControlVarIndex() < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
        }
        while (this.increase || task.getControlVarIndex() <= -1);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setControlVar(task.getControlVarIndex() + (this.increase ? 1 : -1));
        }
    }
}
