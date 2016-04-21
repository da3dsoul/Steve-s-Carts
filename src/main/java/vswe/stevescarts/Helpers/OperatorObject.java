package vswe.stevescarts.Helpers;

import java.util.Collection;
import java.util.HashMap;
import net.minecraft.tileentity.TileEntity;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.TileEntities.TileEntityDetector;

public class OperatorObject
{
    private static HashMap<Byte, OperatorObject> allOperators = new HashMap();
    private byte ID;
    private Localization.GUI.DETECTOR name;
    private int childs;

    public static Collection<OperatorObject> getOperatorList(int meta)
    {
        return DetectorType.getTypeFromMeta(meta).getOperators().values();
    }

    public static HashMap<Byte, OperatorObject> getAllOperators()
    {
        return allOperators;
    }

    public OperatorObject(HashMap<Byte, OperatorObject> operators, int ID, Localization.GUI.DETECTOR name, int childs)
    {
        this.ID = (byte)ID;
        this.name = name;
        this.childs = childs;
        operators.put(Byte.valueOf(this.ID), this);
        allOperators.put(Byte.valueOf(this.ID), this);
    }

    public byte getID()
    {
        return this.ID;
    }

    public String getName()
    {
        return this.name.translate(new String[0]);
    }

    public int getChildCount()
    {
        return this.childs;
    }

    public boolean inTab()
    {
        return true;
    }

    public boolean isChildValid(OperatorObject child)
    {
        return true;
    }

    public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
    {
        return false;
    }

    static
    {
        final HashMap operators = new HashMap();
        OperatorObject var10001 = new OperatorObject(operators, 0, Localization.GUI.DETECTOR.OUTPUT, 1)
        {
            public boolean inTab()
            {
                return false;
            }
            public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
            {
                return A.evaluateLogicTree(detector, cart, depth);
            }
        };
        var10001 = new OperatorObject(operators, 1, Localization.GUI.DETECTOR.AND, 2)
        {
            public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
            {
                return A.evaluateLogicTree(detector, cart, depth) && B.evaluateLogicTree(detector, cart, depth);
            }
        };
        var10001 = new OperatorObject(operators, 2, Localization.GUI.DETECTOR.OR, 2)
        {
            public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
            {
                return A.evaluateLogicTree(detector, cart, depth) || B.evaluateLogicTree(detector, cart, depth);
            }
        };
        var10001 = new OperatorObject(operators, 3, Localization.GUI.DETECTOR.NOT, 1)
        {
            public boolean isChildValid(OperatorObject child)
            {
                return this.getID() != child.ID;
            }
            public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
            {
                return !A.evaluateLogicTree(detector, cart, depth);
            }
        };
        var10001 = new OperatorObject(operators, 4, Localization.GUI.DETECTOR.XOR, 2)
        {
            public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
            {
                return A.evaluateLogicTree(detector, cart, depth) != B.evaluateLogicTree(detector, cart, depth);
            }
        };
        new OperatorObject.OperatorObjectRedirector(operators, 5, Localization.GUI.DETECTOR.TOP, 0, 1, 0);
        new OperatorObject.OperatorObjectRedirector(operators, 6, Localization.GUI.DETECTOR.BOT, 0, -1, 0);
        new OperatorObject.OperatorObjectRedirector(operators, 7, Localization.GUI.DETECTOR.NORTH, 0, 0, -1);
        new OperatorObject.OperatorObjectRedirector(operators, 8, Localization.GUI.DETECTOR.WEST, -1, 0, 0);
        new OperatorObject.OperatorObjectRedirector(operators, 9, Localization.GUI.DETECTOR.SOUTH, 0, 0, 1);
        new OperatorObject.OperatorObjectRedirector(operators, 10, Localization.GUI.DETECTOR.EAST, 1, 0, 0);
        DetectorType[] arr$ = DetectorType.values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            DetectorType type = arr$[i$];
            type.initOperators(new HashMap(operators));
        }
    }

    public static class OperatorObjectRedirector extends OperatorObject
    {
        private int x;
        private int y;
        private int z;

        public OperatorObjectRedirector(HashMap<Byte, OperatorObject> operators, int ID, Localization.GUI.DETECTOR name, int x, int y, int z)
        {
            super(operators, ID, name, 0);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
        {
            int x = this.x + detector.xCoord;
            int y = this.y + detector.yCoord;
            int z = this.z + detector.zCoord;
            TileEntity tileentity = detector.getWorldObj().getTileEntity(x, y, z);
            return tileentity != null && tileentity instanceof TileEntityDetector ? ((TileEntityDetector)tileentity).evaluate(cart, depth) : false;
        }
    }

    public static class OperatorObjectRedstone extends OperatorObject
    {
        private int x;
        private int y;
        private int z;

        public OperatorObjectRedstone(HashMap<Byte, OperatorObject> operators, int ID, Localization.GUI.DETECTOR name, int x, int y, int z)
        {
            super(operators, ID, name, 0);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B)
        {
            int x = this.x + detector.xCoord;
            int y = this.y + detector.yCoord;
            int z = this.z + detector.zCoord;

            if (this.x == 0 && this.y == 0 && this.z == 0)
            {
                return detector.getWorldObj().isBlockIndirectlyGettingPowered(x, y, z);
            }
            else
            {
                byte direction;

                if (this.y > 0)
                {
                    direction = 0;
                }
                else if (this.y < 0)
                {
                    direction = 1;
                }
                else if (this.x > 0)
                {
                    direction = 4;
                }
                else if (this.x < 0)
                {
                    direction = 5;
                }
                else if (this.z > 0)
                {
                    direction = 2;
                }
                else
                {
                    direction = 3;
                }

                return detector.getWorldObj().getIndirectPowerLevelTo(x, y, z, direction) > 0;
            }
        }
    }
}
