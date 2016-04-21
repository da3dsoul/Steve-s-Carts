package vswe.stevescarts.Models.Cart;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdvSide;

public class ModelSideGuns extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/sidegunsModel.png");
    ModelRenderer[][] models = new ModelRenderer[2][];

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureWidth()
    {
        return 64;
    }

    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelSideGuns()
    {
        this.models[0] = this.createSide(false);
        this.models[1] = this.createSide(true);
    }

    private ModelRenderer[] createSide(boolean opposite)
    {
        ModelRenderer anchor = new ModelRenderer(this, 0, 0);
        this.AddRenderer(anchor);
        int mult = opposite ? 1 : -1;
        ModelRenderer handle = new ModelRenderer(this, 0, 8);
        anchor.addChild(handle);
        this.fixSize(handle);
        handle.addBox(-2.0F, -2.0F, -2.5F, 4, 4, 5, 0.0F);
        handle.setRotationPoint(6.0F, 0.0F, 0.0F);
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        handle.addChild(base);
        this.fixSize(base);
        base.addBox(-2.5F, -2.5F, -1.5F, 12, 5, 3, 0.0F);
        base.setRotationPoint(0.0F, 0.0F, 0.0F);
        ModelRenderer gun = new ModelRenderer(this, 0, 0);
        base.addChild(gun);
        this.fixSize(gun);
        gun.addBox(-2.5F, -2.5F, -1.5F, 12, 5, 3, 0.0F);
        gun.setRotationPoint(7.005F, 0.005F, 0.005F);
        ModelRenderer back = new ModelRenderer(this, 18, 8);
        gun.addChild(back);
        this.fixSize(back);
        back.addBox(-6.5F, -2.0F, -0.5F, 7, 4, 1, 0.0F);
        back.setRotationPoint(0.0F, 0.0F, -0.5F * (float)mult);
        ModelRenderer backAttacher = new ModelRenderer(this, 18, 8);
        back.addChild(backAttacher);
        this.fixSize(backAttacher);
        backAttacher.addBox(0.0F, -2.0F, -0.5F + 0.5F * (float)mult, 7, 4, 1, 0.0F);
        backAttacher.setRotationPoint(-6.5F, 0.0F, 0.5F * (float)mult + 0.005F);
        ModelRenderer stabalizer = new ModelRenderer(this, 0, 8);
        back.addChild(stabalizer);
        this.fixSize(stabalizer);
        stabalizer.addBox(-0.5F, -1.5F, -0.5F, 1, 3, 1, 0.0F);
        stabalizer.setRotationPoint(-5.75F, 0.0F, 0.0F);
        ModelRenderer[] missileStands = new ModelRenderer[6];

        for (int missleArmBase = 0; missleArmBase < missileStands.length; ++missleArmBase)
        {
            missileStands[missleArmBase] = new ModelRenderer(this, 0, 17);
            float missleArm;

            if (missleArmBase < 3)
            {
                back.addChild(missileStands[missleArmBase]);
                missleArm = -5.0F;
            }
            else
            {
                backAttacher.addChild(missileStands[missleArmBase]);
                missleArm = 0.0F;
            }

            this.fixSize(missileStands[missleArmBase]);
            missileStands[missleArmBase].addBox(1.0F, -1.5F, -0.5F, 2, 3, 1, 0.0F);
            missileStands[missleArmBase].setRotationPoint(missleArm, 0.0F, 0.0F);
        }

        ModelRenderer var21 = new ModelRenderer(this, 7, 17);
        base.addChild(var21);
        this.fixSize(var21);
        var21.addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, 0.0F);
        var21.setRotationPoint(0.0F, 0.0F, 0.75F * (float)mult);
        ModelRenderer var22 = new ModelRenderer(this, 17, 17);
        var21.addChild(var22);
        this.fixSize(var22);
        var22.addBox(-0.5F, -2.0F, -0.5F, 11, 4, 1, 0.0F);
        var22.setRotationPoint(0.0F, 0.0F, 0.0F);
        ModelRenderer missleArmBaseFake = new ModelRenderer(this);
        base.addChild(missleArmBaseFake);
        this.fixSize(missleArmBaseFake);
        missleArmBaseFake.setRotationPoint(0.0F, 0.0F, 0.75F * (float)mult);
        ModelRenderer missleArmFake = new ModelRenderer(this);
        missleArmBaseFake.addChild(missleArmFake);
        this.fixSize(missleArmFake);
        missleArmFake.setRotationPoint(0.0F, 0.0F, 0.0F);
        ModelRenderer[] missiles = new ModelRenderer[2];

        for (int i = 0; i < 2; ++i)
        {
            ModelRenderer missile = new ModelRenderer(this, 0, 22);
            missiles[i] = missile;
            missleArmFake.addChild(missile);
            this.fixSize(missile);
            missile.addBox(-2.0F, -1.0F, -1.0F, 4, 2, 2, -0.2F);
            missile.setRotationPoint(i == 0 ? 9.5F : 4.0F, 0.0F, (float)mult * -1.0F);
            missile.rotateAngleZ = ((float)Math.PI / 2F);

            for (int missilePart = -1; missilePart <= 1; ++missilePart)
            {
                for (int k = -1; k <= 1; ++k)
                {
                    if (missilePart == 0 || k == 0)
                    {
                        ModelRenderer missilePart1 = new ModelRenderer(this, 12, 22);
                        missile.addChild(missilePart1);
                        this.fixSize(missilePart1);
                        missilePart1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
                        missilePart1.setRotationPoint(-1.0F, (float)missilePart * 0.6F, (float)k * 0.6F);
                    }
                }
            }

            ModelRenderer var23 = new ModelRenderer(this, 12, 24);
            missile.addChild(var23);
            this.fixSize(var23);
            var23.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
            var23.setRotationPoint(1.75F, 0.0F, 0.0F);
        }

        return new ModelRenderer[] {handle, base, gun, back, backAttacher, stabalizer, missileStands[0], missileStands[1], missileStands[2], missileStands[3], missileStands[4], missileStands[5], var21, var22, missleArmBaseFake, missleArmFake, missiles[0], missiles[1]};
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        if (module == null)
        {
            for (int shooter = 0; shooter < 2; ++shooter)
            {
                ModelRenderer[] var10000 = this.models[shooter];
            }
        }
        else
        {
            ModuleShooterAdvSide var11 = (ModuleShooterAdvSide)module;

            for (int i = 0; i < 2; ++i)
            {
                ModelRenderer[] models = this.models[i];
                int mult = i != 0 ? 1 : -1;
                models[0].rotationPointZ = var11.getHandlePos(mult);
                models[1].rotationPointZ = var11.getBasePos(mult);
                models[0].rotateAngleZ = var11.getHandleRot(mult);
                models[2].rotateAngleZ = var11.getGunRot(mult);
                models[3].rotationPointX = var11.getBackPos(mult);
                models[3].rotateAngleY = var11.getBackRot(mult);
                models[4].rotateAngleY = var11.getAttacherRot(mult);
                models[5].rotationPointZ = var11.getStabalizerOut(mult);
                models[5].rotationPointY = var11.getStabalizerDown(mult);
                int j;

                for (j = 0; j < 2; ++j)
                {
                    for (int k = 0; k < 3; ++k)
                    {
                        models[6 + j * 3 + k].rotationPointZ = var11.getStandOut(mult, j, k - 1);
                        models[6 + j * 3 + k].rotationPointY = var11.getStandUp(mult, j, k - 1);
                    }
                }

                models[12].rotationPointX = var11.getArmBasePos(mult, false);
                models[13].rotateAngleY = var11.getArmRot(mult, false);
                models[13].rotationPointX = var11.getArmPos(mult, false);
                models[14].rotationPointX = var11.getArmBasePos(mult, true);
                models[15].rotateAngleY = var11.getArmRot(mult, true);
                models[15].rotationPointX = var11.getArmPos(mult, true);

                for (j = 0; j < 2; ++j)
                {
                    models[16 + j].rotationPointY = var11.getMissilePos(mult);
                    models[16 + j].rotateAngleY = var11.getMissileRot(mult);
                }
            }
        }
    }
}
