package vswe.stevescarts.Computer;

import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ComputerVar implements IWriting
{
    private ModuleComputer module;
    private short info;
    private short val;
    private String name = "??????";
    private static final String validChars = "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public ComputerVar(ModuleComputer module)
    {
        this.module = module;
    }

    public String getText()
    {
        return this.name.replace("?", "");
    }

    public int getMaxLength()
    {
        return 6;
    }

    public void addChar(char c)
    {
        this.name = this.name.replace("?", "");

        for (this.name = this.name + c; this.name.length() < this.getMaxLength(); this.name = this.name + "?")
        {
            ;
        }
    }

    public void removeChar()
    {
        this.name = this.name.replace("?", "");

        for (this.name = this.name.substring(0, this.name.length() - 1); this.name.length() < this.getMaxLength(); this.name = this.name + "?")
        {
            ;
        }
    }

    public int getByteValue()
    {
        byte val = (byte)(this.val & 255);
        return val;
    }

    public void setByteValue(int val)
    {
        if (val < -128)
        {
            val = -128;
        }
        else if (val > 127)
        {
            val = 127;
        }

        if (val < 0)
        {
            val += 256;
        }

        this.val = (short)val;
    }

    public String getFullInfo()
    {
        return this.getText() + " = " + this.getByteValue();
    }

    public boolean isEditing()
    {
        return (this.info & 1) != 0;
    }

    public void setEditing(boolean val)
    {
        this.info = (short)(this.info & -2);
        this.info = (short)(this.info | (val ? 1 : 0));

        if (val)
        {
            this.module.setWriting(this);
        }
        else if (this.module.getWriting() == this)
        {
            this.module.setWriting((IWriting)null);
        }
    }

    public void setInfo(int id, short val)
    {
        if (id == 0)
        {
            this.info = val;
            this.setEditing(this.isEditing());
        }
        else if (id == 1)
        {
            this.val = val;
        }
        else
        {
            id -= 2;
            byte char1 = (byte)((val & 65280) >> 8);
            byte char2 = (byte)(val & 255);
            this.name = this.name.substring(0, id * 2) + this.getChar(char1) + this.getChar(char2) + this.name.substring(id * 2 + 2);
        }
    }

    private char getChar(int index)
    {
        if (index < 0)
        {
            index += 256;
        }

        if (index >= "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length())
        {
            index = 0;
        }

        return "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(index);
    }

    public short getInfo(int id)
    {
        if (id == 0)
        {
            return this.info;
        }
        else if (id == 1)
        {
            return this.val;
        }
        else
        {
            id -= 2;
            int char1 = this.getCode(this.name.charAt(id * 2));
            int char2 = this.getCode(this.name.charAt(id * 2 + 1));
            return (short)(char1 << 8 | char2);
        }
    }

    private int getCode(char c)
    {
        int index = "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".indexOf(c);

        if (index == -1)
        {
            index = 0;
        }

        return index;
    }
}
