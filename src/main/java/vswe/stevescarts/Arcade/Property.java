package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import java.util.Iterator;
import vswe.stevescarts.Interfaces.GuiMinecart;

public abstract class Property extends Place
{
    private String name;
    private int cost;
    private Piece owner;
    private PropertyGroup group;
    private boolean mortgaged;

    public Property(ArcadeMonopoly game, PropertyGroup group, String name, int cost)
    {
        super(game);
        this.group = group;
        group.add(this);
        this.name = name;
        this.cost = cost;
    }

    public void drawValue(GuiMinecart gui)
    {
        Note.drawValue(this.game, gui, 10, 103, 2, this.cost);
    }

    public void drawText(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        this.game.getModule().drawSplitString(gui, this.name, 3 + gui.getGuiLeft(), this.getTextY() + gui.getGuiTop(), 70, true, 4210752);
    }

    protected abstract int getTextY();

    public int getCost()
    {
        return this.cost;
    }

    public void setOwner(Piece val)
    {
        this.owner = val;
    }

    public Piece getOwner()
    {
        return this.owner;
    }

    public boolean hasOwner()
    {
        return this.owner != null;
    }

    public boolean onPieceStop(Piece piece)
    {
        return this.owner == null || this.owner == piece || this.mortgaged;
    }

    public PropertyGroup getGroup()
    {
        return this.group;
    }

    public abstract int getRentCost();

    public int getMortgageValue()
    {
        return this.getCost() / 2;
    }

    public int getOwnedInGroup()
    {
        int owned = 0;
        Iterator i$ = this.getGroup().getProperties().iterator();

        while (i$.hasNext())
        {
            Property property = (Property)i$.next();

            if (property.getOwner() == this.getOwner() && !property.isMortgaged())
            {
                ++owned;
            }
        }

        return owned;
    }

    public boolean isMortgaged()
    {
        return this.mortgaged;
    }

    public boolean canMortgage()
    {
        return true;
    }

    public void mortgage()
    {
        this.mortgaged = true;
    }

    public int getUnMortgagePrice()
    {
        return (int)((float)this.getMortgageValue() * 1.1F);
    }

    public void unMortgage()
    {
        this.mortgaged = false;
    }
}
