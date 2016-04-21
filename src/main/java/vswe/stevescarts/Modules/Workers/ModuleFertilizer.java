package vswe.stevescarts.Modules.Workers;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotFertilizer;

public class ModuleFertilizer extends ModuleWorker implements ISuppliesModule
{
    private int tankPosX = this.guiWidth() - 21;
    private int tankPosY = 20;
    private int range = 1;
    private int fert = 0;
    private final int fertPerBonemeal = 4;
    private final int maxStacksOfBones = 1;

    public ModuleFertilizer(MinecartModular cart)
    {
        super(cart);
    }

    public byte getWorkPriority()
    {
        return (byte)127;
    }

    public boolean hasGui()
    {
        return true;
    }

    protected int getInventoryWidth()
    {
        return 1;
    }

    public void init()
    {
        super.init();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleFarmer)
            {
                this.range = ((ModuleFarmer)module).getExternalRange();
                break;
            }
        }
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/fertilize.png");
        this.drawImage(gui, this.tankPosX, this.tankPosY, 0, 0, 18, 27);
        float percentage = (float)this.fert / (float)this.getMaxFert();
        int size = (int)(percentage * 23.0F);
        this.drawImage(gui, this.tankPosX + 2, this.tankPosY + 2 + (23 - size), 18, 23 - size, 14, size);
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        this.drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.FERTILIZERS.translate(new String[0]) + ": " + this.fert + " / " + this.getMaxFert(), x, y, this.tankPosX, this.tankPosY, 18, 27);
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int guiWidth()
    {
        return super.guiWidth() + 25;
    }

    public int guiHeight()
    {
        return Math.max(super.guiHeight(), 50);
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotFertilizer(this.getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    public boolean work()
    {
        Vec3 next = this.getNextblock();
        int x = (int)next.xCoord;
        int y = (int)next.yCoord;
        int z = (int)next.zCoord;

        for (int i = -this.range; i <= this.range; ++i)
        {
            for (int j = -this.range; j <= this.range; ++j)
            {
                int coordX = x + i;
                int coordY = y - 1;
                int coordZ = z + j;
                this.fertilize(coordX, coordY, coordZ);
            }
        }

        return false;
    }

    private void fertilize(int x, int y, int z)
    {
        Block block = this.getCart().worldObj.getBlock(x, y + 1, z);
        int metadataOfBlockAbove = this.getCart().worldObj.getBlockMetadata(x, y + 1, z);
        int metadata = this.getCart().worldObj.getBlockMetadata(x, y, z);

        if (this.fert > 0)
        {
            if (block instanceof BlockCrops && metadataOfBlockAbove != 7)
            {
                if (metadata > 0 && this.getCart().rand.nextInt(250) == 0 || metadata == 0 && this.getCart().rand.nextInt(1000) == 0)
                {
                    this.getCart().worldObj.setBlockMetadataWithNotify(x, y + 1, z, metadataOfBlockAbove + 1, 3);
                    --this.fert;
                }
            }
            else if (block instanceof BlockSapling && this.getCart().worldObj.getBlockLightValue(x, y + 2, z) >= 9 && this.getCart().rand.nextInt(100) == 0)
            {
                if (this.getCart().rand.nextInt(6) == 0)
                {
                    this.getCart().worldObj.setBlockMetadataWithNotify(x, y + 1, z, metadataOfBlockAbove | 8, 3);
                    ((BlockSapling)Blocks.sapling).func_149878_d(this.getCart().worldObj, x, y + 1, z, this.getCart().rand);
                }

                --this.fert;
            }
        }
    }

    public int numberOfGuiData()
    {
        return 1;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.fert);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.fert = data;
        }
    }

    public void update()
    {
        super.update();
        this.loadSupplies();
    }

    private void loadSupplies()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            if (this.getStack(0) != null)
            {
                boolean isBone = this.getStack(0).getItem() == Items.bone;
                boolean isBoneMeal = this.getStack(0).getItem() == Items.dye && this.getStack(0).getItemDamage() == 15;

                if (isBone || isBoneMeal)
                {
                    byte amount;

                    if (isBoneMeal)
                    {
                        amount = 1;
                    }
                    else
                    {
                        amount = 3;
                    }

                    if (this.fert <= 4 * (192 - amount) && this.getStack(0).stackSize > 0)
                    {
                        --this.getStack(0).stackSize;
                        this.fert += amount * 4;
                    }

                    if (this.getStack(0).stackSize == 0)
                    {
                        this.setStack(0, (ItemStack)null);
                    }
                }
            }
        }
    }

    private int getMaxFert()
    {
        return 768;
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setShort(this.generateNBTName("Fert", id), (short)this.fert);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.fert = tagCompound.getShort(this.generateNBTName("Fert", id));
    }

    public boolean haveSupplies()
    {
        return this.fert > 0;
    }
}
