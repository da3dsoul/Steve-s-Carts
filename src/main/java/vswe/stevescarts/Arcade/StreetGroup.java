package vswe.stevescarts.Arcade;

public class StreetGroup extends PropertyGroup
{
    private float[] color;
    private int houseCost;

    public StreetGroup(int houseCost, int[] color)
    {
        this.houseCost = houseCost;
        this.color = new float[] {(float)color[0] / 256.0F, (float)color[1] / 256.0F, (float)color[2] / 256.0F};
    }

    public float[] getColor()
    {
        return this.color;
    }

    public int getStructureCost()
    {
        return this.houseCost;
    }
}
