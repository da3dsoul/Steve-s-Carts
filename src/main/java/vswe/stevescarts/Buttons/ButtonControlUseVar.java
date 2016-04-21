package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonControlUseVar extends ButtonControl
{
    private boolean use;

    public ButtonControlUseVar(ModuleComputer module, ButtonBase.LOCATION loc, boolean use)
    {
        super(module, loc);
        this.use = use;
    }

    public String toString()
    {
        return this.use ? "Use variable" : "Use integer";
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
        while (this.use == task.getControlUseVar());

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setControlUseVar(this.use);
        }
    }
}
