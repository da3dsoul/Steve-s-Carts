package vswe.stevescarts.Modules.Realtimers;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.AnimationRig;
import vswe.stevescarts.Helpers.AnimationRigVal;

public class ModuleShooterAdvSide extends ModuleShooterAdv
{
    private AnimationRig rig = new AnimationRig();
    private AnimationRigVal handlePos;
    private AnimationRigVal basePos;
    private AnimationRigVal handleRot;
    private AnimationRigVal gunRot;
    private AnimationRigVal backPos;
    private AnimationRigVal backRot;
    private AnimationRigVal attacherRot;
    private AnimationRigVal stabalizerOut;
    private AnimationRigVal stabalizerDown;
    private AnimationRigVal standOut;
    private AnimationRigVal standUp;
    private AnimationRigVal standSlide;
    private AnimationRigVal armBasePos;
    private AnimationRigVal armPos;
    private AnimationRigVal armRot;
    private AnimationRigVal missilePos;
    private AnimationRigVal missileRot;
    private AnimationRigVal armBasePos2;
    private AnimationRigVal armPos2;
    private AnimationRigVal armRot2;

    public ModuleShooterAdvSide(MinecartModular cart)
    {
        super(cart);
        this.handlePos = new AnimationRigVal(this.rig, 8.55F, 9.4F, 0.0F);
        this.basePos = new AnimationRigVal(this.rig, 1.05F, 4.0F, 0.05F);
        this.handleRot = new AnimationRigVal(this.rig, (float)Math.PI, ((float)Math.PI * 3F / 2F), 0.075F);
        this.gunRot = new AnimationRigVal(this.rig, 0.0F, -((float)Math.PI / 2F), 0.0F);
        this.backPos = new AnimationRigVal(this.rig, 4.5F, -3.0F, 0.3F);
        this.backRot = new AnimationRigVal(this.rig, 0.0F, -((float)Math.PI / 2F), 0.2F);
        this.attacherRot = new AnimationRigVal(this.rig, 0.0F, -(float)Math.PI, 0.2F);
        this.stabalizerOut = new AnimationRigVal(this.rig, 0.001F, 0.8F, 0.1F);
        this.stabalizerDown = new AnimationRigVal(this.rig, 0.0F, -2.0F, 0.1F);
        this.standOut = new AnimationRigVal(this.rig, 0.001F, 0.8F, 0.1F);
        this.standUp = new AnimationRigVal(this.rig, 0.0F, 2.0F, 0.1F);
        this.standSlide = new AnimationRigVal(this.rig, 0.0F, 0.25F, 0.01F);
        this.armBasePos = new AnimationRigVal(this.rig, 0.5F, 10.0F, 0.3F);
        this.armPos = new AnimationRigVal(this.rig, -2.25F, 2.5F, 0.0F);
        this.armRot = new AnimationRigVal(this.rig, 0.0F, ((float)Math.PI / 2F), 0.2F);
        this.missilePos = new AnimationRigVal(this.rig, 0.0F, 3.0F, 0.1F);
        this.missileRot = new AnimationRigVal(this.rig, 0.0F, -0.2F, 0.0F);
        this.armRot2 = new AnimationRigVal(this.rig, 0.0F, ((float)Math.PI / 2F), 0.2F);
        this.armBasePos2 = new AnimationRigVal(this.rig, 0.0F, 9.5F, 0.3F);
        this.armPos2 = new AnimationRigVal(this.rig, 0.0F, 5.0F, 0.0F);
        this.handlePos.setUpAndDown(this.basePos);
        this.handlePos.setSpeedToSync(this.basePos, false);
        this.handleRot.setUpAndDown(this.gunRot);
        this.gunRot.setSpeedToSync(this.handleRot, true);
        this.armPos.setSpeedToSync(this.armBasePos, false);
        this.armBasePos.setUpAndDown(this.armPos);
        this.missilePos.setUpAndDown(this.missileRot);
        this.missileRot.setSpeedToSync(this.missilePos, true);
        this.armPos2.setSpeedToSync(this.armBasePos2, false);
        this.armBasePos2.setUpAndDown(this.armPos2);
    }

    public void update()
    {
        super.update();
        this.rig.update(!this.isPipeActive(0));
    }

    public float getHandlePos(int mult)
    {
        return this.handlePos.getVal() * (float)mult;
    }

    public float getBasePos(int mult)
    {
        return this.basePos.getVal() * (float)mult;
    }

    public float getHandleRot(int mult)
    {
        return this.handleRot.getVal();
    }

    public float getGunRot(int mult)
    {
        return this.gunRot.getVal();
    }

    public float getBackPos(int mult)
    {
        return this.backPos.getVal();
    }

    public float getBackRot(int mult)
    {
        return this.backRot.getVal() * (float)mult;
    }

    public float getAttacherRot(int mult)
    {
        return this.attacherRot.getVal() * (float)mult;
    }

    public float getStabalizerOut(int mult)
    {
        return this.stabalizerOut.getVal() * (float)mult;
    }

    public float getStabalizerDown(int mult)
    {
        return this.stabalizerDown.getVal();
    }

    public float getStandOut(int mult, int i, int j)
    {
        return this.standOut.getVal() * (float)j + (float)(mult * i) * 0.5F + 0.003F;
    }

    public float getStandUp(int mult, int i, int j)
    {
        return this.standUp.getVal() - this.standSlide.getVal() * (float)(i * 2 - 1) * (float)j * (float)mult;
    }

    public float getArmBasePos(int mult, boolean fake)
    {
        return this.armBasePos.getVal() - (!fake ? this.armBasePos2.getVal() : 0.0F);
    }

    public float getArmRot(int mult, boolean fake)
    {
        return (this.armRot.getVal() - (!fake ? this.armRot2.getVal() : 0.0F)) * (float)mult;
    }

    public float getArmPos(int mult, boolean fake)
    {
        return this.armPos.getVal() - (!fake ? this.armPos2.getVal() : 0.0F);
    }

    public float getMissilePos(int mult)
    {
        return this.missilePos.getVal();
    }

    public float getMissileRot(int mult)
    {
        return this.missileRot.getVal() * (float)mult;
    }
}
