package vswe.stevescarts.Arcade;

public class Villager extends CardPlace
{
    public Villager(ArcadeMonopoly game)
    {
        super(game);
    }

    protected int getTextureId()
    {
        return 9;
    }

    public Card getCard()
    {
        return (Card)CardVillager.cards.get(this.game.getModule().getCart().rand.nextInt(CardVillager.cards.size()));
    }
}
