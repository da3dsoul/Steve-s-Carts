package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonFlowForStep extends ButtonFlowFor
{
    private boolean decrease;

    public ButtonFlowForStep(ModuleComputer module, ButtonBase.LOCATION loc, boolean decrease)
    {
        super(module, loc);
        this.decrease = decrease;
    }

    public String toString()
    {
        return this.decrease ? "Set step to -1" : "Set step to +1";
    }

    public int texture()
    {
        return this.decrease ? 45 : 44;
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
        while (this.decrease == task.getFlowForDecrease());

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setFlowForDecrease(this.decrease);
        }
    }
}
