package vswe.stevescarts.Arcade;

import java.util.ArrayList;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class Track
{
    private int x;
    private int y;
    private int v;
    private GuiBase.RENDER_ROTATION rotation;
    private TrackOrientation orientation;
    private TrackOrientation orientationBackup;

    public Track(int x, int y, TrackOrientation orientation)
    {
        this.x = x;
        this.y = y;
        this.setOrientation(orientation);
    }

    private void setV(int v)
    {
        this.v = v;
    }

    private void setRotation(GuiBase.RENDER_ROTATION rotation)
    {
        this.rotation = rotation;
    }

    public void setOrientation(TrackOrientation orientation)
    {
        this.orientation = orientation;
        this.setV(orientation.getV());
        this.setRotation(orientation.getRotation());
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getU()
    {
        return 0;
    }

    public int getV()
    {
        return this.v;
    }

    public GuiBase.RENDER_ROTATION getRotation()
    {
        return this.rotation;
    }

    public TrackOrientation getOrientation()
    {
        return this.orientation;
    }

    public void onClick(ArcadeTracks game)
    {
        this.flip();
    }

    public void onEditorClick(ArcadeTracks game)
    {
        if (this.orientation.getOpposite() != null && game.getEditorDetectorTrack() != null)
        {
            game.getEditorDetectorTrack().addTarget(this.getX(), this.getY());
        }
    }

    public void flip()
    {
        if (this.orientation.getOpposite() != null)
        {
            ArcadeGame.playSound("gearswitch", 1.0F, 1.0F);
            this.setOrientation(this.orientation.getOpposite());
        }
    }

    public void saveBackup()
    {
        this.orientationBackup = this.orientation;
    }

    public void loadBackup()
    {
        this.setOrientation(this.orientationBackup);
    }

    public Track copy()
    {
        return new Track(this.x, this.y, this.orientation);
    }

    public void travel(ArcadeTracks game, Cart cart) {}

    public void drawOverlay(ModuleArcade module, GuiMinecart gui, int x, int y, boolean isRunning) {}

    public static void addTrack(ArrayList<Track> tracks, int x1, int y1, int x2, int y2)
    {
        if (x1 != x2 && y1 != y2)
        {
            TrackOrientation corner = getCorner(x1 >= x2, y1 < y2);
            int x2h;

            if (x1 < x2)
            {
                x2h = x2 - 1;
            }
            else
            {
                x2h = x2 + 1;
            }

            int y1v;

            if (y1 < y2)
            {
                y1v = y1 + 1;
            }
            else
            {
                y1v = y1 - 1;
            }

            addHorizontalTrack(tracks, x1, x2h, y1);
            tracks.add(new Track(x2, y1, corner));
            addVerticalTrack(tracks, x2, y1v, y2);
        }
        else if (x1 != x2)
        {
            addHorizontalTrack(tracks, x1, x2, y1);
        }
        else
        {
            addVerticalTrack(tracks, x1, y1, y2);
        }
    }

    private static TrackOrientation getCorner(boolean right, boolean down)
    {
        return right ? (down ? TrackOrientation.CORNER_DOWN_RIGHT : TrackOrientation.CORNER_UP_RIGHT) : (down ? TrackOrientation.CORNER_DOWN_LEFT : TrackOrientation.CORNER_UP_LEFT);
    }

    private static void addHorizontalTrack(ArrayList<Track> tracks, int x1, int x2, int y)
    {
        if (x1 > x2)
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        for (int x = x1; x <= x2; ++x)
        {
            tracks.add(new Track(x, y, TrackOrientation.STRAIGHT_HORIZONTAL));
        }
    }

    private static void addVerticalTrack(ArrayList<Track> tracks, int x, int y1, int y2)
    {
        if (y1 > y2)
        {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }

        for (int y = y1; y <= y2; ++y)
        {
            tracks.add(new Track(x, y, TrackOrientation.STRAIGHT_VERTICAL));
        }
    }

    public void setExtraInfo(byte[] data) {}

    public byte[] getExtraInfo()
    {
        return new byte[0];
    }
}
