package vswe.stevescarts.Modules.Workers.Tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ICropModule;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotSeed;

public abstract class ModuleFarmer extends ModuleTool implements ISuppliesModule, ICropModule
{
    private ArrayList<ICropModule> plantModules;
    private int farming;
    private float farmAngle;
    private float rigAngle = -3.926991F;

    public static final DamageSource sliced = new DamageSource("sliced").setDifficultyScaled();

    public ModuleFarmer(MinecartModular cart)
    {
        super(cart);
    }

    protected abstract int getRange();

    public int getExternalRange()
    {
        return this.getRange();
    }

    public void init()
    {
        super.init();
        this.plantModules = new ArrayList();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ICropModule)
            {
                this.plantModules.add((ICropModule)module);
            }
        }
    }

    public byte getWorkPriority()
    {
        return (byte)80;
    }

    public boolean hasGui()
    {
        return true;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.TOOLS.FARMER.translate(new String[0]), 8, 6, 4210752);
    }

    protected int getInventoryWidth()
    {
        return super.getInventoryWidth() + 3;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        if (x == 0)
        {
            return super.getSlot(slotId, x, y);
        }
        else
        {
            --x;
            return new SlotSeed(this.getCart(), this, slotId, 8 + x * 18, 28 + y * 18);
        }
    }

    public boolean work()
    {
        Vec3 next = this.getNextblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;

        for (int i = -this.getRange(); i <= this.getRange(); ++i)
        {
            for (int j = -this.getRange(); j <= this.getRange(); ++j)
            {
                int coordX = x + i;
                int coordY = y - 1;
                int coordZ = z + j;

                if (this.farm(coordX, coordY, coordZ))
                {
                    return true;
                }

                if (this.till(coordX, coordY, coordZ))
                {
                    return true;
                }

                if (this.plant(coordX, coordY, coordZ))
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean till(int x, int y, int z)
    {
        Block b = this.getCart().worldObj.getBlock(x, y, z);

        if (this.getCart().worldObj.isAirBlock(x, y + 1, z) && (b == Blocks.grass || b == Blocks.dirt))
        {
            if (this.doPreWork())
            {
                this.startWorking(10);
                return true;
            }

            this.stopWorking();
            this.getCart().worldObj.setBlock(x, y, z, Blocks.farmland);
        }

        return false;
    }

    protected boolean plant(int x, int y, int z)
    {
        int hasSeeds = -1;
        Block soilblock = this.getCart().worldObj.getBlock(x, y, z);

        if (soilblock != null)
        {
            for (int cropblock = 0; cropblock < this.getInventorySize(); ++cropblock)
            {
                if (this.getStack(cropblock) != null && this.isSeedValidHandler(this.getStack(cropblock)))
                {
                    Block cropblock1 = this.getCropFromSeedHandler(this.getStack(cropblock));

                    if (cropblock1 != null && cropblock1 instanceof IPlantable && this.getCart().worldObj.isAirBlock(x, y + 1, z) && soilblock.canSustainPlant(this.getCart().worldObj, x, y, z, ForgeDirection.UP, (IPlantable)cropblock1))
                    {
                        hasSeeds = cropblock;
                        break;
                    }
                }
            }

            if (hasSeeds != -1)
            {
                if (this.doPreWork())
                {
                    this.startWorking(25);
                    return true;
                }

                this.stopWorking();
                Block var8 = this.getCropFromSeedHandler(this.getStack(hasSeeds));
                this.getCart().worldObj.setBlock(x, y + 1, z, var8);
                --this.getStack(hasSeeds).stackSize;

                if (this.getStack(hasSeeds).stackSize <= 0)
                {
                    this.setStack(hasSeeds, (ItemStack)null);
                }
            }
        }

        return false;
    }

    protected boolean farm(int x, int y, int z)
    {
        if (!this.isBroken())
        {
            Block block = this.getCart().worldObj.getBlock(x, y + 1, z);
            int m = this.getCart().worldObj.getBlockMetadata(x, y + 1, z);

            if (this.isReadyToHarvestHandler(x, y + 1, z))
            {
                int i$1;

                if (this.doPreWork())
                {
                    int stuff1 = this.enchanter != null ? this.enchanter.getEfficiencyLevel() : 0;
                    i$1 = (int)((double)this.getBaseFarmingTime() / Math.pow(1.2999999523162842D, (double)stuff1));
                    this.setFarming(i$1 * 4);
                    this.startWorking(i$1);
                    return true;
                }

                this.stopWorking();
                ArrayList stuff;

                if (this.shouldSilkTouch(block, x, y, z, m))
                {
                    stuff = new ArrayList();
                    ItemStack i$ = this.getSilkTouchedItem(block, m);

                    if (i$ != null)
                    {
                        stuff.add(i$);
                    }
                }
                else
                {
                    i$1 = this.enchanter != null ? this.enchanter.getFortuneLevel() : 0;
                    stuff = block.getDrops(this.getCart().worldObj, x, y + 1, z, m, i$1);
                }

                Iterator i$2 = stuff.iterator();

                while (i$2.hasNext())
                {
                    ItemStack iStack = (ItemStack)i$2.next();
                    this.getCart().addItemToChest(iStack);

                    if (iStack.stackSize != 0)
                    {
                        EntityItem entityitem = new EntityItem(this.getCart().worldObj, this.getCart().posX, this.getCart().posY, this.getCart().posZ, iStack);
                        entityitem.motionX = (double)((float)(x - this.getCart().x()) / 10.0F);
                        entityitem.motionY = 0.15000000596046448D;
                        entityitem.motionZ = (double)((float)(z - this.getCart().z()) / 10.0F);
                        this.getCart().worldObj.spawnEntityInWorld(entityitem);
                    }
                }

                this.getCart().worldObj.setBlockToAir(x, y + 1, z);
                this.damageTool(3);
            }
        }

        return false;
    }

    protected int getBaseFarmingTime()
    {
        return 25;
    }

    public boolean isSeedValidHandler(ItemStack seed)
    {
        Iterator i$ = this.plantModules.iterator();
        ICropModule module;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            module = (ICropModule)i$.next();
        }
        while (!module.isSeedValid(seed));

        return true;
    }

    protected Block getCropFromSeedHandler(ItemStack seed)
    {
        Iterator i$ = this.plantModules.iterator();
        ICropModule module;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            module = (ICropModule)i$.next();
        }
        while (!module.isSeedValid(seed));

        return module.getCropFromSeed(seed);
    }

    protected boolean isReadyToHarvestHandler(int x, int y, int z)
    {
        Iterator i$ = this.plantModules.iterator();
        ICropModule module;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            module = (ICropModule)i$.next();
        }
        while (!module.isReadyToHarvest(x, y, z));

        return true;
    }

    public boolean isSeedValid(ItemStack seed)
    {
        return seed.getItem() == Items.wheat_seeds || seed.getItem() == Items.potato || seed.getItem() == Items.carrot;
    }

    public Block getCropFromSeed(ItemStack seed)
    {
        return seed.getItem() == Items.carrot ? Blocks.carrots : (seed.getItem() == Items.potato ? Blocks.potatoes : (seed.getItem() == Items.wheat_seeds ? Blocks.wheat : null));
    }

    public boolean isReadyToHarvest(int x, int y, int z)
    {
        Block block = this.getCart().worldObj.getBlock(x, y, z);
        int m = this.getCart().worldObj.getBlockMetadata(x, y, z);
        return block instanceof BlockCrops && m == 7;
    }

    public float getFarmAngle()
    {
        return this.farmAngle;
    }

    public float getRigAngle()
    {
        return this.rigAngle;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    private void setFarming(int val)
    {
        this.farming = val;
        this.updateDw(0, (byte)(val > 0 ? 1 : 0));
    }

    protected boolean isFarming()
    {
        return this.isPlaceholder() ? this.getSimInfo().getIsFarming() : this.getCart().isEngineBurning() && this.getDw(0) != 0;
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            this.setFarming(this.farming - 1);

            Vec3 next = this.getLastblock();
            double minx = next.xCoord;
            double miny = this.getCart().boundingBox.minY + 0.225D;
            double minz = next.zCoord;
            double maxx = minx;
            double maxy = miny + 0.475D;
            double maxz = minz;

            if(this.getCart().x() > minx) {
                minx += 1.49D;
                maxx += 1.74D;
                minz -= 0.65D;
                maxz += 1.65D;
            } else if(this.getCart().x() < minx) {
                minx -= 1.49D;
                maxx -= 1.74D;
                minz -= 0.65D;
                maxz += 1.65D;
            } else if(this.getCart().z() > minz) {
                minz += 1.49D;
                maxz += 1.74D;
                minx -= 0.65D;
                maxx += 1.65D;
            } else if(this.getCart().z() < minz) {
                minz -= 1.49D;
                maxz -= 1.74D;
                minx -= 0.65D;
                maxx += 1.65D;
            }
            List<EntityLivingBase> entitiesWithinAABB = this.getCart().worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(minx, miny, minz, maxx, maxy, maxz));
            if(entitiesWithinAABB != null && !entitiesWithinAABB.isEmpty()) {
                for(EntityLivingBase entity : entitiesWithinAABB) {
                    entity.attackEntityFrom(sliced, 8);
                }
            }
        }
        else
        {
            float up = -3.926991F;
            float down = -(float)Math.PI;
            boolean flag = this.isFarming();

            if (flag)
            {
                if (this.rigAngle < down)
                {
                    this.rigAngle += 0.1F;

                    if (this.rigAngle > down)
                    {
                        this.rigAngle = down;
                    }
                }
                else
                {
                    this.farmAngle = (float)((double)(this.farmAngle + 0.15F) % (Math.PI * 2D));
                }
            }
            else if (this.rigAngle > up)
            {
                this.rigAngle -= 0.075F;

                if (this.rigAngle < up)
                {
                    this.rigAngle = up;
                }
            }
        }
    }

    public boolean haveSupplies()
    {
        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            ItemStack item = this.getStack(i);

            if (item != null && this.isSeedValidHandler(item))
            {
                return true;
            }
        }

        return false;
    }
}
