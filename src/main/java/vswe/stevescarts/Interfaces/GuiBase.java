package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class GuiBase extends GuiNEIKiller
{
    private int myOwnEventButton = 0;
    private long myOwnTimeyWhineyThingy = 0L;
    private int myOwnTouchpadTimeWhineyThingy = 0;

    public GuiBase(Container container)
    {
        super(container);
    }

    public void drawMouseOver(String str, int x, int y)
    {
        ArrayList text = new ArrayList();
        String[] split = str.split("\n");

        for (int i = 0; i < split.length; ++i)
        {
            text.add(split[i]);
        }

        this.drawMouseOver((List)text, x, y);
    }

    public boolean inRect(int x, int y, int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    public void drawMouseOver(List text, int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int var5 = 0;
        Iterator var6 = text.iterator();
        int var16;

        while (var6.hasNext())
        {
            String var15 = (String)var6.next();
            var16 = this.getFontRenderer().getStringWidth(var15);

            if (var16 > var5)
            {
                var5 = var16;
            }
        }

        int var141 = x + 10;
        var16 = y;
        int var9 = 8;

        if (text.size() > 1)
        {
            var9 += 2 + (text.size() - 1) * 10;
        }

        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int var10 = -267386864;
        this.drawGradientRect(var141 - 3, y - 4, var141 + var5 + 3, y - 3, var10, var10);
        this.drawGradientRect(var141 - 3, y + var9 + 3, var141 + var5 + 3, y + var9 + 4, var10, var10);
        this.drawGradientRect(var141 - 3, y - 3, var141 + var5 + 3, y + var9 + 3, var10, var10);
        this.drawGradientRect(var141 - 4, y - 3, var141 - 3, y + var9 + 3, var10, var10);
        this.drawGradientRect(var141 + var5 + 3, y - 3, var141 + var5 + 4, y + var9 + 3, var10, var10);
        int var11 = 1347420415;
        int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
        this.drawGradientRect(var141 - 3, y - 3 + 1, var141 - 3 + 1, y + var9 + 3 - 1, var11, var12);
        this.drawGradientRect(var141 + var5 + 2, y - 3 + 1, var141 + var5 + 3, y + var9 + 3 - 1, var11, var12);
        this.drawGradientRect(var141 - 3, y - 3, var141 + var5 + 3, y - 3 + 1, var11, var11);
        this.drawGradientRect(var141 - 3, y + var9 + 2, var141 + var5 + 3, y + var9 + 3, var12, var12);

        for (int var13 = 0; var13 < text.size(); ++var13)
        {
            String var14 = (String)text.get(var13);
            this.getFontRenderer().drawStringWithShadow(var14, var141, var16, -1);

            if (var13 == 0)
            {
                var16 += 2;
            }

            var16 += 10;
        }

        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public Minecraft getMinecraft()
    {
        return this.mc;
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRendererObj;
    }

    public void setXSize(int val)
    {
        this.xSize = val;
        this.guiLeft = (this.width - this.xSize) / 2;
    }

    public void setYSize(int val)
    {
        this.ySize = val;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    public int getXSize()
    {
        return this.xSize;
    }

    public int getYSize()
    {
        return this.ySize;
    }

    public int getGuiLeft()
    {
        return this.guiLeft;
    }

    public int getGuiTop()
    {
        return this.guiTop;
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        this.drawGuiForeground(x, y);
    }

    public void drawGuiForeground(int x, int y) {}

    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
        this.startScaling();
    }

    private int scaleX(float x)
    {
        float scale = this.getScale();
        x /= scale;
        x += (float)this.getGuiLeft();
        x -= ((float)this.width - (float)this.xSize * scale) / (2.0F * scale);
        return (int)x;
    }

    private int scaleY(float y)
    {
        float scale = this.getScale();
        y /= scale;
        y += (float)this.getGuiTop();
        y -= ((float)this.height - (float)this.ySize * scale) / (2.0F * scale);
        return (int)y;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float f)
    {
        super.drawScreen(this.scaleX((float)x), this.scaleY((float)y), f);
        this.stopScaling();
    }

    protected float getScale()
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        float w = (float)scaledresolution.getScaledWidth() * 0.9F;
        float h = (float)scaledresolution.getScaledHeight() * 0.9F;
        float multX = w / (float)this.getXSize();
        float multY = h / (float)this.getYSize();
        float mult = Math.min(multX, multY);

        if (mult > 1.0F)
        {
            mult = 1.0F;
        }

        return mult;
    }

    private void startScaling()
    {
        GL11.glPushMatrix();
        float scale = this.getScale();
        GL11.glScalef(scale, scale, 1.0F);
        GL11.glTranslatef((float)(-this.guiLeft), (float)(-this.guiTop), 0.0F);
        GL11.glTranslatef(((float)this.width - (float)this.xSize * scale) / (2.0F * scale), ((float)this.height - (float)this.ySize * scale) / (2.0F * scale), 0.0F);
    }

    private void stopScaling()
    {
        GL11.glPopMatrix();
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        this.drawGuiBackground(f, x, y);
    }

    public void drawGuiBackground(float f, int x, int y) {}

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int x, int y, int button)
    {
        x = this.scaleX((float)x);
        y = this.scaleY((float)y);
        super.mouseClicked(x, y, button);
        this.mouseClick(x, y, button);
    }

    public void mouseClick(int x, int y, int button) {}

    protected void mouseMovedOrUp(int x, int y, int button)
    {
        x = this.scaleX((float)x);
        y = this.scaleY((float)y);
        super.mouseMovedOrUp(x, y, button);
        this.mouseMoved(x, y, button);
        this.mouseDraged(x, y, button);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        if (Mouse.getEventButtonState())
        {
            if (this.mc.gameSettings.touchscreen && this.myOwnTouchpadTimeWhineyThingy++ > 0)
            {
                return;
            }

            this.myOwnEventButton = Mouse.getEventButton();
            this.myOwnTimeyWhineyThingy = Minecraft.getSystemTime();
            this.mouseClicked(i, j, this.myOwnEventButton);
        }
        else if (Mouse.getEventButton() != -1)
        {
            if (this.mc.gameSettings.touchscreen && --this.myOwnTouchpadTimeWhineyThingy > 0)
            {
                return;
            }

            this.myOwnEventButton = -1;
            this.mouseMovedOrUp(i, j, Mouse.getEventButton());
        }
        else if (this.myOwnEventButton != -1 && this.myOwnTimeyWhineyThingy > 0L)
        {
            long k = Minecraft.getSystemTime() - this.myOwnTimeyWhineyThingy;
            this.mouseClickMove(i, j, this.myOwnEventButton, k);
        }
        else
        {
            this.mouseMovedOrUp(i, j, -1);
        }
    }

    protected void mouseClickMove(int x, int y, int button, long timeSinceClick)
    {
        x = this.scaleX((float)x);
        y = this.scaleY((float)y);
        super.mouseClickMove(x, y, button, timeSinceClick);
        this.mouseMoved(x, y, -1);
        this.mouseDraged(x, y, button);
    }

    public void mouseMoved(int x, int y, int button) {}

    public void mouseDraged(int x, int y, int button) {}

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char character, int extraInformation)
    {
        if (extraInformation == 1 || !this.disableStandardKeyFunctionality())
        {
            super.keyTyped(character, extraInformation);
        }

        this.keyPress(character, extraInformation);
    }

    public boolean disableStandardKeyFunctionality()
    {
        return false;
    }

    public void keyPress(char character, int extraInformation) {}

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public void enableKeyRepeat(boolean val)
    {
        Keyboard.enableRepeatEvents(val);
    }

    public float getZLevel()
    {
        return this.zLevel;
    }

    public void drawIcon(IIcon icon, int targetX, int targetY, float sizeX, float sizeY, float offsetX, float offsetY)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float x = icon.getMinU() + offsetX * (icon.getMaxU() - icon.getMinU());
        float y = icon.getMinV() + offsetY * (icon.getMaxV() - icon.getMinV());
        float width = (icon.getMaxU() - icon.getMinU()) * sizeX;
        float height = (icon.getMaxV() - icon.getMinV()) * sizeY;
        tessellator.addVertexWithUV((double)(targetX + 0), (double)((float)targetY + 16.0F * sizeY), (double)this.getZLevel(), (double)(x + 0.0F), (double)(y + height));
        tessellator.addVertexWithUV((double)((float)targetX + 16.0F * sizeX), (double)((float)targetY + 16.0F * sizeY), (double)this.getZLevel(), (double)(x + width), (double)(y + height));
        tessellator.addVertexWithUV((double)((float)targetX + 16.0F * sizeX), (double)(targetY + 0), (double)this.getZLevel(), (double)(x + width), (double)(y + 0.0F));
        tessellator.addVertexWithUV((double)(targetX + 0), (double)(targetY + 0), (double)this.getZLevel(), (double)(x + 0.0F), (double)(y + 0.0F));
        tessellator.draw();
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h, GuiBase.RENDER_ROTATION rotation)
    {
        float fw = 0.00390625F;
        float fy = 0.00390625F;
        double a = (double)((float)(u + 0) * fw);
        double b = (double)((float)(u + w) * fw);
        double c = (double)((float)(v + h) * fy);
        double d = (double)((float)(v + 0) * fy);
        double[] ptA = new double[] {a, c};
        double[] ptB = new double[] {b, c};
        double[] ptC = new double[] {b, d};
        double[] ptD = new double[] {a, d};
        double[] pt1;
        double[] pt2;
        double[] pt3;
        double[] pt4;

        switch (GuiBase.NamelessClass815138093.$SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[rotation.ordinal()])
        {
            case 1:
            default:
                pt1 = ptA;
                pt2 = ptB;
                pt3 = ptC;
                pt4 = ptD;
                break;

            case 2:
                pt1 = ptB;
                pt2 = ptC;
                pt3 = ptD;
                pt4 = ptA;
                break;

            case 3:
                pt1 = ptC;
                pt2 = ptD;
                pt3 = ptA;
                pt4 = ptB;
                break;

            case 4:
                pt1 = ptD;
                pt2 = ptA;
                pt3 = ptB;
                pt4 = ptC;
                break;

            case 5:
                pt1 = ptB;
                pt2 = ptA;
                pt3 = ptD;
                pt4 = ptC;
                break;

            case 6:
                pt1 = ptA;
                pt2 = ptD;
                pt3 = ptC;
                pt4 = ptB;
                break;

            case 7:
                pt1 = ptD;
                pt2 = ptC;
                pt3 = ptB;
                pt4 = ptA;
                break;

            case 8:
                pt1 = ptC;
                pt2 = ptB;
                pt3 = ptA;
                pt4 = ptD;
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, pt1[0], pt1[1]);
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, pt2[0], pt2[1]);
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, pt3[0], pt3[1]);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, pt4[0], pt4[1]);
        tessellator.draw();
    }

    static class NamelessClass815138093
    {
        static final int[] $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION = new int[GuiBase.RENDER_ROTATION.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.NORMAL.ordinal()] = 1;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.ROTATE_90.ordinal()] = 2;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.ROTATE_180.ordinal()] = 3;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.ROTATE_270.ordinal()] = 4;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.FLIP_HORIZONTAL.ordinal()] = 5;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.ROTATE_90_FLIP.ordinal()] = 6;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.FLIP_VERTICAL.ordinal()] = 7;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[GuiBase.RENDER_ROTATION.ROTATE_270_FLIP.ordinal()] = 8;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    public static enum RENDER_ROTATION
    {
        NORMAL("NORMAL", 0),
        ROTATE_90("ROTATE_90", 1),
        ROTATE_180("ROTATE_180", 2),
        ROTATE_270("ROTATE_270", 3),
        FLIP_HORIZONTAL("FLIP_HORIZONTAL", 4),
        ROTATE_90_FLIP("ROTATE_90_FLIP", 5),
        FLIP_VERTICAL("FLIP_VERTICAL", 6),
        ROTATE_270_FLIP("ROTATE_270_FLIP", 7);

        private static final GuiBase.RENDER_ROTATION[] $VALUES = new GuiBase.RENDER_ROTATION[]{NORMAL, ROTATE_90, ROTATE_180, ROTATE_270, FLIP_HORIZONTAL, ROTATE_90_FLIP, FLIP_VERTICAL, ROTATE_270_FLIP};

        private RENDER_ROTATION(String var1, int var2) {}

        public GuiBase.RENDER_ROTATION getNextRotation()
        {
            switch (GuiBase.NamelessClass815138093.$SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[this.ordinal()])
            {
                case 1:
                default:
                    return ROTATE_90;

                case 2:
                    return ROTATE_180;

                case 3:
                    return ROTATE_270;

                case 4:
                    return NORMAL;

                case 5:
                    return ROTATE_90_FLIP;

                case 6:
                    return FLIP_VERTICAL;

                case 7:
                    return ROTATE_270_FLIP;

                case 8:
                    return FLIP_HORIZONTAL;
            }
        }

        public GuiBase.RENDER_ROTATION getFlippedRotation()
        {
            switch (GuiBase.NamelessClass815138093.$SwitchMap$vswe$stevescarts$Interfaces$GuiBase$RENDER_ROTATION[this.ordinal()])
            {
                case 1:
                default:
                    return FLIP_HORIZONTAL;

                case 2:
                    return ROTATE_90_FLIP;

                case 3:
                    return FLIP_VERTICAL;

                case 4:
                    return ROTATE_270_FLIP;

                case 5:
                    return NORMAL;

                case 6:
                    return ROTATE_90;

                case 7:
                    return ROTATE_180;

                case 8:
                    return ROTATE_270;
            }
        }
    }
}
