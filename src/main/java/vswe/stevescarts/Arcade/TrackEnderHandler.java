package vswe.stevescarts.Arcade;

public class TrackEnderHandler extends Track
{
    private boolean isSpawner;

    public TrackEnderHandler(int x, int y, TrackOrientation orientation, boolean isSpawner)
    {
        super(x, y, orientation);
        this.isSpawner = isSpawner;
    }

    public void travel(ArcadeTracks game, Cart cart)
    {
        if (this.isSpawner)
        {
            game.getEnderman().setAlive(true);
            game.getEnderman().setDirection(TrackOrientation.DIRECTION.RIGHT);
            game.getEnderman().setX(cart.getX() + 5);
            game.getEnderman().setY(cart.getY());
        }
        else if (game.getEnderman().isAlive())
        {
            game.getEnderman().setAlive(false);
        }

        ArcadeGame.playDefaultSound("mob.endermen.portal", 1.0F, 1.0F);
    }

    public Track copy()
    {
        return new TrackEnderHandler(this.getX(), this.getY(), this.getOrientation(), this.isSpawner);
    }
}
