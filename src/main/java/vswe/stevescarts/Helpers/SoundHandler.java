package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SoundHandler
{
    public static void playDefaultSound(String name, float volume, float pitch)
    {
        SoundHandler.PlayerSound soundObj = new SoundHandler.PlayerSound(Minecraft.getMinecraft().thePlayer, name, volume, pitch);
        Minecraft.getMinecraft().getSoundHandler().playSound(soundObj);
    }

    public static void playSound(String name, float volume, float pitch)
    {
        playDefaultSound("stevescarts:" + name, volume, pitch);
    }

    private static class PlayerSound extends PositionedSound
    {
        private EntityPlayer player;

        protected PlayerSound(EntityPlayer player, String name, float volume, float pitch)
        {
            super(new ResourceLocation(name));
            this.player = player;
            this.volume = volume;
            this.field_147663_c = pitch;
            this.update();
        }

        private void update()
        {
            this.xPosF = (float)this.player.posX;
            this.yPosF = (float)this.player.posY;
            this.zPosF = (float)this.player.posZ;
        }
    }
}
