package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public abstract class Card
{
    private String message;

    public Card(String message)
    {
        this.message = message;
    }

    public void render(ArcadeMonopoly game, GuiMinecart gui, int[] rect, boolean isFront)
    {
        if (isFront)
        {
            game.loadTexture(gui, 1);
            game.getModule().drawImage(gui, rect, 67, 177);
            game.getModule().drawSplitString(gui, this.message, rect[0] + gui.getGuiLeft() + 5, rect[1] + gui.getGuiTop() + 5, rect[2] - 10, true, 4210752);

            if (this.getNote() != null)
            {
                int x = 10;

                if (!this.getMoneyPrefix().equals(""))
                {
                    game.getModule().drawString(gui, this.getMoneyPrefix(), x, 64, 4210752);
                    x += gui.getFontRenderer().getStringWidth(this.getMoneyPrefix()) + 5;
                }

                this.getNote().draw(game, gui, x, 59, this.getNoteCount());
                x += 31;

                if (!this.getMoneyPostfix().equals(""))
                {
                    game.getModule().drawString(gui, this.getMoneyPostfix(), x, 64, 4210752);
                }
            }
        }
        else
        {
            game.getModule().drawImage(gui, rect, 0, rect[3] * this.getBackgroundV());
        }
    }

    public int getNoteCount()
    {
        return 0;
    }

    public Note getNote()
    {
        return null;
    }

    public String getMoneyPrefix()
    {
        return "";
    }

    public String getMoneyPostfix()
    {
        return "";
    }

    public abstract void doStuff(ArcadeMonopoly var1, Piece var2);

    public abstract int getBackgroundV();
}
