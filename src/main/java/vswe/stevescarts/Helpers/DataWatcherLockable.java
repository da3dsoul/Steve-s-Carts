package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;

public class DataWatcherLockable extends DataWatcher
{
    private boolean isLocked = true;
    private List lockedList;

    public DataWatcherLockable(Entity p_i45313_1_)
    {
        super(p_i45313_1_);
    }

    public void release()
    {
        this.isLocked = false;

        if (this.lockedList != null)
        {
            this.updateWatchedObjectsFromList(this.lockedList);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateWatchedObjectsFromList(List lst)
    {
        if (this.isLocked)
        {
            this.lockedList = lst;
        }
        else
        {
            super.updateWatchedObjectsFromList(lst);
        }
    }
}
