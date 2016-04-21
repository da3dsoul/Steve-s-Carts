package vswe.stevescarts.Modules.Addons;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleLiquidDrainer;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ModuleLiquidSensors extends ModuleAddon
{
    private float sensorRotation;
    private int activetime = -1;
    private int mult = 1;

    public ModuleLiquidSensors(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();

        if (this.isDrillSpinning())
        {
            this.sensorRotation += 0.05F * (float)this.mult;

            if (this.mult == 1 && (double)this.sensorRotation > (Math.PI / 4D) || this.mult == -1 && (double)this.sensorRotation < -(Math.PI / 4D))
            {
                this.mult *= -1;
            }
        }
        else
        {
            if (this.sensorRotation != 0.0F)
            {
                if (this.sensorRotation > 0.0F)
                {
                    this.sensorRotation -= 0.05F;

                    if (this.sensorRotation < 0.0F)
                    {
                        this.sensorRotation = 0.0F;
                    }
                }
                else
                {
                    this.sensorRotation += 0.05F;

                    if (this.sensorRotation > 0.0F)
                    {
                        this.sensorRotation = 0.0F;
                    }
                }
            }

            if (this.activetime >= 0)
            {
                ++this.activetime;

                if (this.activetime >= 10)
                {
                    this.setLight(1);
                    this.activetime = -1;
                }
            }
        }
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 1);
    }

    private void activateLight(int light)
    {
        if (this.getLight() != 3 || light != 2)
        {
            this.setLight(light);
            this.activetime = 0;
        }
    }

    public void getInfoFromDrill(byte data)
    {
        byte light = (byte)(data & 3);

        if (light != 1)
        {
            this.activateLight(light);
        }

        data &= -4;
        data = (byte)(data | this.getLight());
        this.setSensorInfo(data);
    }

    private void setLight(int val)
    {
        if (!this.isPlaceholder())
        {
            byte data = this.getDw(0);
            data &= -4;
            data = (byte)(data | val);
            this.setSensorInfo(data);
        }
    }

    private void setSensorInfo(int val)
    {
        if (!this.isPlaceholder())
        {
            this.updateDw(0, val);
        }
    }

    public int getLight()
    {
        return this.isPlaceholder() ? this.getSimInfo().getLiquidLight() : this.getDw(0) & 3;
    }

    protected boolean isDrillSpinning()
    {
        return this.isPlaceholder() ? this.getSimInfo().getDrillSpinning() : (this.getDw(0) & 4) != 0;
    }

    public float getSensorRotation()
    {
        return this.sensorRotation;
    }

    public boolean isDangerous(ModuleDrill drill, int x, int y, int z, int p, int q, int r)
    {
        int x1 = x + p;
        int y1 = y + q;
        int z1 = z + r;
        Block block = this.getCart().worldObj.getBlock(x1, y1, z1);

        if (block == Blocks.lava)
        {
            this.handleLiquid(drill, x1, y1, z1);
            return true;
        }
        else if (block == Blocks.water)
        {
            this.handleLiquid(drill, x1, y1, z1);
            return true;
        }
        else if (block != null && block instanceof IFluidBlock)
        {
            this.handleLiquid(drill, x1, y1, z1);
            return true;
        }
        else
        {
            boolean isWater = block == Blocks.water || block == Blocks.flowing_water || block == Blocks.ice;
            boolean isLava = block == Blocks.lava || block == Blocks.flowing_lava;
            boolean isOther = block != null && block instanceof IFluidBlock;
            boolean isLiquid = isWater || isLava || isOther;

            if (isLiquid)
            {
                if (q == 1)
                {
                    this.handleLiquid(drill, x1, y1, z1);
                    return true;
                }
                else
                {
                    int isFalling1 = this.getCart().worldObj.getBlockMetadata(x1, y1, z1);

                    if ((isFalling1 & 8) == 8)
                    {
                        if (block.isBlockSolid(this.getCart().worldObj, x1, y1 - 1, z1, 1))
                        {
                            this.handleLiquid(drill, x1, y1, z1);
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if (isWater && (isFalling1 & 7) == 7)
                    {
                        return false;
                    }
                    else if (isLava && (isFalling1 & 7) == 7 && this.getCart().worldObj.provider.isHellWorld)
                    {
                        return false;
                    }
                    else if (isLava && (isFalling1 & 7) == 6)
                    {
                        return false;
                    }
                    else
                    {
                        this.handleLiquid(drill, x1, y1, z1);
                        return true;
                    }
                }
            }
            else
            {
                if (q == 1)
                {
                    boolean isFalling = block instanceof BlockFalling;

                    if (isFalling)
                    {
                        return this.isDangerous(drill, x1, y1, z1, 0, 1, 0) || this.isDangerous(drill, x1, y1, z1, 1, 0, 0) || this.isDangerous(drill, x1, y1, z1, -1, 0, 0) || this.isDangerous(drill, x1, y1, z1, 0, 0, 1) || this.isDangerous(drill, x1, y1, z1, 0, 0, -1);
                    }
                }

                return false;
            }
        }
    }

    private void handleLiquid(ModuleDrill drill, int x, int y, int z)
    {
        ModuleLiquidDrainer liquiddrainer = null;
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleLiquidDrainer)
            {
                liquiddrainer = (ModuleLiquidDrainer)module;
                break;
            }
        }

        if (liquiddrainer != null)
        {
            liquiddrainer.handleLiquid(drill, x, y, z);
        }
    }
}
