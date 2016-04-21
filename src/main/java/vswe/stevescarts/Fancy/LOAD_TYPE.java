package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum LOAD_TYPE
{
    KEEP("Keep"),
    OVERRIDE("Override"),
    REQUIRE("Require");
    private String code;

    private LOAD_TYPE(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return this.code;
    }
}
