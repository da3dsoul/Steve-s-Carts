package vswe.stevescarts.Arcade;

import java.util.ArrayList;

public abstract class CardVillager extends Card
{
    public static ArrayList<CardVillager> cards = new ArrayList();

    public CardVillager(String message)
    {
        super(message);
    }

    public int getBackgroundV()
    {
        return 2;
    }

    static
    {
        cards.add(new CardVillager("No, I\'m a helicopter.")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece) {}
        });
    }
}
