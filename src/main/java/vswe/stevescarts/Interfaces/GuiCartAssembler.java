package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerCartAssembler;
import vswe.stevescarts.Helpers.DropDownMenuItem;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Helpers.TitleBox;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleDataHull;
import vswe.stevescarts.Slots.SlotAssembler;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

@SideOnly(Side.CLIENT)
public class GuiCartAssembler extends GuiBase
{
    private ArrayList<GuiCartAssembler.TextWithColor> statusLog;
    private boolean hasErrors;
    private boolean firstLoad = true;
    private static ResourceLocation[] backgrounds = new ResourceLocation[4];
    private static final ResourceLocation textureLeft;
    private static final ResourceLocation textureRight;
    private static final ResourceLocation textureExtra;
    private int[] assembleRect = new int[] {390, 160, 80, 11};
    String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int dropdownX = -1;
    private int dropdownY = -1;
    private int scrollingX;
    private int scrollingY;
    private boolean isScrolling;
    private int[] blackBackground = new int[] {145, 15, 222, 148};
    private TileEntityCartAssembler assembler;
    private InventoryPlayer invPlayer;

    public GuiCartAssembler(InventoryPlayer invPlayer, TileEntityCartAssembler assembler)
    {
        super(new ContainerCartAssembler(invPlayer, assembler));
        this.assembler = assembler;
        this.invPlayer = invPlayer;
        this.setXSize(512);
        this.setYSize(256);
    }

    public void drawGuiForeground(int x, int y)
    {
        this.getFontRenderer().drawString(Localization.GUI.ASSEMBLER.TITLE.translate(new String[0]), 8, 6, 4210752);

        if (this.assembler.isErrorListOutdated)
        {
            this.updateErrorList();
            this.assembler.isErrorListOutdated = false;
        }

        ArrayList lines = this.statusLog;

        if (lines != null)
        {
            int lineCount = lines.size();
            boolean dotdotdot = false;

            if (lineCount > 11)
            {
                lineCount = 10;
                dotdotdot = true;
            }

            for (int i = 0; i < lineCount; ++i)
            {
                GuiCartAssembler.TextWithColor info = (GuiCartAssembler.TextWithColor)lines.get(i);

                if (info != null)
                {
                    this.getFontRenderer().drawString(info.getText(), 375, 40 + i * 10, info.getColor());
                }
            }

            if (dotdotdot)
            {
                this.getFontRenderer().drawString("...", 375, 40 + lineCount * 10, 4210752);
            }
        }
    }

    private void updateErrorList()
    {
        ArrayList lines = new ArrayList();

        if (this.assembler.getStackInSlot(0) == null)
        {
            this.addText(lines, Localization.GUI.ASSEMBLER.ASSEMBLE_INSTRUCTION.translate(new String[0]));
            this.hasErrors = true;
        }
        else
        {
            ModuleData hulldata = ModItems.modules.getModuleData(this.assembler.getStackInSlot(0));

            if (hulldata != null && hulldata instanceof ModuleDataHull)
            {
                ModuleDataHull hull = (ModuleDataHull)hulldata;
                this.addText(lines, Localization.GUI.ASSEMBLER.HULL_CAPACITY.translate(new String[0]) + ": " + hull.getCapacity());
                this.addText(lines, Localization.GUI.ASSEMBLER.COMPLEXITY_CAP.translate(new String[0]) + ": " + hull.getComplexityMax());
                this.addText(lines, Localization.GUI.ASSEMBLER.TOTAL_COST.translate(new String[0]) + ": " + this.assembler.getTotalCost());
                this.addText(lines, Localization.GUI.ASSEMBLER.TOTAl_TIME.translate(new String[0]) + ": " + this.formatTime((int)((float)this.assembler.generateAssemblingTime() / this.assembler.getEfficiency())));
                this.addNewLine(lines);
                ArrayList errors = this.assembler.getErrors();
                this.hasErrors = errors.size() > 0;

                if (errors.size() == 0)
                {
                    this.addText(lines, Localization.GUI.ASSEMBLER.NO_ERROR.translate(new String[0]), 22566);
                }
                else
                {
                    Iterator i$ = errors.iterator();

                    while (i$.hasNext())
                    {
                        String error = (String)i$.next();
                        this.addText(lines, error, 10357518);
                    }
                }
            }
            else
            {
                this.addText(lines, Localization.GUI.ASSEMBLER.INVALID_HULL.translate(new String[0]), 10357518);
                this.hasErrors = true;
            }
        }

        this.statusLog = lines;
    }

    private void addText(ArrayList<GuiCartAssembler.TextWithColor> lines, String text)
    {
        this.addText(lines, text, 4210752);
    }

    private void addText(ArrayList<GuiCartAssembler.TextWithColor> lines, String text, int color)
    {
        List newlines = this.getFontRenderer().listFormattedStringToWidth(text, 130);
        Iterator i$ = newlines.iterator();

        while (i$.hasNext())
        {
            Object line = i$.next();
            lines.add(new GuiCartAssembler.TextWithColor(line.toString(), color));
        }
    }

    private void addNewLine(ArrayList<GuiCartAssembler.TextWithColor> lines)
    {
        lines.add(null);
    }

    public void drawGuiBackground(float f, int x, int y)
    {
        if (this.firstLoad)
        {
            this.updateErrorList();
            this.firstLoad = false;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        ResourceHelper.bindResource(backgrounds[this.assembler.getSimulationInfo().getBackground()]);
        this.drawTexturedModalRect(j + 143, k + 15, 0, 0, 220, 148);
        ResourceHelper.bindResource(textureLeft);
        this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize);
        ResourceHelper.bindResource(textureRight);
        this.drawTexturedModalRect(j + 256, k, 0, 0, this.xSize - 256, this.ySize);
        this.drawTexturedModalRect(j + 256, k, 0, 0, this.xSize - 256, this.ySize);
        ResourceHelper.bindResource(textureExtra);
        ArrayList slots = this.assembler.getSlots();
        Iterator isDisassembling = slots.iterator();
        int srcY;
        int assemblingProgRect;

        while (isDisassembling.hasNext())
        {
            SlotAssembler srcX = (SlotAssembler)isDisassembling.next();
            srcY = srcX.getX() - 1;
            assemblingProgRect = srcX.getY() - 1;
            short fuelProgRect;
            byte assemblingProgress;
            byte assemblingInfo;

            if (srcX.useLargeInterface())
            {
                srcY -= 3;
                assemblingProgRect -= 3;
                assemblingInfo = 24;
                fuelProgRect = 0;
                assemblingProgress = 0;
            }
            else
            {
                assemblingInfo = 18;

                if (srcX.getStack() != null && srcX.getStack().stackSize <= 0)
                {
                    if (srcX.getStack().stackSize == TileEntityCartAssembler.getRemovedSize())
                    {
                        fuelProgRect = 140;
                    }
                    else
                    {
                        fuelProgRect = 122;
                    }

                    assemblingProgress = 40;
                }
                else
                {
                    fuelProgRect = 24;
                    assemblingProgress = 0;
                }
            }

            this.drawTexturedModalRect(j + srcY, k + assemblingProgRect, fuelProgRect, assemblingProgress, assemblingInfo, assemblingInfo);
            int animationTick = srcX.getAnimationTick();

            if (animationTick < 0)
            {
                animationTick = 0;
            }

            if (animationTick < 8 && !srcX.useLargeInterface())
            {
                this.drawTexturedModalRect(j + srcY + 1, k + assemblingProgRect + 1, 0, 24 + animationTick, 16, 8 - animationTick);
                this.drawTexturedModalRect(j + srcY + 1, k + assemblingProgRect + 1 + 8 + animationTick, 0, 32, 16, 8 - animationTick);
            }
        }

        isDisassembling = this.assembler.getTitleBoxes().iterator();

        while (isDisassembling.hasNext())
        {
            TitleBox srcX1 = (TitleBox)isDisassembling.next();
            srcY = srcX1.getY() - 12;
            assemblingProgRect = srcX1.getX();
            this.drawTexturedModalRect(j + assemblingProgRect, k + srcY, 0, 40, 115, 11);
            GL11.glColor4f((float)(srcX1.getColor() >> 16) / 255.0F, (float)(srcX1.getColor() >> 8 & 255) / 255.0F, (float)(srcX1.getColor() & 255) / 255.0F, 1.0F);
            this.drawTexturedModalRect(j + assemblingProgRect + 8, k + srcY + 2, 0, 51 + srcX1.getID() * 7, 115, 7);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        boolean isDisassembling1 = this.assembler.getIsDisassembling();
        short srcX2 = 42;
        srcY = 0;

        if (isDisassembling1)
        {
            srcX2 = 158;
            srcY = 40;
        }

        if (this.hasErrors)
        {
            srcY += 22;
        }
        else if (this.inRect(x - j, y - k, this.assembleRect))
        {
            srcY += 11;
        }

        this.drawTexturedModalRect(j + this.assembleRect[0], k + this.assembleRect[1], srcX2, srcY, this.assembleRect[2], this.assembleRect[3]);
        int[] assemblingProgRect1 = new int[] {375, 180, 115, 11};
        int[] fuelProgRect1 = new int[] {375, 200, 115, 11};
        float assemblingProgress1 = 0.0F;
        String assemblingInfo1;

        if (this.assembler.getIsAssembling())
        {
            assemblingProgress1 = (float)this.assembler.getAssemblingTime() / (float)this.assembler.getMaxAssemblingTime();
            assemblingInfo1 = Localization.GUI.ASSEMBLER.ASSEMBLE_PROGRESS.translate(new String[0]) + ": " + this.formatProgress(assemblingProgress1);
            assemblingInfo1 = assemblingInfo1 + "\n" + Localization.GUI.ASSEMBLER.TIME_LEFT.translate(new String[0]) + ": " + this.formatTime((int)((float)(this.assembler.getMaxAssemblingTime() - this.assembler.getAssemblingTime()) / this.assembler.getEfficiency()));
        }
        else
        {
            assemblingInfo1 = Localization.GUI.ASSEMBLER.IDLE_MESSAGE.translate(new String[0]);
        }

        this.drawProgressBar(assemblingProgRect1, assemblingProgress1, 22, x, y);
        this.drawProgressBar(fuelProgRect1, (float)this.assembler.getFuelLevel() / (float)this.assembler.getMaxFuelLevel(), 31, x, y);
        this.renderDropDownMenu(x, y);
        this.render3DCart();

        if (!this.hasErrors)
        {
            if (isDisassembling1)
            {
                this.drawProgressBarInfo(this.assembleRect, x, y, Localization.GUI.ASSEMBLER.MODIFY_CART.translate(new String[0]));
            }
            else
            {
                this.drawProgressBarInfo(this.assembleRect, x, y, Localization.GUI.ASSEMBLER.ASSEMBLE_CART.translate(new String[0]));
            }
        }

        this.drawProgressBarInfo(assemblingProgRect1, x, y, assemblingInfo1);
        this.drawProgressBarInfo(fuelProgRect1, x, y, Localization.GUI.ASSEMBLER.FUEL_LEVEL.translate(new String[0]) + ": " + this.assembler.getFuelLevel() + "/" + this.assembler.getMaxFuelLevel());
    }

    private String formatProgress(float progress)
    {
        float percentage = (float)((int)(progress * 10000.0F)) / 100.0F;
        return String.format("%05.2f%%", new Object[] {Float.valueOf(percentage)});
    }

    private String formatTime(int ticks)
    {
        int seconds = ticks / 20;
        int var10000 = ticks - seconds * 20;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        int hours = minutes / 60;
        minutes -= hours * 60;
        return String.format("%02d:%02d:%02d", new Object[] {Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    private void drawProgressBarInfo(int[] rect, int x, int y, String str)
    {
        if (this.inRect(x - this.getGuiLeft(), y - this.getGuiTop(), rect))
        {
            this.drawMouseOver(str, x, y);
        }
    }

    private void drawProgressBar(int[] rect, float progress, int barSrcY, int x, int y)
    {
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        byte boxSrcY = 0;

        if (this.inRect(x - j, y - k, rect))
        {
            boxSrcY = 11;
        }

        this.drawTexturedModalRect(j + rect[0], k + rect[1], 122, boxSrcY, rect[2], rect[3]);

        if (progress != 0.0F)
        {
            if (progress > 1.0F)
            {
                progress = 1.0F;
            }

            this.drawTexturedModalRect(j + rect[0] + 1, k + rect[1] + 1, 122, barSrcY, (int)((float)rect[2] * progress), rect[3] - 2);
        }
    }

    private void render3DCart()
    {
        this.assembler.createPlaceholder();
        int left = this.guiLeft;
        int top = this.guiTop;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        float var10000 = (float)(left + 256);
        StevesCarts var10002 = StevesCarts.instance;
        GL11.glTranslatef(var10000, (float)(top + (StevesCarts.renderSteve ? 50 : 100)), 100.0F);
        float scale = 50.0F;
        GL11.glScalef(-scale, scale, scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.assembler.getRoll(), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.assembler.getYaw(), 0.0F, 1.0F, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;

        if (StevesCarts.renderSteve)
        {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            ItemStack stack = player.getCurrentEquippedItem();
            player.setCurrentItemOrArmor(0, this.assembler.getCartFromModules(true));
            float temp = player.rotationPitch;
            player.rotationPitch = ((float)Math.PI / 4F);
            RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            player.rotationPitch = temp;
            player.setCurrentItemOrArmor(0, stack);
        }
        else
        {
            RenderManager.instance.renderEntityWithPosYaw(this.assembler.getPlaceholder(), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        }

        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        this.assembler.getPlaceholder().keepAlive = 0;
    }

    private void renderDropDownMenu(int x, int y)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 200.0F);
        int j = this.getGuiLeft();
        int k = this.getGuiTop();

        if (this.dropdownX != -1 && this.dropdownY != -1)
        {
            ArrayList items = this.assembler.getDropDown();

            for (int i = 0; i < items.size(); ++i)
            {
                DropDownMenuItem item = (DropDownMenuItem)items.get(i);
                int[] rect = item.getRect(this.dropdownX, this.dropdownY, i);
                int[] subrect = new int[0];
                byte srcX = 0;
                int srcY = item.getIsLarge() ? 113 : 93;
                this.drawTexturedModalRect(j + rect[0], k + rect[1], srcX, srcY, rect[2], rect[3]);

                if (item.getIsLarge())
                {
                    this.drawString(item.getName(), j + rect[0] + 55, k + rect[1] + 7);
                }

                this.drawTexturedModalRect(j + rect[0] + 34, k + rect[1] + 2, item.getImageID() % 16 * 16, 179 + item.getImageID() / 16 * 16, 16, 16);

                if (item.hasSubmenu())
                {
                    subrect = item.getSubRect(this.dropdownX, this.dropdownY, i);
                    int var17 = item.getIsSubMenuOpen() ? 0 : 43;
                    short var18 = 133;
                    this.drawTexturedModalRect(j + subrect[0], k + subrect[1], var17, var18, subrect[2], subrect[3]);
                }

                switch (GuiCartAssembler.NamelessClass1278789747.$SwitchMap$vswe$stevescarts$Helpers$DropDownMenuItem$VALUETYPE[item.getType().ordinal()])
                {
                    case 1:
                        this.drawBooleanBox(x, y, 5 + rect[0], 5 + rect[1], item.getBOOL());
                        break;

                    case 2:
                        if (item.getIsSubMenuOpen())
                        {
                            this.drawIncreamentBox(x, y, this.getOffSetXForSubMenuBox(0, 2) + subrect[0], 3 + subrect[1]);
                            this.drawDecreamentBox(x, y, this.getOffSetXForSubMenuBox(1, 2) + subrect[0], 3 + subrect[1]);
                        }

                        int targetX = rect[0] + 16;
                        int targetY = rect[1] + 7;
                        int valueToWrite = item.getINT();

                        if (valueToWrite >= 10)
                        {
                            this.drawDigit(valueToWrite / 10, -1, targetX, targetY);
                            this.drawDigit(valueToWrite % 10, 1, targetX, targetY);
                        }
                        else
                        {
                            this.drawDigit(valueToWrite, 0, targetX, targetY);
                        }

                        break;

                    case 3:
                        if (item.getIsSubMenuOpen())
                        {
                            int count = item.getMULTIBOOLCount();

                            for (int bool = 0; bool < count; ++bool)
                            {
                                this.drawBooleanBox(x, y, subrect[0] + this.getOffSetXForSubMenuBox(bool, count), subrect[1] + 3, item.getMULTIBOOL(bool));
                            }
                        }
                }
            }
        }

        GL11.glPopMatrix();
    }

    private void drawString(String str, int x, int y)
    {
        str = str.toUpperCase();

        for (int i = 0; i < str.length(); ++i)
        {
            char c = str.charAt(i);
            int index = this.validChars.indexOf(c);

            if (index != -1)
            {
                this.drawTexturedModalRect(x + 7 * i, y, 8 * index, 165, 6, 7);
            }
        }
    }

    private int getOffSetXForSubMenuBox(int id, int count)
    {
        return 2 + (int)(20.0F + ((float)id - (float)count / 2.0F) * 10.0F);
    }

    private void drawDigit(int digit, int offset, int targetX, int targetY)
    {
        int srcX = digit * 8;
        short srcY = 172;
        targetX += offset * 4;
        this.drawTexturedModalRect(this.getGuiLeft() + targetX, this.getGuiTop() + targetY, srcX, srcY, 6, 7);
    }

    private void drawIncreamentBox(int mouseX, int mouseY, int x, int y)
    {
        this.drawStandardBox(mouseX, mouseY, x, y, 10);
    }

    private void drawDecreamentBox(int mouseX, int mouseY, int x, int y)
    {
        this.drawStandardBox(mouseX, mouseY, x, y, 20);
    }

    private void drawBooleanBox(int mouseX, int mouseY, int x, int y, boolean itemvalue)
    {
        this.drawStandardBox(mouseX, mouseY, x, y, 0);

        if (itemvalue)
        {
            this.drawTexturedModalRect(this.getGuiLeft() + x + 2, this.getGuiTop() + y + 2, 0, 159, 6, 6);
        }
    }

    private void drawStandardBox(int mouseX, int mouseY, int x, int y, int srcX)
    {
        int targetX = this.getGuiLeft() + x;
        int targetY = this.getGuiTop() + y;
        short srcY = 149;
        this.drawTexturedModalRect(targetX, targetY, srcX, srcY, 10, 10);

        if (this.inRect(mouseX, mouseY, new int[] {targetX, targetY, 10, 10}))
        {
            this.drawTexturedModalRect(targetX, targetY, 30, srcY, 10, 10);
        }
    }

    private boolean clickBox(int mouseX, int mouseY, int x, int y)
    {
        return this.inRect(mouseX, mouseY, new int[] {x, y, 10, 10});
    }

    public void mouseMoved(int x0, int y0, int button)
    {
        super.mouseMoved(x0, y0, button);
        int x = x0 - this.getGuiLeft();
        int y = y0 - this.getGuiTop();

        if (this.dropdownX != -1 && this.dropdownY != -1)
        {
            ArrayList items = this.assembler.getDropDown();

            for (int i = 0; i < items.size(); ++i)
            {
                DropDownMenuItem item = (DropDownMenuItem)items.get(i);
                boolean insideSubRect = false;

                if (item.hasSubmenu())
                {
                    insideSubRect = this.inRect(x, y, item.getSubRect(this.dropdownX, this.dropdownY, i));

                    if (!insideSubRect && item.getIsSubMenuOpen())
                    {
                        item.setIsSubMenuOpen(false);
                    }
                    else if (insideSubRect && !item.getIsSubMenuOpen())
                    {
                        item.setIsSubMenuOpen(true);
                    }
                }

                boolean insideRect = insideSubRect || this.inRect(x, y, item.getRect(this.dropdownX, this.dropdownY, i));

                if (!insideRect && item.getIsLarge())
                {
                    item.setIsLarge(false);
                }
                else if (insideRect && !item.getIsLarge())
                {
                    item.setIsLarge(true);
                }
            }
        }

        if (this.isScrolling)
        {
            if (button != -1)
            {
                this.isScrolling = false;
                this.assembler.setSpinning(true);
            }
            else
            {
                this.assembler.setYaw(this.assembler.getYaw() + (float)x - (float)this.scrollingX);
                this.assembler.setRoll(this.assembler.getRoll() + (float)y - (float)this.scrollingY);
                this.scrollingX = x;
                this.scrollingY = y;
            }
        }
    }

    public void mouseClick(int x0, int y0, int button)
    {
        super.mouseClick(x0, y0, button);
        int x = x0 - this.getGuiLeft();
        int y = y0 - this.getGuiTop();

        if (this.inRect(x, y, this.assembleRect))
        {
            PacketHandler.sendPacket(0, new byte[0]);
        }
        else if (this.inRect(x, y, this.blackBackground))
        {
            if (button == 0)
            {
                if (!this.isScrolling)
                {
                    this.scrollingX = x;
                    this.scrollingY = y;
                    this.isScrolling = true;
                    this.assembler.setSpinning(false);
                }
            }
            else if (button == 1)
            {
                this.dropdownX = x;
                this.dropdownY = y;

                if (this.dropdownY + this.assembler.getDropDown().size() * 20 > 164)
                {
                    this.dropdownY = 164 - this.assembler.getDropDown().size() * 20;
                }
            }
        }
        else
        {
            ArrayList anyLargeItem = this.assembler.getSlots();

            for (int items = 1; items < anyLargeItem.size(); ++items)
            {
                SlotAssembler i = (SlotAssembler)anyLargeItem.get(items);
                int item = i.getX() - 1;
                int rect = i.getY() - 1;
                byte subrect = 18;

                if (this.inRect(x, y, new int[] {item, rect, subrect, subrect}) && i.getStack() != null && i.getStack().stackSize <= 0)
                {
                    PacketHandler.sendPacket(1, new byte[] {(byte)items});
                }
            }
        }

        if (button == 0 && this.dropdownX != -1 && this.dropdownY != -1)
        {
            boolean var14 = false;
            ArrayList var15 = this.assembler.getDropDown();

            for (int var16 = 0; var16 < var15.size(); ++var16)
            {
                DropDownMenuItem var17 = (DropDownMenuItem)var15.get(var16);

                if (var17.getIsLarge())
                {
                    var14 = true;
                    int[] var18 = var17.getRect(this.dropdownX, this.dropdownY, var16);
                    int[] var19 = new int[0];

                    if (var17.hasSubmenu() && var17.getIsSubMenuOpen())
                    {
                        var19 = var17.getSubRect(this.dropdownX, this.dropdownY, var16);
                    }

                    switch (GuiCartAssembler.NamelessClass1278789747.$SwitchMap$vswe$stevescarts$Helpers$DropDownMenuItem$VALUETYPE[var17.getType().ordinal()])
                    {
                        case 1:
                            if (this.clickBox(x, y, 5 + var18[0], 5 + var18[1]))
                            {
                                var17.setBOOL(!var17.getBOOL());
                            }

                            break;

                        case 2:
                            if (var17.getIsSubMenuOpen())
                            {
                                if (this.clickBox(x, y, this.getOffSetXForSubMenuBox(0, 2) + var19[0], 3 + var19[1]))
                                {
                                    var17.setINT(var17.getINT() + 1);
                                }

                                if (this.clickBox(x, y, this.getOffSetXForSubMenuBox(1, 2) + var19[0], 3 + var19[1]))
                                {
                                    var17.setINT(var17.getINT() - 1);
                                }
                            }

                            break;

                        case 3:
                            if (var17.getIsSubMenuOpen())
                            {
                                int count = var17.getMULTIBOOLCount();

                                for (int bool = 0; bool < count; ++bool)
                                {
                                    if (this.clickBox(x, y, var19[0] + this.getOffSetXForSubMenuBox(bool, count), var19[1] + 3))
                                    {
                                        var17.setMULTIBOOL(bool, !var17.getMULTIBOOL(bool));
                                        break;
                                    }
                                }
                            }
                    }
                }
            }

            if (!var14)
            {
                this.dropdownX = this.dropdownY = -1;
            }
        }
    }

    static
    {
        for (int i = 0; i < backgrounds.length; ++i)
        {
            backgrounds[i] = ResourceHelper.getResource("/gui/garageBackground" + i + ".png");
        }

        textureLeft = ResourceHelper.getResource("/gui/garagePart1.png");
        textureRight = ResourceHelper.getResource("/gui/garagePart2.png");
        textureExtra = ResourceHelper.getResource("/gui/garageExtra.png");
    }

    static class NamelessClass1278789747
    {
        static final int[] $SwitchMap$vswe$stevescarts$Helpers$DropDownMenuItem$VALUETYPE = new int[DropDownMenuItem.VALUETYPE.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$DropDownMenuItem$VALUETYPE[DropDownMenuItem.VALUETYPE.BOOL.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$DropDownMenuItem$VALUETYPE[DropDownMenuItem.VALUETYPE.INT.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$DropDownMenuItem$VALUETYPE[DropDownMenuItem.VALUETYPE.MULTIBOOL.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    private class TextWithColor
    {
        private String text;
        private int color;

        public TextWithColor(String text, int color)
        {
            this.text = text;
            this.color = color;
        }

        public String getText()
        {
            return this.text;
        }

        public int getColor()
        {
            return this.color;
        }
    }
}
