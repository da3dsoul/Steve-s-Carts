package vswe.stevescarts.Buttons;

import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonProgramStart extends ButtonAssembly
{
    public ButtonProgramStart(ModuleComputer module, ButtonBase.LOCATION loc)
    {
        super(module, loc);
    }

    public String toString()
    {
        return "Start Program";
    }

    public boolean isVisible()
    {
        return super.isVisible();
    }

    public boolean isEnabled()
    {
        return ((ModuleComputer)this.module).getCurrentProg() != null;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        ComputerProg program = ((ModuleComputer)this.module).getCurrentProg();

        if (program != null)
        {
            program.start();
        }
    }
}
