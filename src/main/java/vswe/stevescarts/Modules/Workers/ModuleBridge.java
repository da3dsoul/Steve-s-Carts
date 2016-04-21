package vswe.stevescarts.Modules.Workers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotBridge;

public class ModuleBridge extends ModuleWorker implements ISuppliesModule
{
    public ModuleBridge(MinecartModular cart)
    {
        super(cart);
    }

    public boolean hasGui()
    {
        return true;
    }

    public int guiWidth()
    {
        return 80;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotBridge(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public byte getWorkPriority()
    {
        return (byte)98;
    }

    public boolean work()
    {
        Vec3 next = this.getNextblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;
        int yLocation;

        if (this.getCart().getYTarget() > y)
        {
            yLocation = y;
        }
        else if (this.getCart().getYTarget() < y)
        {
            yLocation = y - 2;
        }
        else
        {
            yLocation = y - 1;
        }

        if (!BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z) && !BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y - 1, z))
        {
            if (this.doPreWork())
            {
                if (this.tryBuildBridge(x, yLocation, z, false))
                {
                    this.startWorking(22);
                    this.setBridge(true);
                    return true;
                }
            }
            else if (this.tryBuildBridge(x, yLocation, z, true))
            {
                this.stopWorking();
            }
        }

        this.setBridge(false);
        return false;
    }

    private boolean tryBuildBridge(int i, int j, int k, boolean flag)
    {
        Block b = this.getCart().worldObj.getBlock(i, j, k);

        if ((this.countsAsAir(i, j, k) || b instanceof BlockLiquid) && this.isValidForTrack(i, j + 1, k, false))
        {
            for (int m = 0; m < this.getInventorySize(); ++m)
            {
                if (this.getStack(m) != null && SlotBridge.isBridgeMaterial(this.getStack(m)))
                {
                    if (flag)
                    {
                        this.getCart().worldObj.setBlock(i, j, k, Block.getBlockFromItem(this.getStack(m).getItem()), ((ItemBlock)((ItemBlock)this.getStack(m).getItem())).getMetadata(this.getStack(m).getItemDamage()), 3);

                        if (!this.getCart().hasCreativeSupplies())
                        {
                            --this.getStack(m).stackSize;

                            if (this.getStack(m).stackSize == 0)
                            {
                                this.setStack(m, (ItemStack)null);
                            }

                            this.getCart().onInventoryChanged();
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    private void setBridge(boolean val)
    {
        this.updateDw(0, val ? 1 : 0);
    }

    public boolean needBridge()
    {
        return this.isPlaceholder() ? this.getSimInfo().getNeedBridge() : this.getDw(0) != 0;
    }

    public boolean haveSupplies()
    {
        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            ItemStack item = this.getStack(i);

            if (item != null && SlotBridge.isBridgeMaterial(item))
            {
                return true;
            }
        }

        return false;
    }
}
