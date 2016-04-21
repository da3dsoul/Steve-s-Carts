package vswe.stevescarts.Arcade;

public class Chance extends CardPlace
{
    public Chance(ArcadeMonopoly game)
    {
        super(game);
    }

    protected int getTextureId()
    {
        return 0;
    }

    public Card getCard()
    {
        return (Card)CardChance.cards.get(this.game.getModule().getCart().rand.nextInt(CardChance.cards.size()));
    }
}
