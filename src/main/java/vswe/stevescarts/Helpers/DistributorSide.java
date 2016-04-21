package vswe.stevescarts.Helpers;

import java.util.Iterator;
import net.minecraftforge.common.util.ForgeDirection;
import vswe.stevescarts.TileEntities.TileEntityDistributor;

public class DistributorSide
{
    private int id;
    private Localization.GUI.DISTRIBUTOR name;
    private ForgeDirection side;
    private int data;

    public DistributorSide(int id, Localization.GUI.DISTRIBUTOR name, ForgeDirection side)
    {
        this.name = name;
        this.id = id;
        this.side = side;
        this.data = 0;
    }

    public void setData(int data)
    {
        this.data = data;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name.translate(new String[0]);
    }

    public ForgeDirection getSide()
    {
        return this.side;
    }

    public int getIntSide()
    {
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
        {
            if (ForgeDirection.VALID_DIRECTIONS[i] == this.side)
            {
                return i;
            }
        }

        return 6;
    }

    public int getData()
    {
        return this.data;
    }

    public boolean isEnabled(TileEntityDistributor distributor)
    {
        return distributor.getInventories().length == 0 ? false : (this.getSide() == ForgeDirection.DOWN ? !distributor.hasBot : (this.getSide() == ForgeDirection.UP ? !distributor.hasTop : true));
    }

    public boolean isSet(int id)
    {
        return (this.data & 1 << id) != 0;
    }

    public void set(int id)
    {
        int count = 0;
        Iterator i$ = DistributorSetting.settings.iterator();

        while (i$.hasNext())
        {
            DistributorSetting setting = (DistributorSetting)i$.next();

            if (this.isSet(setting.getId()))
            {
                ++count;
            }
        }

        if (count < 11)
        {
            this.data |= 1 << id;
        }
    }

    public void reset(int id)
    {
        this.data &= ~(1 << id);
    }

    public short getLowShortData()
    {
        return (short)(this.getData() & 65535);
    }

    public short getHighShortData()
    {
        return (short)(this.getData() >> 16 & 65535);
    }

    public void setLowShortData(short data)
    {
        this.data = this.fixSignedIssue(this.getHighShortData()) << 16 | this.fixSignedIssue(data);
    }

    public void setHighShortData(short data)
    {
        this.data = this.fixSignedIssue(this.getLowShortData()) | this.fixSignedIssue(data) << 16;
    }

    private int fixSignedIssue(short val)
    {
        return val < 0 ? val + 65536 : val;
    }

    public String getInfo()
    {
        return Localization.GUI.DISTRIBUTOR.SIDE_TOOL_TIP.translate(new String[] {this.getName()});
    }
}
