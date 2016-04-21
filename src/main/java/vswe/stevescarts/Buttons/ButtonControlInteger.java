package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonControlInteger extends ButtonControl
{
    private int dif;

    public ButtonControlInteger(ModuleComputer module, ButtonBase.LOCATION loc, int dif)
    {
        super(module, loc);
        this.dif = dif;
    }

    public String toString()
    {
        return this.dif < 0 ? "Decrease by " + -1 * this.dif : "Increase by " + this.dif;
    }

    public int texture()
    {
        return this.dif == 1 ? 40 : (this.dif == -1 ? 41 : (this.dif == 10 ? 42 : (this.dif == -10 ? 43 : super.texture())));
    }

    public boolean isVisible()
    {
        if (((ModuleComputer)this.module).getSelectedTasks() != null)
        {
            Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

            while (i$.hasNext())
            {
                ComputerTask task = (ComputerTask)i$.next();

                if (task.getControlUseVar() || !task.getControlUseBigInteger(Math.abs(this.dif)))
                {
                    return false;
                }
            }
        }

        return super.isVisible();
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
        while (task.getControlMinInteger() > task.getControlInteger() + this.dif || task.getControlInteger() + this.dif > task.getControlMaxInteger());

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            task.setControlInteger(task.getControlInteger() + this.dif);
        }
    }
}
