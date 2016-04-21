package vswe.stevescarts.Arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class ArcadeTetris extends ArcadeGame
{
    private TetrisBlock[][] board;
    private TetrisPiece piece;
    private static String[] removalSounds = new String[] {"1lines", "2lines", "3lines", "4lines"};
    private int ticks = 0;
    private boolean isPlaying = true;
    private boolean quickMove = false;
    private int gameOverTicks;
    private int highscore;
    private int score;
    private int removed;
    private int[] removedByAmount;
    private int delay = 10;
    private int piecesSinceDelayChange;
    private boolean newHighScore;
    public static final int BOARD_START_X = 189;
    public static final int BOARD_START_Y = 9;
    private static String texture = "/gui/tetris.png";

    public ArcadeTetris(ModuleArcade module)
    {
        super(module, Localization.ARCADE.STACKER);
        this.newgame();
    }

    private void newgame()
    {
        this.board = new TetrisBlock[10][15];
        this.generatePiece();
        this.isPlaying = true;
        this.ticks = 0;
        this.quickMove = false;
        this.score = 0;
        this.removed = 0;
        this.removedByAmount = new int[4];
        this.delay = 10;
        this.piecesSinceDelayChange = 0;
        this.newHighScore = false;
    }

    private void generatePiece()
    {
        this.piece = TetrisPiece.createPiece(this.getModule().getCart().rand.nextInt(7));
    }

    @SideOnly(Side.CLIENT)
    public void update()
    {
        super.update();

        if (this.isPlaying)
        {
            if (this.ticks != 0 && !this.quickMove)
            {
                --this.ticks;
            }
            else
            {
                if (this.piece != null)
                {
                    TetrisPiece.MOVE_RESULT result = this.piece.move(this, this.board, 0, 1, true);

                    if (result == TetrisPiece.MOVE_RESULT.FAIL)
                    {
                        this.piece = null;
                        int removedCount = 0;

                        for (int y = 0; y < this.board[0].length; ++y)
                        {
                            boolean valid = true;
                            int y2;

                            for (y2 = 0; y2 < this.board.length; ++y2)
                            {
                                if (this.board[y2][y] == null)
                                {
                                    valid = false;
                                    break;
                                }
                            }

                            if (valid)
                            {
                                for (y2 = y; y2 >= 0; --y2)
                                {
                                    for (int x = 0; x < this.board.length; ++x)
                                    {
                                        TetrisBlock value = y2 == 0 ? null : this.board[x][y2 - 1];
                                        this.board[x][y2] = value;
                                    }
                                }

                                ++removedCount;
                            }
                        }

                        if (removedCount > 0)
                        {
                            this.removed += removedCount;
                            ++this.removedByAmount[removedCount - 1];
                            this.score += removedCount * removedCount * 100;
                            playSound(removalSounds[removedCount - 1], 1.0F, 1.0F);
                        }

                        this.quickMove = false;
                        ++this.piecesSinceDelayChange;

                        if (this.piecesSinceDelayChange == 8)
                        {
                            this.piecesSinceDelayChange = 0;

                            if (this.delay > 0)
                            {
                                --this.delay;
                            }
                        }
                    }
                    else if (result == TetrisPiece.MOVE_RESULT.GAME_OVER)
                    {
                        this.piece = null;
                        this.isPlaying = false;
                        this.quickMove = false;
                        this.gameOverTicks = 0;
                        this.newHighScore();
                        playSound("gameover", 1.0F, 1.0F);
                    }
                }
                else
                {
                    this.generatePiece();
                }

                this.ticks = this.delay;
            }
        }
        else if (this.gameOverTicks < 170)
        {
            this.gameOverTicks = Math.min(170, this.gameOverTicks + 5);
        }
        else if (this.newHighScore)
        {
            playSound("highscore", 1.0F, 1.0F);
            this.newHighScore = false;
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource(texture);
        this.getModule().drawImage(gui, 187, 7, 0, 40, 104, 154);
        int graphicalValue;

        for (graphicalValue = 0; graphicalValue < this.board.length; ++graphicalValue)
        {
            for (int j = 0; j < this.board[0].length; ++j)
            {
                TetrisBlock b = this.board[graphicalValue][j];

                if (b != null)
                {
                    b.render(this, gui, graphicalValue, j);
                }
            }
        }

        if (this.piece != null)
        {
            this.piece.render(this, gui);
        }

        if (!this.isPlaying)
        {
            graphicalValue = Math.min(this.gameOverTicks, 150);
            this.getModule().drawImage(gui, 189, 159 - graphicalValue, 104, 40, 100, graphicalValue);

            if (graphicalValue == 150 && this.getModule().inRect(x, y, new int[] {189, 9, 100, 150}))
            {
                this.getModule().drawImage(gui, 213, 107, 0, 194, 54, 34);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void keyPress(GuiMinecart gui, char character, int extraInformation)
    {
        if (this.piece != null)
        {
            if (Character.toLowerCase(character) == 119)
            {
                this.piece.rotate(this.board);
            }
            else if (Character.toLowerCase(character) == 97)
            {
                this.piece.move(this, this.board, -1, 0, false);
            }
            else if (Character.toLowerCase(character) == 100)
            {
                this.piece.move(this, this.board, 1, 0, false);
            }
            else if (Character.toLowerCase(character) == 115)
            {
                this.quickMove = true;
            }
        }

        if (Character.toLowerCase(character) == 114)
        {
            this.newgame();
        }
    }

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && !this.isPlaying && this.gameOverTicks >= 150 && this.getModule().inRect(x, y, new int[] {189, 9, 100, 150}))
        {
            this.newgame();
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        this.getModule().drawString(gui, Localization.ARCADE.HIGH_SCORE.translate(new String[] {String.valueOf(this.highscore)}), 10, 20, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.SCORE.translate(new String[] {String.valueOf(this.score)}), 10, 40, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.REMOVED_LINES.translate(new String[] {String.valueOf(this.removed)}), 10, 60, 4210752);

        for (int i = 0; i < 4; ++i)
        {
            this.getModule().drawString(gui, Localization.ARCADE.REMOVED_LINES_COMBO.translate(new String[] {String.valueOf(i), String.valueOf(this.removedByAmount[i])}), 10, 80 + i * 10, 4210752);
        }

        this.getModule().drawString(gui, "W - " + Localization.ARCADE.INSTRUCTION_ROTATE.translate(new String[0]), 340, 20, 4210752);
        this.getModule().drawString(gui, "A - " + Localization.ARCADE.INSTRUCTION_LEFT.translate(new String[0]), 340, 30, 4210752);
        this.getModule().drawString(gui, "S - " + Localization.ARCADE.INSTRUCTION_DROP.translate(new String[0]), 340, 40, 4210752);
        this.getModule().drawString(gui, "D - " + Localization.ARCADE.INSTRUCTION_RIGHT.translate(new String[0]), 340, 50, 4210752);
        this.getModule().drawString(gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(new String[0]), 340, 70, 4210752);
    }

    private void newHighScore()
    {
        if (this.score > this.highscore)
        {
            int val = this.score / 100;
            byte byte1 = (byte)(val & 255);
            byte byte2 = (byte)((val & 65280) >> 8);
            this.getModule().sendPacket(1, new byte[] {byte1, byte2});
            this.newHighScore = true;
        }
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 1)
        {
            short data1 = (short)data[0];
            short data2 = (short)data[1];

            if (data1 < 0)
            {
                data1 = (short)(data1 + 256);
            }

            if (data2 < 0)
            {
                data2 = (short)(data2 + 256);
            }

            this.highscore = (data1 | data2 << 8) * 100;
        }
    }

    public void checkGuiData(Object[] info)
    {
        this.getModule().updateGuiData(info, TrackStory.stories.size(), (short)(this.highscore / 100));
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == TrackStory.stories.size())
        {
            this.highscore = data * 100;
        }
    }

    public void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.getModule().generateNBTName("Highscore", id), (short)this.highscore);
    }

    public void Load(NBTTagCompound tagCompound, int id)
    {
        this.highscore = tagCompound.getShort(this.getModule().generateNBTName("Highscore", id));
    }
}
