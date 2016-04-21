package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class CornerPlace extends Place
{
    private int texture;

    public CornerPlace(ArcadeMonopoly game, int texture)
    {
        super(game);
        this.texture = texture;
    }

    public void draw(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        this.game.loadTexture(gui, 2);
        this.applyColorFilter(gui, states);
        this.game.getModule().drawImage(gui, 0, 0, 122 * (this.texture % 2), 122 * (this.texture / 2), 122, 122);
    }
}
