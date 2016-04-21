package vswe.stevescarts.Helpers;

import net.minecraft.util.StatCollector;
import vswe.stevescarts.ModuleData.ModuleData;

public class ModuleCountPair
{
    private ModuleData data;
    private int count;
    private String name;
    private byte extraData;

    public ModuleCountPair(ModuleData data)
    {
        this.data = data;
        this.count = 1;
        this.name = data.getUnlocalizedName();
    }

    public int getCount()
    {
        return this.count;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void increase()
    {
        ++this.count;
    }

    public boolean isContainingData(ModuleData data)
    {
        return this.data.equals(data);
    }

    public ModuleData getData()
    {
        return this.data;
    }

    public void setExtraData(byte b)
    {
        this.extraData = b;
    }

    public String toString()
    {
        String ret = this.data.getCartInfoText(StatCollector.translateToLocal(this.name), this.extraData);

        if (this.count != 1)
        {
            ret = ret + " x" + this.count;
        }

        return ret;
    }
}
