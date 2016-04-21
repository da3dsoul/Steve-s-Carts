package vswe.stevescarts.Arcade;

public class Jail extends CornerPlace
{
    public Jail(ArcadeMonopoly game)
    {
        super(game, 1);
    }

    protected int getPieceYPosition(int area)
    {
        return area == 1 ? 30 : 95;
    }

    protected int getAllowedWidth(int area)
    {
        return area == 1 ? 90 : 122;
    }

    public int getPieceAreaCount()
    {
        return 2;
    }

    public int getPieceAreaForPiece(Piece piece)
    {
        return piece.isInJail() ? 1 : 0;
    }
}
