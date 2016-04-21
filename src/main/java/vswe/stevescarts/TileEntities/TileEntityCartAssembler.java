package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Blocks.BlockCartAssembler;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerCartAssembler;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.DropDownMenuItem;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ManagerTransfer;
import vswe.stevescarts.Helpers.NBTHelper;
import vswe.stevescarts.Helpers.SimulationInfo;
import vswe.stevescarts.Helpers.TitleBox;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiCartAssembler;
import vswe.stevescarts.Items.ItemCarts;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleDataHull;
import vswe.stevescarts.Slots.SlotAssembler;
import vswe.stevescarts.Slots.SlotAssemblerFuel;
import vswe.stevescarts.Slots.SlotHull;
import vswe.stevescarts.Slots.SlotOutput;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;
import vswe.stevescarts.Upgrades.CombustionFuel;
import vswe.stevescarts.Upgrades.Deployer;
import vswe.stevescarts.Upgrades.Disassemble;
import vswe.stevescarts.Upgrades.FuelCapacity;
import vswe.stevescarts.Upgrades.FuelCost;
import vswe.stevescarts.Upgrades.Manager;
import vswe.stevescarts.Upgrades.TimeFlat;
import vswe.stevescarts.Upgrades.TimeFlatCart;
import vswe.stevescarts.Upgrades.TimeFlatRemoved;
import vswe.stevescarts.Upgrades.WorkEfficiency;

public class TileEntityCartAssembler extends TileEntityBase implements IInventory, ISidedInventory
{
    private int maxAssemblingTime;
    private float currentAssemblingTime = -1.0F;
    protected ItemStack outputItem;
    protected ArrayList<ItemStack> spareModules = new ArrayList();
    private boolean isAssembling;
    public boolean isErrorListOutdated;
    private ArrayList<TitleBox> titleBoxes = new ArrayList();
    private ArrayList<DropDownMenuItem> dropDownItems = new ArrayList();
    private SimulationInfo info;
    private boolean shouldSpin = true;
    private MinecartModular placeholder;
    private float yaw = 0.0F;
    private float roll = 0.0F;
    private boolean rolldown = false;
    private ArrayList<SlotAssembler> slots = new ArrayList();
    private ArrayList<SlotAssembler> engineSlots = new ArrayList();
    private ArrayList<SlotAssembler> addonSlots = new ArrayList();
    private ArrayList<SlotAssembler> chestSlots = new ArrayList();
    private ArrayList<SlotAssembler> funcSlots = new ArrayList();
    private SlotHull hullSlot;
    private SlotAssembler toolSlot;
    private SlotOutput outputSlot;
    private SlotAssemblerFuel fuelSlot;
    private final int[] topbotSlots;
    private final int[] sideSlots;
    private ItemStack lastHull;
    private float fuelLevel;
    private ArrayList<TileEntityUpgrade> upgrades = new ArrayList();
    public boolean isDead;
    private boolean loaded;
    ItemStack[] inventoryStacks;

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiCartAssembler(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerCartAssembler(inv, this);
    }

    public TileEntityCartAssembler()
    {
        byte slotID = 0;
        SlotHull var10001 = new SlotHull(this, slotID, 18, 25);
        int var10 = slotID + 1;
        this.hullSlot = var10001;
        this.slots.add(this.hullSlot);
        TitleBox engineBox = new TitleBox(0, 65, 16225309);
        TitleBox toolBox = new TitleBox(1, 100, 6696337);
        TitleBox attachBox = new TitleBox(2, 135, 23423);
        TitleBox storageBox = new TitleBox(3, 170, 10357518);
        TitleBox addonBox = new TitleBox(4, 205, 22566);
        TitleBox infoBox = new TitleBox(5, 375, 30, 13417984);
        this.titleBoxes.add(engineBox);
        this.titleBoxes.add(toolBox);
        this.titleBoxes.add(attachBox);
        this.titleBoxes.add(storageBox);
        this.titleBoxes.add(addonBox);
        this.titleBoxes.add(infoBox);
        int i;
        SlotAssembler slot;

        for (i = 0; i < 5; ++i)
        {
            slot = new SlotAssembler(this, var10++, engineBox.getX() + 2 + 18 * i, engineBox.getY(), 1, false, i);
            slot.invalidate();
            this.slots.add(slot);
            this.engineSlots.add(slot);
        }

        this.toolSlot = new SlotAssembler(this, var10++, toolBox.getX() + 2, toolBox.getY(), 2, false, 0);
        this.slots.add(this.toolSlot);
        this.toolSlot.invalidate();

        for (i = 0; i < 6; ++i)
        {
            slot = new SlotAssembler(this, var10++, attachBox.getX() + 2 + 18 * i, attachBox.getY(), -1, false, i);
            slot.invalidate();
            this.slots.add(slot);
            this.funcSlots.add(slot);
        }

        for (i = 0; i < 4; ++i)
        {
            slot = new SlotAssembler(this, var10++, storageBox.getX() + 2 + 18 * i, storageBox.getY(), 3, false, i);
            slot.invalidate();
            this.slots.add(slot);
            this.chestSlots.add(slot);
        }

        for (i = 0; i < 12; ++i)
        {
            slot = new SlotAssembler(this, var10++, addonBox.getX() + 2 + 18 * (i % 6), addonBox.getY() + 18 * (i / 6), 4, false, i);
            slot.invalidate();
            this.slots.add(slot);
            this.addonSlots.add(slot);
        }

        this.fuelSlot = new SlotAssemblerFuel(this, var10++, 395, 220);
        this.slots.add(this.fuelSlot);
        this.outputSlot = new SlotOutput(this, var10++, 450, 220);
        this.slots.add(this.outputSlot);
        this.info = new SimulationInfo();
        this.inventoryStacks = new ItemStack[this.slots.size()];
        this.topbotSlots = new int[] {this.getSizeInventory() - this.nonModularSlots()};
        this.sideSlots = new int[] {this.getSizeInventory() - this.nonModularSlots() + 1};
    }

    public void clearUpgrades()
    {
        this.upgrades.clear();
    }

    public void addUpgrade(TileEntityUpgrade upgrade)
    {
        this.upgrades.add(upgrade);
    }

    public void removeUpgrade(TileEntityUpgrade upgrade)
    {
        this.upgrades.remove(upgrade);
    }

    public ArrayList<TileEntityUpgrade> getUpgradeTiles()
    {
        return this.upgrades;
    }

    public ArrayList<AssemblerUpgrade> getUpgrades()
    {
        ArrayList lst = new ArrayList();
        Iterator i$ = this.upgrades.iterator();

        while (i$.hasNext())
        {
            TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();
            lst.add(tile.getUpgrade());
        }

        return lst;
    }

    public ArrayList<BaseEffect> getEffects()
    {
        ArrayList lst = new ArrayList();
        Iterator i$ = this.upgrades.iterator();

        while (i$.hasNext())
        {
            TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();
            AssemblerUpgrade upgrade = tile.getUpgrade();

            if (upgrade != null)
            {
                Iterator i$1 = upgrade.getEffects().iterator();

                while (i$1.hasNext())
                {
                    BaseEffect effect = (BaseEffect)i$1.next();
                    lst.add(effect);
                }
            }
        }

        return lst;
    }

    public SimulationInfo getSimulationInfo()
    {
        return this.info;
    }

    public ArrayList<DropDownMenuItem> getDropDown()
    {
        return this.dropDownItems;
    }

    public ArrayList<TitleBox> getTitleBoxes()
    {
        return this.titleBoxes;
    }

    public static int getRemovedSize()
    {
        return -1;
    }

    public static int getKeepSize()
    {
        return 0;
    }

    public ArrayList<SlotAssembler> getSlots()
    {
        return this.slots;
    }

    public ArrayList<SlotAssembler> getEngines()
    {
        return this.engineSlots;
    }

    public ArrayList<SlotAssembler> getChests()
    {
        return this.chestSlots;
    }

    public ArrayList<SlotAssembler> getAddons()
    {
        return this.addonSlots;
    }

    public ArrayList<SlotAssembler> getFuncs()
    {
        return this.funcSlots;
    }

    public SlotAssembler getToolSlot()
    {
        return this.toolSlot;
    }

    public int getMaxAssemblingTime()
    {
        return this.maxAssemblingTime;
    }

    public int getAssemblingTime()
    {
        return (int)this.currentAssemblingTime;
    }

    private void setAssemblingTime(int val)
    {
        this.currentAssemblingTime = (float)val;
    }

    public boolean getIsAssembling()
    {
        return this.isAssembling;
    }

    public void doAssemble()
    {
        if (!this.hasErrors())
        {
            this.maxAssemblingTime = this.generateAssemblingTime();
            this.createCartFromModules();
            this.isAssembling = true;
            Iterator i$ = this.getUpgradeTiles().iterator();

            while (i$.hasNext())
            {
                TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();

                if (tile.getUpgrade() != null)
                {
                    Iterator i$1 = tile.getUpgrade().getEffects().iterator();

                    while (i$1.hasNext())
                    {
                        BaseEffect effect = (BaseEffect)i$1.next();

                        if (effect instanceof Disassemble)
                        {
                            ItemStack oldcart = tile.getStackInSlot(0);

                            if (oldcart != null && oldcart.getItem() instanceof ItemCarts && oldcart.hasDisplayName())
                            {
                                this.outputItem.setStackDisplayName(oldcart.getDisplayName());
                            }

                            tile.setInventorySlotContents(0, (ItemStack)null);
                        }
                    }
                }
            }
        }
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.doAssemble();
        }
        else if (id == 1)
        {
            byte slotId = data[0];

            if (slotId >= 1 && slotId < this.getSlots().size())
            {
                SlotAssembler slot = (SlotAssembler)this.getSlots().get(slotId);

                if (slot.getStack() != null)
                {
                    if (slot.getStack().stackSize == getKeepSize())
                    {
                        slot.getStack().stackSize = getRemovedSize();
                    }
                    else
                    {
                        slot.getStack().stackSize = getKeepSize();
                    }
                }
            }
        }
    }

    public void onUpgradeUpdate() {}

    public int generateAssemblingTime()
    {
        return this.generateAssemblingTime(this.getModules(true, new int[] {getKeepSize(), getRemovedSize()}), this.getModules(true, new int[] {getKeepSize(), 1}));
    }

    private int generateAssemblingTime(ArrayList<ModuleData> modules, ArrayList<ModuleData> removed)
    {
        int timeRequired = 100;
        Iterator i$;
        ModuleData effect;

        for (i$ = modules.iterator(); i$.hasNext(); timeRequired += this.getAssemblingTime(effect, false))
        {
            effect = (ModuleData)i$.next();
        }

        for (i$ = removed.iterator(); i$.hasNext(); timeRequired += this.getAssemblingTime(effect, true))
        {
            effect = (ModuleData)i$.next();
        }

        i$ = this.getEffects().iterator();

        while (i$.hasNext())
        {
            BaseEffect effect1 = (BaseEffect)i$.next();

            if (effect1 instanceof TimeFlatCart)
            {
                timeRequired += ((TimeFlatCart)effect1).getTicks();
            }
        }

        return (int)Math.round(Math.max(0, timeRequired) * StevesCarts.instance.assemblerTimeScale);
    }

    private int getAssemblingTime(ModuleData module, boolean isRemoved)
    {
        int time = (int)(5.0D * Math.pow((double)module.getCost(), 2.2D));
        time += this.getTimeDecreased(isRemoved);
        return Math.max(0, time);
    }

    public ItemStack getCartFromModules(boolean isSimulated)
    {
        ArrayList items = new ArrayList();

        for (int i = 0; i < this.getSizeInventory() - this.nonModularSlots(); ++i)
        {
            ItemStack item = this.getStackInSlot(i);

            if (item != null)
            {
                if (item.stackSize != getRemovedSize())
                {
                    items.add(item);
                }
                else if (!isSimulated)
                {
                    ItemStack spare = item.copy();
                    spare.stackSize = 1;
                    this.spareModules.add(spare);
                }
            }
        }

        return ModuleData.createModularCartFromItems(items);
    }

    private void createCartFromModules()
    {
        this.spareModules.clear();
        this.outputItem = this.getCartFromModules(false);

        if (this.outputItem != null)
        {
            for (int i = 0; i < this.getSizeInventory() - this.nonModularSlots(); ++i)
            {
                this.setInventorySlotContents(i, (ItemStack)null);
            }
        }
        else
        {
            this.spareModules.clear();
        }
    }

    public ArrayList<ModuleData> getNonHullModules()
    {
        return this.getModules(false);
    }

    public ArrayList<ModuleData> getModules(boolean includeHull)
    {
        return this.getModules(includeHull, new int[] {getRemovedSize()});
    }

    public ArrayList<ModuleData> getModules(boolean includeHull, int[] invalid)
    {
        ArrayList modules = new ArrayList();

        for (int i = includeHull ? 0 : 1; i < this.getSizeInventory() - this.nonModularSlots(); ++i)
        {
            ItemStack item = this.getStackInSlot(i);

            if (item != null)
            {
                boolean validSize = true;

                for (int module = 0; module < invalid.length; ++module)
                {
                    if (invalid[module] == item.stackSize || invalid[module] > 0 && item.stackSize > 0)
                    {
                        validSize = false;
                        break;
                    }
                }

                if (validSize)
                {
                    ModuleData var8 = ModItems.modules.getModuleData(item, true);

                    if (var8 != null)
                    {
                        modules.add(var8);
                    }
                }
            }
        }

        return modules;
    }

    public ModuleDataHull getHullModule()
    {
        if (this.getStackInSlot(0) != null)
        {
            ModuleData hulldata = ModItems.modules.getModuleData(this.getStackInSlot(0));

            if (hulldata instanceof ModuleDataHull)
            {
                return (ModuleDataHull)hulldata;
            }
        }

        return null;
    }

    private boolean hasErrors()
    {
        return this.getErrors().size() > 0;
    }

    public ArrayList<String> getErrors()
    {
        ArrayList errors = new ArrayList();

        if (this.hullSlot.getStack() == null)
        {
            errors.add(Localization.GUI.ASSEMBLER.HULL_ERROR.translate(new String[0]));
        }
        else
        {
            ModuleData hulldata = ModItems.modules.getModuleData(this.getStackInSlot(0));

            if (hulldata != null && hulldata instanceof ModuleDataHull)
            {
                if (this.isAssembling)
                {
                    errors.add(Localization.GUI.ASSEMBLER.BUSY.translate(new String[0]));
                }
                else if (this.outputSlot != null && this.outputSlot.getStack() != null)
                {
                    errors.add(Localization.GUI.ASSEMBLER.DEPARTURE_BAY.translate(new String[0]));
                }

                ArrayList modules = new ArrayList();

                for (int error = 0; error < this.getSizeInventory() - this.nonModularSlots(); ++error)
                {
                    if (this.getStackInSlot(error) != null)
                    {
                        ModuleData data = ModItems.modules.getModuleData(this.getStackInSlot(error));

                        if (data != null)
                        {
                            modules.add(data);
                        }
                    }
                }

                String var6 = ModuleData.checkForErrors((ModuleDataHull)hulldata, modules);

                if (var6 != null)
                {
                    errors.add(var6);
                }
            }
            else
            {
                errors.add(Localization.GUI.ASSEMBLER.INVALID_HULL_SHORT.translate(new String[0]));
            }
        }

        return errors;
    }

    public int getTotalCost()
    {
        ArrayList modules = new ArrayList();

        for (int i = 0; i < this.getSizeInventory() - this.nonModularSlots(); ++i)
        {
            if (this.getStackInSlot(i) != null)
            {
                ModuleData data = ModItems.modules.getModuleData(this.getStackInSlot(i));

                if (data != null)
                {
                    modules.add(data);
                }
            }
        }

        return ModuleData.getTotalCost(modules);
    }

    public void initGuiData(Container con, ICrafting crafting)
    {
        this.updateGuiData(con, crafting, 0, this.getShortFromInt(true, this.maxAssemblingTime));
        this.updateGuiData(con, crafting, 1, this.getShortFromInt(false, this.maxAssemblingTime));
        this.updateGuiData(con, crafting, 2, this.getShortFromInt(true, this.getAssemblingTime()));
        this.updateGuiData(con, crafting, 3, this.getShortFromInt(false, this.getAssemblingTime()));
        this.updateGuiData(con, crafting, 4, (short)(this.isAssembling ? 1 : 0));
        this.updateGuiData(con, crafting, 5, this.getShortFromInt(true, this.getFuelLevel()));
        this.updateGuiData(con, crafting, 6, this.getShortFromInt(false, this.getFuelLevel()));
    }

    public void checkGuiData(Container container, ICrafting crafting)
    {
        ContainerCartAssembler con = (ContainerCartAssembler)container;

        if (con.lastMaxAssemblingTime != this.maxAssemblingTime)
        {
            this.updateGuiData(con, crafting, 0, this.getShortFromInt(true, this.maxAssemblingTime));
            this.updateGuiData(con, crafting, 1, this.getShortFromInt(false, this.maxAssemblingTime));
            con.lastMaxAssemblingTime = this.maxAssemblingTime;
        }

        if (con.lastIsAssembling != this.isAssembling)
        {
            this.updateGuiData(con, crafting, 4, (short)(this.isAssembling ? 1 : 0));
            con.lastIsAssembling = this.isAssembling;
        }

        if (con.lastFuelLevel != this.getFuelLevel())
        {
            this.updateGuiData(con, crafting, 5, this.getShortFromInt(true, this.getFuelLevel()));
            this.updateGuiData(con, crafting, 6, this.getShortFromInt(false, this.getFuelLevel()));
            con.lastFuelLevel = this.getFuelLevel();
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.maxAssemblingTime = this.getIntFromShort(true, this.maxAssemblingTime, data);
        }
        else if (id == 1)
        {
            this.maxAssemblingTime = this.getIntFromShort(false, this.maxAssemblingTime, data);
        }
        else if (id == 2)
        {
            this.setAssemblingTime(this.getIntFromShort(true, this.getAssemblingTime(), data));
        }
        else if (id == 3)
        {
            this.setAssemblingTime(this.getIntFromShort(false, this.getAssemblingTime(), data));
        }
        else if (id == 4)
        {
            this.isAssembling = data != 0;

            if (!this.isAssembling)
            {
                this.setAssemblingTime(0);
            }
        }
        else if (id == 5)
        {
            this.setFuelLevel(this.getIntFromShort(true, this.getFuelLevel(), data));
        }
        else if (id == 6)
        {
            this.setFuelLevel(this.getIntFromShort(false, this.getFuelLevel(), data));
        }
    }

    private void invalidateAll()
    {
        int i;

        for (i = 0; i < this.getEngines().size(); ++i)
        {
            ((SlotAssembler)this.getEngines().get(i)).invalidate();
        }

        for (i = 0; i < this.getAddons().size(); ++i)
        {
            ((SlotAssembler)this.getAddons().get(i)).invalidate();
        }

        for (i = 0; i < this.getChests().size(); ++i)
        {
            ((SlotAssembler)this.getChests().get(i)).invalidate();
        }

        for (i = 0; i < this.getFuncs().size(); ++i)
        {
            ((SlotAssembler)this.getFuncs().get(i)).invalidate();
        }

        this.getToolSlot().invalidate();
    }

    private void validateAll()
    {
        if (this.hullSlot != null)
        {
            ArrayList slots = this.getValidSlotFromHullItem(this.hullSlot.getStack());

            if (slots != null)
            {
                Iterator i$ = slots.iterator();

                while (i$.hasNext())
                {
                    SlotAssembler slot = (SlotAssembler)i$.next();
                    slot.validate();
                }
            }
        }
    }

    public ArrayList<SlotAssembler> getValidSlotFromHullItem(ItemStack hullitem)
    {
        if (hullitem != null)
        {
            ModuleData data = ModItems.modules.getModuleData(hullitem);

            if (data != null && data instanceof ModuleDataHull)
            {
                ModuleDataHull hull = (ModuleDataHull)data;
                return this.getValidSlotFromHull(hull);
            }
        }

        return null;
    }

    private ArrayList<SlotAssembler> getValidSlotFromHull(ModuleDataHull hull)
    {
        ArrayList slots = new ArrayList();
        int i;

        for (i = 0; i < hull.getEngineMax(); ++i)
        {
            slots.add(this.getEngines().get(i));
        }

        for (i = 0; i < hull.getAddonMax(); ++i)
        {
            slots.add(this.getAddons().get(i));
        }

        for (i = 0; i < this.getChests().size(); ++i)
        {
            slots.add(this.getChests().get(i));
        }

        for (i = 0; i < this.getFuncs().size(); ++i)
        {
            slots.add(this.getFuncs().get(i));
        }

        slots.add(this.getToolSlot());
        return slots;
    }

    public int getMaxFuelLevel()
    {
        int capacity = 4000;
        Iterator i$ = this.getEffects().iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();

            if (effect instanceof FuelCapacity)
            {
                capacity += ((FuelCapacity)effect).getFuelCapacity();
            }
        }

        if (capacity > 200000)
        {
            capacity = 200000;
        }
        else if (capacity < 1)
        {
            capacity = 1;
        }

        return capacity;
    }

    public boolean isCombustionFuelValid()
    {
        Iterator i$ = this.getEffects().iterator();
        BaseEffect effect;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            effect = (BaseEffect)i$.next();
        }
        while (!(effect instanceof CombustionFuel));

        return true;
    }

    public int getFuelLevel()
    {
        return (int)this.fuelLevel;
    }

    public void setFuelLevel(int val)
    {
        this.fuelLevel = (float)val;
    }

    private int getTimeDecreased(boolean isRemoved)
    {
        int timeDecr = 0;
        Iterator i$ = this.getEffects().iterator();
        BaseEffect effect;

        while (i$.hasNext())
        {
            effect = (BaseEffect)i$.next();

            if (effect instanceof TimeFlat && !(effect instanceof TimeFlatRemoved))
            {
                timeDecr += ((TimeFlat)effect).getTicks();
            }
        }

        if (isRemoved)
        {
            i$ = this.getEffects().iterator();

            while (i$.hasNext())
            {
                effect = (BaseEffect)i$.next();

                if (effect instanceof TimeFlatRemoved)
                {
                    timeDecr += ((TimeFlat)effect).getTicks();
                }
            }
        }

        return timeDecr;
    }

    private float getFuelCost()
    {
        float cost = 1.0F;
        Iterator i$ = this.getEffects().iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();

            if (effect instanceof FuelCost)
            {
                cost += ((FuelCost)effect).getCost();
            }
        }

        if (cost < 0.05F)
        {
            ;
        }

        return cost;
    }

    public float getEfficiency()
    {
        float efficiency = 1.0F;
        Iterator i$ = this.getEffects().iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();

            if (effect instanceof WorkEfficiency)
            {
                efficiency += ((WorkEfficiency)effect).getEfficiency();
            }
        }

        return efficiency;
    }

    private void deployCart()
    {
        Iterator i$ = this.getUpgradeTiles().iterator();

        while (i$.hasNext())
        {
            TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();
            Iterator i$1 = tile.getUpgrade().getEffects().iterator();

            while (i$1.hasNext())
            {
                BaseEffect effect = (BaseEffect)i$1.next();

                if (effect instanceof Deployer)
                {
                    int x = 2 * tile.xCoord - this.xCoord;
                    int y = 2 * tile.yCoord - this.yCoord;
                    int z = 2 * tile.zCoord - this.zCoord;

                    if (tile.yCoord > this.yCoord)
                    {
                        ++y;
                    }

                    if (BlockRailBase.func_150049_b_(this.worldObj, x, y, z))
                    {
                        try
                        {
                            NBTTagCompound e = this.outputItem.getTagCompound();

                            if (e != null)
                            {
                                MinecartModular cart = new MinecartModular(this.worldObj, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), e, this.outputItem.getDisplayName());
                                this.worldObj.spawnEntityInWorld(cart);
                                cart.temppushX = (double)(tile.xCoord - this.xCoord);
                                cart.temppushZ = (double)(tile.zCoord - this.zCoord);
                                this.managerInteract(cart, true);
                                return;
                            }
                        }
                        catch (Exception var10)
                        {
                            var10.printStackTrace();
                        }
                    }
                }
            }
        }

        this.outputSlot.putStack(this.outputItem);
    }

    public void managerInteract(MinecartModular cart, boolean toCart)
    {
        Iterator i$ = this.getUpgradeTiles().iterator();

        while (i$.hasNext())
        {
            TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();
            Iterator i$1 = tile.getUpgrade().getEffects().iterator();

            while (i$1.hasNext())
            {
                BaseEffect effect = (BaseEffect)i$1.next();

                if (effect instanceof Manager)
                {
                    int x2 = 2 * tile.xCoord - this.xCoord;
                    int y2 = 2 * tile.yCoord - this.yCoord;
                    int z2 = 2 * tile.zCoord - this.zCoord;

                    if (tile.yCoord > this.yCoord)
                    {
                        ++y2;
                    }

                    TileEntity managerentity = this.worldObj.getTileEntity(x2, y2, z2);

                    if (managerentity != null && managerentity instanceof TileEntityManager)
                    {
                        ManagerTransfer transfer = new ManagerTransfer();
                        transfer.setCart(cart);

                        if (tile.yCoord != this.yCoord)
                        {
                            transfer.setSide(-1);
                        }
                        else if (tile.xCoord < this.xCoord)
                        {
                            transfer.setSide(0);
                        }
                        else if (tile.xCoord > this.xCoord)
                        {
                            transfer.setSide(3);
                        }
                        else if (tile.zCoord < this.zCoord)
                        {
                            transfer.setSide(1);
                        }
                        else if (tile.zCoord > this.zCoord)
                        {
                            transfer.setSide(2);
                        }

                        if (toCart)
                        {
                            transfer.setFromCartEnabled(false);
                        }
                        else
                        {
                            transfer.setToCartEnabled(false);
                        }

                        TileEntityManager manager = (TileEntityManager)managerentity;

                        while (true)
                        {
                            if (manager.exchangeItems(transfer))
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    private void deploySpares()
    {
        Iterator i$ = this.getUpgradeTiles().iterator();

        while (i$.hasNext())
        {
            TileEntityUpgrade tile = (TileEntityUpgrade)i$.next();

            if (tile.getUpgrade() != null)
            {
                Iterator i$1 = tile.getUpgrade().getEffects().iterator();

                while (i$1.hasNext())
                {
                    BaseEffect effect = (BaseEffect)i$1.next();

                    if (effect instanceof Disassemble)
                    {
                        Iterator i$2 = this.spareModules.iterator();

                        while (i$2.hasNext())
                        {
                            ItemStack item = (ItemStack)i$2.next();
                            TransferHandler.TransferItem(item, tile, new ContainerUpgrade((IInventory)null, tile), 1);

                            if (item.stackSize > 0)
                            {
                                this.puke(item);
                            }
                        }

                        return;
                    }
                }
            }
        }
    }

    public void puke(ItemStack item)
    {
        EntityItem entityitem = new EntityItem(this.worldObj, (double)this.xCoord, (double)this.yCoord + 0.25D, (double)this.zCoord, item);
        entityitem.motionX = (double)((0.5F - this.worldObj.rand.nextFloat()) / 10.0F);
        entityitem.motionY = 0.15000000596046448D;
        entityitem.motionZ = (double)((0.5F - this.worldObj.rand.nextFloat()) / 10.0F);
        this.worldObj.spawnEntityInWorld(entityitem);
    }

    public void updateEntity()
    {
        if (!this.loaded)
        {
            ((BlockCartAssembler)ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            this.loaded = true;
        }

        if (!this.isAssembling && this.outputSlot != null && this.outputSlot.getStack() != null)
        {
            ItemStack fuel = this.outputSlot.getStack();

            if (fuel.getItem() == ModItems.carts)
            {
                NBTTagCompound info = fuel.getTagCompound();

                if (info != null && info.hasKey("maxTime"))
                {
                    ItemStack newItem = new ItemStack(ModItems.carts);
                    NBTTagCompound save = new NBTTagCompound();
                    save.setByteArray("Modules", info.getByteArray("Modules"));
                    newItem.setTagCompound(save);
                    int modulecount = info.getByteArray("Modules").length;
                    this.maxAssemblingTime = info.getInteger("maxTime");
                    this.setAssemblingTime(info.getInteger("currentTime"));
                    this.spareModules.clear();

                    if (info.hasKey("Spares"))
                    {
                        byte[] moduleIDs = info.getByteArray("Spares");

                        for (int i = 0; i < moduleIDs.length; ++i)
                        {
                            byte id = moduleIDs[i];
                            ItemStack module = new ItemStack(ModItems.modules, 1, id);
                            ModItems.modules.addExtraDataToModule(module, info, i + modulecount);
                            this.spareModules.add(module);
                        }
                    }

                    if (fuel.hasDisplayName())
                    {
                        newItem.setStackDisplayName(fuel.getDisplayName());
                    }

                    this.isAssembling = true;
                    this.outputItem = newItem;
                    this.outputSlot.putStack((ItemStack)null);
                }
            }
        }

        if (this.getFuelLevel() > this.getMaxFuelLevel())
        {
            this.setFuelLevel(this.getMaxFuelLevel());
        }

        if (this.isAssembling && this.outputSlot != null && (float)this.getFuelLevel() >= this.getFuelCost())
        {
            this.currentAssemblingTime += this.getEfficiency();
            this.fuelLevel -= this.getFuelCost();

            if (this.getFuelLevel() <= 0)
            {
                this.setFuelLevel(0);
            }

            if (this.getAssemblingTime() >= this.maxAssemblingTime)
            {
                this.isAssembling = false;
                this.setAssemblingTime(0);

                if (!this.worldObj.isRemote)
                {
                    this.deployCart();
                    this.outputItem = null;
                    this.deploySpares();
                    this.spareModules.clear();
                }
            }
        }

        if (!this.worldObj.isRemote && this.fuelSlot != null && this.fuelSlot.getStack() != null)
        {
            int var10 = this.fuelSlot.getFuelLevel(this.fuelSlot.getStack());

            if (var10 > 0 && this.getFuelLevel() + var10 <= this.getMaxFuelLevel())
            {
                this.setFuelLevel(this.getFuelLevel() + var10);

                if (this.fuelSlot.getStack().getItem().hasContainerItem(this.fuelSlot.getStack()))
                {
                    this.fuelSlot.putStack(new ItemStack(this.fuelSlot.getStack().getItem().getContainerItem()));
                }
                else
                {
                    --this.fuelSlot.getStack().stackSize;
                }

                if (this.fuelSlot.getStack().stackSize <= 0)
                {
                    this.fuelSlot.putStack((ItemStack)null);
                }
            }
        }

        this.updateSlots();
        this.handlePlaceholder();
    }

    public void updateSlots()
    {
        if (this.hullSlot != null)
        {
            if (this.lastHull != null && this.hullSlot.getStack() == null)
            {
                this.invalidateAll();
            }
            else if (this.lastHull == null && this.hullSlot.getStack() != null)
            {
                this.validateAll();
            }
            else if (this.lastHull != this.hullSlot.getStack())
            {
                this.invalidateAll();
                this.validateAll();
            }

            this.lastHull = this.hullSlot.getStack();
        }

        Iterator i$ = this.slots.iterator();

        while (i$.hasNext())
        {
            SlotAssembler slot = (SlotAssembler)i$.next();
            slot.update();
        }
    }

    public void resetPlaceholder()
    {
        this.placeholder = null;
    }

    public MinecartModular getPlaceholder()
    {
        return this.placeholder;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getRoll()
    {
        return this.roll;
    }

    public void setYaw(float val)
    {
        this.yaw = val;
    }

    public void setRoll(float val)
    {
        this.roll = val;
    }

    public void setSpinning(boolean val)
    {
        this.shouldSpin = val;
    }

    public int nonModularSlots()
    {
        return 2;
    }

    private void handlePlaceholder()
    {
        if (this.worldObj.isRemote)
        {
            if (this.placeholder == null)
            {
                return;
            }

            if (!StevesCarts.freezeCartSimulation)
            {
                byte minRoll = -5;
                byte maxRoll = 25;

                if (this.shouldSpin)
                {
                    this.yaw += 2.0F;
                    this.roll %= 360.0F;

                    if (!this.rolldown)
                    {
                        if (this.roll < (float)(minRoll - 3))
                        {
                            this.roll += 5.0F;
                        }
                        else
                        {
                            this.roll += 0.2F;
                        }

                        if (this.roll > (float)maxRoll)
                        {
                            this.rolldown = true;
                        }
                    }
                    else
                    {
                        if (this.roll > (float)(maxRoll + 3))
                        {
                            this.roll -= 5.0F;
                        }
                        else
                        {
                            this.roll -= 0.2F;
                        }

                        if (this.roll < (float)minRoll)
                        {
                            this.rolldown = false;
                        }
                    }
                }
            }

            this.placeholder.onCartUpdate();

            if (this.placeholder == null)
            {
                return;
            }

            this.placeholder.updateFuel();
        }
    }

    public void createPlaceholder()
    {
        if (this.placeholder == null)
        {
            this.placeholder = new MinecartModular(this.worldObj, this, this.getModularInfoBytes());
            this.updateRenderMenu();
            this.isErrorListOutdated = true;
        }
    }

    public void updatePlaceholder()
    {
        if (this.placeholder != null)
        {
            this.placeholder.updateSimulationModules(this.getModularInfoBytes());
            this.updateRenderMenu();
            this.isErrorListOutdated = true;
        }
    }

    private void updateRenderMenu()
    {
        ArrayList list = this.info.getList();
        this.dropDownItems.clear();
        Iterator i$ = list.iterator();

        while (i$.hasNext())
        {
            DropDownMenuItem item = (DropDownMenuItem)i$.next();

            if (item.getModuleClass() == null)
            {
                this.dropDownItems.add(item);
            }
            else
            {
                for (int i = 0; i < this.getSizeInventory() - this.nonModularSlots(); ++i)
                {
                    if (this.getStackInSlot(i) != null && ModuleData.isItemOfModularType(this.getStackInSlot(i), item.getModuleClass()) && (item.getExcludedClass() == null || !ModuleData.isItemOfModularType(this.getStackInSlot(i), item.getExcludedClass())))
                    {
                        this.dropDownItems.add(item);
                        break;
                    }
                }
            }
        }
    }

    private byte[] getModularInfoBytes()
    {
        ArrayList datalist = new ArrayList();

        for (int bytes = 0; bytes < this.getSizeInventory() - this.nonModularSlots(); ++bytes)
        {
            if (this.getStackInSlot(bytes) != null)
            {
                ModuleData i = ModItems.modules.getModuleData(this.getStackInSlot(bytes));

                if (i != null)
                {
                    datalist.add(Byte.valueOf((byte)this.getStackInSlot(bytes).getItemDamage()));
                }
            }
        }

        byte[] var4 = new byte[datalist.size()];

        for (int var5 = 0; var5 < datalist.size(); ++var5)
        {
            var4[var5] = ((Byte)datalist.get(var5)).byteValue();
        }

        return var4;
    }

    public boolean getIsDisassembling()
    {
        for (int i = 0; i < this.getSizeInventory() - this.nonModularSlots(); ++i)
        {
            if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize <= 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.inventoryStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return this.inventoryStacks[i];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.inventoryStacks[i] != null)
        {
            ItemStack itemstack1;

            if (this.inventoryStacks[i].stackSize <= j)
            {
                itemstack1 = this.inventoryStacks[i];
                this.inventoryStacks[i] = null;
                //this.onInventoryChanged();
                return itemstack1;
            }
            else
            {
                itemstack1 = this.inventoryStacks[i].splitStack(j);

                if (this.inventoryStacks[i].stackSize == 0)
                {
                    this.inventoryStacks[i] = null;
                }

                //this.onInventoryChanged();
                return itemstack1;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.inventoryStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        //this.onInventoryChanged();
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.cartassembler";
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return false;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void closeInventory() {}

    public void openInventory() {}

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int i)
    {
        ItemStack item = this.getStackInSlot(i);

        if (item != null)
        {
            this.setInventorySlotContents(i, (ItemStack)null);
            return item.stackSize == 0 ? null : item;
        }
        else
        {
            return null;
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        NBTTagList items = tagCompound.getTagList("Items", NBTHelper.COMPOUND.getId());
        NBTTagCompound outputTag;
        ItemStack iStack;

        for (int spares = 0; spares < items.tagCount(); ++spares)
        {
            outputTag = items.getCompoundTagAt(spares);
            int item = outputTag.getByte("Slot") & 255;
            iStack = ItemStack.loadItemStackFromNBT(outputTag);

            if (item >= 0 && item < this.getSizeInventory())
            {
                this.setInventorySlotContents(item, iStack);
            }
        }

        NBTTagList var7 = tagCompound.getTagList("Spares", NBTHelper.COMPOUND.getId());
        this.spareModules.clear();

        for (int var8 = 0; var8 < var7.tagCount(); ++var8)
        {
            NBTTagCompound var9 = var7.getCompoundTagAt(var8);
            iStack = ItemStack.loadItemStackFromNBT(var9);
            this.spareModules.add(iStack);
        }

        outputTag = (NBTTagCompound)tagCompound.getTag("Output");

        if (outputTag != null)
        {
            this.outputItem = ItemStack.loadItemStackFromNBT(outputTag);
        }

        if (tagCompound.hasKey("Fuel"))
        {
            this.setFuelLevel(tagCompound.getShort("Fuel"));
        }
        else
        {
            this.setFuelLevel(tagCompound.getInteger("IntFuel"));
        }

        this.maxAssemblingTime = tagCompound.getInteger("maxTime");
        this.setAssemblingTime(tagCompound.getInteger("currentTime"));
        this.isAssembling = tagCompound.getBoolean("isAssembling");
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        NBTTagList items = new NBTTagList();

        for (int spares = 0; spares < this.getSizeInventory(); ++spares)
        {
            ItemStack outputTag = this.getStackInSlot(spares);

            if (outputTag != null)
            {
                NBTTagCompound iStack = new NBTTagCompound();
                iStack.setByte("Slot", (byte)spares);
                outputTag.writeToNBT(iStack);
                items.appendTag(iStack);
            }
        }

        tagCompound.setTag("Items", items);
        NBTTagList var7 = new NBTTagList();

        for (int var8 = 0; var8 < this.spareModules.size(); ++var8)
        {
            ItemStack var10 = (ItemStack)this.spareModules.get(var8);

            if (var10 != null)
            {
                NBTTagCompound item = new NBTTagCompound();
                var10.writeToNBT(item);
                var7.appendTag(item);
            }
        }

        tagCompound.setTag("Spares", var7);

        if (this.outputItem != null)
        {
            NBTTagCompound var9 = new NBTTagCompound();
            this.outputItem.writeToNBT(var9);
            tagCompound.setTag("Output", var9);
        }

        tagCompound.setInteger("IntFuel", this.getFuelLevel());
        tagCompound.setInteger("maxTime", this.maxAssemblingTime);
        tagCompound.setInteger("currentTime", this.getAssemblingTime());
        tagCompound.setBoolean("isAssembling", this.isAssembling);
    }

    public ItemStack getOutputOnInterupt()
    {
        if (this.outputItem == null)
        {
            return null;
        }
        else if (!this.outputItem.hasTagCompound())
        {
            return null;
        }
        else
        {
            NBTTagCompound info = this.outputItem.getTagCompound();

            if (info == null)
            {
                return null;
            }
            else
            {
                info.setInteger("currentTime", this.getAssemblingTime());
                info.setInteger("maxTime", this.maxAssemblingTime);
                int modulecount = info.getByteArray("Modules").length;
                new NBTTagCompound();
                byte[] moduleIDs = new byte[this.spareModules.size()];

                for (int i = 0; i < this.spareModules.size(); ++i)
                {
                    ItemStack item = (ItemStack)this.spareModules.get(i);
                    ModuleData data = ModItems.modules.getModuleData(item);

                    if (data != null)
                    {
                        moduleIDs[i] = data.getID();
                        ModItems.modules.addExtraDataToCart(info, item, i + modulecount);
                    }
                }

                info.setByteArray("Spares", moduleIDs);
                return this.outputItem;
            }
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {
        return slotId >= 0 && slotId < this.slots.size() ? ((SlotAssembler)this.slots.get(slotId)).isItemValid(item) : false;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side != 0 && side != 1 ? this.sideSlots : this.topbotSlots;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        return side != 0 && side != 1 ? false : this.isItemValidForSlot(slot, item);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }

    public void increaseFuel(int val)
    {
        this.fuelLevel += (float)val;

        if (this.fuelLevel > (float)this.getMaxFuelLevel())
        {
            this.fuelLevel = (float)this.getMaxFuelLevel();
        }
    }

	@Override
	public boolean hasCustomInventoryName() {return false;}
}
