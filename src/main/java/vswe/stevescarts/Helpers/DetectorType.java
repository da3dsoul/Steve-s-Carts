package vswe.stevescarts.Helpers;

import java.util.HashMap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Blocks.BlockRailAdvDetector;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.TileEntities.TileEntityDetector;

public enum DetectorType
{
	
	/*
	 * 0000 0 Normal
	 * 0001 1 Unit
	 * 0010 2 Stop
	 * 0011 3 Junction
	 * 0100 4 Redstone
	 * 0111 7
	 * 1000 8 On/off
	 */
	
    NORMAL(0, true, false, true, new String[]{"detector_manager_bot", "detector_manager_top", "detector_manager_yellow", "detector_manager_blue", "detector_manager_green", "detector_manager_red"}),
    UNIT(1, false, false, false, new String[]{"detector_manager_bot", "detector_manager_bot", "detector_unit_yellow", "detector_unit_blue", "detector_unit_green", "detector_unit_red"}),
    STOP(2, true, true, false, new String[]{"detector_manager_bot", "detector_station_top", "detector_station_yellow", "detector_station_blue", "detector_station_green", "detector_station_red"})
    {
        public void activate(TileEntityDetector detector, MinecartModular cart)
        {
            cart.releaseCart();
        }
    },
    JUNCTION(3, true, false, false, new String[]{"detector_manager_bot", "detector_junction_top", "detector_junction_yellow", "detector_junction_blue", "detector_junction_green", "detector_junction_red"})
    {
        public void activate(TileEntityDetector detector, MinecartModular cart)
        {
            this.update(detector, true);
        }
        public void deactivate(TileEntityDetector detector)
        {
            this.update(detector, false);
        }
        private void update(TileEntityDetector detector, boolean flag)
        {
            if (detector.getWorldObj().getBlock(detector.xCoord, detector.yCoord + 1, detector.zCoord) == ModBlocks.ADVANCED_DETECTOR.getBlock())
            {
                ((BlockRailAdvDetector)ModBlocks.ADVANCED_DETECTOR.getBlock()).refreshState(detector.getWorldObj(), detector.xCoord, detector.yCoord + 1, detector.zCoord, flag);
            }
        }
    },
    REDSTONE(4, false, false, false, new String[]{"detector_redstone_bot", "detector_redstone_bot", "detector_redstone_yellow", "detector_redstone_blue", "detector_redstone_green", "detector_redstone_red"})
    {
        public void initOperators(HashMap<Byte, OperatorObject> operators)
        {
            super.initOperators(operators);
            new OperatorObject.OperatorObjectRedstone(operators, 11, Localization.GUI.DETECTOR.REDSTONE, 0, 0, 0);
            new OperatorObject.OperatorObjectRedstone(operators, 12, Localization.GUI.DETECTOR.REDSTONE_TOP, 0, 1, 0);
            new OperatorObject.OperatorObjectRedstone(operators, 13, Localization.GUI.DETECTOR.REDSTONE_BOT, 0, -1, 0);
            new OperatorObject.OperatorObjectRedstone(operators, 14, Localization.GUI.DETECTOR.REDSTONE_NORTH, 0, 0, -1);
            new OperatorObject.OperatorObjectRedstone(operators, 15, Localization.GUI.DETECTOR.REDSTONE_WEST, -1, 0, 0);
            new OperatorObject.OperatorObjectRedstone(operators, 16, Localization.GUI.DETECTOR.REDSTONE_SOUTH, 0, 0, 1);
            new OperatorObject.OperatorObjectRedstone(operators, 17, Localization.GUI.DETECTOR.REDSTONE_EAST, 1, 0, 0);
        }
    };
    private int meta;
    private String[] textures;
    private IIcon[] icons;
    private boolean acceptCart;
    private boolean stopCart;
    private boolean emitRedstone;
    private HashMap<Byte, OperatorObject> operators;

    private DetectorType(int meta, boolean acceptCart, boolean stopCart, boolean emitRedstone, String ... textures)
    {
        this.meta = meta;
        this.textures = textures;
        this.acceptCart = acceptCart;
        this.stopCart = stopCart;
        this.emitRedstone = emitRedstone;
    }

    public int getMeta()
    {
        return this.meta;
    }

    public String getName()
    {
        StringBuilder var10000 = (new StringBuilder()).append("item.");
        StevesCarts var10001 = StevesCarts.instance;
        return StatCollector.translateToLocal(var10000.append("SC2:").append("BlockDetector").append(this.meta).append(".name").toString());
    }

    public void registerIcons(IIconRegister register)
    {
        this.icons = new IIcon[this.textures.length];

        for (int i = 0; i < this.textures.length; ++i)
        {
            IIcon[] var10000 = this.icons;
            StringBuilder var10003 = new StringBuilder();
            StevesCarts.instance.getClass();
            var10000[i] = register.registerIcon(var10003.append("stevescarts").append(":").append(this.textures[i]).toString());
        }
    }

    public IIcon getIcon(int side)
    {
        return this.icons[side];
    }

    public boolean canInteractWithCart()
    {
        return this.acceptCart;
    }

    public boolean shouldStopCart()
    {
        return this.stopCart;
    }

    public boolean shouldEmitRedstone()
    {
        return this.emitRedstone;
    }

    public void activate(TileEntityDetector detector, MinecartModular cart) {}

    public void deactivate(TileEntityDetector detector) {}

    public static DetectorType getTypeFromMeta(int meta)
    {
        return values()[meta & 7];
    }

    public void initOperators(HashMap<Byte, OperatorObject> operators)
    {
        this.operators = operators;
    }

    public HashMap<Byte, OperatorObject> getOperators()
    {
        return this.operators;
    }
}
