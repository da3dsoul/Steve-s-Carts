package vswe.stevescarts.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Containers.ContainerDistributor;
import vswe.stevescarts.Helpers.DistributorSetting;
import vswe.stevescarts.Helpers.DistributorSide;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.TileEntities.TileEntityDistributor;
import vswe.stevescarts.TileEntities.TileEntityManager;

@SideOnly(Side.CLIENT)
public class GuiDistributor extends GuiBase
{
    private String mouseOverText;
    private static ResourceLocation texture = ResourceHelper.getResource("/gui/distributor.png");
    private int activeId = -1;
    TileEntityDistributor distributor;
    InventoryPlayer invPlayer;

    public GuiDistributor(InventoryPlayer invPlayer, TileEntityDistributor distributor)
    {
        super(new ContainerDistributor(invPlayer, distributor));
        this.invPlayer = invPlayer;
        this.setXSize(255);
        this.setYSize(186);
        this.distributor = distributor;
    }

    public void drawGuiForeground(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.getFontRenderer().drawString(Localization.GUI.DISTRIBUTOR.TITLE.translate(new String[0]), 8, 6, 4210752);
        TileEntityManager[] invs = this.distributor.getInventories();

        if (invs.length == 0)
        {
            this.getFontRenderer().drawString(Localization.GUI.DISTRIBUTOR.NOT_CONNECTED.translate(new String[0]), 30, 40, 16728128);
        }

        if (this.mouseOverText != null && !this.mouseOverText.equals(""))
        {
            this.drawMouseOver(this.mouseOverText, x - this.getGuiLeft(), y - this.getGuiTop());
        }

        this.mouseOverText = null;
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void drawMouseMover(String str, int x, int y, int[] rect)
    {
        if (this.inRect(x, y, rect))
        {
            this.mouseOverText = str;
        }
    }

    public void drawGuiBackground(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        ResourceHelper.bindResource(texture);
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();
        TileEntityManager[] invs = this.distributor.getInventories();
        ArrayList sides = this.distributor.getSides();
        int id = 0;
        Iterator setting = sides.iterator();
        int[] box;

        while (setting.hasNext())
        {
            DistributorSide setting1 = (DistributorSide)setting.next();

            if (setting1.isEnabled(this.distributor))
            {
                box = this.getSideBoxRect(id);
                int srcX = 0;

                if (this.inRect(x, y, box))
                {
                    srcX = box[2];
                }

                this.drawTexturedModalRect(j + box[0], k + box[1], srcX, this.ySize, box[2], box[3]);
                this.drawTexturedModalRect(j + box[0] + 2, k + box[1] + 2, box[2] * 2 + (box[2] - 4) * setting1.getId(), this.ySize, box[2] - 4, box[3] - 4);
                this.drawMouseMover(Localization.GUI.DISTRIBUTOR.SIDE.translate(new String[] {setting1.getName()}) + (this.activeId != -1 ? "\n[" + Localization.GUI.DISTRIBUTOR.DROP_INSTRUCTION.translate(new String[0]) + "]" : ""), x, y, box);
                int settingCount = 0;
                Iterator i$ = DistributorSetting.settings.iterator();

                while (i$.hasNext())
                {
                    DistributorSetting setting2 = (DistributorSetting)i$.next();

                    if (setting2.isEnabled(this.distributor) && setting1.isSet(setting2.getId()))
                    {
                        int[] settingbox = this.getActiveSettingBoxRect(id, settingCount++);
                        this.drawSetting(setting2, settingbox, this.inRect(x, y, settingbox));
                        this.drawMouseMover(setting2.getName(invs) + "\n[" + Localization.GUI.DISTRIBUTOR.REMOVE_INSTRUCTION.translate(new String[0]) + "]", x, y, settingbox);
                    }
                }

                ++id;
            }
        }

        setting = DistributorSetting.settings.iterator();

        while (setting.hasNext())
        {
            DistributorSetting var18 = (DistributorSetting)setting.next();

            if (var18.isEnabled(this.distributor))
            {
                box = this.getSettingBoxRect(var18.getImageId(), var18.getIsTop());
                this.drawSetting(var18, box, this.inRect(x, y, box));
                this.drawMouseMover(var18.getName(invs), x, y, box);
            }
        }

        if (this.activeId != -1)
        {
            DistributorSetting var17 = (DistributorSetting)DistributorSetting.settings.get(this.activeId);
            this.drawSetting(var17, new int[] {x - 8, y - 8, 16, 16}, true);
        }
    }

    private void drawSetting(DistributorSetting setting, int[] box, boolean hover)
    {
        int j = this.getGuiLeft();
        int k = this.getGuiTop();
        int srcX = 0;

        if (!setting.getIsTop())
        {
            srcX += box[2] * 2;
        }

        if (hover)
        {
            srcX += box[2];
        }

        this.drawTexturedModalRect(j + box[0], k + box[1], srcX, this.ySize + this.getSideBoxRect(0)[3], box[2], box[3]);
        this.drawTexturedModalRect(j + box[0] + 1, k + box[1] + 1, box[2] * 4 + (box[2] - 2) * setting.getImageId(), this.ySize + this.getSideBoxRect(0)[3], box[2] - 2, box[3] - 2);
    }

    private int[] getSideBoxRect(int i)
    {
        return new int[] {20, 18 + i * 24, 22, 22};
    }

    private int[] getSettingBoxRect(int i, boolean topRow)
    {
        return new int[] {20 + i * 18, 143 + (topRow ? 0 : 18), 16, 16};
    }

    private int[] getActiveSettingBoxRect(int side, int setting)
    {
        int[] sideCoords = this.getSideBoxRect(side);
        return new int[] {sideCoords[0] + sideCoords[2] + 5 + setting * 18, sideCoords[1] + (sideCoords[3] - 16) / 2, 16, 16};
    }

    public void mouseClick(int x, int y, int button)
    {
        super.mouseClick(x, y, button);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();

        if (button == 0)
        {
            Iterator i$ = DistributorSetting.settings.iterator();

            while (i$.hasNext())
            {
                DistributorSetting setting = (DistributorSetting)i$.next();

                if (setting.isEnabled(this.distributor))
                {
                    int[] box = this.getSettingBoxRect(setting.getImageId(), setting.getIsTop());

                    if (this.inRect(x, y, box))
                    {
                        this.activeId = setting.getId();
                    }
                }
            }
        }
    }

    public void mouseMoved(int x, int y, int button)
    {
        super.mouseMoved(x, y, button);
        x -= this.getGuiLeft();
        y -= this.getGuiTop();
        int id;
        Iterator i$;
        DistributorSide side;

        if (button == 0 && this.activeId != -1)
        {
            id = 0;
            i$ = this.distributor.getSides().iterator();

            while (i$.hasNext())
            {
                side = (DistributorSide)i$.next();

                if (side.isEnabled(this.distributor))
                {
                    int[] var11 = this.getSideBoxRect(id++);

                    if (this.inRect(x, y, var11))
                    {
                        this.distributor.sendPacket(0, new byte[] {(byte)this.activeId, (byte)side.getId()});
                        break;
                    }
                }
            }

            this.activeId = -1;
        }
        else if (button == 1)
        {
            id = 0;
            i$ = this.distributor.getSides().iterator();

            while (i$.hasNext())
            {
                side = (DistributorSide)i$.next();

                if (side.isEnabled(this.distributor))
                {
                    int settingCount = 0;
                    Iterator i$1 = DistributorSetting.settings.iterator();

                    while (i$1.hasNext())
                    {
                        DistributorSetting setting = (DistributorSetting)i$1.next();

                        if (setting.isEnabled(this.distributor) && side.isSet(setting.getId()))
                        {
                            int[] settingbox = this.getActiveSettingBoxRect(id, settingCount++);

                            if (this.inRect(x, y, settingbox))
                            {
                                this.distributor.sendPacket(1, new byte[] {(byte)setting.getId(), (byte)side.getId()});
                            }
                        }
                    }

                    ++id;
                }
            }
        }
    }
}
