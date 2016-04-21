package vswe.stevescarts.Buttons;

import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonProgramAdd extends ButtonAssembly
{
    public ButtonProgramAdd(ModuleComputer module, ButtonBase.LOCATION loc)
    {
        super(module, loc);
    }

    public String toString()
    {
        return "Add new program";
    }

    public boolean isVisible()
    {
        return super.isVisible();
    }

    public boolean isEnabled()
    {
        return true;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        ((ModuleComputer)this.module).setCurrentProg(new ComputerProg((ModuleComputer)this.module));
    }
}
