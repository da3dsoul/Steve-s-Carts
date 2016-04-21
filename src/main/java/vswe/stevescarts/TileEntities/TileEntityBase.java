package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.tileentity.TileEntity;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Interfaces.GuiBase;

public abstract class TileEntityBase extends TileEntity
{
    public void receivePacket(int id, byte[] data, EntityPlayer player) {}

    @SideOnly(Side.CLIENT)
    public abstract GuiBase getGui(InventoryPlayer var1);

    public abstract ContainerBase getContainer(InventoryPlayer var1);

    public void updateGuiData(Container con, ICrafting crafting, int id, short data)
    {
        crafting.sendProgressBarUpdate(con, id, data);
    }

    public void initGuiData(Container con, ICrafting crafting) {}

    public void checkGuiData(Container con, ICrafting crafting) {}

    public void receiveGuiData(int id, short data) {}

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public short getShortFromInt(boolean first, int val)
    {
        return first ? (short)(val & 65535) : (short)(val >> 16 & 65535);
    }

    public int getIntFromShort(boolean first, int oldVal, short val)
    {
        if (first)
        {
            oldVal = oldVal & -65536 | val;
        }
        else
        {
            oldVal = oldVal & 65535 | val << 16;
        }

        return oldVal;
    }
}
