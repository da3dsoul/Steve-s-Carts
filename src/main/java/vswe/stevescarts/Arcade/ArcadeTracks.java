package vswe.stevescarts.Arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class ArcadeTracks extends ArcadeGame
{
    private TrackLevel currentMap;
    private boolean isMenuOpen = true;
    private boolean isRunning = false;
    private int currentStory = -1;
    private int currentLevel = -1;
    private int[] unlockedLevels;
    ArrayList<Cart> carts = new ArrayList();
    private Cart player;
    private Cart enderman;
    private int playerStartX;
    private int playerStartY;
    private TrackOrientation.DIRECTION playerStartDirection;
    private int itemX;
    private int itemY;
    private boolean isItemTaken;
    private ArrayList<Track> tracks;
    private Track[][] trackMap;
    private int tick;
    private int currentMenuTab = 0;
    private ArrayList<ScrollableList> lists;
    private boolean storySelected;
    private ScrollableList storyList;
    private ScrollableList mapList;
    private ScrollableList userList;
    private ArrayList<TrackLevel> userMaps;
    private boolean isUsingEditor;
    private boolean isSaveMenuOpen;
    private boolean failedToSave;
    private String saveName = "";
    private String lastSavedName = "";
    public static final int LEFT_MARGIN = 5;
    public static final int TOP_MARGIN = 5;
    private static String textureMenu = "/gui/trackgamemenu.png";
    private static String textureGame = "/gui/trackgame.png";
    private final int BUTTON_COUNT = 14;
    private TrackEditor editorTrack;
    private TrackDetector editorDetectorTrack;
    private Track hoveringTrack;
    private boolean isEditorTrackDraging;
    private String validSaveNameCharacters = "abcdefghijklmnopqrstuvwxyz0123456789 ";

    public ArcadeTracks(ModuleArcade module)
    {
        super(module, Localization.ARCADE.OPERATOR);
        this.carts.add(this.player = new Cart(0)
        {
            public void onItemPickUp()
            {
                ArcadeTracks.this.completeLevel();
                ArcadeGame.playSound("win", 1.0F, 1.0F);
            }
            public void onCrash()
            {
                if (ArcadeTracks.this.isPlayingFinalLevel() && ArcadeTracks.this.currentStory < ArcadeTracks.this.unlockedLevels.length - 1 && ArcadeTracks.this.unlockedLevels[ArcadeTracks.this.currentStory + 1] == -1)
                {
                    ArcadeTracks.this.getModule().sendPacket(0, new byte[] {(byte)(ArcadeTracks.this.currentStory + 1), (byte)0});
                }
            }
        });
        this.carts.add(this.enderman = new Cart(1));
        this.lists = new ArrayList();
        this.lists.add(this.storyList = new ScrollableList(this, 5, 40)
        {
            public boolean isVisible()
            {
                return ArcadeTracks.this.currentMenuTab == 0 && !ArcadeTracks.this.storySelected;
            }
        });
        this.lists.add(this.mapList = new ScrollableList(this, 5, 40)
        {
            public boolean isVisible()
            {
                return ArcadeTracks.this.currentMenuTab == 0 && ArcadeTracks.this.storySelected;
            }
        });
        this.lists.add(this.userList = new ScrollableList(this, 5, 40)
        {
            public boolean isVisible()
            {
                return ArcadeTracks.this.currentMenuTab == 1;
            }
        });
        this.unlockedLevels = new int[TrackStory.stories.size()];
        this.unlockedLevels[0] = 0;

        for (int i = 1; i < this.unlockedLevels.length; ++i)
        {
            this.unlockedLevels[i] = -1;
        }

        this.loadStories();

        if (this.getModule().getCart().worldObj.isRemote)
        {
            this.loadUserMaps();
        }
    }

    private void loadStories()
    {
        this.storyList.clearList();

        for (int i = 0; i < TrackStory.stories.size(); ++i)
        {
            if (this.unlockedLevels[i] > -1)
            {
                this.storyList.add(((TrackStory)TrackStory.stories.get(i)).getName());
            }
            else
            {
                this.storyList.add((String)null);
            }
        }
    }

    private void loadMaps()
    {
        int story = this.storyList.getSelectedIndex();

        if (story != -1)
        {
            ArrayList levels = ((TrackStory)TrackStory.stories.get(story)).getLevels();
            this.mapList.clearList();

            for (int i = 0; i < levels.size(); ++i)
            {
                if (this.unlockedLevels[story] >= i)
                {
                    this.mapList.add(((TrackLevel)levels.get(i)).getName());
                }
                else
                {
                    this.mapList.add((String)null);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void loadUserMaps()
    {
        this.userList.clearList();
        this.userMaps = TrackLevel.loadMapsFromFolder();
        int i;

        if (StevesCarts.arcadeDevOperator)
        {
            for (i = 0; i < TrackStory.stories.size(); ++i)
            {
                for (int j = 0; j < ((TrackStory)TrackStory.stories.get(i)).getLevels().size(); ++j)
                {
                    this.userMaps.add(((TrackStory)TrackStory.stories.get(i)).getLevels().get(j));
                }
            }
        }

        for (i = 0; i < this.userMaps.size(); ++i)
        {
            this.userList.add(((TrackLevel)this.userMaps.get(i)).getName());
        }
    }

    private void loadMap(int story, int level)
    {
        this.currentStory = story;
        this.currentLevel = level;
        this.loadMap((TrackLevel)((TrackStory)TrackStory.stories.get(story)).getLevels().get(level));
    }

    private void loadMap(TrackLevel map)
    {
        this.isUsingEditor = false;
        this.trackMap = new Track[27][10];
        this.tracks = new ArrayList();
        Iterator i$ = map.getTracks().iterator();

        while (i$.hasNext())
        {
            Track track = (Track)i$.next();
            Track newtrack = track.copy();
            this.tracks.add(newtrack);

            if (newtrack.getX() >= 0 && newtrack.getX() < this.trackMap.length && newtrack.getY() >= 0 && newtrack.getY() < this.trackMap[0].length)
            {
                this.trackMap[newtrack.getX()][newtrack.getY()] = newtrack;
            }
        }

        this.hoveringTrack = null;
        this.editorTrack = null;
        this.editorDetectorTrack = null;
        this.currentMap = map;
        this.isRunning = false;
        this.playerStartX = this.currentMap.getPlayerStartX();
        this.playerStartY = this.currentMap.getPlayerStartY();
        this.playerStartDirection = this.currentMap.getPlayerStartDirection();
        this.itemX = this.currentMap.getItemX();
        this.itemY = this.currentMap.getItemY();
        this.resetPosition();
    }

    private void resetPosition()
    {
        this.tick = 0;
        this.player.setX(this.playerStartX);
        this.player.setY(this.playerStartY);
        this.isItemTaken = false;
        this.player.setDirection(TrackOrientation.DIRECTION.STILL);
        this.enderman.setAlive(false);
    }

    public Track[][] getTrackMap()
    {
        return this.trackMap;
    }

    public Cart getEnderman()
    {
        return this.enderman;
    }

    private boolean isPlayingFinalLevel()
    {
        return this.isPlayingNormalLevel() && this.currentLevel == ((TrackStory)TrackStory.stories.get(this.currentStory)).getLevels().size() - 1;
    }

    private boolean isUsingEditor()
    {
        return this.isUsingEditor;
    }

    private boolean isPlayingUserLevel()
    {
        return this.currentStory == -1;
    }

    private boolean isPlayingNormalLevel()
    {
        return !this.isUsingEditor() && !this.isPlayingUserLevel();
    }

    public void update()
    {
        super.update();

        if (this.isRunning)
        {
            if (this.tick == 3)
            {
                Iterator i$ = this.carts.iterator();

                while (i$.hasNext())
                {
                    Cart cart = (Cart)i$.next();
                    cart.move(this);
                }

                this.tick = 0;
            }
            else
            {
                ++this.tick;
            }
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        int[] i$;

        if (this.isSaveMenuOpen)
        {
            i$ = this.getSaveMenuArea();

            if (this.failedToSave)
            {
                this.getModule().drawString(gui, Localization.ARCADE.SAVE_ERROR.translate(new String[0]), i$[0] + 3, i$[1] + 3, 16711680);
            }
            else
            {
                this.getModule().drawString(gui, Localization.ARCADE.SAVE.translate(new String[0]), i$[0] + 3, i$[1] + 3, 4210752);
            }

            this.getModule().drawString(gui, this.saveName + (this.saveName.length() < 15 && this.getModule().getCart().worldObj.getWorldTime() % 20L < 10L ? "|" : ""), i$[0] + 5, i$[1] + 16, 16777215);
        }
        else
        {
            Iterator i$1;

            if (this.isMenuOpen)
            {
                i$1 = this.lists.iterator();

                while (i$1.hasNext())
                {
                    ScrollableList message = (ScrollableList)i$1.next();
                    message.drawForeground(gui);
                }

                if (this.currentMenuTab != 0 && this.currentMenuTab != 1)
                {
                    i$ = this.getMenuArea();
                    this.getModule().drawSplitString(gui, Localization.ARCADE.HELP.translate(new String[0]), i$[0] + 10, i$[1] + 20, i$[2] - 20, 4210752);
                }
                else
                {
                    i$ = this.getMenuArea();
                    String message1;

                    if (this.currentMenuTab == 1)
                    {
                        message1 = Localization.ARCADE.USER_MAPS.translate(new String[0]);
                    }
                    else if (this.storySelected)
                    {
                        message1 = ((TrackStory)TrackStory.stories.get(this.storyList.getSelectedIndex())).getName();
                    }
                    else
                    {
                        message1 = Localization.ARCADE.STORIES.translate(new String[0]);
                    }

                    this.getModule().drawString(gui, message1, i$[0] + 5, i$[1] + 32, 4210752);
                }
            }
            else
            {
                i$1 = this.currentMap.getMessages().iterator();

                while (i$1.hasNext())
                {
                    LevelMessage message2 = (LevelMessage)i$1.next();

                    if (message2.isVisible(this.isRunning, this.isRunning && this.player.getDireciotn() == TrackOrientation.DIRECTION.STILL, this.isRunning && this.isItemTaken))
                    {
                        this.getModule().drawSplitString(gui, message2.getMessage(), 9 + message2.getX() * 16, 9 + message2.getY() * 16, message2.getW() * 16, 4210752);
                    }
                }

                if (this.isUsingEditor())
                {
                    this.getModule().drawString(gui, "1-5 - " + Localization.ARCADE.INSTRUCTION_SHAPE.translate(new String[0]), 10, 180, 4210752);
                    this.getModule().drawString(gui, "R - " + Localization.ARCADE.INSTRUCTION_ROTATE_TRACK.translate(new String[0]), 10, 190, 4210752);
                    this.getModule().drawString(gui, "F - " + Localization.ARCADE.INSTRUCTION_FLIP_TRACK.translate(new String[0]), 10, 200, 4210752);
                    this.getModule().drawString(gui, "A - " + Localization.ARCADE.INSTRUCTION_DEFAULT_DIRECTION.translate(new String[0]), 10, 210, 4210752);
                    this.getModule().drawString(gui, "T - " + Localization.ARCADE.INSTRUCTION_TRACK_TYPE.translate(new String[0]), 10, 220, 4210752);
                    this.getModule().drawString(gui, "D - " + Localization.ARCADE.INSTRUCTION_DELETE_TRACK.translate(new String[0]), 10, 230, 4210752);
                    this.getModule().drawString(gui, "C - " + Localization.ARCADE.INSTRUCTION_COPY_TRACK.translate(new String[0]), 10, 240, 4210752);
                    this.getModule().drawString(gui, "S - " + Localization.ARCADE.INSTRUCTION_STEVE.translate(new String[0]), 330, 180, 4210752);
                    this.getModule().drawString(gui, "X - " + Localization.ARCADE.INSTRUCTION_MAP.translate(new String[0]), 330, 190, 4210752);
                    this.getModule().drawString(gui, Localization.ARCADE.LEFT_MOUSE.translate(new String[0]) + " - " + Localization.ARCADE.INSTRUCTION_PLACE_TRACK.translate(new String[0]), 330, 200, 4210752);
                    this.getModule().drawString(gui, Localization.ARCADE.RIGHT_MOUSE.translate(new String[0]) + " - " + Localization.ARCADE.INSTRUCTION_DESELECT_TRACK.translate(new String[0]), 330, 210, 4210752);
                }
            }
        }
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        int i;
        Iterator var8;
        int[] var13;

        if (!this.isSaveMenuOpen && this.isMenuOpen)
        {
            ResourceHelper.bindResource(textureMenu);
            this.getModule().drawImage(gui, this.getMenuArea(), 0, 0);

            for (i = 0; i < 3; ++i)
            {
                var13 = this.getMenuTabArea(i);
                boolean srcX = this.getModule().inRect(x, y, var13);
                boolean srcY = !srcX && i == this.currentMenuTab;

                if (!srcY)
                {
                    this.getModule().drawImage(gui, var13[0], var13[1] + var13[3], 0, srcX ? 114 : 113, var13[2], 1);
                }
            }

            var8 = this.lists.iterator();

            while (var8.hasNext())
            {
                ScrollableList var14 = (ScrollableList)var8.next();
                var14.drawBackground(gui, x, y);
            }
        }
        else if (this.currentMap != null)
        {
            ResourceHelper.bindResource(textureGame);

            if (this.isUsingEditor() && !this.isRunning)
            {
                for (i = 0; i < this.trackMap.length; ++i)
                {
                    for (int rect = 0; rect < this.trackMap[0].length; ++rect)
                    {
                        this.getModule().drawImage(gui, 5 + i * 16, 5 + rect * 16, 16, 128, 16, 16);
                    }
                }
            }

            var8 = this.tracks.iterator();
            Track var11;

            while (var8.hasNext())
            {
                var11 = (Track)var8.next();
                this.getModule().drawImage(gui, getTrackArea(var11.getX(), var11.getY()), 16 * var11.getU(), 16 * var11.getV(), var11.getRotation());
            }

            if (this.isUsingEditor())
            {
                if (this.editorDetectorTrack != null && !this.isRunning)
                {
                    this.editorDetectorTrack.drawOverlay(this.getModule(), gui, this.editorDetectorTrack.getX() * 16 + 8, this.editorDetectorTrack.getY() * 16 + 8, this.isRunning);
                    this.getModule().drawImage(gui, 5 + this.editorDetectorTrack.getX() * 16, 5 + this.editorDetectorTrack.getY() * 16, 32, 128, 16, 16);
                }
            }
            else
            {
                var8 = this.tracks.iterator();

                while (var8.hasNext())
                {
                    var11 = (Track)var8.next();
                    var11.drawOverlay(this.getModule(), gui, x, y, this.isRunning);
                }
            }

            if (!this.isItemTaken)
            {
                byte var9 = 0;

                if (this.isPlayingFinalLevel())
                {
                    var9 = 1;
                }

                this.getModule().drawImage(gui, 5 + this.itemX * 16, 5 + this.itemY * 16, 16 * var9, 240, 16, 16);
            }

            var8 = this.carts.iterator();

            while (var8.hasNext())
            {
                Cart var12 = (Cart)var8.next();
                var12.render(this, gui, this.tick);
            }

            if (this.isUsingEditor() && !this.isRunning)
            {
                this.getModule().drawImage(gui, 5 + this.playerStartX * 16, 5 + this.playerStartY * 16, 162, 212, 8, 8, this.playerStartDirection.getRenderRotation());
            }

            if (!this.isMenuOpen && this.editorTrack != null)
            {
                this.getModule().drawImage(gui, x - 8, y - 8, 16 * this.editorTrack.getU(), 16 * this.editorTrack.getV(), 16, 16, this.editorTrack.getRotation());
            }

            if (this.isSaveMenuOpen)
            {
                int[] var10 = this.getSaveMenuArea();
                this.getModule().drawImage(gui, var10, 0, 144);
            }
        }

        ResourceHelper.bindResource(textureGame);

        for (i = 0; i < 14; ++i)
        {
            if (this.isButtonVisible(i))
            {
                var13 = this.getButtonArea(i);
                int var15 = this.isButtonDisabled(i) ? 208 : (this.getModule().inRect(x, y, var13) ? 224 : 240);
                int var16 = i * 16;
                this.getModule().drawImage(gui, var13, var15, var16);
            }
        }
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        for (int i = 0; i < 14; ++i)
        {
            if (!this.isButtonDisabled(i) && this.isButtonVisible(i))
            {
                this.getModule().drawStringOnMouseOver(gui, this.getButtonText(i), x, y, this.getButtonArea(i));
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (!this.isSaveMenuOpen)
        {
            if (this.isMenuOpen)
            {
                Iterator x2 = this.lists.iterator();

                while (x2.hasNext())
                {
                    ScrollableList y2 = (ScrollableList)x2.next();
                    y2.mouseMovedOrUp(gui, x, y, button);
                }
            }

            if (this.currentMap != null && this.isUsingEditor())
            {
                int x21 = x - 5;
                int y21 = y - 5;
                int gridX = x21 / 16;
                int gridY = y21 / 16;

                if (gridX >= 0 && gridX < this.trackMap.length && gridY >= 0 && gridY < this.trackMap[0].length)
                {
                    this.hoveringTrack = this.trackMap[gridX][gridY];
                }
                else
                {
                    this.hoveringTrack = null;
                }
            }

            this.handleEditorTrack(x, y, button, false);
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        int i;

        if (!this.isSaveMenuOpen)
        {
            Iterator var7;

            if (this.isMenuOpen)
            {
                if (!this.getModule().inRect(x, y, this.getMenuArea()))
                {
                    if (this.currentMap != null)
                    {
                        this.isMenuOpen = false;
                    }
                }
                else
                {
                    for (i = 0; i < 3; ++i)
                    {
                        if (i != this.currentMenuTab && this.getModule().inRect(x, y, this.getMenuTabArea(i)))
                        {
                            this.currentMenuTab = i;
                            break;
                        }
                    }

                    var7 = this.lists.iterator();

                    while (var7.hasNext())
                    {
                        ScrollableList rect = (ScrollableList)var7.next();
                        rect.mouseClicked(gui, x, y, button);
                    }
                }
            }
            else
            {
                if (!this.isRunning)
                {
                    var7 = this.tracks.iterator();

                    while (var7.hasNext())
                    {
                        Track var8 = (Track)var7.next();

                        if (this.getModule().inRect(x, y, getTrackArea(var8.getX(), var8.getY())))
                        {
                            if (this.isUsingEditor())
                            {
                                if (this.editorTrack == null)
                                {
                                    var8.onEditorClick(this);
                                }
                            }
                            else
                            {
                                var8.onClick(this);
                            }
                        }
                    }
                }

                this.handleEditorTrack(x, y, button, true);
            }
        }

        for (i = 0; i < 14; ++i)
        {
            int[] var9 = this.getButtonArea(i);

            if (this.getModule().inRect(x, y, var9) && this.isButtonVisible(i) && !this.isButtonDisabled(i))
            {
                this.buttonClicked(i);
                break;
            }
        }
    }

    public void completeLevel()
    {
        if (this.isPlayingNormalLevel())
        {
            int nextLevel = this.currentLevel + 1;

            if (nextLevel > this.unlockedLevels[this.currentStory])
            {
                this.getModule().sendPacket(0, new byte[] {(byte)this.currentStory, (byte)nextLevel});
            }
        }
    }

    public int[] getMenuArea()
    {
        return new int[] {93, 27, 256, 113};
    }

    private int[] getMenuTabArea(int id)
    {
        int[] menu = this.getMenuArea();
        return new int[] {menu[0] + 1 + id * 85, menu[1] + 1, 84, 12};
    }

    private int[] getSaveMenuArea()
    {
        return new int[] {172, 60, 99, 47};
    }

    private int[] getButtonArea(int id)
    {
        int[] menu;

        if (id != 4 && id != 5)
        {
            if (id > 5 && id < 10)
            {
                menu = this.getMenuArea();
                return new int[] {menu[0] + 235, menu[1] + 20 + (id - 6) * 18, 16, 16};
            }
            else if (id >= 12 && id < 14)
            {
                menu = this.getSaveMenuArea();
                return new int[] {menu[0] + menu[2] - 18 * (id - 11) - 2, menu[1] + menu[3] - 18, 16, 16};
            }
            else
            {
                if (id >= 10 && id < 12)
                {
                    id -= 6;
                }

                return new int[] {455, 26 + id * 18, 16, 16};
            }
        }
        else
        {
            menu = this.getMenuArea();
            return new int[] {menu[0] + 235 - 18 * (id - 4), menu[1] + 20, 16, 16};
        }
    }

    private boolean isButtonVisible(int id)
    {
        return id != 4 && id != 5 ? (id > 5 && id < 10 ? this.isMenuOpen && this.currentMenuTab == 1 : (id >= 10 && id < 12 ? this.isUsingEditor() : (id >= 12 && id < 14 ? this.isSaveMenuOpen : true))) : this.isMenuOpen && this.currentMenuTab == 0;
    }

    private boolean isButtonDisabled(int id)
    {
        switch (id)
        {
            case 0:
                return this.isRunning || this.isMenuOpen || this.isSaveMenuOpen;

            case 1:
                return this.isRunning || this.isMenuOpen || this.isSaveMenuOpen;

            case 2:
                return !this.isRunning || this.isSaveMenuOpen;

            case 3:
                return this.isMenuOpen || this.isSaveMenuOpen || !this.isPlayingNormalLevel() || this.currentLevel + 1 > this.unlockedLevels[this.currentStory];

            case 4:
                return (this.storySelected ? this.mapList : this.storyList).getSelectedIndex() == -1;

            case 5:
                return !this.storySelected;

            case 6:
            case 8:
                return this.userList.getSelectedIndex() == -1;

            case 7:
            case 9:
            case 12:
                return false;

            case 10:
            case 11:
                return this.isMenuOpen || this.isSaveMenuOpen || this.isRunning;

            case 13:
                return this.saveName.length() == 0;

            default:
                return true;
        }
    }

    private void buttonClicked(int id)
    {
        Track track;
        Iterator mapToEdit1;

        switch (id)
        {
            case 0:
                mapToEdit1 = this.tracks.iterator();

                while (mapToEdit1.hasNext())
                {
                    track = (Track)mapToEdit1.next();
                    track.saveBackup();
                }

                this.player.setDirection(this.playerStartDirection);
                this.isRunning = true;
                break;

            case 1:
                this.isMenuOpen = true;
                this.editorTrack = null;
                break;

            case 2:
                mapToEdit1 = this.tracks.iterator();

                while (mapToEdit1.hasNext())
                {
                    track = (Track)mapToEdit1.next();
                    track.loadBackup();
                }

                this.resetPosition();
                this.isRunning = false;
                break;

            case 3:
                this.loadMap(this.currentStory, this.currentLevel + 1);
                break;

            case 4:
                if (this.storySelected)
                {
                    this.loadMap(this.storyList.getSelectedIndex(), this.mapList.getSelectedIndex());
                    this.isMenuOpen = false;
                }
                else
                {
                    this.storySelected = true;
                    this.mapList.clear();
                    this.loadMaps();
                }

                break;

            case 5:
                this.storySelected = false;
                break;

            case 6:
                this.currentStory = -1;
                this.loadMap((TrackLevel)this.userMaps.get(this.userList.getSelectedIndex()));
                this.isMenuOpen = false;
                break;

            case 7:
                this.loadMap(TrackLevel.editor);
                this.isMenuOpen = false;
                this.lastSavedName = "";
                this.isUsingEditor = true;
                break;

            case 8:
                TrackLevel mapToEdit = (TrackLevel)this.userMaps.get(this.userList.getSelectedIndex());
                this.loadMap(mapToEdit);
                this.lastSavedName = mapToEdit.getName();
                this.isMenuOpen = false;
                this.isUsingEditor = true;
                break;

            case 9:
                this.userList.clear();

                if (this.getModule().getCart().worldObj.isRemote)
                {
                    this.loadUserMaps();
                }

                break;

            case 10:
                if (this.lastSavedName.length() == 0)
                {
                    this.isSaveMenuOpen = true;
                    this.failedToSave = false;
                }
                else
                {
                    this.save(this.lastSavedName);
                }

                break;

            case 11:
                this.isSaveMenuOpen = true;
                this.failedToSave = false;
                break;

            case 12:
                this.isSaveMenuOpen = false;
                break;

            case 13:
                if (this.save(this.saveName))
                {
                    this.saveName = "";
                    this.isSaveMenuOpen = false;
                }
        }
    }

    private String getButtonText(int id)
    {
        switch (id)
        {
            case 0:
                return Localization.ARCADE.BUTTON_START.translate(new String[0]);

            case 1:
                return Localization.ARCADE.BUTTON_MENU.translate(new String[0]);

            case 2:
                return Localization.ARCADE.BUTTON_STOP.translate(new String[0]);

            case 3:
                return Localization.ARCADE.BUTTON_NEXT.translate(new String[0]);

            case 4:
                return this.storySelected ? Localization.ARCADE.BUTTON_START_LEVEL.translate(new String[0]) : Localization.ARCADE.BUTTON_SELECT_STORY.translate(new String[0]);

            case 5:
                return Localization.ARCADE.BUTTON_SELECT_OTHER_STORY.translate(new String[0]);

            case 6:
                return Localization.ARCADE.BUTTON_START_LEVEL.translate(new String[0]);

            case 7:
                return Localization.ARCADE.BUTTON_CREATE_LEVEL.translate(new String[0]);

            case 8:
                return Localization.ARCADE.BUTTON_EDIT_LEVEL.translate(new String[0]);

            case 9:
                return Localization.ARCADE.BUTTON_REFRESH.translate(new String[0]);

            case 10:
                return Localization.ARCADE.BUTTON_START.translate(new String[0]);

            case 11:
                return Localization.ARCADE.BUTTON_SAVE_AS.translate(new String[0]);

            case 12:
                return Localization.ARCADE.BUTTON_CANCEL.translate(new String[0]);

            case 13:
                return Localization.ARCADE.BUTTON_SAVE.translate(new String[0]);

            default:
                return "Hello, I\'m a button";
        }
    }

    public static int[] getTrackArea(int x, int y)
    {
        return new int[] {5 + 16 * x, 5 + 16 * y, 16, 16};
    }

    public boolean isItemOnGround()
    {
        return !this.isItemTaken;
    }

    public void pickItemUp()
    {
        this.isItemTaken = true;
    }

    public int getItemX()
    {
        return this.itemX;
    }

    public int getItemY()
    {
        return this.itemY;
    }

    public void Save(NBTTagCompound tagCompound, int id)
    {
        for (int i = 0; i < this.unlockedLevels.length; ++i)
        {
            tagCompound.setByte(this.getModule().generateNBTName("Unlocked" + i, id), (byte)this.unlockedLevels[i]);
        }
    }

    public void Load(NBTTagCompound tagCompound, int id)
    {
        for (int i = 0; i < this.unlockedLevels.length; ++i)
        {
            this.unlockedLevels[i] = tagCompound.getByte(this.getModule().generateNBTName("Unlocked" + i, id));
        }

        this.loadStories();
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.unlockedLevels[data[0]] = data[1];

            if (this.unlockedLevels[data[0]] > ((TrackStory)TrackStory.stories.get(data[0])).getLevels().size() - 1)
            {
                this.unlockedLevels[data[0]] = ((TrackStory)TrackStory.stories.get(data[0])).getLevels().size() - 1;
            }
        }
    }

    public void checkGuiData(Object[] info)
    {
        for (int i = 0; i < this.unlockedLevels.length; ++i)
        {
            this.getModule().updateGuiData(info, i, (short)this.unlockedLevels[i]);
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id >= 0 && id < this.unlockedLevels.length)
        {
            this.unlockedLevels[id] = data;

            if (data != 0)
            {
                this.loadMaps();
            }
            else
            {
                this.loadStories();
            }
        }
    }

    public void setEditorTrack(TrackEditor track)
    {
        if (this.editorTrack != null)
        {
            track.setType(this.editorTrack.getType());
        }

        this.editorTrack = track;
    }

    public void setEditorDetectorTrack(TrackDetector track)
    {
        if (track.equals(this.editorDetectorTrack))
        {
            this.editorDetectorTrack = null;
        }
        else
        {
            this.editorDetectorTrack = track;
        }
    }

    public TrackDetector getEditorDetectorTrack()
    {
        return this.editorDetectorTrack;
    }

    public void keyPress(GuiMinecart gui, char character, int extraInformation)
    {
        if (this.isSaveMenuOpen)
        {
            if (this.saveName.length() < 15 && this.validSaveNameCharacters.indexOf(Character.toLowerCase(character)) != -1)
            {
                this.saveName = this.saveName + character;
            }
            else if (extraInformation == 14 && this.saveName.length() > 0)
            {
                this.saveName = this.saveName.substring(0, this.saveName.length() - 1);
            }
        }
        else
        {
            if (!this.isUsingEditor() || this.isRunning)
            {
                return;
            }

            Object track;

            if (this.editorTrack != null)
            {
                track = this.editorTrack;
            }
            else
            {
                track = this.hoveringTrack;
            }

            Iterator i$;
            TrackOrientation orientation;

            switch (Character.toLowerCase(character))
            {
                case 49:
                    this.setEditorTrack(new TrackEditor(TrackOrientation.CORNER_DOWN_RIGHT));
                    break;

                case 50:
                    this.setEditorTrack(new TrackEditor(TrackOrientation.STRAIGHT_VERTICAL));
                    break;

                case 51:
                    this.setEditorTrack(new TrackEditor(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT));
                    break;

                case 52:
                    this.setEditorTrack(new TrackEditor(TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN));
                    break;

                case 53:
                    this.setEditorTrack(new TrackEditor(TrackOrientation.JUNCTION_4WAY));
                    break;

                case 97:
                    if (track != null && ((Track)track).getOrientation().getOpposite() != null)
                    {
                        ((Track)track).setOrientation(((Track)track).getOrientation().getOpposite());
                    }

                    break;

                case 99:
                    if (this.editorTrack == null && this.hoveringTrack != null)
                    {
                        this.setEditorTrack(new TrackEditor(this.hoveringTrack.getOrientation()));
                        this.editorTrack.setType(this.hoveringTrack.getU());
                    }

                    break;

                case 100:
                    if (this.hoveringTrack != null)
                    {
                        this.tracks.remove(this.hoveringTrack);

                        if (this.hoveringTrack.getX() >= 0 && this.hoveringTrack.getX() < this.trackMap.length && this.hoveringTrack.getY() >= 0 && this.hoveringTrack.getY() < this.trackMap[0].length)
                        {
                            this.trackMap[this.hoveringTrack.getX()][this.hoveringTrack.getY()] = null;
                        }

                        this.hoveringTrack = null;
                    }

                    break;

                case 102:
                    if (track != null)
                    {
                        i$ = TrackOrientation.ALL.iterator();

                        while (i$.hasNext())
                        {
                            orientation = (TrackOrientation)i$.next();

                            if (orientation.getV() == ((Track)track).getV() && (orientation.getV() == 2 || orientation.getV() == 3) && orientation.getRotation() == ((Track)track).getRotation().getFlippedRotation())
                            {
                                ((Track)track).setOrientation(orientation);
                                break;
                            }
                        }
                    }

                case 112:
                default:
                    break;

                case 114:
                    if (track != null)
                    {
                        i$ = TrackOrientation.ALL.iterator();

                        while (i$.hasNext())
                        {
                            orientation = (TrackOrientation)i$.next();

                            if (orientation.getV() == ((Track)track).getV() && (orientation.getV() == 1 && orientation.getRotation() != ((Track)track).getRotation() || orientation.getRotation() == ((Track)track).getRotation().getNextRotation()))
                            {
                                ((Track)track).setOrientation(orientation);
                                break;
                            }
                        }
                    }

                    break;

                case 115:
                    if (this.hoveringTrack != null)
                    {
                        if (this.playerStartX == this.hoveringTrack.getX() && this.playerStartY == this.hoveringTrack.getY())
                        {
                            this.playerStartDirection = this.playerStartDirection.getLeft();
                        }
                        else
                        {
                            this.playerStartX = this.hoveringTrack.getX();
                            this.playerStartY = this.hoveringTrack.getY();
                        }

                        this.resetPosition();
                    }

                    break;

                case 116:
                    if (this.editorTrack != null)
                    {
                        this.editorTrack.nextType();
                    }

                    break;

                case 120:
                    if (this.hoveringTrack != null)
                    {
                        this.itemX = this.hoveringTrack.getX();
                        this.itemY = this.hoveringTrack.getY();
                    }
            }
        }
    }

    private void handleEditorTrack(int x, int y, int button, boolean clicked)
    {
        if (this.isRunning)
        {
            this.isEditorTrackDraging = false;
        }
        else
        {
            if (this.editorTrack != null)
            {
                if ((!clicked || button != 0) && (clicked || button != -1 || !this.isEditorTrackDraging))
                {
                    if (button == 1 || !clicked && this.isEditorTrackDraging)
                    {
                        if (clicked)
                        {
                            this.editorTrack = null;
                        }

                        this.isEditorTrackDraging = false;
                    }
                }
                else
                {
                    int x2 = x - 5;
                    int y2 = y - 5;
                    int gridX = x2 / 16;
                    int gridY = y2 / 16;

                    if (gridX >= 0 && gridX < this.trackMap.length && gridY >= 0 && gridY < this.trackMap[0].length)
                    {
                        if (this.trackMap[gridX][gridY] == null)
                        {
                            Track newtrack = this.editorTrack.getRealTrack(gridX, gridY);
                            this.trackMap[gridX][gridY] = newtrack;
                            this.tracks.add(newtrack);
                        }

                        this.isEditorTrackDraging = true;
                    }
                }
            }
        }
    }

    public boolean disableStandardKeyFunctionality()
    {
        return this.isSaveMenuOpen;
    }

    @SideOnly(Side.CLIENT)
    private boolean save(String name)
    {
        if (StevesCarts.arcadeDevOperator)
        {
            if (!name.startsWith(" "))
            {
                String result = TrackLevel.saveMapToString(name, this.playerStartX, this.playerStartY, this.playerStartDirection, this.itemX, this.itemY, this.tracks);
                System.out.println(result);
                return true;
            }

            name = name.substring(1);
        }

        if (TrackLevel.saveMap(name, this.playerStartX, this.playerStartY, this.playerStartDirection, this.itemX, this.itemY, this.tracks))
        {
            this.lastSavedName = name;
            this.loadUserMaps();
            return true;
        }
        else
        {
            this.saveName = name;
            this.failedToSave = true;
            this.isSaveMenuOpen = true;
            return false;
        }
    }
}
