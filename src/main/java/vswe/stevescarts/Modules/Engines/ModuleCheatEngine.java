package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ModuleCheatEngine extends ModuleEngine
{
    public ModuleCheatEngine(MinecartModular cart)
    {
        super(cart);
    }

    public void loadFuel() {}

    public int getFuelLevel()
    {
        return 9001;
    }

    public void setFuelLevel(int val) {}

    public void drawForeground(GuiMinecart gui)
    {
        String[] split = this.getModuleName().split(" ");
        this.drawString(gui, split[0], 8, 6, 4210752);

        if (split.length > 1)
        {
            this.drawString(gui, split[1], 8, 16, 4210752);
        }

        this.drawString(gui, Localization.MODULES.ENGINES.OVER_9000.translate(new String[] {String.valueOf(this.getFuelLevel())}), 8, 42, 4210752);
    }

    public int getTotalFuel()
    {
        return 9001000;
    }

    public float[] getGuiBarColor()
    {
        return new float[] {0.97F, 0.58F, 0.11F};
    }

    public boolean hasSlots()
    {
        return false;
    }
}
