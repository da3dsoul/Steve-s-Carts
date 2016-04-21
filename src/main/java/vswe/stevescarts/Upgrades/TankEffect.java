package vswe.stevescarts.Upgrades;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Interfaces.GuiUpgrade;
import vswe.stevescarts.Slots.SlotLiquidOutput;
import vswe.stevescarts.Slots.SlotLiquidUpgradeInput;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public abstract class TankEffect extends InventoryEffect
{
    private static final int tankInterfaceX = 35;
    private static final int tankInterfaceY = 20;
    @SideOnly(Side.CLIENT)
    private static ResourceLocation texture;

    public abstract int getTankSize();

    public Class <? extends Slot > getSlot(int id)
    {
        return SlotLiquidOutput.class;
    }

    public Slot createSlot(TileEntityUpgrade upgrade, int id)
    {
        return (Slot)(id == 0 ? new SlotLiquidUpgradeInput(upgrade, upgrade.tank, 16, id, this.getSlotX(id), this.getSlotY(id)) : super.createSlot(upgrade, id));
    }

    public int getInventorySize()
    {
        return 2;
    }

    public int getSlotX(int id)
    {
        return 8;
    }

    public int getSlotY(int id)
    {
        return 24 * (id + 1);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(TileEntityUpgrade upgrade, GuiUpgrade gui, int x, int y)
    {
        if (texture == null)
        {
            texture = ResourceHelper.getResource("/gui/tank.png");
        }

        upgrade.tank.drawFluid(gui, 35, 20);
        ResourceHelper.bindResource(texture);
        gui.drawTexturedModalRect(gui.getGuiLeft() + 35, gui.getGuiTop() + 20, 0, 0, 36, 51);
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(TileEntityUpgrade upgrade, GuiUpgrade gui, int x, int y)
    {
        this.drawMouseOver(gui, upgrade.tank.getMouseOver(), x, y, new int[] {35, 20, 36, 51});
    }

    public void checkGuiData(TileEntityUpgrade upgrade, ContainerUpgrade con, ICrafting crafting, boolean isNew)
    {
        boolean changed = false;
        byte id = 0;
        byte amount1 = 1;
        byte amount2 = 2;
        boolean meta = true;
        FluidStack oldfluid = (FluidStack)con.olddata;

        if ((isNew || oldfluid != null) && upgrade.tank.getFluid() == null)
        {
            upgrade.updateGuiData(con, crafting, id, (short) - 1);
            changed = true;
        }
        else if (upgrade.tank.getFluid() != null)
        {
            if (!isNew && oldfluid != null)
            {
                if (oldfluid.fluidID != upgrade.tank.getFluid().fluidID)
                {
                    upgrade.updateGuiData(con, crafting, id, (short)upgrade.tank.getFluid().fluidID);
                    changed = true;
                }

                if (oldfluid.amount != upgrade.tank.getFluid().amount)
                {
                    upgrade.updateGuiData(con, crafting, amount1, upgrade.getShortFromInt(true, upgrade.tank.getFluid().amount));
                    upgrade.updateGuiData(con, crafting, amount2, upgrade.getShortFromInt(false, upgrade.tank.getFluid().amount));
                    changed = true;
                }
            }
            else
            {
                upgrade.updateGuiData(con, crafting, id, (short)upgrade.tank.getFluid().fluidID);
                upgrade.updateGuiData(con, crafting, amount1, upgrade.getShortFromInt(true, upgrade.tank.getFluid().amount));
                upgrade.updateGuiData(con, crafting, amount2, upgrade.getShortFromInt(false, upgrade.tank.getFluid().amount));
                changed = true;
            }
        }

        if (changed)
        {
            if (upgrade.tank.getFluid() == null)
            {
                con.olddata = null;
            }
            else
            {
                con.olddata = upgrade.tank.getFluid().copy();
            }
        }
    }

    public void receiveGuiData(TileEntityUpgrade upgrade, int id, short data)
    {
        if (id == 0)
        {
            if (data == -1)
            {
                upgrade.tank.setFluid((FluidStack)null);
            }
            else if (upgrade.tank.getFluid() == null)
            {
                upgrade.tank.setFluid(new FluidStack(data, 0));
            }
        }
        else if (upgrade.tank.getFluid() != null)
        {
            upgrade.tank.getFluid().amount = upgrade.getIntFromShort(id == 1, upgrade.tank.getFluid().amount, data);
        }
    }

    public void init(TileEntityUpgrade upgrade)
    {
        upgrade.tank = new Tank(upgrade, this.getTankSize(), 0);
        upgrade.getCompound().setByte("Tick", (byte)0);
    }

    public void update(TileEntityUpgrade upgrade)
    {
        super.update(upgrade);
        upgrade.getCompound().setByte("Tick", (byte)(upgrade.getCompound().getByte("Tick") - 1));

        if (upgrade.getCompound().getByte("Tick") <= 0)
        {
            upgrade.getCompound().setByte("Tick", (byte)5);

            if (!upgrade.getWorldObj().isRemote && this.slots != null && this.slots.size() >= 2)
            {
                upgrade.tank.containerTransfer();
            }
        }
    }

    public void load(TileEntityUpgrade upgrade, NBTTagCompound compound)
    {
        if (compound.getByte("Exists") != 0)
        {
            upgrade.tank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
        }
        else
        {
            upgrade.tank.setFluid((FluidStack)null);
        }
    }

    public void save(TileEntityUpgrade upgrade, NBTTagCompound compound)
    {
        if (upgrade.tank.getFluid() == null)
        {
            compound.setByte("Exists", (byte)0);
        }
        else
        {
            compound.setByte("Exists", (byte)1);
            upgrade.tank.getFluid().writeToNBT(compound);
        }
    }
}
