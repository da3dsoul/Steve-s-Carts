package vswe.stevescarts.Arcade;

public class Utility extends Property
{
    private int utilId;

    public Utility(ArcadeMonopoly game, PropertyGroup group, int utilId, String name)
    {
        super(game, group, name, 150);
        this.utilId = utilId;
    }

    protected int getTextureId()
    {
        return 6 + this.utilId;
    }

    protected int getTextY()
    {
        return 10;
    }

    public int getRentCost()
    {
        return this.getRentCost(this.getOwnedInGroup());
    }

    public int getId()
    {
        return this.utilId;
    }

    public int getRentCost(int owned)
    {
        return this.game.getTotalDieEyes() * getMultiplier(owned);
    }

    public static int getMultiplier(int i)
    {
        switch (i)
        {
            case 1:
                return 6;

            case 2:
                return 15;

            case 3:
                return 50;

            default:
                return 0;
        }
    }
}
