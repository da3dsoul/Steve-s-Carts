package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.Containers.ContainerActivator;
import vswe.stevescarts.Helpers.ActivatorOption;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityActivator;

@SideOnly(Side.CLIENT)
public class GuiActivator extends GuiBase
{
    private static ResourceLocation texture = ResourceHelper.getResource("/gui/activator.png");
    TileEntityActivator activator;
    InventoryPlayer invPlayer;

    public GuiActivator(InventoryPlayer invPlayer, TileEntityActivator activator)
    {
        super(new ContainerActivator(invPlayer, activator));
        this.invPlayer = invPlayer;
        this.setXSize(255);
        this.setYSize(222);
        this.activator = activator;
    }

    public void drawGuiForeground(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.getFontRenderer().drawString(Localization.GUI.TOGGLER.TITLE.translate(new String[0]), 8, 6, 4210752);
        int i;
        ActivatorOption option;
        int[] box;

        for (i = 0; i < this.activator.getOptions().size(); ++i)
        {
            option = (ActivatorOption)this.activator.getOptions().get(i);
            box = this.getBoxRect(i);
            this.getFontRenderer().drawString(option.getName(), box[0] + box[2] + 6, box[1] + 4, 4210752);
        }

        for (i = 0; i < this.activator.getOptions().size(); ++i)
        {
            option = (ActivatorOption)this.activator.getOptions().get(i);
            box = this.getBoxRect(i);
            this.drawMouseMover(option.getInfo(), x, y, box);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void drawMouseMover(String str, int x, int y, int[] rect)
    {
        if (this.inRect(x - this.getGuiLeft(), y - this.getGuiTop(), rect))
        {
            this.drawMouseOver(str, x - this.getGuiLeft(), y - this.getGuiTop());
        }
    }

    public void drawGuiBackground(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        ResourceHelper.bindResource(texture);
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();

        for (int i = 0; i < this.activator.getOptions().size(); ++i)
        {
            ActivatorOption option = (ActivatorOption)this.activator.getOptions().get(i);
            int[] box = this.getBoxRect(i);
            byte srcX = 0;

            if (this.inRect(x, y, box))
            {
                srcX = 16;
            }

            this.drawTexturedModalRect(j + box[0], k + box[1], srcX, this.ySize, box[2], box[3]);
            this.drawTexturedModalRect(j + box[0] + 1, k + box[1] + 1, (box[2] - 2) * option.getOption(), this.ySize + box[3], box[2] - 2, box[3] - 2);
        }
    }

    private int[] getBoxRect(int i)
    {
        return new int[] {20, 22 + i * 20, 16, 16};
    }

    public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();

        for (int i = 0; i < this.activator.getOptions().size(); ++i)
        {
            int[] box = this.getBoxRect(i);

            if (this.inRect(x, y, box))
            {
                byte data = (byte)(button == 0 ? 0 : 1);
                data = (byte)(data | i << 1);
                PacketHandler.sendPacket(0, new byte[] {data});
            }
        }
    }
}
