package vswe.stevescarts.Arcade;

import java.util.Iterator;
import vswe.stevescarts.Interfaces.GuiMinecart;

public abstract class Unit
{
    protected int x;
    protected int y;
    protected ArcadeInvaders game;
    protected boolean dead;
    protected int health;

    public Unit(ArcadeInvaders game, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.game = game;
        this.health = 1;
    }

    public abstract void draw(GuiMinecart var1);

    public Unit.UPDATE_RESULT update()
    {
        if (!this.dead)
        {
            this.hitCalculation();
        }

        return this.dead ? Unit.UPDATE_RESULT.DEAD : Unit.UPDATE_RESULT.DONE;
    }

    protected void hitCalculation()
    {
        Iterator i$ = this.game.projectiles.iterator();

        while (i$.hasNext())
        {
            Projectile projectile = (Projectile)i$.next();

            if (!projectile.dead && (this.isObstacle() || projectile.playerProjectile != this.isPlayer()) && this.collidesWith(projectile))
            {
                --this.health;

                if (this.health == 0)
                {
                    this.dead = true;
                }

                projectile.dead = true;
            }
        }
    }

    protected boolean collidesWith(Unit unit)
    {
        return this.isUnitAinUnitB(this, unit) || this.isUnitAinUnitB(unit, this);
    }

    private boolean isUnitAinUnitB(Unit a, Unit b)
    {
        return (a.x >= b.x && a.x <= b.x + b.getHitboxWidth() || a.x + a.getHitboxWidth() >= b.x && a.x + a.getHitboxWidth() <= b.x + b.getHitboxWidth()) && (a.y >= b.y && a.y <= b.y + b.getHitboxHeight() || a.y + a.getHitboxHeight() >= b.y && a.y + a.getHitboxHeight() <= b.y + b.getHitboxHeight());
    }

    protected boolean isPlayer()
    {
        return false;
    }

    protected boolean isObstacle()
    {
        return false;
    }

    protected abstract int getHitboxWidth();

    protected abstract int getHitboxHeight();

    public static enum UPDATE_RESULT
    {
        DONE("DONE", 0),
        TURN_BACK("TURN_BACK", 1),
        DEAD("DEAD", 2),
        GAME_OVER("GAME_OVER", 3),
        TARGET("TARGET", 4);

        private static final Unit.UPDATE_RESULT[] $VALUES = new Unit.UPDATE_RESULT[]{DONE, TURN_BACK, DEAD, GAME_OVER, TARGET};

        private UPDATE_RESULT(String var1, int var2) {}
    }
}
