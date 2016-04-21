package vswe.stevescarts.Listeners;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import vswe.stevescarts.Carts.MinecartModular;

public class OverlayRenderer
{
    public OverlayRenderer()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        if (event.phase == Phase.END)
        {
            this.renderOverlay();
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderOverlay()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;

        if (minecraft.currentScreen == null && player.ridingEntity != null && player.ridingEntity instanceof MinecartModular)
        {
            ((MinecartModular)player.ridingEntity).renderOverlay(minecraft);
        }
    }
}
