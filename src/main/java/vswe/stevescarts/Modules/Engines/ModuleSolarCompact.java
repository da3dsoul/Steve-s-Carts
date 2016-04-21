package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.AnimationRig;
import vswe.stevescarts.Helpers.AnimationRigVal;

public class ModuleSolarCompact extends ModuleSolarBase
{
    private AnimationRig rig = new AnimationRig();
    private AnimationRigVal extraction;
    private AnimationRigVal topbot;
    private AnimationRigVal leftright;
    private AnimationRigVal corner;
    private AnimationRigVal angle;
    private AnimationRigVal extraction2;
    private AnimationRigVal innerextraction;

    public ModuleSolarCompact(MinecartModular cart)
    {
        super(cart);
        this.extraction = new AnimationRigVal(this.rig, 0.4F, 2.0F, 0.1F);
        this.topbot = new AnimationRigVal(this.rig, 0.1F, 4.0F, 0.25F);
        this.leftright = new AnimationRigVal(this.rig, 0.01F, 6.0F, 0.2F);
        this.corner = new AnimationRigVal(this.rig, 0.1F, 4.0F, 0.25F);
        this.extraction2 = new AnimationRigVal(this.rig, 0.0F, 1.8F, 0.1F);
        this.innerextraction = new AnimationRigVal(this.rig, 0.4F, 3.0F, 0.2F);
        this.angle = new AnimationRigVal(this.rig, 0.0F, ((float)Math.PI / 2F), 0.1F);
        this.innerextraction.setUpAndDown(this.angle);
    }

    protected int getMaxCapacity()
    {
        return 25000;
    }

    protected int getGenSpeed()
    {
        return 5;
    }

    public boolean updatePanels()
    {
        return this.rig.update(this.isGoingDown());
    }

    public float getExtractionDist()
    {
        return this.extraction.getVal() + this.extraction2.getVal();
    }

    public float getTopBotExtractionDist()
    {
        return this.topbot.getVal();
    }

    public float getLeftRightExtractionDist()
    {
        return this.leftright.getVal();
    }

    public float getCornerExtractionDist()
    {
        return this.corner.getVal();
    }

    public float getPanelAngle()
    {
        return this.angle.getVal();
    }

    public float getInnerExtraction()
    {
        return this.innerextraction.getVal();
    }
}
