package vswe.stevescarts.Modules.Storages.Tanks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Helpers.Localization;

public class ModuleCheatTank extends ModuleTank
{
    private static final ColorHelper[] colors = new ColorHelper[] {ColorHelper.YELLOW, ColorHelper.GREEN, ColorHelper.RED, ColorHelper.ORANGE};
    private int mode;

    public ModuleCheatTank(MinecartModular cart)
    {
        super(cart);
    }

    protected String getTankInfo()
    {
        String str = super.getTankInfo();
        str = str + "\n\n" + Localization.MODULES.TANKS.CREATIVE_MODE.translate(new String[] {colors[this.mode].toString(), String.valueOf(this.mode)}) + "\n" + Localization.MODULES.TANKS.CHANGE_MODE.translate(new String[0]);

        if (this.mode != 0)
        {
            str = str + "\n" + Localization.MODULES.TANKS.RESET_MODE.translate(new String[0]);
        }

        return str;
    }

    protected int getTankSize()
    {
        return 2000000000;
    }

    public boolean hasVisualTank()
    {
        return false;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0 && (data[0] & 1) != 0)
        {
            if (this.mode != 0 && (data[0] & 2) != 0)
            {
                this.mode = 0;
            }
            else if (++this.mode == colors.length)
            {
                this.mode = 1;
            }

            this.updateAmount();
            this.updateDw();
        }
        else
        {
            super.receivePacket(id, data, player);
        }
    }

    public int numberOfGuiData()
    {
        return super.numberOfGuiData() + 1;
    }

    protected void checkGuiData(Object[] info)
    {
        super.checkGuiData(info);
        this.updateGuiData(info, super.numberOfGuiData(), (short)this.mode);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == super.numberOfGuiData())
        {
            this.mode = data;
        }
        else
        {
            super.receiveGuiData(id, data);
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setByte(this.generateNBTName("mode", id), (byte)this.mode);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.mode = tagCompound.getByte(this.generateNBTName("mode", id));
    }

    private void updateAmount()
    {
        if (this.tank.getFluid() != null)
        {
            if (this.mode == 1)
            {
                this.tank.getFluid().amount = this.getTankSize();
            }
            else if (this.mode == 2)
            {
                this.tank.getFluid().amount = 0;

                if (!this.tank.isLocked())
                {
                    this.tank.setFluid((FluidStack)null);
                }
            }
            else if (this.mode == 3)
            {
                this.tank.getFluid().amount = this.getTankSize() / 2;
            }
        }
    }

    public void onFluidUpdated(int tankid)
    {
        this.updateAmount();
        super.onFluidUpdated(tankid);
    }
}
