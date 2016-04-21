package vswe.stevescarts.Modules.Realtimers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotMilker;

public class ModuleMilker extends ModuleBase
{
    int cooldown = 0;
    int milkbuffer = 0;

    public ModuleMilker(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();

        if (this.cooldown <= 0)
        {
            if (!this.getCart().worldObj.isRemote && this.getCart().hasFuel())
            {
                this.generateMilk();
                this.depositeMilk();
            }

            this.cooldown = 20;
        }
        else
        {
            --this.cooldown;
        }
    }

    private void depositeMilk()
    {
        if (this.milkbuffer > 0)
        {
            FluidStack ret = FluidContainerRegistry.getFluidForFilledItem(new ItemStack(Items.milk_bucket));

            if (ret != null)
            {
                ret.amount = this.milkbuffer;
                this.milkbuffer -= this.getCart().fill(ret, true);
            }

            if (this.milkbuffer == 1000)
            {
                for (int i = 0; i < this.getInventorySize(); ++i)
                {
                    ItemStack bucket = this.getStack(i);

                    if (bucket != null && bucket.getItem() == Items.bucket)
                    {
                        ItemStack milk = new ItemStack(Items.milk_bucket);
                        this.getCart().addItemToChest(milk);

                        if (milk.stackSize <= 0)
                        {
                            this.milkbuffer = 0;

                            if (--bucket.stackSize <= 0)
                            {
                                this.setStack(i, (ItemStack)null);
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateMilk()
    {
        if (this.milkbuffer < 1000)
        {
            Entity rider = this.getCart().riddenByEntity;

            if (rider != null && rider instanceof EntityCow)
            {
                this.milkbuffer = Math.min(this.milkbuffer + 75, 1000);
            }
        }
    }

    public boolean hasGui()
    {
        return true;
    }

    protected int getInventoryWidth()
    {
        return 2;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotMilker(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.generateNBTName("Milk", id), (short)this.milkbuffer);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.milkbuffer = tagCompound.getShort(this.generateNBTName("Milk", id));
    }
}
