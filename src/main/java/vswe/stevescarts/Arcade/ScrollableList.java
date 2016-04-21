package vswe.stevescarts.Arcade;

import java.util.ArrayList;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ScrollableList
{
    private int x;
    private int y;
    private ArcadeTracks game;
    private ArrayList<String> items;
    private int scrollPosition;
    private boolean isScrolling;
    private int selectedIndex = -1;

    public ScrollableList(ArcadeTracks game, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.game = game;
        this.items = new ArrayList();
    }

    public void clearList()
    {
        this.items.clear();
    }

    public void clear()
    {
        this.selectedIndex = -1;
        this.scrollPosition = 0;
    }

    public void add(String str)
    {
        this.items.add(str);
    }

    public boolean isVisible()
    {
        return true;
    }

    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }

    public void onClick() {}

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        if (this.isVisible())
        {
            int[] menu = this.game.getMenuArea();
            this.game.getModule().drawImage(gui, menu[0] + this.x, menu[1] + this.y, 0, 192, 132, 64);

            for (int area = 0; area < this.items.size(); ++area)
            {
                int[] rect = this.getLevelButtonArea(area);

                if (rect[3] > 0)
                {
                    int srcY = 188 + (this.items.get(area) == null ? 34 : (this.game.getModule().inRect(x, y, rect) ? 17 : 0));
                    int borderSrcY = 239;

                    if (rect[4] < 0)
                    {
                        srcY -= rect[4];
                        borderSrcY -= rect[4];
                    }

                    this.game.getModule().drawImage(gui, rect, 146, srcY);

                    if (area == this.selectedIndex)
                    {
                        this.game.getModule().drawImage(gui, rect, 146, borderSrcY);
                    }
                }
            }

            int[] var9 = this.getScrollArea();
            this.game.getModule().drawImage(gui, var9[0], var9[1] + this.scrollPosition, 132, 256 - (this.items.size() >= 4 ? 32 : 16), 14, 16);
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        if (this.isVisible())
        {
            for (int i = 0; i < this.items.size(); ++i)
            {
                int[] rect = this.getLevelButtonArea(i);
                int x = rect[0] + 4;
                int y = rect[1] + 5;

                if (rect[4] < 0)
                {
                    y += rect[4];
                }

                if (rect[4] >= -5 && rect[4] <= 48)
                {
                    this.game.getModule().drawString(gui, this.items.get(i) == null ? "<???>" : (String)this.items.get(i), x, y, 4210752);
                }
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (this.isVisible())
        {
            if (this.isScrolling)
            {
                if (button != -1)
                {
                    this.isScrolling = false;
                }
                else
                {
                    this.doScroll(y);
                }
            }
        }
    }

    private void doScroll(int y)
    {
        int[] area = this.getScrollArea();
        this.scrollPosition = y - area[1] - 8;

        if (this.scrollPosition < 0)
        {
            this.scrollPosition = 0;
        }
        else if (this.scrollPosition > 42)
        {
            this.scrollPosition = 42;
        }
    }

    private int getScrollLevel()
    {
        int totalSize = this.items.size() * 18;
        byte availableSpace = 60;
        int canNotFit = totalSize - availableSpace;
        int scrollLength = this.getScrollArea()[3] - 16;
        return (int)((float)canNotFit * ((float)this.scrollPosition / (float)scrollLength));
    }

    private int[] getLevelButtonArea(int id)
    {
        int[] menu = this.game.getMenuArea();
        int offSetY = 18 * id - this.getScrollLevel();
        int height = 17;
        int y = menu[1] + this.y + 2 + offSetY;

        if (offSetY < 0)
        {
            height += offSetY;
            y -= offSetY;
        }
        else if (offSetY + height > 60)
        {
            height = 60 - offSetY;
        }

        return new int[] {menu[0] + 2 + this.x, y, 108, height, offSetY};
    }

    private int[] getScrollArea()
    {
        int[] menu = this.game.getMenuArea();
        return new int[] {menu[0] + this.x + 116, menu[1] + this.y + 3, 14, 58};
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (this.isVisible())
        {
            for (int i = 0; i < this.items.size(); ++i)
            {
                if (this.items.get(i) != null)
                {
                    int[] rect = this.getLevelButtonArea(i);

                    if (rect[3] > 0 && this.game.getModule().inRect(x, y, rect))
                    {
                        if (this.selectedIndex == i)
                        {
                            this.selectedIndex = -1;
                        }
                        else
                        {
                            this.selectedIndex = i;
                        }

                        this.onClick();
                        break;
                    }
                }
            }

            if (this.items.size() >= 4 && this.game.getModule().inRect(x, y, this.getScrollArea()))
            {
                this.doScroll(y);
                this.isScrolling = true;
            }
        }
    }
}
