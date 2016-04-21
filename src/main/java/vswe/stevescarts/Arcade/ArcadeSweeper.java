package vswe.stevescarts.Arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class ArcadeSweeper extends ArcadeGame
{
    private Tile[][] tiles;
    protected boolean isPlaying;
    protected boolean hasFinished;
    private int currentGameType;
    private int ticks;
    protected int creepersLeft;
    protected int emptyLeft;
    private boolean hasStarted;
    private int[] highscore = new int[] {999, 999, 999};
    private int highscoreTicks;
    private static String textureMenu = "/gui/sweeper.png";

    public ArcadeSweeper(ModuleArcade module)
    {
        super(module, Localization.ARCADE.CREEPER);
        this.newGame(this.currentGameType);
    }

    private void newGame(int size)
    {
        switch (size)
        {
            case 0:
                this.newGame(9, 9, 10);
                break;

            case 1:
                this.newGame(16, 16, 40);
                break;

            case 2:
                this.newGame(30, 16, 99);
        }
    }

    @SideOnly(Side.CLIENT)
    public void update()
    {
        super.update();

        if (this.hasStarted && this.isPlaying && !this.hasFinished && this.ticks < 19980)
        {
            ++this.ticks;
        }

        if (this.highscoreTicks > 0)
        {
            ++this.highscoreTicks;

            if (this.highscoreTicks == 78)
            {
                this.highscoreTicks = 0;
                playSound("highscore", 1.0F, 1.0F);
            }
        }
    }

    private void newGame(int width, int height, int totalCreepers)
    {
        this.isPlaying = true;
        this.ticks = 0;
        this.creepersLeft = totalCreepers;
        this.emptyLeft = width * height - totalCreepers;
        this.hasStarted = false;
        this.hasFinished = false;
        this.highscoreTicks = 0;
        this.tiles = new Tile[width][height];
        int creepers;
        int x;

        for (creepers = 0; creepers < width; ++creepers)
        {
            for (x = 0; x < height; ++x)
            {
                this.tiles[creepers][x] = new Tile(this);
            }
        }

        creepers = 0;
        int y;

        while (creepers < totalCreepers)
        {
            x = this.getModule().getCart().rand.nextInt(width);
            y = this.getModule().getCart().rand.nextInt(height);

            if (!this.tiles[x][y].isCreeper())
            {
                this.tiles[x][y].setCreeper();
                ++creepers;
            }
        }

        for (x = 0; x < width; ++x)
        {
            for (y = 0; y < height; ++y)
            {
                if (!this.tiles[x][y].isCreeper())
                {
                    int count = 0;

                    for (int i = -1; i <= 1; ++i)
                    {
                        for (int j = -1; j <= 1; ++j)
                        {
                            if (i != 0 || j != 0)
                            {
                                int x0 = x + i;
                                int y0 = y + j;

                                if (x0 >= 0 && y0 >= 0 && x0 < width && y0 < height && this.tiles[x0][y0].isCreeper())
                                {
                                    ++count;
                                }
                            }
                        }
                    }

                    this.tiles[x][y].setNearbyCreepers(count);
                }
            }
        }
    }

    private int getMarginLeft()
    {
        return (443 - this.tiles.length * 10) / 2;
    }

    private int getMarginTop()
    {
        return (168 - this.tiles[0].length * 10) / 2;
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource(textureMenu);

        for (int i = 0; i < this.tiles.length; ++i)
        {
            for (int j = 0; j < this.tiles[0].length; ++j)
            {
                this.tiles[i][j].draw(this, gui, this.getMarginLeft() + i * 10, this.getMarginTop() + j * 10, x, y);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.isPlaying)
        {
            x -= this.getMarginLeft();
            y -= this.getMarginTop();
            int xc = x / 10;
            int yc = y / 10;

            if (button == 0)
            {
                this.openTile(xc, yc, true);
            }
            else if (button == 1 && this.isValidCoordinate(xc, yc))
            {
                this.hasStarted = true;
                playSound("flagclick", 1.0F, 1.0F);
                this.tiles[xc][yc].mark();
            }
            else if (button == 2 && this.isValidCoordinate(xc, yc) && this.tiles[xc][yc].getState() == Tile.TILE_STATE.OPENED)
            {
                playSound("click", 1.0F, 1.0F);
                int nearby = this.tiles[xc][yc].getNearbyCreepers();

                if (nearby != 0)
                {
                    int i;
                    int j;

                    for (i = -1; i <= 1; ++i)
                    {
                        for (j = -1; j <= 1; ++j)
                        {
                            if ((i != 0 || j != 0) && this.isValidCoordinate(xc + i, yc + j) && this.tiles[xc + i][yc + j].getState() == Tile.TILE_STATE.FLAGGED)
                            {
                                --nearby;
                            }
                        }
                    }

                    if (nearby == 0)
                    {
                        for (i = -1; i <= 1; ++i)
                        {
                            for (j = -1; j <= 1; ++j)
                            {
                                if (i != 0 || j != 0)
                                {
                                    this.openTile(xc + i, yc + j, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isValidCoordinate(int x, int y)
    {
        return x >= 0 && y >= 0 && x < this.tiles.length && y < this.tiles[0].length;
    }

    private void openTile(int x, int y, boolean first)
    {
        if (this.isValidCoordinate(x, y))
        {
            this.hasStarted = true;
            Tile.TILE_OPEN_RESULT result = this.tiles[x][y].open();
            int i;

            if (this.emptyLeft == 0)
            {
                this.hasFinished = true;
                this.isPlaying = false;
                playSound("goodjob", 1.0F, 1.0F);

                if (this.highscore[this.currentGameType] > this.ticks / 20)
                {
                    this.highscoreTicks = 1;
                    i = this.ticks / 20;
                    byte j = (byte)(i & 255);
                    byte byte2 = (byte)((i & 65280) >> 8);
                    this.getModule().sendPacket(3, new byte[] {(byte)this.currentGameType, j, byte2});
                }
            }
            else if (result == Tile.TILE_OPEN_RESULT.BLOB)
            {
                if (first)
                {
                    playSound("blobclick", 1.0F, 1.0F);
                }

                for (i = -1; i <= 1; ++i)
                {
                    for (int var8 = -1; var8 <= 1; ++var8)
                    {
                        this.openTile(x + i, y + var8, false);
                    }
                }
            }
            else if (result == Tile.TILE_OPEN_RESULT.DEAD)
            {
                this.isPlaying = false;
                playDefaultSound("random.explode", 1.0F, (1.0F + (this.getModule().getCart().rand.nextFloat() - this.getModule().getCart().rand.nextFloat()) * 0.2F) * 0.7F);
            }
            else if (result == Tile.TILE_OPEN_RESULT.OK && first)
            {
                playSound("click", 1.0F, 1.0F);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void keyPress(GuiMinecart gui, char character, int extraInformation)
    {
        if (Character.toLowerCase(character) == 114)
        {
            this.newGame(this.currentGameType);
        }
        else if (Character.toLowerCase(character) == 116)
        {
            this.currentGameType = (this.currentGameType + 1) % 3;
            this.newGame(this.currentGameType);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        String[] mapnames = new String[] {Localization.ARCADE.MAP_1.translate(new String[0]), Localization.ARCADE.MAP_2.translate(new String[0]), Localization.ARCADE.MAP_3.translate(new String[0])};
        this.getModule().drawString(gui, Localization.ARCADE.LEFT.translate(new String[] {String.valueOf(this.creepersLeft)}), 10, 180, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.TIME.translate(new String[] {String.valueOf(this.ticks / 20)}), 10, 190, 4210752);
        this.getModule().drawString(gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(new String[0]), 10, 210, 4210752);
        this.getModule().drawString(gui, "T - " + Localization.ARCADE.INSTRUCTION_CHANGE_MAP.translate(new String[0]), 10, 230, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.MAP.translate(new String[] {mapnames[this.currentGameType]}), 10, 240, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.HIGH_SCORES.translate(new String[0]), 330, 180, 4210752);

        for (int i = 0; i < 3; ++i)
        {
            this.getModule().drawString(gui, Localization.ARCADE.HIGH_SCORE_ENTRY.translate(new String[] {mapnames[i], String.valueOf(this.highscore[i])}), 330, 190 + i * 10, 4210752);
        }
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 3)
        {
            short data1 = (short)data[1];
            short data2 = (short)data[2];

            if (data1 < 0)
            {
                data1 = (short)(data1 + 256);
            }

            if (data2 < 0)
            {
                data2 = (short)(data2 + 256);
            }

            this.highscore[data[0]] = data1 | data2 << 8;
        }
    }

    public void checkGuiData(Object[] info)
    {
        for (int i = 0; i < 3; ++i)
        {
            this.getModule().updateGuiData(info, TrackStory.stories.size() + 2 + i, (short)this.highscore[i]);
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id >= TrackStory.stories.size() + 2 && id < TrackStory.stories.size() + 5)
        {
            this.highscore[id - (TrackStory.stories.size() + 2)] = data;
        }
    }

    public void Save(NBTTagCompound tagCompound, int id)
    {
        for (int i = 0; i < 3; ++i)
        {
            tagCompound.setShort(this.getModule().generateNBTName("HighscoreSweeper" + i, id), (short)this.highscore[i]);
        }
    }

    public void Load(NBTTagCompound tagCompound, int id)
    {
        for (int i = 0; i < 3; ++i)
        {
            this.highscore[i] = tagCompound.getShort(this.getModule().generateNBTName("HighscoreSweeper" + i, id));
        }
    }
}
