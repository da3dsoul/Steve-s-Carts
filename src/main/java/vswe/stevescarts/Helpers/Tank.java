package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Interfaces.GuiBase;

public class Tank implements IFluidTank
{
    private FluidStack fluid;
    private int tankSize;
    private ITankHolder owner;
    private int tankid;
    private boolean isLocked;

    public Tank(ITankHolder owner, int tankSize, int tankid)
    {
        this.owner = owner;
        this.tankSize = tankSize;
        this.tankid = tankid;
    }

    public Tank copy()
    {
        Tank tank = new Tank(this.owner, this.tankSize, this.tankid);

        if (this.getFluid() != null)
        {
            tank.setFluid(this.getFluid().copy());
        }

        return tank;
    }

    public FluidStack getFluid()
    {
        return this.fluid;
    }

    public void setFluid(FluidStack fluid)
    {
        this.fluid = fluid;
    }

    public int getCapacity()
    {
        return this.tankSize;
    }

    public int getTankPressure()
    {
        return 0;
    }

    public void containerTransfer()
    {
        ItemStack item = this.owner.getInputContainer(this.tankid);

        if (item != null)
        {
            if (FluidContainerRegistry.isFilledContainer(item))
            {
                FluidStack full = FluidContainerRegistry.getFluidForFilledItem(item);

                if (full != null)
                {
                    int fluidContent = this.fill(full, false, false);

                    if (fluidContent == full.amount)
                    {
                        Item container = item.getItem().getContainerItem();
                        ItemStack containerStack = null;

                        if (container != null)
                        {
                            containerStack = new ItemStack(container, 1);
                            this.owner.addToOutputContainer(this.tankid, containerStack);
                        }

                        if (containerStack == null || containerStack.stackSize == 0)
                        {
                            --item.stackSize;

                            if (item.stackSize <= 0)
                            {
                                this.owner.clearInputContainer(this.tankid);
                            }

                            this.fill(full, true, false);
                        }
                    }
                }
            }
            else if (FluidContainerRegistry.isEmptyContainer(item))
            {
                ItemStack var6 = FluidContainerRegistry.fillFluidContainer(this.fluid, item);

                if (var6 != null)
                {
                    FluidStack var7 = FluidContainerRegistry.getFluidForFilledItem(var6);

                    if (var7 != null)
                    {
                        this.owner.addToOutputContainer(this.tankid, var6);

                        if (var6.stackSize == 0)
                        {
                            --item.stackSize;

                            if (item.stackSize <= 0)
                            {
                                this.owner.clearInputContainer(this.tankid);
                            }

                            this.drain(var7.amount, true, false);
                        }
                    }
                }
            }
        }
    }

    public int fill(FluidStack resource, boolean doFill)
    {
        return this.fill(resource, doFill, false);
    }

    public int fill(FluidStack resource, boolean doFill, boolean isRemote)
    {
        if (resource != null && (this.fluid == null || resource.isFluidEqual(this.fluid)))
        {
            int free = this.tankSize - (this.fluid == null ? 0 : this.fluid.amount);
            int fill = Math.min(free, resource.amount);

            if (doFill && !isRemote)
            {
                if (this.fluid == null)
                {
                    this.fluid = resource.copy();
                    this.fluid.amount = 0;
                }

                this.fluid.amount += fill;
                this.owner.onFluidUpdated(this.tankid);
            }

            return fill;
        }
        else
        {
            return 0;
        }
    }

    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        return this.drain(maxDrain, doDrain, false);
    }

    public FluidStack drain(int maxDrain, boolean doDrain, boolean isRemote)
    {
        if (this.fluid == null)
        {
            return null;
        }
        else
        {
            int amount = this.fluid.amount;
            int drain = Math.min(amount, maxDrain);
            FluidStack ret = this.fluid.copy();
            ret.amount = drain;

            if (doDrain && !isRemote)
            {
                this.fluid.amount -= drain;

                if (this.fluid.amount <= 0 && !this.isLocked)
                {
                    this.fluid = null;
                }

                this.owner.onFluidUpdated(this.tankid);
            }

            return ret;
        }
    }

    public void setLocked(boolean val)
    {
        this.isLocked = val;
    }

    public boolean isLocked()
    {
        return this.isLocked;
    }

    public String getMouseOver()
    {
        String name = Localization.MODULES.TANKS.EMPTY.translate(new String[0]);
        int amount = 0;

        if (this.fluid != null)
        {
            name = this.fluid.getFluid().getLocalizedName();

            if (name.indexOf(".") != -1)
            {
                name = FluidRegistry.getFluidName(this.fluid);
            }

            if (name != null && !name.equals(""))
            {
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            }
            else
            {
                name = Localization.MODULES.TANKS.INVALID.translate(new String[0]);
            }

            amount = this.fluid.amount;
        }

        return "\u00a7F" + name + "\n\u00a77" + this.formatNumber(amount) + " / " + this.formatNumber(this.tankSize);
    }

    private String formatNumber(int number)
    {
        return String.format("%,d", new Object[] {Integer.valueOf(number)}).replace('\u00a0', ' ');
    }

    public static IconData getIconAndTexture(FluidStack stack)
    {
        IIcon icon = null;
        String texture = null;

        if (stack != null)
        {
            Fluid fluid = stack.getFluid();

            if (fluid != null)
            {
                icon = fluid.getIcon();

                if (icon == null)
                {
                    if (FluidRegistry.WATER.equals(fluid))
                    {
                        icon = Blocks.water.getIcon(0, 0);
                    }
                    else if (FluidRegistry.LAVA.equals(fluid))
                    {
                        icon = Blocks.lava.getIcon(0, 0);
                    }
                }

                if (icon != null)
                {
                    texture = "/atlas/blocks.png";
                }
            }
        }

        return new IconData(icon, texture);
    }

    private static float getColorComponent(int color, int id)
    {
        return (float)((color & 255 << id * 8) >> id * 8) / 255.0F;
    }

    public static void applyColorFilter(FluidStack fluid)
    {
        int color = fluid.getFluid().getColor(fluid);
        GL11.glColor4f(getColorComponent(color, 2), getColorComponent(color, 1), getColorComponent(color, 0), 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public void drawFluid(GuiBase gui, int startX, int startY)
    {
        if (this.fluid != null)
        {
            int fluidLevel = (int)(48.0F * ((float)this.fluid.amount / (float)this.tankSize));
            IconData data = getIconAndTexture(this.fluid);

            if (data.getIcon() == null)
            {
                return;
            }

            ResourceHelper.bindResource(data.getResource());
            applyColorFilter(this.fluid);

            for (int y = 0; y < 3; ++y)
            {
                int pixels = fluidLevel - (2 - y) * 16;

                if (pixels > 0)
                {
                    if (pixels > 16)
                    {
                        pixels = 16;
                    }

                    for (int x = 0; x < 2; ++x)
                    {
                        this.owner.drawImage(this.tankid, gui, data.getIcon(), startX + 2 + 16 * x, startY + 1 + 16 * y + (16 - pixels), 0, 16 - pixels, 16, pixels);
                    }
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public int getFluidAmount()
    {
        return this.fluid == null ? 0 : this.fluid.amount;
    }

    public FluidTankInfo getInfo()
    {
        return new FluidTankInfo(this.fluid, this.getCapacity());
    }
}
