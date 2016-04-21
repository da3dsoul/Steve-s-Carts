package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Street extends Property
{
    private float[] color;
    private int structures;
    private int baseRent;

    public Street(ArcadeMonopoly game, StreetGroup group, String name, int cost, int baseRent)
    {
        super(game, group, name, cost);
        this.color = group.getColor();
        this.baseRent = baseRent;
    }

    public void draw(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        super.draw(gui, states);
        GL11.glColor4f(this.color[0], this.color[1], this.color[2], 1.0F);
        this.game.getModule().drawImage(gui, 0, 0, 76, 0, 76, 22);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.structures > 0 && this.structures < 5)
        {
            for (int i = 0; i < this.structures; ++i)
            {
                this.game.getModule().drawImage(gui, 3 + i * 18, 3, 76, 22, 16, 16);
            }
        }
        else if (this.structures == 5)
        {
            this.game.getModule().drawImage(gui, 3, 3, 92, 22, 16, 16);
        }

        this.drawValue(gui);
    }

    public void increaseStructure()
    {
        ++this.structures;
    }

    protected int getTextY()
    {
        return 30;
    }

    public int getRentCost(int structureCount)
    {
        switch (structureCount)
        {
            case 1:
                return this.baseRent * 5;

            case 2:
                return this.baseRent * 15;

            case 3:
                return this.baseRent * 40;

            case 4:
                return this.baseRent * 70;

            case 5:
                return this.baseRent * 100;

            default:
                return this.baseRent;
        }
    }

    public int getRentCost(boolean ownsAll)
    {
        return ownsAll ? this.baseRent * 2 : this.baseRent;
    }

    public int getRentCost()
    {
        return this.structures == 0 ? this.getRentCost(this.ownsAllInGroup(this.getOwner())) : this.getRentCost(this.structures);
    }

    public int getStructureCount()
    {
        return this.structures;
    }

    public int getStructureCost()
    {
        return ((StreetGroup)this.getGroup()).getStructureCost();
    }

    public boolean ownsAllInGroup(Piece currentPiece)
    {
        Iterator i$ = this.getGroup().getProperties().iterator();
        Property property;

        do
        {
            if (!i$.hasNext())
            {
                return true;
            }

            property = (Property)i$.next();
        }
        while (property.getOwner() == currentPiece && !property.isMortgaged());

        return false;
    }

    public boolean canMortgage()
    {
        return super.canMortgage() && this.structures == 0;
    }

    public int getStructureSellPrice()
    {
        return this.getStructureCost() / 2;
    }

    public void decreaseStructures()
    {
        --this.structures;
    }
}
