package vswe.stevescarts.Modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.Buttons.ButtonBase;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerMinecart;
import vswe.stevescarts.Helpers.CompButtons;
import vswe.stevescarts.Helpers.NBTHelper;
import vswe.stevescarts.Helpers.SimulationInfo;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Slots.SlotBase;

public abstract class ModuleBase
{
    private MinecartModular cart;
    private ItemStack[] cargo;
    private int offSetX;
    private int offSetY;
    private int guiDataOffset;
    private int dataWatcherOffset;
    private int packetOffset;
    private ArrayList<ButtonBase> buttons;
    private CompButtons buttonSorter;
    protected int slotGlobalStart;
    private byte moduleId;
    private ArrayList<ModelCartbase> models;
    protected ArrayList<SlotBase> slotList;
    private int moduleButtonId = 0;

    public ModuleBase(MinecartModular cart)
    {
        this.cart = cart;
        this.cargo = new ItemStack[this.getInventorySize()];
    }

    public void init()
    {
        if (this.useButtons())
        {
            this.buttons = new ArrayList();
            this.buttonSorter = new CompButtons();
            this.loadButtons();
            this.buttonVisibilityChanged();
        }
    }

    public void preInit() {}

    public MinecartModular getCart()
    {
        return this.cart;
    }

    public boolean isPlaceholder()
    {
        return this.getCart().isPlaceholder;
    }

    protected SimulationInfo getSimInfo()
    {
        return this.getCart().placeholderAsssembler.getSimulationInfo();
    }

    public void setModuleId(byte val)
    {
        this.moduleId = val;
    }

    public byte getModuleId()
    {
        return this.moduleId;
    }

    public void onInventoryChanged() {}

    public int getX()
    {
        return this.doStealInterface() ? 0 : this.offSetX;
    }

    public int getY()
    {
        return this.doStealInterface() ? 0 : this.offSetY;
    }

    public void setX(int val)
    {
        this.offSetX = val;
    }

    public void setY(int val)
    {
        this.offSetY = val;
    }

    public int getInventorySize()
    {
        return !this.hasSlots() ? 0 : this.getInventoryWidth() * this.getInventoryHeight();
    }

    public int guiWidth()
    {
        return 15 + this.getInventoryWidth() * 18;
    }

    public int guiHeight()
    {
        return 27 + this.getInventoryHeight() * 18;
    }

    protected int getInventoryWidth()
    {
        return 3;
    }

    protected int getInventoryHeight()
    {
        return 1;
    }

    public void keyPress(GuiMinecart gui, char character, int extraInformation) {}

    public ArrayList<SlotBase> getSlots()
    {
        return this.slotList;
    }

    public int generateSlots(int slotCount)
    {
        this.slotGlobalStart = slotCount;
        this.slotList = new ArrayList();

        for (int j = 0; j < this.getInventoryHeight(); ++j)
        {
            for (int i = 0; i < this.getInventoryWidth(); ++i)
            {
                this.slotList.add(this.getSlot(slotCount++, i, j));
            }
        }

        return slotCount;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return null;
    }

    public boolean hasSlots()
    {
        return this.hasGui();
    }

    public void update() {}

    public boolean hasFuel(int consumption)
    {
        return false;
    }

    public float getMaxSpeed()
    {
        return 1.1F;
    }

    public int getYTarget()
    {
        return -1;
    }

    public void moveMinecartOnRail(int x, int y, int z) {}

    public ItemStack getStack(int slot)
    {
        return this.cargo[slot];
    }

    public void setStack(int slot, ItemStack item)
    {
        this.cargo[slot] = item;
    }

    public void addStack(int slotStart, int slotEnd, ItemStack item)
    {
        this.getCart().addItemToChest(item, this.slotGlobalStart + slotStart, this.slotGlobalStart + slotEnd);
    }

    public void addStack(int slot, ItemStack item)
    {
        this.addStack(slot, slot, item);
    }

    public boolean dropOnDeath()
    {
        return true;
    }

    public void onDeath() {}

    public boolean hasGui()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiMinecart gui) {}

    @SideOnly(Side.CLIENT)
    public void drawString(GuiMinecart gui, String str, int[] rect, int c)
    {
        if (rect.length >= 4)
        {
            this.drawString(gui, str, rect[0] + (rect[2] - gui.getFontRenderer().getStringWidth(str)) / 2, rect[1] + (rect[3] - gui.getFontRenderer().FONT_HEIGHT + 3) / 2, c);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawString(GuiMinecart gui, String str, int x, int y, int c)
    {
        this.drawString(gui, str, x, y, -1, false, c);
    }

    @SideOnly(Side.CLIENT)
    public void drawString(GuiMinecart gui, String str, int x, int y, int w, boolean center, int c)
    {
        int j = gui.getGuiLeft();
        int k = gui.getGuiTop();
        int[] rect = new int[] {x, y, w, 8};

        if (!this.doStealInterface())
        {
            this.handleScroll(rect);
        }

        if (rect[3] == 8)
        {
            if (center)
            {
                gui.getFontRenderer().drawString(str, rect[0] + (rect[2] - gui.getFontRenderer().getStringWidth(str)) / 2 + this.getX(), rect[1] + this.getY(), c);
            }
            else
            {
                gui.getFontRenderer().drawString(str, rect[0] + this.getX(), rect[1] + this.getY(), c);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawStringWithShadow(GuiMinecart gui, String str, int x, int y, int c)
    {
        int j = gui.getGuiLeft();
        int k = gui.getGuiTop();
        int[] rect = new int[] {x, y, 0, 8};

        if (!this.doStealInterface())
        {
            this.handleScroll(rect);
        }

        if (rect[3] == 8)
        {
            gui.getFontRenderer().drawStringWithShadow(str, rect[0] + this.getX(), rect[1] + this.getY(), c);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawSplitString(GuiMinecart gui, String str, int x, int y, int w, int c)
    {
        this.drawSplitString(gui, str, x, y, w, false, c);
    }

    @SideOnly(Side.CLIENT)
    public void drawSplitString(GuiMinecart gui, String str, int x, int y, int w, boolean center, int c)
    {
        List newlines = gui.getFontRenderer().listFormattedStringToWidth(str, w);

        for (int i = 0; i < newlines.size(); ++i)
        {
            String line = newlines.get(i).toString();
            this.drawString(gui, line, x, y + i * 8, w, center, c);
        }
    }

    public void drawItemInInterface(GuiMinecart gui, ItemStack item, int x, int y)
    {
        int[] rect = new int[] {x, y, 16, 16};
        this.handleScroll(rect);

        if (rect[3] == 16)
        {
            RenderItem renderitem = new RenderItem();
            GL11.glDisable(GL11.GL_LIGHTING);
            renderitem.renderItemIntoGUI(gui.getMinecraft().fontRenderer, gui.getMinecraft().renderEngine, item, gui.getGuiLeft() + rect[0] + this.getX(), gui.getGuiTop() + rect[1] + this.getY());
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(GuiMinecart gui, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY)
    {
        this.drawImage(gui, targetX, targetY, srcX, srcY, sizeX, sizeY, GuiBase.RENDER_ROTATION.NORMAL);
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(GuiMinecart gui, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY, GuiBase.RENDER_ROTATION rotation)
    {
        this.drawImage(gui, new int[] {targetX, targetY, sizeX, sizeY}, srcX, srcY, rotation);
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(GuiMinecart gui, int[] rect, int srcX, int srcY)
    {
        this.drawImage(gui, rect, srcX, srcY, GuiBase.RENDER_ROTATION.NORMAL);
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(GuiMinecart gui, int[] rect, int srcX, int srcY, GuiBase.RENDER_ROTATION rotation)
    {
        if (rect.length >= 4)
        {
            rect = this.cloneRect(rect);

            if (!this.doStealInterface())
            {
                srcY -= this.handleScroll(rect);
            }

            if (rect[3] > 0)
            {
                gui.drawTexturedModalRect(gui.getGuiLeft() + rect[0] + this.getX(), gui.getGuiTop() + rect[1] + this.getY(), srcX, srcY, rect[2], rect[3], rotation);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(GuiMinecart gui, IIcon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY)
    {
        this.drawImage(gui, icon, new int[] {targetX, targetY, sizeX, sizeY}, srcX, srcY);
    }

    @SideOnly(Side.CLIENT)
    public void drawImage(GuiMinecart gui, IIcon icon, int[] rect, int srcX, int srcY)
    {
        if (rect.length >= 4)
        {
            rect = this.cloneRect(rect);

            if (!this.doStealInterface())
            {
                srcY -= this.handleScroll(rect);
            }

            if (rect[3] > 0)
            {
                gui.drawIcon(icon, gui.getGuiLeft() + rect[0] + this.getX(), gui.getGuiTop() + rect[1] + this.getY(), (float)rect[2] / 16.0F, (float)rect[3] / 16.0F, (float)srcX / 16.0F, (float)srcY / 16.0F);
            }
        }
    }

    public int handleScroll(int[] rect)
    {
        rect[1] -= this.getCart().getRealScrollY();
        int y = rect[1] + this.getY();

        if (y < 4)
        {
            int dif = y - 4;
            rect[3] += dif;
            byte y1 = 4;
            rect[1] = y1 - this.getY();
            return dif;
        }
        else if (y + rect[3] > 168)
        {
            rect[3] = Math.max(0, 168 - y);
            return 0;
        }
        else
        {
            return 0;
        }
    }

    private int[] cloneRect(int[] rect)
    {
        return new int[] {rect[0], rect[1], rect[2], rect[3]};
    }

    public boolean useButtons()
    {
        return false;
    }

    public final void buttonVisibilityChanged()
    {
        Collections.sort(this.buttons, this.buttonSorter);
        ButtonBase.LOCATION lastLoc = null;
        int id = 0;
        Iterator i$ = this.buttons.iterator();

        while (i$.hasNext())
        {
            ButtonBase button = (ButtonBase)i$.next();

            if (button.isVisible())
            {
                if (lastLoc != null && button.getLocation() != lastLoc)
                {
                    id = 0;
                }

                lastLoc = button.getLocation();
                button.setCurrentID(id);
                ++id;
            }
        }
    }

    public ModuleBase.RAILDIRECTION getSpecialRailDirection(int x, int y, int z)
    {
        return ModuleBase.RAILDIRECTION.DEFAULT;
    }

    protected void loadButtons() {}

    public final void addButton(ButtonBase button)
    {
        button.setIdInModule(this.moduleButtonId++);
        this.buttons.add(button);
    }

    public String generateNBTName(String name, int id)
    {
        return "module" + id + name;
    }

    public final void writeToNBT(NBTTagCompound tagCompound, int id)
    {
        if (this.getInventorySize() > 0)
        {
            NBTTagList items = new NBTTagList();

            for (int i = 0; i < this.getInventorySize(); ++i)
            {
                if (this.getStack(i) != null)
                {
                    NBTTagCompound item = new NBTTagCompound();
                    item.setByte("Slot", (byte)i);
                    this.getStack(i).writeToNBT(item);
                    items.appendTag(item);
                }
            }

            tagCompound.setTag(this.generateNBTName("Items", id), items);
        }

        this.Save(tagCompound, id);
    }

    protected void Save(NBTTagCompound tagCompound, int id) {}

    public final void readFromNBT(NBTTagCompound tagCompound, int id)
    {
        if (this.getInventorySize() > 0)
        {
            NBTTagList items = tagCompound.getTagList(this.generateNBTName("Items", id), NBTHelper.COMPOUND.getId());

            for (int i = 0; i < items.tagCount(); ++i)
            {
                NBTTagCompound item = items.getCompoundTagAt(i);
                int slot = item.getByte("Slot") & 255;

                if (slot >= 0 && slot < this.getInventorySize())
                {
                    this.setStack(slot, ItemStack.loadItemStackFromNBT(item));
                }
            }
        }

        this.Load(tagCompound, id);
    }

    protected void Load(NBTTagCompound tagCompound, int id) {}

    @SideOnly(Side.CLIENT)
    public final void drawButtonText(GuiMinecart gui)
    {
        Iterator i$ = this.buttons.iterator();

        while (i$.hasNext())
        {
            ButtonBase button = (ButtonBase)i$.next();
            button.drawButtonText(gui, this);
        }
    }

    @SideOnly(Side.CLIENT)
    public final void drawButtons(GuiMinecart gui, int x, int y)
    {
        Iterator i$ = this.buttons.iterator();

        while (i$.hasNext())
        {
            ButtonBase button = (ButtonBase)i$.next();
            button.drawButton(gui, this, x, y);
        }
    }

    @SideOnly(Side.CLIENT)
    public final void drawButtonOverlays(GuiMinecart gui, int x, int y)
    {
        Iterator i$ = this.buttons.iterator();

        while (i$.hasNext())
        {
            ButtonBase button = (ButtonBase)i$.next();

            if (button.isVisible())
            {
                this.drawStringOnMouseOver(gui, button.toString(), x, y, button.getBounds());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public final void mouseClickedButton(GuiMinecart gui, int x, int y, int mousebutton)
    {
        Iterator i$ = this.buttons.iterator();

        while (i$.hasNext())
        {
            ButtonBase button = (ButtonBase)i$.next();

            if (this.inRect(x, y, button.getBounds()))
            {
                button.computeOnClick(gui, mousebutton);
            }
        }
    }

    public void sendButtonPacket(ButtonBase button, byte clickinfo)
    {
        byte id = (byte)button.getIdInModule();
        System.out.println("Sent button " + button.getIdInModule());
        this.sendPacket(this.totalNumberOfPackets() - 1, new byte[] {id, clickinfo});
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y) {}

    @SideOnly(Side.CLIENT)
    public void drawBackgroundItems(GuiMinecart gui, int x, int y) {}

    @SideOnly(Side.CLIENT)
    public void mouseClicked(GuiMinecart gui, int x, int y, int button) {}

    @SideOnly(Side.CLIENT)
    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button) {}

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y) {}

    protected boolean inRect(int x, int y, int x1, int y1, int sizeX, int sizeY)
    {
        return this.inRect(x, y, new int[] {x1, y1, sizeX, sizeY});
    }

    public boolean inRect(int x, int y, int[] rect)
    {
        if (rect.length < 4)
        {
            return false;
        }
        else
        {
            rect = this.cloneRect(rect);

            if (!this.doStealInterface())
            {
                this.handleScroll(rect);
            }

            return x >= rect[0] && x <= rect[0] + rect[2] && y >= rect[1] && y <= rect[1] + rect[3];
        }
    }

    public boolean receiveDamage()
    {
        return true;
    }

    protected void turnback()
    {
        if(this.getCart().returningHome) return;
        Iterator i$ = this.getCart().getModules().iterator();
        ModuleBase module;

        do
        {
            if (!i$.hasNext())
            {
                this.getCart().turnback();
                return;
            }

            module = (ModuleBase)i$.next();
        }
        while (module == this || !module.preventTurnback());
    }

    protected boolean preventTurnback()
    {
        return false;
    }

    public final int totalNumberOfPackets()
    {
        return this.numberOfPackets() + (this.useButtons() ? 1 : 0);
    }

    protected int numberOfPackets()
    {
        return 0;
    }

    public int getPacketStart()
    {
        return this.packetOffset;
    }

    public void setPacketStart(int val)
    {
        this.packetOffset = val;
    }

    protected void sendPacket(int id)
    {
        this.sendPacket(id, new byte[0]);
    }

    public void sendPacket(int id, byte data)
    {
        this.sendPacket(id, new byte[] {data});
    }

    public void sendPacket(int id, byte[] data)
    {
        PacketHandler.sendPacket(this.getPacketStart() + id, data);
    }

    protected void sendPacket(int id, EntityPlayer player)
    {
        this.sendPacket(id, new byte[0], player);
    }

    protected void sendPacket(int id, byte data, EntityPlayer player)
    {
        this.sendPacket(id, new byte[] {data}, player);
    }

    protected void sendPacket(int id, byte[] data, EntityPlayer player)
    {
        PacketHandler.sendPacketToPlayer(this.getPacketStart() + id, data, player, this.getCart());
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player) {}

    public final void delegateReceivedPacket(int id, byte[] data, EntityPlayer player)
    {
        if (id >= 0 && id < this.totalNumberOfPackets())
        {
            if (id == this.totalNumberOfPackets() - 1 && this.useButtons())
            {
                int buttonId = data[0];

                if (buttonId < 0)
                {
                    buttonId += 256;
                }

                System.out.println("Received button " + buttonId);
                Iterator i$ = this.buttons.iterator();

                while (i$.hasNext())
                {
                    ButtonBase button = (ButtonBase)i$.next();

                    if (button.getIdInModule() == buttonId)
                    {
                        byte buttoninformation = data[1];
                        boolean isCtrlDown = (buttoninformation & 64) != 0;
                        boolean isShiftDown = (buttoninformation & 128) != 0;
                        int mousebutton = buttoninformation & 63;

                        if (button.isVisible() && button.isEnabled())
                        {
                            button.onServerClick(player, mousebutton, isCtrlDown, isShiftDown);
                        }

                        break;
                    }
                }
            }
            else
            {
                this.receivePacket(id, data, player);
            }
        }
    }

    public int numberOfDataWatchers()
    {
        return 0;
    }

    public int getDataWatcherStart()
    {
        return this.dataWatcherOffset;
    }

    public void setDataWatcherStart(int val)
    {
        this.dataWatcherOffset = val;
    }

    public void initDw() {}

    private int getDwId(int id)
    {
        id += 2 + this.getDataWatcherStart();

        if (id >= 16)
        {
            id += 7;
        }

        return id;
    }

    protected final void addIntDw(int id, int val)
    {
        this.getCart().getDataWatcher().addObject(this.getDwId(id), Integer.valueOf(val));
    }

    protected final void updateIntDw(int id, int val)
    {
        this.getCart().getDataWatcher().updateObject(this.getDwId(id), Integer.valueOf(val));
    }

    protected final int getIntDw(int id)
    {
        return this.getCart().getDataWatcher().getWatchableObjectInt(this.getDwId(id));
    }

    protected final void addShortDw(int id, int val)
    {
        this.getCart().getDataWatcher().addObject(this.getDwId(id), Short.valueOf((short)val));
    }

    protected final void updateShortDw(int id, int val)
    {
        this.getCart().getDataWatcher().updateObject(this.getDwId(id), Short.valueOf((short)val));
    }

    protected final short getShortDw(int id)
    {
        return this.getCart().getDataWatcher().getWatchableObjectShort(this.getDwId(id));
    }

    protected final void addDw(int id, int val)
    {
        this.getCart().getDataWatcher().addObject(this.getDwId(id), Byte.valueOf((byte)val));
    }

    protected final void updateDw(int id, int val)
    {
        this.getCart().getDataWatcher().updateObject(this.getDwId(id), Byte.valueOf((byte)val));
    }

    protected final byte getDw(int id)
    {
        return (byte)this.getCart().getDataWatcher().getWatchableObjectByte(this.getDwId(id));
    }

    protected final void addStringDw(int id, String val)
    {
        this.getCart().getDataWatcher().addObject(this.getDwId(id), val);
    }

    protected final void updateStringDw(int id, String val)
    {
        this.getCart().getDataWatcher().updateObject(this.getDwId(id), val);
    }

    protected final String getStringDw(int id)
    {
        return this.getCart().getDataWatcher().getWatchableObjectString(this.getDwId(id));
    }

    public int numberOfGuiData()
    {
        return 0;
    }

    public int getGuiDataStart()
    {
        return this.guiDataOffset;
    }

    public void setGuiDataStart(int val)
    {
        this.guiDataOffset = val;
    }

    private final void updateGuiData(Container con, List players, int id, short data)
    {
        Iterator iterator = players.iterator();

        while (iterator.hasNext())
        {
            ICrafting player = (ICrafting)iterator.next();
            player.sendProgressBarUpdate(con, id, data);
        }
    }

    public void updateGuiData(Object[] info, int id, short data)
    {
        ContainerMinecart con = (ContainerMinecart)info[0];

        if (con != null)
        {
            int globalId = id + this.getGuiDataStart();
            List players = (List)info[1];
            boolean isNew = ((Boolean)info[2]).booleanValue();
            boolean flag = isNew;

            if (!isNew)
            {
                if (con.cache != null)
                {
                    Short val = (Short)con.cache.get(Short.valueOf((short)globalId));

                    if (val != null)
                    {
                        flag = val.shortValue() != data;
                    }
                    else
                    {
                        flag = true;
                    }
                }
                else
                {
                    flag = true;
                }
            }

            if (flag)
            {
                if (con.cache == null)
                {
                    con.cache = new HashMap();
                }

                this.updateGuiData(con, players, globalId, data);
                con.cache.put(Short.valueOf((short)globalId), Short.valueOf(data));
            }
        }
    }

    public final void initGuiData(Container con, ICrafting player)
    {
        ArrayList players = new ArrayList();
        players.add(player);
        this.checkGuiData(con, players, true);
    }

    protected void checkGuiData(Object[] info) {}

    public final void checkGuiData(Container con, List players, boolean isNew)
    {
        if (con != null)
        {
            this.checkGuiData(new Object[] {con, players, Boolean.valueOf(isNew)});
        }
    }

    public void receiveGuiData(int id, short data) {}

    public int getConsumption(boolean isMoving)
    {
        return 0;
    }

    public void setModels(ArrayList<ModelCartbase> models)
    {
        this.models = models;
    }

    public ArrayList<ModelCartbase> getModels()
    {
        return this.models;
    }

    public boolean haveModels()
    {
        return this.models != null;
    }

    @SideOnly(Side.CLIENT)
    public final void drawStringOnMouseOver(GuiMinecart gui, String str, int x, int y, int x1, int y1, int w, int h)
    {
        this.drawStringOnMouseOver(gui, str, x, y, new int[] {x1, y1, w, h});
    }

    @SideOnly(Side.CLIENT)
    public final void drawStringOnMouseOver(GuiMinecart gui, String str, int x, int y, int[] rect)
    {
        if (this.inRect(x, y, rect))
        {
            x += this.getX();
            y += this.getY();
            gui.drawMouseOver(str, x, y);
        }
    }

    protected void drawImage(int[] rect, int sourceX, int sourceY)
    {
        this.drawImage(rect[0], rect[1], sourceX, sourceY, rect[2], rect[3]);
    }

    protected void drawImage(int targetX, int targetY, int sourceX, int sourceY, int width, int height)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV((double)(targetX + 0), (double)(targetY + height), -90.0D, (double)((float)(sourceX + 0) * var7), (double)((float)(sourceY + height) * var8));
        tess.addVertexWithUV((double)(targetX + width), (double)(targetY + height), -90.0D, (double)((float)(sourceX + width) * var7), (double)((float)(sourceY + height) * var8));
        tess.addVertexWithUV((double)(targetX + width), (double)(targetY + 0), -90.0D, (double)((float)(sourceX + width) * var7), (double)((float)(sourceY + 0) * var8));
        tess.addVertexWithUV((double)(targetX + 0), (double)(targetY + 0), -90.0D, (double)((float)(sourceX + 0) * var7), (double)((float)(sourceY + 0) * var8));
        tess.draw();
    }

    @SideOnly(Side.CLIENT)
    protected EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft() != null ? Minecraft.getMinecraft().thePlayer : null;
    }

    @SideOnly(Side.CLIENT)
    public void renderOverlay(Minecraft minecraft) {}

    public boolean stopEngines()
    {
        return false;
    }

    public boolean shouldCartRender()
    {
        return true;
    }

    public double getPushFactor()
    {
        return -1.0D;
    }

    public float[] getColor()
    {
        return new float[] {1.0F, 1.0F, 1.0F};
    }

    public float mountedOffset(Entity rider)
    {
        return 0.0F;
    }

    protected boolean countsAsAir(int x, int y, int z)
    {
        if (this.getCart().worldObj.isAirBlock(x, y, z))
        {
            return true;
        }
        else
        {
            Block b = this.getCart().worldObj.getBlock(x, y, z);
            return b == null || b.getMaterial().isReplaceable();
        }
    }

    public void activatedByRail(int x, int y, int z, boolean active) {}

    public ModuleData getData()
    {
        return (ModuleData)ModuleData.getList().get(Byte.valueOf(this.getModuleId()));
    }

    public boolean doStealInterface()
    {
        return false;
    }

    public boolean hasExtraData()
    {
        return false;
    }

    public byte getExtraData()
    {
        return (byte)0;
    }

    public void setExtraData(byte b) {}

    protected FakePlayer getFakePlayer()
    {
        return FakePlayerFactory.getMinecraft((WorldServer)this.getCart().worldObj);
    }

    public boolean disableStandardKeyFunctionality()
    {
        return false;
    }

    public void addToLabel(ArrayList<String> label) {}

    public boolean onInteractFirst(EntityPlayer entityplayer)
    {
        return false;
    }

    public void postUpdate() {}

    public String getModuleName()
    {
        return ((ModuleData)ModuleData.getList().get(Byte.valueOf(this.getModuleId()))).getName();
    }

    public static enum RAILDIRECTION
    {
        DEFAULT("DEFAULT", 0),
        NORTH("NORTH", 1),
        WEST("WEST", 2),
        SOUTH("SOUTH", 3),
        EAST("EAST", 4),
        LEFT("LEFT", 5),
        FORWARD("FORWARD", 6),
        RIGHT("RIGHT", 7);

        private static final ModuleBase.RAILDIRECTION[] $VALUES = new ModuleBase.RAILDIRECTION[]{DEFAULT, NORTH, WEST, SOUTH, EAST, LEFT, FORWARD, RIGHT};

        private RAILDIRECTION(String var1, int var2) {}
    }
}
