package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ResourceHelper
{
    private static HashMap<String, ResourceLocation> resources = new HashMap();
    private static HashMap<String, ResourceLocation> pathResources = new HashMap();

    public static ResourceLocation getResource(String path)
    {
        return new ResourceLocation("stevescarts", "textures" + path);
    }

    public static ResourceLocation getResourceFromPath(String path)
    {
        return new ResourceLocation("textures" + path);
    }

    public static void bindResource(ResourceLocation resource)
    {
        if (resource != null)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        }
    }

    public static void bindResource(String path)
    {
        if (resources.containsKey(path))
        {
            bindResource((ResourceLocation)resources.get(path));
        }
        else
        {
            ResourceLocation resource = getResource(path);
            resources.put(path, resource);
            bindResource(resource);
        }
    }

    public static void bindResourcePath(String path)
    {
        if (pathResources.containsKey(path))
        {
            bindResource((ResourceLocation)pathResources.get(path));
        }
        else
        {
            ResourceLocation resource = getResourceFromPath(path);
            pathResources.put(path, resource);
            bindResource(resource);
        }
    }
}
