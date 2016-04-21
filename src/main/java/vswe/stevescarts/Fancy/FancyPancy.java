package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.StringUtils;

@SideOnly(Side.CLIENT)
public class FancyPancy
{
    private FancyPancyHandler fancyPancyHandler;
    public int priority = 0;
    private String[] images;
    private int currentImage = 0;
    private EnumSet<ANIMATION_TYPE> types;
    private int interval = 50;
    private int intervalWait = 200;
    private int ticks;
    private String[] servers;
    private boolean hasServerBlackList;
    private LOAD_TYPE loadType;
    private List<FancyPancy.SpecialLoadSetting> loadPixelSettings;
    private String[] teams;
    private List<int[]> dimensions;
    private List<long[]> times;
    private boolean useLocalTime = true;
    private String[] observers;
    private boolean hasObserverBlacklist;
    private String[] observerTeams;

    public FancyPancy(FancyPancyHandler fancyPancyHandler, HashMap<String, String[]> entries)
    {
        this.fancyPancyHandler = fancyPancyHandler;
        this.addImages((String[])entries.get("Image"));
        this.setTypes((String[])entries.get("Animation"));
        this.setInterval((String[])entries.get("Interval"));
        this.setPriority((String[])entries.get("Priority"));
        this.setServers((String[])entries.get("Server"), (String[])entries.get("Blocked"));
        this.setLoad((String[])entries.get("Load"));
        this.setTeams((String[])entries.get("Team"));
        this.setDimensions((String[])entries.get("World"));
        this.setTimes((String[])entries.get("Time"));
        this.setTimeMode((String[])entries.get("Zone"));
        this.setObservers((String[])entries.get("Observer"), (String[])entries.get("BlockedObserver"), (String[])entries.get("ObserverTeam"));
        this.loadType = fancyPancyHandler.getDefaultLoadType();
    }

    private void setObservers(String[] observers, String[] blockedObservers, String[] observerTeams)
    {
        if (observers != null && observers.length > 0)
        {
            this.observers = observers;
            this.hasObserverBlacklist = false;
        }
        else if (blockedObservers != null && blockedObservers.length > 0)
        {
            this.observers = blockedObservers;
            this.hasObserverBlacklist = true;
        }

        this.observerTeams = observerTeams;
    }

    private void setTimeMode(String[] zones)
    {
        if (zones != null && zones.length > 0)
        {
            if (zones[0].equals("Local"))
            {
                this.useLocalTime = true;
            }
            else if (zones[0].equals("GMT"))
            {
                this.useLocalTime = false;
            }
        }
    }

    public void setTimes(String[] times)
    {
        if (times != null)
        {
            this.times = new ArrayList();
            String[] arr$ = times;
            int len$ = times.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                String level = arr$[i$];
                String[] split = level.split(";");

                if (split.length == 2)
                {
                    try
                    {
                        long[] ignored = new long[split.length];

                        for (int i = 0; i < split.length; ++i)
                        {
                            ignored[i] = Long.parseLong(split[i]);
                        }

                        this.times.add(ignored);
                    }
                    catch (Exception var9)
                    {
                        ;
                    }
                }
            }

            if (this.times.size() == 0)
            {
                this.times = null;
            }
        }
    }

    public void setDimensions(String[] dimensions)
    {
        if (dimensions != null)
        {
            this.dimensions = new ArrayList();
            String[] arr$ = dimensions;
            int len$ = dimensions.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                String dimension = arr$[i$];
                String[] split = dimension.split(";");

                if (split.length == 1 || split.length == 2)
                {
                    try
                    {
                        int[] ignored = new int[split.length];

                        for (int i = 0; i < split.length; ++i)
                        {
                            ignored[i] = Integer.parseInt(split[i].trim());
                        }

                        this.dimensions.add(ignored);
                    }
                    catch (Exception var9)
                    {
                        ;
                    }
                }
            }

            if (this.dimensions.size() == 0)
            {
                this.dimensions = null;
            }
        }
    }

    public void update()
    {
        if (this.types.contains(ANIMATION_TYPE.PAUSE) && this.currentImage == this.images.length - 1 && this.intervalWait >= 0)
        {
            if (++this.ticks >= this.intervalWait)
            {
                this.ticks = 0;
                this.nextImage();
            }
        }
        else if (!this.types.contains(ANIMATION_TYPE.STILL) && this.interval >= 0 && ++this.ticks >= this.interval)
        {
            this.ticks = 0;
            this.nextImage();
        }
    }

    private void setTypes(String[] typeStrings)
    {
        this.types = EnumSet.noneOf(ANIMATION_TYPE.class);

        if (typeStrings != null)
        {
            String[] arr$ = typeStrings;
            int len$ = typeStrings.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                String typeString = arr$[i$];
                ANIMATION_TYPE[] arr$1 = ANIMATION_TYPE.values();
                int len$1 = arr$1.length;

                for (int i$1 = 0; i$1 < len$1; ++i$1)
                {
                    ANIMATION_TYPE animation = arr$1[i$1];

                    if (animation.getCode().equals(typeString))
                    {
                        this.types.add(animation);
                        break;
                    }
                }
            }
        }

        if (this.types.size() == 0)
        {
            this.types.add(ANIMATION_TYPE.STILL);
        }
        else if (this.types.contains(ANIMATION_TYPE.RANDOM))
        {
            this.nextImage();
        }
    }

    private void nextImage()
    {
        if (this.types.contains(ANIMATION_TYPE.RANDOM))
        {
            this.currentImage = (int)(Math.random() * (double)this.images.length);
        }
        else
        {
            this.currentImage = (this.currentImage + 1) % this.images.length;
        }
    }

    public String getImage()
    {
        return this.images == null ? null : this.images[this.currentImage];
    }

    public void setInterval(String[] intervals)
    {
        if (intervals != null && intervals.length >= 1)
        {
            try
            {
                this.interval = Integer.parseInt(intervals[0]);

                if (intervals.length >= 2)
                {
                    this.intervalWait = Integer.parseInt(intervals[1]);
                }
            }
            catch (Exception var3)
            {
                ;
            }
        }
    }

    public void addImages(String[] images)
    {
        if (images != null)
        {
            this.images = new String[images.length];

            for (int i = 0; i < images.length; ++i)
            {
                String image = images[i];

                if (image.startsWith("^"))
                {
                    image = image.substring(1);
                }
                else if (image.startsWith("*"))
                {
                    String defaultPath = this.fancyPancyHandler.getDefaultUrl();

                    if (defaultPath == null)
                    {
                        defaultPath = "https://dl.dropbox.com/u/46486053/";
                    }

                    image = defaultPath + image.substring(1) + ".png";
                }
                else
                {
                    image = "https://dl.dropbox.com/u/46486053/" + image + ".png";
                }

                this.images[i] = image;
            }
        }
    }

    public void setPriority(String[] priorities)
    {
        if (priorities != null)
        {
            try
            {
                this.priority = Integer.parseInt(priorities[0]);
            }
            catch (Exception var3)
            {
                ;
            }
        }
    }

    public void setServers(String[] serversWhiteList, String[] serversBlackList)
    {
        if (serversWhiteList != null && serversWhiteList.length > 0)
        {
            this.servers = serversWhiteList;
            this.hasServerBlackList = false;
        }
        else if (serversBlackList != null && serversBlackList.length > 0)
        {
            this.servers = serversBlackList;
            this.hasServerBlackList = true;
        }
    }

    public boolean isValid(AbstractClientPlayer player, boolean hasMojangFancy, boolean usingMojangFancy)
    {
        if (!this.isObserverValid())
        {
            return false;
        }
        else if (!this.isServerValid())
        {
            return false;
        }
        else if (!this.isTimeValid())
        {
            return false;
        }
        else if (!this.isTeamValid(player))
        {
            return false;
        }
        else if (!this.isDimensionValid(player))
        {
            return false;
        }
        else if (hasMojangFancy && !usingMojangFancy && this.loadPixelSettings != null)
        {
            return false;
        }
        else
        {
            boolean specialCheck = this.loadPixelSettings == null || usingMojangFancy && this.doSpecialCheck(player);

            switch (FancyPancy.NamelessClass308090458.$SwitchMap$vswe$stevescarts$Fancy$LOAD_TYPE[this.loadType.ordinal()])
            {
                case 1:
                    return !hasMojangFancy || !specialCheck;

                case 2:
                    return !hasMojangFancy || specialCheck;

                case 3:
                    return hasMojangFancy && specialCheck;

                default:
                    return false;
            }
        }
    }

    private boolean isObserverValid()
    {
        EntityClientPlayerMP observerPlayer = Minecraft.getMinecraft().thePlayer;
        String observerName = StringUtils.stripControlCodes(observerPlayer.getDisplayName());

        if (this.observers != null && this.observers.length != 0)
        {
            boolean foundObserver = false;
            String[] arr$ = this.observers;
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                String observer = arr$[i$];

                if (observer.equals(observerName))
                {
                    foundObserver = true;
                    break;
                }
            }

            if (this.hasObserverBlacklist == foundObserver)
            {
                return false;
            }
        }

        return this.isTeamValid(observerPlayer, this.observerTeams);
    }

    private boolean isTimeValid()
    {
        if (this.times == null)
        {
            return true;
        }
        else
        {
            Calendar calendar;

            if (this.useLocalTime)
            {
                calendar = Calendar.getInstance();
            }
            else
            {
                calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            }

            long now = (calendar.getTimeInMillis() + (long)calendar.getTimeZone().getOffset(calendar.getTimeInMillis())) / 1000L;
            Iterator i$ = this.times.iterator();
            long[] time;

            do
            {
                if (!i$.hasNext())
                {
                    return false;
                }

                time = (long[])i$.next();
            }
            while (time[0] > now || now > time[1]);

            return true;
        }
    }

    private boolean isDimensionValid(AbstractClientPlayer player)
    {
        if (this.dimensions != null && this.dimensions.size() != 0)
        {
            if (player.worldObj != null && player.worldObj.provider != null)
            {
                int id = player.worldObj.provider.dimensionId;
                Iterator i$ = this.dimensions.iterator();
                int[] levelRange;

                do
                {
                    if (!i$.hasNext())
                    {
                        return false;
                    }

                    levelRange = (int[])i$.next();
                }
                while ((levelRange.length != 1 || levelRange[0] != id) && (levelRange.length != 2 || levelRange[0] > id || id > levelRange[1]));

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    private boolean isTeamValid(AbstractClientPlayer player)
    {
        return this.isTeamValid(player, this.teams);
    }

    private boolean isTeamValid(AbstractClientPlayer player, String[] teams)
    {
        if (teams != null && teams.length != 0)
        {
            Team team = player.getTeam();
            String requiredTeam = team == null ? "~" : team.getRegisteredName();
            String[] arr$ = teams;
            int len$ = teams.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                String teamName = arr$[i$];

                if (teamName.equals(requiredTeam))
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean doSpecialCheck(AbstractClientPlayer player)
    {
        ThreadDownloadImageData data = this.fancyPancyHandler.getCurrentTexture(player);

        if (data == null)
        {
            return false;
        }
        else
        {
            BufferedImage image = (BufferedImage)ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, data, 2);

            if (image != null)
            {
                Iterator i$ = this.loadPixelSettings.iterator();

                while (i$.hasNext())
                {
                    FancyPancy.SpecialLoadSetting loadPixelSetting = (FancyPancy.SpecialLoadSetting)i$.next();
                    int color = image.getRGB(loadPixelSetting.x, loadPixelSetting.y);
                    int R = color >>> 16 & 255;
                    int G = color >>> 8 & 255;
                    int B = color & 255;

                    if (R == loadPixelSetting.r && G == loadPixelSetting.g && B == loadPixelSetting.b)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean isServerValid()
    {
        if (this.servers == null)
        {
            return true;
        }
        else
        {
            String[] arr$ = this.servers;
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                String server = arr$[i$];

                if (server.equals(this.fancyPancyHandler.getServerHash()))
                {
                    return !this.hasServerBlackList;
                }
            }

            return this.hasServerBlackList;
        }
    }

    public void setLoad(String[] loadSettings)
    {
        if (loadSettings != null && loadSettings.length > 0)
        {
            String loadTypeString = loadSettings[0];
            LOAD_TYPE[] i = LOAD_TYPE.values();
            int setting = i.length;

            for (int split = 0; split < setting; ++split)
            {
                LOAD_TYPE ignored = i[split];

                if (ignored.getCode().equals(loadTypeString))
                {
                    this.loadType = ignored;
                    break;
                }
            }

            if (loadSettings.length > 1)
            {
                this.loadPixelSettings = new ArrayList();

                for (int var8 = 1; var8 < loadSettings.length; ++var8)
                {
                    String var9 = loadSettings[var8];
                    String[] var10 = var9.split(";");

                    if (var10.length == 5)
                    {
                        try
                        {
                            FancyPancy.SpecialLoadSetting var11 = new FancyPancy.SpecialLoadSetting((FancyPancy.NamelessClass308090458)null);
                            var11.x = Integer.parseInt(var10[0].trim());
                            var11.y = Integer.parseInt(var10[1].trim());
                            var11.r = Integer.parseInt(var10[2].trim());
                            var11.g = Integer.parseInt(var10[3].trim());
                            var11.b = Integer.parseInt(var10[4].trim());
                            this.loadPixelSettings.add(var11);
                        }
                        catch (Exception var7)
                        {
                            ;
                        }
                    }
                }

                if (this.loadPixelSettings.size() == 0)
                {
                    this.loadPixelSettings = null;
                }
            }
        }
    }

    public void setTeams(String[] teams)
    {
        this.teams = teams;
    }

    static class NamelessClass308090458
    {
        static final int[] $SwitchMap$vswe$stevescarts$Fancy$LOAD_TYPE = new int[LOAD_TYPE.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Fancy$LOAD_TYPE[LOAD_TYPE.KEEP.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Fancy$LOAD_TYPE[LOAD_TYPE.OVERRIDE.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Fancy$LOAD_TYPE[LOAD_TYPE.REQUIRE.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    private class SpecialLoadSetting
    {
        private int x;
        private int y;
        private int r;
        private int g;
        private int b;

        private SpecialLoadSetting() {}

        SpecialLoadSetting(FancyPancy.NamelessClass308090458 x1)
        {
            this();
        }
    }
}
