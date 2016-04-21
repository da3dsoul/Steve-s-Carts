package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.InterfaceEffect;
import vswe.stevescarts.Upgrades.InventoryEffect;

@SideOnly(Side.CLIENT)
public class GuiUpgrade extends GuiBase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/gui/upgrade.png");
    private TileEntityUpgrade upgrade;
    private InventoryPlayer invPlayer;

    public GuiUpgrade(InventoryPlayer invPlayer, TileEntityUpgrade upgrade)
    {
        super(new ContainerUpgrade(invPlayer, upgrade));
        this.upgrade = upgrade;
        this.invPlayer = invPlayer;
        this.setXSize(256);
        this.setYSize(190);
    }

    public void drawGuiForeground(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        if (this.upgrade.getUpgrade() != null)
        {
            this.getFontRenderer().drawString(this.upgrade.getUpgrade().getName(), 8, 6, 4210752);
            InterfaceEffect gui = this.upgrade.getUpgrade().getInterfaceEffect();

            if (gui != null)
            {
                gui.drawForeground(this.upgrade, this);
                gui.drawMouseOver(this.upgrade, this, x, y);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public void drawGuiBackground(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        ResourceHelper.bindResource(texture);
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        if (this.upgrade.getUpgrade() != null)
        {
            InventoryEffect inventory = this.upgrade.getUpgrade().getInventoryEffect();

            if (inventory != null)
            {
                for (int gui = 0; gui < inventory.getInventorySize(); ++gui)
                {
                    this.drawTexturedModalRect(j + inventory.getSlotX(gui) - 1, k + inventory.getSlotY(gui) - 1, 0, this.ySize, 18, 18);
                }
            }

            InterfaceEffect var8 = this.upgrade.getUpgrade().getInterfaceEffect();

            if (var8 != null)
            {
                var8.drawBackground(this.upgrade, this, x, y);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
