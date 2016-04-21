package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class Cart
{
    private int x;
    private int y;
    private TrackOrientation.DIRECTION dir;
    private int imageIndex;
    private boolean enabled;

    public Cart(int imageIndex)
    {
        this.imageIndex = imageIndex;
        this.enabled = true;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public TrackOrientation.DIRECTION getDireciotn()
    {
        return this.dir;
    }

    public void setX(int val)
    {
        this.x = val;
    }

    public void setY(int val)
    {
        this.y = val;
    }

    public void setDirection(TrackOrientation.DIRECTION val)
    {
        this.dir = val;
    }

    public void setAlive(boolean val)
    {
        this.enabled = val;
    }

    public void move(ArcadeTracks game)
    {
        if (this.enabled)
        {
            this.x += this.dir.getX();
            this.y += this.dir.getY();

            if (this.x >= 0 && this.y >= 0 && this.x < game.getTrackMap().length && this.y < game.getTrackMap()[0].length && game.getTrackMap()[this.x][this.y] != null)
            {
                game.getTrackMap()[this.x][this.y].travel(game, this);
                this.dir = game.getTrackMap()[this.x][this.y].getOrientation().travel(this.dir.getOpposite());
            }
            else
            {
                if (this.dir != TrackOrientation.DIRECTION.STILL)
                {
                    this.onCrash();
                }

                this.dir = TrackOrientation.DIRECTION.STILL;
            }

            if (game.isItemOnGround() && this.x == game.getItemX() && this.y == game.getItemY())
            {
                this.onItemPickUp();
                game.pickItemUp();
            }
        }
    }

    public void onItemPickUp() {}

    public void onCrash() {}

    public void render(ArcadeTracks game, GuiMinecart gui, int tick)
    {
        if (this.enabled)
        {
            int x = 7 + (int)(16.0F * ((float)this.x + (float)this.dir.getX() * ((float)tick / 4.0F)));
            int y = 7 + (int)(16.0F * ((float)this.y + (float)this.dir.getY() * ((float)tick / 4.0F)));
            int u = 256 - 12 * (this.imageIndex + 1);
            short v = 244;
            byte w = 12;
            byte h = 12;
            game.drawImageInArea(gui, x, y, u, v, w, h);
        }
    }

    public boolean isAlive()
    {
        return this.enabled;
    }
}
