package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Realtimers.ModuleDynamite;

@SideOnly(Side.CLIENT)
public class ModelDynamite extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/tntModel.png");
    private ModelRenderer anchor = new ModelRenderer(this, 0, 0);
    private ModelRenderer[] dynamites;
    private float sizemult;

    public ResourceLocation getResource(ModuleBase module)
    {
        return texture;
    }

    public float extraMult()
    {
        return 0.25F;
    }

    public ModelDynamite()
    {
        this.AddRenderer(this.anchor);
        this.dynamites = new ModelRenderer[54];
        this.dynamites[0] = this.createDynamite(0.0F, 0.0F, 0.0F);
        this.dynamites[3] = this.createDynamite(-1.0F, 0.0F, 0.0F);
        this.dynamites[4] = this.createDynamite(1.0F, 0.0F, 0.0F);
        this.dynamites[18] = this.createDynamite(-2.0F, 0.0F, 0.0F);
        this.dynamites[19] = this.createDynamite(2.0F, 0.0F, 0.0F);
        this.dynamites[9] = this.createDynamite(-0.5F, 1.0F, 0.0F);
        this.dynamites[10] = this.createDynamite(0.5F, 1.0F, 0.0F);
        this.dynamites[24] = this.createDynamite(-1.5F, 1.0F, 0.0F);
        this.dynamites[25] = this.createDynamite(1.5F, 1.0F, 0.0F);
        this.dynamites[15] = this.createDynamite(0.0F, 2.0F, 0.0F);
        this.dynamites[30] = this.createDynamite(-1.0F, 2.0F, 0.0F);
        this.dynamites[31] = this.createDynamite(1.0F, 2.0F, 0.0F);
        this.dynamites[36] = this.createDynamite(-3.0F, 0.0F, 0.0F);
        this.dynamites[37] = this.createDynamite(3.0F, 0.0F, 0.0F);
        this.dynamites[42] = this.createDynamite(-2.5F, 1.0F, 0.0F);
        this.dynamites[43] = this.createDynamite(2.5F, 1.0F, 0.0F);
        this.dynamites[48] = this.createDynamite(-2.0F, 2.0F, 0.0F);
        this.dynamites[49] = this.createDynamite(2.0F, 2.0F, 0.0F);
        this.dynamites[1] = this.createDynamite(0.0F, 0.0F, -1.0F);
        this.dynamites[5] = this.createDynamite(-1.0F, 0.0F, -1.0F);
        this.dynamites[7] = this.createDynamite(1.0F, 0.0F, -1.0F);
        this.dynamites[20] = this.createDynamite(-2.0F, 0.0F, -1.0F);
        this.dynamites[22] = this.createDynamite(2.0F, 0.0F, -1.0F);
        this.dynamites[11] = this.createDynamite(-0.5F, 1.0F, -1.0F);
        this.dynamites[13] = this.createDynamite(0.5F, 1.0F, -1.0F);
        this.dynamites[26] = this.createDynamite(-1.5F, 1.0F, -1.0F);
        this.dynamites[28] = this.createDynamite(1.5F, 1.0F, -1.0F);
        this.dynamites[16] = this.createDynamite(0.0F, 2.0F, -1.0F);
        this.dynamites[32] = this.createDynamite(-1.0F, 2.0F, -1.0F);
        this.dynamites[34] = this.createDynamite(1.0F, 2.0F, -1.0F);
        this.dynamites[38] = this.createDynamite(-3.0F, 0.0F, -1.0F);
        this.dynamites[40] = this.createDynamite(3.0F, 0.0F, -1.0F);
        this.dynamites[44] = this.createDynamite(-2.5F, 1.0F, -1.0F);
        this.dynamites[46] = this.createDynamite(2.5F, 1.0F, -1.0F);
        this.dynamites[50] = this.createDynamite(-2.0F, 2.0F, -1.0F);
        this.dynamites[52] = this.createDynamite(2.0F, 2.0F, -1.0F);
        this.dynamites[2] = this.createDynamite(0.0F, 0.0F, 1.0F);
        this.dynamites[8] = this.createDynamite(-1.0F, 0.0F, 1.0F);
        this.dynamites[6] = this.createDynamite(1.0F, 0.0F, 1.0F);
        this.dynamites[21] = this.createDynamite(-2.0F, 0.0F, 1.0F);
        this.dynamites[23] = this.createDynamite(2.0F, 0.0F, 1.0F);
        this.dynamites[14] = this.createDynamite(-0.5F, 1.0F, 1.0F);
        this.dynamites[12] = this.createDynamite(0.5F, 1.0F, 1.0F);
        this.dynamites[29] = this.createDynamite(-1.5F, 1.0F, 1.0F);
        this.dynamites[27] = this.createDynamite(1.5F, 1.0F, 1.0F);
        this.dynamites[17] = this.createDynamite(0.0F, 2.0F, 1.0F);
        this.dynamites[35] = this.createDynamite(-1.0F, 2.0F, 1.0F);
        this.dynamites[33] = this.createDynamite(1.0F, 2.0F, 1.0F);
        this.dynamites[41] = this.createDynamite(-3.0F, 0.0F, 1.0F);
        this.dynamites[39] = this.createDynamite(3.0F, 0.0F, 1.0F);
        this.dynamites[47] = this.createDynamite(-2.5F, 1.0F, 1.0F);
        this.dynamites[45] = this.createDynamite(2.5F, 1.0F, 1.0F);
        this.dynamites[53] = this.createDynamite(-2.0F, 2.0F, 1.0F);
        this.dynamites[51] = this.createDynamite(2.0F, 2.0F, 1.0F);
    }

    private ModelRenderer createDynamite(float x, float y, float z)
    {
        ModelRenderer dynamite = new ModelRenderer(this, 0, 0);
        this.anchor.addChild(dynamite);
        this.fixSize(dynamite);
        dynamite.addBox(-8.0F, -4.0F, -4.0F, 16, 8, 8, 0.0F);
        dynamite.setRotationPoint(x * 10.0F, y * -8.0F, z * 18.0F);
        dynamite.rotateAngleY = ((float)Math.PI / 2F);
        return dynamite;
    }

    public void applyEffects(ModuleBase module, float yaw, float pitch, float roll)
    {
        if (module == null)
        {
            for (int size = 0; size < this.dynamites.length; ++size)
            {
                this.dynamites[size].isHidden = false;
            }
        }
        else
        {
            float var9 = ((ModuleDynamite)module).explosionSize();
            float max = 44.0F;
            float perModel = max / (float)this.dynamites.length;

            for (int i = 0; i < this.dynamites.length; ++i)
            {
                this.dynamites[i].isHidden = (float)i * perModel >= var9;
            }
        }

        this.anchor.setRotationPoint(0.0F, -24.0F / this.sizemult, 0.0F);
    }

    public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        if (module == null)
        {
            this.sizemult = 1.0F;
            super.render(render, module, yaw, pitch, roll, mult, partialtime);
        }
        else
        {
            float fusemult = (float)Math.abs(Math.sin((double)((float)((ModuleDynamite)module).getFuse() / (float)((ModuleDynamite)module).getFuseLength()) * Math.PI * 6.0D));
            this.sizemult = fusemult * 0.5F + 1.0F;
            GL11.glScalef(this.sizemult, this.sizemult, this.sizemult);
            super.render(render, module, yaw, pitch, roll, mult, partialtime);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, fusemult);
            super.render(render, module, yaw, pitch, roll, mult, partialtime);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glScalef(1.0F / this.sizemult, 1.0F / this.sizemult, 1.0F / this.sizemult);
        }
    }
}
