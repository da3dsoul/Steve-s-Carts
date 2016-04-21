package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Storages.ModuleStorage;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotChest;

public abstract class ModuleChest extends ModuleStorage
{
    private float chestAngle;

    public ModuleChest(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();
        this.handleChest();
    }

    public boolean hasGui()
    {
        return true;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotChest(this.getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int guiWidth()
    {
        return 15 + this.getInventoryWidth() * 18;
    }

    public int guiHeight()
    {
        return 20 + this.getInventoryHeight() * 18;
    }

    public float getChestAngle()
    {
        return this.chestAngle;
    }

    protected boolean lidClosed()
    {
        return this.chestAngle <= 0.0F;
    }

    protected float getLidSpeed()
    {
        return 0.15707964F;
    }

    protected float chestFullyOpenAngle()
    {
        return 1.3744469F;
    }

    protected boolean hasVisualChest()
    {
        return true;
    }

    protected boolean playChestSound()
    {
        return this.hasVisualChest();
    }

    public int numberOfDataWatchers()
    {
        return this.hasVisualChest() ? 1 : 0;
    }

    public void initDw()
    {
        if (this.hasVisualChest())
        {
            this.addDw(0, 0);
        }
    }

    public void openChest()
    {
        if (this.hasVisualChest())
        {
            this.updateDw(0, this.getDw(0) + 1);
        }
    }

    public void closeChest()
    {
        if (this.hasVisualChest())
        {
            this.updateDw(0, this.getDw(0) - 1);
        }
    }

    protected boolean isChestActive()
    {
        return this.hasVisualChest() ? (this.isPlaceholder() ? this.getSimInfo().getChestActive() : this.getDw(0) > 0) : false;
    }

    protected void handleChest()
    {
        if (this.hasVisualChest())
        {
            if (this.isChestActive() && this.lidClosed() && this.playChestSound())
            {
                this.getCart().worldObj.playSoundEffect(this.getCart().posX, this.getCart().posY, this.getCart().posZ, "random.chestopen", 0.5F, this.getCart().worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.isChestActive() && this.chestAngle < this.chestFullyOpenAngle())
            {
                this.chestAngle += this.getLidSpeed();

                if (this.chestAngle > this.chestFullyOpenAngle())
                {
                    this.chestAngle = this.chestFullyOpenAngle();
                }
            }
            else if (!this.isChestActive() && !this.lidClosed())
            {
                float lastAngle = this.chestAngle;
                this.chestAngle -= this.getLidSpeed();

                if ((double)this.chestAngle < 1.1780972450961724D && (double)lastAngle >= 1.1780972450961724D && this.playChestSound())
                {
                    this.getCart().worldObj.playSoundEffect(this.getCart().posX, this.getCart().posY, this.getCart().posZ, "random.chestclosed", 0.5F, this.getCart().worldObj.rand.nextFloat() * 0.1F + 0.9F);
                }

                if (this.chestAngle < 0.0F)
                {
                    this.chestAngle = 0.0F;
                }
            }
        }
    }

    public boolean isCompletelyFilled()
    {
        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            if (this.getStack(i) == null)
            {
                return false;
            }
        }

        return true;
    }

    public boolean isCompletelyEmpty()
    {
        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            if (this.getStack(i) != null)
            {
                return false;
            }
        }

        return true;
    }
}
