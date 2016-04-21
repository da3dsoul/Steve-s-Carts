package vswe.stevescarts.TileEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerActivator;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Helpers.ActivatorOption;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiActivator;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Realtimers.ModuleCage;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class TileEntityActivator extends TileEntityBase
{
    private ArrayList<ActivatorOption> options;

    @SideOnly(Side.CLIENT)
    public GuiBase getGui(InventoryPlayer inv)
    {
        return new GuiActivator(inv, this);
    }

    public ContainerBase getContainer(InventoryPlayer inv)
    {
        return new ContainerActivator(inv, this);
    }

    public TileEntityActivator()
    {
        this.loadOptions();
    }

    private void loadOptions()
    {
        this.options = new ArrayList();
        this.options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_DRILL, ModuleDrill.class));
        this.options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_SHIELD, ModuleShield.class));
        this.options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_INVISIBILITY, ModuleInvisible.class));
        this.options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_CHUNK, ModuleChunkLoader.class));
        this.options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_CAGE_AUTO, ModuleCage.class, 0));
        this.options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_CAGE, ModuleCage.class, 1));
    }

    public ArrayList<ActivatorOption> getOptions()
    {
        return this.options;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        Iterator i$ = this.options.iterator();

        while (i$.hasNext())
        {
            ActivatorOption option = (ActivatorOption)i$.next();
            option.setOption(nbttagcompound.getByte(option.getName()));
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        Iterator i$ = this.options.iterator();

        while (i$.hasNext())
        {
            ActivatorOption option = (ActivatorOption)i$.next();
            nbttagcompound.setByte(option.getName(), (byte)option.getOption());
        }
    }

    public void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            boolean leftClick = (data[0] & 1) == 0;
            int optionId = (data[0] & -2) >> 1;

            if (optionId >= 0 && optionId < this.options.size())
            {
                ((ActivatorOption)this.options.get(optionId)).changeOption(leftClick);
            }
        }
    }

    public void initGuiData(Container con, ICrafting crafting)
    {
        for (int i = 0; i < this.options.size(); ++i)
        {
            this.updateGuiData(con, crafting, i, (short)((ActivatorOption)this.options.get(i)).getOption());
        }
    }

    public void checkGuiData(Container con, ICrafting crafting)
    {
        for (int i = 0; i < this.options.size(); ++i)
        {
            int option = ((ActivatorOption)this.options.get(i)).getOption();
            int lastoption = ((Integer)((ContainerActivator)con).lastOptions.get(i)).intValue();

            if (option != lastoption)
            {
                this.updateGuiData(con, crafting, i, (short)option);
                ((ContainerActivator)con).lastOptions.set(i, Integer.valueOf(option));
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        if (id >= 0 && id < this.options.size())
        {
            ((ActivatorOption)this.options.get(id)).setOption(data);
        }
    }

    public void handleCart(MinecartModular cart, boolean isOrange)
    {
        Iterator i$ = this.options.iterator();

        while (i$.hasNext())
        {
            ActivatorOption option = (ActivatorOption)i$.next();

            if (!option.isDisabled())
            {
                cart.handleActivator(option, isOrange);
            }
        }
    }
}
