package vswe.stevescarts.Arcade;

import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class TetrisPiece
{
    private TetrisPiecePart[] parts;
    private int x;
    private int y;
    private String sound;
    private float volume;
    private int rotationOffset;

    private TetrisPiece(TetrisPiecePart[] parts)
    {
        this.parts = parts;
        this.x = 5;
        this.y = -2;
    }

    public static TetrisPiece createPiece(int type)
    {
        String sound = null;
        float volume = 0.5F;
        byte rotationOffset = 0;
        TetrisPiecePart[] parts;

        switch (type)
        {
            case 0:
                parts = createEndermanParts();
                sound = "mob.endermen.hit";
                break;

            case 1:
                parts = createSlimeParts();
                sound = "mob.slime.big";
                rotationOffset = 1;
                break;

            case 2:
                parts = createWitherParts();
                sound = "mob.wither.hurt";
                volume = 0.25F;
                break;

            case 3:
                parts = createWitchParts();
                sound = "mob.cat.hitt";
                break;

            case 4:
                parts = createPigParts();
                sound = "mob.pig.say";
                break;

            case 5:
                parts = createSteveParts();
                sound = "damage.hit";
                break;

            case 6:
                parts = createSheepParts();
                sound = "mob.sheep.say";
                break;

            default:
                return null;
        }

        TetrisPiece piece = new TetrisPiece(parts);
        piece.sound = sound;
        piece.rotationOffset = rotationOffset;
        piece.volume = volume;
        return piece;
    }

    private static TetrisPiecePart[] createEndermanParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(0, 0), 0, -1), new TetrisPiecePart(new TetrisBlock(0, 10), 0, 0), new TetrisPiecePart(new TetrisBlock(0, 20), 0, 1), new TetrisPiecePart(new TetrisBlock(0, 30), 0, 2)};
        return parts;
    }

    private static TetrisPiecePart[] createSlimeParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(10, 0), 0, 0), new TetrisPiecePart(new TetrisBlock(20, 0), 1, 0), new TetrisPiecePart(new TetrisBlock(10, 10), 0, 1), new TetrisPiecePart(new TetrisBlock(20, 10), 1, 1)};
        return parts;
    }

    private static TetrisPiecePart[] createWitherParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(30, 0), -1, 0), new TetrisPiecePart(new TetrisBlock(40, 0), 0, 0), new TetrisPiecePart(new TetrisBlock(50, 0), 1, 0), new TetrisPiecePart(new TetrisBlock(40, 10), 0, 1)};
        return parts;
    }

    private static TetrisPiecePart[] createWitchParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(70, 0), 0, -1), new TetrisPiecePart(new TetrisBlock(70, 10), 0, 0), new TetrisPiecePart(new TetrisBlock(70, 20), 0, 1), new TetrisPiecePart(new TetrisBlock(60, 20), -1, 1)};
        return parts;
    }

    private static TetrisPiecePart[] createPigParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(80, 0), 0, -1), new TetrisPiecePart(new TetrisBlock(80, 10), 0, 0), new TetrisPiecePart(new TetrisBlock(80, 20), 0, 1), new TetrisPiecePart(new TetrisBlock(90, 20), 1, 1)};
        return parts;
    }

    private static TetrisPiecePart[] createSteveParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(100, 0), -1, -1), new TetrisPiecePart(new TetrisBlock(110, 0), 0, -1), new TetrisPiecePart(new TetrisBlock(110, 10), 0, 0), new TetrisPiecePart(new TetrisBlock(120, 10), 1, 0)};
        return parts;
    }

    private static TetrisPiecePart[] createSheepParts()
    {
        TetrisPiecePart[] parts = new TetrisPiecePart[] {new TetrisPiecePart(new TetrisBlock(130, 10), -1, 1), new TetrisPiecePart(new TetrisBlock(140, 10), 0, 1), new TetrisPiecePart(new TetrisBlock(140, 0), 0, 0), new TetrisPiecePart(new TetrisBlock(150, 0), 1, 0)};
        return parts;
    }

    public void render(ArcadeTetris game, GuiMinecart gui)
    {
        for (int i = 0; i < this.parts.length; ++i)
        {
            this.parts[i].render(game, gui, this.x, this.y);
        }
    }

    public void rotate(TetrisBlock[][] board)
    {
        int i;

        for (i = 0; i < this.parts.length; ++i)
        {
            if (!this.parts[i].canRotate(board, this.x, this.y, this.rotationOffset))
            {
                return;
            }
        }

        for (i = 0; i < this.parts.length; ++i)
        {
            this.parts[i].rotate(this.rotationOffset);
        }
    }

    public TetrisPiece.MOVE_RESULT move(ArcadeTetris game, TetrisBlock[][] board, int offX, int offY, boolean placeOnFail)
    {
        for (int i = 0; i < this.parts.length; ++i)
        {
            if (!this.parts[i].canMoveTo(board, this.x + offX, this.y + offY))
            {
                boolean isGameOver = false;

                if (placeOnFail)
                {
                    for (int j = 0; j < this.parts.length; ++j)
                    {
                        if (this.parts[j].canPlaceInBoard(this.y))
                        {
                            this.parts[j].placeInBoard(board, this.x, this.y);
                        }
                        else
                        {
                            isGameOver = true;
                        }
                    }

                    if (StevesCarts.instance.useArcadeMobSounds)
                    {
                        if (this.sound != null)
                        {
                            ArcadeGame.playDefaultSound(this.sound, this.volume, (game.getModule().getCart().rand.nextFloat() - game.getModule().getCart().rand.nextFloat()) * 0.2F + 1.0F);
                        }
                    }
                    else
                    {
                        ArcadeGame.playSound("boop", 1.0F, 1.0F);
                    }
                }

                return isGameOver ? TetrisPiece.MOVE_RESULT.GAME_OVER : TetrisPiece.MOVE_RESULT.FAIL;
            }
        }

        this.x += offX;
        this.y += offY;
        return TetrisPiece.MOVE_RESULT.SUCCESS;
    }

    public static enum MOVE_RESULT
    {
        SUCCESS("SUCCESS", 0),
        FAIL("FAIL", 1),
        GAME_OVER("GAME_OVER", 2);

        private static final TetrisPiece.MOVE_RESULT[] $VALUES = new TetrisPiece.MOVE_RESULT[]{SUCCESS, FAIL, GAME_OVER};

        private MOVE_RESULT(String var1, int var2) {}
    }
}
