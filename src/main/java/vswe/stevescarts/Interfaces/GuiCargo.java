package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Containers.ContainerCargo;
import vswe.stevescarts.Helpers.CargoItemSelection;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityCargo;

@SideOnly(Side.CLIENT)
public class GuiCargo extends GuiManager
{
    private static ResourceLocation[] texturesLeft = new ResourceLocation[] {ResourceHelper.getResource("/gui/cargoVersion0Part1.png"), ResourceHelper.getResource("/gui/cargoVersion1Part1.png")};
    private static ResourceLocation[] texturesRight = new ResourceLocation[] {ResourceHelper.getResource("/gui/cargoVersion0Part2.png"), ResourceHelper.getResource("/gui/cargoVersion1Part2.png")};

    public GuiCargo(InventoryPlayer invPlayer, TileEntityCargo cargo)
    {
        super(invPlayer, cargo, new ContainerCargo(invPlayer, cargo));
        this.setXSize(305);
        this.setYSize(222);
    }

    protected String getMaxSizeOverlay(int id)
    {
        int amount = this.getCargo().getAmount(id);
        int type = this.getCargo().getAmountType(id);
        return type == 0 ? Localization.GUI.CARGO.TRANSFER_ALL.translate(new String[0]) : (type == 1 ? Localization.GUI.CARGO.TRANSFER_ITEMS.translate(new String[] {String.valueOf(amount), String.valueOf(amount)}): Localization.GUI.CARGO.TRANSFER_STACKS.translate(new String[] {String.valueOf(amount), String.valueOf(amount)}));
    }

    protected String getMaxSizeText(int id)
    {
        int type = this.getCargo().getAmountType(id);
        String s;

        if (type == 0)
        {
            s = Localization.GUI.CARGO.TRANSFER_ALL_SHORT.translate(new String[0]);
        }
        else
        {
            int amount = this.getCargo().getAmount(id);
            s = String.valueOf(amount);

            if (type == 1)
            {
                s = s + " " + Localization.GUI.CARGO.TRANSFER_ITEMS_SHORT.translate(new String[0]);
            }
            else
            {
                s = s + " " + Localization.GUI.CARGO.TRANSFER_STACKS_SHORT.translate(new String[0]);
            }
        }

        return s;
    }

    protected void drawBackground(int left, int top)
    {
        byte version;

        if (this.getManager().layoutType == 0)
        {
            version = 0;
        }
        else
        {
            version = 1;
        }

        ResourceHelper.bindResource(texturesLeft[version]);
        this.drawTexturedModalRect(left, top, 0, 0, 256, this.ySize);
        ResourceHelper.bindResource(texturesRight[version]);
        this.drawTexturedModalRect(left + 256, top, 0, 0, this.xSize - 256, this.ySize);
    }

    protected int getArrowSourceX()
    {
        return 49;
    }

    protected int getColorSourceX()
    {
        return 105;
    }

    protected int getCenterTargetX()
    {
        return 98;
    }

    protected void drawColors(int id, int color, int left, int top)
    {
        super.drawColors(id, color, left, top);

        if (this.getManager().layoutType == 2)
        {
            int[] coords = this.getInvCoords(id);
            this.drawTexturedModalRect(left + coords[0] - 2, top + coords[1] - 2, 125, 56 * color, 92, 56);
        }
    }

    protected void drawItems(int id, RenderItem renderitem, int left, int top)
    {
        ItemStack cartIcon;
        label14:
        {
            if (this.getCargo().target[id] >= 0)
            {
                int var10000 = this.getCargo().target[id];
                this.getCargo();

                if (var10000 < TileEntityCargo.itemSelections.size())
                {
                    this.getCargo();

                    if (((CargoItemSelection)TileEntityCargo.itemSelections.get(this.getCargo().target[id])).getIcon() != null)
                    {
                        cartIcon = ((CargoItemSelection)TileEntityCargo.itemSelections.get(this.getCargo().target[id])).getIcon();
                        break label14;
                    }
                }
            }

            cartIcon = new ItemStack(Items.minecart, 1);
        }
        int[] coords = this.getBoxCoords(id);
        GL11.glDisable(GL11.GL_LIGHTING);
        renderitem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, cartIcon, left + coords[0], top + coords[1]);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    protected int offsetObjectY(int layout, int x, int y)
    {
        return layout != 0 ? -5 + y * 10 : super.offsetObjectY(layout, x, y);
    }

    protected boolean sendOnClick(int id, int x, int y, byte data)
    {
        if (this.inRect(x, y, this.getBoxCoords(id)))
        {
            this.getManager().sendPacket(1, data);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void drawExtraOverlay(int id, int x, int y)
    {
        if (this.getCargo().target[id] >= 0)
        {
            int var10000 = this.getCargo().target[id];
            this.getCargo();

            if (var10000 < TileEntityCargo.itemSelections.size())
            {
                this.getCargo();
                CargoItemSelection item = (CargoItemSelection)TileEntityCargo.itemSelections.get(this.getCargo().target[id]);

                if (item.getName() != null)
                {
                    this.drawMouseOver(Localization.GUI.CARGO.CHANGE_STORAGE_AREA.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + item.getName(), x, y, this.getBoxCoords(id));
                }
                else
                {
                    this.drawMouseOver(Localization.GUI.CARGO.CHANGE_STORAGE_AREA.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + Localization.GUI.CARGO.UNKNOWN_AREA.translate(new String[0]), x, y, this.getBoxCoords(id));
                }

                return;
            }
        }

        this.drawMouseOver(Localization.GUI.CARGO.CHANGE_STORAGE_AREA.translate(new String[0]) + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate(new String[0]) + ": " + Localization.GUI.CARGO.UNKNOWN_AREA.translate(new String[0]), x, y, this.getBoxCoords(id));
    }

    protected Block getBlock()
    {
        return ModBlocks.CARGO_MANAGER.getBlock();
    }

    protected String getManagerName()
    {
        return Localization.GUI.CARGO.TITLE.translate(new String[0]);
    }

    private int[] getInvCoords(int id)
    {
        int x = id % 2;
        int y = id / 2;
        int xCoord = 8 + x * 198;
        int yCoord = 11 + y * 64;
        return new int[] {xCoord, yCoord};
    }

    private TileEntityCargo getCargo()
    {
        return (TileEntityCargo)this.getManager();
    }

    protected String getLayoutString()
    {
        return Localization.GUI.CARGO.CHANGE_SLOT_LAYOUT.translate(new String[0]);
    }

    protected String getLayoutOption(int id)
    {
        switch (id)
        {
            case 0:
            default:
                return Localization.GUI.CARGO.LAYOUT_SHARED.translate(new String[0]);

            case 1:
                return Localization.GUI.CARGO.LAYOUT_SIDE.translate(new String[0]);

            case 2:
                return Localization.GUI.CARGO.LAYOUT_COLOR.translate(new String[0]);
        }
    }
}
