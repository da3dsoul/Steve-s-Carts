package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class CapeHandler extends FancyPancyHandler
{
    public CapeHandler()
    {
        super("Cape");
    }

    public String getDefaultUrl(AbstractClientPlayer player)
    {
        return null;
    }

    public ResourceLocation getDefaultResource(AbstractClientPlayer player)
    {
        return null;
    }

    public ThreadDownloadImageData getCurrentTexture(AbstractClientPlayer player)
    {
        return null;
    }

    public ResourceLocation getCurrentResource(AbstractClientPlayer player)
    {
        return player.getLocationCape();
    }

    public void setCurrentResource(AbstractClientPlayer player, ResourceLocation resource, String url)
    {
        ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, resource, 4);
        ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, this.tryToDownloadFancy(resource, url), 2);
    }

    public LOAD_TYPE getDefaultLoadType()
    {
        return LOAD_TYPE.KEEP;
    }

    public String getDefaultUrl()
    {
        return "http://skins.minecraft.net/MinecraftCloaks/";
    }
}
