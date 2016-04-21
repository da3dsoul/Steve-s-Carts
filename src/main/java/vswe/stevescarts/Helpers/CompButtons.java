package vswe.stevescarts.Helpers;

import java.util.Comparator;
import vswe.stevescarts.Buttons.ButtonBase;

public class CompButtons implements Comparator
{
    public int compare(Object obj1, Object obj2)
    {
        ButtonBase button1 = (ButtonBase)obj1;
        ButtonBase button2 = (ButtonBase)obj2;

        if (!button1.isVisible() && !button2.isVisible())
        {
            return 0;
        }
        else if (!button1.isVisible())
        {
            return 1;
        }
        else if (!button2.isVisible())
        {
            return -1;
        }
        else
        {
            int location1 = button1.getLocationID();
            int location2 = button2.getLocationID();

            if (location1 != location2)
            {
                return location1 < location2 ? -1 : 1;
            }
            else
            {
                int id1 = button1.getIdInModule();
                int id2 = button1.getIdInModule();
                return id1 == id2 ? 0 : (id1 < id2 ? -1 : 1);
            }
        }
    }
}
