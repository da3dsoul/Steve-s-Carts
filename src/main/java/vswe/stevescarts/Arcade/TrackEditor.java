package vswe.stevescarts.Arcade;

public class TrackEditor extends Track
{
    private int type = 0;

    public TrackEditor(TrackOrientation orientation)
    {
        super(0, 0, orientation);
    }

    public Track copy()
    {
        TrackEditor newTrack = new TrackEditor(this.getOrientation());
        newTrack.type = this.type;
        return newTrack;
    }

    public Track getRealTrack(int x, int y)
    {
        return getRealTrack(x, y, this.type, this.getOrientation());
    }

    public static Track getRealTrack(int x, int y, int type, TrackOrientation orientation)
    {
        switch (type)
        {
            case 1:
                return new TrackDetector(x, y, orientation);

            case 2:
                return new TrackHeavy(x, y, orientation);

            default:
                return new Track(x, y, orientation);
        }
    }

    public int getU()
    {
        return this.type;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int val)
    {
        this.type = val;
    }

    public void nextType()
    {
        this.type = (this.type + 1) % 3;
    }
}
