package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;

public class SlotArrow extends SlotBase
{
    private ModuleShooter shooter;

    public SlotArrow(IInventory iinventory, ModuleShooter shooter, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.shooter = shooter;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        return this.shooter.isValidProjectileItem(itemstack);
    }
}
