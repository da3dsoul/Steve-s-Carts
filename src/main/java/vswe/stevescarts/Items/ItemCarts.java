package vswe.stevescarts.Items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CartVersion;
import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Helpers.ModuleCountPair;
import vswe.stevescarts.ModuleData.ModuleData;

public class ItemCarts extends ItemMinecart
{
    public ItemCarts()
    {
        super(0);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab((CreativeTabs)null);
    }

    public String getName()
    {
        return "Modular cart";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.itemIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("modular_cart").append("_icon").toString());
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        CartVersion.updateItemStack(par1ItemStack);

        if (BlockRailBase.func_150049_b_(par3World, par4, par5, par6))
        {
            if (!par3World.isRemote)
            {
                try
                {
                    NBTTagCompound e = par1ItemStack.getTagCompound();

                    if (e != null && !e.hasKey("maxTime"))
                    {
                        MinecartModular cart = new MinecartModular(par3World, (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), e, par1ItemStack.getDisplayName());
                        par3World.spawnEntityInWorld(cart);
                    }
                }
                catch (Exception var13)
                {
                    var13.printStackTrace();
                    return false;
                }
            }

            --par1ItemStack.stackSize;
            return true;
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean useExtraInfo)
    {
        CartVersion.updateItemStack(item);
        NBTTagCompound info = item.getTagCompound();

        if (info != null)
        {
            NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
            byte[] bytes = moduleIDTag.func_150292_c();
            ArrayList counts = new ArrayList();
            int maxTime;

            for (maxTime = 0; maxTime < bytes.length; ++maxTime)
            {
                byte currentTime = bytes[maxTime];
                ModuleData timeLeft = (ModuleData)ModuleData.getList().get(Byte.valueOf(currentTime));

                if (timeLeft != null)
                {
                    boolean module = false;

                    if (!info.hasKey("Data" + maxTime))
                    {
                        Iterator name = counts.iterator();

                        while (name.hasNext())
                        {
                            ModuleCountPair count = (ModuleCountPair)name.next();

                            if (count.isContainingData(timeLeft))
                            {
                                count.increase();
                                module = true;
                                break;
                            }
                        }
                    }

                    if (!module)
                    {
                        ModuleCountPair var22 = new ModuleCountPair(timeLeft);

                        if (info.hasKey("Data" + maxTime))
                        {
                            var22.setExtraData(info.getByte("Data" + maxTime));
                        }

                        counts.add(var22);
                    }
                }
            }

            Iterator var15 = counts.iterator();

            while (var15.hasNext())
            {
                ModuleCountPair var17 = (ModuleCountPair)var15.next();
                list.add(var17.toString());
            }

            int var18;

            if (info.hasKey("Spares"))
            {
                byte[] var16 = info.getByteArray("Spares");

                for (var18 = 0; var18 < var16.length; ++var18)
                {
                    byte var19 = var16[var18];
                    ModuleData var21 = (ModuleData)ModuleData.getList().get(Byte.valueOf(var19));

                    if (var21 != null)
                    {
                        String var23 = var21.getName();

                        if (info.hasKey("Data" + (bytes.length + var18)))
                        {
                            var23 = var21.getCartInfoText(var23, info.getByte("Data" + (bytes.length + var18)));
                        }

                        list.add(ColorHelper.ORANGE + var23);
                    }
                }
            }

            if (info.hasKey("maxTime"))
            {
                list.add(ColorHelper.RED + "Incomplete cart!");
                maxTime = info.getInteger("maxTime");
                var18 = info.getInteger("currentTime");
                int var20 = maxTime - var18;
                list.add(ColorHelper.RED + "Time left: " + this.formatTime(var20));
            }
        }
        else
        {
            list.add("No modules loaded");
        }
    }

    private String formatTime(int ticks)
    {
        int seconds = ticks / 20;
        int var10000 = ticks - seconds * 20;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        int hours = minutes / 60;
        minutes -= hours * 60;
        return String.format("%02d:%02d:%02d", new Object[] {Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag()
    {
        return true;
    }
}
