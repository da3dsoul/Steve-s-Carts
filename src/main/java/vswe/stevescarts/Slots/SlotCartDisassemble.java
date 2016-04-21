package vswe.stevescarts.Slots;

import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;
import vswe.stevescarts.Upgrades.Disassemble;

public class SlotCartDisassemble extends SlotCart
{
    public SlotCartDisassemble(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        if (this.inventory instanceof TileEntityUpgrade)
        {
            TileEntityUpgrade upgrade = (TileEntityUpgrade)this.inventory;

            if (upgrade.getUpgrade() != null)
            {
                Iterator i$ = upgrade.getUpgrade().getEffects().iterator();

                while (i$.hasNext())
                {
                    BaseEffect effect = (BaseEffect)i$.next();

                    if (effect instanceof Disassemble)
                    {
                        return ((Disassemble)effect).canDisassemble(upgrade) == 2 && super.isItemValid(itemstack);
                    }
                }
            }
        }

        return false;
    }
}
