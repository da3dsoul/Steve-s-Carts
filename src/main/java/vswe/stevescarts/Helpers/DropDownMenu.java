package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.IIcon;
import vswe.stevescarts.Interfaces.GuiDetector;

@SideOnly(Side.CLIENT)
public class DropDownMenu
{
    public static final int SCROLL_HEIGHT = 170;
    public static final int SCROLL_HEADER_HEIGHT = 14;
    public static final int SCROLL_TOP_MARGIN = 20;
    public static final int TAB_COUNT = 3;
    private int moduleScroll;
    private int index;
    private boolean forceGoUp;

    public DropDownMenu(int index)
    {
        this.index = index;
        this.moduleScroll = 0;
    }

    public static void update(GuiDetector gui, int x, int y, ArrayList<DropDownMenu> menus)
    {
        Iterator i$;
        DropDownMenu menu;

        if (gui.currentObject == null)
        {
            i$ = menus.iterator();

            while (i$.hasNext())
            {
                menu = (DropDownMenu)i$.next();

                if (gui.inRect(x, y, menu.getHeaderRect()))
                {
                    menu.forceGoUp = false;
                    menu.update(true);
                    Iterator i$1 = menus.iterator();

                    while (i$1.hasNext())
                    {
                        DropDownMenu menu2 = (DropDownMenu)i$1.next();

                        if (!menu.equals(menu2))
                        {
                            menu2.forceGoUp = true;
                            menu2.update(false);
                        }
                    }

                    return;
                }
            }

            i$ = menus.iterator();

            while (i$.hasNext())
            {
                menu = (DropDownMenu)i$.next();
                menu.update(gui.inRect(x, y, menu.getMainRect()));
            }
        }
        else
        {
            i$ = menus.iterator();

            while (i$.hasNext())
            {
                menu = (DropDownMenu)i$.next();
                menu.update(false);
            }
        }
    }

    private void update(boolean hasFocus)
    {
        if (!this.forceGoUp && hasFocus)
        {
            if (this.moduleScroll < 156)
            {
                this.moduleScroll += 10;

                if (this.moduleScroll > 156)
                {
                    this.moduleScroll = 156;
                }
            }
        }
        else if (this.moduleScroll > 0)
        {
            this.moduleScroll -= 25;

            if (this.moduleScroll <= 0)
            {
                this.moduleScroll = 0;
                this.forceGoUp = false;
            }
        }
    }

    public void drawMain(GuiDetector gui, int x, int y)
    {
        ResourceHelper.bindResource(GuiDetector.dropdownTexture);
        int[] rect = this.getMainRect();
        gui.drawTexturedModalRect(gui.getGuiLeft() + rect[0], gui.getGuiTop() + rect[1], 0, 156 - this.moduleScroll, rect[2], rect[3]);
    }

    public void drawHeader(GuiDetector gui)
    {
        ResourceHelper.bindResource(GuiDetector.dropdownTexture);
        int[] rect = this.getHeaderRect();
        gui.drawTexturedModalRect(gui.getGuiLeft() + rect[0], gui.getGuiTop() + rect[1], 77 * this.index, 156, rect[2], rect[3]);
    }

    public void drawContent(GuiDetector gui, int index, int srcX, int srcY)
    {
        int[] rect = this.getContentRect(index);

        if (rect != null)
        {
            int gap = rect[1] - this.getMainRect()[1] + rect[3];

            if (gap > 0)
            {
                int height = Math.min(rect[3], gap);
                int offset = rect[3] - height;
                gui.drawTexturedModalRect(gui.getGuiLeft() + rect[0], gui.getGuiTop() + rect[1] + offset, srcX, srcY + offset, rect[2], height);
            }
        }
    }

    public void drawContent(GuiDetector gui, int index, IIcon icon)
    {
        int[] rect = this.getContentRect(index);

        if (rect != null)
        {
            int gap = rect[1] - this.getMainRect()[1] + rect[3];

            if (gap > 0)
            {
                int height = Math.min(rect[3], gap);
                int offset = rect[3] - height;
                gui.drawIcon(icon, gui.getGuiLeft() + rect[0], gui.getGuiTop() + rect[1] + offset, (float)rect[2] / 16.0F, (float)height / 16.0F, 0.0F, (float)offset / 16.0F);
            }
        }
    }

    public int[] getContentRect(int posId)
    {
        byte objectsPerRow = 11;
        byte objectsRows = 7;
        byte objectWidth = 16;
        byte objectHeight = 16;
        byte objectY = 31;

        if (this.index == 2)
        {
            objectsPerRow = 9;
            objectsRows = 10;
            objectWidth = 20;
            objectHeight = 11;
            objectY = 34;
        }

        posId = this.getCurrentId(posId, objectsPerRow * objectsRows);

        if (posId >= 0 && posId < objectsPerRow * objectsRows)
        {
            int x = posId % objectsPerRow;
            int y = posId / objectsPerRow;
            int targetX = x * (objectWidth + 3) + 25;
            int targetY = y * (objectHeight + 3) + 20 + objectY + this.getScroll() - 170;
            return new int[] {targetX, targetY, objectWidth, objectHeight};
        }
        else
        {
            return null;
        }
    }

    public int[] getMainRect()
    {
        return new int[] {11, 20, 232, this.moduleScroll};
    }

    public int[] getHeaderRect()
    {
        return new int[] {11 + 77 * this.index, 20 + this.moduleScroll, (int)Math.ceil(77.33333587646484D), 14};
    }

    public int getScroll()
    {
        return this.moduleScroll;
    }

    protected int getCurrentId(int index, int objects)
    {
        return index;
    }

    public void onClick(GuiDetector gui, int x, int y) {}
}
