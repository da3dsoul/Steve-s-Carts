package vswe.stevescarts.Modules.Workers;

import mods.railcraft.api.core.items.ITrackItem;
import mods.railcraft.api.tracks.ITrackInstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleRemover extends ModuleWorker
{
    private int removeX;
    private int removeY = -1;
    private int removeZ;

    public ModuleRemover(MinecartModular cart)
    {
        super(cart);
    }

    public byte getWorkPriority()
    {
        return (byte)120;
    }

    protected boolean preventTurnback()
    {
        return true;
    }

    public boolean work()
    {
        if (this.removeY != -1 && (this.removeX != this.getCart().x() || this.removeZ != this.getCart().z()) && this.removeRail(this.removeX, this.removeY, this.removeZ, true))
        {
            return false;
        }
        else
        {
            Vec3 next = this.getNextblock();
            Vec3 last = this.getLastblock();
            boolean front = this.isRailAtCoords(next);
            boolean back = this.isRailAtCoords(last);

            if (!front)
            {
                if (back)
                {
                    this.turnback();

                    if (this.removeRail(this.getCart().x(), this.getCart().y(), this.getCart().z(), false))
                    {
                        return true;
                    }
                }
            }
            else if (!back && this.removeRail(this.getCart().x(), this.getCart().y(), this.getCart().z(), false))
            {
                return true;
            }

            return false;
        }
    }

    private boolean isRailAtCoords(Vec3 coords)
    {
        int x = (int)coords.xCoord;
        int y = (int)coords.yCoord;
        int z = (int)coords.zCoord;
        return BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y + 1, z) || BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z) || BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y - 1, z);
    }

    private boolean removeRail(int x, int y, int z, boolean flag)
    {
        if (flag)
        {
            if (BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z))
            {
                if (this.doPreWork())
                {
                    this.startWorking(12);
                    return true;
                }

                int rInt = this.getCart().rand.nextInt(100);
                ItemStack iStack = this.getCart().worldObj.getBlock(x, y, z).getDrops(this.getCart().worldObj, x, y, z, this.getCart().worldObj.getBlockMetadata(x, y, z), 0).get(0);
               	if(iStack == null) return false;
                this.getCart().addItemToChest(iStack);

                if (iStack.stackSize == 0)
                {
                    this.getCart().worldObj.setBlockToAir(x, y, z);
                }

                this.removeY = -1;
            }
            else
            {
                this.removeY = -1;
            }
        }
        else if (BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y - 1, z))
        {
            this.removeX = x;
            this.removeY = y - 1;
            this.removeZ = z;
        }
        else if (BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z))
        {
            this.removeX = x;
            this.removeY = y;
            this.removeZ = z;
        }
        else if (BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y + 1, z))
        {
            this.removeX = x;
            this.removeY = y + 1;
            this.removeZ = z;
        }

        this.stopWorking();
        return false;
    }
}
