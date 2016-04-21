package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.PrintStream;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerDetector;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.Helpers.LogicObject;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiDetector;

public class TileEntityDetector
        extends TileEntityBase
{
    public LogicObject mainObj;

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiDetector(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerDetector(inv, this);
    }

    public TileEntityDetector()
    {
        this.mainObj = new LogicObject((byte)1, (byte)0);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        byte count = nbttagcompound.getByte("LogicObjectCount");
        for (int i = 0; i < count; i++) {
            loadLogicObjectFromInteger(nbttagcompound.getInteger("LogicObject" + i));
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        int count = saveLogicObject(nbttagcompound, this.mainObj, 0, false);
        nbttagcompound.setByte("LogicObjectCount", (byte)count);
    }

    private int saveLogicObject(NBTTagCompound nbttagcompound, LogicObject obj, int id, boolean saveMe)
    {
        if (saveMe) {
            nbttagcompound.setInteger("LogicObject" + id++, saveLogicObjectToInteger(obj));
        }
        for (LogicObject child : obj.getChilds()) {
            id = saveLogicObject(nbttagcompound, child, id, true);
        }
        return id;
    }

    private int saveLogicObjectToInteger(LogicObject obj)
    {
        int returnVal = 0;
        returnVal |= obj.getId() << 24;
        returnVal |= obj.getParent().getId() << 16;
        returnVal |= obj.getExtra() << 8;
        returnVal |= obj.getData() << 0;
        return returnVal;
    }

    private void loadLogicObjectFromInteger(int val)
    {
        byte id = (byte)(val >> 24 & 0xFF);
        byte parent = (byte)(val >> 16 & 0xFF);
        byte extra = (byte)(val >> 8 & 0xFF);
        byte data = (byte)(val >> 0 & 0xFF);

        createObject(id, parent, extra, data);
    }

    private int activeTimer = 20;
    private short oldData;
    private boolean hasOldData;

    public void updateEntity()
    {
        if ((this.activeTimer > 0) &&
                (--this.activeTimer == 0))
        {
            DetectorType.getTypeFromMeta(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)).deactivate(this);
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 0xFFFFFFF7, 3);
        }
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            byte lowestId = -1;
            for (int i = 0; i < 128; i++) {
                if (!isIdOccupied(this.mainObj, i))
                {
                    lowestId = (byte)i;
                    break;
                }
            }
            if (lowestId == -1) {
                return;
            }
            createObject(lowestId, data[0], data[1], data[2]);
        }
        else if (id == 1)
        {
            removeObject(this.mainObj, data[0]);
        }
    }

    private void createObject(byte id, byte parentId, byte extra, byte data)
    {
        LogicObject newObject = new LogicObject(id, extra, data);

        LogicObject parent = getObjectFromId(this.mainObj, parentId);
        if (parent != null) {
            newObject.setParent(parent);
        }
    }

    private LogicObject getObjectFromId(LogicObject object, int id)
    {
        if (object.getId() == id) {
            return object;
        }
        for (LogicObject child : object.getChilds())
        {
            LogicObject result = getObjectFromId(child, id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private boolean removeObject(LogicObject object, int idToRemove)
    {
        if (object.getId() == idToRemove)
        {
            object.setParent(null);
            return true;
        }
        for (LogicObject child : object.getChilds()) {
            if (removeObject(child, idToRemove)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIdOccupied(LogicObject object, int id)
    {
        if (object.getId() == id) {
            return true;
        }
        for (LogicObject child : object.getChilds()) {
            if (isIdOccupied(child, id)) {
                return true;
            }
        }
        return false;
    }

    public void initGuiData(Container con, ICrafting crafting) {}

    public void checkGuiData(Container con, ICrafting crafting)
    {
        sendUpdatedLogicObjects(con, crafting, this.mainObj, ((ContainerDetector)con).mainObj);
    }

    private void sendUpdatedLogicObjects(Container con, ICrafting crafting, LogicObject real, LogicObject cache)
    {
        if (!real.equals(cache))
        {
            LogicObject parent = cache.getParent();
            cache.setParent(null);
            LogicObject clone = real.copy(parent);
            removeLogicObject(con, crafting, cache);
            sendLogicObject(con, crafting, clone);
            cache = clone;
        }
        while (real.getChilds().size() > cache.getChilds().size())
        {
            int i = cache.getChilds().size();
            LogicObject clone = ((LogicObject)real.getChilds().get(i)).copy(cache);
            sendLogicObject(con, crafting, clone);
        }
        while (real.getChilds().size() < cache.getChilds().size())
        {
            int i = real.getChilds().size();
            LogicObject toBeRemoved = (LogicObject)cache.getChilds().get(i);
            toBeRemoved.setParent(null);
            removeLogicObject(con, crafting, toBeRemoved);
        }
        for (int i = 0; i < real.getChilds().size(); i++) {
            sendUpdatedLogicObjects(con, crafting, (LogicObject)real.getChilds().get(i), (LogicObject)cache.getChilds().get(i));
        }
    }

    private void sendAllLogicObjects(Container con, ICrafting crafting, LogicObject obj)
    {
        sendLogicObject(con, crafting, obj);
        for (LogicObject child : obj.getChilds()) {
            sendAllLogicObjects(con, crafting, child);
        }
    }

    private void sendLogicObject(Container con, ICrafting crafting, LogicObject obj)
    {
        if (obj.getParent() == null) {
            return;
        }
        short data = (short)(obj.getId() << 8 | obj.getParent().getId());
        short data2 = (short)(obj.getExtra() << 8 | obj.getData());


        updateGuiData(con, crafting, 0, data);
        updateGuiData(con, crafting, 1, data2);
    }

    private void removeLogicObject(Container con, ICrafting crafting, LogicObject obj)
    {
        updateGuiData(con, crafting, 2, (short)obj.getId());
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.oldData = data;
            this.hasOldData = true;
        }
        else if (id == 1)
        {
            if (!this.hasOldData)
            {
                System.out.println("Doesn't have the other part of the data");
                return;
            }
            byte logicid = (byte)((this.oldData & 0xFF00) >> 8);
            byte parent = (byte)(this.oldData & 0xFF);
            byte extra = (byte)((data & 0xFF00) >> 8);
            byte logicdata = (byte)(data & 0xFF);

            createObject(logicid, parent, extra, logicdata);
            recalculateTree();
            this.hasOldData = false;
        }
        else if (id == 2)
        {
            removeObject(this.mainObj, data);
            recalculateTree();
        }
    }

    public void recalculateTree()
    {
        this.mainObj.generatePosition(5, 60, 245, 0);
    }

    public boolean evaluate(MinecartModular cart, int depth)
    {
        return this.mainObj.evaluateLogicTree(this, cart, depth);
    }

    public void handleCart(MinecartModular cart)
    {
        boolean truthValue = evaluate(cart, 0);

        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        boolean isOn = (meta & 0x8) != 0;
        if (truthValue != isOn)
        {
            if (truthValue)
            {
                DetectorType.getTypeFromMeta(meta).activate(this, cart);
                meta |= 0x8;
            }
            else
            {
                DetectorType.getTypeFromMeta(meta).deactivate(this);
                meta &= 0xFFFFFFF7;
            }
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta, 3);
        }
        if (truthValue) {
            this.activeTimer = 20;
        }
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        }
        return entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }
}
