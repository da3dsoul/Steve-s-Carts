package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Arcade.ArcadeGame;
import vswe.stevescarts.Arcade.ArcadeInvaders;
import vswe.stevescarts.Arcade.ArcadeSweeper;
import vswe.stevescarts.Arcade.ArcadeTetris;
import vswe.stevescarts.Arcade.ArcadeTracks;
import vswe.stevescarts.Arcade.TrackStory;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleArcade extends ModuleBase
{
    private ArrayList<ArcadeGame> games = new ArrayList();
    private ArcadeGame currentGame;
    private int afkTimer;

    public ModuleArcade(MinecartModular cart)
    {
        super(cart);
        this.games.add(new ArcadeTracks(this));
        this.games.add(new ArcadeTetris(this));
        this.games.add(new ArcadeInvaders(this));
        this.games.add(new ArcadeSweeper(this));
    }

    private boolean isGameActive()
    {
        return this.getCart().worldObj.isRemote && this.currentGame != null;
    }

    public boolean doStealInterface()
    {
        return this.isGameActive();
    }

    public boolean hasSlots()
    {
        return false;
    }

    public boolean hasGui()
    {
        return true;
    }

    public int guiWidth()
    {
        return 190;
    }

    public int guiHeight()
    {
        return 115;
    }

    public void update()
    {
        if (this.isGameActive() && this.afkTimer < 10)
        {
            this.currentGame.update();
            ++this.afkTimer;
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        if (this.isGameActive())
        {
            this.currentGame.drawForeground(gui);
        }
        else
        {
            this.drawString(gui, this.getModuleName(), 8, 6, 4210752);

            for (int i = 0; i < this.games.size(); ++i)
            {
                int[] text = this.getButtonTextArea(i);

                if (text[3] == 8)
                {
                    this.drawString(gui, ((ArcadeGame)this.games.get(i)).getName(), text[0], text[1], 4210752);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/arcade.png");
        this.afkTimer = 0;
        int[] rect;

        if (this.isGameActive())
        {
            rect = this.getExitArea();
            byte i = 0;
            int button = 104 + (this.inRect(x, y, rect) ? 16 : 0);
            this.drawImage(gui, rect, i, button);
            this.currentGame.drawBackground(gui, x, y);
        }
        else
        {
            rect = this.getListArea();
            this.drawImage(gui, rect, 0, 0);

            for (int var10 = 0; var10 < this.games.size(); ++var10)
            {
                int[] var11 = this.getButtonGraphicArea(var10);
                byte srcX = 0;
                int srcY = 136 + (this.inRect(x, y, this.getButtonBoundsArea(var10)) ? var11[3] : 0);

                if (var11[3] > 0)
                {
                    this.drawImage(gui, var11, srcX, srcY);
                    int[] icon = this.getButtonIconArea(var10);
                    this.drawImage(gui, icon, var10 * 16, rect[3]);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        if (this.isGameActive())
        {
            this.drawStringOnMouseOver(gui, "Exit", x, y, this.getExitArea());
            this.currentGame.drawMouseOver(gui, x, y);
        }
    }

    private int[] getExitArea()
    {
        return new int[] {455, 6, 16, 16};
    }

    private int[] getListArea()
    {
        return new int[] {15, 20, 170, 88};
    }

    private int[] getButtonBoundsArea(int i)
    {
        return this.getButtonArea(i, false);
    }

    private int[] getButtonGraphicArea(int i)
    {
        return this.getButtonArea(i, true);
    }

    private int[] getButtonArea(int i, boolean graphic)
    {
        int[] list = this.getListArea();
        return new int[] {list[0] + 2, list[1] + 2 + i * 21, 166, graphic ? 21 : 20};
    }

    private int[] getButtonTextArea(int i)
    {
        int[] button = this.getButtonGraphicArea(i);
        return new int[] {button[0] + 24, button[1] + 6, button[2], 8};
    }

    private int[] getButtonIconArea(int i)
    {
        int[] button = this.getButtonGraphicArea(i);
        return new int[] {button[0] + 2, button[1] + 2, 16, 16};
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.isGameActive())
        {
            if (button == 0 && this.inRect(x, y, this.getExitArea()))
            {
                this.currentGame.unload(gui);
                this.currentGame = null;
            }
            else
            {
                this.currentGame.mouseClicked(gui, x, y, button);
            }
        }
        else if (button == 0)
        {
            for (int i = 0; i < this.games.size(); ++i)
            {
                if (this.inRect(x, y, this.getButtonBoundsArea(i)))
                {
                    this.currentGame = (ArcadeGame)this.games.get(i);
                    this.currentGame.load(gui);
                    break;
                }
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (this.isGameActive())
        {
            this.currentGame.mouseMovedOrUp(gui, x, y, button);
        }
    }

    public void keyPress(GuiMinecart gui, char character, int extraInformation)
    {
        if (this.isGameActive())
        {
            this.currentGame.keyPress(gui, character, extraInformation);
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        Iterator i$ = this.games.iterator();

        while (i$.hasNext())
        {
            ArcadeGame game = (ArcadeGame)i$.next();
            game.Save(tagCompound, id);
        }
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        Iterator i$ = this.games.iterator();

        while (i$.hasNext())
        {
            ArcadeGame game = (ArcadeGame)i$.next();
            game.Load(tagCompound, id);
        }
    }

    public int numberOfPackets()
    {
        return 4;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        Iterator i$ = this.games.iterator();

        while (i$.hasNext())
        {
            ArcadeGame game = (ArcadeGame)i$.next();
            game.receivePacket(id, data, player);
        }
    }

    public int numberOfGuiData()
    {
        return TrackStory.stories.size() + 5;
    }

    protected void checkGuiData(Object[] info)
    {
        Iterator i$ = this.games.iterator();

        while (i$.hasNext())
        {
            ArcadeGame game = (ArcadeGame)i$.next();
            game.checkGuiData(info);
        }
    }

    public void receiveGuiData(int id, short data)
    {
        Iterator i$ = this.games.iterator();

        while (i$.hasNext())
        {
            ArcadeGame game = (ArcadeGame)i$.next();
            game.receiveGuiData(id, data);
        }
    }

    public boolean disableStandardKeyFunctionality()
    {
        return this.currentGame != null ? this.currentGame.disableStandardKeyFunctionality() : false;
    }
}
