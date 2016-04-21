package vswe.stevescarts.Helpers;

import java.util.ArrayList;

public class AnimationRig
{
    private ArrayList<AnimationRigVal> rigs = new ArrayList();

    public boolean update(boolean goDown)
    {
        int i;

        if (goDown)
        {
            for (i = this.rigs.size() - 1; i >= 0; --i)
            {
                if (((AnimationRigVal)this.rigs.get(i)).update(goDown))
                {
                    return false;
                }
            }

            return false;
        }
        else
        {
            for (i = 0; i < this.rigs.size(); ++i)
            {
                if (((AnimationRigVal)this.rigs.get(i)).update(goDown))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public void addVal(AnimationRigVal val)
    {
        this.rigs.add(val);
    }
}
