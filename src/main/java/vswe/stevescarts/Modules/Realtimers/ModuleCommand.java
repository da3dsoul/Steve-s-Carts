package vswe.stevescarts.Modules.Realtimers;

import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public abstract class ModuleCommand extends ModuleBase implements ICommandSender
{
    private String command = "say HI";
    private int[] textbox = new int[] {10, 10, 145, 90};

    public ModuleCommand(MinecartModular cart)
    {
        super(cart);
    }

    public void drawForeground(GuiMinecart gui)
    {
        List lines = gui.getFontRenderer().listFormattedStringToWidth(this.command, this.textbox[2] - 4);

        for (int i = 0; i < lines.size(); ++i)
        {
            String line = lines.get(i).toString();
            this.drawString(gui, line, this.textbox[0] + 2, this.textbox[1] + 2 + i * 8, 16777215);
        }
    }

    public boolean hasGui()
    {
        return true;
    }

    public boolean hasSlots()
    {
        return false;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/command.png");
        this.drawImage(gui, this.textbox, 0, 0);
    }

    public void keyPress(char character, int extraInformation)
    {
        if (extraInformation == 14)
        {
            if (this.command.length() > 0)
            {
                this.command = this.command.substring(0, this.command.length() - 1);
            }
        }
        else
        {
            this.command = this.command + Character.toString(character);
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return "@";
    }

    public void sendChatToPlayer(String var1) {}

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int var1, String var2)
    {
        return var1 <= 2;
    }

    public String translateString(String var1, Object ... var2)
    {
        return var1;
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(this.getCart().x(), this.getCart().y(), this.getCart().z());
    }

    private void executeCommand()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            ;
        }
    }

    public void moveMinecartOnRail(int x, int y, int z)
    {
        if (this.getCart().worldObj.getBlock(x, y, z) == Blocks.detector_rail)
        {
            this.executeCommand();
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setString(this.generateNBTName("Command", id), this.command);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.command = tagCompound.getString(this.generateNBTName("Command", id));
    }
}
