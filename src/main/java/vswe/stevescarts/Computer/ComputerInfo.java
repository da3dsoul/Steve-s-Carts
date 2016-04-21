package vswe.stevescarts.Computer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import vswe.stevescarts.Buttons.ButtonBase;
import vswe.stevescarts.Buttons.ButtonInfoType;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleColorizer;
import vswe.stevescarts.Modules.Addons.ModuleHeightControl;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Realtimers.ModuleDynamite;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ComputerInfo
{
    private static HashMap<Byte, ComputerInfo> infos = new HashMap();
    private Class <? extends ModuleBase > moduleClass;
    private byte id;
    private String name;
    private int texture;

    public static HashMap<Byte, ComputerInfo> getMap()
    {
        return infos;
    }

    public static Collection<ComputerInfo> getList()
    {
        return infos.values();
    }

    public static void createButtons(MinecartModular cart, ModuleComputer assembly)
    {
        Iterator i$ = getList().iterator();

        while (i$.hasNext())
        {
            ComputerInfo info = (ComputerInfo)i$.next();

            if (info.isInfoValid(cart))
            {
                new ButtonInfoType(assembly, ButtonBase.LOCATION.TASK, info.id);
            }
        }
    }

    private static int processColor(int val)
    {
        return val == 255 ? 64 : val / 4;
    }

    private static byte clamp(byte val, int min, int max)
    {
        return (byte)Math.max((byte)min, (byte)Math.min(val, (byte)max));
    }

    public ComputerInfo(int id, String name, int texture, Class <? extends ModuleBase > moduleClass)
    {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte)id;
        this.texture = texture;
        infos.put(Byte.valueOf(this.id), this);
    }

    public boolean isInfoValid(MinecartModular cart)
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

    public void getHandler(MinecartModular cart, ComputerVar var)
    {
        Iterator i$ = cart.getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (this.moduleClass.isAssignableFrom(module.getClass()) && this.isValid(module))
            {
                var.setByteValue(this.get(module));
                break;
            }
        }
    }

    protected int get(ModuleBase module)
    {
        return 0;
    }

    protected boolean isValid(ModuleBase module)
    {
        return true;
    }

    static
    {
        ComputerInfo var10001 = new ComputerInfo(1, "Light threshold [0-15]", 84, ModuleTorch.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleTorch)module).getThreshold();
            }
        };
        var10001 = new ComputerInfo(2, "Light level [0-15]", 85, ModuleTorch.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleTorch)module).getLightLevel();
            }
        };
        var10001 = new ComputerInfo(3, "Shield [0-1]", 86, ModuleShield.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleShield)module).isActive(0) ? 1 : 0;
            }
        };
        var10001 = new ComputerInfo(4, "Drill [0-1]", 87, ModuleDrill.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleDrill)module).isActive(0) ? 1 : 0;
            }
        };
        var10001 = new ComputerInfo(5, "Invisibility core [0-1]", 88, ModuleInvisible.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleInvisible)module).isActive(0) ? 1 : 0;
            }
        };
        var10001 = new ComputerInfo(6, "Chunk loader [0-1]", 89, ModuleChunkLoader.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleChunkLoader)module).isActive(0) ? 1 : 0;
            }
        };
        var10001 = new ComputerInfo(7, "Fuse Length [2-127]", 90, ModuleDynamite.class)
        {
            protected int get(ModuleBase module)
            {
                return ComputerInfo.clamp((byte)((ModuleDynamite)module).getFuseLength(), 2, 127);
            }
        };
        var10001 = new ComputerInfo(8, "Active Pipes", 91, ModuleShooter.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleShooter)module).getActivePipes();
            }
            protected boolean isValid(ModuleBase module)
            {
                return !(module instanceof ModuleShooterAdv);
            }
        };
        var10001 = new ComputerInfo(9, "Selected Target", 92, ModuleShooterAdv.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleShooterAdv)module).selectedOptions();
            }
        };
        var10001 = new ComputerInfo(10, "Red [0-64]", 93, ModuleColorizer.class)
        {
            protected int get(ModuleBase module)
            {
                return ComputerInfo.processColor(((ModuleColorizer)module).getColorVal(0));
            }
        };
        var10001 = new ComputerInfo(11, "Green [0-64]", 94, ModuleColorizer.class)
        {
            protected int get(ModuleBase module)
            {
                return ComputerInfo.processColor(((ModuleColorizer)module).getColorVal(1));
            }
        };
        var10001 = new ComputerInfo(12, "Blue [0-64]", 95, ModuleColorizer.class)
        {
            protected int get(ModuleBase module)
            {
                return ComputerInfo.processColor(((ModuleColorizer)module).getColorVal(2));
            }
        };
        var10001 = new ComputerInfo(13, "Y target [-128-127]", 96, ModuleHeightControl.class)
        {
            protected int get(ModuleBase module)
            {
                return ((ModuleHeightControl)module).getYTarget() - 128;
            }
        };
        var10001 = new ComputerInfo(14, "Y level [-128-127]", 97, ModuleHeightControl.class)
        {
            protected int get(ModuleBase module)
            {
                return ComputerInfo.clamp((byte)((int)(module.getCart().posY - 128.0D)), -128, 127);
            }
        };
    }
}
