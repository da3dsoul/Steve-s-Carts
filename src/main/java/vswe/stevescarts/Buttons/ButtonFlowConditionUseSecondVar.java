package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowConditionUseSecondVar extends ButtonFlowCondition
{
    private boolean use;

    public ButtonFlowConditionUseSecondVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean use)
    {
        super(module, loc);
        this.use = use;
    }

    public String toString()
    {
        return this.use ? "Use second variable" : "Use integer";
    }

    public int texture()
    {
        return this.use ? 38 : 39;
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
        while (this.use == task.getFlowConditionUseSecondVar());

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setFlowConditionUseSecondVar(this.use);
        }
    }
}
