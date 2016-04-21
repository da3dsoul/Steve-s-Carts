package vswe.stevescarts.Buttons;

import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerVar;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonVarAdd extends ButtonAssembly
{
    public ButtonVarAdd(ModuleComputer module, ButtonBase.LOCATION loc)
    {
        super(module, loc);
    }

    public String toString()
    {
        return "Add Variable";
    }

    public boolean isVisible()
    {
        return super.isVisible();
    }

    public int texture()
    {
        return 25;
    }

    public boolean isEnabled()
    {
        return ((ModuleComputer)this.module).getCurrentProg() != null;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        if (((ModuleComputer)this.module).getCurrentProg() != null)
        {
            ComputerVar var = new ComputerVar((ModuleComputer)this.module);
            var.setEditing(true);
            ((ModuleComputer)this.module).getCurrentProg().getVars().add(var);
        }
    }
}
