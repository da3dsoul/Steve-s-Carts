package vswe.stevescarts.Modules.Engines;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFuel;

public abstract class ModuleCoalBase extends ModuleEngine
{
    private int fireCoolDown;
    private int fireIndex;

    public ModuleCoalBase(MinecartModular cart)
    {
        super(cart);
    }

    protected void loadFuel()
    {
        int consumption = this.getCart().getConsumption(true) * 2;

        if (this.getFuelLevel() <= consumption)
        {
            for (int i = 0; i < this.getInventorySize(); ++i)
            {
                this.setFuelLevel(this.getFuelLevel() + SlotFuel.getItemBurnTime(this, this.getStack(i)));

                if (this.getFuelLevel() > consumption)
                {
                    if (this.getStack(i) != null)
                    {
                        if (this.getStack(i).getItem().hasContainerItem(this.getStack(i)))
                        {
                            this.setStack(i, new ItemStack(this.getStack(i).getItem().getContainerItem()));
                        }
                        else
                        {
                            --this.getStack(i).stackSize;
                        }

                        if (this.getStack(i).stackSize == 0)
                        {
                            this.setStack(i, (ItemStack)null);
                        }
                    }

                    break;
                }
            }
        }
    }

    public int getTotalFuel()
    {
        int totalfuel = this.getFuelLevel();

        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            if (this.getStack(i) != null)
            {
                totalfuel += SlotFuel.getItemBurnTime(this, this.getStack(i)) * this.getStack(i).stackSize;
            }
        }

        return totalfuel;
    }

    public float[] getGuiBarColor()
    {
        return new float[] {0.0F, 0.0F, 0.0F};
    }

    public void smoke()
    {
        double oX = 0.0D;
        double oZ = 0.0D;

        if (this.getCart().motionX != 0.0D)
        {
            oX = (double)(this.getCart().motionX > 0.0D ? -1 : 1);
        }

        if (this.getCart().motionZ != 0.0D)
        {
            oZ = (double)(this.getCart().motionZ > 0.0D ? -1 : 1);
        }

        if (this.getCart().rand.nextInt(2) == 0)
        {
            this.getCart().worldObj.spawnParticle("largesmoke", this.getCart().posX + oX * 0.85D, this.getCart().posY + 0.12D, this.getCart().posZ + oZ * 0.85D, 0.0D, 0.0D, 0.0D);
        }

        if (this.getCart().rand.nextInt(30) == 0)
        {
            this.getCart().worldObj.spawnParticle("flame", this.getCart().posX + oX * 0.75D, this.getCart().posY + 0.15D, this.getCart().posZ + oZ * 0.75D, this.getCart().motionX, this.getCart().motionY, this.getCart().motionZ);
        }
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotFuel(this.getCart(), slotId, 8 + x * 18, 23 + 18 * y);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ENGINES.COAL.translate(new String[0]), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_FUEL.translate(new String[0]);

        if (this.getFuelLevel() > 0)
        {
            strfuel = Localization.MODULES.ENGINES.FUEL.translate(new String[] {String.valueOf(this.getFuelLevel())});
        }

        this.drawString(gui, strfuel, 8, 42, 4210752);
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.getFuelLevel());
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.setFuelLevel(data);

            if (this.getFuelLevel() < 0)
            {
                this.setFuelLevel(this.getFuelLevel() + 65536);
            }
        }
    }

    public void update()
    {
        super.update();

        if (this.fireCoolDown <= 0)
        {
            this.fireIndex = this.getCart().rand.nextInt(4) + 1;
            this.fireCoolDown = 2;
        }
        else
        {
            --this.fireCoolDown;
        }
    }

    public int getFireIndex()
    {
        return this.getCart().isEngineBurning() ? this.fireIndex : 0;
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setShort(this.generateNBTName("Fuel", id), (short)this.getFuelLevel());
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.setFuelLevel(tagCompound.getShort(this.generateNBTName("Fuel", id)));

        if (this.getFuelLevel() < 0)
        {
            this.setFuelLevel(this.getFuelLevel() + 65536);
        }
    }

    public abstract double getFuelMultiplier();
}
