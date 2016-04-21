package vswe.stevescarts.Modules.Storages.Chests;

import java.util.Random;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.Localization;

public class ModuleEggBasket extends ModuleChest
{
    public ModuleEggBasket(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 6;
    }

    protected int getInventoryHeight()
    {
        return 4;
    }

    protected boolean playChestSound()
    {
        return false;
    }

    protected float getLidSpeed()
    {
        return 0.02094395F;
    }

    protected float chestFullyOpenAngle()
    {
        return 0.3926991F;
    }

    public byte getExtraData()
    {
        return (byte)0;
    }

    public boolean hasExtraData()
    {
        return true;
    }

    public void setExtraData(byte b)
    {
        if (b != 0)
        {
            Random rand = this.getCart().rand;
            int eggs = 1 + rand.nextInt(4) + rand.nextInt(4);
            ItemStack easterEgg = ComponentTypes.PAINTED_EASTER_EGG.getItemStack(eggs);
            this.setStack(0, easterEgg);
        }
    }

    public String getModuleInfoText(byte b)
    {
        return b == 0 ? Localization.MODULE_INFO.STORAGE_EMPTY.translate(new String[0]) : Localization.MODULE_INFO.EGG_STORAGE_FULL.translate(new String[0]);
    }
    public String getCartInfoText(String name, byte b)
    {
        return b == 0 ? Localization.MODULE_INFO.STORAGE_EMPTY.translate(new String[0]) + " " + name : Localization.MODULE_INFO.STORAGE_FULL.translate(new String[0]) + " " + name;
    }
}
