package vswe.stevescarts.Fancy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import vswe.stevescarts.Helpers.ResourceHelper;

@SideOnly(Side.CLIENT)
public abstract class FancyPancyHandler
{
    private final String code;
    private static final int PROTOCOL_VERSION = 0;
    private HashMap<String, UserFancy> fancies;
    private HashMap<String, ServerFancy> serverFancies;
    private boolean ready = false;
    private String serverHash;
    private int serverReHash;

    public HashMap<String, ServerFancy> getServerFancies()
    {
        return this.serverFancies;
    }

    public HashMap<String, UserFancy> getFancies()
    {
        return this.fancies;
    }

    public final String getCode()
    {
        return this.code;
    }

    public FancyPancyHandler(String code)
    {
        this.code = code;
        FMLCommonHandler.instance().bus().register(this);
        this.fancies = new HashMap();
        this.serverFancies = new HashMap();
    }

    public void setReady(boolean ready)
    {
        this.ready = ready;
    }

    private void generateServerHash()
    {
        ServerData data = (ServerData)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 7);
        String ip;

        if (data != null)
        {
            if (data.serverIP.equals("127.0.0.1"))
            {
                ip = "localhost";
            }
            else
            {
                ip = data.serverIP;
            }
        }
        else if (Minecraft.getMinecraft().getIntegratedServer() != null && Minecraft.getMinecraft().getIntegratedServer().getPublic())
        {
            ip = "localhost";
        }
        else
        {
            ip = "single player";
        }

        this.serverHash = this.md5(ip.toLowerCase());
        this.serverReHash = 100;
    }

    public String getServerHash()
    {
        if (this.serverReHash == 0)
        {
            this.generateServerHash();
        }

        return this.serverHash;
    }

    private String md5(String str)
    {
        try
        {
            MessageDigest ignored = MessageDigest.getInstance("MD5");
            ignored.update(str.getBytes());
            byte[] bytes = ignored.digest();
            String result = "";
            byte[] arr$ = bytes;
            int len$ = bytes.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                byte b = arr$[i$];
                result = result + Integer.toString((b & 255) + 256, 16).substring(1);
            }

            return result;
        }
        catch (NoSuchAlgorithmException var9)
        {
            return null;
        }
    }

    @SubscribeEvent
    public void tick(PlayerTickEvent event)
    {
        if (this.ready && event.phase == Phase.START)
        {
            if (this.serverReHash > 0)
            {
                --this.serverReHash;
            }

            EntityPlayer player = event.player;

            if (player instanceof AbstractClientPlayer)
            {
                this.loadNewFancy((AbstractClientPlayer)player);
            }
        }
    }

    private void loadNewFancy(AbstractClientPlayer player)
    {
        if (player != null)
        {
            String username = StringUtils.stripControlCodes(player.getDisplayName());
            UserFancy fancyObj = (UserFancy)this.fancies.get(username);

            if (fancyObj == null && this.serverFancies.size() > 0 && this.serverFancies.containsKey(this.getServerHash()))
            {
                fancyObj = new UserFancy(this);
                this.fancies.put(username, fancyObj);
            }

            if (fancyObj != null)
            {
                fancyObj.update(player);
                String fancy = fancyObj.getImage(player);

                if (fancy != null)
                {
                    ResourceLocation loc = ResourceHelper.getResourceFromPath(fancy);

                    if (!loc.equals(this.getCurrentResource(player)))
                    {
                        this.setCurrentResource(player, loc, fancy);
                    }
                }
            }
        }
    }

    public ThreadDownloadImageData tryToDownloadFancy(ResourceLocation fancy, String fancyUrl)
    {
        return this.tryToDownloadFancy(fancy, fancyUrl, (ResourceLocation)null, (IImageBuffer)null);
    }

    public ThreadDownloadImageData tryToDownloadFancy(ResourceLocation fancy, String fancyUrl, ResourceLocation fallbackResource, IImageBuffer optionalBuffer)
    {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        Object object = texturemanager.getTexture(fancy);

        if (object == null)
        {
            object = new ThreadDownloadImageData((File)null, fancyUrl, fallbackResource, optionalBuffer);
            texturemanager.loadTexture(fancy, (ITextureObject)object);
        }

        return (ThreadDownloadImageData)object;
    }

    public abstract String getDefaultUrl(AbstractClientPlayer var1);

    public abstract ResourceLocation getDefaultResource(AbstractClientPlayer var1);

    public abstract ThreadDownloadImageData getCurrentTexture(AbstractClientPlayer var1);

    public abstract ResourceLocation getCurrentResource(AbstractClientPlayer var1);

    public abstract void setCurrentResource(AbstractClientPlayer var1, ResourceLocation var2, String var3);

    public abstract LOAD_TYPE getDefaultLoadType();

    public abstract String getDefaultUrl();
}
