package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleLiquidSensors;

@SideOnly(Side.CLIENT)
public class ModelLiquidSensors extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/sensorModel.png");
    ModelRenderer[] sensor1 = this.createSensor(false);
    ModelRenderer[] sensor2 = this.createSensor(true);

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureWidth()
    {
        return 32;
    }

    protected int getTextureHeight()
    {
        return 16;
    }

    private ModelRenderer[] createSensor(boolean right)
    {
        ModelRenderer base = new ModelRenderer(this, 0, 0);
        this.AddRenderer(base);
        base.addBox(0.5F, 2.0F, 0.5F, 1, 4, 1, 0.0F);

        if (right)
        {
            base.setRotationPoint(-10.0F, -11.0F, 6.0F);
        }
        else
        {
            base.setRotationPoint(-10.0F, -11.0F, -8.0F);
        }

        ModelRenderer head = new ModelRenderer(this, 4, 0);
        this.fixSize(head);
        base.addChild(head);
        head.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        head.setRotationPoint(1.0F, 0.0F, 1.0F);
        ModelRenderer face = new ModelRenderer(this, 20, 0);
        this.fixSize(face);
        head.addChild(face);
        face.addBox(-0.5F, -1.0F, -1.0F, 1, 2, 2, 0.0F);
        face.setRotationPoint(-2.5F, 0.0F, 0.0F);
        ModelRenderer[] dynamic = new ModelRenderer[4];
        dynamic[0] = head;

        for (int i = 1; i < 4; ++i)
        {
            ModelRenderer light = new ModelRenderer(this, 20, 1 + i * 3);
            this.fixSize(light);
            head.addChild(light);
            light.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
            light.setRotationPoint(0.0F, -2.5F, 0.0F);
            dynamic[i] = light;
        }

        return dynamic;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        this.sensor1[0].rotateAngleY = module == null ? 0.0F : -((ModuleLiquidSensors)module).getSensorRotation();
        this.sensor2[0].rotateAngleY = module == null ? 0.0F : ((ModuleLiquidSensors)module).getSensorRotation();
        int active = module == null ? 2 : ((ModuleLiquidSensors)module).getLight();

        for (int i = 1; i < 4; ++i)
        {
            this.sensor1[i].isHidden = i != active;
            this.sensor2[i].isHidden = i != active;
        }
    }
}
