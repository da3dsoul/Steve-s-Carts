package vswe.stevescarts.Arcade;

public class TrackHeavy extends Track
{
    public TrackHeavy(int x, int y, TrackOrientation orientation)
    {
        super(x, y, orientation);
    }

    public void onClick(ArcadeTracks game) {}

    public Track copy()
    {
        return new TrackHeavy(this.getX(), this.getY(), this.getOrientation());
    }

    public int getU()
    {
        return 2;
    }
}
