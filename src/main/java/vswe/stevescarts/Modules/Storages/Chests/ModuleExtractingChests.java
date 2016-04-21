package vswe.stevescarts.Modules.Storages.Chests;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleExtractingChests extends ModuleChest
{
    private final float startOffset = -14.0F;
    private final float endOffset = -24.5F;
    private float chestOffset = -14.0F;

    public ModuleExtractingChests(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 18;
    }

    protected int getInventoryHeight()
    {
        return 4;
    }

    protected float chestFullyOpenAngle()
    {
        return ((float)Math.PI / 2F);
    }

    protected void handleChest()
    {
        if (this.isChestActive() && this.lidClosed() && this.chestOffset > this.endOffset)
        {
            this.chestOffset -= 0.5F;
        }
        else if (!this.isChestActive() && this.lidClosed() && this.chestOffset < this.startOffset)
        {
            this.chestOffset += 0.5F;
        }
        else
        {
            super.handleChest();
        }
    }

    public float getChestOffset()
    {
        return this.chestOffset;
    }
}
