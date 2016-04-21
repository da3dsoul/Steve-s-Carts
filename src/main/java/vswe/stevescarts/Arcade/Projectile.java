package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class Projectile extends Unit
{
    protected boolean playerProjectile;

    public Projectile(ArcadeInvaders game, int x, int y, boolean playerProjectile)
    {
        super(game, x, y);
        this.playerProjectile = playerProjectile;
    }

    public void draw(GuiMinecart gui)
    {
        if (this.playerProjectile)
        {
            this.game.getModule().drawImage(gui, this.x, this.y, 38, 0, 5, 16);
        }
        else
        {
            this.game.getModule().drawImage(gui, this.x, this.y, 32, 0, 6, 6);
        }
    }

    protected void hitCalculation() {}

    public Unit.UPDATE_RESULT update()
    {
        if (super.update() == Unit.UPDATE_RESULT.DEAD)
        {
            return Unit.UPDATE_RESULT.DEAD;
        }
        else
        {
            this.y += this.playerProjectile ? -5 : 5;

            if (this.y >= 0 && this.y <= 168)
            {
                return Unit.UPDATE_RESULT.DONE;
            }
            else
            {
                this.dead = true;
                return Unit.UPDATE_RESULT.DEAD;
            }
        }
    }

    protected int getHitboxWidth()
    {
        return this.playerProjectile ? 5 : 6;
    }

    protected int getHitboxHeight()
    {
        return this.playerProjectile ? 16 : 6;
    }
}
