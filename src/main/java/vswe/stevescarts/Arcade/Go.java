package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Go extends CornerPlace
{
    public Go(ArcadeMonopoly game)
    {
        super(game, 0);
    }

    public void draw(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        super.draw(gui, states);
        Note.DIAMOND.draw(this.game, gui, 45, 5, 2);
    }

    public void drawText(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        this.game.getModule().drawString(gui, "Collect", 5, 10, 4210752);
        this.game.getModule().drawString(gui, "as you pass.", 5, 20, 4210752);
    }

    public void onPiecePass(Piece piece)
    {
        piece.addMoney(Note.DIAMOND, 2, true);
    }
}
