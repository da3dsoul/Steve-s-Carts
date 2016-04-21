package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class Player extends Unit
{
    protected boolean ready;
    private int targetX;
    private int targetY;

    public Player(ArcadeInvaders game, int x, int y)
    {
        super(game, x, y);
    }

    public Player(ArcadeInvaders game)
    {
        this(game, 200, 150);
        this.ready = true;
    }

    public void draw(GuiMinecart gui)
    {
        if (!this.ready && this.targetY != this.y)
        {
            this.game.drawImageInArea(gui, this.x, this.y, 16, 16, 16, 16, 3, 0, 1000, 1000);
        }
        else
        {
            this.game.drawImageInArea(gui, this.x, this.y, 16, 16, 16, 16);
        }
    }

    protected void setTarget(int x, int y)
    {
        this.targetX = x;
        this.targetY = y;
    }

    public Unit.UPDATE_RESULT update()
    {
        if (!this.ready)
        {
            if (this.targetY == this.y && this.targetX == this.x)
            {
                this.ready = true;
            }
            else if (this.targetY == this.y)
            {
                this.x = Math.min(this.targetX, this.x + 8);
            }
            else if (this.x == -15)
            {
                this.y = Math.max(this.targetY, this.y - 8);
            }
            else
            {
                this.x = Math.max(-15, this.x - 8);
            }
        }
        else if (super.update() == Unit.UPDATE_RESULT.DEAD)
        {
            return Unit.UPDATE_RESULT.DEAD;
        }

        return Unit.UPDATE_RESULT.DONE;
    }

    public void move(int dir)
    {
        this.x += dir * 5;

        if (this.x < 10)
        {
            this.x = 10;
        }
        else if (this.x > 417)
        {
            this.x = 417;
        }
    }

    protected boolean isPlayer()
    {
        return true;
    }

    protected int getHitboxWidth()
    {
        return 16;
    }

    protected int getHitboxHeight()
    {
        return 16;
    }
}
