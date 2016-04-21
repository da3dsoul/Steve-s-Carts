package vswe.stevescarts.Modules.Workers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.ModuleBuilderIntelligence;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotBridge;

import java.util.ArrayList;
import java.util.Iterator;

public class ModuleBridgeLarge extends ModuleBridge implements ISuppliesModule
{

    public ModuleBridgeLarge(MinecartModular cart)
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

    protected int getInventoryHeight()
    {
        return 3;
    }

    // Drill still breaks bridge. Bridge builds one side and stops. Will try to place bridge up when there is a rail. FIX.

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotBridge(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    private ModuleBuilderIntelligence intelligence;

    public void init()
    {
        super.init();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleBuilderIntelligence)
            {
                this.intelligence = (ModuleBuilderIntelligence)module;
            }
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public boolean work() {
        Vec3 next = this.getNextblock();
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;
        int yLocation;

        if (this.getCart().getYTarget() > y) {
            yLocation = y;
        } else if (this.getCart().getYTarget() < y) {
            yLocation = y - 2;
        } else {
            yLocation = y - 1;
        }

        if(BlockRailBase.func_150049_b_(this.getCart().worldObj, x,y,z)) {
            yLocation = y - 1;
        } else if(BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y - 1, z)) {
            yLocation = y - 2;
        }

        int range = 1;

        ArrayList<ModuleBase> modules = this.getCart().getModules();
        for (ModuleBase mod : modules) {
            if (mod instanceof ModuleDrill) {
                range = ((ModuleDrill) mod).blocksOnSide();
                break;
            }
        }

        for (int holeX = -range; holeX <= range; holeX++) {

            int coordX = x + (this.getCart().z() != z ? holeX : 0);
            int coordZ = z + (this.getCart().x() != x ? holeX : 0);

            if (this.intelligence == null || this.intelligence.isActive(holeX + range)) {
                if (checkBridgeNeeded(coordX, yLocation, coordZ)) {
                    return true;
                }
            }
        }

        this.setBridge(false);

        return false;
    }



    @Override
    public boolean preventAutoShutdown() {
        return true;
    }

    private boolean checkBridgeNeeded(int x, int yLocation, int z)
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
                        boolean flag2 = this.getCart().worldObj.setBlock(i, j, k, Block.getBlockFromItem(this.getStack(m).getItem()), ((ItemBlock) this.getStack(m).getItem()).getMetadata(this.getStack(m).getItemDamage()), 3);

                        if (!this.getCart().hasCreativeSupplies() && flag2)
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
