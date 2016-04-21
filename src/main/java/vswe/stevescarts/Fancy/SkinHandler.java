package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SkinHandler extends FancyPancyHandler
{
    public SkinHandler()
    {
        super("Skin");
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
        return player.getLocationSkin();
    }

    public void setCurrentResource(AbstractClientPlayer player, ResourceLocation resource, String url)
    {
        ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, resource, 3);
        ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, this.tryToDownloadFancy(resource, url, AbstractClientPlayer.locationStevePng, new ImageBufferDownload()), 1);
    }

    public LOAD_TYPE getDefaultLoadType()
    {
        return LOAD_TYPE.OVERRIDE;
    }

    public String getDefaultUrl()
    {
        return "http://skins.minecraft.net/MinecraftSkins/";
    }
}
