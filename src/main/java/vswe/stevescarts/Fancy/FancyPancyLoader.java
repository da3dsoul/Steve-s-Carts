package vswe.stevescarts.Fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.ThreadDownloadImageData;

@SideOnly(Side.CLIENT)
public class FancyPancyLoader implements Runnable
{
    private Map<String, FancyPancyHandler> fancyTypes = new HashMap();

    public static boolean isImageReady(ThreadDownloadImageData image)
    {
        if (image == null)
        {
            return false;
        }
        else
        {
            image.getGlTextureId();
            return ((Boolean)ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, image, 7)).booleanValue();
        }
    }

    public FancyPancyLoader()
    {
        this.add(new OverheadHandler());
        (new Thread(this)).start();
    }

    private void add(FancyPancyHandler fancyPancyHandler)
    {
        this.fancyTypes.put(fancyPancyHandler.getCode(), fancyPancyHandler);
    }

    public void run()
    {
        try
        {
            HttpURLConnection i$ = (HttpURLConnection)(new URL("https://dl.dropbox.com/u/46486053/RemoteInfo.txt")).openConnection();
            HttpURLConnection.setFollowRedirects(true);
            i$.setConnectTimeout(Integer.MAX_VALUE);
            i$.setDoInput(true);
            i$.connect();
            BufferedReader fancyPancyHandler = new BufferedReader(new InputStreamReader(i$.getInputStream()));
            String line;

            while ((line = fancyPancyHandler.readLine()) != null)
            {
                int commentStart = line.indexOf("#");

                if (commentStart != -1)
                {
                    line = line.substring(0, commentStart);
                }

                line = line.trim();
                String[] split = line.split(":");

                if (split.length == 2)
                {
                    String command = split[0];
                    String content = split[1];
                    HashMap entries = new HashMap();
                    int start = 0;
                    int i$1;

                    while (true)
                    {
                        int fancyPancyHandler1 = content.indexOf("[", start);

                        if (fancyPancyHandler1 == -1)
                        {
                            break;
                        }

                        String users = content.substring(start, fancyPancyHandler1).trim();
                        int servers = content.indexOf("]", fancyPancyHandler1);

                        if (servers == -1)
                        {
                            break;
                        }

                        start = servers + 1;
                        String arr$ = content.substring(fancyPancyHandler1 + 1, servers).trim();
                        String[] len$ = arr$.split(",");

                        for (i$1 = 0; i$1 < len$.length; ++i$1)
                        {
                            len$[i$1] = len$[i$1].trim();
                        }

                        entries.put(users, len$);
                    }

                    FancyPancyHandler var21 = (FancyPancyHandler)this.fancyTypes.get(command);

                    if (var21 != null)
                    {
                        String[] var22 = (String[])entries.get("User");
                        String[] var23;
                        int var26;

                        if (var22 != null)
                        {
                            var23 = var22;
                            int var25 = var22.length;

                            for (var26 = 0; var26 < var25; ++var26)
                            {
                                String var27 = var23[var26];
                                UserFancy var28 = (UserFancy)var21.getFancies().get(var27);

                                if (var28 == null)
                                {
                                    var28 = new UserFancy(var21);
                                    var21.getFancies().put(var27, var28);
                                }

                                var28.add(new FancyPancy(var21, entries));
                            }
                        }
                        else
                        {
                            var23 = (String[])entries.get("Server");

                            if (var23 != null)
                            {
                                String[] var24 = var23;
                                var26 = var23.length;

                                for (i$1 = 0; i$1 < var26; ++i$1)
                                {
                                    String server = var24[i$1];
                                    ServerFancy serverFancy = (ServerFancy)var21.getServerFancies().get(server);

                                    if (serverFancy == null)
                                    {
                                        serverFancy = new ServerFancy();
                                        var21.getServerFancies().put(server, serverFancy);
                                    }

                                    serverFancy.add(new FancyPancy(var21, entries));
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Throwable var18)
        {
            var18.printStackTrace();
        }

        Iterator var19 = this.fancyTypes.values().iterator();

        while (var19.hasNext())
        {
            FancyPancyHandler var20 = (FancyPancyHandler)var19.next();
            var20.setReady(true);
        }
    }
}
