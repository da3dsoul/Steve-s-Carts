package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class TetrisBlock
{
    private int u;
    private int v;
    private GuiBase.RENDER_ROTATION r;

    public TetrisBlock(int u, int v)
    {
        this.u = u;
        this.v = v;
        this.r = GuiBase.RENDER_ROTATION.NORMAL;
    }

    public void render(ArcadeTetris game, GuiMinecart gui, int x, int y)
    {
        if (y >= 0)
        {
            game.getModule().drawImage(gui, 189 + x * 10, 9 + y * 10, this.u, this.v, 10, 10, this.r);
        }
    }

    public void rotate()
    {
        this.r = this.r.getNextRotation();
    }
}
