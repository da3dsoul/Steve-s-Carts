package vswe.stevescarts.Modules.Workers;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotTorch;

import java.util.UUID;

public class ModuleTorch extends ModuleWorker implements ISuppliesModule
{
    private int light;
    private int lightLimit = 8;
    private int[] boxRect = new int[] {12, this.guiHeight() - 10, 46, 9};
    boolean markerMoving = false;

    private boolean init = false;

    private FakePlayer fakePlayer;

    public ModuleTorch(MinecartModular cart)
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
        return new SlotTorch(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public byte getWorkPriority()
    {
        return (byte)95;
    }

    private void setup() {
        if(!this.getCart().worldObj.isRemote) {
            fakePlayer = FakePlayerFactory.get((WorldServer) this.getCart().worldObj, new GameProfile(UUID.fromString("5ae51d0b-e8bc-5a02-09f4-b5dbb05963da"), "[CoFH]"));
            fakePlayer.addedToChunk = false;
        }
    }

    public void updateFakePlayer(int var1, int x, int y, int z, int facing) {
        fakePlayer.inventory.currentItem = 0;
        fakePlayer.inventory.setInventorySlotContents(0, getStack(var1).copy());

        float var9;
        // South -> 0
        // West -> 90
        // North -> 180
        // East -> -90
        // North, South, West, East
        switch(facing) {
            case 2:
                var9 = 180.0F;
                break;
            case 3:
                var9 = 0.0F;
                break;
            case 4:
                var9 = 90.0F;
                break;
            case 5:
                var9 = -90.0F;
                break;
            default: var9 = 0.0F;
                break;
        }

        fakePlayer.setLocationAndAngles(x + 0.5, y + 0.5, z + 0.5, var9, 0);
    }

    public boolean simRightClick(ItemStack stack1, int x, int y, int z, int facing) {
        ItemStack stack = stack1.copy();
        if(stack != null && !this.simRightClick2(stack, x, y, z, facing)) {
            return false;
        }

        PlayerInteractEvent playerInteractEvent = ForgeEventFactory.onPlayerInteract(fakePlayer, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1, this.getCart().worldObj);
        if(playerInteractEvent.useItem == Event.Result.DENY) {
            return false;
        }

        stack.useItemRightClick(this.getCart().worldObj, fakePlayer);

        return true;
    }

    public boolean simRightClick2(ItemStack stack, int x, int y, int z, int side) {
        float hitX = 0.5F;
        float hitY = 0.5F;
        float hitZ = 0.5F;

        PlayerInteractEvent playerInteractEvent = ForgeEventFactory.onPlayerInteract(fakePlayer, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, this.getCart().worldObj);
        if(playerInteractEvent.isCanceled()) {
            return false;
        } else {
            if(stack.getItem().onItemUseFirst(stack, fakePlayer, this.getCart().worldObj, x, y, z, 1, hitX, hitY, hitZ)) {
                return true;
            } else {
                if(stack.getItem().onItemUseFirst(stack, fakePlayer, this.getCart().worldObj, x, y, z, side, hitX, hitY, hitZ)) {
                   return true;
                }
                if(!stack.tryPlaceItemIntoWorld(fakePlayer, this.getCart().worldObj, x, y, z, 1, hitX, hitY, hitZ)) {
                    if(!stack.tryPlaceItemIntoWorld(fakePlayer, this.getCart().worldObj, x, y, z, side, hitX, hitY, hitZ)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public boolean work()
    {
        if(!init) {
            setup();
            init = true;
        }
        Vec3 next = this.getLastblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;

        if (this.light <= this.lightLimit)
        {
            label65:

            for (int side = -1; side <= 1; side += 2)
            {
                int xTorch = x + (this.getCart().z() != z ? side : 0);
                int zTorch = z + (this.getCart().x() != x ? side : 0);

                label66:
                for (int level = 2; level >= -2; --level)
                {
                    if (this.getCart().worldObj.isAirBlock(xTorch, y + level, zTorch) || this.getCart().worldObj.getBlock(xTorch, y + level, zTorch).isReplaceable(this.getCart().worldObj, xTorch, y + level, zTorch))
                    {
                        int i = 0;

                        while (true)
                        {
                            if (i >= this.getInventorySize())
                            {
                                continue label66;
                            }

                            if (this.getStack(i) != null) {
                                Block block = Block.getBlockFromItem(this.getStack(i).getItem());
                                if (block != null && block.canPlaceBlockAt(getCart().worldObj, xTorch, y + level, zTorch)) {
                                    if (this.doPreWork()) {
                                        this.startWorking(3);
                                        return true;
                                    }

                                    int facing = 0;
                                    if(x > xTorch) {
                                        facing = ForgeDirection.WEST.ordinal();
                                    } else if(x < xTorch) {
                                        facing = ForgeDirection.EAST.ordinal();
                                    } else if(z > zTorch) {
                                        facing = ForgeDirection.NORTH.ordinal();
                                    } else if(z < zTorch) {
                                        facing = ForgeDirection.SOUTH.ordinal();
                                    }

                                    updateFakePlayer(i, x, y + level, z, facing);
                                    boolean placed = simRightClick(getStack(i), xTorch, y + level, zTorch, facing);


                                    if(placed) {
                                        if (!this.getCart().hasCreativeSupplies()) {
                                            --this.getStack(i).stackSize;

                                            if (this.getStack(i).stackSize == 0) {
                                                this.setStack(i, (ItemStack) null);
                                            }

                                            this.onInventoryChanged();
                                        }

                                        continue label65;
                                    }
                                }
                            }

                            ++i;
                        }
                    }

                    if (this.getCart().worldObj.getBlock(xTorch, y + level, zTorch).getLightValue(this.getCart().worldObj, xTorch, y + level, zTorch) >= 0)
                    {
                        break;
                    }
                }
            }
        }

        if(!this.doPreWork()) this.stopWorking();
        return false;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/torch.png");
        int barLength = 3 * this.light;

        if (this.light == 15)
        {
            --barLength;
        }

        int srcX = 0;

        if (this.inRect(x, y, this.boxRect))
        {
            srcX += this.boxRect[2];
        }

        this.drawImage(gui, this.boxRect, srcX, 0);
        this.drawImage(gui, 13, this.guiHeight() - 10 + 1, 0, 9, barLength, 7);
        this.drawImage(gui, 12 + 3 * this.lightLimit, this.guiHeight() - 10, 0, 16, 1, 9);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, "Threshold: " + this.lightLimit + " Current: " + this.light, x, y, this.boxRect);
    }

    public int guiHeight()
    {
        return super.guiHeight() + 10;
    }

    public int numberOfGuiData()
    {
        return 2;
    }

    protected void checkGuiData(Object[] info)
    {
        short data = (short)(this.light & 15);
        data |= (short)((this.lightLimit & 15) << 4);
        this.updateGuiData(info, 0, data);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.light = data & 15;
            this.lightLimit = (data & 240) >> 4;
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            this.lightLimit = data[0];

            if (this.lightLimit < 0)
            {
                this.lightLimit = 0;
            }
            else if (this.lightLimit > 15)
            {
                this.lightLimit = 15;
            }
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0 && this.inRect(x, y, this.boxRect))
        {
            this.generatePacket(x, y);
            this.markerMoving = true;
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (this.markerMoving)
        {
            this.generatePacket(x, y);
        }

        if (button != -1)
        {
            this.markerMoving = false;
        }
    }

    private void generatePacket(int x, int y)
    {
        int xInBox = x - this.boxRect[0];
        int val = xInBox / 3;

        if (val < 0)
        {
            val = 0;
        }
        else if (val > 15)
        {
            val = 15;
        }

        this.sendPacket(0, (byte)val);
    }

    public void setThreshold(byte val)
    {
        this.lightLimit = val;
    }

    public int getThreshold()
    {
        return this.lightLimit;
    }

    public int getLightLevel()
    {
        return this.light;
    }

    public void update()
    {
        super.update();
        this.light = this.getCart().worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Block,this.getCart().x(), this.getCart().y() + 1, this.getCart().z());
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
        this.calculateTorches();
    }

    private void calculateTorches()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            int val = 0;

            for (int i = 0; i < 3; ++i)
            {
                val |= (this.getStack(i) != null ? 1 : 0) << i;
            }

            this.updateDw(0, val);
        }
    }

    public int getTorches()
    {
        return this.isPlaceholder() ? this.getSimInfo().getTorchInfo() : this.getDw(0);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("lightLimit", id), (byte)this.lightLimit);
        //tagCompound.setByte("pass", (byte)this.pass);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.lightLimit = tagCompound.getByte(this.generateNBTName("lightLimit", id));
        //this.pass = tagCompound.getByte("pass");
        this.calculateTorches();
    }

    public boolean haveSupplies()
    {
        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            ItemStack item = this.getStack(i);

            if (item != null)
            {
                return true;
            }
        }

        return false;
    }
}
