package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Containers.ContainerLiquid;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityLiquid;

@SideOnly(Side.CLIENT)
public class GuiLiquid extends GuiManager
{
    private static ResourceLocation texture = ResourceHelper.getResource("/gui/liquidmanager.png");
    private static ResourceLocation textureExtra = ResourceHelper.getResource("/gui/liquidmanagerExtra.png");

    public GuiLiquid(InventoryPlayer invPlayer, TileEntityLiquid liquid)
    {
        super(invPlayer, liquid, new ContainerLiquid(invPlayer, liquid));
        this.setXSize(230);
        this.setYSize(222);
    }

    protected String getMaxSizeOverlay(int id)
    {
        int amount = this.getLiquid().getMaxAmount(id);
        return !this.getLiquid().hasMaxAmount(id) ? Localization.GUI.LIQUID.TRANSFER_ALL.translate(new String[0]) : Localization.GUI.LIQUID.TRANSFER_BUCKETS.translate(new String[] {this.getMaxSizeText(id)});
    }

    protected String getMaxSizeText(int id)
    {
        if (!this.getLiquid().hasMaxAmount(id))
        {
            return Localization.GUI.LIQUID.TRANSFER_ALL_SHORT.translate(new String[0]);
        }
        else
        {
            String B = String.valueOf((float)this.getLiquid().getMaxAmount(id) / 1000.0F);

            if (B.charAt(B.length() - 1) == 48)
            {
                B = B.substring(0, B.length() - 2);
            }
            else if (B.charAt(0) == 48)
            {
                B = B.substring(1);
            }

            return B + Localization.GUI.LIQUID.TRANSFER_BUCKET_SHORT.translate(new String[0]);
        }
    }

    protected void drawBackground(int left, int top)
    {
        ResourceHelper.bindResource(texture);
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);

        if (this.getLiquid().getTanks() != null)
        {
            for (int version = 0; version < 4; ++version)
            {
                int[] i = this.getTankCoords(version);
                this.getLiquid().getTanks()[version].drawFluid(this, i[0], i[1]);
            }
        }

        ResourceHelper.bindResource(textureExtra);
        byte var7;

        if (this.getManager().layoutType == 0)
        {
            var7 = 0;
        }
        else
        {
            var7 = 1;
        }

        int var8;

        for (var8 = 0; var8 < 2; ++var8)
        {
            this.drawTexturedModalRect(left + (var8 == 0 ? 27 : 171), top + 63, 0, 102 + var7 * 12, 32, 12);
        }

        for (var8 = 0; var8 < 4; ++var8)
        {
            int[] coords = this.getTankCoords(var8);
            int type = var8 % 2;
            this.drawTexturedModalRect(left + coords[0], top + coords[1], 0, 51 * type, 36, 51);
        }
    }

    protected int getArrowSourceX()
    {
        return 72;
    }

    protected int getColorSourceX()
    {
        return 128;
    }

    protected int getCenterTargetX()
    {
        return 62;
    }

    protected void drawColors(int id, int color, int left, int top)
    {
        super.drawColors(id, color, left, top);

        if (this.getManager().layoutType == 2)
        {
            int[] coords = this.getTankCoords(id);
            this.drawTexturedModalRect(left + coords[0], top + coords[1], 36, 51 * color, 36, 51);
        }
    }

    protected int offsetObjectY(int layout, int x, int y)
    {
        return -5 + y * 10;
    }

    protected void drawExtraOverlay(int id, int x, int y)
    {
        this.drawMouseOver(this.getLiquid().getTanks()[id].getMouseOver(), x, y, this.getTankCoords(id));
    }

    protected Block getBlock()
    {
        return ModBlocks.LIQUID_MANAGER.getBlock();
    }

    protected String getManagerName()
    {
        return Localization.GUI.LIQUID.TITLE.translate(new String[0]);
    }

    private int[] getTankCoords(int id)
    {
        int x = id % 2;
        int y = id / 2;
        int xCoord = 25 + x * 144;
        int yCoord = 12 + y * 63;
        return new int[] {xCoord, yCoord, 36, 51};
    }

    private TileEntityLiquid getLiquid()
    {
        return (TileEntityLiquid)this.getManager();
    }

    protected String getLayoutString()
    {
        return Localization.GUI.LIQUID.CHANGE_LAYOUT.translate(new String[0]);
    }

    protected String getLayoutOption(int id)
    {
        switch (id)
        {
            case 0:
            default:
                return Localization.GUI.LIQUID.LAYOUT_ALL.translate(new String[0]);

            case 1:
                return Localization.GUI.LIQUID.LAYOUT_SIDE.translate(new String[0]);

            case 2:
                return Localization.GUI.LIQUID.LAYOUT_COLOR.translate(new String[0]);
        }
    }
}
