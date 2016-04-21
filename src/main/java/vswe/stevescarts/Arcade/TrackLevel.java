package vswe.stevescarts.Arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import vswe.stevescarts.Helpers.Localization;

public class TrackLevel
{
    public static final TrackLevel editor = new TrackLevel(Localization.STORIES.THE_BEGINNING.MAP_EDITOR, 0, 0, TrackOrientation.DIRECTION.RIGHT, 26, 9);
    private static String MAP_FOLDER_PATH = "sc2/arcade/trackoperator/";
    private Localization.STORIES.THE_BEGINNING name;
    private int playerX;
    private int playerY;
    private TrackOrientation.DIRECTION playerDir;
    private int itemX;
    private int itemY;
    private ArrayList<Track> tracks;
    private ArrayList<LevelMessage> messages;

    private static byte getFileVersion()
    {
        return (byte)0;
    }

    @SideOnly(Side.CLIENT)
    public static ArrayList<TrackLevel> loadMapsFromFolder()
    {
        ArrayList maps = new ArrayList();

        try
        {
            File exception = new File(Minecraft.getMinecraft().mcDataDir, MAP_FOLDER_PATH);
            File[] children = exception.listFiles();

            if (children != null)
            {
                File[] arr$ = children;
                int len$ = children.length;

                for (int i$ = 0; i$ < len$; ++i$)
                {
                    File child = arr$[i$];

                    if (child.isFile())
                    {
                        String name = child.getName();
                        TrackLevel map = loadMap(name);

                        if (map != null)
                        {
                            maps.add(map);
                        }
                    }
                }
            }
        }
        catch (Exception var9)
        {
            System.out.println("Failed to load the maps");
        }

        return maps;
    }

    @SideOnly(Side.CLIENT)
    public static TrackLevel loadMap(String filename)
    {
        try
        {
            byte[] exception = readFromFile(new File(Minecraft.getMinecraft().mcDataDir, MAP_FOLDER_PATH + filename));
            return loadMapData(exception);
        }
        catch (Exception var2)
        {
            var2.printStackTrace();
            return null;
        }
    }

    public static TrackLevel loadMap(byte[] bytes)
    {
        try
        {
            return loadMapData(bytes);
        }
        catch (Exception var2)
        {
            var2.printStackTrace();
            return null;
        }
    }

    public static TrackLevel loadMapData(byte[] bytes) throws IOException
    {
        ByteArrayInputStream data = new ByteArrayInputStream(bytes);
        int version = data.read();
        int namelength = data.read();
        byte[] namebytes = new byte[namelength];
        data.read(namebytes, 0, namelength);
        new String(namebytes, Charset.forName("UTF-8"));
        int header = data.read() << 24 | data.read() << 16 | data.read() << 8 | data.read() << 0;
        int playerX = header & 31;
        int playerY = header >> 5 & 15;
        TrackOrientation.DIRECTION playerDir = TrackOrientation.DIRECTION.fromInteger(header >> 9 & 3);
        int itemX = header >> 11 & 31;
        int itemY = header >> 16 & 15;
        int tracksize = header >> 20 & 511;
        TrackLevel map = new TrackLevel((Localization.STORIES.THE_BEGINNING)null, playerX, playerY, playerDir, itemX, itemY);

        for (int i = 0; i < tracksize; ++i)
        {
            int trackdata = data.read() << 16 | data.read() << 8 | data.read() << 0;
            int trackX = trackdata & 31;
            int trackY = trackdata >> 5 & 15;
            int type = trackdata >> 9 & 7;
            TrackOrientation orientation = (TrackOrientation)TrackOrientation.ALL.get(trackdata >> 12 & 63);
            int extraLength = trackdata >> 18 & 63;
            Track track = TrackEditor.getRealTrack(trackX, trackY, type, orientation);
            byte[] extraData = new byte[extraLength];
            data.read(extraData);
            track.setExtraInfo(extraData);
            map.getTracks().add(track);
        }

        return map;
    }

    @SideOnly(Side.CLIENT)
    public static boolean saveMap(String name, int playerX, int playerY, TrackOrientation.DIRECTION playerDir, int itemX, int itemY, ArrayList<Track> tracks)
    {
        try
        {
            byte[] ex = saveMapData(name, playerX, playerY, playerDir, itemX, itemY, tracks);
            writeToFile(new File(Minecraft.getMinecraft().mcDataDir, "sc2/arcade/trackoperator/" + name.replace(" ", "_") + ".dat"), ex);
            return true;
        }
        catch (IOException var8)
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public static String saveMapToString(String name, int playerX, int playerY, TrackOrientation.DIRECTION playerDir, int itemX, int itemY, ArrayList<Track> tracks)
    {
        try
        {
            byte[] ex = saveMapData(name, playerX, playerY, playerDir, itemX, itemY, tracks);
            String str = "TrackLevel.loadMap(new byte[] {";

            for (int i = 0; i < ex.length; ++i)
            {
                if (i != 0)
                {
                    str = str + ",";
                }

                str = str + ex[i];
            }

            str = str + "});";
            return str;
        }
        catch (IOException var10)
        {
            return "";
        }
    }

    @SideOnly(Side.CLIENT)
    public static byte[] saveMapData(String name, int playerX, int playerY, TrackOrientation.DIRECTION playerDir, int itemX, int itemY, ArrayList<Track> tracks) throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);
        data.writeByte(getFileVersion());
        data.writeByte(name.length());
        data.writeBytes(name);
        byte header = 0;
        int header1 = header | playerX;
        header1 |= playerY << 5;
        header1 |= playerDir.toInteger() << 9;
        header1 |= itemX << 11;
        header1 |= itemY << 16;
        header1 |= tracks.size() << 20;
        data.writeInt(header1);
        Iterator i$ = tracks.iterator();

        while (i$.hasNext())
        {
            Track track = (Track)i$.next();
            byte trackdata = 0;
            byte[] extraData = track.getExtraInfo();
            int trackdata1 = trackdata | track.getX();
            trackdata1 |= track.getY() << 5;
            trackdata1 |= track.getU() << 9;
            trackdata1 |= track.getOrientation().toInteger() << 12;
            trackdata1 |= extraData.length << 18;
            data.write((trackdata1 & 16711680) >> 16);
            data.write((trackdata1 & 65280) >> 8);
            data.write(trackdata1 & 255);
            data.write(extraData);
        }

        return stream.toByteArray();
    }

    @SideOnly(Side.CLIENT)
    private static void writeToFile(File file, byte[] bytes) throws IOException
    {
        createFolder(file.getParentFile());
        FileOutputStream writer = new FileOutputStream(file);
        writer.write(bytes);
        writer.close();
    }

    @SideOnly(Side.CLIENT)
    private static byte[] readFromFile(File file) throws IOException
    {
        createFolder(file.getParentFile());
        FileInputStream reader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        reader.read(bytes);
        reader.close();
        return bytes;
    }

    @SideOnly(Side.CLIENT)
    private static void createFolder(File dir) throws IOException
    {
        if (dir != null)
        {
            File parent = dir.getParentFile();
            createFolder(parent);

            if (!dir.isDirectory())
            {
                dir.mkdirs();
            }
        }
    }

    public TrackLevel(Localization.STORIES.THE_BEGINNING name, int playerX, int playerY, TrackOrientation.DIRECTION playerDir, int itemX, int itemY)
    {
        this.name = name;
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerDir = playerDir;
        this.itemX = itemX;
        this.itemY = itemY;
        this.tracks = new ArrayList();
        this.messages = new ArrayList();
    }

    public String getName()
    {
        return this.name.translate(new String[0]);
    }

    public void setName(Localization.STORIES.THE_BEGINNING name)
    {
        this.name = name;
    }

    public int getPlayerStartX()
    {
        return this.playerX;
    }

    public int getPlayerStartY()
    {
        return this.playerY;
    }

    public TrackOrientation.DIRECTION getPlayerStartDirection()
    {
        return this.playerDir;
    }

    public int getItemX()
    {
        return this.itemX;
    }

    public int getItemY()
    {
        return this.itemY;
    }

    public ArrayList<Track> getTracks()
    {
        return this.tracks;
    }

    public ArrayList<LevelMessage> getMessages()
    {
        return this.messages;
    }

    public void addMessage(LevelMessage levelMessage)
    {
        this.messages.add(levelMessage);
    }
}
