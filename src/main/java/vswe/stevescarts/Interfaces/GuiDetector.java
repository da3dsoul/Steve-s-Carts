package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Containers.ContainerDetector;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.Helpers.DropDownMenu;
import vswe.stevescarts.Helpers.DropDownMenuPages;
import vswe.stevescarts.Helpers.LogicObject;
import vswe.stevescarts.Helpers.ModuleState;
import vswe.stevescarts.Helpers.OperatorObject;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.TileEntities.TileEntityDetector;

@SideOnly(Side.CLIENT)
public class GuiDetector extends GuiBase
{
    private ArrayList<DropDownMenu> menus;
    private DropDownMenuPages modulesMenu;
    private DropDownMenu statesMenu;
    private DropDownMenu flowMenu;
    public static ResourceLocation texture = ResourceHelper.getResource("/gui/detector.png");
    public static ResourceLocation moduleTexture = ResourceHelper.getResourceFromPath("/atlas/items.png");
    public static ResourceLocation stateTexture = ResourceHelper.getResource("/gui/states.png");
    public static ResourceLocation dropdownTexture = ResourceHelper.getResource("/gui/detector2.png");
    public LogicObject currentObject;
    TileEntityDetector detector;
    InventoryPlayer invPlayer;

    public GuiDetector(InventoryPlayer invPlayer, TileEntityDetector detector)
    {
        super(new ContainerDetector(invPlayer, detector));
        this.invPlayer = invPlayer;
        this.setXSize(255);
        this.setYSize(202);
        this.detector = detector;
        Iterator i$ = detector.mainObj.getChilds().iterator();

        if (i$.hasNext())
        {
            LogicObject child = (LogicObject)i$.next();
            child.setParent((LogicObject)null);
        }

        detector.recalculateTree();
        this.menus = new ArrayList();
        this.menus.add(this.modulesMenu = new DropDownMenuPages(0, 2));
        this.menus.add(this.statesMenu = new DropDownMenu(1));
        this.menus.add(this.flowMenu = new DropDownMenu(2));
    }

    public void drawGuiForeground(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.getFontRenderer().drawString(DetectorType.getTypeFromMeta(this.detector.getBlockMetadata()).getName(), 8, 6, 4210752);
        int flowPosId;
        Iterator i$;
        int[] target;

        if (this.modulesMenu.getScroll() != 0)
        {
            flowPosId = 0;
            i$ = ModuleData.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleData operator = (ModuleData)i$.next();

                if (operator.getIsValid())
                {
                    target = this.modulesMenu.getContentRect(flowPosId);

                    if (this.drawMouseOver(operator.getName(), x, y, target))
                    {
                        break;
                    }

                    ++flowPosId;
                }
            }
        }
        else if (this.statesMenu.getScroll() != 0)
        {
            flowPosId = 0;

            for (i$ = ModuleState.getStateList().iterator(); i$.hasNext(); ++flowPosId)
            {
                ModuleState var7 = (ModuleState)i$.next();
                target = this.statesMenu.getContentRect(flowPosId);

                if (this.drawMouseOver(var7.getName(), x, y, target))
                {
                    break;
                }
            }
        }
        else if (this.flowMenu.getScroll() != 0)
        {
            flowPosId = 0;
            i$ = OperatorObject.getOperatorList(this.detector.getBlockMetadata()).iterator();

            while (i$.hasNext())
            {
                OperatorObject var8 = (OperatorObject)i$.next();

                if (var8.inTab())
                {
                    target = this.flowMenu.getContentRect(flowPosId);

                    if (this.drawMouseOver(var8.getName(), x, y, target))
                    {
                        break;
                    }

                    ++flowPosId;
                }
            }
        }
        else
        {
            this.drawMouseOverFromObject(this.detector.mainObj, x, y);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private boolean drawMouseOverFromObject(LogicObject obj, int x, int y)
    {
        if (this.drawMouseOver(obj.getName(), x, y, obj.getRect()))
        {
            return true;
        }
        else
        {
            Iterator i$ = obj.getChilds().iterator();
            LogicObject child;

            do
            {
                if (!i$.hasNext())
                {
                    return false;
                }

                child = (LogicObject)i$.next();
            }
            while (!this.drawMouseOverFromObject(child, x, y));

            return true;
        }
    }

    private boolean drawMouseOver(String str, int x, int y, int[] rect)
    {
        if (rect != null && this.inRect(x - this.getGuiLeft(), y - this.getGuiTop(), rect))
        {
            this.drawMouseOver(str, x - this.getGuiLeft(), y - this.getGuiTop());
            return true;
        }
        else
        {
            return false;
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
        this.detector.mainObj.draw(this, x, y);
        DropDownMenu.update(this, x, y, this.menus);
        this.flowMenu.drawMain(this, x, y);
        ResourceHelper.bindResource(texture);
        int flowPosId = 0;
        Iterator statePosId = OperatorObject.getOperatorList(this.detector.getBlockMetadata()).iterator();

        while (statePosId.hasNext())
        {
            OperatorObject modulePosId = (OperatorObject)statePosId.next();

            if (modulePosId.inTab())
            {
                int[] i$ = this.getOperatorTexture(modulePosId.getID());
                this.flowMenu.drawContent(this, flowPosId, i$[0], i$[1]);
                ++flowPosId;
            }
        }

        this.statesMenu.drawMain(this, x, y);
        ResourceHelper.bindResource(stateTexture);
        int var11 = 0;

        for (Iterator var12 = ModuleState.getStateList().iterator(); var12.hasNext(); ++var11)
        {
            ModuleState var14 = (ModuleState)var12.next();
            int[] module = this.getModuleTexture(var14.getID());
            this.statesMenu.drawContent(this, var11, module[0], module[1]);
        }

        this.modulesMenu.drawMain(this, x, y);
        ResourceHelper.bindResource(moduleTexture);
        int var13 = 0;
        Iterator var15 = ModuleData.getModules().iterator();

        while (var15.hasNext())
        {
            ModuleData var16 = (ModuleData)var15.next();

            if (var16.getIsValid())
            {
                this.modulesMenu.drawContent(this, var13, var16.getIcon());
                ++var13;
            }
        }

        this.flowMenu.drawHeader(this);
        this.statesMenu.drawHeader(this);
        this.modulesMenu.drawHeader(this);

        if (this.currentObject != null)
        {
            this.currentObject.draw(this, -500, -500, x, y);
        }
    }

    public int[] getOperatorTexture(byte operatorId)
    {
        int x = operatorId % 11;
        int y = operatorId / 11;
        return new int[] {36 + x * 20, this.ySize + y * 11};
    }

    public int[] getModuleTexture(byte moduleId)
    {
        int srcX = moduleId % 16 * 16;
        int srcY = moduleId / 16 * 16;
        return new int[] {srcX, srcY};
    }

    private int[] getOperatorRect(int posId)
    {
        return new int[] {20 + posId * 30, 20, 20, 11};
    }

    public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();

        if (button == 0)
        {
            if (isShiftKeyDown())
            {
                if (this.currentObject == null)
                {
                    this.pickupObject(x, y, this.detector.mainObj);
                }
            }
            else
            {
                int modulePosId = 0;
                Iterator statePosId = ModuleData.getModules().iterator();

                while (statePosId.hasNext())
                {
                    ModuleData flowPosId = (ModuleData)statePosId.next();

                    if (flowPosId.getIsValid())
                    {
                        int[] i$ = this.modulesMenu.getContentRect(modulePosId);

                        if (this.inRect(x, y, i$))
                        {
                            this.currentObject = new LogicObject((byte)0, flowPosId.getID());
                            return;
                        }

                        ++modulePosId;
                    }
                }

                int var10 = 0;

                for (Iterator var11 = ModuleState.getStateList().iterator(); var11.hasNext(); ++var10)
                {
                    ModuleState var13 = (ModuleState)var11.next();
                    int[] menu = this.statesMenu.getContentRect(var10);

                    if (this.inRect(x, y, menu))
                    {
                        this.currentObject = new LogicObject((byte)2, var13.getID());
                        return;
                    }
                }

                int var12 = 0;
                Iterator var14 = OperatorObject.getOperatorList(this.detector.getBlockMetadata()).iterator();

                while (var14.hasNext())
                {
                    OperatorObject var15 = (OperatorObject)var14.next();

                    if (var15.inTab())
                    {
                        int[] target = this.flowMenu.getContentRect(var12);

                        if (this.inRect(x, y, target))
                        {
                            this.currentObject = new LogicObject((byte)1, var15.getID());
                            return;
                        }

                        ++var12;
                    }
                }

                var14 = this.menus.iterator();

                while (var14.hasNext())
                {
                    DropDownMenu var16 = (DropDownMenu)var14.next();
                    var16.onClick(this, x, y);
                }
            }
        }
        else if (button == 1 && this.currentObject == null)
        {
            this.removeObject(x, y, this.detector.mainObj);
        }
    }

    public void mouseMoved(int x, int y, int button)
    {
        super.mouseMoved(x, y, button);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();

        if (button != -1 && this.currentObject != null)
        {
            this.dropOnObject(x, y, this.detector.mainObj, this.currentObject);
            this.currentObject = null;
        }
    }

    private boolean removeObject(int x, int y, LogicObject object)
    {
        if (this.inRect(x, y, object.getRect()) && object.canBeRemoved())
        {
            object.setParent(this.detector, (LogicObject)null);
            return true;
        }
        else
        {
            Iterator i$ = object.getChilds().iterator();
            LogicObject child;

            do
            {
                if (!i$.hasNext())
                {
                    return false;
                }

                child = (LogicObject)i$.next();
            }
            while (!this.removeObject(x, y, child));

            return true;
        }
    }

    private boolean pickupObject(int x, int y, LogicObject object)
    {
        if (this.inRect(x, y, object.getRect()) && object.canBeRemoved())
        {
            this.currentObject = object;
            object.setParent(this.detector, (LogicObject)null);
            return true;
        }
        else
        {
            Iterator i$ = object.getChilds().iterator();
            LogicObject child;

            do
            {
                if (!i$.hasNext())
                {
                    return false;
                }

                child = (LogicObject)i$.next();
            }
            while (!this.pickupObject(x, y, child));

            return true;
        }
    }

    private boolean dropOnObject(int x, int y, LogicObject object, LogicObject drop)
    {
        if (this.inRect(x, y, object.getRect()))
        {
            if (object.hasRoomForChild() && object.isChildValid(drop))
            {
                drop.setParent(this.detector, object);
            }

            return true;
        }
        else
        {
            Iterator i$ = object.getChilds().iterator();
            LogicObject child;

            do
            {
                if (!i$.hasNext())
                {
                    return false;
                }

                child = (LogicObject)i$.next();
            }
            while (!this.dropOnObject(x, y, child, drop));

            return true;
        }
    }
}
