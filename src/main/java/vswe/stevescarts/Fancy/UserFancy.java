package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;

@SideOnly(Side.CLIENT)
public class UserFancy
{
    private FancyPancyHandler fancyPancyHandler;
    private FancyPancy activeFancyPancy;
    private int activeCheck = 100;
    private List<FancyPancy> fancies = new ArrayList();
    private boolean hasMojangFancy;
    private boolean doneMojangFancyCheck;

    public UserFancy(FancyPancyHandler fancyPancyHandler)
    {
        this.fancyPancyHandler = fancyPancyHandler;
    }

    public void update(AbstractClientPlayer player)
    {
        this.updateMojangFancyState(player);

        if (this.activeFancyPancy != null)
        {
            this.activeFancyPancy.update();
        }

        if (++this.activeCheck >= 100 || this.activeCheck >= 20 && player.equals(Minecraft.getMinecraft().thePlayer))
        {
            this.activeCheck = 0;
            this.updateActive(player);
        }
    }

    private void updateMojangFancyState(AbstractClientPlayer player)
    {
        if (!this.doneMojangFancyCheck)
        {
            ThreadDownloadImageData fancyData = this.fancyPancyHandler.getCurrentTexture(player);

            if (fancyData != null)
            {
                Thread thread = (Thread)ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, fancyData, 5);

                if (thread != null && !thread.isAlive())
                {
                    this.hasMojangFancy = FancyPancyLoader.isImageReady(fancyData);
                    this.doneMojangFancyCheck = true;
                    this.updateActive(player);
                }
            }
            else
            {
                this.doneMojangFancyCheck = true;
                this.updateActive(player);
            }
        }
    }

    private void updateActive(AbstractClientPlayer player)
    {
        this.activeFancyPancy = null;

        if (this.doneMojangFancyCheck)
        {
            int highest = Integer.MIN_VALUE;
            FancyPancy i$;

            for (Iterator serverFancy = this.fancies.iterator(); serverFancy.hasNext(); highest = this.findHighPriorityFancy(player, i$, highest))
            {
                i$ = (FancyPancy)serverFancy.next();
            }

            ServerFancy serverFancy1 = (ServerFancy)this.fancyPancyHandler.getServerFancies().get(this.fancyPancyHandler.getServerHash());
            FancyPancy fancyPancy;

            if (serverFancy1 != null)
            {
                for (Iterator i$1 = serverFancy1.getFancies().iterator(); i$1.hasNext(); highest = this.findHighPriorityFancy(player, fancyPancy, highest))
                {
                    fancyPancy = (FancyPancy)i$1.next();
                }
            }
        }
    }

    private int findHighPriorityFancy(AbstractClientPlayer player, FancyPancy fancyPancy, int highest)
    {
        if (fancyPancy.isValid(player, this.hasMojangFancy, this.isUsingMojangFancy(player)) && fancyPancy.priority > highest)
        {
            highest = fancyPancy.priority;
            this.activeFancyPancy = fancyPancy;
        }

        return highest;
    }

    private boolean isUsingMojangFancy(AbstractClientPlayer player)
    {
        return this.hasMojangFancy && this.fancyPancyHandler.getDefaultResource(player).equals(this.fancyPancyHandler.getCurrentResource(player));
    }

    public void add(FancyPancy fancyPancy)
    {
        this.fancies.add(fancyPancy);
    }

    public String getImage(AbstractClientPlayer player)
    {
        return this.activeFancyPancy != null ? this.activeFancyPancy.getImage() : this.fancyPancyHandler.getDefaultUrl(player);
    }
}
