package vswe.stevescarts.Arcade;

public abstract class CardPlace extends Place
{
    public CardPlace(ArcadeMonopoly game)
    {
        super(game);
    }

    public abstract Card getCard();

    public boolean onPieceStop(Piece piece)
    {
        return false;
    }
}
