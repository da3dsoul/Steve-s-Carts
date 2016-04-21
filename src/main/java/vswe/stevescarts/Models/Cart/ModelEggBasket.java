package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Chests.ModuleEggBasket;

@SideOnly(Side.CLIENT)
public class ModelEggBasket extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/chestModelEaster.png");
    ModelRenderer chesttop;

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    protected int getTextureHeight()
    {
        return 128;
    }

    public ModelEggBasket()
    {
        ModelRenderer chesthandletop;

        for (int chestbot = 0; chestbot < 2; ++chestbot)
        {
            chesthandletop = new ModelRenderer(this, 0, 13);
            this.AddRenderer(chesthandletop);
            chesthandletop.addBox(-8.0F, -2.5F, -0.5F, 16, 5, 1, 0.0F);
            chesthandletop.setRotationPoint(0.0F, -8.5F, -5.5F + (float)(chestbot * 11));
            ModelRenderer i = new ModelRenderer(this, 0, 19);
            this.AddRenderer(i);
            i.addBox(-5.0F, -2.5F, -0.5F, 10, 5, 1, 0.0F);
            i.setRotationPoint(-7.5F + (float)(chestbot * 15), -8.5F, 0.0F);
            i.rotateAngleY = ((float)Math.PI / 2F);
            ModelRenderer chesthandle = new ModelRenderer(this, 0, 36);
            this.AddRenderer(chesthandle);
            chesthandle.addBox(-1.0F, -1.5F, -0.5F, 2, 3, 1, 0.0F);
            chesthandle.setRotationPoint(0.0F, -12.5F, -5.5F + (float)(chestbot * 11));
            ModelRenderer chesthandlesmall = new ModelRenderer(this, 0, 40);
            this.AddRenderer(chesthandlesmall);
            chesthandlesmall.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
            chesthandlesmall.setRotationPoint(0.0F, -14.5F, -4.5F + (float)(chestbot * 9));
        }

        this.chesttop = new ModelRenderer(this, 0, 0);
        this.AddRenderer(this.chesttop);
        this.chesttop.addBox(-7.0F, -5.0F, -0.5F, 14, 10, 1, 0.0F);
        this.chesttop.setRotationPoint(0.0F, -11.5F, 0.0F);
        this.chesttop.rotateAngleX = ((float)Math.PI / 2F);
        this.chesttop.rotateAngleY = 0.1F;
        ModelRenderer var6 = new ModelRenderer(this, 0, 25);
        this.AddRenderer(var6);
        var6.addBox(-7.0F, -5.0F, -0.5F, 14, 10, 1, 0.0F);
        var6.setRotationPoint(0.0F, -5.5F, 0.0F);
        var6.rotateAngleX = ((float)Math.PI / 2F);
        chesthandletop = new ModelRenderer(this, 0, 42);
        this.AddRenderer(chesthandletop);
        chesthandletop.addBox(-1.0F, -4.0F, -0.5F, 2, 8, 1, 0.0F);
        chesthandletop.setRotationPoint(0.0F, -15.5F, 0.0F);
        chesthandletop.rotateAngleX = ((float)Math.PI / 2F);

        for (int var7 = 0; var7 < 12; ++var7)
        {
            this.addEgg(var7);
        }
    }

    private void addEgg(int id)
    {
        int x = id % 3;
        int y = id / 3;
        float xCoord = -3.0F + (float)x * 3.3333333F;
        float yCoord = -5.0F + (float)y * 3.5F;
        int textureY = 19 + id * 5;
        ModelRenderer eggbot = new ModelRenderer(this, 30, textureY);
        this.AddRenderer(eggbot);
        eggbot.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
        eggbot.setRotationPoint(yCoord, -6.5F, xCoord);
        ModelRenderer eggbase = new ModelRenderer(this, 38, textureY);
        this.AddRenderer(eggbase);
        eggbase.addBox(-1.5F, -1.0F, -1.5F, 3, 2, 3, 0.0F);
        eggbase.setRotationPoint(yCoord, -7.5F, xCoord);
        ModelRenderer eggmiddle = new ModelRenderer(this, 50, textureY);
        this.AddRenderer(eggmiddle);
        eggmiddle.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
        eggmiddle.setRotationPoint(yCoord, -8.75F, xCoord);
        ModelRenderer eggtip = new ModelRenderer(this, 58, textureY);
        this.AddRenderer(eggtip);
        eggtip.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        eggtip.setRotationPoint(yCoord, -9.25F, xCoord);
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        if (module != null)
        {
            this.chesttop.rotateAngleY = 0.1F + ((ModuleEggBasket)module).getChestAngle();
        }
    }
}
