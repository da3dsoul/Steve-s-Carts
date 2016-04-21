package vswe.stevescarts.Arcade;

import java.util.ArrayList;
import java.util.Iterator;

public class Piece
{
    private ArcadeMonopoly game;
    private int pos;
    private int u;
    private int extended;
    private int[] money;
    private Piece.CONTROLLED_BY control;
    private ArrayList<NoteAnimation> animationNotes;
    private ArrayList<NoteAnimation> oldnotes;
    private boolean bankrupt;
    private int turnsInJail;

    public Piece(ArcadeMonopoly game, int u, Piece.CONTROLLED_BY control)
    {
        this.game = game;
        this.pos = 0;
        this.u = u;
        this.money = new int[] {30, 30, 30, 30, 30, 30, 30};
        this.control = control;
        this.animationNotes = new ArrayList();
        this.oldnotes = new ArrayList();
        this.turnsInJail = -1;
    }

    public void move(int dif)
    {
        this.pos = (this.pos + dif) % 48;
    }

    public int getPosition()
    {
        return this.pos;
    }

    public int getV()
    {
        return this.u;
    }

    public int[] getNoteCount()
    {
        return this.money;
    }

    public int getNoteCount(Note note)
    {
        int money = this.money[note.getId()];

        for (int i = 0; i < this.oldnotes.size(); ++i)
        {
            if (note == ((NoteAnimation)this.oldnotes.get(i)).getNote())
            {
                --money;
            }
        }

        return money;
    }

    public int getTotalMoney()
    {
        int money = 0;
        int i;

        for (i = 0; i < Note.notes.size(); ++i)
        {
            money += ((Note)Note.notes.get(i)).getUnits() * this.money[i];
        }

        for (i = 0; i < this.oldnotes.size(); ++i)
        {
            money -= ((NoteAnimation)this.oldnotes.get(i)).getNote().getUnits();
        }

        return money;
    }

    public void addMoney(int money, boolean useAnimation)
    {
        for (int i = Note.notes.size() - 1; i >= 0; --i)
        {
            Note note = (Note)Note.notes.get(i);
            int notesToAdd = money / note.getUnits();

            if (notesToAdd > 0)
            {
                this.addMoney(note, notesToAdd, true);
                money -= notesToAdd * note.getUnits();
            }

            if (money == 0)
            {
                return;
            }
        }
    }

    public void addMoney(Note note, int amount, boolean useAnimation)
    {
        if (useAnimation)
        {
            int min = 10;
            Iterator i = this.animationNotes.iterator();

            while (i.hasNext())
            {
                NoteAnimation animation = (NoteAnimation)i.next();

                if (animation.getAnimation() < min)
                {
                    min = animation.getAnimation();
                }
            }

            for (int var7 = 0; var7 < amount; ++var7)
            {
                this.animationNotes.add(0, new NoteAnimation(note, min - 10, true));
                min -= 10;
            }
        }
        else
        {
            int[] var10000 = this.money;
            int var10001 = note.getId();
            var10000[var10001] += amount;
        }
    }

    public void removeNewNoteAnimation(int i)
    {
        if (((NoteAnimation)this.animationNotes.get(i)).isNew())
        {
            this.addMoney(((NoteAnimation)this.animationNotes.get(i)).getNote(), 1, false);
        }
        else
        {
            Note note = ((NoteAnimation)this.animationNotes.get(i)).getNote();

            for (int j = this.oldnotes.size() - 1; j >= 0; --j)
            {
                if (note == ((NoteAnimation)this.oldnotes.get(j)).getNote())
                {
                    this.oldnotes.remove(j);
                    break;
                }
            }

            this.removeMoney(note, 1, false);
        }

        this.animationNotes.remove(i);
    }

    public ArrayList<NoteAnimation> getAnimationNotes()
    {
        return this.animationNotes;
    }

    public boolean removeMoney(int money, boolean useAnimation)
    {
        int[] noteCounts = new int[Note.notes.size()];
        int[] moneyBelowThisLevel = new int[Note.notes.size()];
        int totalmoney = 0;
        int i;

        for (i = 0; i < noteCounts.length; ++i)
        {
            noteCounts[i] = this.getNoteCount((Note)Note.notes.get(i));
            moneyBelowThisLevel[i] = totalmoney;
            totalmoney += noteCounts[i] * ((Note)Note.notes.get(i)).getUnits();
        }

        if (totalmoney >= money)
        {
            for (i = Note.notes.size() - 1; i >= 0; --i)
            {
                Note note = (Note)Note.notes.get(i);
                int notesToRemove = money / note.getUnits();
                notesToRemove = Math.min(notesToRemove, noteCounts[i]);
                this.removeMoney(note, notesToRemove, useAnimation);
                money -= note.getUnits() * notesToRemove;

                if (money == 0)
                {
                    return true;
                }

                if (moneyBelowThisLevel[i] < money)
                {
                    this.removeMoney(note, 1, useAnimation);
                    money -= note.getUnits();
                    this.addMoney(-money, useAnimation);
                    return true;
                }
            }
        }

        return false;
    }

    private void removeMoney(Note note, int amount, boolean useAnimation)
    {
        if (useAnimation)
        {
            int min = 10;
            Iterator i = this.animationNotes.iterator();
            NoteAnimation animation;

            while (i.hasNext())
            {
                animation = (NoteAnimation)i.next();

                if (animation.getAnimation() < min)
                {
                    min = animation.getAnimation();
                }
            }

            for (int var7 = 0; var7 < amount; ++var7)
            {
                animation = new NoteAnimation(note, min - 10, false);
                this.animationNotes.add(0, animation);
                this.oldnotes.add(0, animation);
                min -= 10;
            }
        }
        else
        {
            int[] var10000 = this.money;
            int var10001 = note.getId();
            var10000[var10001] -= amount;
        }
    }

    public int[] getMenuRect(int i)
    {
        int w = 50 + this.extended;
        return new int[] {443 - w, 10 + i * 30, w, 30};
    }

    public int[] getPlayerMenuRect(int i)
    {
        int[] menu = this.getMenuRect(i);
        return new int[] {menu[0] + 19, menu[1] + 3, 24, 24};
    }

    public void updateExtending(boolean inRect)
    {
        if (inRect && this.extended < 175)
        {
            this.extended = Math.min(175, this.extended + 20);
        }
        else if (!inRect && this.extended > 0)
        {
            this.extended = Math.max(0, this.extended - 50);
        }
    }

    public Piece.CONTROLLED_BY getController()
    {
        return this.control;
    }

    public boolean showProperties()
    {
        return this == this.game.getCurrentPiece();
    }

    public boolean canAffordProperty(Property property)
    {
        return this.getTotalMoney() >= property.getCost();
    }

    public void purchaseProperty(Property property)
    {
        if (this.removeMoney(property.getCost(), true))
        {
            property.setOwner(this);
        }
        else
        {
            System.out.println("Couldn\'t remove the resources, this is very weird :S");
        }
    }

    public void bankrupt(Piece owesMoneyToThis)
    {
        int money = this.getTotalMoney();
        this.removeMoney(money, true);

        if (owesMoneyToThis != null)
        {
            owesMoneyToThis.addMoney(money, true);
        }

        Place[] arr$ = this.game.getPlaces();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Place place = arr$[i$];

            if (place instanceof Property)
            {
                Property property = (Property)place;

                if (property.getOwner() == this)
                {
                    property.setOwner(owesMoneyToThis);
                }
            }
        }

        this.bankrupt = true;
    }

    public boolean canAffordRent(Property property)
    {
        return this.getTotalMoney() >= property.getRentCost();
    }

    public void payPropertyRent(Property property)
    {
        if (this.removeMoney(property.getRentCost(), true))
        {
            property.getOwner().addMoney(property.getRentCost(), true);
        }
        else
        {
            System.out.println("Couldn\'t remove the resources, this is very weird :S");
        }
    }

    public boolean isBankrupt()
    {
        return this.bankrupt;
    }

    public boolean canAffordStructure(Street street)
    {
        return this.getTotalMoney() >= street.getStructureCost();
    }

    public void buyStructure(Street street)
    {
        if (this.removeMoney(street.getStructureCost(), true))
        {
            street.increaseStructure();
        }
        else
        {
            System.out.println("Couldn\'t remove the resources, this is very weird :S");
        }
    }

    public boolean isInJail()
    {
        return this.turnsInJail >= 0;
    }

    public void goToJail()
    {
        this.turnsInJail = 0;
        this.pos = 14;
    }

    public void releaseFromJail()
    {
        this.turnsInJail = -1;
    }

    public void increaseTurnsInJail()
    {
        ++this.turnsInJail;
    }

    public int getTurnsInJail()
    {
        return this.turnsInJail;
    }

    public void payFine()
    {
        if (this.removeMoney(50, true))
        {
            this.releaseFromJail();
        }
        else
        {
            System.out.println("Couldn\'t remove the resources, this is very weird :S");
        }
    }

    public boolean canAffordFine()
    {
        return this.getTotalMoney() >= 50;
    }

    public void getMoneyFromMortgage(Property selectedPlace)
    {
        this.addMoney(selectedPlace.getMortgageValue(), true);
        selectedPlace.mortgage();
    }

    public boolean canAffordUnMortgage(Property selectedPlace)
    {
        return this.getTotalMoney() >= selectedPlace.getUnMortgagePrice();
    }

    public void payUnMortgage(Property selectedPlace)
    {
        if (this.removeMoney(selectedPlace.getUnMortgagePrice(), true))
        {
            selectedPlace.unMortgage();
        }
        else
        {
            System.out.println("Couldn\'t remove the resources, this is very weird :S");
        }
    }

    public void sellStructure(Street selectedPlace)
    {
        this.addMoney(selectedPlace.getStructureSellPrice(), true);
        selectedPlace.decreaseStructures();
    }

    public static enum CONTROLLED_BY
    {
        PLAYER("PLAYER", 0),
        COMPUTER("COMPUTER", 1),
        OTHER("OTHER", 2);

        private static final Piece.CONTROLLED_BY[] $VALUES = new Piece.CONTROLLED_BY[]{PLAYER, COMPUTER, OTHER};

        private CONTROLLED_BY(String var1, int var2) {}
    }
}
