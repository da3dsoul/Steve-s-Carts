package vswe.stevescarts.Computer;

import java.util.Iterator;
import java.util.Random;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ComputerTask
{
    private static Random rand = new Random();
    private ModuleComputer module;
    private ComputerProg prog;
    private int info;

    public ComputerTask(ModuleComputer module, ComputerProg prog)
    {
        this.module = module;
        this.prog = prog;
    }

    public int getTime()
    {
        return 5;
    }

    public int run(ComputerProg prog, int id)
    {
        int i$;
        int var;
        ComputerTask var1;

        if (this.isFlowGoto())
        {
            i$ = this.getFlowLabelId();

            for (var = 0; var < prog.getTasks().size(); ++var)
            {
                var1 = (ComputerTask)prog.getTasks().get(var);

                if (var1.isFlowLabel() && var1.getFlowLabelId() == i$)
                {
                    return var;
                }
            }
        }
        else
        {
            ComputerTask var2;
            boolean var7;
            int var13;

            if (this.isFlowCondition())
            {
                var7 = this.evalFlowCondition();
                var = 0;

                if (!var7)
                {
                    if (!this.isFlowIf() && !this.isFlowElseif())
                    {
                        if (this.isFlowWhile())
                        {
                            for (var13 = id + 1; var13 < prog.getTasks().size(); ++var13)
                            {
                                var2 = (ComputerTask)prog.getTasks().get(var13);

                                if (var2.isFlowWhile())
                                {
                                    ++var;
                                }
                                else if (var2.isFlowEndwhile())
                                {
                                    if (var == 0)
                                    {
                                        return var13;
                                    }

                                    --var;
                                }
                            }
                        }
                    }
                    else
                    {
                        for (var13 = id + 1; var13 < prog.getTasks().size(); ++var13)
                        {
                            var2 = (ComputerTask)prog.getTasks().get(var13);

                            if (var2.isFlowIf())
                            {
                                ++var;
                            }
                            else if (var2.isFlowElseif() || var2.isFlowElse() || var2.isFlowEndif())
                            {
                                if (var == 0)
                                {
                                    return var13;
                                }

                                if (var2.isFlowEndif())
                                {
                                    --var;
                                }
                            }
                        }
                    }
                }
            }
            else if (this.isFlowFor())
            {
                var7 = this.evalFlowFor();

                if (!var7)
                {
                    var = 0;

                    for (var13 = id + 1; var13 < prog.getTasks().size(); ++var13)
                    {
                        var2 = (ComputerTask)prog.getTasks().get(var13);

                        if (var2.isFlowFor())
                        {
                            ++var;
                        }
                        else if (var2.isFlowEndfor())
                        {
                            if (var == 0)
                            {
                                return var13;
                            }

                            --var;
                        }
                    }
                }
            }
            else if (!this.isFlowContinue() && !this.isFlowBreak())
            {
                if (isVar(this.getType()) && !this.isVarEmpty())
                {
                    ComputerVar var11 = this.getVarVar();

                    if (var11 != null)
                    {
                        ComputerVar var15;

                        if (this.getVarUseFirstVar())
                        {
                            var15 = this.getVarFirstVar();

                            if (var15 == null)
                            {
                                return -1;
                            }

                            var = var15.getByteValue();
                        }
                        else
                        {
                            var = this.getVarFirstInteger();
                        }

                        if (this.hasTwoValues())
                        {
                            if (this.getVarUseSecondVar())
                            {
                                var15 = this.getVarSecondVar();

                                if (var15 == null)
                                {
                                    return -1;
                                }

                                var13 = var15.getByteValue();
                            }
                            else
                            {
                                var13 = this.getVarSecondInteger();
                            }
                        }
                        else
                        {
                            var13 = 0;
                        }

                        var11.setByteValue(this.calcVarValue(var, var13));
                    }
                }
                else if (isControl(this.getType()) && !this.isControlEmpty())
                {
                    ComputerControl var10 = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)this.getControlType()));

                    if (var10 != null && var10.isControlValid(this.module.getCart()))
                    {
                        if (this.getControlUseVar())
                        {
                            ComputerVar var14 = this.getControlVar();

                            if (var14 == null)
                            {
                                return -1;
                            }

                            var = var14.getByteValue();
                        }
                        else
                        {
                            var = this.getControlInteger();
                        }

                        var10.runHandler(this.module.getCart(), (byte)var);
                    }
                }
                else
                {
                    ComputerVar var12;

                    if (isInfo(this.getType()) && !this.isInfoEmpty())
                    {
                        ComputerInfo var9 = (ComputerInfo)ComputerInfo.getMap().get(Byte.valueOf((byte)this.getControlType()));

                        if (var9 != null && var9.isInfoValid(this.module.getCart()))
                        {
                            var12 = this.getInfoVar();

                            if (var12 != null)
                            {
                                var9.getHandler(this.module.getCart(), var12);
                            }
                        }
                    }
                    else if (isVar(this.getType()))
                    {
                        Iterator var8 = prog.getVars().iterator();

                        while (var8.hasNext())
                        {
                            var12 = (ComputerVar)var8.next();
                            System.out.println(var12.getFullInfo());
                        }
                    }
                }
            }
            else
            {
                i$ = 0;

                for (var = id + 1; var < prog.getTasks().size(); ++var)
                {
                    var1 = (ComputerTask)prog.getTasks().get(var);

                    if (!var1.isFlowWhile() && !var1.isFlowFor())
                    {
                        if (var1.isFlowEndwhile() || var1.isFlowEndfor())
                        {
                            if (i$ == 0)
                            {
                                if (this.isFlowContinue())
                                {
                                    return var1.preload(prog, var);
                                }

                                return var;
                            }

                            --i$;
                        }
                    }
                    else
                    {
                        ++i$;
                    }
                }
            }
        }

        return -1;
    }

    public int preload(ComputerProg prog, int id)
    {
        int nested;
        int i;
        ComputerTask task;

        if (!this.isFlowElseif() && !this.isFlowElse())
        {
            if (this.isFlowEndwhile())
            {
                nested = 0;

                for (i = id - 1; i >= 0; --i)
                {
                    task = (ComputerTask)prog.getTasks().get(i);

                    if (task.isFlowEndwhile())
                    {
                        ++nested;
                    }
                    else if (task.isFlowWhile())
                    {
                        if (nested == 0)
                        {
                            return i;
                        }

                        --nested;
                    }
                }
            }
            else if (this.isFlowFor())
            {
                ComputerVar var8 = this.getFlowForVar();

                if (var8 != null)
                {
                    if (this.getFlowForUseStartVar())
                    {
                        ComputerVar var9 = this.getFlowForStartVar();

                        if (var9 != null)
                        {
                            var8.setByteValue(var9.getByteValue());
                        }
                    }
                    else
                    {
                        var8.setByteValue(this.getFlowForStartInteger());
                    }
                }
            }
            else if (this.isFlowEndfor())
            {
                System.out.println("End for");
                nested = 0;

                for (i = id - 1; i >= 0; --i)
                {
                    task = (ComputerTask)prog.getTasks().get(i);

                    if (task.isFlowEndfor())
                    {
                        ++nested;
                    }
                    else if (task.isFlowFor())
                    {
                        if (nested == 0)
                        {
                            ComputerVar var = task.getFlowForVar();

                            if (var != null)
                            {
                                int dif = task.getFlowForDecrease() ? -1 : 1;
                                var.setByteValue(var.getByteValue() + dif);
                            }

                            return i;
                        }

                        --nested;
                    }
                }
            }
        }
        else
        {
            nested = 0;

            for (i = id + 1; i < prog.getTasks().size(); ++i)
            {
                task = (ComputerTask)prog.getTasks().get(i);

                if (task.isFlowIf())
                {
                    ++nested;
                }
                else if (task.isFlowEndif())
                {
                    if (nested == 0)
                    {
                        return i;
                    }

                    --nested;
                }
            }
        }

        return id;
    }

    public ComputerTask clone()
    {
        ComputerTask clone = new ComputerTask(this.module, this.prog);
        clone.info = this.info;
        return clone;
    }

    public ComputerProg getProgram()
    {
        return this.prog;
    }

    public void setInfo(int id, short val)
    {
        int iVal = val;

        if (val < 0)
        {
            iVal = val + 65536;
        }

        boolean oldVal = this.getIsActivated();
        this.info &= ~(65535 << id * 16);
        this.info |= iVal << id * 16;

        if (oldVal != this.getIsActivated())
        {
            this.module.activationChanged();
        }
    }

    public short getInfo(int id)
    {
        return (short)((this.info & 65535 << id * 16) >> id * 16);
    }

    public void setIsActivated(boolean val)
    {
        boolean oldVal = this.getIsActivated();
        this.info &= -2;
        this.info |= val ? 1 : 0;

        if (oldVal != val)
        {
            this.module.activationChanged();
        }
    }

    public boolean getIsActivated()
    {
        return (this.info & 1) != 0;
    }

    public void setType(int type)
    {
        int oldType = this.getType();
        boolean flag = isBuild(oldType);
        this.info &= -15;
        this.info |= type << 1;

        if (oldType != type && (!flag || !isBuild(type)))
        {
            this.info &= 15;
        }
    }

    public int getType()
    {
        return (this.info & 14) >> 1;
    }

    public static boolean isEmpty(int type)
    {
        return type == 0;
    }

    public static boolean isFlow(int type)
    {
        return type == 1;
    }

    public static boolean isVar(int type)
    {
        return type == 2;
    }

    public static boolean isControl(int type)
    {
        return type == 3;
    }

    public static boolean isInfo(int type)
    {
        return type == 4;
    }

    public static boolean isBuild(int type)
    {
        return type == 5 || isAddon(type);
    }

    public static boolean isAddon(int type)
    {
        return type == 6;
    }

    public int getImage()
    {
        return isEmpty(this.getType()) ? -1 : (isFlow(this.getType()) ? this.getFlowImageForTask() : (isVar(this.getType()) ? getVarImage(this.getVarType()) : (isControl(this.getType()) ? getControlImage(this.getControlType()) : (isInfo(this.getType()) ? getInfoImage(this.getInfoType()) : -1))));
    }

    public static String getTypeName(int type)
    {
        switch (type)
        {
            case 1:
                return "Flow Control";

            case 2:
                return "Variable Control";

            case 3:
                return "Module Control";

            case 4:
                return "Module Info";

            case 5:
                return "Builder";

            case 6:
                return "Addon";

            default:
                return "Empty";
        }
    }

    public String toString()
    {
        return isEmpty(this.getType()) ? "Empty" : (isFlow(this.getType()) ? getFlowTypeName(this.getFlowType()) + " " + this.getFlowText() : (isVar(this.getType()) ? getVarTypeName(this.getVarType()) + ": " + this.getVarText() : (isControl(this.getType()) ? "Set " + getControlTypeName(this.getControlType()) + " to " + this.getControlText() : (isInfo(this.getType()) ? "Set " + getVarName(this.getInfoVar()) + " to " + getInfoTypeName(this.getInfoType()) : "Unknown"))));
    }

    public int getFlowType()
    {
        return (this.info & 240) >> 4;
    }

    public void setFlowType(int type)
    {
        int oldType = this.getFlowType();

        if (oldType != type)
        {
            boolean conditionFlag = this.isFlowCondition();
            this.info &= -241;
            this.info |= type << 4;

            if (!conditionFlag || !this.isFlowCondition())
            {
                this.info &= 255;
            }
        }
    }

    public boolean isFlowEmpty()
    {
        return isFlow(this.getType()) && this.getFlowType() == 0;
    }

    public boolean isFlowLabel()
    {
        return isFlow(this.getType()) && this.getFlowType() == 1;
    }

    public boolean isFlowGoto()
    {
        return isFlow(this.getType()) && this.getFlowType() == 2;
    }

    public boolean isFlowIf()
    {
        return isFlow(this.getType()) && this.getFlowType() == 3;
    }

    public boolean isFlowElseif()
    {
        return isFlow(this.getType()) && this.getFlowType() == 4;
    }

    public boolean isFlowElse()
    {
        return isFlow(this.getType()) && this.getFlowType() == 5;
    }

    public boolean isFlowWhile()
    {
        return isFlow(this.getType()) && this.getFlowType() == 6;
    }

    public boolean isFlowFor()
    {
        return isFlow(this.getType()) && this.getFlowType() == 7;
    }

    public boolean isFlowEnd()
    {
        return isFlow(this.getType()) && this.getFlowType() == 8;
    }

    public boolean isFlowBreak()
    {
        return isFlow(this.getType()) && this.getFlowType() == 9;
    }

    public boolean isFlowContinue()
    {
        return isFlow(this.getType()) && this.getFlowType() == 10;
    }

    public boolean isFlowCondition()
    {
        return this.isFlowIf() || this.isFlowElseif() || this.isFlowWhile();
    }

    public static int getFlowImage(int type)
    {
        return 12 + type;
    }

    public int getFlowImageForTask()
    {
        return this.isFlowEnd() ? getEndImage(this.getFlowEndType()) : getFlowImage(this.getFlowType());
    }

    public static String getFlowTypeName(int type)
    {
        switch (type)
        {
            case 1:
                return "Label";

            case 2:
                return "GoTo";

            case 3:
                return "If";

            case 4:
                return "Else if";

            case 5:
                return "Else";

            case 6:
                return "While";

            case 7:
                return "For";

            case 8:
                return "End";

            case 9:
                return "Break";

            case 10:
                return "Continue";

            default:
                return "Empty";
        }
    }

    public String getFlowText()
    {
        if (!this.isFlowLabel() && !this.isFlowGoto())
        {
            if (this.isFlowCondition())
            {
                ComputerVar str2 = this.getFlowConditionVar();
                String str1 = getVarName(str2);
                str1 = str1 + " ";
                str1 = str1 + getFlowOperatorName(this.getFlowConditionOperator(), false);
                str1 = str1 + " ";

                if (this.getFlowConditionUseSecondVar())
                {
                    ComputerVar var2 = this.getFlowConditionSecondVar();
                    str1 = str1 + getVarName(var2);
                }
                else
                {
                    str1 = str1 + this.getFlowConditionInteger();
                }

                return str1;
            }
            else if (this.isFlowFor())
            {
                String str = getVarName(this.getFlowForVar());
                str = str + " = ";

                if (this.getFlowForUseStartVar())
                {
                    str = str + getVarName(this.getFlowForStartVar());
                }
                else
                {
                    str = str + this.getFlowForStartInteger();
                }

                str = str + " to ";

                if (this.getFlowForUseEndVar())
                {
                    str = str + getVarName(this.getFlowForEndVar());
                }
                else
                {
                    str = str + this.getFlowForEndInteger();
                }

                str = str + "  step " + (this.getFlowForDecrease() ? "-" : "+") + "1";
                return str;
            }
            else
            {
                return this.isFlowEnd() ? getEndTypeName(this.getFlowEndType()) : "(Not set)";
            }
        }
        else
        {
            return "[" + this.getFlowLabelId() + "]";
        }
    }

    public int getFlowLabelId()
    {
        return (this.info & 7936) >> 8;
    }

    public void setFlowLabelId(int id)
    {
        if (id < 0)
        {
            id = 0;
        }
        else if (id > 31)
        {
            id = 31;
        }

        this.info &= -7937;
        this.info |= id << 8;
    }

    public int getFlowConditionVarIndex()
    {
        return this.getVarIndex(8);
    }

    public ComputerVar getFlowConditionVar()
    {
        return this.getVar(8);
    }

    public void setFlowConditionVar(int val)
    {
        this.setVar(8, val);
    }

    public int getFlowConditionOperator()
    {
        return (this.info & 57344) >> 13;
    }

    public void setFlowConditionOperator(int val)
    {
        this.info &= -57345;
        this.info |= val << 13;
    }

    public boolean isFlowConditionOperatorEquals()
    {
        return this.getFlowConditionOperator() == 0;
    }

    public boolean isFlowConditionOperatorNotequals()
    {
        return this.getFlowConditionOperator() == 1;
    }

    public boolean isFlowConditionOperatorGreaterequals()
    {
        return this.getFlowConditionOperator() == 2;
    }

    public boolean isFlowConditionOperatorGreater()
    {
        return this.getFlowConditionOperator() == 3;
    }

    public boolean isFlowConditionOperatorLesserequals()
    {
        return this.getFlowConditionOperator() == 4;
    }

    public boolean isFlowConditionOperatorLesser()
    {
        return this.getFlowConditionOperator() == 5;
    }

    public boolean getFlowConditionUseSecondVar()
    {
        return this.getUseOptionalVar(16);
    }

    public void setFlowConditionUseSecondVar(boolean val)
    {
        this.setUseOptionalVar(16, val);
    }

    public int getFlowConditionInteger()
    {
        return this.getInteger(17);
    }

    public void setFlowConditionInteger(int val)
    {
        this.setInteger(17, val);
    }

    public int getFlowConditionSecondVarIndex()
    {
        return this.getVarIndex(17);
    }

    public ComputerVar getFlowConditionSecondVar()
    {
        return this.getVar(17);
    }

    public void setFlowConditionSecondVar(int val)
    {
        this.setVar(17, val);
    }

    public boolean evalFlowCondition()
    {
        if (!this.isFlowCondition())
        {
            return false;
        }
        else
        {
            ComputerVar var = this.getFlowConditionVar();

            if (var == null)
            {
                return false;
            }
            else
            {
                int varValue = var.getByteValue();
                int compareWith;

                if (this.getFlowConditionUseSecondVar())
                {
                    ComputerVar var2 = this.getFlowConditionVar();

                    if (var2 == null)
                    {
                        return false;
                    }

                    compareWith = var2.getByteValue();
                }
                else
                {
                    compareWith = this.getFlowConditionInteger();
                }

                return this.isFlowConditionOperatorEquals() ? varValue == compareWith : (this.isFlowConditionOperatorNotequals() ? varValue != compareWith : (this.isFlowConditionOperatorGreaterequals() ? varValue >= compareWith : (this.isFlowConditionOperatorGreater() ? varValue > compareWith : (this.isFlowConditionOperatorLesserequals() ? varValue <= compareWith : (this.isFlowConditionOperatorLesser() ? varValue < compareWith : false)))));
            }
        }
    }

    public static String getFlowOperatorName(int type, boolean isLong)
    {
        switch (type)
        {
            case 0:
                return isLong ? "Equals to" : "=";

            case 1:
                return isLong ? "Not equals to" : "!=";

            case 2:
                return isLong ? "Greater than or equals to" : ">=";

            case 3:
                return isLong ? "Greater than" : ">";

            case 4:
                return isLong ? "Smaller than or equals to" : "<=";

            case 5:
                return isLong ? "Smaller than" : "<";

            default:
                return isLong ? "Unknown" : "?";
        }
    }

    public int getFlowForVarIndex()
    {
        return this.getVarIndex(8);
    }

    public ComputerVar getFlowForVar()
    {
        return this.getVar(8);
    }

    public void setFlowForVar(int val)
    {
        this.setVar(8, val);
    }

    public boolean getFlowForUseStartVar()
    {
        return this.getUseOptionalVar(13);
    }

    public void setFlowForUseStartVar(boolean val)
    {
        this.setUseOptionalVar(13, val);
    }

    public int getFlowForStartInteger()
    {
        return this.getInteger(14);
    }

    public void setFlowForStartInteger(int val)
    {
        this.setInteger(14, val);
    }

    public int getFlowForStartVarIndex()
    {
        return this.getVarIndex(14);
    }

    public ComputerVar getFlowForStartVar()
    {
        return this.getVar(14);
    }

    public void setFlowForStartVar(int val)
    {
        this.setVar(14, val);
    }

    public boolean getFlowForUseEndVar()
    {
        return this.getUseOptionalVar(22);
    }

    public void setFlowForUseEndVar(boolean val)
    {
        this.setUseOptionalVar(22, val);
    }

    public int getFlowForEndInteger()
    {
        return this.getInteger(23);
    }

    public void setFlowForEndInteger(int val)
    {
        this.setInteger(23, val);
    }

    public int getFlowForEndVarIndex()
    {
        return this.getVarIndex(23);
    }

    public ComputerVar getFlowForEndVar()
    {
        return this.getVar(23);
    }

    public void setFlowForEndVar(int val)
    {
        this.setVar(23, val);
    }

    public boolean getFlowForDecrease()
    {
        return (this.info & Integer.MIN_VALUE) != 0;
    }

    public void setFlowForDecrease(boolean val)
    {
        this.info &= Integer.MAX_VALUE;
        this.info |= (val ? 1 : 0) << 31;
    }

    public boolean evalFlowFor()
    {
        if (!this.isFlowFor())
        {
            return false;
        }
        else
        {
            ComputerVar var = this.getFlowForVar();

            if (var == null)
            {
                return false;
            }
            else
            {
                int varValue = var.getByteValue();
                int compareWith;

                if (this.getFlowForUseEndVar())
                {
                    ComputerVar var2 = this.getFlowForEndVar();

                    if (var2 == null)
                    {
                        return false;
                    }

                    compareWith = var2.getByteValue();
                }
                else
                {
                    compareWith = this.getFlowForEndInteger();
                }

                return varValue != compareWith;
            }
        }
    }

    public int getFlowEndType()
    {
        return (this.info & 768) >> 8;
    }

    public void setFlowEndType(int val)
    {
        if (val < 0)
        {
            val = 0;
        }
        else if (val > 3)
        {
            val = 3;
        }

        this.info &= -769;
        this.info |= val << 8;
    }

    public boolean isFlowEndif()
    {
        return this.isFlowEnd() && this.getFlowEndType() == 1;
    }

    public boolean isFlowEndwhile()
    {
        return this.isFlowEnd() && this.getFlowEndType() == 2;
    }

    public boolean isFlowEndfor()
    {
        return this.isFlowEnd() && this.getFlowEndType() == 3;
    }

    public static String getEndTypeName(int type)
    {
        switch (type)
        {
            case 1:
                return "If";

            case 2:
                return "While";

            case 3:
                return "For";

            default:
                return "(not set)";
        }
    }

    public static int getEndImage(int type)
    {
        return type == 0 ? 20 : 45 + type;
    }

    public int getVarType()
    {
        return (this.info & 496) >> 4;
    }

    public void setVarType(int val)
    {
        this.info &= -497;
        this.info |= val << 4;
    }

    public boolean isVarEmpty()
    {
        return isVar(this.getType()) && this.getVarType() == 0;
    }

    public boolean isVarSet()
    {
        return isVar(this.getType()) && this.getVarType() == 1;
    }

    public boolean isVarAdd()
    {
        return isVar(this.getType()) && this.getVarType() == 2;
    }

    public boolean isVarSub()
    {
        return isVar(this.getType()) && this.getVarType() == 3;
    }

    public boolean isVarMult()
    {
        return isVar(this.getType()) && this.getVarType() == 4;
    }

    public boolean isVarDiv()
    {
        return isVar(this.getType()) && this.getVarType() == 5;
    }

    public boolean isVarMod()
    {
        return isVar(this.getType()) && this.getVarType() == 6;
    }

    public boolean isVarAnd()
    {
        return isVar(this.getType()) && this.getVarType() == 7;
    }

    public boolean isVarOr()
    {
        return isVar(this.getType()) && this.getVarType() == 8;
    }

    public boolean isVarXor()
    {
        return isVar(this.getType()) && this.getVarType() == 9;
    }

    public boolean isVarNot()
    {
        return isVar(this.getType()) && this.getVarType() == 10;
    }

    public boolean isVarShiftR()
    {
        return isVar(this.getType()) && this.getVarType() == 11;
    }

    public boolean isVarShiftL()
    {
        return isVar(this.getType()) && this.getVarType() == 12;
    }

    public boolean isVarMax()
    {
        return isVar(this.getType()) && this.getVarType() == 13;
    }

    public boolean isVarMin()
    {
        return isVar(this.getType()) && this.getVarType() == 14;
    }

    public boolean isVarAbs()
    {
        return isVar(this.getType()) && this.getVarType() == 15;
    }

    public boolean isVarClamp()
    {
        return isVar(this.getType()) && this.getVarType() == 16;
    }

    public boolean isVarRand()
    {
        return isVar(this.getType()) && this.getVarType() == 17;
    }

    public boolean hasOneValue()
    {
        return this.isVarSet() || this.isVarNot() || this.isVarAbs();
    }

    public boolean hasTwoValues()
    {
        return !this.isVarEmpty() && !this.hasOneValue();
    }

    public int getVarVarIndex()
    {
        return this.getVarIndex(9);
    }

    public ComputerVar getVarVar()
    {
        return this.getVar(9);
    }

    public void setVarVar(int val)
    {
        this.setVar(9, val);
    }

    public boolean getVarUseFirstVar()
    {
        return this.getUseOptionalVar(14);
    }

    public void setVarUseFirstVar(boolean val)
    {
        this.setUseOptionalVar(14, val);
    }

    public int getVarFirstInteger()
    {
        return this.getInteger(15);
    }

    public void setVarFirstInteger(int val)
    {
        this.setInteger(15, val);
    }

    public int getVarFirstVarIndex()
    {
        return this.getVarIndex(15);
    }

    public ComputerVar getVarFirstVar()
    {
        return this.getVar(15);
    }

    public void setVarFirstVar(int val)
    {
        this.setVar(15, val);
    }

    public boolean getVarUseSecondVar()
    {
        return this.getUseOptionalVar(23);
    }

    public void setVarUseSecondVar(boolean val)
    {
        this.setUseOptionalVar(23, val);
    }

    public int getVarSecondInteger()
    {
        return this.getInteger(24);
    }

    public void setVarSecondInteger(int val)
    {
        this.setInteger(24, val);
    }

    public int getVarSecondVarIndex()
    {
        return this.getVarIndex(24);
    }

    public ComputerVar getVarSecondVar()
    {
        return this.getVar(24);
    }

    public void setVarSecondVar(int val)
    {
        this.setVar(24, val);
    }

    public static String getVarTypeName(int type)
    {
        switch (type)
        {
            case 1:
                return "Set";

            case 2:
                return "Addition";

            case 3:
                return "Subtraction";

            case 4:
                return "Multiplication";

            case 5:
                return "Integer division";

            case 6:
                return "Modulus";

            case 7:
                return "Bitwise And";

            case 8:
                return "Bitwise Or";

            case 9:
                return "Bitwise Xor";

            case 10:
                return "Bitwise Not";

            case 11:
                return "Right Bitshift";

            case 12:
                return "Left Bitshift";

            case 13:
                return "Maximum Value";

            case 14:
                return "Minimum Value";

            case 15:
                return "Absolute Value";

            case 16:
                return "Clamp Value";

            case 17:
                return "Random Value";

            default:
                return "Empty";
        }
    }

    public String getVarPrefix()
    {
        return this.isVarMax() ? "max(" : (this.isVarMin() ? "min(" : (this.isVarClamp() ? "clamp(" + getVarName(this.getVarVar()) + ", " : (this.isVarAbs() ? "abs(" : (this.isVarNot() ? "~" : (this.isVarRand() ? "rand(" : "")))));
    }

    public String getVarMidfix()
    {
        return !this.isVarMax() && !this.isVarMin() && !this.isVarClamp() && !this.isVarRand() ? (this.isVarAdd() ? " + " : (this.isVarSub() ? " - " : (this.isVarMult() ? " * " : (this.isVarDiv() ? " / " : (this.isVarMod() ? " % " : (this.isVarAnd() ? " & " : (this.isVarOr() ? " | " : (this.isVarXor() ? " ^ " : (this.isVarShiftR() ? " >> " : (this.isVarShiftL() ? " << " : "")))))))))) : ", ";
    }

    public String getVarPostfix()
    {
        return !this.isVarMax() && !this.isVarMin() && !this.isVarClamp() && !this.isVarAbs() && !this.isVarRand() ? "" : ")";
    }

    public String getVarText()
    {
        if (this.isVarEmpty())
        {
            return "(Not set)";
        }
        else
        {
            String str = "";
            str = str + getVarName(this.getVarVar());
            str = str + " = ";
            str = str + this.getVarPrefix();

            if (this.getVarUseFirstVar())
            {
                str = str + getVarName(this.getVarFirstVar());
            }
            else
            {
                str = str + this.getVarFirstInteger();
            }

            if (this.hasTwoValues())
            {
                str = str + this.getVarMidfix();

                if (this.getVarUseSecondVar())
                {
                    str = str + getVarName(this.getVarSecondVar());
                }
                else
                {
                    str = str + this.getVarSecondInteger();
                }
            }

            str = str + this.getVarPostfix();
            return str;
        }
    }

    public static int getVarImage(int type)
    {
        return type == 17 ? 98 : 49 + type;
    }

    public int calcVarValue(int val1, int val2)
    {
        if (this.isVarSet())
        {
            return val1;
        }
        else if (this.isVarAdd())
        {
            return val1 + val2;
        }
        else if (this.isVarSub())
        {
            return val1 - val2;
        }
        else if (this.isVarMult())
        {
            return val1 * val2;
        }
        else if (this.isVarDiv())
        {
            return val1 / val2;
        }
        else if (this.isVarMod())
        {
            return val1 % val2;
        }
        else if (this.isVarAnd())
        {
            return val1 & val2;
        }
        else if (this.isVarOr())
        {
            return val1 | val2;
        }
        else if (this.isVarXor())
        {
            return val1 ^ val2;
        }
        else if (this.isVarNot())
        {
            byte var4 = (byte)val1;
            var4 = (byte)(~var4);
            return var4;
        }
        else if (this.isVarShiftR())
        {
            val2 = Math.max(val2, 8);
            val2 = Math.min(val2, 0);
            return val1 >> val2;
        }
        else if (this.isVarShiftL())
        {
            val2 = Math.max(val2, 8);
            val2 = Math.min(val2, 0);
            return val1 << val2;
        }
        else if (this.isVarMax())
        {
            return Math.max(val1, val2);
        }
        else if (this.isVarMin())
        {
            return Math.min(val1, val2);
        }
        else if (this.isVarAbs())
        {
            return Math.abs(val1);
        }
        else if (this.isVarClamp())
        {
            int temp = this.getVarVar().getByteValue();
            temp = Math.max(temp, val1);
            temp = Math.min(temp, val2);
            return temp;
        }
        else if (this.isVarRand())
        {
            ++val2;
            return val2 <= val1 ? 0 : rand.nextInt(val2 - val1) + val1;
        }
        else
        {
            return 0;
        }
    }

    public int getControlType()
    {
        return (this.info & 4080) >> 4;
    }

    public void setControlType(int val)
    {
        this.info &= -4081;
        this.info |= val << 4;

        if (!this.getControlUseVar())
        {
            int min = this.getControlMinInteger();
            int max = this.getControlMaxInteger();

            if (this.getControlInteger() < min)
            {
                this.setControlInteger(min);
            }
            else if (this.getControlInteger() > max)
            {
                this.setControlInteger(max);
            }
        }
    }

    public boolean isControlEmpty()
    {
        return this.getControlType() == 0;
    }

    public static String getControlTypeName(int type)
    {
        if (type == 0)
        {
            return "Empty";
        }
        else
        {
            ComputerControl control = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)type));
            return control == null ? "(not set)" : control.getName();
        }
    }

    public static int getControlImage(int type)
    {
        if (type == 0)
        {
            return 68;
        }
        else
        {
            ComputerControl control = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)type));
            return control == null ? -1 : control.getTexture();
        }
    }

    public String getControlText()
    {
        if (this.isControlEmpty())
        {
            return "(not set)";
        }
        else if (!this.isControlActivator())
        {
            if (this.getControlUseVar())
            {
                ComputerVar var = this.getControlVar();
                return getVarName(var);
            }
            else
            {
                return String.valueOf(this.getControlInteger());
            }
        }
        else
        {
            return "Activate";
        }
    }

    public boolean getControlUseVar()
    {
        return this.getUseOptionalVar(12);
    }

    public void setControlUseVar(boolean val)
    {
        this.setUseOptionalVar(12, val);
    }

    public int getControlInteger()
    {
        return this.getInteger(13);
    }

    public void setControlInteger(int val)
    {
        this.setInteger(13, val);
    }

    public int getControlVarIndex()
    {
        return this.getVarIndex(13);
    }

    public ComputerVar getControlVar()
    {
        return this.getVar(13);
    }

    public void setControlVar(int val)
    {
        this.setVar(13, val);
    }

    public int getControlMinInteger()
    {
        ComputerControl control = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)this.getControlType()));
        return control == null ? -128 : control.getIntegerMin();
    }

    public int getControlMaxInteger()
    {
        ComputerControl control = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)this.getControlType()));
        return control == null ? 127 : control.getIntegerMax();
    }

    public boolean getControlUseBigInteger(int size)
    {
        ComputerControl control = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)this.getControlType()));
        return control == null ? false : control.useIntegerOfSize(size);
    }

    public boolean isControlActivator()
    {
        ComputerControl control = (ComputerControl)ComputerControl.getMap().get(Byte.valueOf((byte)this.getControlType()));
        return control == null ? false : control.isActivator();
    }

    public int getInfoType()
    {
        return (this.info & 4080) >> 4;
    }

    public void setInfoType(int val)
    {
        this.info &= -4081;
        this.info |= val << 4;
    }

    public boolean isInfoEmpty()
    {
        return this.getInfoType() == 0;
    }

    public static String getInfoTypeName(int type)
    {
        if (type == 0)
        {
            return "Empty";
        }
        else
        {
            ComputerInfo info = (ComputerInfo)ComputerInfo.getMap().get(Byte.valueOf((byte)type));
            return info == null ? "(not set)" : info.getName();
        }
    }

    public static int getInfoImage(int type)
    {
        if (type == 0)
        {
            return 83;
        }
        else
        {
            ComputerInfo info = (ComputerInfo)ComputerInfo.getMap().get(Byte.valueOf((byte)type));
            return info == null ? -1 : info.getTexture();
        }
    }

    public int getInfoVarIndex()
    {
        return this.getVarIndex(12);
    }

    public ComputerVar getInfoVar()
    {
        return this.getVar(12);
    }

    public void setInfoVar(int val)
    {
        this.setVar(12, val);
    }

    private static String getVarName(ComputerVar var)
    {
        return var == null ? "(not set)" : var.getText();
    }

    private int getInteger(int startBit)
    {
        int val = (this.info & 255 << startBit) >> startBit;
        return val > 127 ? val - 255 : val;
    }

    private void setInteger(int startBit, int val)
    {
        if (val < -128)
        {
            val = -128;
        }
        else if (val > 127)
        {
            val = 127;
        }

        if (val < 0)
        {
            val += 256;
        }

        this.info &= ~(255 << startBit);
        this.info |= val << startBit;
    }

    private boolean getUseOptionalVar(int startBit)
    {
        return (this.info & 1 << startBit) != 0;
    }

    private void setUseOptionalVar(int startBit, boolean val)
    {
        if (val != this.getUseOptionalVar(startBit))
        {
            this.info &= ~(1 << startBit);
            this.info |= (val ? 1 : 0) << startBit;
            this.setInteger(startBit + 1, 0);
        }
    }

    private int getVarIndex(int startBit)
    {
        return ((this.info & 31 << startBit) >> startBit) - 1;
    }

    public ComputerVar getVar(int startBit)
    {
        int ind = this.getVarIndex(startBit);
        return ind >= 0 && ind < this.prog.getVars().size() ? (ComputerVar)this.prog.getVars().get(ind) : null;
    }

    public void setVar(int startBit, int val)
    {
        if (val < -1)
        {
            val = -1;
        }
        else if (val >= this.prog.getVars().size())
        {
            val = this.prog.getVars().size() - 2;
        }

        ++val;
        this.info &= ~(31 << startBit);
        this.info |= val << startBit;
    }
}
