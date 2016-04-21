package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityManager;

@SideOnly(Side.CLIENT)
public abstract class GuiManager extends GuiBase
{
    private TileEntityManager manager;
    private InventoryPlayer invPlayer;

    public GuiManager(InventoryPlayer invPlayer, TileEntityManager manager, ContainerManager container)
    {
        super(container);
        this.manager = manager;
        this.invPlayer = invPlayer;
    }

    public void drawGuiForeground(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        int[] coords = this.getMiddleCoords();
        this.getFontRenderer().drawString(this.getManagerName(), coords[0] - 34, 65, 4210752);
        this.getFontRenderer().drawString(Localization.GUI.MANAGER.TITLE.translate(new String[0]), coords[0] + coords[2], 65, 4210752);
        int i;

        for (i = 0; i < 4; ++i)
        {
            coords = this.getTextCoords(i);
            String str = this.getMaxSizeText(i);
            this.getFontRenderer().drawString(str, coords[0], coords[1], 4210752);
        }

        for (i = 0; i < 4; ++i)
        {
            this.drawExtraOverlay(i, x, y);
            this.drawMouseOver(Localization.GUI.MANAGER.CHANGE_TRANSFER_DIRECTION.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + (this.manager.toCart[i] ? Localization.GUI.MANAGER.DIRECTION_TO_CART.translate(new String[0]) : Localization.GUI.MANAGER.DIRECTION_FROM_CART.translate(new String[0])), x, y, this.getArrowCoords(i));
            this.drawMouseOver(Localization.GUI.MANAGER.CHANGE_TURN_BACK_SETTING.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + (this.manager.color[i] == 5 ? Localization.GUI.MANAGER.TURN_BACK_NOT_SELECTED.translate(new String[0]) : (this.manager.doReturn[this.manager.color[i] - 1] ? Localization.GUI.MANAGER.TURN_BACK_DO.translate(new String[0]) : Localization.GUI.MANAGER.TURN_BACK_DO_NOT.translate(new String[0]))), x, y, this.getReturnCoords(i));
            this.drawMouseOver(Localization.GUI.MANAGER.CHANGE_TRANSFER_SIZE.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + this.getMaxSizeOverlay(i), x, y, this.getTextCoords(i));
            this.drawMouseOver(Localization.GUI.MANAGER.CHANGE_SIDE.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SIDE.translate(new String[0]) + ": " + (new String[] {Localization.GUI.MANAGER.SIDE_RED.translate(new String[0]), Localization.GUI.MANAGER.SIDE_BLUE.translate(new String[0]), Localization.GUI.MANAGER.SIDE_YELLOW.translate(new String[0]), Localization.GUI.MANAGER.SIDE_GREEN.translate(new String[0]), Localization.GUI.MANAGER.SIDE_DISABLED.translate(new String[0])})[this.manager.color[i] - 1], x, y, this.getColorpickerCoords(i));
        }

        this.drawMouseOver(this.getLayoutString() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + this.getLayoutOption(this.manager.layoutType), x, y, this.getMiddleCoords());
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    protected void drawMouseOver(String str, int x, int y, int[] rect)
    {
        if (this.inRect(x - this.getGuiLeft(), y - this.getGuiTop(), rect))
        {
            this.drawMouseOver(str, x - this.getGuiLeft(), y - this.getGuiTop());
        }
    }

    public void drawGuiBackground(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int left = this.getGuiLeft();
        int top = this.getGuiTop();
        this.drawBackground(left, top);

        for (int renderitem = 0; renderitem < 4; ++renderitem)
        {
            this.drawArrow(renderitem, left, top);
            int coords = this.manager.color[renderitem] - 1;

            if (coords != 4)
            {
                this.drawColors(renderitem, coords, left, top);
            }
        }

        RenderItem var9 = new RenderItem();
        int[] var10 = this.getMiddleCoords();
        var9.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(this.getBlock(), 1), left + var10[0], top + var10[1]);

        for (int i = 0; i < 4; ++i)
        {
            this.drawItems(i, var9, left, top);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawArrow(int id, int left, int top)
    {
        int sourceX = this.getArrowSourceX();
        byte sourceY = 28;
        int sourceY1 = sourceY + 56 * id;

        if (!this.manager.toCart[id])
        {
            sourceX += 28;
        }

        int targetX = this.getArrowCoords(id)[0];
        int targetY = this.getArrowCoords(id)[1];
        byte sizeX = 28;
        byte sizeY = 28;
        this.drawTexturedModalRect(left + targetX, top + targetY, sourceX, sourceY1, sizeX, sizeY);

        if (id == this.manager.getLastSetting() && this.manager.color[id] != 5)
        {
            sourceY1 -= 28;
            int scaledProgress = this.manager.moveProgressScaled(42);
            int offsetX = 0;
            int offsetY = 0;
            int sizeX1;
            int sizeY1;

            if (this.manager.toCart[id])
            {
                sizeX1 = 14;

                if (id % 2 == 0)
                {
                    offsetX = 14;
                }

                sizeY1 = scaledProgress;

                if (scaledProgress > 19)
                {
                    sizeY1 = 19;
                }

                if (id < 2)
                {
                    offsetY = 28 - sizeY1;
                }
            }
            else
            {
                sizeY1 = 14;

                if (id >= 2)
                {
                    offsetY = 14;
                }

                sizeX1 = scaledProgress;

                if (scaledProgress > 19)
                {
                    sizeX1 = 19;
                }

                if (id % 2 == 1)
                {
                    offsetX = 28 - sizeX1;
                }
            }

            this.drawTexturedModalRect(left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY1 + offsetY, sizeX1, sizeY1);
            offsetY = 0;
            offsetX = 0;
            sizeY1 = 28;
            sizeX1 = 28;

            if (scaledProgress > 19)
            {
                scaledProgress -= 19;

                if (this.manager.toCart[id])
                {
                    sizeX1 = scaledProgress;

                    if (scaledProgress > 23)
                    {
                        sizeX1 = 23;
                    }

                    if (id % 2 == 0)
                    {
                        offsetX = 22 - sizeX1;
                    }
                    else
                    {
                        offsetX = 6;
                    }
                }
                else
                {
                    sizeY1 = scaledProgress;

                    if (scaledProgress > 23)
                    {
                        sizeY1 = 23;
                    }

                    if (id >= 2)
                    {
                        offsetY = 22 - sizeY1;
                    }
                    else
                    {
                        offsetY = 6;
                    }
                }

                this.drawTexturedModalRect(left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY1 + offsetY, sizeX1, sizeY1);
            }
        }
    }

    protected void drawColors(int id, int color, int left, int top)
    {
        int[] coords = this.getReturnCoords(id);
        this.drawTexturedModalRect(left + coords[0], top + coords[1], this.getColorSourceX() + (this.manager.doReturn[this.manager.color[id] - 1] ? 8 : 0), 80 + 8 * color, 8, 8);
        coords = this.getBoxCoords(id);
        this.drawTexturedModalRect(left + coords[0] - 2, top + coords[1] - 2, this.getColorSourceX(), 20 * color, 20, 20);
    }

    protected int[] getMiddleCoords()
    {
        return new int[] {this.getCenterTargetX() + 45, 61, 20, 20};
    }

    protected int[] getBoxCoords(int id)
    {
        int x = id % 2;
        int y = id / 2;
        int xCoord = this.getCenterTargetX() + 4 + x * 82;
        int yCoord = 17 + y * 88;
        yCoord += this.offsetObjectY(this.manager.layoutType, x, y);
        return new int[] {xCoord, yCoord, 20, 20};
    }

    protected int[] getArrowCoords(int id)
    {
        int x = id % 2;
        int y = id / 2;
        int xCoord = this.getCenterTargetX() + 25 + x * 28;
        int yCoord = 17 + y * 76;
        yCoord += this.offsetObjectY(this.manager.layoutType, x, y);
        return new int[] {xCoord, yCoord, 28, 28};
    }

    protected int[] getTextCoords(int id)
    {
        int[] coords = this.getBoxCoords(id);
        int xCoord = coords[0];
        int yCoord = coords[1];

        if (id >= 2)
        {
            yCoord -= 12;
        }
        else
        {
            yCoord += 20;
        }

        return new int[] {xCoord, yCoord, 20, 10};
    }

    protected int[] getColorpickerCoords(int id)
    {
        int x = id % 2;
        int y = id / 2;
        int xCoord = this.getCenterTargetX() + 3 + x * 92;
        int yCoord = 49 + y * 32;
        yCoord += this.offsetObjectY(this.manager.layoutType, x, y);
        return new int[] {xCoord, yCoord, 8, 8};
    }

    protected int[] getReturnCoords(int id)
    {
        int x = id % 2;
        int y = id / 2;
        int xCoord = this.getCenterTargetX() + 14 + x * 70;
        int yCoord = 49 + y * 32;
        yCoord += this.offsetObjectY(this.manager.layoutType, x, y);
        return new int[] {xCoord, yCoord, 8, 8};
    }

    public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);

        if (button == 0 || button == 1)
        {
            x -= this.getGuiLeft();
            y -= this.getGuiTop();

            if (this.inRect(x, y, this.getMiddleCoords()))
            {
                this.manager.sendPacket(5, (byte)(button == 0 ? 1 : -1));
            }
            else
            {
                for (int i = 0; i < 4; ++i)
                {
                    byte data = (byte)i;
                    data = (byte)(data | button << 2);

                    if (this.inRect(x, y, this.getArrowCoords(i)))
                    {
                        this.manager.sendPacket(0, (byte)i);
                        break;
                    }

                    if (this.inRect(x, y, this.getTextCoords(i)))
                    {
                        this.manager.sendPacket(2, data);
                        break;
                    }

                    if (this.inRect(x, y, this.getColorpickerCoords(i)))
                    {
                        this.manager.sendPacket(3, data);
                        break;
                    }

                    if (this.inRect(x, y, this.getReturnCoords(i)))
                    {
                        this.manager.sendPacket(4, (byte)i);
                        break;
                    }

                    if (this.sendOnClick(i, x, y, data))
                    {
                        break;
                    }
                }
            }
        }
    }

    protected void drawExtraOverlay(int id, int x, int y) {}

    protected boolean sendOnClick(int id, int x, int y, byte data)
    {
        return false;
    }

    protected int offsetObjectY(int layout, int x, int y)
    {
        return 0;
    }

    protected void drawItems(int id, RenderItem renderitem, int left, int top) {}

    protected abstract String getMaxSizeOverlay(int var1);

    protected abstract String getMaxSizeText(int var1);

    protected abstract void drawBackground(int var1, int var2);

    protected abstract int getArrowSourceX();

    protected abstract int getColorSourceX();

    protected abstract int getCenterTargetX();

    protected abstract Block getBlock();

    protected abstract String getManagerName();

    protected abstract String getLayoutOption(int var1);

    protected abstract String getLayoutString();

    protected TileEntityManager getManager()
    {
        return this.manager;
    }
}
