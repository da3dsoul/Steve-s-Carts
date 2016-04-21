package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ServerFancy
{
    private List<FancyPancy> fancies = new ArrayList();

    public void add(FancyPancy fancyPancy)
    {
        this.fancies.add(fancyPancy);
    }

    public List<FancyPancy> getFancies()
    {
        return this.fancies;
    }
}
