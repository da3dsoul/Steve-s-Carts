package vswe.stevescarts.Helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vswe.stevescarts.Interfaces.GuiBase;

public interface ITankHolder
{
    ItemStack getInputContainer(int var1);

    void clearInputContainer(int var1);

    void addToOutputContainer(int var1, ItemStack var2);

    void onFluidUpdated(int var1);

    @SideOnly(Side.CLIENT)
    void drawImage(int var1, GuiBase var2, IIcon var3, int var4, int var5, int var6, int var7, int var8, int var9);
}
