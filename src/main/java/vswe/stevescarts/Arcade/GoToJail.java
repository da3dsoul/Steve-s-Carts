package vswe.stevescarts.Arcade;

public class GoToJail extends CornerPlace
{
    public GoToJail(ArcadeMonopoly game)
    {
        super(game, 3);
    }

    public boolean onPieceStop(Piece piece)
    {
        piece.goToJail();
        return super.onPieceStop(piece);
    }
}
