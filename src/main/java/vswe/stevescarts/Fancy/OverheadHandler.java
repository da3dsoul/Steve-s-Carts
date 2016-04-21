package vswe.stevescarts.Fancy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Post;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverheadHandler extends FancyPancyHandler
{
    private ModelRenderer model;
    private Map<String, OverheadHandler.OverheadData> dataObjects;

    public OverheadHandler()
    {
        super("Overhead");
        MinecraftForge.EVENT_BUS.register(this);
        this.dataObjects = new HashMap();
        ModelBase base = new ModelBase()
        {
        };
        this.model = new ModelRenderer(base, 0, 0);
        this.model.addBox(-16.0F, 0.0F, 0.0F, 32, 23, 0);
    }

    private OverheadHandler.OverheadData getData(AbstractClientPlayer player)
    {
        OverheadHandler.OverheadData data = (OverheadHandler.OverheadData)this.dataObjects.get(StringUtils.stripControlCodes(player.getDisplayName()));

        if (data == null)
        {
            data = new OverheadHandler.OverheadData(player, null);
        }

        return data;
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
        return this.getData(player).image;
    }

    public ResourceLocation getCurrentResource(AbstractClientPlayer player)
    {
        return this.getData(player).resourceLocation;
    }

    public void setCurrentResource(AbstractClientPlayer player, ResourceLocation resource, String url)
    {
        OverheadHandler.OverheadData data = this.getData(player);
        data.resourceLocation = resource;
        data.image = this.tryToDownloadFancy(resource, url);
    }

    public LOAD_TYPE getDefaultLoadType()
    {
        return LOAD_TYPE.OVERRIDE;
    }

    public String getDefaultUrl()
    {
        return null;
    }

    @SubscribeEvent
    public void render(Post event)
    {
        if (event.entity instanceof AbstractClientPlayer && event.renderer instanceof RenderPlayer)
        {
            AbstractClientPlayer player = (AbstractClientPlayer)event.entity;
            RenderPlayer renderer = (RenderPlayer)event.renderer;
            EntityClientPlayerMP observer = Minecraft.getMinecraft().thePlayer;
            boolean isObserver = player == observer;
            double distanceSq = player.getDistanceSqToEntity(observer);
            double distanceLimit = player.isSneaking() ? (double)RendererLivingEntity.NAME_TAG_RANGE_SNEAK : (double)RendererLivingEntity.NAME_TAG_RANGE;

            if (distanceSq < distanceLimit * distanceLimit)
            {
                if (player.isPlayerSleeping())
                {
                    this.renderOverHead(renderer, player, event.x, event.y - 1.5D, event.z, isObserver);
                }
                else
                {
                    this.renderOverHead(renderer, player, event.x, event.y, event.z, isObserver);
                }
            }
        }
    }

    private void renderOverHead(RenderPlayer renderer, AbstractClientPlayer player, double x, double y, double z, boolean isObserver)
    {
        OverheadHandler.OverheadData data = this.getData(player);

        if (FancyPancyLoader.isImageReady(data.image))
        {
            RenderManager renderManager = (RenderManager)ReflectionHelper.getPrivateValue(Render.class, renderer, 1);

            if (isObserver && player.openContainer != null && renderManager.playerViewY == 180.0F)
            {
                return;
            }

            renderManager.renderEngine.bindTexture(data.resourceLocation);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + player.height + (isObserver ? 0.8F : 1.1F), (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
            this.model.render(0.015F);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private class OverheadData
    {
        private ResourceLocation resourceLocation;
        private ThreadDownloadImageData image;

        private OverheadData(AbstractClientPlayer player)
        {
            this.resourceLocation = OverheadHandler.this.getDefaultResource(player);
            OverheadHandler.this.dataObjects.put(StringUtils.stripControlCodes(player.getDisplayName()), this);
        }

        OverheadData(AbstractClientPlayer x1, Object x2)
        {
            this(x1);
        }
    }
}
