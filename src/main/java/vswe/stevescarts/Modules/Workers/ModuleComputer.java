package vswe.stevescarts.Modules.Workers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Buttons.ButtonBase;
import vswe.stevescarts.Buttons.ButtonControlInteger;
import vswe.stevescarts.Buttons.ButtonControlType;
import vswe.stevescarts.Buttons.ButtonControlUseVar;
import vswe.stevescarts.Buttons.ButtonControlVar;
import vswe.stevescarts.Buttons.ButtonFlowConditionInteger;
import vswe.stevescarts.Buttons.ButtonFlowConditionOperator;
import vswe.stevescarts.Buttons.ButtonFlowConditionSecondVar;
import vswe.stevescarts.Buttons.ButtonFlowConditionUseSecondVar;
import vswe.stevescarts.Buttons.ButtonFlowConditionVar;
import vswe.stevescarts.Buttons.ButtonFlowEndType;
import vswe.stevescarts.Buttons.ButtonFlowForEndInteger;
import vswe.stevescarts.Buttons.ButtonFlowForEndVar;
import vswe.stevescarts.Buttons.ButtonFlowForStartInteger;
import vswe.stevescarts.Buttons.ButtonFlowForStartVar;
import vswe.stevescarts.Buttons.ButtonFlowForStep;
import vswe.stevescarts.Buttons.ButtonFlowForUseEndVar;
import vswe.stevescarts.Buttons.ButtonFlowForUseStartVar;
import vswe.stevescarts.Buttons.ButtonFlowForVar;
import vswe.stevescarts.Buttons.ButtonFlowType;
import vswe.stevescarts.Buttons.ButtonInfoType;
import vswe.stevescarts.Buttons.ButtonInfoVar;
import vswe.stevescarts.Buttons.ButtonKeyboard;
import vswe.stevescarts.Buttons.ButtonLabelId;
import vswe.stevescarts.Buttons.ButtonProgramAdd;
import vswe.stevescarts.Buttons.ButtonProgramStart;
import vswe.stevescarts.Buttons.ButtonTask;
import vswe.stevescarts.Buttons.ButtonTaskType;
import vswe.stevescarts.Buttons.ButtonVarAdd;
import vswe.stevescarts.Buttons.ButtonVarFirstInteger;
import vswe.stevescarts.Buttons.ButtonVarFirstVar;
import vswe.stevescarts.Buttons.ButtonVarSecondInteger;
import vswe.stevescarts.Buttons.ButtonVarSecondVar;
import vswe.stevescarts.Buttons.ButtonVarType;
import vswe.stevescarts.Buttons.ButtonVarUseFirstVar;
import vswe.stevescarts.Buttons.ButtonVarUseSecondVar;
import vswe.stevescarts.Buttons.ButtonVarVar;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Computer.ComputerControl;
import vswe.stevescarts.Computer.ComputerInfo;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;
import vswe.stevescarts.Computer.ComputerVar;
import vswe.stevescarts.Computer.IWriting;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class ModuleComputer extends ModuleWorker
{
    private IWriting writing;
    private short info;
    private ArrayList<ComputerProg> programs = new ArrayList();
    private ComputerProg editProg;
    private ArrayList<ComputerTask> editTasks = new ArrayList();
    private ComputerProg activeProg;
    private static final int headerSize = 1;
    private static final int programHeaderSize = 3;
    private static final int taskMaxCount = 256;
    private static final int varMaxCount = 63;
    private static final int taskSize = 2;
    private static final int varSize = 5;

    public ModuleComputer(MinecartModular cart)
    {
        super(cart);
    }

    public byte getWorkPriority()
    {
        return (byte)5;
    }

    public boolean hasGui()
    {
        return true;
    }

    public boolean hasSlots()
    {
        return false;
    }

    public int guiWidth()
    {
        return 443;
    }

    public int guiHeight()
    {
        return 250;
    }

    public void drawForeground(GuiMinecart gui)
    {
        if (this.isWriting())
        {
            this.drawString(gui, this.getWriting().getText(), 100, 6, 4210752);
            this.drawString(gui, "Max Length: " + this.getWriting().getMaxLength(), 100, 18, 4210752);
        }
    }

    protected void loadButtons()
    {
        new ButtonProgramAdd(this, ButtonBase.LOCATION.OVERVIEW);
        new ButtonProgramStart(this, ButtonBase.LOCATION.OVERVIEW);
        int i;

        for (i = 0; i < 7; ++i)
        {
            new ButtonTaskType(this, ButtonBase.LOCATION.PROGRAM, i);
        }

        new ButtonVarAdd(this, ButtonBase.LOCATION.PROGRAM);

        for (i = 0; i < 11; ++i)
        {
            new ButtonFlowType(this, ButtonBase.LOCATION.TASK, i);
        }

        new ButtonLabelId(this, ButtonBase.LOCATION.TASK, true);
        new ButtonLabelId(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionVar(this, ButtonBase.LOCATION.TASK, true);

        for (i = 0; i < 6; ++i)
        {
            new ButtonFlowConditionOperator(this, ButtonBase.LOCATION.TASK, i);
        }

        new ButtonFlowConditionUseSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionUseSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonFlowConditionSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForUseStartVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForUseStartVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonFlowForStartVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForStartVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForUseEndVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForUseEndVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonFlowForEndVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForEndVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForStep(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForStep(this, ButtonBase.LOCATION.TASK, true);

        for (i = 0; i < 4; ++i)
        {
            new ButtonFlowEndType(this, ButtonBase.LOCATION.TASK, i);
        }

        for (i = 0; i < 18; ++i)
        {
            new ButtonVarType(this, ButtonBase.LOCATION.TASK, i);
        }

        new ButtonVarVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarUseFirstVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarUseFirstVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonVarFirstVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarFirstVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarUseSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarUseSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonVarSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonControlType(this, ButtonBase.LOCATION.TASK, 0);
        ComputerControl.createButtons(this.getCart(), this);
        new ButtonControlUseVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonControlUseVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonControlVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonControlVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonInfoType(this, ButtonBase.LOCATION.TASK, 0);
        ComputerInfo.createButtons(this.getCart(), this);
        new ButtonInfoVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonInfoVar(this, ButtonBase.LOCATION.TASK, true);

        for (i = 0; i < 21; ++i)
        {
            new ButtonTask(this, ButtonBase.LOCATION.FLOATING, i);
        }

        ButtonKeyboard.generateKeyboard(this);
    }

    public boolean useButtons()
    {
        return true;
    }

    public boolean isWriting()
    {
        return this.writing != null;
    }

    public IWriting getWriting()
    {
        return this.writing;
    }

    public void setWriting(IWriting val)
    {
        this.writing = val;
    }

    public void flipShift()
    {
        this.info = (short)(this.info ^ 1);
    }

    public void flipCaps()
    {
        this.info = (short)(this.info ^ 2);
    }

    public boolean getShift()
    {
        return (this.info & 1) != 0;
    }

    public boolean getCaps()
    {
        return (this.info & 2) != 0;
    }

    public boolean isLower()
    {
        return this.getShift() == this.getCaps();
    }

    public void disableShift()
    {
        this.info = (short)(this.info & -2);
    }

    public ComputerProg getCurrentProg()
    {
        return this.editProg;
    }

    public ArrayList<ComputerTask> getSelectedTasks()
    {
        return this.editTasks;
    }

    public void setCurrentProg(ComputerProg prog)
    {
        this.editProg = prog;
    }

    public void setActiveProgram(ComputerProg prog)
    {
        this.activeProg = prog;
    }

    public ComputerProg getActiveProgram()
    {
        return this.activeProg;
    }

    public boolean work()
    {
        if (this.activeProg != null)
        {
            if (this.doPreWork())
            {
                this.startWorking(this.activeProg.getRunTime());
            }
            else
            {
                if (!this.activeProg.run())
                {
                    this.activeProg = null;
                }

                this.stopWorking();
            }
        }

        return true;
    }

    public void update()
    {
        super.update();
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player) {}

    public int numberOfPackets()
    {
        return 0;
    }

    public int numberOfGuiData()
    {
        return 831;
    }

    public void activationChanged()
    {
        this.editTasks.clear();

        if (this.editProg != null)
        {
            Iterator i$ = this.editProg.getTasks().iterator();

            while (i$.hasNext())
            {
                ComputerTask task = (ComputerTask)i$.next();

                if (task.getIsActivated())
                {
                    this.editTasks.add(task);
                }
            }
        }
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, this.info);

        if (this.editProg != null)
        {
            this.updateGuiData(info, 1, this.editProg.getInfo());
            int tasks = this.editProg.getTasks().size();
            int vars = this.editProg.getVars().size();
            this.updateGuiData(info, 2, (short)(tasks << 8 | vars));

            if (this.editProg == this.activeProg)
            {
                this.updateGuiData(info, 3, (short)this.activeProg.getActiveId());
            }
            else
            {
                this.updateGuiData(info, 3, (short)256);
            }

            int varId;
            int internalId;

            for (varId = 0; varId < tasks; ++varId)
            {
                ComputerTask theVar = (ComputerTask)this.editProg.getTasks().get(varId);

                for (internalId = 0; internalId < 2; ++internalId)
                {
                    this.updateGuiData(info, 4 + varId * 2 + internalId, theVar.getInfo(internalId));
                }
            }

            for (varId = 0; varId < vars; ++varId)
            {
                ComputerVar var7 = (ComputerVar)this.editProg.getVars().get(varId);

                for (internalId = 0; internalId < 5; ++internalId)
                {
                    this.updateGuiData(info, 516 + varId * 5 + internalId, var7.getInfo(internalId));
                }
            }
        }
        else
        {
            this.updateGuiData(info, 1, (short)0);
        }
    }

    public void receiveGuiData(int id, short data)
    {
        System.out.println("ID " + id + " Data " + data);

        if (id == 0)
        {
            this.info = data;
        }
        else if (id == 1)
        {
            if (data == 0)
            {
                this.editProg = null;
            }
            else
            {
                if (this.editProg == null)
                {
                    this.editProg = new ComputerProg(this);
                }

                this.editProg.setInfo(data);
            }
        }
        else if (this.editProg != null)
        {
            int taskId;
            int varId;

            if (id == 2)
            {
                taskId = data >> 8 & 255;
                varId = data & 255;
                this.editProg.setTaskCount(taskId);
                this.editProg.setVarCount(varId);
            }
            else if (id == 3)
            {
                if (data >= 0 && data < 256)
                {
                    this.activeProg = this.editProg;
                    this.editProg.setActiveId(data);
                }
                else
                {
                    this.activeProg = null;
                    this.editProg.setActiveId(0);
                }
            }
            else
            {
                taskId = id - 1 - 3;
                int var;

                if (taskId < 512)
                {
                    varId = taskId / 2;
                    var = taskId % 2;

                    if (varId >= 0 && varId < this.editProg.getTasks().size())
                    {
                        ComputerTask varInternalPos = (ComputerTask)this.editProg.getTasks().get(varId);
                        varInternalPos.setInfo(var, data);
                    }
                }
                else
                {
                    varId = taskId - 512;
                    var = varId / 5;
                    int varInternalPos1 = varId % 5;

                    if (var >= 0 && var < this.editProg.getVars().size())
                    {
                        ComputerVar theVar = (ComputerVar)this.editProg.getVars().get(var);
                        theVar.setInfo(varInternalPos1, data);
                    }
                }
            }
        }
    }
}
