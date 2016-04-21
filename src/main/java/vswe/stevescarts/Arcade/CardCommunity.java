package vswe.stevescarts.Arcade;

import java.util.ArrayList;

public abstract class CardCommunity extends Card
{
    public static ArrayList<CardCommunity> cards = new ArrayList();

    public CardCommunity(String message)
    {
        super(message);
    }

    public int getBackgroundV()
    {
        return 1;
    }

    static
    {
        cards.add(new CardCommunity("You just found a ton of buckets in the dungeon.")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece)
            {
                piece.addMoney(Note.IRON, 9, true);
            }
            public int getNoteCount()
            {
                return 9;
            }
            public Note getNote()
            {
                return Note.IRON;
            }
            public String getMoneyPrefix()
            {
                return "Collect";
            }
        });
        cards.add(new CardCommunity("D2")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece) {}
        });
        cards.add(new CardCommunity("D3")
        {
            public void doStuff(ArcadeMonopoly game, Piece piece) {}
        });
    }
}
