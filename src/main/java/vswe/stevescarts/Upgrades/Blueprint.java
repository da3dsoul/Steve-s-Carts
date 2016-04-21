package vswe.stevescarts.Upgrades;

import java.util.ArrayList;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Slots.SlotCart;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Blueprint extends SimpleInventoryEffect
{
    public Blueprint()
    {
        super(1, 1);
    }

    public Class <? extends Slot > getSlot(int i)
    {
        return SlotCart.class;
    }

    public String getName()
    {
        return Localization.UPGRADES.BLUEPRINT.translate(new String[0]);
    }

    public boolean isValidForBluePrint(TileEntityUpgrade upgrade, ArrayList<ModuleData> modules, ModuleData module)
    {
        ItemStack blueprint = upgrade.getStackInSlot(0);

        if (blueprint != null)
        {
            NBTTagCompound info = blueprint.getTagCompound();

            if (info == null)
            {
                return false;
            }
            else
            {
                NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");

                if (moduleIDTag == null)
                {
                    return false;
                }
                else
                {
                    byte[] IDs = moduleIDTag.func_150292_c();
                    ArrayList missing = new ArrayList();
                    byte[] arr$ = IDs;
                    int len$ = IDs.length;

                    for (int i$ = 0; i$ < len$; ++i$)
                    {
                        byte id = arr$[i$];
                        ModuleData blueprintModule = (ModuleData)ModuleData.getList().get(Byte.valueOf(id));
                        int index = modules.indexOf(blueprintModule);

                        if (index != -1)
                        {
                            modules.remove(index);
                        }
                        else
                        {
                            missing.add(blueprintModule);
                        }
                    }

                    return missing.contains(module);
                }
            }
        }
        else
        {
            return false;
        }
    }
}
