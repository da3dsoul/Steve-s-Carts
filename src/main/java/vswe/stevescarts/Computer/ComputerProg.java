package vswe.stevescarts.Computer;

import java.util.ArrayList;
import vswe.stevescarts.Modules.Workers.ModuleComputer;

public class ComputerProg
{
    private ModuleComputer module;
    private int activeTaskId;
    private ArrayList<ComputerTask> tasks;
    private ArrayList<ComputerVar> vars;
    private short info;
    private String myName;

    public ComputerProg(ModuleComputer module)
    {
        this.module = module;
        this.tasks = new ArrayList();
        this.vars = new ArrayList();
        this.info = 1;
    }

    public void start()
    {
        this.module.setActiveProgram(this);

        if (this.activeTaskId >= 0 && this.activeTaskId < this.tasks.size())
        {
            this.activeTaskId = ((ComputerTask)this.tasks.get(this.activeTaskId)).preload(this, this.activeTaskId);
        }
        else
        {
            this.activeTaskId = 0;
        }
    }

    public int getActiveId()
    {
        return this.activeTaskId;
    }

    public void setActiveId(int val)
    {
        this.activeTaskId = val;
    }

    public int getRunTime()
    {
        if (this.activeTaskId >= 0 && this.activeTaskId < this.tasks.size())
        {
            return ((ComputerTask)this.tasks.get(this.activeTaskId)).getTime();
        }
        else
        {
            this.activeTaskId = 0;
            return 0;
        }
    }

    public boolean run()
    {
        if (this.activeTaskId >= 0 && this.activeTaskId < this.tasks.size())
        {
            int result = ((ComputerTask)this.tasks.get(this.activeTaskId)).run(this, this.activeTaskId);

            if (result == -1)
            {
                ++this.activeTaskId;
            }
            else
            {
                this.activeTaskId = result;
            }

            if (this.activeTaskId >= 0 && this.activeTaskId < this.tasks.size())
            {
                if (result == -1)
                {
                    this.activeTaskId = ((ComputerTask)this.tasks.get(this.activeTaskId)).preload(this, this.activeTaskId);
                }

                return true;
            }
            else
            {
                this.activeTaskId = 0;
                return false;
            }
        }
        else
        {
            this.activeTaskId = 0;
            return false;
        }
    }

    public ArrayList<ComputerTask> getTasks()
    {
        return this.tasks;
    }

    public ArrayList<ComputerVar> getVars()
    {
        return this.vars;
    }

    public void setTaskCount(int count)
    {
        while (this.tasks.size() > count)
        {
            this.tasks.remove(this.tasks.size() - 1);
        }

        while (this.tasks.size() < count)
        {
            this.tasks.add(new ComputerTask(this.module, this));
        }
    }

    public void setVarCount(int count)
    {
        while (this.vars.size() > count)
        {
            this.vars.remove(this.vars.size() - 1);
        }

        while (this.vars.size() < count)
        {
            this.vars.add(new ComputerVar(this.module));
        }
    }

    public short getInfo()
    {
        return this.info;
    }

    public void setInfo(short val)
    {
        this.info = val;
    }

    public void setName(String name)
    {
        this.myName = name;
    }

    public String getName()
    {
        return this.myName;
    }

    public String toString()
    {
        return this.getName();
    }
}
