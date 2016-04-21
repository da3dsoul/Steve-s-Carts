package vswe.stevescarts.Arcade;

import java.util.Iterator;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Building extends Unit
{
    public Building(ArcadeInvaders game, int x, int y)
    {
        super(game, x, y);
        this.health = 10;
    }

    public void draw(GuiMinecart gui)
    {
        this.game.getModule().drawImage(gui, this.x, this.y, 32 + (10 - this.health) * 16, 16, 16, 16);
    }

    protected int getHitboxWidth()
    {
        return 16;
    }

    protected int getHitboxHeight()
    {
        return 16;
    }

    protected boolean isObstacle()
    {
        return true;
    }

    public Unit.UPDATE_RESULT update()
    {
        if (super.update() == Unit.UPDATE_RESULT.DEAD)
        {
            return Unit.UPDATE_RESULT.DEAD;
        }
        else
        {
            Iterator i$ = this.game.invaders.iterator();
            Unit invader;

            do
            {
                if (!i$.hasNext())
                {
                    return Unit.UPDATE_RESULT.DONE;
                }

                invader = (Unit)i$.next();
            }
            while (invader.dead || !this.collidesWith(invader));

            this.dead = true;
            this.health = 0;
            return Unit.UPDATE_RESULT.DEAD;
        }
    }
}
