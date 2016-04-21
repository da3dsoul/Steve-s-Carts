package vswe.stevescarts.Arcade;

import java.util.ArrayList;
import vswe.stevescarts.Interfaces.GuiBase;

public abstract class TrackOrientation
{
    public static ArrayList<TrackOrientation> ALL = new ArrayList();
    public static TrackOrientation JUNCTION_4WAY = new TrackOrientation(5, GuiBase.RENDER_ROTATION.NORMAL)
    {
        public TrackOrientation.DIRECTION travel(TrackOrientation.DIRECTION in)
        {
            return in.getOpposite();
        }
    };
    public static TrackOrientation STRAIGHT_HORIZONTAL = new TrackOrientation.TrackOrientationStraight(1, GuiBase.RENDER_ROTATION.ROTATE_90, TrackOrientation.DIRECTION.RIGHT);
    public static TrackOrientation STRAIGHT_VERTICAL = new TrackOrientation.TrackOrientationStraight(1, GuiBase.RENDER_ROTATION.NORMAL, TrackOrientation.DIRECTION.DOWN);
    public static TrackOrientation CORNER_DOWN_RIGHT = new TrackOrientation.TrackOrientationCorner(0, GuiBase.RENDER_ROTATION.NORMAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.RIGHT);
    public static TrackOrientation CORNER_DOWN_LEFT = new TrackOrientation.TrackOrientationCorner(0, GuiBase.RENDER_ROTATION.ROTATE_90, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.LEFT);
    public static TrackOrientation CORNER_UP_LEFT = new TrackOrientation.TrackOrientationCorner(0, GuiBase.RENDER_ROTATION.ROTATE_180, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.LEFT);
    public static TrackOrientation CORNER_UP_RIGHT = new TrackOrientation.TrackOrientationCorner(0, GuiBase.RENDER_ROTATION.ROTATE_270, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.RIGHT);
    public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN = new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.NORMAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.DOWN.getRight());
    public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT = new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.ROTATE_270, TrackOrientation.DIRECTION.RIGHT, TrackOrientation.DIRECTION.RIGHT.getRight());
    public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP = new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.ROTATE_180, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.UP.getRight());
    public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT = new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.ROTATE_90, TrackOrientation.DIRECTION.LEFT, TrackOrientation.DIRECTION.LEFT.getRight());
    public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_DOWN = (new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.FLIP_HORIZONTAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.DOWN.getLeft())).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN);
    public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_RIGHT = (new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.ROTATE_270_FLIP, TrackOrientation.DIRECTION.RIGHT, TrackOrientation.DIRECTION.RIGHT.getLeft())).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT);
    public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_UP = (new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.FLIP_VERTICAL, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.UP.getLeft())).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP);
    public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_LEFT = (new TrackOrientation.TrackOrientation3Way(4, GuiBase.RENDER_ROTATION.ROTATE_90_FLIP, TrackOrientation.DIRECTION.LEFT, TrackOrientation.DIRECTION.LEFT.getLeft())).setOpposite(JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.NORMAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.DOWN.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.FLIP_HORIZONTAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.DOWN.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.ROTATE_180, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.UP.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.FLIP_VERTICAL, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.UP.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.ROTATE_270_FLIP, TrackOrientation.DIRECTION.RIGHT, TrackOrientation.DIRECTION.RIGHT.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.ROTATE_90, TrackOrientation.DIRECTION.LEFT, TrackOrientation.DIRECTION.LEFT.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.ROTATE_90_FLIP, TrackOrientation.DIRECTION.LEFT, TrackOrientation.DIRECTION.LEFT.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation.TrackOrientation3Way(2, GuiBase.RENDER_ROTATION.ROTATE_270, TrackOrientation.DIRECTION.RIGHT, TrackOrientation.DIRECTION.RIGHT.getOpposite());
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_RIGHT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.NORMAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.RIGHT)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_LEFT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.FLIP_HORIZONTAL, TrackOrientation.DIRECTION.DOWN, TrackOrientation.DIRECTION.LEFT)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_LEFT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.ROTATE_180, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.LEFT)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_RIGHT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.FLIP_VERTICAL, TrackOrientation.DIRECTION.UP, TrackOrientation.DIRECTION.RIGHT)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_RIGHT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.ROTATE_270_FLIP, TrackOrientation.DIRECTION.RIGHT, TrackOrientation.DIRECTION.DOWN)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_LEFT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.ROTATE_90, TrackOrientation.DIRECTION.LEFT, TrackOrientation.DIRECTION.DOWN)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_LEFT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.ROTATE_90_FLIP, TrackOrientation.DIRECTION.LEFT, TrackOrientation.DIRECTION.UP)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT);
    public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_RIGHT = (new TrackOrientation.TrackOrientation3Way(3, GuiBase.RENDER_ROTATION.ROTATE_270, TrackOrientation.DIRECTION.RIGHT, TrackOrientation.DIRECTION.UP)).setOpposite(JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT);
    private int v;
    private GuiBase.RENDER_ROTATION rotation;
    private TrackOrientation opposite;
    private int val;

    TrackOrientation(int v, GuiBase.RENDER_ROTATION rotation)
    {
        this.v = v;
        this.rotation = rotation;
        this.val = ALL.size();
        ALL.add(this);
    }

    protected TrackOrientation setOpposite(TrackOrientation opposite)
    {
        this.opposite = opposite;

        if (this.opposite.opposite != null)
        {
            this.opposite.opposite.opposite = this;
        }
        else
        {
            this.opposite.opposite = this;
        }

        return this;
    }

    public int getV()
    {
        return this.v;
    }

    public GuiBase.RENDER_ROTATION getRotation()
    {
        return this.rotation;
    }

    public TrackOrientation getOpposite()
    {
        return this.opposite;
    }

    public int toInteger()
    {
        return this.val;
    }

    public abstract TrackOrientation.DIRECTION travel(TrackOrientation.DIRECTION var1);

    static class NamelessClass492989324
    {
        static final int[] $SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION = new int[TrackOrientation.DIRECTION.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[TrackOrientation.DIRECTION.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[TrackOrientation.DIRECTION.DOWN.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[TrackOrientation.DIRECTION.LEFT.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[TrackOrientation.DIRECTION.RIGHT.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    public static enum DIRECTION
    {
        UP("UP", 0, 0, 0, -1),
        DOWN("DOWN", 1, 1, 0, 1),
        LEFT("LEFT", 2, 2, -1, 0),
        RIGHT("RIGHT", 3, 3, 1, 0),
        STILL("STILL", 4, -1, 0, 0);
        ArrayList<TrackOrientation.DIRECTION> ALL;
        private int x;
        private int y;
        private int val;

        private static final TrackOrientation.DIRECTION[] $VALUES = new TrackOrientation.DIRECTION[]{UP, DOWN, LEFT, RIGHT, STILL};

        private DIRECTION(String var1, int var2, int val, int x, int y)
        {
            this.val = val;
            this.x = x;
            this.y = y;
        }

        public int getX()
        {
            return this.x;
        }

        public int getY()
        {
            return this.y;
        }

        public TrackOrientation.DIRECTION getOpposite()
        {
            switch (TrackOrientation.NamelessClass492989324.$SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[this.ordinal()])
            {
                case 1:
                    return DOWN;

                case 2:
                    return UP;

                case 3:
                    return RIGHT;

                case 4:
                    return LEFT;

                default:
                    return STILL;
            }
        }

        public TrackOrientation.DIRECTION getLeft()
        {
            switch (TrackOrientation.NamelessClass492989324.$SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[this.ordinal()])
            {
                case 1:
                    return RIGHT;

                case 2:
                    return LEFT;

                case 3:
                    return UP;

                case 4:
                    return DOWN;

                default:
                    return STILL;
            }
        }

        public TrackOrientation.DIRECTION getRight()
        {
            switch (TrackOrientation.NamelessClass492989324.$SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[this.ordinal()])
            {
                case 1:
                    return LEFT;

                case 2:
                    return RIGHT;

                case 3:
                    return DOWN;

                case 4:
                    return UP;

                default:
                    return STILL;
            }
        }

        public GuiBase.RENDER_ROTATION getRenderRotation()
        {
            switch (TrackOrientation.NamelessClass492989324.$SwitchMap$vswe$stevescarts$Arcade$TrackOrientation$DIRECTION[this.ordinal()])
            {
                case 1:
                    return GuiBase.RENDER_ROTATION.NORMAL;

                case 2:
                    return GuiBase.RENDER_ROTATION.ROTATE_180;

                case 3:
                    return GuiBase.RENDER_ROTATION.ROTATE_270;

                case 4:
                    return GuiBase.RENDER_ROTATION.ROTATE_90;

                default:
                    return GuiBase.RENDER_ROTATION.NORMAL;
            }
        }

        public int toInteger()
        {
            return this.val;
        }

        public static TrackOrientation.DIRECTION fromInteger(int i)
        {
            TrackOrientation.DIRECTION[] arr$ = values();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                TrackOrientation.DIRECTION dir = arr$[i$];

                if (dir.val == i)
                {
                    return dir;
                }
            }

            return null;
        }
    }

    private static class TrackOrientation3Way extends TrackOrientation
    {
        private TrackOrientation.DIRECTION entrance;
        private TrackOrientation.DIRECTION active;

        TrackOrientation3Way(int v, GuiBase.RENDER_ROTATION rotation, TrackOrientation.DIRECTION entrance, TrackOrientation.DIRECTION active)
        {
            super(v, rotation);
            this.entrance = entrance;
            this.active = active;
        }

        public TrackOrientation.DIRECTION travel(TrackOrientation.DIRECTION in)
        {
            return in.equals(this.entrance) ? this.active : this.entrance;
        }
    }

    private static class TrackOrientationCorner extends TrackOrientation
    {
        private TrackOrientation.DIRECTION dir1;
        private TrackOrientation.DIRECTION dir2;

        TrackOrientationCorner(int v, GuiBase.RENDER_ROTATION rotation, TrackOrientation.DIRECTION dir1, TrackOrientation.DIRECTION dir2)
        {
            super(v, rotation);
            this.dir1 = dir1;
            this.dir2 = dir2;
        }

        public TrackOrientation.DIRECTION travel(TrackOrientation.DIRECTION in)
        {
            return in.equals(this.dir1) ? this.dir2 : (in.equals(this.dir2) ? this.dir1 : in.getOpposite());
        }
    }

    private static class TrackOrientationStraight extends TrackOrientation
    {
        private TrackOrientation.DIRECTION base;

        TrackOrientationStraight(int v, GuiBase.RENDER_ROTATION rotation, TrackOrientation.DIRECTION base)
        {
            super(v, rotation);
            this.base = base;
        }

        public TrackOrientation.DIRECTION travel(TrackOrientation.DIRECTION in)
        {
            return in.equals(this.base) ? this.base.getOpposite() : this.base;
        }
    }
}
