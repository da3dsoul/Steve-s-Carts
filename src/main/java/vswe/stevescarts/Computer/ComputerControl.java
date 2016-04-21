package vswe.stevescarts.Computer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import vswe.stevescarts.Buttons.ButtonBase;
import vswe.stevescarts.Buttons.ButtonControlType;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleColorizer;
import vswe.stevescarts.Modules.Addons.ModuleHeightControl;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Realtimers.ModuleDynamite;
import vswe.stevescarts.Modules.Realtimers.ModuleFirework;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ComputerControl
{
    private static HashMap<Byte, ComputerControl> controls = new HashMap();
    private Class <? extends ModuleBase > moduleClass;
    private byte id;
    private String name;
    private int texture;

    public static HashMap<Byte, ComputerControl> getMap()
    {
        return controls;
    }

    public static Collection<ComputerControl> getList()
    {
        return controls.values();
    }

    public static void createButtons(MinecartModular cart, ModuleComputer assembly)
    {
        Iterator i$ = getList().iterator();

        while (i$.hasNext())
        {
            ComputerControl control = (ComputerControl)i$.next();

            if (control.isControlValid(cart))
            {
                new ButtonControlType(assembly, ButtonBase.LOCATION.TASK, control.id);
            }
        }
    }

    private byte clamp(byte val, int min, int max)
    {
        return (byte)Math.max((byte)min, (byte)Math.min(val, (byte)max));
    }

    public ComputerControl(int id, String name, int texture, Class <? extends ModuleBase > moduleClass)
    {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte)id;
        this.texture = texture;
        controls.put(Byte.valueOf(this.id), this);
    }

    public boolean isControlValid(MinecartModular cart)
    {
        Iterator i$ = cart.getModules().iterator();
        ModuleBase module;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            module = (ModuleBase)i$.next();
        }
        while (!this.moduleClass.isAssignableFrom(module.getClass()) || !this.isValid(module));

        return true;
    }

    public String getName()
    {
        return this.name;
    }

    public int getTexture()
    {
        return this.texture;
    }

    public void runHandler(MinecartModular cart, byte val)
    {
        Iterator i$ = cart.getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (this.moduleClass.isAssignableFrom(module.getClass()) && this.isValid(module))
            {
                this.run(module, this.clamp(val, (byte)this.getIntegerMin(), (byte)this.getIntegerMax()));
                break;
            }
        }
    }

    public int getIntegerMin()
    {
        return this.isBoolean() ? 0 : this.getMin();
    }

    public int getIntegerMax()
    {
        return this.isBoolean() ? 1 : this.getMax();
    }

    public boolean useIntegerOfSize(int size)
    {
        return !this.isBoolean() || size <= 1;
    }

    protected boolean isBoolean()
    {
        return false;
    }

    protected boolean isActivator()
    {
        return false;
    }

    protected void run(ModuleBase module, byte val) {}

    protected int getMin()
    {
        return -127;
    }

    protected int getMax()
    {
        return 128;
    }

    protected boolean isValid(ModuleBase module)
    {
        return true;
    }

    static
    {
        ComputerControl var10001 = new ComputerControl(1, "Light threshold [0-15]", 69, ModuleTorch.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleTorch)module).setThreshold(val);
            }
            protected int getMin()
            {
                return 0;
            }
            protected int getMax()
            {
                return 15;
            }
        };
        var10001 = new ComputerControl(2, "Shield", 70, ModuleShield.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleShield)module).setShieldStatus(val != 0);
            }
            protected boolean isBoolean()
            {
                return true;
            }
        };
        var10001 = new ComputerControl(3, "Drill", 71, ModuleDrill.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleDrill)module).setDrillEnabled(val != 0);
            }
            protected boolean isBoolean()
            {
                return true;
            }
        };
        var10001 = new ComputerControl(4, "Invisibility Core", 72, ModuleInvisible.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleInvisible)module).setIsVisible(val == 0);
            }
            protected boolean isBoolean()
            {
                return true;
            }
        };
        var10001 = new ComputerControl(5, "Chunk loader", 73, ModuleChunkLoader.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleChunkLoader)module).setChunkLoading(val != 0);
            }
            protected boolean isBoolean()
            {
                return true;
            }
        };
        var10001 = new ComputerControl(6, "Fuse length [2-127]", 74, ModuleDynamite.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleDynamite)module).setFuseLength(val);
            }
            protected int getMin()
            {
                return 2;
            }
        };
        var10001 = new ComputerControl(7, "Prime", 75, ModuleDynamite.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleDynamite)module).prime();
            }
            protected boolean isActivator()
            {
                return true;
            }
        };
        var10001 = new ComputerControl(8, "Active pipes", 76, ModuleShooter.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleShooter)module).setActivePipes(val);
            }
            protected boolean isValid(ModuleBase module)
            {
                return !(module instanceof ModuleShooterAdv);
            }
        };
        var10001 = new ComputerControl(9, "Selected targets", 77, ModuleShooterAdv.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleShooterAdv)module).setOptions(val);
            }
        };
        var10001 = new ComputerControl(10, "Fire", 78, ModuleFirework.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleFirework)module).fire();
            }
            protected boolean isActivator()
            {
                return true;
            }
        };
        var10001 = new ComputerControl(11, "Red [0-64]", 79, ModuleColorizer.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleColorizer)module).setColorVal(0, (byte)Math.min(val * 4, 255));
            }
            protected int getMin()
            {
                return 0;
            }
            protected int getMax()
            {
                return 64;
            }
        };
        var10001 = new ComputerControl(12, "Green [0-64]", 80, ModuleColorizer.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleColorizer)module).setColorVal(1, (byte)Math.min(val * 4, 255));
            }
            protected int getMin()
            {
                return 0;
            }
            protected int getMax()
            {
                return 64;
            }
        };
        var10001 = new ComputerControl(13, "Blue [0-64]", 81, ModuleColorizer.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleColorizer)module).setColorVal(2, (byte)Math.min(val * 4, 255));
            }
            protected int getMin()
            {
                return 0;
            }
            protected int getMax()
            {
                return 64;
            }
        };
        var10001 = new ComputerControl(14, "Y target [-128-127]", 85, ModuleHeightControl.class)
        {
            protected void run(ModuleBase module, byte val)
            {
                ((ModuleHeightControl)module).setYTarget(val + 128);
            }
        };
    }
}
