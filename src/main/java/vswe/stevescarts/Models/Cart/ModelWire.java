package vswe.stevescarts.Models.Cart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;

@SideOnly(Side.CLIENT)
public abstract class ModelWire extends ModelCartbase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/models/wireModel.png");

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
        return 2;
    }

    protected int baseZ()
    {
        return 0;
    }

    protected void CreateEnd(int x, int y)
    {
        this.CreateEnd(x, y, this.baseZ());
    }

    protected void CreateEnd(int x, int y, int z)
    {
        ModelRenderer end = new ModelRenderer(this, 28, 0);
        this.AddRenderer(end);
        end.addBox(0.5F, 0.5F, 0.5F, 1, 1, 1, 0.0F);
        end.setRotationPoint(-7.5F + (float)y, -5.5F - (float)z, -5.5F + (float)x);
    }

    protected void CreateWire(int x1, int y1, int x2, int y2)
    {
        this.CreateWire(x1, y1, this.baseZ(), x2, y2, this.baseZ());
    }

    protected void CreateWire(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        if (x1 == x2 || y1 == y2 || z1 == z2)
        {
            int length;
            boolean rotate;
            boolean rotateZ;

            if (y1 != y2)
            {
                rotate = false;
                rotateZ = false;
                length = y2 - y1 + 1;

                if (length < 0)
                {
                    length *= -1;
                    y1 = y2;
                }
            }
            else if (z1 != z2)
            {
                rotate = false;
                rotateZ = true;
                length = z2 - z1 + 1;

                if (length < 0)
                {
                    length *= -1;
                    z1 = z2;
                }
            }
            else
            {
                rotate = true;
                rotateZ = false;
                length = x2 - x1 + 1;

                if (length < 0)
                {
                    length *= -1;
                    x1 = x2;
                }
            }

            if (length <= 13)
            {
                ModelRenderer wire = new ModelRenderer(this, 0, 0);
                this.AddRenderer(wire);
                wire.addBox((float)length / 2.0F, 0.5F, 0.5F, length, 1, 1, 0.0F);

                if (rotateZ)
                {
                    wire.setRotationPoint(-7.5F + (float)y1, -4.0F + (float)length / 2.0F - (float)z1, -5.5F + (float)x1);
                    wire.rotateAngleZ = ((float)Math.PI * 3F / 2F);
                }
                else if (rotate)
                {
                    wire.setRotationPoint(-5.5F + (float)y1, -5.5F - (float)z1, -5.0F - (float)length / 2.0F + (float)x1);
                    wire.rotateAngleY = ((float)Math.PI * 3F / 2F);
                }
                else
                {
                    wire.setRotationPoint(-7.0F - (float)length / 2.0F + (float)y1, -5.5F - (float)z1, -5.5F + (float)x1);
                }
            }
        }
    }
}
