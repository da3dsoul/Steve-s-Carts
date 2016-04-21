package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class Tile
{
    private int nearbyCreepers;
    private Tile.TILE_STATE state;
    private ArcadeSweeper game;

    public Tile(ArcadeSweeper game)
    {
        this.state = Tile.TILE_STATE.CLOSED;
        this.game = game;
    }

    public void setCreeper()
    {
        this.nearbyCreepers = 9;
    }

    public void setNearbyCreepers(int val)
    {
        this.nearbyCreepers = val;
    }

    public boolean isCreeper()
    {
        return this.nearbyCreepers == 9;
    }

    public void draw(ArcadeSweeper game, GuiMinecart gui, int x, int y, int mx, int my)
    {
        int[] rect = new int[] {x, y, 10, 10};

        if (this.isCreeper() && game.hasFinished)
        {
            game.getModule().drawImage(gui, rect, 30, 0);
        }
        else
        {
            int u = !this.isOpen() && (this.state != Tile.TILE_STATE.FLAGGED || this.isCreeper() || game.isPlaying || game.hasFinished) ? (game.getModule().inRect(mx, my, rect) ? 20 : 10) : 0;
            game.getModule().drawImage(gui, rect, u, 0);

            if (this.isOpen() && this.nearbyCreepers != 0)
            {
                game.getModule().drawImage(gui, x + 1, y + 1, (this.nearbyCreepers - 1) * 8, 11, 8, 8);
            }

            if (this.state == Tile.TILE_STATE.FLAGGED)
            {
                if (!game.isPlaying && !this.isCreeper())
                {
                    game.getModule().drawImage(gui, x + 1, y + 1, 16, 20, 8, 8);
                }
                else
                {
                    game.getModule().drawImage(gui, x + 1, y + 1, 0, 20, 8, 8);
                }
            }
            else if (this.state == Tile.TILE_STATE.MARKED)
            {
                game.getModule().drawImage(gui, x + 1, y + 1, 8, 20, 8, 8);
            }
        }
    }

    private boolean isOpen()
    {
        return this.isCreeper() && !this.game.isPlaying && !this.game.hasFinished || this.state == Tile.TILE_STATE.OPENED;
    }

    public Tile.TILE_OPEN_RESULT open()
    {
        if (this.state != Tile.TILE_STATE.OPENED && this.state != Tile.TILE_STATE.FLAGGED)
        {
            this.state = Tile.TILE_STATE.OPENED;

            if (this.nearbyCreepers == 0)
            {
                --this.game.emptyLeft;
                return Tile.TILE_OPEN_RESULT.BLOB;
            }
            else if (this.isCreeper())
            {
                return Tile.TILE_OPEN_RESULT.DEAD;
            }
            else
            {
                --this.game.emptyLeft;
                return Tile.TILE_OPEN_RESULT.OK;
            }
        }
        else
        {
            return Tile.TILE_OPEN_RESULT.FAILED;
        }
    }

    public void mark()
    {
        switch (Tile.NamelessClass1047627363.$SwitchMap$vswe$stevescarts$Arcade$Tile$TILE_STATE[this.state.ordinal()])
        {
            case 1:
                this.state = Tile.TILE_STATE.FLAGGED;
                --this.game.creepersLeft;
                break;

            case 2:
                this.state = Tile.TILE_STATE.MARKED;
                ++this.game.creepersLeft;
                break;

            case 3:
                this.state = Tile.TILE_STATE.CLOSED;
        }
    }

    public Tile.TILE_STATE getState()
    {
        return this.state;
    }

    public void setState(Tile.TILE_STATE state)
    {
        this.state = state;
    }

    public int getNearbyCreepers()
    {
        return this.nearbyCreepers;
    }

    static class NamelessClass1047627363
    {
        static final int[] $SwitchMap$vswe$stevescarts$Arcade$Tile$TILE_STATE = new int[Tile.TILE_STATE.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$Tile$TILE_STATE[Tile.TILE_STATE.CLOSED.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$Tile$TILE_STATE[Tile.TILE_STATE.FLAGGED.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$Tile$TILE_STATE[Tile.TILE_STATE.MARKED.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    public static enum TILE_OPEN_RESULT
    {
        OK("OK", 0),
        BLOB("BLOB", 1),
        FAILED("FAILED", 2),
        DEAD("DEAD", 3);

        private static final Tile.TILE_OPEN_RESULT[] $VALUES = new Tile.TILE_OPEN_RESULT[]{OK, BLOB, FAILED, DEAD};

        private TILE_OPEN_RESULT(String var1, int var2) {}
    }

    public static enum TILE_STATE
    {
        CLOSED("CLOSED", 0),
        OPENED("OPENED", 1),
        FLAGGED("FLAGGED", 2),
        MARKED("MARKED", 3);

        private static final Tile.TILE_STATE[] $VALUES = new Tile.TILE_STATE[]{CLOSED, OPENED, FLAGGED, MARKED};

        private TILE_STATE(String var1, int var2) {}
    }
}
