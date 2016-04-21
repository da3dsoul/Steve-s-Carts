package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ModuleCountPair;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;

@SideOnly(Side.CLIENT)
public class GuiMinecart extends GuiBase
{
    private static ResourceLocation textureLeft = ResourceHelper.getResource("/gui/guiBase1.png");
    private static ResourceLocation textureRight = ResourceHelper.getResource("/gui/guiBase2.png");
    public static ResourceLocation moduleTexture = ResourceHelper.getResourceFromPath("/atlas/items.png");
    private boolean isScrolling;
    private int[] scrollBox = new int[] {450, 15, 18, 225};
    private MinecartModular cart;

    public GuiMinecart(InventoryPlayer invPlayer, MinecartModular cart)
    {
        super(cart.getCon(invPlayer));
        this.setup(cart);
    }

    protected void setup(MinecartModular cart)
    {
        this.cart = cart;
        this.setXSize(478);
        this.setYSize(256);
    }

    public void drawGuiForeground(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        if (this.cart.getModules() != null)
        {
            ModuleBase thief = this.cart.getInterfaceThief();

            if (thief != null)
            {
                this.drawModuleForeground(thief);
                this.drawModuleMouseOver(thief, x, y);
            }
            else
            {
                Iterator i$ = this.cart.getModules().iterator();
                ModuleBase module;

                while (i$.hasNext())
                {
                    module = (ModuleBase)i$.next();
                    this.drawModuleForeground(module);
                }

                this.renderModuleListText(x, y);
                i$ = this.cart.getModules().iterator();

                while (i$.hasNext())
                {
                    module = (ModuleBase)i$.next();
                    this.drawModuleMouseOver(module, x, y);
                }

                this.renderModuleListMouseOver(x, y);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public void drawGuiBackground(float f, int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        ResourceHelper.bindResource(textureLeft);
        this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize);
        ResourceHelper.bindResource(textureRight);
        this.drawTexturedModalRect(j + 256, k, 0, 0, this.xSize - 256, this.ySize);
        ModuleBase thief = this.cart.getInterfaceThief();
        Iterator i$;
        ModuleBase module;

        if (thief != null)
        {
            this.drawModuleSlots(thief);
            this.drawModuleBackground(thief, x, y);
            this.drawModuleBackgroundItems(thief, x, y);
            i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                module = (ModuleBase)i$.next();

                if (module.hasGui() && module.hasSlots())
                {
                    ArrayList slotsList = module.getSlots();
                    Iterator i$1 = slotsList.iterator();

                    while (i$1.hasNext())
                    {
                        SlotBase slot = (SlotBase)i$1.next();
                        this.resetSlot(slot);
                    }
                }
            }
        }
        else if (this.cart.getModules() != null)
        {
            this.drawTexturedModalRect(j + this.scrollBox[0], k + this.scrollBox[1], 222, 24, this.scrollBox[2], this.scrollBox[3]);
            this.drawTexturedModalRect(j + this.scrollBox[0] + 2, k + this.scrollBox[1] + 2 + this.cart.getScrollY(), 240, 26 + (this.cart.canScrollModules ? 0 : 25), 14, 25);
            i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                module = (ModuleBase)i$.next();
                this.drawModuleSlots(module);
            }

            i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                module = (ModuleBase)i$.next();
                this.drawModuleBackground(module, x, y);
            }

            this.renderModuleList(x, y);
            i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                module = (ModuleBase)i$.next();
                this.drawModuleBackgroundItems(module, x, y);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderModuleList(int x, int y)
    {
        x -= this.getGuiLeft();
        y -= this.getGuiTop();
        ArrayList moduleCounts = this.cart.getModuleCounts();
        ResourceHelper.bindResource(moduleTexture);
        GL11.glEnable(GL11.GL_BLEND);

        for (int i = 0; i < moduleCounts.size(); ++i)
        {
            ModuleCountPair count = (ModuleCountPair)moduleCounts.get(i);
            float alpha = this.inRect(x, y, this.getModuleDisplayX(i), this.getModuleDisplayY(i), 16, 16) ? 1.0F : 0.5F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            this.drawIcon(count.getData().getIcon(), this.getGuiLeft() + this.getModuleDisplayX(i), this.getGuiTop() + this.getModuleDisplayY(i), 1.0F, 1.0F, 0.0F, 0.0F);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderModuleListText(int x, int y)
    {
        x -= this.getGuiLeft();
        y -= this.getGuiTop();
        ArrayList moduleCounts = this.cart.getModuleCounts();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getFontRenderer().drawString(this.cart.getCartName(), 5, 172, 4210752);
        GL11.glEnable(GL11.GL_BLEND);

        for (int i = 0; i < moduleCounts.size(); ++i)
        {
            ModuleCountPair count = (ModuleCountPair)moduleCounts.get(i);

            if (count.getCount() != 1)
            {
                int alpha = (int)((this.inRect(x, y, this.getModuleDisplayX(i), this.getModuleDisplayY(i), 16, 16) ? 1.0F : 0.75F) * 256.0F);
                String str = String.valueOf(count.getCount());
                this.getFontRenderer().drawStringWithShadow(str, this.getModuleDisplayX(i) + 16 - this.getFontRenderer().getStringWidth(str), this.getModuleDisplayY(i) + 8, 16777215 | alpha << 24);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderModuleListMouseOver(int x, int y)
    {
        x -= this.getGuiLeft();
        y -= this.getGuiTop();
        ArrayList moduleCounts = this.cart.getModuleCounts();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < moduleCounts.size(); ++i)
        {
            ModuleCountPair count = (ModuleCountPair)moduleCounts.get(i);

            if (this.inRect(x, y, this.getModuleDisplayX(i), this.getModuleDisplayY(i), 16, 16))
            {
                this.drawMouseOver(count.toString(), x, y);
            }
        }
    }

    private int getModuleDisplayX(int id)
    {
        return id % 8 * 18 + 7;
    }

    private int getModuleDisplayY(int id)
    {
        return id / 8 * 18 + 182;
    }

    public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);
        ModuleBase thief = this.cart.getInterfaceThief();

        if (thief != null)
        {
            this.handleModuleMouseClicked(thief, x, y, button);
        }
        else if (this.cart.getModules() != null)
        {
            if (this.inRect(x - this.getGuiLeft(), y - this.getGuiTop(), this.scrollBox[0], this.scrollBox[1], this.scrollBox[2], this.scrollBox[3]))
            {
                this.isScrolling = true;
            }

            Iterator i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                this.handleModuleMouseClicked(module, x, y, button);
            }
        }
    }

    protected boolean inRect(int x, int y, int x1, int y1, int sizeX, int sizeY)
    {
        return x >= x1 && x <= x1 + sizeX && y >= y1 && y <= y1 + sizeY;
    }

    public void mouseMoved(int x, int y, int button)
    {
        super.mouseMoved(x, y, button);

        if (this.isScrolling)
        {
            int thief = y - this.getGuiTop() - 12 - (this.scrollBox[1] + 2);

            if (thief < 0)
            {
                thief = 0;
            }
            else if (thief > 198)
            {
                thief = 198;
            }

            this.cart.setScrollY(thief);
        }

        if (button != -1)
        {
            this.isScrolling = false;
        }

        if (this.cart.getModules() != null)
        {
            ModuleBase thief1 = this.cart.getInterfaceThief();

            if (thief1 != null)
            {
                this.handleModuleMouseMoved(thief1, x, y, button);
            }
            else
            {
                Iterator i$ = this.cart.getModules().iterator();

                while (i$.hasNext())
                {
                    ModuleBase module = (ModuleBase)i$.next();
                    this.handleModuleMouseMoved(module, x, y, button);
                }
            }
        }
    }

    public void keyPress(char character, int extraInformation)
    {
        super.keyPress(character, extraInformation);

        if (this.cart.getModules() != null)
        {
            ModuleBase thief = this.cart.getInterfaceThief();

            if (thief != null)
            {
                this.handleModuleKeyPress(thief, character, extraInformation);
            }
            else
            {
                Iterator i$ = this.cart.getModules().iterator();

                while (i$.hasNext())
                {
                    ModuleBase module = (ModuleBase)i$.next();
                    this.handleModuleKeyPress(module, character, extraInformation);
                }
            }
        }
    }

    public boolean disableStandardKeyFunctionality()
    {
        if (this.cart.getModules() != null)
        {
            ModuleBase thief = this.cart.getInterfaceThief();

            if (thief != null)
            {
                return thief.disableStandardKeyFunctionality();
            }

            Iterator i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();

                if (module.disableStandardKeyFunctionality())
                {
                    return true;
                }
            }
        }

        return false;
    }

    private void drawModuleForeground(ModuleBase module)
    {
        if (module.hasGui())
        {
            module.drawForeground(this);

            if (module.useButtons())
            {
                module.drawButtonText(this);
            }
        }
    }

    private void drawModuleMouseOver(ModuleBase module, int x, int y)
    {
        if (module.hasGui())
        {
            module.drawMouseOver(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY());

            if (module.useButtons())
            {
                module.drawButtonOverlays(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY());
            }
        }
    }

    private void drawModuleSlots(ModuleBase module)
    {
        if (module.hasGui() && module.hasSlots())
        {
            ArrayList slotsList = module.getSlots();
            Iterator i$ = slotsList.iterator();

            while (i$.hasNext())
            {
                SlotBase slot = (SlotBase)i$.next();
                int[] rect = new int[] {slot.getX() + 1, slot.getY() + 1, 16, 16};
                module.handleScroll(rect);
                boolean drawAll = rect[3] == 16;

                if (drawAll)
                {
                    slot.xDisplayPosition = slot.getX() + module.getX() + 1;
                    slot.yDisplayPosition = slot.getY() + module.getY() + 1 - this.cart.getRealScrollY();
                }
                else
                {
                    this.resetSlot(slot);
                }

                module.drawImage(this, slot.getX(), slot.getY(), this.xSize - 256, 0, 18, 18);

                if (!drawAll)
                {
                    module.drawImage(this, slot.getX() + 1, slot.getY() + 1, this.xSize - 256 + 18, 1, 16, 16);
                }
            }
        }
    }

    private void resetSlot(SlotBase slot)
    {
        slot.xDisplayPosition = -9001;
        slot.yDisplayPosition = -9001;
    }

    private void drawModuleBackground(ModuleBase module, int x, int y)
    {
        if (module.hasGui())
        {
            module.drawBackground(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY());

            if (module.useButtons())
            {
                module.drawButtons(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY());
            }
        }
    }

    private void drawModuleBackgroundItems(ModuleBase module, int x, int y)
    {
        if (module.hasGui())
        {
            module.drawBackgroundItems(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY());
        }
    }

    private void handleModuleMouseClicked(ModuleBase module, int x, int y, int button)
    {
        module.mouseClicked(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY(), button);

        if (module.useButtons())
        {
            module.mouseClickedButton(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY(), button);
        }
    }

    private void handleModuleMouseMoved(ModuleBase module, int x, int y, int button)
    {
        module.mouseMovedOrUp(this, x - this.getGuiLeft() - module.getX(), y - this.getGuiTop() - module.getY(), button);
    }

    private void handleModuleKeyPress(ModuleBase module, char character, int extraInformation)
    {
        module.keyPress(this, character, extraInformation);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int k = Mouse.getEventDWheel();
        if (k != 0) {

            int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

            if (k < 0) {
                k = -1;
            }
            if(k > 0)
            {
                k = 1;
            }

            if (this.inRect(x - this.getGuiLeft(), y - this.getGuiTop(), 0,0,xSize,ySize))
            {
                int moduleSize = this.cart.modularSpaceHeight;
                // int realscroll = (int)((float)(moduleSize - 168) / 198.0F * (float)this.cart.getScrollY());
                // 36 = (moduleSize - 168) / 198 * scrollY
                // 36 * 198 / (moduleSize - 168)

                int scroll = this.cart.getScrollY() + (-k * 7524) / (moduleSize - 168);
                scroll = MathHelper.clamp_int(scroll,0,198);
                this.cart.setScrollY(scroll);
            }

        }
    }
}
