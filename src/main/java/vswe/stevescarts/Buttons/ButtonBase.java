package vswe.stevescarts.Buttons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public abstract class ButtonBase
{
    protected final ButtonBase.LOCATION loc;
    protected final ModuleBase module;
    private boolean lastVisibility;
    private int currentID;
    private int moduleID;
    @SideOnly(Side.CLIENT)
    private static ResourceLocation texture = ResourceHelper.getResource("/gui/buttons.png");

    public ButtonBase(ModuleBase module, ButtonBase.LOCATION loc)
    {
        this.module = module;
        module.addButton(this);
        this.loc = loc;
    }

    public void setCurrentID(int id)
    {
        this.currentID = id;
    }

    public void setIdInModule(int id)
    {
        this.moduleID = id;
    }

    public int getIdInModule()
    {
        return this.moduleID;
    }

    public String toString()
    {
        return "";
    }

    public boolean isEnabled()
    {
        return false;
    }

    public boolean hasText()
    {
        return false;
    }

    public boolean isVisible()
    {
        return false;
    }

    public final void computeOnClick(GuiMinecart gui, int mousebutton)
    {
        if (this.isVisible() && this.isEnabled())
        {
            this.onClientClick(mousebutton, GuiMinecart.isCtrlKeyDown(), GuiMinecart.isShiftKeyDown());

            if (this.handleClickOnServer())
            {
                byte clickinformation = (byte)(mousebutton & 63);
                clickinformation = (byte)(clickinformation | (GuiMinecart.isCtrlKeyDown() ? 1 : 0) << 6);
                clickinformation = (byte)(clickinformation | (GuiMinecart.isShiftKeyDown() ? 1 : 0) << 7);
                this.module.sendButtonPacket(this, clickinformation);
            }
        }
    }

    public void onClientClick(int mousebutton, boolean ctrlKey, boolean shiftKey) {}

    public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {}

    public boolean handleClickOnServer()
    {
        return true;
    }

    private boolean useTexture()
    {
        return this.texture() != -1;
    }

    public int ColorCode()
    {
        return 0;
    }

    private boolean hasBorder()
    {
        return this.borderID() != -1;
    }

    public int borderID()
    {
        return -1;
    }

    public int texture()
    {
        return -1;
    }

    public int textureX()
    {
        return this.texture() % 21 * 12;
    }

    public int textureY()
    {
        return 60 + this.texture() / 21 * 12;
    }

    public void drawButtonText(GuiMinecart gui, ModuleBase module)
    {
        if (this.isVisible() && this.hasText())
        {
            module.drawString(gui, this.toString(), this.X() + 8, this.Y() + 7, 16777215);
        }
    }

    public void drawButton(GuiMinecart gui, ModuleBase module, int x, int y)
    {
        boolean visibility = this.isVisible();

        if (visibility != this.lastVisibility)
        {
            module.buttonVisibilityChanged();
        }

        this.lastVisibility = visibility;
        ResourceHelper.bindResource(texture);

        if (visibility)
        {
            int sourceX = 0;
            int sourceY = 20;

            if (this.isEnabled())
            {
                sourceX = 20 * (this.ColorCode() + 1);
            }

            if (module.inRect(x, y, this.getBounds()))
            {
                sourceY += 20;
            }

            module.drawImage(gui, this.getBounds(), sourceX, sourceY);

            if (this.useTexture())
            {
                module.drawImage(gui, this.X() + 4, this.Y() + 4, this.textureX(), this.textureY(), 12, 12);
            }

            if (this.hasBorder())
            {
                module.drawImage(gui, this.getBounds(), this.borderID() * 20, 0);
            }
        }
    }

    public int[] getBounds()
    {
        return new int[] {this.X(), this.Y(), 20, 20};
    }

    public int X()
    {
        switch (ButtonBase.NamelessClass1277976066.$SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[this.loc.ordinal()])
        {
            case 1:
                return 15 + this.currentID * 25;

            case 2:
                return 125 + this.currentID % 6 * 25;

            case 3:
                return 306 + this.currentID % 5 * 25;

            case 4:
                return 0;

            case 5:
                return 115 + this.currentID % 7 * 25;

            case 6:
                return 400 + this.currentID % 3 * 25;

            case 7:
                return 366 + this.currentID % 5 * 25;

            case 8:
                return 111 + this.currentID % 6 * 22;

            default:
                return -1;
        }
    }

    public int Y()
    {
        switch (ButtonBase.NamelessClass1277976066.$SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[this.loc.ordinal()])
        {
            case 1:
                return 143;

            case 2:
                return 118 + this.currentID / 6 * 25;

            case 3:
                return 32 + this.currentID / 5 * 25;

            case 4:
                return 0;

            case 5:
                return 32 + this.currentID / 7 * 25;

            case 6:
                return 32 + this.currentID / 3 * 25;

            case 7:
                return 118 + this.currentID / 5 * 25;

            case 8:
                return 19 + this.currentID / 6 * 22;

            default:
                return -1;
        }
    }

    public ButtonBase.LOCATION getLocation()
    {
        return this.loc;
    }

    public int getLocationID()
    {
        for (int i = 0; i < ButtonBase.LOCATION.values().length; ++i)
        {
            if (ButtonBase.LOCATION.values()[i] == this.loc)
            {
                return i;
            }
        }

        return 0;
    }

    static class NamelessClass1277976066
    {
        static final int[] $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION = new int[ButtonBase.LOCATION.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.OVERVIEW.ordinal()] = 1;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.PROGRAM.ordinal()] = 2;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.TASK.ordinal()] = 3;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.DEFINED.ordinal()] = 4;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.FLOATING.ordinal()] = 5;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.VARIABLE.ordinal()] = 6;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.BUILD.ordinal()] = 7;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Buttons$ButtonBase$LOCATION[ButtonBase.LOCATION.MODEL.ordinal()] = 8;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    public static enum LOCATION
    {
        OVERVIEW("OVERVIEW", 0),
        PROGRAM("PROGRAM", 1),
        TASK("TASK", 2),
        DEFINED("DEFINED", 3),
        FLOATING("FLOATING", 4),
        VARIABLE("VARIABLE", 5),
        BUILD("BUILD", 6),
        MODEL("MODEL", 7);

        private static final ButtonBase.LOCATION[] $VALUES = new ButtonBase.LOCATION[]{OVERVIEW, PROGRAM, TASK, DEFINED, FLOATING, VARIABLE, BUILD, MODEL};

        private LOCATION(String var1, int var2) {}
    }
}
