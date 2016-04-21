package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public abstract class ModuleSolarTop extends ModuleSolarBase
{
    private float minVal = -4.0F;
    private float maxVal = -13.0F;
    private float minAngle = 0.0F;
    private float maxAngle = ((float)Math.PI / 2F);
    private float innerRotation = 0.0F;
    private float movingLevel;

    public ModuleSolarTop(MinecartModular cart)
    {
        super(cart);
        this.movingLevel = this.minVal;
    }

    public float getInnerRotation()
    {
        return this.innerRotation;
    }

    public float getMovingLevel()
    {
        return this.movingLevel;
    }

    public boolean updatePanels()
    {
        if (this.movingLevel > this.minVal)
        {
            this.movingLevel = this.minVal;
        }

        if (this.innerRotation < this.minAngle)
        {
            this.innerRotation = this.minAngle;
        }
        else if (this.innerRotation > this.maxAngle)
        {
            this.innerRotation = this.maxAngle;
        }

        float targetAngle = this.isGoingDown() ? this.minAngle : this.maxAngle;

        if (this.movingLevel > this.maxVal && this.innerRotation != targetAngle)
        {
            this.movingLevel -= 0.2F;

            if (this.movingLevel <= this.maxVal)
            {
                this.movingLevel = this.maxVal;
            }
        }
        else if (this.innerRotation != targetAngle)
        {
            this.innerRotation += this.isGoingDown() ? -0.05F : 0.05F;

            if (!this.isGoingDown() && this.innerRotation >= targetAngle || this.isGoingDown() && this.innerRotation <= targetAngle)
            {
                this.innerRotation = targetAngle;
            }
        }
        else if (this.movingLevel < this.minVal)
        {
            this.movingLevel += 0.2F;

            if (this.movingLevel >= this.minVal)
            {
                this.movingLevel = this.minVal;
            }
        }

        return this.innerRotation == this.maxAngle;
    }

    protected abstract int getPanelCount();
}
