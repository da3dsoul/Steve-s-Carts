package vswe.stevescarts.Helpers;

import vswe.stevescarts.Modules.ModuleBase;

public class DropDownMenuItem
{
    private String name;
    private int imageID;
    private DropDownMenuItem.VALUETYPE type;
    private boolean isLarge;
    private boolean subOpen;
    private byte value;
    private Class <? extends ModuleBase > moduleClass;
    private Class <? extends ModuleBase > excludedClass;
    private int multiCount;
    private int intMinValue;
    private int intMaxValue;

    public DropDownMenuItem(String name, int imageID, DropDownMenuItem.VALUETYPE type, Class <? extends ModuleBase > moduleClass)
    {
        this(name, imageID, type, moduleClass, (Class)null);
    }

    public DropDownMenuItem(String name, int imageID, DropDownMenuItem.VALUETYPE type, Class <? extends ModuleBase > moduleClass, Class <? extends ModuleBase > excludedClass)
    {
        this.name = name;
        this.imageID = imageID;
        this.type = type;
        this.moduleClass = moduleClass;
        this.excludedClass = excludedClass;
        this.isLarge = false;
        this.subOpen = false;
        this.value = 0;
    }

    public String getName()
    {
        return this.name;
    }

    public Class <? extends ModuleBase > getModuleClass()
    {
        return this.moduleClass;
    }

    public Class <? extends ModuleBase > getExcludedClass()
    {
        return this.excludedClass;
    }

    public int getImageID()
    {
        return this.imageID;
    }

    public boolean hasSubmenu()
    {
        return this.type != DropDownMenuItem.VALUETYPE.BOOL;
    }

    public boolean getIsSubMenuOpen()
    {
        return this.subOpen;
    }

    public void setIsSubMenuOpen(boolean val)
    {
        this.subOpen = val;
    }

    public boolean getIsLarge()
    {
        return this.isLarge;
    }

    public void setIsLarge(boolean val)
    {
        this.isLarge = val;
    }

    public int[] getRect(int menuX, int menuY, int id)
    {
        return this.getIsLarge() ? new int[] {menuX, menuY + id * 20, 130, 20}: new int[] {menuX, menuY + id * 20, 54, 20};
    }

    public int[] getSubRect(int menuX, int menuY, int id)
    {
        return this.getIsSubMenuOpen() ? new int[] {menuX - 43, menuY + id * 20 + 2, 52, 16}: new int[] {menuX, menuY + id * 20 + 2, 9, 16};
    }

    public DropDownMenuItem.VALUETYPE getType()
    {
        return this.type;
    }

    public boolean getBOOL()
    {
        return this.value != 0;
    }

    public void setBOOL(boolean val)
    {
        this.value = (byte)(val ? 1 : 0);
    }

    public int getINT()
    {
        return this.value;
    }

    public void setINT(int val)
    {
        if (val < this.intMinValue)
        {
            val = this.intMinValue;
        }
        else if (val > this.intMaxValue)
        {
            val = this.intMaxValue;
        }

        this.value = (byte)val;
    }

    public void setMULTIBOOL(byte val)
    {
        this.value = val;
    }

    public void setMULTIBOOL(int i, boolean val)
    {
        this.value = (byte)(this.value & ~(1 << i) | (val ? 1 : 0) << i);
    }

    public byte getMULTIBOOL()
    {
        return this.value;
    }

    public boolean getMULTIBOOL(int i)
    {
        return (this.value & 1 << i) != 0;
    }

    public void setMULTIBOOLCount(int val)
    {
        if (val > 4)
        {
            val = 4;
        }
        else if (val < 2)
        {
            val = 2;
        }

        this.multiCount = val;
    }

    public int getMULTIBOOLCount()
    {
        return this.multiCount;
    }

    public void setINTLimit(int min, int max)
    {
        this.intMinValue = min;
        this.intMaxValue = max;
        this.setINT(this.getINT());
    }

    public static enum VALUETYPE
    {
        BOOL("BOOL", 0),
        INT("INT", 1),
        MULTIBOOL("MULTIBOOL", 2);

        private static final DropDownMenuItem.VALUETYPE[] $VALUES = new DropDownMenuItem.VALUETYPE[]{BOOL, INT, MULTIBOOL};

        private VALUETYPE(String var1, int var2) {}
    }
}
