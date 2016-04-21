package vswe.stevescarts.Arcade;

import java.util.ArrayList;

public abstract class CardChance extends Card
{
    public static ArrayList<CardChance> cards = new ArrayList();

    public CardChance(String message)
    {
        super(message);
    }

    public int getBackgroundV()
    {
        return 0;
    }

    static
    {
        cards.add(new CardChance("Jaded managed to crash the server, again. The server had to roll back. Go 3 steps back.")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece)
            {
                game.movePiece(-3);
            }
        });
        cards.add(new CardChance("C2")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece)
            {
                piece.bankrupt((Piece)null);
            }
        });
        cards.add(new CardChance("You found a linking book in the middle of nowhere and foolishly you used it. You shouldn\'t have done that, now you\'re trapped in a void age.")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece)
            {
                piece.goToJail();
            }
        });
    }
}
