package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class NoteAnimation
{
    private Note note;
    private int animation;
    private boolean isNew;

    public NoteAnimation(Note note, int start, boolean isNew)
    {
        this.note = note;
        this.animation = start;
        this.isNew = isNew;
    }

    public boolean draw(ArcadeMonopoly game, GuiMinecart gui, int x, int y)
    {
        if (this.animation >= 0)
        {
            if (this.isNew)
            {
                this.note.draw(game, gui, x, y - 10 + this.animation / 2);
            }
            else
            {
                this.note.draw(game, gui, x, y + this.animation);
            }
        }

        return ++this.animation > 20;
    }

    public Note getNote()
    {
        return this.note;
    }

    public int getAnimation()
    {
        return this.animation;
    }

    public boolean isNew()
    {
        return this.isNew;
    }
}
