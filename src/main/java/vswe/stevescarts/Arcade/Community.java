package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Community extends CardPlace
{
    public Community(ArcadeMonopoly game)
    {
        super(game);
    }

    protected int getTextureId()
    {
        return 5;
    }

    public void drawText(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        this.game.getModule().drawSplitString(gui, "Dungeon Chest", 3 + gui.getGuiLeft(), 10 + gui.getGuiTop(), 70, true, 4210752);
    }

    public Card getCard()
    {
        return (Card)CardCommunity.cards.get(this.game.getModule().getCart().rand.nextInt(CardCommunity.cards.size()));
    }
}
