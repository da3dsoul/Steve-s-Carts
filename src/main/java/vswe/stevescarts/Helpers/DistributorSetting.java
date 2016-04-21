package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import vswe.stevescarts.TileEntities.TileEntityDistributor;
import vswe.stevescarts.TileEntities.TileEntityManager;

public class DistributorSetting
{
    public static ArrayList<DistributorSetting> settings = new ArrayList();
    private int id;
    private int imageId;
    private boolean top;
    private Localization.GUI.DISTRIBUTOR name;

    public DistributorSetting(int id, boolean top, Localization.GUI.DISTRIBUTOR name)
    {
        this.id = id;
        this.top = top;
        this.name = name;
        this.imageId = id / 2;
    }

    public boolean isValid(TileEntityManager manager, int chunkId, boolean top)
    {
        return top == this.top;
    }

    public int getId()
    {
        return this.id;
    }

    public int getImageId()
    {
        return this.imageId;
    }

    public String getName(TileEntityManager[] manager)
    {
        return manager != null && manager.length > 1 ? this.name.translate(new String[0]) + " (" + (this.getIsTop() ? Localization.GUI.DISTRIBUTOR.MANAGER_TOP.translate(new String[0]) : Localization.GUI.DISTRIBUTOR.MANAGER_BOT.translate(new String[0])) + ")" : this.name.translate(new String[0]);
    }

    public boolean getIsTop()
    {
        return this.top;
    }

    public boolean isEnabled(TileEntityDistributor distributor)
    {
        return distributor.getInventories().length == 0 ? false : (this.top ? distributor.hasTop : distributor.hasBot);
    }

    static
    {
        settings.add(new DistributorSetting(0, true, Localization.GUI.DISTRIBUTOR.SETTING_ALL));
        settings.add(new DistributorSetting(1, false, Localization.GUI.DISTRIBUTOR.SETTING_ALL));
        settings.add(new DistributorSetting.distributorSettingColor(2, true, Localization.GUI.DISTRIBUTOR.SETTING_RED, 1));
        settings.add(new DistributorSetting.distributorSettingColor(3, false, Localization.GUI.DISTRIBUTOR.SETTING_RED, 1));
        settings.add(new DistributorSetting.distributorSettingColor(4, true, Localization.GUI.DISTRIBUTOR.SETTING_BLUE, 2));
        settings.add(new DistributorSetting.distributorSettingColor(5, false, Localization.GUI.DISTRIBUTOR.SETTING_BLUE, 2));
        settings.add(new DistributorSetting.distributorSettingColor(6, true, Localization.GUI.DISTRIBUTOR.SETTING_YELLOW, 3));
        settings.add(new DistributorSetting.distributorSettingColor(7, false, Localization.GUI.DISTRIBUTOR.SETTING_YELLOW, 3));
        settings.add(new DistributorSetting.distributorSettingColor(8, true, Localization.GUI.DISTRIBUTOR.SETTING_GREEN, 4));
        settings.add(new DistributorSetting.distributorSettingColor(9, false, Localization.GUI.DISTRIBUTOR.SETTING_GREEN, 4));
        settings.add(new DistributorSetting.distributorSettingChunk(10, true, Localization.GUI.DISTRIBUTOR.SETTING_TOP_LEFT, 0));
        settings.add(new DistributorSetting.distributorSettingChunk(11, false, Localization.GUI.DISTRIBUTOR.SETTING_TOP_LEFT, 0));
        settings.add(new DistributorSetting.distributorSettingChunk(12, true, Localization.GUI.DISTRIBUTOR.SETTING_TOP_RIGHT, 1));
        settings.add(new DistributorSetting.distributorSettingChunk(13, false, Localization.GUI.DISTRIBUTOR.SETTING_TOP_RIGHT, 1));
        settings.add(new DistributorSetting.distributorSettingChunk(14, true, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_LEFT, 2));
        settings.add(new DistributorSetting.distributorSettingChunk(15, false, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_LEFT, 2));
        settings.add(new DistributorSetting.distributorSettingChunk(16, true, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_RIGHT, 3));
        settings.add(new DistributorSetting.distributorSettingChunk(17, false, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_RIGHT, 3));
        settings.add(new DistributorSetting.distributorSettingDirection(18, true, Localization.GUI.DISTRIBUTOR.SETTING_TO_CART, true));
        settings.add(new DistributorSetting.distributorSettingDirection(19, false, Localization.GUI.DISTRIBUTOR.SETTING_TO_CART, true));
        settings.add(new DistributorSetting.distributorSettingDirection(20, true, Localization.GUI.DISTRIBUTOR.SETTING_FROM_CART, false));
        settings.add(new DistributorSetting.distributorSettingDirection(21, false, Localization.GUI.DISTRIBUTOR.SETTING_FROM_CART, false));
    }

    private static class distributorSettingChunk extends DistributorSetting
    {
        private int chunk;

        public distributorSettingChunk(int id, boolean top, Localization.GUI.DISTRIBUTOR name, int chunk)
        {
            super(id, top, name);
            this.chunk = chunk;
        }

        public boolean isValid(TileEntityManager manager, int chunkId, boolean top)
        {
            return manager.layoutType == 0 ? super.isValid(manager, chunkId, top) : super.isValid(manager, chunkId, top) && this.chunk == chunkId;
        }
    }

    private static class distributorSettingColor extends DistributorSetting
    {
        private int color;

        public distributorSettingColor(int id, boolean top, Localization.GUI.DISTRIBUTOR name, int color)
        {
            super(id, top, name);
            this.color = color;
        }

        public boolean isValid(TileEntityManager manager, int chunkId, boolean top)
        {
            return manager.layoutType == 0 ? super.isValid(manager, chunkId, top) : super.isValid(manager, chunkId, top) && manager.color[chunkId] == this.color;
        }
    }

    private static class distributorSettingDirection extends DistributorSetting
    {
        private boolean toCart;

        public distributorSettingDirection(int id, boolean top, Localization.GUI.DISTRIBUTOR name, boolean toCart)
        {
            super(id, top, name);
            this.toCart = toCart;
        }

        public boolean isValid(TileEntityManager manager, int chunkId, boolean top)
        {
            return manager.layoutType == 0 ? super.isValid(manager, chunkId, top) : super.isValid(manager, chunkId, top) && manager.toCart[chunkId] == this.toCart;
        }
    }
}
