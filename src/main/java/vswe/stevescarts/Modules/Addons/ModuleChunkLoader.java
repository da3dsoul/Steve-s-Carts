package vswe.stevescarts.Modules.Addons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;

public class ModuleChunkLoader extends ModuleAddon implements IActivatorModule
{
    private boolean rdyToInit;
    private int[] buttonRect = new int[] {20, 20, 24, 12};

    public ModuleChunkLoader(MinecartModular cart)
    {
        super(cart);
    }

    public boolean hasSlots()
    {
        return false;
    }

    public boolean hasGui()
    {
        return true;
    }

    public int guiWidth()
    {
        return 80;
    }

    public int guiHeight()
    {
        return 35;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, "Chunk Loader", 8, 6, 4210752);
    }

    public void update()
    {
        super.update();

        if (!this.rdyToInit)
        {
            this.rdyToInit = true;
        }

        if (this.isLoadingChunk() && !this.getCart().hasFuelForModule() && !this.getCart().worldObj.isRemote)
        {
            this.setChunkLoading(false);
        }
    }

    public void setChunkLoading(boolean val)
    {
        if (!this.isPlaceholder())
        {
            this.updateDw(0, (byte)(val ? 1 : 0));

            if (!this.getCart().worldObj.isRemote && this.rdyToInit)
            {
                if (val)
                {
                    this.getCart().initChunkLoading();
                }
                else
                {
                    this.getCart().dropChunkLoading();
                }
            }
        }
    }

    private boolean isLoadingChunk()
    {
        return this.isPlaceholder() ? false : this.getDw(0) != 0;
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/chunk.png");
        int imageID = this.isLoadingChunk() ? 1 : 0;
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
        this.drawStringOnMouseOver(gui, this.getStateName(), x, y, this.buttonRect);
    }

    private String getStateName()
    {
        return !this.isLoadingChunk() ? "Activate chunk loading" : "Deactivate chunk loading";
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
            this.setChunkLoading(!this.isLoadingChunk());
        }
    }

    public int numberOfPackets()
    {
        return 1;
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public int getConsumption(boolean isMoving)
    {
        return this.isLoadingChunk() ? 5 : super.getConsumption(isMoving);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setBoolean(this.generateNBTName("ChunkLoading", id), this.isLoadingChunk());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setChunkLoading(tagCompound.getBoolean(this.generateNBTName("ChunkLoading", id)));
    }

    public void doActivate(int id)
    {
        this.setChunkLoading(true);
    }

    public void doDeActivate(int id)
    {
        this.setChunkLoading(false);
    }

    public boolean isActive(int id)
    {
        return this.isLoadingChunk();
    }
}
