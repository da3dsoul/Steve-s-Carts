package vswe.stevescarts.Carts;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.*;

import mods.railcraft.api.tracks.ITrackLockdown;
import mods.railcraft.api.tracks.RailTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import vswe.stevescarts.Helpers.*;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Containers.ContainerMinecart;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleCreativeSupplies;
import vswe.stevescarts.Modules.Engines.ModuleEngine;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Modules.Workers.ModuleWorker;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class MinecartModular extends EntityMinecart implements IInventory, IEntityAdditionalSpawnData, IFluidHandler, IEnergyReceiver
{
    public int disabledX;
    public int disabledY;
    public int disabledZ;
    protected boolean wasDisabled;
    public double pushX;
    public double pushZ;
    public double temppushX;
    public double temppushZ;
    protected boolean engineFlag;
    private int motorRotation;
    public boolean cornerFlip;
    private int fixedMeta;
    private int fixedMX;
    private int fixedMY;
    private int fixedMZ;
    private byte[] moduleLoadingData;
    private Ticket cartTicket;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;
    private int workingTime;
    private ModuleWorker workingComponent;
    public TileEntityCartAssembler placeholderAsssembler;
    public boolean isPlaceholder;
    public int keepAlive;
    public static final int MODULAR_SPACE_WIDTH = 443;
    public static final int MODULAR_SPACE_HEIGHT = 168;
    public int modularSpaceHeight;
    public boolean canScrollModules;
    private ArrayList<ModuleCountPair> moduleCounts;
    public static final int[][][] railDirectionCoordinates = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    private ArrayList<ModuleBase> modules;
    private ArrayList<ModuleWorker> workModules;
    private ArrayList<ModuleEngine> engineModules;
    private ArrayList<ModuleTank> tankModules;
    private ModuleCreativeSupplies creativeSupplies;
    public Random rand;
    protected String name;
    public byte cartVersion;
    public byte collidedHorizontallyTicks = 0;
    public boolean isCollidedForward;
    private int scrollY;
    @SideOnly(Side.CLIENT)
    private MovingSound sound;
    @SideOnly(Side.CLIENT)
    private MovingSound soundRiding;
    @SideOnly(Side.CLIENT)
    private int keepSilent;

    public boolean returningHome;

    public String currentTool = "";

    private int ticksFromLoad = 0;

    private int[] prevChunk = new int[2];

    private byte motSigns = -1;

    public ArrayList<ModuleBase> getModules()
    {
        return this.modules;
    }

    public ArrayList<ModuleWorker> getWorkers()
    {
        return this.workModules;
    }

    public ArrayList<ModuleEngine> getEngines()
    {
        return this.engineModules;
    }

    public ArrayList<ModuleTank> getTanks()
    {
        return this.tankModules;
    }

    public ArrayList<ModuleCountPair> getModuleCounts()
    {
        return this.moduleCounts;
    }

    public MinecartModular(World world, double x, double y, double z, NBTTagCompound info, String name)
    {
        super(world, x, y, z);
        this.engineFlag = false;
        this.fixedMeta = -1;
        this.rand = new Random();
        this.overrideDatawatcher();
        this.cartVersion = info.getByte("CartVersion");
        this.loadModules(info);
        this.name = name;

        for (int i = 0; i < this.modules.size(); ++i)
        {
            if (((ModuleBase)this.modules.get(i)).hasExtraData() && info.hasKey("Data" + i))
            {
                ((ModuleBase)this.modules.get(i)).setExtraData(info.getByte("Data" + i));
            }
        }
    }

    public MinecartModular(World world)
    {
        super(world);
        this.engineFlag = false;
        this.fixedMeta = -1;
        this.rand = new Random();
        this.overrideDatawatcher();
    }

    public MinecartModular(World world, TileEntityCartAssembler assembler, byte[] data)
    {
        this(world);
        this.setPlaceholder(assembler);
        this.loadPlaceholderModules(data);
    }

    private void overrideDatawatcher()
    {
        if (this.worldObj.isRemote)
        {
            this.dataWatcher = new DataWatcherLockable(this);
            this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
            this.dataWatcher.addObject(1, Short.valueOf((short)300));
            this.entityInit();
        }
    }

    private void loadPlaceholderModules(byte[] data)
    {
        if (this.modules == null)
        {
            this.modules = new ArrayList();
            this.doLoadModules(data);
        }
        else
        {
            ArrayList modulesToAdd = new ArrayList();
            ArrayList oldModules = new ArrayList();
            int newModuleData;

            for (newModuleData = 0; newModuleData < this.moduleLoadingData.length; ++newModuleData)
            {
                oldModules.add(Byte.valueOf(this.moduleLoadingData[newModuleData]));
            }

            int i1;

            for (newModuleData = 0; newModuleData < data.length; ++newModuleData)
            {
                boolean i = false;

                for (i1 = 0; i1 < oldModules.size(); ++i1)
                {
                    if (data[newModuleData] == ((Byte)oldModules.get(i1)).byteValue())
                    {
                        i = true;
                        oldModules.remove(i1);
                        break;
                    }
                }

                if (!i)
                {
                    modulesToAdd.add(Byte.valueOf(data[newModuleData]));
                }
            }

            Iterator var7 = oldModules.iterator();

            while (var7.hasNext())
            {
                byte var9 = ((Byte)var7.next()).byteValue();

                for (i1 = 0; i1 < this.modules.size(); ++i1)
                {
                    if (var9 == ((ModuleBase)this.modules.get(i1)).getModuleId())
                    {
                        this.modules.remove(i1);
                        break;
                    }
                }
            }

            byte[] var8 = new byte[modulesToAdd.size()];

            for (int var10 = 0; var10 < modulesToAdd.size(); ++var10)
            {
                var8[var10] = ((Byte)modulesToAdd.get(var10)).byteValue();
            }

            this.doLoadModules(var8);
        }

        this.initModules();
        this.moduleLoadingData = data;
    }

    private void loadModules(NBTTagCompound info)
    {
        NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");

        if (moduleIDTag != null)
        {
            if (this.worldObj.isRemote)
            {
                this.moduleLoadingData = moduleIDTag.func_150292_c();
            }
            else
            {
                this.moduleLoadingData = CartVersion.updateCart(this, moduleIDTag.func_150292_c());
            }

            this.loadModules(this.moduleLoadingData);
        }
    }

    public void updateSimulationModules(byte[] bytes)
    {
        if (!this.isPlaceholder)
        {
            System.out.println("You\'re stupid! This is not a placeholder cart.");
        }
        else
        {
            this.loadPlaceholderModules(bytes);
        }
    }

    protected void loadModules(byte[] bytes)
    {
        this.modules = new ArrayList();
        this.doLoadModules(bytes);
        this.initModules();
    }

    private void doLoadModules(byte[] bytes)
    {
        byte[] arr$ = bytes;
        int len$ = bytes.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            byte id = arr$[i$];

            try
            {
                Class e = ((ModuleData)ModuleData.getList().get(Byte.valueOf(id))).getModuleClass();
                Constructor moduleConstructor = e.getConstructor(new Class[] {MinecartModular.class});
                Object moduleObject = moduleConstructor.newInstance(new Object[] {this});
                ModuleBase module = (ModuleBase)moduleObject;
                module.setModuleId(id);
                this.modules.add(module);
            }
            catch (Exception var10)
            {
                System.out.println("Failed to load module with ID " + id + "! More info below.");
                var10.printStackTrace();
            }
        }
    }

    private void initModules()
    {
        this.moduleCounts = new ArrayList();
        Iterator x = this.modules.iterator();
        ModuleBase y;

        while (x.hasNext())
        {
            y = (ModuleBase)x.next();
            ModuleData maxH = (ModuleData)ModuleData.getList().get(Byte.valueOf(y.getModuleId()));
            boolean guidata = false;
            Iterator datawatcher = this.moduleCounts.iterator();

            while (true)
            {
                if (datawatcher.hasNext())
                {
                    ModuleCountPair packets = (ModuleCountPair)datawatcher.next();

                    if (!packets.isContainingData(maxH))
                    {
                        continue;
                    }

                    packets.increase();
                    guidata = true;
                }

                if (!guidata)
                {
                    this.moduleCounts.add(new ModuleCountPair(maxH));
                }

                break;
            }
        }

        x = this.modules.iterator();

        while (x.hasNext())
        {
            y = (ModuleBase)x.next();
            y.preInit();
        }

        this.workModules = new ArrayList();
        this.engineModules = new ArrayList();
        this.tankModules = new ArrayList();
        boolean x1 = false;
        boolean y1 = false;
        boolean maxH1 = false;
        int guidata1 = 0;
        int datawatcher1 = 0;
        int packets1 = 0;

        if (this.worldObj.isRemote)
        {
            this.generateModels();
        }

        Iterator sorter = this.modules.iterator();

        while (sorter.hasNext())
        {
            ModuleBase i$ = (ModuleBase)sorter.next();

            if (i$ instanceof ModuleWorker)
            {
                this.workModules.add((ModuleWorker)i$);
            }
            else if (i$ instanceof ModuleEngine)
            {
                this.engineModules.add((ModuleEngine)i$);
            }
            else if (i$ instanceof ModuleTank)
            {
                this.tankModules.add((ModuleTank)i$);
            }
            else if (i$ instanceof ModuleCreativeSupplies)
            {
                this.creativeSupplies = (ModuleCreativeSupplies)i$;
            }
        }

        CompWorkModule sorter1 = new CompWorkModule();
        Collections.sort(this.workModules, sorter1);

        if (!this.isPlaceholder)
        {
            ArrayList i$3 = new ArrayList();
            int module = 0;
            ModuleBase i$1;
            Iterator i$2;

            for (Iterator currentY = this.modules.iterator(); currentY.hasNext(); packets1 += i$1.totalNumberOfPackets())
            {
                i$1 = (ModuleBase)currentY.next();

                if (i$1.hasGui())
                {
                    boolean line = false;
                    i$2 = i$3.iterator();

                    while (i$2.hasNext())
                    {
                        GuiAllocationHelper module1 = (GuiAllocationHelper)i$2.next();

                        if (module1.width + i$1.guiWidth() <= 443)
                        {
                            i$1.setX(module1.width);
                            module1.width += i$1.guiWidth();
                            module1.maxHeight = Math.max(module1.maxHeight, i$1.guiHeight());
                            module1.modules.add(i$1);
                            line = true;
                            break;
                        }
                    }

                    if (!line)
                    {
                        GuiAllocationHelper i$6 = new GuiAllocationHelper();
                        i$1.setX(0);
                        i$6.width = i$1.guiWidth();
                        i$6.maxHeight = i$1.guiHeight();
                        i$6.modules.add(i$1);
                        i$3.add(i$6);
                    }

                    i$1.setGuiDataStart(guidata1);
                    guidata1 += i$1.numberOfGuiData();

                    if (i$1.hasSlots())
                    {
                        module = i$1.generateSlots(module);
                    }
                }

                i$1.setDataWatcherStart(datawatcher1);
                datawatcher1 += i$1.numberOfDataWatchers();

                if (i$1.numberOfDataWatchers() > 0)
                {
                    i$1.initDw();
                }

                i$1.setPacketStart(packets1);
            }

            int currentY1 = 0;
            GuiAllocationHelper line1;

            for (Iterator i$5 = i$3.iterator(); i$5.hasNext(); currentY1 += line1.maxHeight)
            {
                line1 = (GuiAllocationHelper)i$5.next();
                i$2 = line1.modules.iterator();

                while (i$2.hasNext())
                {
                    ModuleBase module3 = (ModuleBase)i$2.next();
                    module3.setY(currentY1);
                }
            }

            if (currentY1 > 168)
            {
                this.canScrollModules = true;
            }

            this.modularSpaceHeight = currentY1;
        }

        Iterator i$4 = this.modules.iterator();

        while (i$4.hasNext())
        {
            ModuleBase module2 = (ModuleBase)i$4.next();
            module2.init();
        }
    }

    private Vec3 getNextblock(boolean flag)
    {
        int i = x();
        int j = y();
        int k = z();

        if (BlockRailBase.func_150049_b_(worldObj, i, j - 1, k))
        {
            --j;
        }

        Block b = worldObj.getBlock(i, j, k);

        if (BlockRailBase.func_150051_a(b))
        {
            int meta = ((BlockRailBase)b).getBasicRailMetadata(worldObj, this, i, j, k);

            if (meta >= 2 && meta <= 5)
            {
                ++j;
            }

            int[][] logic = MinecartModular.railDirectionCoordinates[meta];
            double pX = pushX;
            double pZ = pushZ;
            boolean xDir = pX > 0.0D && logic[0][0] > 0 || pX == 0.0D || logic[0][0] == 0 || pX < 0.0D && logic[0][0] < 0;
            boolean zDir = pZ > 0.0D && logic[0][2] > 0 || pZ == 0.0D || logic[0][2] == 0 || pZ < 0.0D && logic[0][2] < 0;
            int dir = (xDir && zDir) == flag ? 0 : 1;
            return Vec3.createVectorHelper((double)(i + logic[dir][0]), (double)(j + logic[dir][1]), (double)(k + logic[dir][2]));
        }
        else
        {
            return Vec3.createVectorHelper((double)i, (double)j, (double)k);
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (this.worldObj.isRemote)
        {
            for (int i$ = 0; i$ < this.getSizeInventory(); ++i$)
            {
                this.setInventorySlotContents(i$, (ItemStack)null);
            }
        }

        super.setDead();

        if (this.modules != null)
        {
            Iterator var3 = this.modules.iterator();

            while (var3.hasNext())
            {
                ModuleBase module = (ModuleBase)var3.next();
                module.onDeath();
            }
        }

        this.dropChunkLoading();
    }

    @SideOnly(Side.CLIENT)
    public void renderOverlay(Minecraft minecraft)
    {
        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                module.renderOverlay(minecraft);
            }
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    public void updateFuel()
    {
        int consumption = this.getConsumption();

        if (consumption > 0)
        {
            ModuleEngine engine = this.getCurrentEngine();

            if (engine != null)
            {
                engine.consumeFuel(consumption);

                if (!this.isPlaceholder && this.worldObj.isRemote && this.hasFuel() && !this.isDisabled())
                {
                    engine.smoke();
                }
            }
        }

        if (this.hasFuel())
        {
            if (!this.engineFlag)
            {
                this.pushX = this.temppushX;
                this.pushZ = this.temppushZ;
            }
        }
        else if (this.engineFlag)
        {
            this.temppushX = this.pushX;
            this.temppushZ = this.pushZ;
            this.pushX = this.pushZ = 0.0D;
        }

        this.setEngineBurning(this.hasFuel() && !this.isDisabled());
    }

    public boolean isEngineBurning()
    {
        return this.isCartFlag(0);
    }

    public void setEngineBurning(boolean on)
    {
        this.setCartFlag(0, on);
    }

    private boolean isCartFlag(int id)
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1 << id) != 0;
    }

    private void setCartFlag(int id, boolean val)
    {
        if (!this.worldObj.isRemote)
        {
            byte data = (byte)(this.dataWatcher.getWatchableObjectByte(16) & ~(1 << id) | (val ? 1 : 0) << id);
            this.dataWatcher.updateObject(16, Byte.valueOf(data));
        }
    }

    private ModuleEngine getCurrentEngine()
    {
        if (this.modules == null)
        {
            return null;
        }
        else
        {
            Iterator consumption = this.modules.iterator();

            while (consumption.hasNext())
            {
                ModuleBase priority = (ModuleBase)consumption.next();

                if (priority.stopEngines())
                {
                    return null;
                }
            }

            int consumption1 = this.getConsumption(true);
            ArrayList priority1 = new ArrayList();
            int mostImportant = -1;
            Iterator i$ = this.engineModules.iterator();

            while (i$.hasNext())
            {
                ModuleEngine engine = (ModuleEngine)i$.next();

                if (engine.hasFuel(consumption1) && (mostImportant == -1 || mostImportant >= engine.getPriority()))
                {
                    if (engine.getPriority() < mostImportant)
                    {
                        priority1.clear();
                    }

                    mostImportant = engine.getPriority();
                    priority1.add(engine);
                }
            }

            if (priority1.size() > 0)
            {
                if (this.motorRotation >= priority1.size())
                {
                    this.motorRotation = 0;
                }

                this.motorRotation = (this.motorRotation + 1) % priority1.size();
                return (ModuleEngine)priority1.get(this.motorRotation);
            }
            else
            {
                return null;
            }
        }
    }

    public int getConsumption()
    {
        return this.getConsumption(!this.isDisabled() && this.isEngineBurning());
    }

    public int getConsumption(boolean isMoving)
    {
        int consumption = isMoving ? 1 : 0;
        ModuleBase module;

        if (this.modules != null && !this.isPlaceholder)
        {
            for (Iterator i$ = this.modules.iterator(); i$.hasNext(); consumption += module.getConsumption(isMoving))
            {
                module = (ModuleBase)i$.next();
            }
        }

        return consumption;
    }

    public float getEyeHeight()
    {
        return 0.9F;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        if (this.modules != null && this.riddenByEntity != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                float offset = module.mountedOffset(this.riddenByEntity);

                if (offset != 0.0F)
                {
                    return (double)offset;
                }
            }
        }

        return super.getMountedYOffset();
    }

    public ItemStack getCartItem()
    {
        if (this.modules != null)
        {
            ItemStack cart = ModuleData.createModularCart(this);

            if (this.name != null && !this.name.equals("") && !this.name.equals(ModItems.carts.getName()))
            {
                cart.setStackDisplayName(this.name);
            }

            return cart;
        }
        else
        {
            return new ItemStack(ModItems.carts);
        }
    }

    public void killMinecart(DamageSource dmg)
    {
        this.setDead();

        if (this.dropOnDeath())
        {
            this.entityDropItem(this.getCartItem(), 0.0F);

            for (int i = 0; i < this.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.getStackInSlot(i);

                if (itemstack != null)
                {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j = this.rand.nextInt(21) + 10;

                        if (j > itemstack.stackSize)
                        {
                            j = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j;
                        EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double)f, this.posY + (double)f1, this.posZ + (double)f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        this.worldObj.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }
    }

    public boolean dropOnDeath()
    {
        if (this.isPlaceholder)
        {
            return false;
        }
        else
        {
            if (this.modules != null)
            {
                Iterator i$ = this.modules.iterator();

                while (i$.hasNext())
                {
                    ModuleBase module = (ModuleBase)i$.next();

                    if (!module.dropOnDeath())
                    {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public float getMaxCartSpeedOnRail()
    {
        float maxSpeed = super.getMaxCartSpeedOnRail();

        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                float tempMax = module.getMaxSpeed();

                if (tempMax < maxSpeed)
                {
                    maxSpeed = tempMax;
                }
            }
        }

        return maxSpeed;
    }

    public boolean isPoweredCart()
    {
        return this.engineModules.size() > 0;
    }

    public int getDefaultDisplayTileData()
    {
        return -1;
    }

    public int getMinecartType()
    {
        return -1;
    }

    public float[] getColor()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                float[] color = module.getColor();

                if (color[0] != 1.0F || color[1] != 1.0F || color[2] != 1.0F)
                {
                    return color;
                }
            }
        }

        return new float[] {1.0F, 1.0F, 1.0F};
    }

    public int getYTarget()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                int yTarget = module.getYTarget();

                if (yTarget != -1)
                {
                    return yTarget;
                }
            }
        }

        return (int)this.posY;
    }

    public ModuleBase getInterfaceThief()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();

                if (module.doStealInterface())
                {
                    return module;
                }
            }
        }

        return null;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource dmg, float par2)
    {
        if (this.isPlaceholder)
        {
            return false;
        }
        else
        {
            return super.attackEntityFrom(dmg, par2);
        }
    }

    /**
     * Return whether this entity is invulnerable to damage.
     */
    @Override
    public boolean isEntityInvulnerable() {
        if (this.modules != null)
        {
            for(ModuleBase module : this.modules) {

                if (!module.receiveDamage()) {
                    return true;
                }
            }
        }
        return super.isEntityInvulnerable();
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     *
     * @param p_70015_1_
     */
    @Override
    public void setFire(int p_70015_1_) {
        super.setFire(p_70015_1_);
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     *
     * @param p_70081_1_
     */
    @Override
    protected void dealFireDamage(int p_70081_1_) {
        super.dealFireDamage(p_70081_1_);
    }

    /**
     * Called every tick the minecart is on an activator rail. Args: x, y, z, is the rail receiving power
     */
    public void onActivatorRailPass(int x, int y, int z, boolean active)
    {
        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                module.activatedByRail(x, y, z, active);
            }
        }
    }

    public void moveMinecartOnRail(int x, int y, int z, double acceleration)
    {
        super.moveMinecartOnRail(x, y, z, acceleration);

        if (this.modules != null)
        {
            Iterator block = this.modules.iterator();

            while (block.hasNext())
            {
                ModuleBase blockBelow = (ModuleBase)block.next();
                blockBelow.moveMinecartOnRail(x, y, z);
            }
        }

        Block block1 = this.worldObj.getBlock(x, y, z);
        Block blockBelow1 = this.worldObj.getBlock(x, y - 1, z);
        int metaBelow = this.worldObj.getBlockMetadata(x, y - 1, z);
        int m = ((BlockRailBase)block1).getBasicRailMetadata(this.worldObj, this, x, y, z);

        if ((m != 6 && m != 7 || this.motionX >= 0.0D) && (m != 8 && m != 9 || this.motionX <= 0.0D))
        {
            this.cornerFlip = false;
        }
        else
        {
            this.cornerFlip = true;
        }

        if (block1 != ModBlocks.ADVANCED_DETECTOR.getBlock() && !(RailTools.isCartLockedDown(this)) && this.isDisabled())
        {
            this.releaseCart();
        }

        boolean canBeDisabled = block1 == ModBlocks.ADVANCED_DETECTOR.getBlock() && (blockBelow1 != ModBlocks.DETECTOR_UNIT.getBlock() || !DetectorType.getTypeFromMeta(metaBelow).canInteractWithCart() || DetectorType.getTypeFromMeta(metaBelow).shouldStopCart() || RailTools.isCartLockedDown(this));
        boolean forceUnDisable = this.wasDisabled && this.disabledX == x && this.disabledY == y && this.disabledZ == z;

        if (!forceUnDisable && this.wasDisabled)
        {
            this.wasDisabled = false;
        }

        canBeDisabled = forceUnDisable ? false : canBeDisabled;

        if (canBeDisabled && !this.isDisabled())
        {
            this.setIsDisabled(true);

            if (this.pushX != 0.0D || this.pushZ != 0.0D)
            {
                this.temppushX = this.pushX;
                this.temppushZ = this.pushZ;
                this.pushX = this.pushZ = 0.0D;
            }

            this.disabledX = x;
            this.disabledY = y;
            this.disabledZ = z;
        }

        if (this.fixedMX != x || this.fixedMY != y || this.fixedMZ != z)
        {
            this.fixedMeta = -1;
            this.fixedMY = -1;
        }
    }

    public int getRailMeta(int x, int y, int z)
    {
        ModuleBase.RAILDIRECTION dir = ModuleBase.RAILDIRECTION.DEFAULT;
        Iterator Yaw = this.getModules().iterator();

        while (Yaw.hasNext())
        {
            ModuleBase flag = (ModuleBase)Yaw.next();
            dir = flag.getSpecialRailDirection(x, y, z);

            if (dir != ModuleBase.RAILDIRECTION.DEFAULT)
            {
                break;
            }
        }

        if (dir == ModuleBase.RAILDIRECTION.DEFAULT)
        {
            return -1;
        }
        else
        {
            int Yaw1 = (int)(this.rotationYaw % 180.0F);

            if (Yaw1 < 0)
            {
                Yaw1 += 180;
            }

            boolean flag1 = Yaw1 >= 45 && Yaw1 <= 135;

            if (this.fixedMeta == -1)
            {
                switch (MinecartModular.NamelessClass1544293788.$SwitchMap$vswe$stevescarts$Modules$ModuleBase$RAILDIRECTION[dir.ordinal()])
                {
                    case 1:
                        if (flag1)
                        {
                            this.fixedMeta = 0;
                        }
                        else
                        {
                            this.fixedMeta = 1;
                        }

                        break;

                    case 2:
                        if (flag1)
                        {
                            if (this.motionZ > 0.0D)
                            {
                                this.fixedMeta = 9;
                            }
                            else if (this.motionZ <= 0.0D)
                            {
                                this.fixedMeta = 7;
                            }
                        }
                        else if (this.motionX > 0.0D)
                        {
                            this.fixedMeta = 8;
                        }
                        else if (this.motionX < 0.0D)
                        {
                            this.fixedMeta = 6;
                        }

                        break;

                    case 3:
                        if (flag1)
                        {
                            if (this.motionZ > 0.0D)
                            {
                                this.fixedMeta = 8;
                            }
                            else if (this.motionZ <= 0.0D)
                            {
                                this.fixedMeta = 6;
                            }
                        }
                        else if (this.motionX > 0.0D)
                        {
                            this.fixedMeta = 7;
                        }
                        else if (this.motionX < 0.0D)
                        {
                            this.fixedMeta = 9;
                        }

                        break;

                    case 4:
                        if (flag1)
                        {
                            if (this.motionZ > 0.0D)
                            {
                                this.fixedMeta = 0;
                            }
                        }
                        else if (this.motionX > 0.0D)
                        {
                            this.fixedMeta = 7;
                        }
                        else if (this.motionX < 0.0D)
                        {
                            this.fixedMeta = 6;
                        }

                        break;

                    default:
                        this.fixedMeta = -1;
                }

                if (this.fixedMeta == -1)
                {
                    return -1;
                }

                this.fixedMX = x;
                this.fixedMY = y;
                this.fixedMZ = z;
            }

            return this.fixedMeta;
        }
    }

    public void resetRailDirection()
    {
        this.fixedMeta = -1;
    }

    public void turnback()
    {
        this.pushX *= -1.0D;
        this.pushZ *= -1.0D;
        this.temppushX *= -1.0D;
        this.temppushZ *= -1.0D;
        this.motionX *= -1.0D;
        this.motionY *= -1.0D;
        this.motionZ *= -1.0D;
    }

    public void releaseCart()
    {
        this.wasDisabled = true;
        this.setIsDisabled(false);
        this.pushX = this.temppushX;
        this.pushZ = this.temppushZ;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                module.onInventoryChanged();
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        int slotCount = 0;
        ModuleBase module;

        if (this.modules != null)
        {
            for (Iterator i$ = this.modules.iterator(); i$.hasNext(); slotCount += module.getInventorySize())
            {
                module = (ModuleBase)i$.next();
            }
        }

        return slotCount;
    }

    protected void func_145821_a(int par1, int par2, int par3, double par4, double par6, Block par8, int par9)
    {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase)
        {
            float d2 = ((EntityLivingBase)this.riddenByEntity).moveForward;
            ((EntityLivingBase)this.riddenByEntity).moveForward = 0.0F;
            super.func_145821_a(par1, par2, par3, par4, par6, par8, par9);
            ((EntityLivingBase)this.riddenByEntity).moveForward = d2;
        }
        else
        {
            super.func_145821_a(par1, par2, par3, par4, par6, par8, par9);
        }

        double d21 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d21 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D)
        {
            d21 = (double)MathHelper.sqrt_double(d21);
            this.pushX /= d21;
            this.pushZ /= d21;

            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D)
            {
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            }
            else
            {
                this.pushX = this.motionX;
                this.pushZ = this.motionZ;
            }
        }
    }

    protected void applyDrag()
    {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        this.engineFlag = d0 > 1.0E-4D;

        if (this.isDisabled())
        {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }
        else if (this.engineFlag)
        {
            d0 = (double)MathHelper.sqrt_double(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = this.getPushFactor();
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            this.motionX += this.pushX * d1;
            this.motionZ += this.pushZ * d1;
        }
        else
        {
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

    protected double getPushFactor()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                double factor = module.getPushFactor();

                if (factor >= 0.0D)
                {
                    return factor;
                }
            }
        }

        return 0.05D;
    }

    /**
     * Save the entity to NBT (calls an abstract helper method to write extra data)
     */
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setString("cartName", this.name);
        tagCompound.setDouble("pushX", this.pushX);
        tagCompound.setDouble("pushZ", this.pushZ);
        tagCompound.setDouble("temppushX", this.temppushX);
        tagCompound.setDouble("temppushZ", this.temppushZ);
        tagCompound.setShort("workingTime", (short)this.workingTime);
        tagCompound.setByteArray("Modules", this.moduleLoadingData);
        tagCompound.setByte("CartVersion", this.cartVersion);
        tagCompound.setByte("collidedHorizontallyTicks", this.collidedHorizontallyTicks);

        NBTTagList moduleData = new NBTTagList();

        if (this.modules != null)
        {

            for (int i = 0; i < this.modules.size(); ++i)
            {
                NBTTagCompound modCompound = new NBTTagCompound();
                ModuleBase module = (ModuleBase)this.modules.get(i);
                try {
                    module.writeToNBT(modCompound, i);
                    moduleData.appendTag(modCompound);
                } catch (Throwable t) {t.printStackTrace();}
            }
            tagCompound.setTag("ModuleDataList", moduleData);
        }
        tagCompound.setBoolean("returningHome",returningHome);
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.name = tagCompound.getString("cartName");
        this.pushX = tagCompound.getDouble("pushX");
        this.pushZ = tagCompound.getDouble("pushZ");
        this.temppushX = tagCompound.getDouble("temppushX");
        this.temppushZ = tagCompound.getDouble("temppushZ");
        this.workingTime = tagCompound.getShort("workingTime");
        this.cartVersion = tagCompound.getByte("CartVersion");
        this.collidedHorizontallyTicks = tagCompound.getByte("collidedHorizontallyTicks");
        byte oldVersion = this.cartVersion;
        this.loadModules(tagCompound);
        int newSlot;

        if (this.modules != null)
        {
            if(tagCompound.hasKey("ModuleDataList")) {
                NBTTagList moduleData = tagCompound.getTagList("ModuleDataList", NBTHelper.COMPOUND.getId());
                for (newSlot = 0; newSlot < this.modules.size(); ++newSlot)
                {
                    NBTTagCompound modCompound = moduleData.getCompoundTagAt(newSlot);
                    ModuleBase slotCount = (ModuleBase)this.modules.get(newSlot);
                    try {
                        slotCount.readFromNBT(modCompound, newSlot);
                    } catch (Throwable t) {t.printStackTrace();}
                }
            } else {
                for (newSlot = 0; newSlot < this.modules.size(); ++newSlot)
                {
                    ModuleBase slotCount = (ModuleBase)this.modules.get(newSlot);
                    slotCount.readFromNBT(tagCompound, newSlot);
                }
            }
        }
        if(tagCompound.hasKey("returningHome"))
            returningHome = tagCompound.getBoolean("returningHome");

        if (oldVersion < 2)
        {
            newSlot = -1;
            int var8 = 0;
            ModuleBase i;

            for (Iterator lastitem = this.modules.iterator(); lastitem.hasNext(); var8 += i.getInventorySize())
            {
                i = (ModuleBase)lastitem.next();

                if (i instanceof ModuleTool)
                {
                    newSlot = var8;
                    break;
                }
            }

            if (newSlot != -1)
            {
                ItemStack var9 = null;

                for (int var10 = newSlot; var10 < this.getSizeInventory(); ++var10)
                {
                    ItemStack thisitem = this.getStackInSlot(var10);
                    this.setInventorySlotContents(var10, var9);
                    var9 = thisitem;
                }
            }
        }
    }

    public boolean isDisabled()
    {
        return this.isCartFlag(1);
    }

    public void setIsDisabled(boolean disabled)
    {
        this.setCartFlag(1, disabled);
    }


    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     *
     * @param p_70091_1_
     * @param p_70091_3_
     * @param p_70091_5_
    */

    @Override
    public void moveEntity(double p_70091_1_, double p_70091_3_, double p_70091_5_) {
        Vec3 next = getNextblock(false);
        double diffX = 0;
        double diffZ = 0;
        if(next.xCoord != x()) {
            diffZ = 0.15D;
        }
        if(next.zCoord != z()) {
            diffX = 0.15D;
        }

        List collidingList = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(p_70091_1_,0,p_70091_3_).contract(diffX, 0.15D, diffZ));
        collidingList.remove(this.boundingBox);
        if(ridingEntity != null) collidingList.remove(ridingEntity.boundingBox);
        if(riddenByEntity != null) collidingList.remove(riddenByEntity.boundingBox);
        isCollidedForward = !collidingList.isEmpty();

        super.moveEntity(p_70091_1_, p_70091_3_, p_70091_5_);
    }

    @Override
    public void onChunkLoad() {
        super.onChunkLoad();
        for(ModuleBase module : this.modules) {
            if(module instanceof ModuleChunkLoader) {
                if(((ModuleChunkLoader) module).isActive(0)) {
                    this.initChunkLoading();
                }
            }
        }
        ticksFromLoad = 200;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        for(int i = -1; i <= 1; i++) {
            for(int k = -1; k <= 1; k++) {
                if (!this.worldObj.getChunkFromChunkCoords((MathHelper.floor_double(posX) >> 4) + i, (MathHelper.floor_double(posZ) >> 4) + k).isChunkLoaded) {
                    return;
                }
            }
        }
        if(ticksFromLoad > 0) {
            ticksFromLoad--;
            lastTickPosX = posX;
            lastTickPosY = posY;
            lastTickPosZ = posZ;
            return;
        }

        if(!isPlaceholder && getBoundingBox() != null && isInBlock()) {
            label0:
            for(int i = -1; i < 2; i++) {
                for (int k = -1; k < 2; k++) {
                    for (int j = -1; j < 50; j++) {
                        if (BlockRailBase.func_150049_b_(worldObj, x() + i, y() + j, z() + k)) {
                            List list2 = worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().copy().expand(0.5,0,0.5).addCoord(motionX,motionY,motionZ).addCoord(0,5D,0));
                            for(Object entity : list2) {
                                if(entity instanceof EntityPlayer && worldObj.isRemote) {
                                    ((Entity) entity).setPosition(((Entity) entity).posX + i, ((Entity) entity).posY + j, ((Entity) entity).posZ + k);
                                } else if(!(entity instanceof EntityPlayer) && !worldObj.isRemote) {
                                    ((Entity) entity).setPosition(((Entity) entity).posX + i, ((Entity) entity).posY + j, ((Entity) entity).posZ + k);
                                }
                            }
                            if(!worldObj.isRemote) this.setPosition(x() + i + 0.5, y() + j + 0.5, z() + k + 0.5);
                            lastTickPosX = posX;
                            lastTickPosY = posY;
                            lastTickPosZ = posZ;
                            break label0;
                        }
                    }
                }
            }
        }

        if(!worldObj.isRemote && posY < 1) setPosition(posX, 270, posZ);

        super.onUpdate();

        this.onCartUpdate();

        if (this.worldObj.isRemote)
        {
            this.updateSounds();
        }

        if (!isPlaceholder && getBoundingBox() != null) {
            double motX = posX - lastTickPosX;
            double motY = posY - lastTickPosY;
            double motZ = posZ - lastTickPosZ;

            List list2 = worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().copy().expand(0.5,0,0.5).addCoord(motX,motY,motZ).addCoord(0,5D,0));
            for(Object entity : list2) {
                if(entity instanceof EntityPlayer && worldObj.isRemote) {
                    double[] move = new double[]{motX, motY, motZ};
                    move = handleEntityCollision((Entity) entity, move[0], move[1], move[2]);
                    ((Entity) entity).setPosition(((Entity) entity).posX + move[0], ((Entity) entity).posY, ((Entity) entity).posZ + move[2]);
                } else if(!(entity instanceof EntityPlayer) && !worldObj.isRemote) {
                    double[] move = new double[]{motX, motY, motZ};
                    move = handleEntityCollision((Entity) entity, move[0], move[1], move[2]);
                    ((Entity) entity).setPosition(((Entity) entity).posX + move[0], ((Entity) entity).posY, ((Entity) entity).posZ + move[2]);
                }
            }
        }


        if(isCollidedForward && collidedHorizontallyTicks >= 60) {
            collidedHorizontallyTicks = 0;
            turnback();
        } else if(isCollidedForward) {
            collidedHorizontallyTicks++;
        } else if(!isCollidedForward) {
            collidedHorizontallyTicks = 0;
        }

        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
    }

    public boolean isInBlock()
    {
        if(worldObj.getBlock(x(),(int)Math.floor(this.getBoundingBox().maxY),z()).isOpaqueCube()) return true;

        return false;
    }

    public boolean isInAir()
    {
        if(BlockRailBase.func_150049_b_(this.worldObj, x(),y(),z()) || BlockRailBase.func_150049_b_(this.worldObj, x(),y() - 1,z())) return false;
        AxisAlignedBB p_72875_1_ = this.getBoundingBox().copy();
        int i = MathHelper.floor_double(p_72875_1_.minX);
        int j = MathHelper.floor_double(p_72875_1_.maxX + 1.0D);
        int k = MathHelper.floor_double(p_72875_1_.minY);
        int l = MathHelper.floor_double(p_72875_1_.maxY + 1.0D);
        int i1 = MathHelper.floor_double(p_72875_1_.minZ);
        int j1 = MathHelper.floor_double(p_72875_1_.maxZ + 1.0D);

        for (int k1 = i; k1 < j; ++k1)
        {
            for (int l1 = k; l1 < l; ++l1)
            {
                for (int i2 = i1; i2 < j1; ++i2)
                {
                    if (worldObj.getBlock(k1, l1, i2).getMaterial() == Material.air)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private double[] handleEntityCollision(Entity entity, double motionX, double motionY, double motionZ) {
        double X = motionX;
        double Y = motionY;
        double Z = motionZ;
        List list = this.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(X, Y, Z));
        int j;

        for (int i = 0; i < list.size(); ++i)
        {
            Y = ((AxisAlignedBB)list.get(i)).calculateYOffset(entity.boundingBox, Y);
        }

        for (j = 0; j < list.size(); ++j)
        {
            X = ((AxisAlignedBB)list.get(j)).calculateXOffset(entity.boundingBox, X);
        }

        for (j = 0; j < list.size(); ++j)
        {
            Z = ((AxisAlignedBB)list.get(j)).calculateZOffset(entity.boundingBox, Z);
        }

        motionX = X;
        motionY = Y;
        motionZ = Z;

        return new double[] {motionX,motionY,motionZ};
    }



    public void onCartUpdate()
    {
        if (this.modules != null)
        {
            this.updateFuel();
            Iterator i$ = this.modules.iterator();
            ModuleBase module;

            while (i$.hasNext())
            {
                module = (ModuleBase)i$.next();
                module.update();
            }

            i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                module = (ModuleBase)i$.next();
                module.postUpdate();
            }

            this.work();
            this.setCurrentCartSpeedCapOnRail(this.getMaxCartSpeedOnRail());
        }

        if (this.isPlaceholder && this.keepAlive++ > 20)
        {
            this.kill();
            this.placeholderAsssembler.resetPlaceholder();
        }
    }

    public boolean hasFuel()
    {
        if (this.isDisabled())
        {
            return false;
        }
        else
        {
            if (this.modules != null)
            {
                Iterator i$ = this.modules.iterator();

                while (i$.hasNext())
                {
                    ModuleBase module = (ModuleBase)i$.next();

                    if (module.stopEngines())
                    {
                        return false;
                    }
                }
            }

            return this.hasFuelForModule();
        }
    }

    public boolean hasFuelForModule()
    {
        if (this.isPlaceholder)
        {
            return true;
        }
        else
        {
            int consumption = this.getConsumption(true);

            if (this.modules != null)
            {
                Iterator i$ = this.modules.iterator();

                while (i$.hasNext())
                {
                    ModuleBase module = (ModuleBase)i$.next();

                    if (module.hasFuel(consumption))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return entityplayer.getDistanceSq((double)this.x(), (double)this.y(), (double)this.z()) <= 64.0D;
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer entityplayer)
    {
        if (this.isPlaceholder)
        {
            return false;
        }
        else
        {
            if (this.modules != null && !entityplayer.isSneaking())
            {
                boolean interupt = false;
                Iterator i$ = this.modules.iterator();

                while (i$.hasNext())
                {
                    ModuleBase module = (ModuleBase)i$.next();

                    if (module.onInteractFirst(entityplayer))
                    {
                        interupt = true;
                    }
                }

                if (interupt)
                {
                    return true;
                }
            }

            if (!this.worldObj.isRemote)
            {
                if (!this.isDisabled() && this.riddenByEntity != entityplayer)
                {
                    this.temppushX = this.posX - entityplayer.posX;
                    this.temppushZ = this.posZ - entityplayer.posZ;
                }

                if (!this.isDisabled() && this.hasFuel() && this.pushX == 0.0D && this.pushZ == 0.0D)
                {
                    this.pushX = this.temppushX;
                    this.pushZ = this.temppushZ;
                }

                FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 0, this.worldObj, this.getEntityId(), 0, 0);
                this.openInventory();
            }

            return true;
        }
    }

    public void loadChunks()
    {
        this.loadChunks(this.cartTicket, this.x() >> 4, this.z() >> 4);
    }

    public void loadChunks(int chunkX, int chunkZ)
    {
        this.loadChunks(this.cartTicket, chunkX, chunkZ);
    }

    public void loadChunks(Ticket ticket)
    {
        this.loadChunks(ticket, this.x() >> 4, this.z() >> 4);
    }

    public void loadChunks(Ticket ticket, int chunkX, int chunkZ)
    {
        if (!this.worldObj.isRemote && ticket != null)
        {
            if (this.cartTicket == null)
            {
                this.cartTicket = ticket;
            }

            if(prevChunk[0] == 0 && prevChunk[1] == 0) {
                prevChunk = new int[] {chunkX,chunkZ};
            }

            for (int i = -1; i <= 1; ++i)
            {
                for (int j = -1; j <= 1; ++j)
                {
                    ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(chunkX + i, chunkZ + j));
                    if(i != 0 && prevChunk[0] == chunkX + i) {
                        ForgeChunkManager.unforceChunk(ticket, new ChunkCoordIntPair(chunkX + i * 2, chunkZ));
                    }
                    if(j != 0 && prevChunk[1] == chunkZ + j) {
                        ForgeChunkManager.unforceChunk(ticket, new ChunkCoordIntPair(chunkX, chunkZ + j * 2));
                    }
                }
            }

            prevChunk = new int[] {chunkX,chunkZ};
        }
    }

    public void initChunkLoading()
    {
        if (!this.worldObj.isRemote && this.cartTicket == null)
        {
            this.cartTicket = ForgeChunkManager.requestTicket(StevesCarts.instance, this.worldObj, Type.ENTITY);

            if (this.cartTicket != null)
            {
                this.cartTicket.bindEntity(this);
                this.cartTicket.setChunkListDepth(9);
                this.loadChunks();
            }
        }
    }

    public void dropChunkLoading()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.cartTicket != null)
            {
                ForgeChunkManager.releaseTicket(this.cartTicket);
                this.cartTicket = null;
            }
        }
    }

    public void setWorker(ModuleWorker worker)
    {
        if (this.workingComponent != null && worker != null)
        {
            this.workingComponent.stopWorking();
        }

        this.workingComponent = worker;

        if (worker == null)
        {
            this.setWorkingTime(0);
        }
    }

    public ModuleWorker getWorker()
    {
        return this.workingComponent;
    }

    public void setWorkingTime(int val)
    {
        this.workingTime = val;
    }

    private void work()
    {
        if (!this.isPlaceholder)
        {
            if (!this.worldObj.isRemote && this.hasFuel())
            {
                if (this.workingTime <= 0)
                {
                    ModuleWorker oldComponent = this.workingComponent;

                    if (this.workingComponent != null)
                    {
                        boolean work = false;
                        try {
                            try {
                                currentTool = StatCollector.translateToLocal("item.SC2:" + workingComponent.getData().getRawName() + ".name");
                            } catch (Throwable t) {
                                currentTool = "ERROR GETTING NAME";
                            }
                            work = this.workingComponent.work();
                        } catch (Throwable t) {
                            currentTool = "ERROR";
                            t.printStackTrace();
                        }

                        if (this.workingComponent != null && oldComponent == this.workingComponent && this.workingTime <= 0 && !this.workingComponent.preventAutoShutdown())
                        {
                            this.workingComponent.stopWorking();
                        }

                        if (work)
                        {
                            this.work();
                            return;
                        }
                    }

                    if (this.workModules != null)
                    {
                        Iterator var4 = this.workModules.iterator();

                        while (var4.hasNext())
                        {
                            ModuleWorker module = (ModuleWorker)var4.next();

                            try {
                                try {
                                    currentTool = StatCollector.translateToLocal("item.SC2:" + module.getData().getRawName() + ".name");
                                } catch (Throwable t) {
                                    currentTool = "ERROR GETTING NAME";
                                }
                                if (module.work()) {
                                    return;
                                }
                            } catch (Throwable t) {
                                currentTool = "ERROR";
                                t.printStackTrace();
                            }
                        }
                    }
                }
                else
                {
                    --this.workingTime;
                }
            }
        }
    }

    public void handleActivator(ActivatorOption option, boolean isOrange)
    {
        Iterator i$ = this.modules.iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof IActivatorModule && option.getModule().isAssignableFrom(module.getClass()))
            {
                IActivatorModule iactivator = (IActivatorModule)module;

                if (option.shouldActivate(isOrange))
                {
                    iactivator.doActivate(option.getId());
                }
                else if (option.shouldDeactivate(isOrange))
                {
                    iactivator.doDeActivate(option.getId());
                }
                else if (option.shouldToggle())
                {
                    if (iactivator.isActive(option.getId()))
                    {
                        iactivator.doDeActivate(option.getId());
                    }
                    else
                    {
                        iactivator.doActivate(option.getId());
                    }
                }
            }
        }
    }

    public boolean getRenderFlippedYaw(float yaw)
    {
        yaw %= 360.0F;

        if (yaw < 0.0F)
        {
            yaw += 360.0F;
        }

        if (this.oldRender && Math.abs(yaw - this.lastRenderYaw) >= 90.0F && Math.abs(yaw - this.lastRenderYaw) <= 270.0F && (this.motionX <= 0.0D || this.lastMotionX >= 0.0D) && (this.motionZ <= 0.0D || this.lastMotionZ >= 0.0D) && (this.motionX >= 0.0D || this.lastMotionX <= 0.0D) && (this.motionZ >= 0.0D || this.lastMotionZ <= 0.0D) && this.wrongRender < 50)
        {
            ++this.wrongRender;
            return true;
        }
        else
        {
            this.lastMotionX = this.motionX;
            this.lastMotionZ = this.motionZ;
            this.lastRenderYaw = yaw;
            this.oldRender = true;
            this.wrongRender = 0;
            return false;
        }
    }

    public ArrayList<String> getLabel()
    {
        ArrayList label = new ArrayList();

        if (this.getModules() != null)
        {
            Iterator i$ = this.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                module.addToLabel(label);
            }
        }

        return label;
    }

    public int x()
    {
        return MathHelper.floor_double(this.posX);
    }

    public int y()
    {
        return MathHelper.floor_double(this.posY);
    }

    public int z()
    {
        return MathHelper.floor_double(this.posZ);
    }

    public void addItemToChest(ItemStack iStack)
    {
        TransferHandler.TransferItem(iStack, this, this.getCon((InventoryPlayer)null), Slot.class, (Class)null, -1);
    }

    public void addItemToChest(ItemStack iStack, int start, int end)
    {
        TransferHandler.TransferItem(iStack, this, start, end, this.getCon((InventoryPlayer)null), Slot.class, (Class)null, -1);
    }

    public void addItemToChest(ItemStack iStack, Class validSlot, Class invalidSlot)
    {
        TransferHandler.TransferItem(iStack, this, this.getCon((InventoryPlayer)null), validSlot, invalidSlot, -1);
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        ModuleBase module;

        if (this.modules != null)
        {
            for (Iterator i$ = this.modules.iterator(); i$.hasNext(); i -= module.getInventorySize())
            {
                module = (ModuleBase)i$.next();

                if (i < module.getInventorySize())
                {
                    return module.getStack(i);
                }
            }
        }

        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack item)
    {
        ModuleBase module;

        if (this.modules != null)
        {
            for (Iterator i$ = this.modules.iterator(); i$.hasNext(); i -= module.getInventorySize())
            {
                module = (ModuleBase)i$.next();

                if (i < module.getInventorySize())
                {
                    module.setStack(i, item);
                    break;
                }
            }
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int n)
    {
        if (this.modules == null)
        {
            return null;
        }
        else if (this.getStackInSlot(i) != null)
        {
            ItemStack item;

            if (this.getStackInSlot(i).stackSize <= n)
            {
                item = this.getStackInSlot(i);
                this.setInventorySlotContents(i, (ItemStack)null);
                return item;
            }
            else
            {
                item = this.getStackInSlot(i).splitStack(n);

                if (this.getStackInSlot(i).stackSize == 0)
                {
                    this.setInventorySlotContents(i, (ItemStack)null);
                }

                return item;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.getStackInSlot(i) != null)
        {
            ItemStack var2 = this.getStackInSlot(i);
            this.setInventorySlotContents(i, (ItemStack)null);
            return var2;
        }
        else
        {
            return null;
        }
    }

    public Container getCon(InventoryPlayer player)
    {
        return new ContainerMinecart(player, this);
    }

    public void openInventory()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();

                if (module instanceof ModuleChest)
                {
                    ((ModuleChest)module).openChest();
                }
            }
        }
    }

    public void closeInventory()
    {
        if (this.modules != null)
        {
            Iterator i$ = this.modules.iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();

                if (module instanceof ModuleChest)
                {
                    ((ModuleChest)module).closeChest();
                }
            }
        }
    }

    public void setPlaceholder(TileEntityCartAssembler assembler)
    {
        this.isPlaceholder = true;
        this.placeholderAsssembler = assembler;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return super.getBoundingBox();
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return super.canBeCollidedWith();
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    private void generateModels()
    {
        if (this.modules != null)
        {
            ArrayList invalid = new ArrayList();
            Iterator i = this.modules.iterator();
            ModuleBase module;
            ModuleData data;

            while (i.hasNext())
            {
                module = (ModuleBase)i.next();
                data = module.getData();

                if (data.haveRemovedModels())
                {
                    Iterator models = data.getRemovedModels().iterator();

                    while (models.hasNext())
                    {
                        String i$ = (String)models.next();
                        invalid.add(i$);
                    }
                }
            }

            for (int var8 = this.modules.size() - 1; var8 >= 0; --var8)
            {
                module = (ModuleBase)this.modules.get(var8);
                data = module.getData();

                if (data != null && data.haveModels(this.isPlaceholder))
                {
                    ArrayList var9 = new ArrayList();
                    Iterator var10 = data.getModels(this.isPlaceholder).keySet().iterator();

                    while (var10.hasNext())
                    {
                        String str = (String)var10.next();

                        if (!invalid.contains(str))
                        {
                            var9.add(data.getModels(this.isPlaceholder).get(str));
                            invalid.add(str);
                        }
                    }

                    if (var9.size() > 0)
                    {
                        module.setModels(var9);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player)
    {
        return new GuiMinecart(player.inventory, this);
    }

    public void writeSpawnData(ByteBuf data)
    {
        data.writeByte(this.moduleLoadingData.length);
        byte[] arr$ = this.moduleLoadingData;
        int len$ = arr$.length;
        int i$;
        byte b;

        for (i$ = 0; i$ < len$; ++i$)
        {
            b = arr$[i$];
            data.writeByte(b);
        }

        data.writeByte(this.name.getBytes().length);
        arr$ = this.name.getBytes();
        len$ = arr$.length;

        for (i$ = 0; i$ < len$; ++i$)
        {
            b = arr$[i$];
            data.writeByte(b);
        }
    }

    public void readSpawnData(ByteBuf data)
    {
        byte length = data.readByte();
        byte[] bytes = new byte[length];
        data.readBytes(bytes);
        this.loadModules(bytes);
        byte nameLength = data.readByte();
        byte[] nameBytes = new byte[nameLength];

        for (int i = 0; i < nameLength; ++i)
        {
            nameBytes[i] = data.readByte();
        }

        this.name = new String(nameBytes);

        if (this.getDataWatcher() instanceof DataWatcherLockable)
        {
            ((DataWatcherLockable)this.getDataWatcher()).release();
        }
    }

    public void setScrollY(int val)
    {
        if (this.canScrollModules)
        {
            this.scrollY = val;
        }
    }

    public int getScrollY()
    {
        return this.getInterfaceThief() != null ? 0 : this.scrollY;
    }

    public int getRealScrollY()
    {
        return (int)((float)(this.modularSpaceHeight - 168) / 198.0F * (float)this.getScrollY());
    }

    public int fill(FluidStack resource, boolean doFill)
    {
        return this.fill(ForgeDirection.UNKNOWN, resource, doFill);
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int amount = 0;

        if (resource != null && resource.amount > 0)
        {
            FluidStack fluid = resource.copy();

            for (int i = 0; i < this.tankModules.size(); ++i)
            {
                int tempAmount = ((ModuleTank)this.tankModules.get(i)).fill(fluid, doFill);
                amount += tempAmount;
                fluid.amount -= tempAmount;

                if (fluid.amount <= 0)
                {
                    break;
                }
            }
        }

        return amount;
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.drain(from, (FluidStack)null, maxDrain, doDrain);
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return this.drain(from, resource, resource == null ? 0 : resource.amount, doDrain);
    }

    private FluidStack drain(ForgeDirection from, FluidStack resource, int maxDrain, boolean doDrain)
    {
        FluidStack ret = resource;

        if (resource != null)
        {
            ret = resource.copy();
            ret.amount = 0;
        }

        for (int i = 0; i < this.tankModules.size(); ++i)
        {
            FluidStack temp = null;
            temp = ((ModuleTank)this.tankModules.get(i)).drain(maxDrain, doDrain);

            if (temp != null && (ret == null || ret.isFluidEqual(temp)))
            {
                if (ret == null)
                {
                    ret = temp;
                }
                else
                {
                    ret.amount += temp.amount;
                }

                maxDrain -= temp.amount;

                if (maxDrain <= 0)
                {
                    break;
                }
            }
        }

        return ret != null && ret.amount == 0 ? null : ret;
    }

    public int drain(Fluid type, int maxDrain, boolean doDrain)
    {
        int amount = 0;

        if (type != null && maxDrain > 0)
        {
            Iterator i$ = this.tankModules.iterator();

            while (i$.hasNext())
            {
                ModuleTank tank = (ModuleTank)i$.next();
                FluidStack drained = tank.drain(maxDrain, false);

                if (drained != null && type.equals(drained.getFluid()))
                {
                    amount += drained.amount;
                    maxDrain -= drained.amount;

                    if (doDrain)
                    {
                        tank.drain(drained.amount, true);
                    }

                    if (maxDrain <= 0)
                    {
                        break;
                    }
                }
            }
        }

        return amount;
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection direction)
    {
        FluidTankInfo[] ret = new FluidTankInfo[this.tankModules.size()];

        for (int i = 0; i < ret.length; ++i)
        {
            ret[i] = new FluidTankInfo(((ModuleTank)this.tankModules.get(i)).getFluid(), ((ModuleTank)this.tankModules.get(i)).getCapacity());
        }

        return ret;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        ModuleBase module;

        if (this.modules != null)
        {
            for (Iterator i$ = this.modules.iterator(); i$.hasNext(); slot -= module.getInventorySize())
            {
                module = (ModuleBase)i$.next();

                if (slot < module.getInventorySize())
                {
                    return ((SlotBase)module.getSlots().get(slot)).isItemValid(item);
                }
            }
        }

        return false;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "container.modularcart";
    }

    public String getCartName()
    {
        return this.name != null && this.name.length() != 0 ? this.name : "Modular Cart";
    }

    public boolean hasCreativeSupplies()
    {
        return this.creativeSupplies != null;
    }

    public boolean canRiderInteract()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void setSound(MovingSound sound, boolean riding)
    {
        if (riding)
        {
            this.soundRiding = sound;
        }
        else
        {
            this.sound = sound;
        }
    }

    @SideOnly(Side.CLIENT)
    public void silent()
    {
        this.keepSilent = 6;
    }

    @SideOnly(Side.CLIENT)
    private void updateSounds()
    {
        if (this.keepSilent > 1)
        {
            --this.keepSilent;
            this.stopSound(this.sound);
            this.stopSound(this.soundRiding);
            this.sound = null;
            this.soundRiding = null;
        }
        else if (this.keepSilent == 1)
        {
            this.keepSilent = 0;
            Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundMinecart(this));
            Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundMinecartRiding(Minecraft.getMinecraft().thePlayer, this));
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopSound(MovingSound sound)
    {
        if (sound != null)
        {
            ReflectionHelper.setPrivateValue(MovingSound.class, sound, Boolean.valueOf(true), 0);
        }
    }

    /**
     * Add energy to an IEnergyReceiver, internal distribution is left entirely to the IEnergyReceiver.
     *
     * @param from       Orientation the energy is received from.
     * @param maxReceive Maximum amount of energy to receive.
     * @param simulate   If TRUE, the charge will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) received.
     */
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int stored = maxReceive;
        for(ModuleEngine engine : getEngines()) {
            if(engine instanceof IEnergyReceiver) {
                stored -= ((IEnergyReceiver)engine).receiveEnergy(from, stored, simulate);
                if(stored < 0) StevesCarts.logger.info("Minecart received too much energy. " + stored * -1 + " too much.");
                if(stored == 0) break;
            }
        }
        return maxReceive - stored;
    }

    /**
     * Returns the amount of energy currently stored.
     *
     * @param from
     */
    @Override
    public int getEnergyStored(ForgeDirection from) {
        int stored = 0;
        for(ModuleEngine engine : getEngines()) {
            if(engine instanceof IEnergyReceiver) {
                stored += ((IEnergyReceiver)engine).getEnergyStored(from);
            }
        }
        return stored;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     *
     * @param from
     */
    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        int stored = 0;
        for(ModuleEngine engine : getEngines()) {
            if(engine instanceof IEnergyReceiver) {
                stored += ((IEnergyReceiver)engine).getMaxEnergyStored(from);
            }
        }
        return stored;
    }

    /**
     * Returns TRUE if the TileEntity can connect on a given side.
     *
     * @param from
     */
    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    static class NamelessClass1544293788
    {
        static final int[] $SwitchMap$vswe$stevescarts$Modules$ModuleBase$RAILDIRECTION = new int[ModuleBase.RAILDIRECTION.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Modules$ModuleBase$RAILDIRECTION[ModuleBase.RAILDIRECTION.FORWARD.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Modules$ModuleBase$RAILDIRECTION[ModuleBase.RAILDIRECTION.LEFT.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Modules$ModuleBase$RAILDIRECTION[ModuleBase.RAILDIRECTION.RIGHT.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Modules$ModuleBase$RAILDIRECTION[ModuleBase.RAILDIRECTION.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    @Override
    public void markDirty() {
        //worldObj.updateEntityWithOptionalForce(this, false);
    }
}
