package vswe.stevescarts.Upgrades;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.ICrafting;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Interfaces.GuiUpgrade;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public abstract class InterfaceEffect extends BaseEffect
{
    @SideOnly(Side.CLIENT)
    public void drawForeground(TileEntityUpgrade upgrade, GuiUpgrade gui) {}

    @SideOnly(Side.CLIENT)
    public void drawBackground(TileEntityUpgrade upgrade, GuiUpgrade gui, int x, int y) {}

    @SideOnly(Side.CLIENT)
    public void drawMouseOver(TileEntityUpgrade upgrade, GuiUpgrade gui, int x, int y) {}

    public void checkGuiData(TileEntityUpgrade upgrade, ContainerUpgrade con, ICrafting crafting, boolean isNew) {}

    public void receiveGuiData(TileEntityUpgrade upgrade, int id, short data) {}

    protected void drawMouseOver(GuiUpgrade gui, String str, int x, int y, int[] rect)
    {
        if (gui.inRect(x - gui.getGuiLeft(), y - gui.getGuiTop(), rect))
        {
            gui.drawMouseOver(str, x - gui.getGuiLeft(), y - gui.getGuiTop());
        }
    }
}
