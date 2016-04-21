package vswe.stevescarts.Helpers;

public class TitleBox
{
    private int id;
    private int x;
    private int y;
    private int color;

    public TitleBox(int id, int y, int color)
    {
        this(id, 16, y, color);
    }

    public TitleBox(int id, int x, int y, int color)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getID()
    {
        return this.id;
    }

    public int getColor()
    {
        return this.color;
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
