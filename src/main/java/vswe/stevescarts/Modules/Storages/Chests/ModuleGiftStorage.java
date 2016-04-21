package vswe.stevescarts.Modules.Storages.Chests;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.GiftItem;
import vswe.stevescarts.Helpers.Localization;

public class ModuleGiftStorage extends ModuleChest
{
    public ModuleGiftStorage(MinecartModular cart)
    {
        super(cart);
    }

    protected int getInventoryWidth()
    {
        return 9;
    }

    protected int getInventoryHeight()
    {
        return 4;
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
            ArrayList items = GiftItem.generateItems(this.getCart().rand, GiftItem.ChristmasList, 50 + this.getCart().rand.nextInt(700), 1 + this.getCart().rand.nextInt(5));

            for (int i = 0; i < items.size(); ++i)
            {
                this.setStack(i, (ItemStack)items.get(i));
            }
        }
    }

    public String getModuleInfoText(byte b)
    {
        return b == 0 ? Localization.MODULE_INFO.STORAGE_EMPTY.translate(new String[0]) : Localization.MODULE_INFO.GIFT_STORAGE_FULL.translate(new String[0]);
    }
    public String getCartInfoText(String name, byte b)
    {
        return b == 0 ? Localization.MODULE_INFO.STORAGE_EMPTY.translate(new String[0]) + " " + name : Localization.MODULE_INFO.STORAGE_FULL.translate(new String[0]) + " " + name;
    }
}
