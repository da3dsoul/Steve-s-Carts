package vswe.stevescarts.Arcade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;

public class ArcadeMonopoly extends ArcadeGame
{
    private Die die;
    private Die die2;
    private ArrayList<Piece> pieces = new ArrayList();
    private int currentPiece;
    private Place[] places;
    private int selectedPlace = -1;
    private int diceTimer;
    private int diceCount;
    private int diceDelay;
    private ArrayList<Button> buttons;
    private Button roll;
    private Button end;
    private Button purchase;
    private Button rent;
    private Button bankrupt;
    private Button bed;
    private Button card;
    private Button jail;
    private Button mortgage;
    private Button unmortgage;
    private Button sellbed;
    private boolean rolled;
    private boolean controllable;
    private boolean endable;
    private boolean openedCard;
    private Card currentCard;
    private float cardScale;
    private int cardRotation;
    public static final int PLACE_WIDTH = 76;
    public static final int PLACE_HEIGHT = 122;
    public static final int BOARD_WIDTH = 14;
    public static final int BOARD_HEIGHT = 10;
    public static final float SCALE = 0.17F;
    private static final int CARD_WIDTH = 142;
    private static final int CARD_HEIGHT = 80;
    private static String[] textures = new String[5];

    protected Place getSelectedPlace()
    {
        return this.selectedPlace == -1 ? null : this.places[this.selectedPlace];
    }

    protected Piece getCurrentPiece()
    {
        return (Piece)this.pieces.get(this.currentPiece);
    }

    public ArcadeMonopoly(ModuleArcade module)
    {
        super(module, Localization.ARCADE.MADNESS);
        this.pieces.add(new Piece(this, 0, Piece.CONTROLLED_BY.PLAYER));
        this.pieces.add(new Piece(this, 1, Piece.CONTROLLED_BY.COMPUTER));
        this.pieces.add(new Piece(this, 2, Piece.CONTROLLED_BY.COMPUTER));
        this.pieces.add(new Piece(this, 3, Piece.CONTROLLED_BY.COMPUTER));
        this.pieces.add(new Piece(this, 4, Piece.CONTROLLED_BY.COMPUTER));
        StreetGroup streetGroup1 = new StreetGroup(50, new int[] {89, 12, 56});
        StreetGroup streetGroup2 = new StreetGroup(50, new int[] {102, 45, 145});
        StreetGroup streetGroup3 = new StreetGroup(50, new int[] {135, 166, 213});
        StreetGroup streetGroup4 = new StreetGroup(100, new int[] {239, 56, 120});
        StreetGroup streetGroup5 = new StreetGroup(100, new int[] {245, 128, 45});
        StreetGroup streetGroup6 = new StreetGroup(150, new int[] {238, 58, 35});
        StreetGroup streetGroup7 = new StreetGroup(150, new int[] {252, 231, 4});
        StreetGroup streetGroup8 = new StreetGroup(200, new int[] {19, 165, 92});
        StreetGroup streetGroup9 = new StreetGroup(200, new int[] {40, 78, 161});
        PropertyGroup stationGroup = new PropertyGroup();
        PropertyGroup utilGroup = new PropertyGroup();
        this.places = new Place[] {new Go(this), new Street(this, streetGroup1, "Soaryn Chest", 30, 2), new Community(this), new Street(this, streetGroup1, "Eddie\'s Cobble Stairs", 30, 2), new Place(this), new Utility(this, utilGroup, 0, "Test"), new Street(this, streetGroup2, "Ecu\'s Eco Escape", 60, 4), new Station(this, stationGroup, 0, "Wooden Station"), new Street(this, streetGroup2, "Test", 60, 4), new Villager(this), new Street(this, streetGroup3, "Direwolf\'s 9x9", 100, 6), new Chance(this), new Street(this, streetGroup3, "Greg\'s Forest", 100, 6), new Street(this, streetGroup3, "Alice\'s Tunnel", 110, 8), new Jail(this), new Street(this, streetGroup4, "Flora\'s Alveary", 140, 10), new Utility(this, utilGroup, 1, "Test"), new Street(this, streetGroup4, "Sengir\'s Greenhouse", 140, 10), new Street(this, streetGroup4, "Test", 160, 12), new Station(this, stationGroup, 1, "Standard Station"), new Street(this, streetGroup5, "Muse\'s Moon Base", 200, 14), new Community(this), new Street(this, streetGroup5, "Algorithm\'s Crafting CPU", 200, 14), new Street(this, streetGroup5, "Pink Lemmingaide Stand", 240, 16), new CornerPlace(this, 2), new Street(this, streetGroup6, "Covert\'s Railyard", 250, 18), new Chance(this), new Street(this, streetGroup6, "Test", 250, 18), new Street(this, streetGroup6, "Test", 270, 20), new Community(this), new Street(this, streetGroup6, "Test", 270, 20), new Station(this, stationGroup, 2, "Reinforced Station"), new Street(this, streetGroup7, "Player\'s Industrial Warehouse", 320, 22), new Villager(this), new Street(this, streetGroup7, "Dan\'s Computer Repair", 320, 22), new Street(this, streetGroup7, "iChun\'s Hat Shop", 350, 24), new Utility(this, utilGroup, 2, "Test"), new Street(this, streetGroup7, "Lex\'s Forge", 350, 24), new GoToJail(this), new Street(this, streetGroup8, "Morvelaira\'s Pretty Wall", 400, 26), new Street(this, streetGroup8, "Rorax\'s Tower of Doom", 400, 26), new Community(this), new Street(this, streetGroup8, "Jaded\'s Crash Lab", 440, 30), new Station(this, stationGroup, 3, "Galgadorian Station"), new Chance(this), new Street(this, streetGroup9, "Test", 500, 40), new Place(this), new Street(this, streetGroup9, "Vswe\'s Redstone Tower", 600, 50)};
        ((Property)this.places[1]).setOwner((Piece)this.pieces.get(0));
        ((Property)this.places[3]).setOwner((Piece)this.pieces.get(0));
        this.die = new Die(this, 0);
        this.die2 = new Die(this, 1);
        this.buttons = new ArrayList();
        this.buttons.add(this.roll = new Button()
        {
            public String getName()
            {
                return "Roll";
            }
            public boolean isVisible()
            {
                return true;
            }
            public boolean isEnabled()
            {
                return ArcadeMonopoly.this.diceCount == 0 && ArcadeMonopoly.this.diceTimer == 0 && !ArcadeMonopoly.this.rolled;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.rolled = true;
                ArcadeMonopoly.this.throwDice();
            }
        });
        this.buttons.add(this.end = new Button()
        {
            public String getName()
            {
                return "End Turn";
            }
            public boolean isVisible()
            {
                return true;
            }
            public boolean isEnabled()
            {
                return ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.endable;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.rolled = false;
                ArcadeMonopoly.this.controllable = false;
                ArcadeMonopoly.this.nextPiece();
                ArcadeMonopoly.this.endable = false;
                ArcadeMonopoly.this.openedCard = false;

                if (ArcadeMonopoly.this.useAI())
                {
                    ArcadeMonopoly.this.roll.onClick();
                }
            }
        });
        this.buttons.add(this.purchase = new Button()
        {
            public String getName()
            {
                return "Purchase";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()] instanceof Property && !((Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()]).hasOwner();
            }
            public boolean isEnabled()
            {
                Property property = (Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()];
                return ArcadeMonopoly.this.getCurrentPiece().canAffordProperty(property);
            }
            public boolean isVisibleForPlayer()
            {
                return ArcadeMonopoly.this.getSelectedPlace() == null;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().purchaseProperty((Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()]);
            }
        });
        this.buttons.add(this.rent = new Button()
        {
            public String getName()
            {
                return "Pay Rent";
            }
            public boolean isVisible()
            {
                if (ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()] instanceof Property)
                {
                    Property property = (Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()];
                    return property.hasOwner() && property.getOwner() != ArcadeMonopoly.this.getCurrentPiece() && !property.isMortgaged();
                }
                else
                {
                    return false;
                }
            }
            public boolean isEnabled()
            {
                Property property = (Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()];
                return !ArcadeMonopoly.this.endable && ArcadeMonopoly.this.getCurrentPiece().canAffordRent(property);
            }
            public boolean isVisibleForPlayer()
            {
                return ArcadeMonopoly.this.getSelectedPlace() == null;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().payPropertyRent((Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()]);
                ArcadeMonopoly.this.endable = true;
            }
        });
        this.buttons.add(this.bankrupt = new Button()
        {
            public String getName()
            {
                return "Go Bankrupt";
            }
            public boolean isVisible()
            {
                return !ArcadeMonopoly.this.endable && ArcadeMonopoly.this.rent.isVisible() && !ArcadeMonopoly.this.rent.isEnabled();
            }
            public boolean isEnabled()
            {
                return true;
            }
            public boolean isVisibleForPlayer()
            {
                return ArcadeMonopoly.this.getSelectedPlace() == null;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().bankrupt(((Property)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()]).getOwner());
                ArcadeMonopoly.this.endable = true;
            }
        });
        this.buttons.add(this.bed = new Button()
        {
            public String getName()
            {
                return "Buy Bed";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.getSelectedPlace() != null && ArcadeMonopoly.this.getSelectedPlace() instanceof Street;
            }
            public boolean isEnabled()
            {
                Street street = (Street)ArcadeMonopoly.this.getSelectedPlace();
                return ArcadeMonopoly.this.controllable && street.ownsAllInGroup(ArcadeMonopoly.this.getCurrentPiece()) && street.getStructureCount() < 5 && ArcadeMonopoly.this.getCurrentPiece().canAffordStructure(street) && !street.isMortgaged();
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().buyStructure((Street)ArcadeMonopoly.this.getSelectedPlace());
            }
        });
        this.buttons.add(this.card = new Button()
        {
            public String getName()
            {
                return "Pick a Card";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()] instanceof CardPlace && (!ArcadeMonopoly.this.openedCard || ArcadeMonopoly.this.currentCard != null);
            }
            public boolean isEnabled()
            {
                return !ArcadeMonopoly.this.openedCard;
            }
            public boolean isVisibleForPlayer()
            {
                return ArcadeMonopoly.this.getSelectedPlace() == null;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.openCard(((CardPlace)ArcadeMonopoly.this.places[ArcadeMonopoly.this.getCurrentPiece().getPosition()]).getCard());
            }
        });
        this.buttons.add(this.jail = new Button()
        {
            public String getName()
            {
                return "Pay for /tpx";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.getCurrentPiece().isInJail();
            }
            public boolean isEnabled()
            {
                return ArcadeMonopoly.this.getCurrentPiece().canAffordFine();
            }
            public boolean isVisibleForPlayer()
            {
                return ArcadeMonopoly.this.getSelectedPlace() == null;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().payFine();
                ArcadeMonopoly.this.endable = true;
            }
        });
        this.buttons.add(this.mortgage = new Button()
        {
            public String getName()
            {
                return "Mortgage";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.getSelectedPlace() != null && ArcadeMonopoly.this.getSelectedPlace() instanceof Property && ((Property)ArcadeMonopoly.this.getSelectedPlace()).getOwner() == ArcadeMonopoly.this.getCurrentPiece() && !((Property)ArcadeMonopoly.this.getSelectedPlace()).isMortgaged();
            }
            public boolean isEnabled()
            {
                return ((Property)ArcadeMonopoly.this.getSelectedPlace()).canMortgage();
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().getMoneyFromMortgage((Property)ArcadeMonopoly.this.getSelectedPlace());
            }
        });
        this.buttons.add(this.unmortgage = new Button()
        {
            public String getName()
            {
                return "Unmortage";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.controllable && ArcadeMonopoly.this.getSelectedPlace() != null && ArcadeMonopoly.this.getSelectedPlace() instanceof Property && ((Property)ArcadeMonopoly.this.getSelectedPlace()).getOwner() == ArcadeMonopoly.this.getCurrentPiece() && ((Property)ArcadeMonopoly.this.getSelectedPlace()).isMortgaged();
            }
            public boolean isEnabled()
            {
                return ArcadeMonopoly.this.getCurrentPiece().canAffordUnMortgage((Property)ArcadeMonopoly.this.getSelectedPlace());
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().payUnMortgage((Property)ArcadeMonopoly.this.getSelectedPlace());
            }
        });
        this.buttons.add(this.sellbed = new Button()
        {
            public String getName()
            {
                return "Sell Bed";
            }
            public boolean isVisible()
            {
                return ArcadeMonopoly.this.getSelectedPlace() != null && ArcadeMonopoly.this.getSelectedPlace() instanceof Street;
            }
            public boolean isEnabled()
            {
                Street street = (Street)ArcadeMonopoly.this.getSelectedPlace();
                return ArcadeMonopoly.this.controllable && street.getStructureCount() > 0;
            }
            public void onClick()
            {
                ArcadeMonopoly.this.getCurrentPiece().sellStructure((Street)ArcadeMonopoly.this.getSelectedPlace());
            }
        });

        if (this.getCurrentPiece().getController() == Piece.CONTROLLED_BY.COMPUTER)
        {
            this.roll.onClick();
        }
    }

    private boolean useAI()
    {
        return this.getCurrentPiece().getController() == Piece.CONTROLLED_BY.COMPUTER;
    }

    private void nextPiece()
    {
        this.currentPiece = (this.currentPiece + 1) % this.pieces.size();

        if (this.getCurrentPiece().isBankrupt())
        {
            this.nextPiece();
        }
    }

    private void throwDice()
    {
        if (this.diceCount == 0)
        {
            if (this.diceTimer == 0)
            {
                this.diceTimer = 20;
            }

            this.die.randomize();
            this.die2.randomize();
        }
    }

    public void movePiece(int steps)
    {
        this.diceCount = steps;
    }

    public int getTotalDieEyes()
    {
        return this.die.getNumber() + this.die2.getNumber();
    }

    public boolean hasDoubleDice()
    {
        return this.die.getNumber() == this.die2.getNumber();
    }

    @SideOnly(Side.CLIENT)
    public void update()
    {
        super.update();

        if (this.diceDelay == 0)
        {
            if (this.diceTimer > 0)
            {
                this.throwDice();

                if (--this.diceTimer == 0)
                {
                    if (this.getCurrentPiece().isInJail())
                    {
                        this.controllable = true;

                        if (this.hasDoubleDice())
                        {
                            this.getCurrentPiece().releaseFromJail();
                            this.endable = true;

                            if (this.useAI())
                            {
                                this.end.onClick();
                            }
                        }
                        else
                        {
                            this.getCurrentPiece().increaseTurnsInJail();

                            if (this.getCurrentPiece().getTurnsInJail() < 3)
                            {
                                this.endable = true;

                                if (this.useAI())
                                {
                                    this.end.onClick();
                                }
                            }
                            else if (this.useAI())
                            {
                                if (this.jail.isVisible() && this.jail.isEnabled())
                                {
                                    this.jail.onClick();
                                }
                                else
                                {
                                    this.bankrupt.onClick();
                                }

                                this.end.onClick();
                            }
                        }
                    }
                    else
                    {
                        this.movePiece(this.getTotalDieEyes());
                    }
                }
            }
            else if (this.diceCount != 0)
            {
                if (this.diceCount > 0)
                {
                    this.getCurrentPiece().move(1);
                    this.places[this.getCurrentPiece().getPosition()].onPiecePass(this.getCurrentPiece());
                    --this.diceCount;
                }
                else
                {
                    this.getCurrentPiece().move(-1);
                    ++this.diceCount;
                }

                if (this.diceCount == 0)
                {
                    if (this.places[this.getCurrentPiece().getPosition()].onPieceStop(this.getCurrentPiece()))
                    {
                        this.endable = true;
                    }

                    this.controllable = true;

                    if (this.useAI())
                    {
                        if (this.purchase.isVisible() && this.purchase.isEnabled())
                        {
                            this.purchase.onClick();
                        }
                        else if (this.card.isVisible() && this.card.isEnabled())
                        {
                            this.card.onClick();
                        }
                        else if (this.rent.isVisible())
                        {
                            if (this.rent.isEnabled())
                            {
                                this.rent.onClick();
                            }
                            else
                            {
                                this.bankrupt.onClick();
                            }
                        }

                        if (this.end.isVisible() && this.end.isEnabled())
                        {
                            this.end.onClick();
                        }
                    }
                }
            }

            this.diceDelay = 3;
        }
        else
        {
            --this.diceDelay;
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        this.loadTexture(gui, 1);
        this.die.draw(gui, 20, 20);
        this.die2.draw(gui, 50, 20);
        float smallgridX = (float)x / 0.17F - 686.94116F;
        float smallgridY = (float)y / 0.17F - 30.117645F;
        boolean foundHover = false;

        if (this.selectedPlace != -1)
        {
            this.drawPropertyOnBoardWithPositionRotationAndScale(gui, this.places[this.selectedPlace], this.selectedPlace, true, false, (int)((590.6666666666666D - (double)(this.getId(this.selectedPlace) == 0 ? 122 : 76)) / 2.0D), 51, 0, 0.75F);
        }

        int id;

        for (id = 0; id < this.places.length; ++id)
        {
            if (!foundHover && this.getModule().inRect((int)smallgridX, (int)smallgridY, this.getSmallgridPlaceArea(id)))
            {
                if (this.selectedPlace == -1)
                {
                    this.drawPropertyOnBoardWithPositionRotationAndScale(gui, this.places[id], id, true, false, (int)((590.6666666666666D - (double)(this.getId(id) == 0 ? 122 : 76)) / 2.0D), 51, 0, 0.75F);
                }

                foundHover = true;
                this.drawPropertyOnBoard(gui, this.places[id], id, this.getSide(id), this.getId(id), true);
            }
            else
            {
                this.drawPropertyOnBoard(gui, this.places[id], id, this.getSide(id), this.getId(id), false);
            }
        }

        int[] var20;

        for (id = 0; id < this.pieces.size(); ++id)
        {
            Piece utility = (Piece)this.pieces.get(id);
            this.loadTexture(gui, 1);
            int[] i = utility.getMenuRect(id);
            this.getModule().drawImage(gui, i, 0, 122);
            int v;

            for (int rect = 0; rect < 3; ++rect)
            {
                v = 0;

                switch (rect)
                {
                    case 0:
                        v = utility.getController() == Piece.CONTROLLED_BY.PLAYER ? 0 : (utility.getController() == Piece.CONTROLLED_BY.COMPUTER ? 1 : 2);
                        break;

                    case 1:
                        v = ((Piece)this.pieces.get(id)).isBankrupt() ? 4 : (this.currentPiece == id ? (this.diceCount == 0 ? (this.diceTimer > 0 ? 3 : 2) : 1) : 0);
                        break;

                    case 2:
                        v = this.getSelectedPlace() != null && this.getSelectedPlace() instanceof Property && ((Property)this.getSelectedPlace()).getOwner() == this.pieces.get(id) ? (((Property)this.getSelectedPlace()).isMortgaged() ? 2 : 1) : 0;
                }

                this.getModule().drawImage(gui, i[0] + 3, i[1] + 3 + rect * 9, rect * 12, 152 + 6 * v, 12, 6);
            }

            var20 = utility.getPlayerMenuRect(id);
            this.getModule().drawImage(gui, var20, 232, 24 * utility.getV());
            Note.drawPlayerValue(this, gui, i[0] + 50, i[1] + 2, utility.getNoteCount());

            for (v = utility.getAnimationNotes().size() - 1; v >= 0; --v)
            {
                NoteAnimation animation = (NoteAnimation)utility.getAnimationNotes().get(v);
                int animX = i[0] + 50 + (6 - animation.getNote().getId()) * 20;

                if (animX + 16 > 443)
                {
                    animX = var20[0] + (var20[2] - 16) / 2;
                }

                if (animation.draw(this, gui, animX, i[1] + 2))
                {
                    utility.removeNewNoteAnimation(v);
                }
            }

            utility.updateExtending(this.getModule().inRect(x, y, i));
        }

        this.loadTexture(gui, 1);
        id = 0;
        Iterator var14 = this.buttons.iterator();

        while (var14.hasNext())
        {
            Button var18 = (Button)var14.next();

            if (var18.isReallyVisible(this))
            {
                var20 = this.getButtonRect(id++);
                byte var21 = 0;

                if (!var18.isReallyEnabled(this))
                {
                    var21 = 1;
                }
                else if (this.getModule().inRect(x, y, var20))
                {
                    var21 = 2;
                }

                this.getModule().drawImage(gui, var20, 152, var21 * 18);
            }
        }

        if (this.getSelectedPlace() != null)
        {
            int var19;

            if (this.getSelectedPlace() instanceof Street)
            {
                Street var15 = (Street)this.getSelectedPlace();
                this.getModule().drawImage(gui, 32, 185, 76, 22, 16, 16);

                if (var15.getOwner() != null && !var15.isMortgaged())
                {
                    if (var15.getStructureCount() == 0)
                    {
                        this.getModule().drawImage(gui, 7, var15.ownsAllInGroup(var15.getOwner()) ? 241 : 226, 124, 22, 5, 10);
                    }
                    else
                    {
                        this.getModule().drawImage(gui, 323, 172 + (var15.getStructureCount() - 1) * 17, 124, 22, 5, 10);
                    }
                }

                for (var19 = 1; var19 <= 5; ++var19)
                {
                    this.drawStreetRent(gui, var15, var19);
                }

                Note.drawValue(this, gui, 62, 170, 3, var15.getMortgageValue());
                Note.drawValue(this, gui, 62, 185, 3, var15.getStructureCost());
                Note.drawValue(this, gui, 62, 222, 3, var15.getRentCost(false));
                Note.drawValue(this, gui, 62, 237, 3, var15.getRentCost(true));
            }
            else if (this.getSelectedPlace() instanceof Station)
            {
                Station var16 = (Station)this.getSelectedPlace();

                if (var16.getOwner() != null && !var16.isMortgaged())
                {
                    this.getModule().drawImage(gui, 323, 184 + (var16.getOwnedInGroup() - 1) * 17, 124, 22, 5, 10);
                }

                Note.drawValue(this, gui, 62, 170, 3, var16.getMortgageValue());

                for (var19 = 1; var19 <= 4; ++var19)
                {
                    this.drawStationRent(gui, var16, var19);
                }
            }
            else if (this.getSelectedPlace() instanceof Utility)
            {
                Utility var17 = (Utility)this.getSelectedPlace();

                if (var17.getOwner() != null && !var17.isMortgaged())
                {
                    this.getModule().drawImage(gui, 323, 184 + (var17.getOwnedInGroup() - 1) * 17, 124, 22, 5, 10);
                }

                Note.drawValue(this, gui, 62, 170, 3, var17.getMortgageValue());

                for (var19 = 1; var19 <= 3; ++var19)
                {
                    this.drawUtilityRent(gui, var17, var19);
                }
            }
        }

        if (this.currentCard != null)
        {
            this.cardScale = Math.min(this.cardScale + 0.02F, 1.0F);
            this.cardRotation = Math.max(0, this.cardRotation - 6);
            this.drawCard(gui, true);
            this.drawCard(gui, false);

            if (this.cardScale == 1.0F && this.useAI())
            {
                this.removeCard();
            }
        }
    }

    private void openCard(Card card)
    {
        this.openedCard = true;
        this.currentCard = card;
        this.cardScale = 0.0F;
        this.cardRotation = 540;
    }

    private void drawCard(GuiMinecart gui, boolean isFront)
    {
        GL11.glPushMatrix();
        short x = 150;
        byte y = 44;
        float s = this.cardScale;
        float posX = (float)(gui.getGuiLeft() + 71);
        float posY = (float)(gui.getGuiTop() + 40);
        GL11.glTranslatef(0.0F, 0.0F, 100.0F);
        GL11.glTranslatef(posX + (float)x, posY + (float)y, 0.0F);
        GL11.glScalef(s, s, 1.0F);
        GL11.glRotatef((float)(this.cardRotation + (isFront ? 0 : 180)), 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-posX, -posY, 0.0F);
        this.loadTexture(gui, 0);
        int[] rect = new int[] {0, 0, 142, 80};
        this.currentCard.render(this, gui, rect, isFront);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui)
    {
        int id = 0;
        Iterator i$ = this.buttons.iterator();

        while (i$.hasNext())
        {
            Button button = (Button)i$.next();

            if (button.isReallyVisible(this))
            {
                this.getModule().drawString(gui, button.getName(), this.getButtonRect(id++), 4210752);
            }
        }

        if (this.getSelectedPlace() != null)
        {
            if (this.getSelectedPlace() instanceof Street)
            {
                this.getModule().drawString(gui, "Mortgage", 10, 175, 4210752);
                this.getModule().drawString(gui, "Buy", 10, 190, 4210752);
                this.getModule().drawString(gui, "Rents", 10, 215, 4210752);
                this.getModule().drawString(gui, "Normal", 14, 227, 4210752);
                this.getModule().drawString(gui, "Group", 14, 242, 4210752);
            }
            else if (this.getSelectedPlace() instanceof Station)
            {
                this.getModule().drawString(gui, "Mortgage", 10, 175, 4210752);
                this.getModule().drawString(gui, "Rents", 330, 170, 4210752);
            }
            else if (this.getSelectedPlace() instanceof Utility)
            {
                this.getModule().drawString(gui, "Mortgage", 10, 175, 4210752);
                this.getModule().drawSplitString(gui, "The rent depends on the eye count of the dice, if you own one Utility it\'s " + Utility.getMultiplier(1) + "x the eye count, if you own two it\'s " + Utility.getMultiplier(2) + "x and if you own them all it\'s " + Utility.getMultiplier(3) + "x.", 10, 195, 145, 4210752);
                this.getModule().drawString(gui, "Rents", 330, 170, 4210752);
            }
        }
    }

    private void drawStreetRent(GuiMinecart gui, Street street, int structures)
    {
        this.loadTexture(gui, 1);
        int graphicalStructures = structures;
        byte u = 0;

        if (structures == 5)
        {
            graphicalStructures = 1;
            u = 1;
        }

        int yPos = 169 + (structures - 1) * 17;

        for (int i = 0; i < graphicalStructures; ++i)
        {
            this.getModule().drawImage(gui, 330 + i * 6, yPos, 76 + u * 16, 22, 16, 16);
        }

        Note.drawValue(this, gui, 370, yPos, 3, street.getRentCost(structures));
    }

    private void drawStationRent(GuiMinecart gui, Station station, int ownedStations)
    {
        this.loadTexture(gui, 1);
        int yPos = 181 + (ownedStations - 1) * 17;

        for (int i = 0; i < ownedStations; ++i)
        {
            this.getModule().drawImage(gui, 330 + i * 16, yPos, 76 + i * 16, 70, 16, 16);
        }

        Note.drawValue(this, gui, 410, yPos, 2, station.getRentCost(ownedStations));
    }

    private void drawUtilityRent(GuiMinecart gui, Utility utility, int utils)
    {
        this.loadTexture(gui, 1);
        int yPos = 181 + (utils - 1) * 17;

        for (int i = 0; i < utils; ++i)
        {
            this.getModule().drawImage(gui, 330 + i * 16, yPos, 76 + i * 16, 86, 16, 16);
        }

        Note.drawValue(this, gui, 400, yPos, 2, utility.getRentCost(utils));
    }

    private int[] getButtonRect(int i)
    {
        return new int[] {10, 50 + i * 22, 80, 18};
    }

    private int getSide(int i)
    {
        return i < 14 ? 0 : (i < 24 ? 1 : (i < 38 ? 2 : 3));
    }

    private int getId(int i)
    {
        return i < 14 ? i : (i < 24 ? i - 14 : (i < 38 ? i - 24 : i - 38));
    }

    private int[] getSmallgridPlaceArea(int id)
    {
        int side = this.getSide(id);
        int i = this.getId(id);

        if (i == 0)
        {
            switch (side)
            {
                case 0:
                    return new int[] {1110, 806, 122, 122};
                case 1:
                    return new int[] {0, 806, 122, 122};
                case 2:
                    return new int[] {0, 0, 122, 122};
                case 3:
                default:
                    return new int[] {1110, 0, 122, 122};
            }
        }
        else
        {
            --i;

            switch (side)
            {
                case 0:
                    return new int[] {122 + (13 - i) * 76 - 76, 806, 76, 122};
                case 1:
                    return new int[] {0, 122 + (9 - i) * 76 - 76, 122, 76};
                case 2:
                    return new int[] {122 + i * 76, 0, 76, 122};
                case 3:
                default:
                    return new int[] {1110, 122 + i * 76, 122, 76};
            }
        }
    }

    private void drawPropertyOnBoard(GuiMinecart gui, Place place, int id, int side, int i, boolean hover)
    {
        int offX;
        int offY;
        short rotation;

        if (i == 0)
        {
            switch (side)
            {
                case 0:
                    offX = 1110;
                    offY = 806;
                    rotation = 0;
                    break;

                case 1:
                    offX = 122;
                    offY = 806;
                    rotation = 90;
                    break;

                case 2:
                    offX = 122;
                    offY = 122;
                    rotation = 180;
                    break;

                case 3:
                default:
                    offX = 1110;
                    offY = 122;
                    rotation = 270;
            }
        }
        else
        {
            --i;

            switch (side)
            {
                case 0:
                    offX = 122 + (13 - i) * 76 - 76;
                    offY = 806;
                    rotation = 0;
                    break;

                case 1:
                    offX = 122;
                    offY = 122 + (9 - i) * 76 - 76;
                    rotation = 90;
                    break;

                case 2:
                    offX = 122 + i * 76 + 76;
                    offY = 122;
                    rotation = 180;
                    break;

                case 3:
                default:
                    offX = 1110;
                    offY = 122 + i * 76 + 76;
                    rotation = 270;
            }
        }

        offX += 686;
        offY += 30;
        this.drawPropertyOnBoardWithPositionRotationAndScale(gui, place, id, false, hover, offX, offY, rotation, 0.17F);
    }

    private void drawPropertyOnBoardWithPositionRotationAndScale(GuiMinecart gui, Place place, int id, boolean zoom, boolean hover, int x, int y, int r, float s)
    {
        GL11.glPushMatrix();
        EnumSet states = EnumSet.noneOf(Place.PLACE_STATE.class);

        if (zoom)
        {
            states.add(Place.PLACE_STATE.ZOOMED);
        }
        else if (hover)
        {
            states.add(Place.PLACE_STATE.HOVER);
        }

        if (this.selectedPlace == id)
        {
            states.add(Place.PLACE_STATE.SELECTED);
        }

        if (place instanceof Property)
        {
            Property posX = (Property)place;

            if (posX.hasOwner() && posX.getOwner().showProperties())
            {
                states.add(Place.PLACE_STATE.MARKED);
            }
        }

        float var17 = (float)gui.getGuiLeft();
        float posY = (float)gui.getGuiTop();
        GL11.glTranslatef(var17 + (float)x * s, posY + (float)y * s, 0.0F);
        GL11.glScalef(s, s, 1.0F);
        GL11.glRotatef((float)r, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-var17, -posY, 0.0F);
        place.draw(gui, states);
        int[] total = new int[place.getPieceAreaCount()];

        for (int pos = 0; pos < this.pieces.size(); ++pos)
        {
            if (!((Piece)this.pieces.get(pos)).isBankrupt() && ((Piece)this.pieces.get(pos)).getPosition() == id)
            {
                ++total[place.getPieceAreaForPiece((Piece)this.pieces.get(pos))];
            }
        }

        int[] var18 = new int[place.getPieceAreaCount()];

        for (int i = 0; i < this.pieces.size(); ++i)
        {
            if (!((Piece)this.pieces.get(i)).isBankrupt() && ((Piece)this.pieces.get(i)).getPosition() == id)
            {
                this.loadTexture(gui, 1);
                int area = place.getPieceAreaForPiece((Piece)this.pieces.get(i));
                place.drawPiece(gui, (Piece)this.pieces.get(i), total[area], var18[area]++, area, states);
            }
        }

        place.drawText(gui, states);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int b)
    {
        float smallgridX = (float)x / 0.17F - 686.94116F;
        float smallgridY = (float)y / 0.17F - 30.117645F;
        int id;

        for (id = 0; id < this.places.length; ++id)
        {
            if (this.getModule().inRect((int)smallgridX, (int)smallgridY, this.getSmallgridPlaceArea(id)))
            {
                if (this.places[id] instanceof Property)
                {
                    if (id == this.selectedPlace)
                    {
                        this.selectedPlace = -1;
                    }
                    else
                    {
                        this.selectedPlace = id;
                    }

                    return;
                }

                break;
            }
        }

        id = 0;
        Iterator rect = this.buttons.iterator();
        Button button;

        do
        {
            if (!rect.hasNext())
            {
                if (this.currentCard != null && this.cardScale == 1.0F)
                {
                    int[] var10 = new int[] {150, 44, 142, 80};

                    if (this.getModule().inRect(x, y, var10))
                    {
                        this.removeCard();
                    }
                }

                this.selectedPlace = -1;
                return;
            }

            button = (Button)rect.next();
        }
        while (!button.isReallyVisible(this) || !this.getModule().inRect(x, y, this.getButtonRect(id++)));

        if (button.isReallyEnabled(this))
        {
            button.onClick();
        }
    }

    public void loadTexture(GuiMinecart gui, int number)
    {
        ResourceHelper.bindResource(textures[number]);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Place[] getPlaces()
    {
        return this.places;
    }

    private void removeCard()
    {
        this.currentCard.doStuff(this, this.getCurrentPiece());
        this.currentCard = null;
        this.endable = true;

        if (this.diceCount == 0 && this.useAI())
        {
            this.end.onClick();
        }
    }

    static
    {
        for (int i = 0; i < textures.length; ++i)
        {
            textures[i] = "/gui/monopoly_" + i + ".png";
        }
    }
}
