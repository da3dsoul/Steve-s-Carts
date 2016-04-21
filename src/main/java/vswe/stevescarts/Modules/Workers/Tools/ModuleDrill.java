package vswe.stevescarts.Modules.Workers.Tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.*;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.Modules.Workers.ModuleRailer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;

public abstract class ModuleDrill extends ModuleTool implements IActivatorModule
{
    private ModuleDrillIntelligence intelligence;
    private ModuleLiquidSensors liquidsensors;
    private ModuleOreTracker tracker;
    private byte sensorLight = 1;
    private float drillRotation;
    private int miningCoolDown;
    private int[] buttonRect = new int[] {15, 30, 24, 12};

    public ModuleDrill(MinecartModular cart)
    {
        super(cart);
    }

    public byte getWorkPriority()
    {
        return (byte)50;
    }

    public void init()
    {
        super.init();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleDrillIntelligence)
            {
                this.intelligence = (ModuleDrillIntelligence)module;
            }

            if (module instanceof ModuleLiquidSensors)
            {
                this.liquidsensors = (ModuleLiquidSensors)module;
            }

            if (module instanceof ModuleOreTracker)
            {
                this.tracker = (ModuleOreTracker)module;
            }
        }
    }

    public boolean work()
    {
        if (!this.isDrillEnabled())
        {
            this.stopDrill();
            this.stopWorking();
            return false;
        }
        else
        {
            if (!this.doPreWork())
            {
                this.stopDrill();
                this.stopWorking();
            }

            if (this.isBroken())
            {
                return false;
            }
            else
            {
                Vec3 next = this.getNextblock();
                int x = (int)next.xCoord;
                int y = (int)next.yCoord;
                int z = (int)next.zCoord;
                int[] range = this.mineRange();

                for (int holeY = range[1]; holeY >= range[0]; --holeY)
                {
                    for (int holeX = -this.blocksOnSide(); holeX <= this.blocksOnSide(); ++holeX)
                    {
                        if (this.intelligence == null || this.intelligence.isActive(holeX + this.blocksOnSide(), holeY, range[2], x > this.getCart().x() || z < this.getCart().z()))
                        {
                            int coordX = x + (this.getCart().z() != z ? holeX : 0);
                            int coordY = y + holeY;
                            int coordZ = z + (this.getCart().x() != x ? holeX : 0);

                            if (this.mineBlockAndRevive(coordX, coordY, coordZ, next, holeX, holeY))
                            {
                                return true;
                            }
                        }
                    }
                }

                if (this.countsAsAir(x, y + range[0], z) && !this.isValidForTrack(x, y + range[0], z, true) && this.mineBlockAndRevive(x, y + (range[0] - 1), z, next, 0, range[0] - 1))
                {
                    return true;
                }
                else
                {
                    this.stopWorking();
                    this.stopDrill();
                    stopRailer();
                    return false;
                }
            }
        }
    }

    private void stopRailer() {
        for (ModuleBase mod : this.getCart().getModules()) {
            if (mod instanceof ModuleRailer) {
                ((ModuleRailer) mod).kill();
                break;
            }
        }
    }

    protected int[] mineRange()
    {
        Vec3 next = this.getNextblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;
        int yTarget = this.getCart().getYTarget();
        return !BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z) && !BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y - 1, z) ? (y > yTarget ? new int[] { -1, this.blocksOnTop() - 1, 1}: (y < yTarget ? new int[] {1, this.blocksOnTop() + 1, 0}: new int[] {0, this.blocksOnTop() - 1, 1})): new int[] {0, this.blocksOnTop() - 1, 1};
    }

    public abstract int blocksOnTop();

    public abstract int blocksOnSide();

    public int getAreaWidth()
    {
        return this.blocksOnSide() * 2 + 1;
    }

    public int getAreaHeight()
    {
        return this.blocksOnTop();
    }

    private boolean mineBlockAndRevive(int coordX, int coordY, int coordZ, Vec3 next, int holeX, int holeY)
    {
        try {
            if (this.mineBlock(coordX, coordY, coordZ, next, holeX, holeY, false)) {
                return true;
            } else if (this.isDead()) {
                this.revive();
                return true;
            } else {
                return false;
            }
        } catch(Throwable t) {
            t.printStackTrace();
            if (this.isDead()) {
                this.revive();
                return true;
            } else {
                return false;
            }
        }
    }

    protected boolean mineBlock(int coordX, int coordY, int coordZ, Vec3 next, int holeX, int holeY, boolean flag)
    {
        if (this.tracker != null)
        {
            BlockCoord valid = new BlockCoord(coordX, coordY, coordZ);
            BlockCoord storage = this.tracker.findBlockToMine(this, valid);

            if (storage != null)
            {
                this.getCart().currentTool = StatCollector.translateToLocal("item.SC2:" + tracker.getData().getRawName() + ".name");
                coordX = storage.getX();
                coordY = storage.getY();
                coordZ = storage.getZ();
            }
        }

        Object var18 = this.isValidBlock(coordX, coordY, coordZ, holeX, holeY, flag);
        TileEntity var19 = null;

        if (var18 instanceof TileEntity)
        {
            var19 = (TileEntity)var18;
        }
        else if (var18 == null)
        {
            return false;
        }

        Block b = this.getCart().worldObj.getBlock(coordX, coordY, coordZ);
        int m = this.getCart().worldObj.getBlockMetadata(coordX, coordY, coordZ);
        float h = b.getBlockHardness(this.getCart().worldObj, coordX, coordY, coordZ);

        if (h < 0.0F)
        {
            h = 0.0F;
        }

        int fortune;

        if (var19 != null)
        {
            for (fortune = 0; fortune < ((IInventory)var19).getSizeInventory(); ++fortune)
            {
                ItemStack iStack = ((IInventory)var19).getStackInSlot(fortune);

                if (iStack != null)
                {
                    if (!this.minedItem(iStack, next))
                    {
                        return false;
                    }

                    ((IInventory)var19).setInventorySlotContents(fortune, (ItemStack)null);
                }
            }
        }

        fortune = this.enchanter != null ? this.enchanter.getFortuneLevel() : 0;

        if (this.shouldSilkTouch(b, coordX, coordY, coordZ, m))
        {
            ItemStack stacks = this.getSilkTouchedItem(b, m);

            if (stacks != null && !this.minedItem(stacks, next))
            {
                return false;
            }

            this.getCart().worldObj.setBlockToAir(coordX, coordY, coordZ);
        }
        else if (b.getDrops(this.getCart().worldObj, coordX, coordY, coordZ, m, fortune).size() != 0)
        {
            ArrayList var20 = b.getDrops(this.getCart().worldObj, coordX, coordY, coordZ, m, fortune);
            boolean shouldRemove = false;

            for (int i = 0; i < var20.size(); ++i)
            {
                if (!this.minedItem((ItemStack)var20.get(i), next))
                {
                    return false;
                }

                shouldRemove = true;
            }

            if (shouldRemove)
            {
                this.getCart().worldObj.setBlockToAir(coordX, coordY, coordZ);
            }
        }
        else
        {
            this.getCart().worldObj.setBlockToAir(coordX, coordY, coordZ);
        }

        this.damageTool(1 + (int)h);
        this.startWorking(this.getTimeToMine(h));
        this.startDrill();
        return true;
    }

    protected boolean minedItem(ItemStack iStack, Vec3 Coords)
    {
        if (iStack != null && iStack.stackSize > 0)
        {
            Iterator size = this.getCart().getModules().iterator();

            while (size.hasNext())
            {
                ModuleBase hasChest = (ModuleBase)size.next();

                if (hasChest instanceof ModuleIncinerator)
                {
                    ((ModuleIncinerator)hasChest).incinerate(iStack);

                    if (iStack.stackSize <= 0)
                    {
                        return true;
                    }
                }
            }

            int size1 = iStack.stackSize;
            this.getCart().addItemToChest(iStack);

            if (iStack.stackSize == 0)
            {
                return true;
            }
            else
            {
                boolean hasChest1 = false;
                Iterator entityitem = this.getCart().getModules().iterator();

                while (entityitem.hasNext())
                {
                    ModuleBase module = (ModuleBase)entityitem.next();

                    if (module instanceof ModuleChest)
                    {
                        hasChest1 = true;
                        break;
                    }
                }

                EntityItem entityitem1;

                if (hasChest1)
                {
                    if (iStack.stackSize != size1)
                    {
                        entityitem1 = new EntityItem(this.getCart().worldObj, this.getCart().posX, this.getCart().posY, this.getCart().posZ, iStack);
                        entityitem1.motionX = (double)((float)((double)this.getCart().z() - Coords.zCoord) / 10.0F);
                        entityitem1.motionY = 0.15000000596046448D;
                        entityitem1.motionZ = (double)((float)((double)this.getCart().x() - Coords.xCoord) / 10.0F);
                        this.getCart().worldObj.spawnEntityInWorld(entityitem1);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    entityitem1 = new EntityItem(this.getCart().worldObj, this.getCart().posX, this.getCart().posY, this.getCart().posZ, iStack);
                    entityitem1.motionX = (double)((float)((double)this.getCart().x() - Coords.xCoord) / 10.0F);
                    entityitem1.motionY = 0.15000000596046448D;
                    entityitem1.motionZ = (double)((float)((double)this.getCart().z() - Coords.zCoord) / 10.0F);
                    this.getCart().worldObj.spawnEntityInWorld(entityitem1);
                    return true;
                }
            }
        }
        else
        {
            return true;
        }
    }

    private int getTimeToMine(float hardness)
    {
        int efficiency = this.enchanter != null ? this.enchanter.getEfficiencyLevel() : 0;
        return (int)((double)(this.getTimeMult() * hardness) / Math.pow(1.2999999523162842D, (double)efficiency)) + (this.liquidsensors != null ? 2 : 0);
    }

    protected abstract float getTimeMult();

    public Object isValidBlock(int x, int y, int z, int i, int j, boolean flag)
    {
        if ((flag || !BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y, z)) && !BlockRailBase.func_150049_b_(this.getCart().worldObj, x, y + 1, z))
        {
            Block b = this.getCart().worldObj.getBlock(x, y, z);
            this.getCart().worldObj.getBlockMetadata(x, y, z);

            if (b == null)
            {
                return null;
            }
            else if (b == Blocks.air)
            {
                return null;
            }
            else if (b == Blocks.bedrock)
            {
                return null;
            }
            else if (b instanceof BlockLiquid)
            {
                return null;
            }
            else if (b.getBlockHardness(this.getCart().worldObj, x, y, z) < 0.0F)
            {
                return null;
            }
            else if ((i != 0 || j > 0) && (b == Blocks.torch || b == Blocks.redstone_wire || b == Blocks.redstone_torch || b == Blocks.unlit_redstone_torch || b == Blocks.powered_repeater || b == Blocks.unpowered_repeater || b == Blocks.powered_comparator || b == Blocks.unpowered_comparator || b == ModBlocks.MODULE_TOGGLER.getBlock() || isLightingBlock(b, x, y, z)))
            {
                return null;
            }
            else
            {
                if (b instanceof BlockContainer)
                {
                    TileEntity tileentity = this.getCart().worldObj.getTileEntity(x, y, z);

                    if (tileentity != null && IInventory.class.isInstance(tileentity))
                    {
                        if (i == 0 && j <= 0)
                        {
                            return tileentity;
                        }

                        return null;
                    }
                }

                if (this.liquidsensors != null)
                {
                    if (this.liquidsensors.isDangerous(this, x, y, z, 0, 1, 0) || this.liquidsensors.isDangerous(this, x, y, z, 1, 0, 0) || this.liquidsensors.isDangerous(this, x, y, z, -1, 0, 0) || this.liquidsensors.isDangerous(this, x, y, z, 0, 0, 1) || this.liquidsensors.isDangerous(this, x, y, z, 0, 0, -1))
                    {
                        this.sensorLight = 3;
                        return null;
                    }

                    this.sensorLight = 2;
                }

                return Boolean.valueOf(false);
            }
        }
        else
        {
            return null;
        }
    }

    private boolean isLightingBlock(Block block, int x, int y, int z) {
        for(ModuleBase mod : this.getCart().getModules()) {
            if(mod instanceof ModuleTorch) {
                for(int i = 0; i < mod.getInventorySize(); i++) {
                    ItemStack stack = mod.getStack(i);
                    if(stack == null) continue;
                    ArrayList<ItemStack> list = block.getDrops(this.getCart().worldObj, x, y, z, this.getCart().worldObj.getBlockMetadata(x,y,z), this.enchanter != null ? this.enchanter.getFortuneLevel() : 0);
                    for(ItemStack stack1 : list) {
                        if(stack1 == null) continue;
                        if(stack1.isItemEqual(stack)) return true;
                    }

                    Block block1 = Block.getBlockFromItem(stack.getItem());
                    if(block1 != null) {
                        return block == block1;
                    }
                }
                break;
            }
        }
        return false;
    }

    public void update()
    {
        super.update();

        if (this.getCart().hasFuel() && this.isMining() || this.miningCoolDown < 10)
        {
            this.drillRotation = (float)((double)(this.drillRotation + 0.03F * (float)(10 - this.miningCoolDown)) % (Math.PI * 2D));

            if (this.isMining())
            {
                this.miningCoolDown = 0;
            }
            else
            {
                ++this.miningCoolDown;
            }
        }

        if (!this.getCart().worldObj.isRemote && this.liquidsensors != null)
        {
            byte data = this.sensorLight;

            if (this.isDrillSpinning())
            {
                data = (byte)(data | 4);
            }

            this.liquidsensors.getInfoFromDrill(data);
            this.sensorLight = 1;
        }
    }

    protected void startDrill()
    {
        this.updateDw(0, 1);
    }

    protected void stopDrill()
    {
        this.updateDw(0, 0);
    }

    protected boolean isMining()
    {
        return this.isPlaceholder() ? this.getSimInfo().getDrillSpinning() : this.getDw(0) != 0;
    }

    protected boolean isDrillSpinning()
    {
        return this.isMining() || this.miningCoolDown < 10;
    }

    public void initDw()
    {
        this.addDw(0, 0);
        this.addDw(1, 1);
    }

    public int numberOfDataWatchers()
    {
        return 2;
    }

    public float getDrillRotation()
    {
        return this.drillRotation;
    }

    private boolean isDrillEnabled()
    {
        return this.getDw(1) != 0;
    }

    public void setDrillEnabled(boolean val)
    {
        this.updateDw(1, val ? 1 : 0);
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && this.inRect(x, y, this.buttonRect))
        {
            this.sendPacket(0);
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.setDrillEnabled(!this.isDrillEnabled());
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public boolean hasGui()
    {
        return true;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.TOOLS.DRILL.translate(new String[0]), 8, 6, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        super.drawBackground(gui, x, y);
        ResourceHelper.bindResource("/gui/drill.png");
        int imageID = this.isDrillEnabled() ? 1 : 0;
        byte borderID = 0;

        if (this.inRect(x, y, this.buttonRect))
        {
            borderID = 1;
        }

        this.drawImage(gui, this.buttonRect, 0, this.buttonRect[3] * borderID);
        int srcY = this.buttonRect[3] * 2 + imageID * (this.buttonRect[3] - 2);
        this.drawImage(gui, this.buttonRect[0] + 1, this.buttonRect[1] + 1, 0, srcY, this.buttonRect[2] - 2, this.buttonRect[3] - 2);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        super.drawMouseOver(gui, x, y);
        this.drawStringOnMouseOver(gui, this.getStateName(), x, y, this.buttonRect);
    }

    private String getStateName()
    {
        return Localization.MODULES.TOOLS.TOGGLE.translate(new String[] {this.isDrillEnabled() ? "1" : "0"});
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setBoolean(this.generateNBTName("DrillEnabled", id), this.isDrillEnabled());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.setDrillEnabled(tagCompound.getBoolean(this.generateNBTName("DrillEnabled", id)));
    }

    public void doActivate(int id)
    {
        this.setDrillEnabled(true);
    }

    public void doDeActivate(int id)
    {
        this.setDrillEnabled(false);
    }

    public boolean isActive(int id)
    {
        return this.isDrillEnabled();
    }
}
