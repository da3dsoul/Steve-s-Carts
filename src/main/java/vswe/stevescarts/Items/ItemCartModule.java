package vswe.stevescarts.Items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class ItemCartModule extends Item
{
    IIcon unknownIcon;

    public ItemCartModule()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(StevesCarts.tabsSC2);
    }

    public String getName(ItemStack par1ItemStack)
    {
        ModuleData data = this.getModuleData(par1ItemStack, true);
        return data == null ? "Unknown SC2 module" : data.getName();
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int dmg)
    {
        ModuleData data = (ModuleData)ModuleData.getList().get(Byte.valueOf((byte)dmg));
        return data != null ? data.getIcon() : this.unknownIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        Iterator i$ = ModuleData.getList().values().iterator();

        while (i$.hasNext())
        {
            ModuleData module = (ModuleData)i$.next();
            module.createIcon(register);
        }

        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.unknownIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("unknown_icon").toString());
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getUnlocalizedName()
    {
        return "item.SC2:unknownmodule";
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack item)
    {
        ModuleData data = this.getModuleData(item, true);
        return data != null ? "item.SC2:" + data.getRawName() : this.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)

    /**
     * This returns the sub items
     */
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
        Iterator i$ = ModuleData.getList().values().iterator();

        while (i$.hasNext())
        {
            ModuleData module = (ModuleData)i$.next();

            if (module.getIsValid())
            {
                par3List.add(module.getItemStack());
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        ModuleData module = this.getModuleData(par1ItemStack, true);

        if (module != null)
        {
            module.addInformation(par3List, par1ItemStack.getTagCompound());
        }
        else if (par1ItemStack != null && par1ItemStack.getItem() instanceof ItemCartModule)
        {
            par3List.add("Module id " + par1ItemStack.getItemDamage());
        }
        else
        {
            par3List.add("Unknown module id");
        }
    }

    public ModuleData getModuleData(ItemStack itemstack)
    {
        return this.getModuleData(itemstack, false);
    }

    public ModuleData getModuleData(ItemStack itemstack, boolean ignoreSize)
    {
        return itemstack != null && itemstack.getItem() instanceof ItemCartModule && (ignoreSize || itemstack.stackSize != TileEntityCartAssembler.getRemovedSize()) ? (ModuleData)ModuleData.getList().get(Byte.valueOf((byte)itemstack.getItemDamage())) : null;
    }

    public void addExtraDataToCart(NBTTagCompound save, ItemStack module, int i)
    {
        if (module.getTagCompound() != null && module.getTagCompound().hasKey("Data"))
        {
            save.setByte("Data" + i, module.getTagCompound().getByte("Data"));
        }
        else
        {
            ModuleData data = this.getModuleData(module, true);

            if (data.isUsingExtraData())
            {
                save.setByte("Data" + i, data.getDefaultExtraData());
            }
        }
    }

    public void addExtraDataToModule(NBTTagCompound save, ModuleBase module, int i)
    {
        if (module.hasExtraData())
        {
            save.setByte("Data" + i, module.getExtraData());
        }
    }

    public void addExtraDataToModule(ItemStack module, NBTTagCompound info, int i)
    {
        NBTTagCompound save = module.getTagCompound();

        if (save == null)
        {
            module.setTagCompound(save = new NBTTagCompound());
        }

        if (info != null && info.hasKey("Data" + i))
        {
            save.setByte("Data", info.getByte("Data" + i));
        }
        else
        {
            ModuleData data = this.getModuleData(module, true);

            if (data.isUsingExtraData())
            {
                save.setByte("Data", data.getDefaultExtraData());
            }
        }
    }
}
