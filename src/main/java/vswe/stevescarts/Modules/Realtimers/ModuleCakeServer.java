package vswe.stevescarts.Modules.Realtimers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCake;

public class ModuleCakeServer extends ModuleBase implements ISuppliesModule
{
    private int cooldown = 0;
    private static final int MAX_CAKES = 10;
    private static final int SLICES_PER_CAKE = 6;
    private static final int MAX_TOTAL_SLICES = 66;
    private int[] rect = new int[] {40, 20, 13, 36};

    public ModuleCakeServer(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            if (this.getCart().hasCreativeSupplies())
            {
                if (this.cooldown >= 20)
                {
                    if (this.getCakeBuffer() < 66)
                    {
                        this.setCakeBuffer(this.getCakeBuffer() + 1);
                    }

                    this.cooldown = 0;
                }
                else
                {
                    ++this.cooldown;
                }
            }

            ItemStack item = this.getStack(0);

            if (item != null && item.getItem().equals(Items.cake) && this.getCakeBuffer() + 6 <= 66)
            {
                this.setCakeBuffer(this.getCakeBuffer() + 6);
                this.setStack(0, (ItemStack)null);
            }
        }
    }

    private void setCakeBuffer(int i)
    {
        this.updateShortDw(0, i);
    }

    private int getCakeBuffer()
    {
        return this.isPlaceholder() ? 6 : this.getShortDw(0);
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addShortDw(0, 0);
    }

    public boolean hasGui()
    {
        return true;
    }

    protected int getInventoryWidth()
    {
        return 1;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotCake(this.getCart(), slotId, 8 + x * 18, 38 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.CAKE_SERVER.translate(new String[0]), 8, 6, 4210752);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.generateNBTName("Cake", id), (short)this.getCakeBuffer());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setCakeBuffer(tagCompound.getShort(this.generateNBTName("Cake", id)));
    }

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CAKES.translate(new String[] {String.valueOf(this.getCakes()), String.valueOf(10)}) + "\n" + Localization.MODULES.ATTACHMENTS.SLICES.translate(new String[] {String.valueOf(this.getSlices()), String.valueOf(6)}), x, y, this.rect);
    }

    private int getCakes()
    {
        return this.getCakeBuffer() == 66 ? 10 : this.getCakeBuffer() / 6;
    }

    private int getSlices()
    {
        return this.getCakeBuffer() == 66 ? 6 : this.getCakeBuffer() % 6;
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/cake.png");
        this.drawImage(gui, this.rect, 0, this.inRect(x, y, this.rect) ? this.rect[3] : 0);
        int maxHeight = this.rect[3] - 2;
        int height = (int)((float)this.getCakes() / 10.0F * (float)maxHeight);

        if (height > 0)
        {
            this.drawImage(gui, this.rect[0] + 1, this.rect[1] + 1 + maxHeight - height, this.rect[2], maxHeight - height, 7, height);
        }

        height = (int)((float)this.getSlices() / 6.0F * (float)maxHeight);

        if (height > 0)
        {
            this.drawImage(gui, this.rect[0] + 9, this.rect[1] + 1 + maxHeight - height, this.rect[2] + 7, maxHeight - height, 3, height);
        }
    }

    public int guiWidth()
    {
        return 75;
    }

    public int guiHeight()
    {
        return 60;
    }

    public boolean onInteractFirst(EntityPlayer entityplayer)
    {
        if (this.getCakeBuffer() > 0)
        {
            if (!this.getCart().worldObj.isRemote && entityplayer.canEat(false))
            {
                this.setCakeBuffer(this.getCakeBuffer() - 1);
                entityplayer.getFoodStats().addStats(2, 0.1F);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int getRenderSliceCount()
    {
        int count = this.getSlices();

        if (count == 0 && this.getCakes() > 0)
        {
            count = 6;
        }

        return count;
    }

    public boolean haveSupplies()
    {
        return this.getCakeBuffer() > 0;
    }
}
