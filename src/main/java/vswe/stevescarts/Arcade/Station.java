package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Station extends Property
{
    private String name;
    private int stationId;

    public Station(ArcadeMonopoly game, PropertyGroup group, int stationId, String name)
    {
        super(game, group, name, 200);
        this.stationId = stationId;
        this.name = name;
    }

    protected int getTextureId()
    {
        return 1 + this.stationId;
    }

    public void draw(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        super.draw(gui, states);
        this.drawValue(gui);
    }

    protected int getTextY()
    {
        return 10;
    }

    public int getRentCost(int ownedStations)
    {
        return 25 * (int)Math.pow(2.0D, (double)(ownedStations - 1));
    }

    public int getRentCost()
    {
        return this.getRentCost(this.getOwnedInGroup());
    }
}
