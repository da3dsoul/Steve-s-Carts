package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.astar.AStar;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.Plants.ModulePlantSize;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ITreeModule;
import vswe.stevescarts.Modules.ITreeSizeModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFuel;
import vswe.stevescarts.Slots.SlotSapling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class ModuleWoodcutter extends ModuleTool implements ISuppliesModule, ITreeModule, ITreeSizeModule {
    private ArrayList<ITreeModule> treeModules;
    private ModulePlantSize plantSize;
    private ITreeSizeModule treeSize;
    private boolean isPlanting;
    private float cutterAngle = ((float) Math.PI / 4F);

    private AStar blockSearch;

    public ModuleWoodcutter(MinecartModular cart) {
        super(cart);
    }

    public byte getWorkPriority() {
        return (byte) 80;
    }

    public boolean hasGui() {
        return true;
    }

    public void drawForeground(GuiMinecart gui) {
        this.drawString(gui, Localization.MODULES.TOOLS.CUTTER.translate(new String[0]), 8, 6, 4210752);
    }

    protected int getInventoryWidth() {
        return super.getInventoryWidth() + 3;
    }

    protected SlotBase getSlot(int slotId, int x, int y) {
        if (x == 0) {
            return super.getSlot(slotId, x, y);
        } else {
            --x;
            return new SlotSapling(this.getCart(), this, slotId, 8 + x * 18, 28 + y * 18);
        }
    }

    public boolean useDurability() {
        return true;
    }

    public void init() {
        super.init();
        this.treeModules = new ArrayList();
        Iterator i$ = this.getCart().getModules().iterator();

        treeSize = this;

        while (i$.hasNext()) {
            ModuleBase module = (ModuleBase) i$.next();

            if (module instanceof ITreeModule) {
                this.treeModules.add((ITreeModule) module);
            } else if (module instanceof ModulePlantSize) {
                this.plantSize = (ModulePlantSize) module;
            } else if(module instanceof ITreeSizeModule) {
                this.treeSize = (ITreeSizeModule) module;
            }
        }
        blockSearch = new AStar(this.getCart().worldObj, this);
    }

    public abstract int getPercentageDropChance();

    public ArrayList<ItemStack> getTierDrop(ArrayList<ItemStack> baseItems) {
        ArrayList nerfedItems = new ArrayList();
        Iterator i$ = baseItems.iterator();

        while (i$.hasNext()) {
            ItemStack item = (ItemStack) i$.next();

            if (item != null) {
                this.dropItemByMultiplierChance(nerfedItems, item, this.getPercentageDropChance());
            }
        }

        return nerfedItems;
    }

    private void dropItemByMultiplierChance(ArrayList<ItemStack> items, ItemStack item, int percentage) {
        for (int drop = 0; percentage > 0; percentage -= 100) {
            if (this.getCart().rand.nextInt(100) < percentage) {
                items.add(item.copy());
                ++drop;
            }
        }
    }

    public boolean work() {
        Vec3 next = this.getNextblock();
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;
        int size = this.getPlantSize();
        this.destroyLeaveBlockOnTrack(x, y, z);
        this.destroyLeaveBlockOnTrack(x, y + 1, z);
        int i;
        int j;
        int farmX;
        int farmY;
        int farmZ;

        for (i = -size; i <= size; ++i) {
            if (i != 0) {
                j = i;

                if (i < 0) {
                    j = -size - i - 1;
                }

                farmX = x + (this.getCart().z() != z ? j : 0);
                farmY = y - 1;
                farmZ = z + (this.getCart().x() != x ? j : 0);

                if (this.plant(size, farmX, farmY, farmZ, x, z)) {
                    this.setCutting(false);
                    return true;
                }
            }
        }

        if (!this.isPlanting) {
            for (i = -1; i <= 1; ++i) {
                for (j = -1; j <= 1; ++j) {
                    farmX = x + i;
                    farmY = y - 1;
                    farmZ = z + j;

                    if (this.farm(farmX, farmY, farmZ)) {
                        this.setCutting(true);
                        return true;
                    }
                }
            }
        }

        this.isPlanting = false;
        this.setCutting(false);
        return false;
    }

    private boolean plant(int size, int x, int y, int z, int cx, int cz) {
        if (size == 1) {
            if ((x + z) % 2 == 0) {
                return false;
            }
        } else if (x == cx && x / size % 2 == 0 || z == cz && z / size % 2 == 0) {
            return false;
        }

        int saplingSlotId = -1;
        ItemStack sapling = null;

        for (int i = 0; i < this.getInventorySize(); ++i) {
            SlotBase slot = (SlotBase) this.getSlots().get(i);

            if (slot.containsValidItem()) {
                saplingSlotId = i;
                sapling = this.getStack(i);
                break;
            }
        }

        if (sapling != null) {
            if (this.doPreWork()) {
                if (sapling.tryPlaceItemIntoWorld(this.getFakePlayer(), this.getCart().worldObj, x, y, z, 1, 0.0F, 0.0F, 0.0F)) {
                    if (sapling.stackSize == 0) {
                        this.setStack(saplingSlotId, (ItemStack) null);
                    }

                    this.startWorking(25);
                    this.isPlanting = true;
                    return true;
                }
            } else {
                this.stopWorking();
                this.isPlanting = false;
            }
        }

        return false;
    }

    private boolean farm(int x, int y, int z) {
        if (!this.isBroken()) {
            Block b = this.getCart().worldObj.getBlock(x, y + 1, z);
            this.getCart().worldObj.getBlockMetadata(x, y + 1, z);

            if (b != null && this.isWoodHandler(b, x, y + 1, z)) {

                ChunkCoordinates coord = blockSearch.calcFarthestBlock(new ChunkCoordinates(x, y + 1, z));

                if (this.removeBlock(coord.posX, coord.posY,coord.posZ)) {
                    return true;
                }

                this.stopWorking();
            }
        }

        return false;
    }

    private void recursiveSearch(int i, int j, int k, TreeSet<BlockCoord> checked) {
        BlockCoord here = new BlockCoord(i, j, k);
        checked.add(here);
        Block b = this.getCart().worldObj.getBlock(i, j, k);
        int m = this.getCart().worldObj.getBlockMetadata(i, j, k);

        if (b == null) {
            return;
        } else {
            int first;
            int efficiency;

            if (checked.size() < 9216 && here.getHorizontalDistToCartSquared(this.getCart()) <= treeSize.getMaxHorizontalDistance()) {

                for (int switchY = -1; switchY >= 1; switchY++) {
                    for (first = -1; first <= 1; ++first) {
                        for (efficiency = -1; efficiency <= 1; ++efficiency) {
                            Block entityitem = this.getCart().worldObj.getBlock(i + first, j + switchY, k + efficiency);

                            if (entityitem != null) {
                                if (!this.isWoodHandler(entityitem, i + first, j + switchY, k + efficiency) && !this.isLeavesHandler(entityitem, i + first, j + switchY, k + efficiency)) {
                                    continue;
                                }

                                if (!checked.contains(new BlockCoord(i + first, j + switchY, k + efficiency))) {
                                    this.recursiveSearch(i + first, j + switchY, k + efficiency, checked);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean removeBlock(int i, int j, int k) {
        Block b = this.getCart().worldObj.getBlock(i, j, k);
        int m = this.getCart().worldObj.getBlockMetadata(i, j, k);
        int first;
        int efficiency;

        ArrayList var14;

        if (this.shouldSilkTouch(b, i, j, k, m)) {
            var14 = new ArrayList();
            ItemStack var15 = this.getSilkTouchedItem(b, m);

            if (var15 != null) {
                var14.add(var15);
            }
        } else {
            int var16 = this.enchanter != null ? this.enchanter.getFortuneLevel() : 0;
            var14 = b.getDrops(this.getCart().worldObj, i, j, k, m, var16);
            first = 200;

            if (var16 > 0) {
                first -= 10 << var16;

                if (first < 40) {
                    first = 40;
                }
            }

            if ((m & 3) == 0 && b == Blocks.leaves && this.getCart().rand.nextInt(first) == 0) {
                var14.add(new ItemStack(Items.apple, 1, 0));
            }
        }

        ArrayList var17 = this.getTierDrop(var14);
        boolean var18 = true;

        for (Iterator var19 = var17.iterator(); var19.hasNext(); var18 = false) {
            ItemStack var21 = (ItemStack) var19.next();
            this.getCart().addItemToChest(var21, Slot.class, SlotFuel.class);

            if (var21.stackSize != 0) {
                if (var18) {
                    return false;
                }

                EntityItem var22 = new EntityItem(this.getCart().worldObj, this.getCart().posX, this.getCart().posY, this.getCart().posZ, var21);
                var22.motionX = (double) ((float) (i - this.getCart().x()) / 10.0F);
                var22.motionY = 0.15000000596046448D;
                var22.motionZ = (double) ((float) (k - this.getCart().z()) / 10.0F);
                this.getCart().worldObj.spawnEntityInWorld(var22);
            }
        }

        this.getCart().worldObj.setBlockToAir(i, j, k);
        byte var20;

        if (this.isLeavesHandler(b, i, j, k)) {
            var20 = 2;
            this.damageTool(1);
        } else {
            var20 = 25;
            this.damageTool(5);
        }

        efficiency = this.enchanter != null ? this.enchanter.getEfficiencyLevel() : 0;
        this.startWorking((int) ((double) var20 / Math.pow(1.2999999523162842D, (double) efficiency)));
        return true;
    }

    public void initDw() {
        this.addDw(0, 0);
    }

    public int numberOfDataWatchers() {
        return 1;
    }

    private void setCutting(boolean val) {
        this.updateDw(0, (byte) (val ? 1 : 0));
    }

    protected boolean isCutting() {
        return this.isPlaceholder() ? this.getSimInfo().getIsCutting() : this.getDw(0) != 0;
    }

    public float getCutterAngle() {
        return this.cutterAngle;
    }

    public void update() {
        super.update();
        boolean cuttingflag = this.isCutting();

        if (cuttingflag || this.cutterAngle != ((float) Math.PI / 4F)) {
            boolean flag = false;

            if (!cuttingflag && this.cutterAngle < ((float) Math.PI / 4F)) {
                flag = true;
            }

            this.cutterAngle = (float) ((double) (this.cutterAngle + 0.9F) % (Math.PI * 2D));

            if (!cuttingflag && this.cutterAngle > ((float) Math.PI / 4F) && flag) {
                this.cutterAngle = ((float) Math.PI / 4F);
            }
        }
    }

    public boolean haveSupplies() {
        for (int i = 0; i < this.getInventorySize(); ++i) {
            if (((SlotBase) this.getSlots().get(i)).containsValidItem()) {
                return true;
            }
        }

        return false;
    }

    public int getMaxHorizontalDistance() { return 175;}

    public int getMaxHorizontalDistanceWithModule() {
        return this.treeSize.getMaxHorizontalDistance();
    }

    public boolean isLeavesHandler(Block b, int x, int y, int z) {
        Iterator i$ = this.treeModules.iterator();
        ITreeModule module;

        try {
            do {
                if (!i$.hasNext()) {
                    return false;
                }

                module = (ITreeModule) i$.next();
            }
            while (!module.isLeaves(b, x, y, z));
        } catch (Throwable t) { return false; }

        return true;
    }

    public boolean isWoodHandler(Block b, int x, int y, int z) {
        Iterator i$ = this.treeModules.iterator();
        ITreeModule module;

        do {
            if (!i$.hasNext()) {
                return false;
            }

            module = (ITreeModule) i$.next();
        }
        while (!module.isWood(b, x, y, z));

        return true;
    }

    public boolean isSaplingHandler(ItemStack sapling) {
        Iterator i$ = this.treeModules.iterator();
        ITreeModule module;

        do {
            if (!i$.hasNext()) {
                return false;
            }

            module = (ITreeModule) i$.next();
        }
        while (!module.isSapling(sapling));

        return true;
    }

    public boolean isLeaves(Block b, int x, int y, int z) {
        return b == Blocks.leaves;
    }

    public boolean isWood(Block b, int x, int y, int z) {
        return b == Blocks.log || b == Blocks.log2;
    }

    public boolean isSapling(ItemStack sapling) {
        return sapling != null && Block.getBlockFromItem(sapling.getItem()) == Blocks.sapling;
    }

    private int getPlantSize() {
        return this.plantSize != null ? this.plantSize.getSize() : 1;
    }

    private void destroyLeaveBlockOnTrack(int x, int y, int z) {
        Block b = this.getCart().worldObj.getBlock(x, y, z);

        if (b != null && this.isLeavesHandler(b, x, y, z)) {
            this.getCart().worldObj.setBlockToAir(x, y, z);
        }
    }
}
