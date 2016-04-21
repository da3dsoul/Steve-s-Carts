package vswe.stevescarts.Upgrades;

public abstract class SimpleInventoryEffect extends InventoryEffect
{
    private int inventoryWidth;
    private int inventoryHeight;

    public SimpleInventoryEffect(int inventoryWidth, int inventoryHeight)
    {
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
    }

    public int getInventorySize()
    {
        return this.inventoryWidth * this.inventoryHeight;
    }

    public int getSlotX(int id)
    {
        return (256 - 18 * this.inventoryWidth) / 2 + id % this.inventoryWidth * 18;
    }

    public int getSlotY(int id)
    {
        return (107 - 18 * this.inventoryHeight) / 2 + id / this.inventoryWidth * 18;
    }
}
