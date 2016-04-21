package vswe.stevescarts.Buttons;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public abstract class ButtonVarInteger extends ButtonVar
{
    private int dif;

    public ButtonVarInteger(ModuleComputer module, ButtonBase.LOCATION loc, int dif)
    {
        super(module, loc);
        this.dif = dif;
    }

    public String toString()
    {
        return this.dif < 0 ? "Decrease " + this.getName() + " by " + -1 * this.dif : "Increase " + this.getName() + " by " + this.dif;
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

                if (this.isVarVisible(task))
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
        while (-128 > this.getInteger(task) + this.dif || this.getInteger(task) + this.dif > 127);

        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        Iterator i$ = ((ModuleComputer)this.module).getSelectedTasks().iterator();

        while (i$.hasNext())
        {
            ComputerTask task = (ComputerTask)i$.next();
            this.setInteger(task, this.getInteger(task) + this.dif);
        }
    }

    protected abstract String getName();

    protected abstract boolean isVarVisible(ComputerTask var1);

    protected abstract int getInteger(ComputerTask var1);

    protected abstract void setInteger(ComputerTask var1, int var2);
}
