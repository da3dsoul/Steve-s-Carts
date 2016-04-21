package vswe.stevescarts.Arcade;

import java.util.EnumSet;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Place
{
    protected ArcadeMonopoly game;

    public Place(ArcadeMonopoly game)
    {
        this.game = game;
    }

    protected int getTextureId()
    {
        return -1;
    }

    public void draw(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        int t;
        int u;
        int v;

        if (this.getTextureId() == -1)
        {
            t = 1;
            u = 0;
            v = 0;
        }
        else
        {
            t = 3 + this.getTextureId() / 6;
            u = this.getTextureId() % 3;
            v = this.getTextureId() % 6 / 3;
        }

        this.game.loadTexture(gui, t);
        this.applyColorFilter(gui, states);
        this.game.getModule().drawImage(gui, 0, 0, 76 * u, 122 * v, 76, 122);
    }

    public void applyColorFilter(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states)
    {
        if (states.contains(Place.PLACE_STATE.SELECTED))
        {
            if (states.contains(Place.PLACE_STATE.HOVER))
            {
                GL11.glColor4f(1.0F, 0.8F, 0.5F, 1.0F);
            }
            else
            {
                GL11.glColor4f(1.0F, 1.0F, 0.75F, 1.0F);
            }
        }
        else if (states.contains(Place.PLACE_STATE.MARKED))
        {
            if (states.contains(Place.PLACE_STATE.HOVER))
            {
                GL11.glColor4f(1.0F, 0.75F, 1.0F, 1.0F);
            }
            else
            {
                GL11.glColor4f(1.0F, 0.85F, 0.85F, 1.0F);
            }
        }
        else if (states.contains(Place.PLACE_STATE.HOVER))
        {
            GL11.glColor4f(0.9F, 0.9F, 1.0F, 1.0F);
        }
    }

    public void drawText(GuiMinecart gui, EnumSet<Place.PLACE_STATE> states) {}

    public void drawPiece(GuiMinecart gui, Piece piece, int total, int pos, int area, EnumSet<Place.PLACE_STATE> states)
    {
        boolean SIZE = true;
        boolean PADDING = true;
        boolean MARGIN = true;
        int allowedWidth = this.getAllowedWidth(area) - 10;
        int fullWidth = total * 26 - 2;
        int startX;
        int offSet;

        if (allowedWidth < fullWidth && total > 1)
        {
            startX = 5;
            offSet = (allowedWidth - 24) / (total - 1);
        }
        else
        {
            startX = 5 + (allowedWidth - fullWidth) / 2;
            offSet = 26;
        }

        this.game.getModule().drawImage(gui, startX + offSet * pos, this.getPieceYPosition(area), 232, piece.getV() * 24, 24, 24);
    }

    protected int getPieceYPosition(int area)
    {
        return 70;
    }

    protected int getAllowedWidth(int area)
    {
        return 76;
    }

    public void onPiecePass(Piece piece) {}

    public boolean onPieceStop(Piece piece)
    {
        return true;
    }

    public void onClick() {}

    public int getPieceAreaCount()
    {
        return 1;
    }

    public int getPieceAreaForPiece(Piece piece)
    {
        return 0;
    }

    public static enum PLACE_STATE
    {
        HOVER("HOVER", 0),
        SELECTED("SELECTED", 1),
        MARKED("MARKED", 2),
        ZOOMED("ZOOMED", 3);

        private static final Place.PLACE_STATE[] $VALUES = new Place.PLACE_STATE[]{HOVER, SELECTED, MARKED, ZOOMED};

        private PLACE_STATE(String var1, int var2) {}
    }
}
