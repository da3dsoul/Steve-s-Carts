package vswe.stevescarts.Helpers;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class IconData
{
    private IIcon icon;
    private String texture;

    public IconData(IIcon icon, String texture)
    {
        this.icon = icon;
        this.texture = texture;
    }

    public IIcon getIcon()
    {
        return this.icon;
    }

    public String getTexture()
    {
        return this.texture;
    }

    public ResourceLocation getResource()
    {
        return ResourceHelper.getResourceFromPath(this.texture);
    }
}
