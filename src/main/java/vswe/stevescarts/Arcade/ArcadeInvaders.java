package vswe.stevescarts.Arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class ArcadeInvaders extends ArcadeGame
{
    protected ArrayList<Unit> invaders = new ArrayList();
    private ArrayList<Player> lives = new ArrayList();
    private ArrayList<Unit> buildings = new ArrayList();
    protected ArrayList<Projectile> projectiles = new ArrayList();
    private Player player;
    protected int moveDirection;
    protected int moveSpeed;
    protected int moveDown;
    private int fireDelay;
    private int score;
    private int highscore;
    protected boolean hasPahighast;
    protected boolean canSpawnPahighast;
    private boolean newHighscore;
    private int gameoverCounter;
    private static String texture = "/gui/invaders.png";
    private static final String[][] numbers = new String[][] {{"XXXX", "X  X", "X  X", "X  X", "X  X", "X  X", "XXXX"}, {"   X", "   X", "   X", "   X", "   X", "   X", "   X"}, {"XXXX", "   X", "   X", "XXXX", "X   ", "X   ", "XXXX"}, {"XXXX", "   X", "   X", "XXXX", "   X", "   X", "XXXX"}, {"X  X", "X  X", "X  X", "XXXX", "   X", "   X", "   X"}, {"XXXX", "X   ", "X   ", "XXXX", "   X", "   X", "XXXX"}, {"XXXX", "X   ", "X   ", "XXXX", "X  X", "X  X", "XXXX"}, {"XXXX", "   X", "   X", "   X", "   X", "   X", "   X"}, {"XXXX", "X  X", "X  X", "XXXX", "X  X", "X  X", "XXXX"}, {"XXXX", "X  X", "X  X", "XXXX", "   X", "   X", "XXXX"}};

    public ArcadeInvaders(ModuleArcade module)
    {
        super(module, Localization.ARCADE.GHAST);
        this.start();
    }

    private void start()
    {
        this.buildings.clear();
        this.lives.clear();
        this.projectiles.clear();
        this.player = new Player(this);
        int i;

        for (i = 0; i < 3; ++i)
        {
            this.lives.add(new Player(this, 10 + i * 20, 190));
        }

        for (i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.buildings.add(new Building(this, 48 + i * 96 + j * 16, 120));
            }
        }

        this.moveSpeed = 0;
        this.fireDelay = 0;
        this.score = 0;
        this.canSpawnPahighast = false;
        this.newHighscore = false;
        this.spawnInvaders();
    }

    private void spawnInvaders()
    {
        this.invaders.clear();
        this.hasPahighast = false;

        for (int j = 0; j < 3; ++j)
        {
            for (int i = 0; i < 14; ++i)
            {
                this.invaders.add(new InvaderGhast(this, 20 + i * 20, 10 + 25 * j));
            }
        }

        ++this.moveSpeed;
        this.moveDirection = 1;
        this.moveDown = 0;
    }

    @SideOnly(Side.CLIENT)
    public void update()
    {
        super.update();
        boolean flag;

        if (this.player != null)
        {
            if (this.player.ready)
            {
                flag = false;
                boolean i = false;
                int invader;

                for (invader = this.invaders.size() - 1; invader >= 0; --invader)
                {
                    Unit invader1 = (Unit)this.invaders.get(invader);
                    Unit.UPDATE_RESULT result = invader1.update();

                    if (result == Unit.UPDATE_RESULT.DEAD)
                    {
                        if (((InvaderGhast)invader1).isPahighast)
                        {
                            this.hasPahighast = false;
                        }

                        playDefaultSound("mob.ghast.scream", 0.03F, 1.0F);
                        this.invaders.remove(invader);
                        ++this.score;
                    }
                    else if (result == Unit.UPDATE_RESULT.TURN_BACK)
                    {
                        flag = true;
                    }
                    else if (result == Unit.UPDATE_RESULT.GAME_OVER)
                    {
                        i = true;
                    }
                }

                if (this.moveDown > 0)
                {
                    --this.moveDown;
                }

                if (flag)
                {
                    this.moveDirection *= -1;
                    this.moveDown = 5;
                }

                if (this.invaders.size() == 0 || this.hasPahighast && this.invaders.size() == 1)
                {
                    this.score += this.hasPahighast ? 200 : 50;
                    this.canSpawnPahighast = true;
                    this.spawnInvaders();
                }

                if (i)
                {
                    this.lives.clear();
                    this.projectiles.clear();
                    this.player = null;
                    this.newHighScore();
                    return;
                }

                for (invader = this.buildings.size() - 1; invader >= 0; --invader)
                {
                    if (((Unit)this.buildings.get(invader)).update() == Unit.UPDATE_RESULT.DEAD)
                    {
                        this.buildings.remove(invader);
                    }
                }

                for (invader = this.projectiles.size() - 1; invader >= 0; --invader)
                {
                    if (((Projectile)this.projectiles.get(invader)).update() == Unit.UPDATE_RESULT.DEAD)
                    {
                        this.projectiles.remove(invader);
                    }
                }

                if (Keyboard.isKeyDown(30))
                {
                    this.player.move(-1);
                }
                else if (Keyboard.isKeyDown(32))
                {
                    this.player.move(1);
                }

                if (this.fireDelay == 0 && Keyboard.isKeyDown(17))
                {
                    this.projectiles.add(new Projectile(this, this.player.x + 8 - 2, this.player.y - 15, true));
                    playDefaultSound("random.bow", 0.8F, 1.0F / (this.getModule().getCart().rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
                    this.fireDelay = 10;
                }
                else if (this.fireDelay > 0)
                {
                    --this.fireDelay;
                }
            }

            if (this.player.update() == Unit.UPDATE_RESULT.DEAD)
            {
                this.projectiles.clear();
                playSound("hit", 1.0F, 1.0F);

                if (this.lives.size() != 0)
                {
                    ((Player)this.lives.get(0)).setTarget(this.player.x, this.player.y);
                    this.player = (Player)this.lives.get(0);
                    this.lives.remove(0);
                }
                else
                {
                    this.player = null;
                    this.newHighScore();
                }
            }
        }
        else if (this.gameoverCounter == 0)
        {
            flag = false;

            for (int var6 = this.invaders.size() - 1; var6 >= 0; --var6)
            {
                Unit var7 = (Unit)this.invaders.get(var6);

                if (var7.update() == Unit.UPDATE_RESULT.TARGET)
                {
                    flag = true;
                }
            }

            if (!flag)
            {
                this.gameoverCounter = 1;
            }
        }
        else if (this.newHighscore && this.gameoverCounter < 5)
        {
            ++this.gameoverCounter;

            if (this.gameoverCounter == 5)
            {
                playSound("highscore", 1.0F, 1.0F);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource(texture);
        int i$;

        for (i$ = 0; i$ < 27; ++i$)
        {
            this.getModule().drawImage(gui, 5 + i$ * 16, 150, 16, 32, 16, 16);
        }

        for (i$ = 0; i$ < 5; ++i$)
        {
            this.getModule().drawImage(gui, 3 + i$ * 16, 190, 16, 32, 16, 16);
        }

        Iterator var6 = this.invaders.iterator();
        Unit building;

        while (var6.hasNext())
        {
            building = (Unit)var6.next();
            building.draw(gui);
        }

        if (this.player != null)
        {
            this.player.draw(gui);
        }

        var6 = this.lives.iterator();

        while (var6.hasNext())
        {
            building = (Unit)var6.next();
            building.draw(gui);
        }

        var6 = this.projectiles.iterator();

        while (var6.hasNext())
        {
            building = (Unit)var6.next();
            building.draw(gui);
        }

        var6 = this.buildings.iterator();

        while (var6.hasNext())
        {
            building = (Unit)var6.next();
            building.draw(gui);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        this.getModule().drawString(gui, Localization.ARCADE.EXTRA_LIVES.translate(new String[0]) + ":", 10, 180, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.HIGH_SCORE.translate(new String[] {String.valueOf(this.highscore)}), 10, 210, 4210752);
        this.getModule().drawString(gui, Localization.ARCADE.SCORE.translate(new String[] {String.valueOf(this.score)}), 10, 220, 4210752);
        this.getModule().drawString(gui, "W - " + Localization.ARCADE.INSTRUCTION_SHOOT.translate(new String[0]), 330, 180, 4210752);
        this.getModule().drawString(gui, "A - " + Localization.ARCADE.INSTRUCTION_LEFT.translate(new String[0]), 330, 190, 4210752);
        this.getModule().drawString(gui, "D - " + Localization.ARCADE.INSTRUCTION_RIGHT.translate(new String[0]), 330, 200, 4210752);
        this.getModule().drawString(gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(new String[0]), 330, 220, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void keyPress(GuiMinecart gui, char character, int extraInformation)
    {
        if (Character.toLowerCase(character) == 114)
        {
            this.start();
        }
    }

    private void newHighScore()
    {
        this.buildings.clear();
        int digits;

        if (this.score == 0)
        {
            digits = 1;
        }
        else
        {
            digits = (int)Math.floor(Math.log10((double)this.score)) + 1;
        }

        this.canSpawnPahighast = false;
        int currentGhast = 0;
        int val;

        for (val = 0; val < digits; ++val)
        {
            int byte1 = this.score / (int)Math.pow(10.0D, (double)(digits - val - 1)) % 10;
            String[] byte2 = numbers[byte1];

            for (int j = 0; j < byte2.length; ++j)
            {
                String line = byte2[j];

                for (int k = 0; k < line.length(); ++k)
                {
                    if (line.charAt(k) == 88)
                    {
                        int x = (443 - (digits * 90 - 10)) / 2 + val * 90 + k * 20;
                        int y = 5 + j * 20;
                        InvaderGhast ghast;

                        if (currentGhast >= this.invaders.size())
                        {
                            this.invaders.add(ghast = new InvaderGhast(this, x, -20));
                            ++currentGhast;
                        }
                        else
                        {
                            ghast = (InvaderGhast)this.invaders.get(currentGhast++);
                        }

                        ghast.setTarget(x, y);
                    }
                }
            }
        }

        for (val = currentGhast; val < this.invaders.size(); ++val)
        {
            InvaderGhast var12 = (InvaderGhast)this.invaders.get(val);
            var12.setTarget(var12.x, -25);
        }

        this.gameoverCounter = 0;

        if (this.score > this.highscore)
        {
            this.newHighscore = true;
            val = this.score;
            byte var13 = (byte)(val & 255);
            byte var14 = (byte)((val & 65280) >> 8);
            this.getModule().sendPacket(2, new byte[] {var13, var14});
        }
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 2)
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

            this.highscore = data1 | data2 << 8;
        }
    }

    public void checkGuiData(Object[] info)
    {
        this.getModule().updateGuiData(info, TrackStory.stories.size() + 1, (short)this.highscore);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == TrackStory.stories.size() + 1)
        {
            this.highscore = data;
        }
    }

    public void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.getModule().generateNBTName("HighscoreGhast", id), (short)this.highscore);
    }

    public void Load(NBTTagCompound tagCompound, int id)
    {
        this.highscore = tagCompound.getShort(this.getModule().generateNBTName("HighscoreGhast", id));
    }
}
