package vswe.stevescarts.Helpers;

import net.minecraft.item.ItemStack;

public class CargoItemSelection
{
    private Class validSlot;
    private ItemStack icon;
    private Localization.GUI.CARGO name;

    public CargoItemSelection(Localization.GUI.CARGO name, Class validSlot, ItemStack icon)
    {
        this.name = name;
        this.validSlot = validSlot;
        this.icon = icon;
    }

    public Class getValidSlot()
    {
        return this.validSlot;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public String getName()
    {
        return this.name == null ? null : this.name.translate(new String[0]);
    }
}
