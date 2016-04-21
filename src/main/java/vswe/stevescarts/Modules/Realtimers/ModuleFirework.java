package vswe.stevescarts.Modules.Realtimers;

import java.util.ArrayList;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFirework;

public class ModuleFirework extends ModuleBase
{
    private int fireCooldown;

    public ModuleFirework(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        if (this.fireCooldown > 0)
        {
            --this.fireCooldown;
        }
    }

    public void activatedByRail(int x, int y, int z, boolean active)
    {
        if (active && this.fireCooldown == 0 && this.getCart().hasFuel())
        {
            this.fire();
            this.fireCooldown = 20;
        }
    }

    public boolean hasGui()
    {
        return true;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotFirework(this.getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int guiWidth()
    {
        return 15 + this.getInventoryWidth() * 18;
    }

    public int guiHeight()
    {
        return 20 + this.getInventoryHeight() * 18;
    }

    protected int getInventoryWidth()
    {
        return 8;
    }

    protected int getInventoryHeight()
    {
        return 3;
    }

    public void fire()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            ItemStack firework = this.getFirework();

            if (firework != null)
            {
                this.launchFirework(firework);
            }
        }
    }

    private ItemStack getFirework()
    {
        boolean hasGunpowder = false;
        boolean hasPaper = false;

        for (int firework = 0; firework < this.getInventorySize(); ++firework)
        {
            ItemStack maxGunpowder = this.getStack(firework);

            if (maxGunpowder != null)
            {
                if (maxGunpowder.getItem() == Items.fireworks)
                {
                    ItemStack countGunpowder = maxGunpowder.copy();
                    countGunpowder.stackSize = 1;
                    this.removeItemStack(maxGunpowder, 1, firework);
                    return countGunpowder;
                }

                if (maxGunpowder.getItem() == Items.paper)
                {
                    hasPaper = true;
                }
                else if (maxGunpowder.getItem() == Items.gunpowder)
                {
                    hasGunpowder = true;
                }
            }
        }

        if (hasPaper && hasGunpowder)
        {
            ItemStack var13 = new ItemStack(Items.fireworks);
            int var14 = this.getCart().rand.nextInt(3) + 1;
            int var15 = 0;
            boolean removedPaper = false;
            int chargeCount;

            for (chargeCount = 0; chargeCount < this.getInventorySize(); ++chargeCount)
            {
                ItemStack itemstackNBT = this.getStack(chargeCount);

                if (itemstackNBT != null)
                {
                    if (itemstackNBT.getItem() == Items.paper && !removedPaper)
                    {
                        this.removeItemStack(itemstackNBT, 1, chargeCount);
                        removedPaper = true;
                    }
                    else if (itemstackNBT.getItem() == Items.gunpowder && var15 < var14)
                    {
                        while (itemstackNBT.stackSize > 0 && var15 < var14)
                        {
                            ++var15;
                            this.removeItemStack(itemstackNBT, 1, chargeCount);
                        }
                    }
                }
            }

            for (chargeCount = 1; chargeCount < 7 && this.getCart().rand.nextInt(3 + chargeCount / 3) == 0; ++chargeCount)
            {
                ;
            }

            NBTTagCompound var16 = new NBTTagCompound();
            NBTTagCompound fireworksNBT = new NBTTagCompound();
            NBTTagList explosionsNBT = new NBTTagList();

            for (int i = 0; i < chargeCount; ++i)
            {
                ItemStack charge = this.getCharge();

                if (charge == null)
                {
                    break;
                }

                if (charge.hasTagCompound() && charge.getTagCompound().hasKey("Explosion"))
                {
                    explosionsNBT.appendTag(charge.getTagCompound().getCompoundTag("Explosion"));
                }
            }

            fireworksNBT.setTag("Explosions", explosionsNBT);
            fireworksNBT.setByte("Flight", (byte)var15);
            var16.setTag("Fireworks", fireworksNBT);
            var13.setTagCompound(var16);
            return var13;
        }
        else
        {
            return null;
        }
    }

    private ItemStack getCharge()
    {
        for (int charge = 0; charge < this.getInventorySize(); ++charge)
        {
            ItemStack itemNBT = this.getStack(charge);

            if (itemNBT != null && itemNBT.getItem() == Items.firework_charge)
            {
                ItemStack explosionNBT = itemNBT.copy();
                explosionNBT.stackSize = 1;
                this.removeItemStack(itemNBT, 1, charge);
                return explosionNBT;
            }
        }

        ItemStack var15 = new ItemStack(Items.firework_charge);
        NBTTagCompound var16 = new NBTTagCompound();
        NBTTagCompound var17 = new NBTTagCompound();
        byte type = 0;
        boolean removedGunpowder = false;
        boolean canHasTrail = this.getCart().rand.nextInt(16) == 0;
        boolean canHasFlicker = this.getCart().rand.nextInt(8) == 0;
        boolean canHasModifier = this.getCart().rand.nextInt(4) == 0;
        byte modifierType = (byte)(this.getCart().rand.nextInt(4) + 1);
        boolean removedModifier = false;
        boolean removedDiamond = false;
        boolean removedGlow = false;

        for (int colors = 0; colors < this.getInventorySize(); ++colors)
        {
            ItemStack fade = this.getStack(colors);

            if (fade != null)
            {
                if (fade.getItem() == Items.gunpowder && !removedGunpowder)
                {
                    this.removeItemStack(fade, 1, colors);
                    removedGunpowder = true;
                }
                else if (fade.getItem() == Items.glowstone_dust && canHasFlicker && !removedGlow)
                {
                    this.removeItemStack(fade, 1, colors);
                    removedGlow = true;
                    var17.setBoolean("Flicker", true);
                }
                else if (fade.getItem() == Items.diamond && canHasTrail && !removedDiamond)
                {
                    this.removeItemStack(fade, 1, colors);
                    removedDiamond = true;
                    var17.setBoolean("Trail", true);
                }
                else if (canHasModifier && !removedModifier && (fade.getItem() == Items.fire_charge && modifierType == 1 || fade.getItem() == Items.gold_nugget && modifierType == 2 || fade.getItem() == Items.skull && modifierType == 3 || fade.getItem() == Items.feather && modifierType == 4))
                {
                    this.removeItemStack(fade, 1, colors);
                    removedModifier = true;
                    type = modifierType;
                }
            }
        }

        int[] var18 = this.generateColors(type != 0 ? 7 : 8);

        if (var18 == null)
        {
            return null;
        }
        else
        {
            var17.setIntArray("Colors", var18);

            if (this.getCart().rand.nextInt(4) == 0)
            {
                int[] var19 = this.generateColors(8);

                if (var19 != null)
                {
                    var17.setIntArray("FadeColors", var19);
                }
            }

            var17.setByte("Type", type);
            var16.setTag("Explosion", var17);
            var15.setTagCompound(var16);
            return var15;
        }
    }

    private int[] generateColors(int maxColorCount)
    {
        int[] maxColors = new int[16];
        int[] currentColors = new int[16];
        int colorCount;
        int var10001;

        for (colorCount = 0; colorCount < this.getInventorySize(); ++colorCount)
        {
            ItemStack colorPointers = this.getStack(colorCount);

            if (colorPointers != null && colorPointers.getItem() == Items.dye)
            {
                var10001 = colorPointers.getItemDamage();
                maxColors[var10001] += colorPointers.stackSize;
            }
        }

        for (colorCount = this.getCart().rand.nextInt(2) + 1; colorCount <= maxColorCount - 2 && this.getCart().rand.nextInt(2) == 0; colorCount += 2)
        {
            ;
        }

        ArrayList var11 = new ArrayList();

        for (int usedColors = 0; usedColors < 16; ++usedColors)
        {
            if (maxColors[usedColors] > 0)
            {
                var11.add(Integer.valueOf(usedColors));
            }
        }

        if (var11.size() == 0)
        {
            return null;
        }
        else
        {
            int i;
            ArrayList var12;

            for (var12 = new ArrayList(); colorCount > 0 && var11.size() > 0; --colorCount)
            {
                int colors = this.getCart().rand.nextInt(var11.size());
                i = ((Integer)var11.get(colors)).intValue();
                ++currentColors[i];

                if (--maxColors[i] <= 0)
                {
                    var11.remove(colors);
                }

                var12.add(Integer.valueOf(i));
            }

            int[] var13 = new int[var12.size()];

            for (i = 0; i < var13.length; ++i)
            {
                var13[i] = ItemDye.field_150922_c[((Integer)var12.get(i)).intValue()];
            }

            for (i = 0; i < this.getInventorySize(); ++i)
            {
                ItemStack item = this.getStack(i);

                if (item != null && item.getItem() == Items.dye && currentColors[item.getItemDamage()] > 0)
                {
                    int count = Math.min(currentColors[item.getItemDamage()], item.stackSize);
                    var10001 = item.getItemDamage();
                    currentColors[var10001] -= count;
                }
            }

            return var13;
        }
    }

    private void removeItemStack(ItemStack item, int count, int id)
    {
        if (!this.getCart().hasCreativeSupplies())
        {
            item.stackSize -= count;

            if (item.stackSize <= 0)
            {
                this.setStack(id, (ItemStack)null);
            }
        }
    }

    private void launchFirework(ItemStack firework)
    {
        EntityFireworkRocket rocket = new EntityFireworkRocket(this.getCart().worldObj, this.getCart().posX, this.getCart().posY + 1.0D, this.getCart().posZ, firework);
        this.getCart().worldObj.spawnEntityInWorld(rocket);
    }
}
