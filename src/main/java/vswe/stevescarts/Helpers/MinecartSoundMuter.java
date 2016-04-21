package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.MinecraftForge;
import vswe.stevescarts.Carts.MinecartModular;

public class MinecartSoundMuter
{
    public MinecartSoundMuter()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void soundPlay(PlaySoundEvent17 event)
    {
        ISound sound = event.sound;
        EntityMinecart cart;
        MinecartModular modular;

        if (sound instanceof MovingSoundMinecartRiding)
        {
            MovingSoundMinecartRiding cartSound = (MovingSoundMinecartRiding)sound;
            cart = (EntityMinecart)ReflectionHelper.getPrivateValue(MovingSoundMinecartRiding.class, cartSound, 1);

            if (cart instanceof MinecartModular)
            {
                modular = (MinecartModular)cart;
                modular.setSound(cartSound, true);
            }
        }
        else if (sound instanceof MovingSoundMinecart)
        {
            MovingSoundMinecart cartSound1 = (MovingSoundMinecart)sound;
            cart = (EntityMinecart)ReflectionHelper.getPrivateValue(MovingSoundMinecart.class, cartSound1, 0);

            if (cart instanceof MinecartModular)
            {
                modular = (MinecartModular)cart;
                modular.setSound(cartSound1, false);
            }
        }
    }
}
