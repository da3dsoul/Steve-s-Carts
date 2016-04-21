package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowConditionOperator extends ButtonFlowCondition
{
    private int typeId;

    public ButtonFlowConditionOperator(ModuleComputer module, ButtonBase.LOCATION loc, int typeId)
    {
        super(module, loc);
        this.typeId = typeId;
    }

    public String toString()
    {
        return "Change to " + ComputerTask.getFlowOperatorName(this.typeId, true);
    }

    public int texture()
    {
        return 32 + this.typeId;
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
        while (this.typeId == task.getFlowConditionOperator());

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setFlowConditionOperator(this.typeId);
        }
    }
}
