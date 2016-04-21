package vswe.stevescarts.Modules.Workers;

import java.util.ArrayList;

import mods.railcraft.api.core.items.ITrackItem;
import mods.railcraft.api.tracks.ITrackInstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.ModuleHeightControl;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotBuilder;

public class ModuleRailer extends ModuleWorker implements ISuppliesModule
{
    private boolean hasGeneratedAngles = false;
    private float[] railAngles;

    public ModuleRailer(MinecartModular cart)
    {
        super(cart);
    }

    public boolean hasGui()
    {
        return true;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotBuilder(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.RAILER.translate(new String[0]), 8, 6, 4210752);
    }

    public byte getWorkPriority()
    {
        return (byte)100;
    }

    public boolean work()
    {
        Vec3 next = this.getNextblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;

        //if(BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z)) return false;
        //if(BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y - 1, z)) return false;
        //if(BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y + 1, z)) return false;

        ArrayList pos = this.getValidRailPositions(x, y, z);

        if (this.doPreWork())
        {
            boolean var9 = false;

            for (int front = 0; front < pos.size(); ++front)
            {
                if (this.tryPlaceTrack(((Integer[])pos.get(front))[0].intValue(), ((Integer[])pos.get(front))[1].intValue(), ((Integer[])pos.get(front))[2].intValue(), false))
                {
                    var9 = true;
                    break;
                }
            }

            if (var9)
            {
                this.startWorking(12);
            }
            else
            {
                boolean var10 = false;

                for (int i1 = -1; i1 <= 1; i1++)
                {
                    if (BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y + i1, z))
                    {
                        var10 = true;
                        break;
                    }
                }

                if (!var10)
                {
                    this.getCart().turnback();
                }
            }

            return true;
        }
        else
        {
            this.stopWorking();

            for (int i = 0; i < pos.size() && !this.tryPlaceTrack(((Integer[])pos.get(i))[0].intValue(), ((Integer[])pos.get(i))[1].intValue(), ((Integer[])pos.get(i))[2].intValue(), true); ++i)
            {
                ;
            }

            return false;
        }
    }

    protected ArrayList<Integer[]> getValidRailPositions(int x, int y, int z)
    {
        ArrayList<Integer[]> lst = new ArrayList();

        boolean hasHeightController = false;
        for(ModuleBase mod : this.getCart().getModules()) {
            if(mod instanceof ModuleHeightControl) {
                hasHeightController = true;
            }
        }

        if(hasHeightController) {
            if (this.getCart().getYTarget() > y) {
                lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y + 1), Integer.valueOf(z)});
                lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
            } else if(this.getCart().getYTarget() < y) {
                lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y - 1), Integer.valueOf(z)});
            } else {
                lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
            }

            return lst;
        }



        if (y >= this.getCart().y())
        {
            lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y + 1), Integer.valueOf(z)});
        }

        lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
        lst.add(new Integer[] {Integer.valueOf(x), Integer.valueOf(y - 1), Integer.valueOf(z)});
        return lst;
    }

    protected boolean validRail(Item item)
    {
        return Block.getBlockFromItem(item) instanceof BlockRailBase || item instanceof ITrackItem;
    }
    
    protected boolean isRailcraftRail(Item item)
    {
    	return item instanceof ITrackItem;
    }

    private boolean tryPlaceTrack(int i, int j, int k, boolean flag)
    {
        if (this.isValidForTrack(i, j, k, true))
        {
            for (int l = 0; l < this.getInventorySize(); ++l)
            {
                if (this.getStack(l) != null && this.validRail(this.getStack(l).getItem()))
                {
                    if (flag)
                    {
                    	if(isRailcraftRail(this.getStack(l).getItem()))
                    	{
                    		if(!((ITrackItem)this.getStack(l).getItem()).placeTrack(this.getStack(l), this.getCart().worldObj, i, j, k))
                    			return false;
                    	}else
                    	{
                    		if(!this.getCart().worldObj.setBlock(i, j, k, Block.getBlockFromItem(this.getStack(l).getItem()), ((ItemBlock)((ItemBlock)this.getStack(l).getItem())).getMetadata(this.getStack(l).getItemDamage()), 3))
                    			return false;
                    	}

                        if (!this.getCart().hasCreativeSupplies())
                        {
                            --this.getStack(l).stackSize;

                            if (this.getStack(l).stackSize == 0)
                            {
                                this.setStack(l, (ItemStack)null);
                            }

                            this.getCart().onInventoryChanged();
                        }
                    }

                    return true;
                }
            }

            this.turnback();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        this.calculateRails();
    }

    private void calculateRails()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            byte valid = 0;

            for (int i = 0; i < this.getInventorySize(); ++i)
            {
                if (this.getStack(i) != null && this.validRail(this.getStack(i).getItem()))
                {
                    ++valid;
                }
            }

            this.updateDw(0, valid);
        }
    }

    public int getRails()
    {
        return this.isPlaceholder() ? this.getSimInfo().getRailCount() : this.getDw(0);
    }

    public float getRailAngle(int i)
    {
        if (!this.hasGeneratedAngles)
        {
            this.railAngles = new float[this.getInventorySize()];

            for (int j = 0; j < this.getInventorySize(); ++j)
            {
                this.railAngles[j] = this.getCart().rand.nextFloat() / 2.0F - 0.25F;
            }

            this.hasGeneratedAngles = true;
        }

        return this.railAngles[i];
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.calculateRails();
    }

    public boolean haveSupplies()
    {
        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            ItemStack item = this.getStack(i);

            if (item != null && this.validRail(item.getItem()))
            {
                return true;
            }
        }

        return false;
    }
}
