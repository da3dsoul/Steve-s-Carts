package vswe.stevescarts.Renders;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.IconData;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Modules.ModuleBase;

public class RendererMinecart extends Render
{
    public RendererMinecart()
    {
        this.shadowSize = 0.5F;
    }

    public void renderCart(MinecartModular cart, double x, double y, double z, float yaw, float partialTickTime)
    {
        if (cart.getModules() != null)
        {
            Iterator partialPosX = cart.getModules().iterator();

            while (partialPosX.hasNext())
            {
                ModuleBase module = (ModuleBase)partialPosX.next();

                if (!module.shouldCartRender())
                {
                    return;
                }
            }
        }

        GL11.glPushMatrix();
        double partialPosX1 = cart.lastTickPosX + (cart.posX - cart.lastTickPosX) * (double)partialTickTime;
        double partialPosY = cart.lastTickPosY + (cart.posY - cart.lastTickPosY) * (double)partialTickTime;
        double partialPosZ = cart.lastTickPosZ + (cart.posZ - cart.lastTickPosZ) * (double)partialTickTime;
        float partialRotPitch = cart.prevRotationPitch + (cart.rotationPitch - cart.prevRotationPitch) * partialTickTime;
        Vec3 posFromRail = cart.func_70489_a(partialPosX1, partialPosY, partialPosZ);

        if (posFromRail != null && cart.canUseRail())
        {
            double damageRot = 0.30000001192092896D;
            Vec3 damageDir = cart.func_70495_a(partialPosX1, partialPosY, partialPosZ, damageRot);
            Vec3 flip = cart.func_70495_a(partialPosX1, partialPosY, partialPosZ, -damageRot);

            if (damageDir == null)
            {
                damageDir = posFromRail;
            }

            if (flip == null)
            {
                flip = posFromRail;
            }

            x += posFromRail.xCoord - partialPosX1;
            y += (damageDir.yCoord + flip.yCoord) / 2.0D - partialPosY;
            z += posFromRail.zCoord - partialPosZ;
            Vec3 difference = flip.addVector(-damageDir.xCoord, -damageDir.yCoord, -damageDir.zCoord);

            if (difference.lengthVector() != 0.0D)
            {
                difference = difference.normalize();
                yaw = (float)(Math.atan2(difference.zCoord, difference.xCoord) * 180.0D / Math.PI);
                partialRotPitch = (float)(Math.atan(difference.yCoord) * 73.0D);
            }
        }

        yaw = 180.0F - yaw;
        partialRotPitch *= -1.0F;
        float damageRot1 = (float)cart.getRollingAmplitude() - partialTickTime;
        float damageTime = cart.getDamage() - partialTickTime;
        float damageDir1 = (float)cart.getRollingDirection();

        if (damageTime < 0.0F)
        {
            damageTime = 0.0F;
        }

        boolean flip1 = cart.motionX > 0.0D != cart.motionZ > 0.0D;

        if (cart.cornerFlip)
        {
            flip1 = !flip1;
        }

        if (cart.getRenderFlippedYaw(yaw + (flip1 ? 0.0F : 180.0F)))
        {
            flip1 = !flip1;
        }

        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(partialRotPitch, 0.0F, 0.0F, 1.0F);

        if (damageRot1 > 0.0F)
        {
            damageRot1 = MathHelper.sin(damageRot1) * damageRot1 * damageTime / 10.0F * damageDir1;
            GL11.glRotatef(damageRot1, 1.0F, 0.0F, 0.0F);
        }

        yaw += flip1 ? 0.0F : 180.0F;
        GL11.glRotatef(flip1 ? 0.0F : 180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.renderModels(cart, (float)(Math.PI * (double)yaw / 180.0D), partialRotPitch, damageRot1, 0.0625F, partialTickTime);
        GL11.glPopMatrix();
        this.renderLabel(cart, x, y, z);
    }

    public void renderModels(MinecartModular cart, float yaw, float pitch, float roll, float mult, float partialtime)
    {
        if (cart.getModules() != null)
        {
            Iterator i$ = cart.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();

                if (module.haveModels())
                {
                    Iterator i$1 = module.getModels().iterator();

                    while (i$1.hasNext())
                    {
                        ModelCartbase model = (ModelCartbase)i$1.next();
                        model.render(this, module, yaw, pitch, roll, mult, partialtime);
                    }
                }
            }
        }
    }

    public void renderLiquidCuboid(FluidStack liquid, int tankSize, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float mult)
    {
        IconData data = Tank.getIconAndTexture(liquid);

        if (data != null && data.getIcon() != null)
        {
            if (liquid.amount > 0)
            {
                float filled = (float)liquid.amount / (float)tankSize;
                GL11.glPushMatrix();
                GL11.glTranslatef(x * mult, (y + sizeY * (1.0F - filled) / 2.0F) * mult, z * mult);
                ResourceHelper.bindResource(data.getResource());
                Tank.applyColorFilter(liquid);
                float scale = 0.5F;
                GL11.glScalef(scale, scale, scale);
                GL11.glDisable(GL11.GL_LIGHTING);
                mult /= scale;
                this.renderCuboid(data.getIcon(), (double)(sizeX * mult), (double)(sizeY * mult * filled), (double)(sizeZ * mult));
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glPopMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private void renderCuboid(IIcon icon, double sizeX, double sizeY, double sizeZ)
    {
        this.renderFace(icon, sizeX, sizeZ, 0.0F, 90.0F, 0.0F, -((float)(sizeY / 2.0D)), 0.0F);
        this.renderFace(icon, sizeX, sizeZ, 0.0F, -90.0F, 0.0F, (float)(sizeY / 2.0D), 0.0F);
        this.renderFace(icon, sizeX, sizeY, 0.0F, 0.0F, 0.0F, 0.0F, (float)(sizeZ / 2.0D));
        this.renderFace(icon, sizeX, sizeY, 180.0F, 0.0F, 0.0F, 0.0F, -((float)(sizeZ / 2.0D)));
        this.renderFace(icon, sizeZ, sizeY, 90.0F, 0.0F, (float)(sizeX / 2.0D), 0.0F, 0.0F);
        this.renderFace(icon, sizeZ, sizeY, -90.0F, 0.0F, -((float)(sizeX / 2.0D)), 0.0F, 0.0F);
    }

    private void renderFace(IIcon icon, double totalTargetW, double totalTargetH, float yaw, float roll, float offX, float offY, float offZ)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(offX, offY, offZ);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(roll, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.instance;
        double srcX = (double)icon.getMinU();
        double srcY = (double)icon.getMinV();
        double srcW = (double)icon.getMaxU() - srcX;
        double srcH = (double)icon.getMaxV() - srcY;
        double d = 0.001D;
        double currentTargetW;

        for (double currentTargetX = 0.0D; totalTargetW - currentTargetX > d * 2.0D; currentTargetX += currentTargetW - d)
        {
            currentTargetW = Math.min(totalTargetW - currentTargetX, 1.0D);
            double currentTargetH;

            for (double currentTargetY = 0.0D; totalTargetH - currentTargetY > d * 2.0D; currentTargetY += currentTargetH - d)
            {
                currentTargetH = Math.min(totalTargetH - currentTargetY, 1.0D);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV(currentTargetX - totalTargetW / 2.0D, currentTargetY - totalTargetH / 2.0D, 0.0D, srcX, srcY);
                tessellator.addVertexWithUV(currentTargetX + currentTargetW - totalTargetW / 2.0D, currentTargetY - totalTargetH / 2.0D, 0.0D, srcX + srcW * currentTargetW, srcY);
                tessellator.addVertexWithUV(currentTargetX + currentTargetW - totalTargetW / 2.0D, currentTargetY + currentTargetH - totalTargetH / 2.0D, 0.0D, srcX + srcW * currentTargetW, srcY + srcH * currentTargetH);
                tessellator.addVertexWithUV(currentTargetX - totalTargetW / 2.0D, currentTargetY + currentTargetH - totalTargetH / 2.0D, 0.0D, srcX, srcY + srcH * currentTargetH);
                tessellator.draw();
            }
        }

        GL11.glPopMatrix();
    }

    protected void renderLabel(MinecartModular cart, double x, double y, double z)
    {
        ArrayList labels = cart.getLabel();

        if (labels != null && labels.size() > 0)
        {
            float distance = cart.getDistanceToEntity(this.renderManager.livingPlayer);

            if (distance <= 64.0F)
            {
                FontRenderer frend = this.getFontRendererFromRenderManager();
                float var12 = 1.6F;
                float var13 = 0.016666668F * var12;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)x + 0.0F, (float)y + 1.0F + (float)(labels.size() - 1) * 0.12F, (float)z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-var13, -var13, var13);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int boxwidth = 0;
                int boxheight = 0;

                for (Iterator halfW = labels.iterator(); halfW.hasNext(); boxheight += frend.FONT_HEIGHT)
                {
                    String halfH = (String)halfW.next();
                    boxwidth = Math.max(boxwidth, frend.getStringWidth(halfH));
                }

                int halfW1 = boxwidth / 2;
                int halfH1 = boxheight / 2;
                Tessellator tes = Tessellator.instance;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tes.startDrawingQuads();
                tes.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                tes.addVertex((double)(-halfW1 - 1), (double)(-halfH1 - 1), 0.0D);
                tes.addVertex((double)(-halfW1 - 1), (double)(halfH1 + 1), 0.0D);
                tes.addVertex((double)(halfW1 + 1), (double)(halfH1 + 1), 0.0D);
                tes.addVertex((double)(halfW1 + 1), (double)(-halfH1 - 1), 0.0D);
                tes.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                int yPos = -halfH1;
                Iterator i$;
                String label;

                for (i$ = labels.iterator(); i$.hasNext(); yPos += frend.FONT_HEIGHT)
                {
                    label = (String)i$.next();
                    frend.drawString(label, -frend.getStringWidth(label) / 2, yPos, 553648127);
                }

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                yPos = -halfH1;

                for (i$ = labels.iterator(); i$.hasNext(); yPos += frend.FONT_HEIGHT)
                {
                    label = (String)i$.next();
                    frend.drawString(label, -frend.getStringWidth(label) / 2, yPos, -1);
                }

                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return null;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        this.renderCart((MinecartModular)par1Entity, x, y, z, yaw, partialTickTime);
    }
}
