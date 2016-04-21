package vswe.stevescarts.Arcade;

import java.util.ArrayList;
import java.util.Iterator;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class TrackDetector extends Track
{
    private ArrayList<TrackDetector.TrackCoordinate> targets = new ArrayList();

    public TrackDetector(int x, int y, TrackOrientation orientation)
    {
        super(x, y, orientation);
    }

    public Track copy()
    {
        TrackDetector newTrack = new TrackDetector(this.getX(), this.getY(), this.getOrientation());
        newTrack.targets = this.targets;
        return newTrack;
    }

    public TrackDetector addTarget(int x, int y)
    {
        if ((int)Math.ceil((double)((float)this.targets.size() * 1.125F)) == 63)
        {
            return this;
        }
        else
        {
            for (int i = 0; i < this.targets.size(); ++i)
            {
                if (((TrackDetector.TrackCoordinate)this.targets.get(i)).getX() == x && ((TrackDetector.TrackCoordinate)this.targets.get(i)).getY() == y)
                {
                    this.targets.remove(i);
                    return this;
                }
            }

            this.targets.add(new TrackDetector.TrackCoordinate(x, y));
            return this;
        }
    }

    public void setExtraInfo(byte[] data)
    {
        int startPosition = 0;
        short content = 0;

        for (int i = 0; i < data.length; ++i)
        {
            short val = (short)data[i];

            if (val < 0)
            {
                val = (short)(val + 256);
            }

            content = (short)(content | (val & (int)Math.pow(2.0D, (double)Math.min(8, 9 - startPosition)) - 1) << startPosition);

            if (startPosition == 0)
            {
                startPosition = 8;
            }
            else
            {
                this.addTarget(content & 31, (content & 480) >> 5);
                content = (short)((val & (int)Math.pow(2.0D, (double)(startPosition - 1)) - 1 << 9 - startPosition) >> 9 - startPosition);
                startPosition = (startPosition + 8) % 9;
            }
        }
    }

    public byte[] getExtraInfo()
    {
        byte[] ret = new byte[(int)Math.ceil((double)((float)this.targets.size() * 1.125F))];
        int currentByte = 0;
        int startPosition = 0;

        for (int i = 0; i < this.targets.size(); ++i)
        {
            short data = (short)((TrackDetector.TrackCoordinate)this.targets.get(i)).getX();
            data = (short)(data | ((TrackDetector.TrackCoordinate)this.targets.get(i)).getY() << 5);
            ret[currentByte] |= (byte)((data & (int)Math.pow(2.0D, (double)(8 - startPosition)) - 1) << startPosition);
            ++currentByte;
            ret[currentByte] = (byte)((data & (int)Math.pow(2.0D, (double)(1 + startPosition)) - 1 << 8 - startPosition) >> 8 - startPosition);
            startPosition = (startPosition + 1) % 8;

            if (startPosition == 0)
            {
                ++currentByte;
            }
        }

        return ret;
    }

    public int getU()
    {
        return 1;
    }

    public void travel(ArcadeTracks game, Cart cart)
    {
        Iterator i$ = this.targets.iterator();

        while (i$.hasNext())
        {
            TrackDetector.TrackCoordinate target = (TrackDetector.TrackCoordinate)i$.next();
            Track track = game.getTrackMap()[target.getX()][target.getY()];

            if (track != null)
            {
                track.flip();
            }
        }
    }

    public void drawOverlay(ModuleArcade module, GuiMinecart gui, int x, int y, boolean isRunning)
    {
        if (!isRunning && module.inRect(x, y, ArcadeTracks.getTrackArea(this.getX(), this.getY())))
        {
            Iterator i$ = this.targets.iterator();

            while (i$.hasNext())
            {
                TrackDetector.TrackCoordinate target = (TrackDetector.TrackCoordinate)i$.next();
                module.drawImage(gui, ArcadeTracks.getTrackArea(target.getX(), target.getY()), 0, 128);
            }
        }
    }

    public void onEditorClick(ArcadeTracks game)
    {
        game.setEditorDetectorTrack(this);
    }

    private static class TrackCoordinate
    {
        private int x;
        private int y;

        public TrackCoordinate(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public int getX()
        {
            return this.x;
        }

        public int getY()
        {
            return this.y;
        }
    }
}
