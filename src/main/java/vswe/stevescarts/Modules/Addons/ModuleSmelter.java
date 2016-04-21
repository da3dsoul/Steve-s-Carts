package vswe.stevescarts.Modules.Addons;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCartCrafterResult;
import vswe.stevescarts.Slots.SlotFurnaceInput;

public class ModuleSmelter extends ModuleRecipe
{
    private int energyBuffer;
    private int cooldown = 0;

    public ModuleSmelter(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        if (!this.getCart().worldObj.isRemote)
        {
            if (this.getCart().hasFuelForModule() && this.energyBuffer < 10)
            {
                ++this.energyBuffer;
            }

            if (this.cooldown <= 0)
            {
                if (this.energyBuffer == 10)
                {
                    ItemStack recipe = this.getStack(0);
                    ItemStack result = null;

                    if (recipe != null)
                    {
                        result = FurnaceRecipes.smelting().getSmeltingResult(recipe);
                    }

                    if (result != null)
                    {
                        result = result.copy();
                    }

                    if (result != null && this.getCart().getModules() != null)
                    {
                        this.prepareLists();

                        if (this.canCraftMoreOfResult(result))
                        {
                            ArrayList originals = new ArrayList();
                            int i;
                            ItemStack item;

                            for (i = 0; i < this.allTheSlots.size(); ++i)
                            {
                                item = ((SlotBase)this.allTheSlots.get(i)).getStack();
                                originals.add(item == null ? null : item.copy());
                            }

                            label61:

                            for (i = 0; i < this.inputSlots.size(); ++i)
                            {
                                item = ((SlotBase)this.inputSlots.get(i)).getStack();

                                if (item != null && item.isItemEqual(recipe) && ItemStack.areItemStackTagsEqual(item, recipe))
                                {
                                    if (--item.stackSize <= 0)
                                    {
                                        ((SlotBase)this.inputSlots.get(i)).putStack((ItemStack)null);
                                    }

                                    this.getCart().addItemToChest(result, this.getValidSlot(), (Class)null);

                                    if (result.stackSize != 0)
                                    {
                                        int j = 0;

                                        while (true)
                                        {
                                            if (j >= this.allTheSlots.size())
                                            {
                                                break label61;
                                            }

                                            ((SlotBase)this.allTheSlots.get(j)).putStack((ItemStack)originals.get(j));
                                            ++j;
                                        }
                                    }
                                    else
                                    {
                                        this.energyBuffer = 0;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                this.cooldown = 40;
            }
            else
            {
                --this.cooldown;
            }
        }
    }

    public int getConsumption(boolean isMoving)
    {
        return this.energyBuffer < 10 ? 15 : super.getConsumption(isMoving);
    }

    public boolean hasGui()
    {
        return true;
    }

    protected int getInventoryWidth()
    {
        return 1;
    }

    protected int getInventoryHeight()
    {
        return 2;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return (SlotBase)(y == 0 ? new SlotFurnaceInput(this.getCart(), slotId, 10 + 18 * x, 15 + 18 * y) : new SlotCartCrafterResult(this.getCart(), slotId, 10 + 18 * x, 15 + 18 * y));
    }

    public int numberOfGuiData()
    {
        return super.numberOfGuiData() + 1;
    }

    protected void checkGuiData(Object[] info)
    {
        super.checkGuiData(info);
        this.updateGuiData(info, super.numberOfGuiData() + 0, (short)this.energyBuffer);
    }

    public void receiveGuiData(int id, short data)
    {
        super.receiveGuiData(id, data);

        if (id == super.numberOfGuiData() + 0)
        {
            this.energyBuffer = data;
        }
    }

    public void onInventoryChanged()
    {
        if (this.getCart().worldObj.isRemote)
        {
            if (this.getStack(0) != null)
            {
                this.setStack(1, FurnaceRecipes.smelting().getSmeltingResult(this.getStack(0)));
            }
            else
            {
                this.setStack(1, (ItemStack)null);
            }
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        super.drawForeground(gui);
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int guiWidth()
    {
        return this.canUseAdvancedFeatures() ? 100 : 45;
    }

    protected int[] getArea()
    {
        return new int[] {32, 25, 16, 16};
    }

    protected boolean canUseAdvancedFeatures()
    {
        return false;
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        super.Load(tagCompound, id);
        this.energyBuffer = tagCompound.getByte(this.generateNBTName("Buffer", id));
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        super.Save(tagCompound, id);
        tagCompound.setByte(this.generateNBTName("Buffer", id), (byte)this.energyBuffer);
    }

    protected int getLimitStartX()
    {
        return 55;
    }

    protected int getLimitStartY()
    {
        return 15;
    }
}
