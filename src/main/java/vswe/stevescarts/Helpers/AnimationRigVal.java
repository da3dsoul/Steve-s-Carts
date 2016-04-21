package vswe.stevescarts.Helpers;

public class AnimationRigVal
{
    private float val;
    private float min;
    private float max;
    private float speed;
    private AnimationRigVal down;
    private AnimationRigVal up;

    public AnimationRigVal(AnimationRig rig, float min, float max, float speed)
    {
        this.min = min;
        this.max = max;
        this.speed = speed;
        this.val = this.min;
        rig.addVal(this);
    }

    public void setUp(AnimationRigVal up)
    {
        this.up = up;
    }

    public void setDown(AnimationRigVal down)
    {
        this.down = down;
    }

    public void setUpAndDown(AnimationRigVal up)
    {
        this.setUp(up);
        up.setDown(this);
    }

    public float getVal()
    {
        return this.val;
    }

    public boolean update(boolean goDown)
    {
        float target = goDown ? this.min : this.max;

        if (target == this.val)
        {
            return false;
        }
        else
        {
            if (this.val < target)
            {
                this.val += this.speed;

                if (this.val > target)
                {
                    this.val = target;
                }
            }
            else if (this.val > target)
            {
                this.val -= this.speed;

                if (this.val < target)
                {
                    this.val = target;
                }
            }

            if (goDown)
            {
                if (this.down != null)
                {
                    this.down.update(goDown);
                }
            }
            else if (this.up != null)
            {
                this.up.update(goDown);
            }

            return true;
        }
    }

    public void setSpeedToSync(AnimationRigVal syncTo, boolean invert)
    {
        this.speed = (this.max - this.min) / ((syncTo.max - syncTo.min) / syncTo.speed);

        if (invert)
        {
            this.speed *= -1.0F;
        }
    }
}
