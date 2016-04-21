package vswe.stevescarts.Buttons;

import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Computer.ComputerVar;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ButtonKeyboardSpecial extends ButtonKeyboard
{
    private ButtonKeyboardSpecial.KEY key;

    protected ButtonKeyboardSpecial(ModuleComputer module, int x, int y, ButtonKeyboardSpecial.KEY key)
    {
        super(module, x, y, ' ');
        this.key = key;
    }

    public String toString()
    {
        return this.key.toString();
    }

    public boolean isEnabled()
    {
        return this.key != ButtonKeyboardSpecial.KEY.BACKSPACE && this.key != ButtonKeyboardSpecial.KEY.ENTER ? super.isEnabled() : ((ModuleComputer)this.module).getWriting().getText().length() > 0;
    }

    public int texture()
    {
        return this.key == ButtonKeyboardSpecial.KEY.CAPS ? 26 : (this.key == ButtonKeyboardSpecial.KEY.SHIFT ? 27 : (this.key == ButtonKeyboardSpecial.KEY.BACKSPACE ? 28 : (this.key == ButtonKeyboardSpecial.KEY.ENTER ? 29 : super.texture())));
    }

    public int X()
    {
        int temp = this.y;
        this.y = 0;
        int temp2 = super.X();
        this.y = temp;
        return temp2;
    }

    public boolean hasText()
    {
        return false;
    }

    public int borderID()
    {
        return (this.key != ButtonKeyboardSpecial.KEY.SHIFT || !((ModuleComputer)this.module).getShift()) && (this.key != ButtonKeyboardSpecial.KEY.CAPS || !((ModuleComputer)this.module).getCaps()) ? super.borderID() : 3;
    }

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey)
    {
        if (this.key == ButtonKeyboardSpecial.KEY.BACKSPACE)
        {
            ((ModuleComputer)this.module).getWriting().removeChar();
        }
        else if (this.key == ButtonKeyboardSpecial.KEY.ENTER)
        {
            if (((ModuleComputer)this.module).getWriting() instanceof ComputerVar)
            {
                ((ComputerVar)((ModuleComputer)this.module).getWriting()).setEditing(false);
            }
        }
        else if (this.key == ButtonKeyboardSpecial.KEY.SHIFT)
        {
            ((ModuleComputer)this.module).flipShift();
        }
        else if (this.key == ButtonKeyboardSpecial.KEY.CAPS)
        {
            ((ModuleComputer)this.module).flipCaps();
        }
    }

    public static enum KEY
    {
        SHIFT("SHIFT", 0),
        CAPS("CAPS", 1),
        BACKSPACE("BACKSPACE", 2),
        ENTER("ENTER", 3);

        private static final ButtonKeyboardSpecial.KEY[] $VALUES = new ButtonKeyboardSpecial.KEY[]{SHIFT, CAPS, BACKSPACE, ENTER};

        private KEY(String var1, int var2) {}
    }
}
