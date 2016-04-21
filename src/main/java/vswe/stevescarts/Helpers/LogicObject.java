package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.PacketHandler;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiDetector;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.TileEntities.TileEntityDetector;

public class LogicObject
{
    private byte id;
    private LogicObject parent;
    private byte type;
    private ArrayList<LogicObject> childs;
    private int x;
    private int y;
    private int level;
    private byte data;

    public LogicObject(byte id, byte type, byte data)
    {
        this.id = id;
        this.type = type;
        this.data = data;
        this.childs = new ArrayList();
    }

    public LogicObject(byte type, byte data)
    {
        this((byte)0, type, data);
    }

    public void setParent(TileEntityDetector detector, LogicObject parent)
    {
        if (parent != null)
        {
            PacketHandler.sendPacket(0, new byte[] {parent.id, this.getExtra(), this.data});
            Iterator i$ = this.childs.iterator();

            while (i$.hasNext())
            {
                LogicObject child = (LogicObject)i$.next();
                child.setParent(detector, this);
            }
        }
        else
        {
            PacketHandler.sendPacket(1, new byte[] {this.id});
        }
    }

    public void setParent(LogicObject parent)
    {
        if (this.parent != null)
        {
            this.parent.childs.remove(this);
        }

        this.parent = parent;

        if (this.parent != null && this.parent.hasRoomForChild())
        {
            this.parent.childs.add(this);
        }
    }

    public ArrayList<LogicObject> getChilds()
    {
        return this.childs;
    }

    public LogicObject getParent()
    {
        return this.parent;
    }

    public byte getId()
    {
        return this.id;
    }

    public byte getExtra()
    {
        return this.type;
    }

    public byte getData()
    {
        return this.data;
    }

    public void setX(int val)
    {
        this.x = val;
    }

    public void setY(int val)
    {
        this.y = val;
    }

    public void setXCenter(int val)
    {
        this.setX(val + (!this.isOperator() ? -8 : -10));
    }

    public void setYCenter(int val)
    {
        this.setY(val + (!this.isOperator() ? -8 : -5));
    }

    @SideOnly(Side.CLIENT)
    public void draw(GuiDetector gui, int mouseX, int mouseY, int x, int y)
    {
        this.generatePosition(x - 50, y, 100, 0);
        this.draw(gui, mouseX, mouseY);
    }

    @SideOnly(Side.CLIENT)
    public void draw(GuiDetector gui, int mouseX, int mouseY)
    {
        if (!this.isOperator())
        {
            ResourceHelper.bindResource(GuiDetector.texture);
            byte i$ = 0;

            if (gui.inRect(mouseX, mouseY, this.getRect()))
            {
                i$ = 1;
            }

            gui.drawTexturedModalRect(gui.getGuiLeft() + this.x, gui.getGuiTop() + this.y, 0, 202 + i$ * 16, 16, 16);

            if (this.isModule())
            {
                ResourceHelper.bindResource(GuiDetector.moduleTexture);
                ModuleData child = (ModuleData)ModuleData.getList().get(Byte.valueOf(this.data));

                if (child != null)
                {
                    gui.drawIcon(child.getIcon(), gui.getGuiLeft() + this.x, gui.getGuiTop() + this.y, 1.0F, 1.0F, 0.0F, 0.0F);
                }
            }
            else
            {
                ResourceHelper.bindResource(GuiDetector.stateTexture);
                int[] child1 = gui.getModuleTexture(this.data);
                gui.drawTexturedModalRect(gui.getGuiLeft() + this.x, gui.getGuiTop() + this.y, child1[0], child1[1], 16, 16);
            }
        }
        else
        {
            ResourceHelper.bindResource(GuiDetector.texture);
            int[] i$1 = gui.getOperatorTexture(this.data);
            gui.drawTexturedModalRect(gui.getGuiLeft() + this.x, gui.getGuiTop() + this.y, i$1[0], i$1[1], 20, 11);

            if (gui.inRect(mouseX, mouseY, this.getRect()))
            {
                byte child2;

                if (gui.currentObject == null)
                {
                    child2 = 2;
                }
                else if (this.hasRoomForChild() && this.isChildValid(gui.currentObject))
                {
                    child2 = 0;
                }
                else
                {
                    child2 = 1;
                }

                gui.drawTexturedModalRect(gui.getGuiLeft() + this.x, gui.getGuiTop() + this.y, 16, 202 + child2 * 11, 20, 11);
            }
        }

        if (this.parent != null && this.parent.maxChilds() > 1)
        {
            int i$2 = gui.getGuiLeft() + this.x;
            int child3 = gui.getGuiTop() + this.y;
            int px2 = gui.getGuiLeft() + this.parent.x;
            int py2 = gui.getGuiTop() + this.parent.y;
            py2 += 5;
            i$2 += !this.isOperator() ? 8 : 10;
            boolean tooClose = false;

            if (this.x > this.parent.x)
            {
                px2 += 20;

                if (i$2 < px2)
                {
                    tooClose = true;
                }
            }
            else if (i$2 > px2)
            {
                tooClose = true;
            }

            if (!tooClose)
            {
                GuiDetector.drawRect(i$2, py2, px2, py2 + 1, -12566464);
                GuiDetector.drawRect(i$2, child3, i$2 + 1, py2, -12566464);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        Iterator i$3 = this.childs.iterator();

        while (i$3.hasNext())
        {
            LogicObject child4 = (LogicObject)i$3.next();
            child4.draw(gui, mouseX, mouseY);
        }
    }

    public void generatePosition(int x, int y, int w, int level)
    {
        this.setXCenter(x + w / 2);
        this.setYCenter(y);
        this.level = level;
        int max = this.maxChilds();

        for (int i = 0; i < this.childs.size(); ++i)
        {
            ((LogicObject)this.childs.get(i)).generatePosition(x + w / max * i, y + (!((LogicObject)this.childs.get(i)).isOperator() ? 16 : 11), w / max, level + (((LogicObject)this.childs.get(i)).maxChilds() > 1 ? 1 : 0));
        }
    }

    private boolean isModule()
    {
        return this.type == 0;
    }

    private boolean isOperator()
    {
        return this.type == 1;
    }

    private boolean isState()
    {
        return this.type == 2;
    }

    private OperatorObject getOperator()
    {
        return this.isOperator() ? (OperatorObject)OperatorObject.getAllOperators().get(Byte.valueOf(this.data)) : null;
    }

    public boolean evaluateLogicTree(TileEntityDetector detector, MinecartModular cart, int depth)
    {
        if (depth >= 1000)
        {
            return false;
        }
        else if (this.isState())
        {
        	//FMLLog.info("%s", "[SC2::LogicObject::Evaluate] depth = " + depth);
        	//FMLLog.info("%s", "[SC2::LogicObject::Evaluate] Is a State");
            ModuleState operator2 = (ModuleState)ModuleState.getStates().get(Byte.valueOf(this.getData()));
            return operator2 != null ? operator2.evaluate(cart) : false;
        }
        else if (this.isModule())
        {
        	//FMLLog.info("%s", "[SC2::LogicObject::Evaluate] depth = " + depth);
        	//FMLLog.info("%s", "[SC2::LogicObject::Evaluate] Is a Module");
            Iterator operator1 = cart.getModules().iterator();
            ModuleBase module;

            do
            {
                if (!operator1.hasNext())
                {
                    return false;
                }

                module = (ModuleBase)operator1.next();
            }
            while (this.getData() != module.getModuleId());

            return true;
        }
        else if (this.getChilds().size() != this.maxChilds())
        {
            return false;
        }
        else
        {
        	//FMLLog.info("%s", "[SC2::LogicObject::Evaluate] depth = " + depth);
        	//FMLLog.info("%s", "[SC2::LogicObject::Evaluate] Is a Logic Operation");
            OperatorObject operator = this.getOperator();
            return operator != null ? (operator.getChildCount() == 2 ? operator.evaluate(detector, cart, depth + 1, (LogicObject)this.getChilds().get(0), (LogicObject)this.getChilds().get(1)) : (operator.getChildCount() == 1 ? operator.evaluate(detector, cart, depth + 1, (LogicObject)this.getChilds().get(0), (LogicObject)null) : operator.evaluate(detector, cart, depth + 1, (LogicObject)null, (LogicObject)null))) : false;
        }
    }

    private int maxChilds()
    {
        OperatorObject operator = this.getOperator();
        return operator != null ? operator.getChildCount() : 0;
    }

    public boolean isChildValid(LogicObject child)
    {
        if (this.level >= 4 && child.isOperator())
        {
            return false;
        }
        else if (this.level >= 5)
        {
            return false;
        }
        else
        {
            OperatorObject operator = this.getOperator();
            OperatorObject operatorchild = child.getOperator();
            return operator != null && operatorchild != null ? operator.isChildValid(operatorchild) : true;
        }
    }

    public boolean canBeRemoved()
    {
        OperatorObject operator = this.getOperator();
        return operator != null ? operator.inTab() : true;
    }

    public boolean hasRoomForChild()
    {
        return this.childs.size() < this.maxChilds();
    }

    public int[] getRect()
    {
        return !this.isOperator() ? new int[] {this.x, this.y, 16, 16}: new int[] {this.x, this.y, 20, 11};
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof LogicObject))
        {
            return false;
        }
        else
        {
            LogicObject logic = (LogicObject)obj;
            return logic.id == this.id && (logic.parent == null && this.parent == null || logic.parent != null && this.parent != null && logic.parent.id == this.parent.id) && logic.getExtra() == this.getExtra() && logic.getData() == this.getData();
        }
    }

    public LogicObject copy(LogicObject parent)
    {
        LogicObject obj = new LogicObject(this.id, this.getExtra(), this.getData());
        obj.setParent(parent);
        return obj;
    }

    public String getName()
    {
        if (this.isState())
        {
            ModuleState name2 = (ModuleState)ModuleState.getStates().get(Byte.valueOf(this.getData()));
            return name2 == null ? "Undefined" : name2.getName();
        }
        else if (this.isModule())
        {
            ModuleData name1 = (ModuleData)ModuleData.getList().get(Byte.valueOf(this.getData()));
            return name1 == null ? "Undefined" : name1.getName();
        }
        else
        {
            String name = "Undefined";
            OperatorObject operator = this.getOperator();

            if (operator != null)
            {
                name = operator.getName();
            }

            return name + "\nChild nodes: " + this.getChilds().size() + "/" + this.maxChilds();
        }
    }
}
