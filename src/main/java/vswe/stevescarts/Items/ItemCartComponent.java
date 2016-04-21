package vswe.stevescarts.Items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.EntityEasterEgg;

public class ItemCartComponent extends Item
{
    private IIcon[] icons;
    private IIcon unknownIcon;

    public static int size()
    {
        return ComponentTypes.values().length;
    }

    public ItemCartComponent()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(StevesCarts.tabsSC2Components);
    }

    private String getName(int dmg)
    {
        return ComponentTypes.values()[dmg].getName();
    }

    public String getName(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.getItemDamage() >= 0 && par1ItemStack.getItemDamage() < size() && this.getName(par1ItemStack.getItemDamage()) != null ? this.getName(par1ItemStack.getItemDamage()) : "Unknown SC2 Component";
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int dmg)
    {
        return dmg >= 0 && dmg < this.icons.length && this.icons[dmg] != null ? this.icons[dmg] : this.unknownIcon;
    }

    private String getRawName(int i)
    {
        return this.getName(i).replace(":", "").replace(" ", "_").toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        this.icons = new IIcon[size()];

        for (int i = 0; i < this.icons.length; ++i)
        {
            if (this.getName(i) != null)
            {
                IIcon[] var10000 = this.icons;
                StringBuilder var10003 = new StringBuilder();
                StevesCarts.instance.getClass();
                var10000[i] = register.registerIcon(var10003.append("stevescarts").append(":").append(this.getRawName(i)).append("_icon").toString());
            }
        }

        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.unknownIcon = register.registerIcon(var10002.append("stevescarts").append(":").append("unknown_icon").toString());
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack item)
    {
        return item != null && item.getItemDamage() >= 0 && item.getItemDamage() < size() && this.getName(item.getItemDamage()) != null ? "item.SC2:" + this.getRawName(item.getItemDamage()) : this.getUnlocalizedName();
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getUnlocalizedName()
    {
        return "item.SC2:unknowncomponent";
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack == null || par1ItemStack.getItemDamage() < 0 || par1ItemStack.getItemDamage() >= size() || this.getName(par1ItemStack.getItemDamage()) == null)
        {
            if (par1ItemStack != null && par1ItemStack.getItem() instanceof ItemCartComponent)
            {
                par3List.add("Component id " + par1ItemStack.getItemDamage());
            }
            else
            {
                par3List.add("Unknown component id");
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * This returns the sub items
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < size(); ++i)
        {
            ItemStack iStack = new ItemStack(par1, 1, i);

            if (this.isValid(iStack))
            {
                par3List.add(iStack);
            }
        }
    }

    public boolean isValid(ItemStack item)
    {
        return item != null && item.getItem() instanceof ItemCartComponent && this.getName(item.getItemDamage()) != null ? (item.getItemDamage() >= 50 && item.getItemDamage() < 58 ? StevesCarts.isChristmas : (item.getItemDamage() >= 66 && item.getItemDamage() < 72 ? StevesCarts.isEaster : item.getItemDamage() < 72 || item.getItemDamage() >= 80)) : false;
    }

    public static ItemStack getWood(int type, boolean isLog)
    {
        return getWood(type, isLog, 1);
    }

    public static ItemStack getWood(int type, boolean isLog, int count)
    {
        return new ItemStack(ModItems.component, count, 72 + type * 2 + (isLog ? 0 : 1));
    }

    public static boolean isWoodLog(ItemStack item)
    {
        return item != null && item.getItemDamage() >= 72 && item.getItemDamage() < 80 ? (item.getItemDamage() - 72) % 2 == 0 : false;
    }

    public static boolean isWoodTwig(ItemStack item)
    {
        return item != null && item.getItemDamage() >= 72 && item.getItemDamage() < 80 ? (item.getItemDamage() - 72) % 2 == 1 : false;
    }

    private boolean isEdibleEgg(ItemStack item)
    {
        return item != null && item.getItemDamage() >= 66 && item.getItemDamage() < 70;
    }

    private boolean isThrowableEgg(ItemStack item)
    {
        return item != null && item.getItemDamage() == 70;
    }

    public ItemStack onEaten(ItemStack item, World world, EntityPlayer player)
    {
        if (this.isEdibleEgg(item))
        {
            if (item.getItemDamage() == 66)
            {
                world.createExplosion((Entity)null, player.posX, player.posY, player.posZ, 0.1F, false);
            }
            else if (item.getItemDamage() == 67)
            {
                player.setFire(5);

                if (!world.isRemote)
                {
                    player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 600, 0));
                }
            }
            else if (item.getItemDamage() == 68)
            {
                if (!world.isRemote)
                {
                    player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 50, 2));
                }
            }
            else if (item.getItemDamage() == 69)
            {
                if (!world.isRemote)
                {
                    player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 300, 4));
                }
            }
            else if (item.getItemDamage() == 70)
            {
                ;
            }

            if (!player.capabilities.isCreativeMode)
            {
                --item.stackSize;
            }

            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            player.getFoodStats().addStats(2, 0.0F);
            return item;
        }
        else
        {
            return super.onEaten(item, world, player);
        }
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack item)
    {
        return this.isEdibleEgg(item) ? 32 : super.getMaxItemUseDuration(item);
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack item)
    {
        return this.isEdibleEgg(item) ? EnumAction.eat : super.getItemUseAction(item);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
        if (this.isEdibleEgg(item))
        {
            player.setItemInUse(item, this.getMaxItemUseDuration(item));
            return item;
        }
        else if (this.isThrowableEgg(item))
        {
            if (!player.capabilities.isCreativeMode)
            {
                --item.stackSize;
            }

            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!world.isRemote)
            {
                world.spawnEntityInWorld(new EntityEasterEgg(world, player));
            }

            return item;
        }
        else
        {
            return super.onItemRightClick(item, world, player);
        }
    }
}
