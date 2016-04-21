package vswe.stevescarts.Helpers;

import vswe.stevescarts.Interfaces.GuiDetector;

public class DropDownMenuPages extends DropDownMenu
{
    private int page = 0;
    private int maxPages;
    private int[] leftArrow = new int[] {20, 20, 5, 7};
    private int[] rightArrow = new int[] {70, 20, 5, 7};

    public DropDownMenuPages(int index, int max)
    {
        super(index);
        this.maxPages = max;
    }

    protected int getCurrentId(int index, int objects)
    {
        return index - objects * this.page;
    }

    public void drawMain(GuiDetector gui, int x, int y)
    {
        super.drawMain(gui, x, y);
        this.drawObject(gui, x, y, new int[] {30, 20, 23, 7}, 0, 170, 0, 0);
        this.drawObject(gui, x, y, new int[] {60, 20, 5, 7}, 24 + 6 * this.page, 170, 0, 0);
        this.drawObject(gui, x, y, this.leftArrow, 0, 177, 5, 0);
        this.drawObject(gui, x, y, this.rightArrow, 0, 184, 5, 0);
    }

    private void drawObject(GuiDetector gui, int x, int y, int[] rect, int srcX, int srcY, int hoverDifX, int hoverDifY)
    {
        rect = new int[] {rect[0], rect[1], rect[2], rect[3]};
        rect[1] += 20 + this.getScroll() - 170;
        int gap = rect[1] - this.getMainRect()[1] + rect[3];

        if (gap > 0)
        {
            int height = Math.min(rect[3], gap);
            int offset = rect[3] - height;
            rect[3] = height;

            if (gui.inRect(x, y, rect))
            {
                srcX += hoverDifX;
                srcY += hoverDifY;
            }

            gui.drawTexturedModalRect(gui.getGuiLeft() + rect[0], gui.getGuiTop() + rect[1] + offset, srcX, srcY + offset, rect[2], rect[3]);
        }
    }

    public void onClick(GuiDetector gui, int x, int y)
    {
        if (this.clicked(gui, x, y, this.leftArrow))
        {
            --this.page;

            if (this.page < 0)
            {
                this.page = this.maxPages - 1;
            }
        }
        else if (this.clicked(gui, x, y, this.rightArrow))
        {
            ++this.page;

            if (this.page >= this.maxPages)
            {
                this.page = 0;
            }
        }
    }

    private boolean clicked(GuiDetector gui, int x, int y, int[] rect)
    {
        rect = new int[] {rect[0], rect[1], rect[2], rect[3]};
        rect[1] += 20 + this.getScroll() - 170;
        int gap = rect[1] - this.getMainRect()[1] + rect[3];

        if (gap > 0)
        {
            rect[3] = Math.min(rect[3], gap);
            return gui.inRect(x, y, rect);
        }
        else
        {
            return false;
        }
    }
}
