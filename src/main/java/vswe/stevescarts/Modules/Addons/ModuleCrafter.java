package vswe.stevescarts.Modules.Addons;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CraftingDummy;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCartCrafter;
import vswe.stevescarts.Slots.SlotCartCrafterResult;

public class ModuleCrafter extends ModuleRecipe
{
    private CraftingDummy dummy = new CraftingDummy(this);
    private int cooldown = 0;

    public ModuleCrafter(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        if (this.cooldown <= 0)
        {
            if (!this.getCart().worldObj.isRemote && this.getValidSlot() != null)
            {
                ItemStack result = this.dummy.getResult();

                if (result != null && this.getCart().getModules() != null)
                {
                    if (result.stackSize == 0)
                    {
                        result.stackSize = 1;
                    }

                    this.prepareLists();

                    if (this.canCraftMoreOfResult(result))
                    {
                        ArrayList originals = new ArrayList();

                        for (int containers = 0; containers < this.allTheSlots.size(); ++containers)
                        {
                            ItemStack valid = ((SlotBase)this.allTheSlots.get(containers)).getStack();
                            originals.add(valid == null ? null : valid.copy());
                        }

                        ArrayList var10 = new ArrayList();
                        boolean var11 = true;
                        boolean edited = false;
                        int i;
                        ItemStack container;

                        for (i = 0; i < 9; ++i)
                        {
                            container = this.getStack(i);

                            if (container != null)
                            {
                                var11 = false;

                                for (int j = 0; j < this.inputSlots.size(); ++j)
                                {
                                    ItemStack item = ((SlotBase)this.inputSlots.get(j)).getStack();

                                    if (item != null && item.isItemEqual(container) && ItemStack.areItemStackTagsEqual(item, container))
                                    {
                                        edited = true;

                                        if (item.getItem().hasContainerItem(item))
                                        {
                                            var10.add(item.getItem().getContainerItem(item));
                                        }

                                        --item.stackSize;

                                        if (item.stackSize <= 0)
                                        {
                                            ((SlotBase)this.inputSlots.get(j)).putStack((ItemStack)null);
                                        }

                                        var11 = true;
                                        break;
                                    }
                                }

                                if (!var11)
                                {
                                    break;
                                }
                            }
                        }

                        if (var11)
                        {
                            this.getCart().addItemToChest(result, this.getValidSlot(), (Class)null);

                            if (result.stackSize > 0)
                            {
                                var11 = false;
                            }
                            else
                            {
                                edited = true;

                                for (i = 0; i < var10.size(); ++i)
                                {
                                    container = (ItemStack)var10.get(i);

                                    if (container != null)
                                    {
                                        this.getCart().addItemToChest(container, this.getValidSlot(), (Class)null);

                                        if (container.stackSize > 0)
                                        {
                                            var11 = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (!var11 && edited)
                        {
                            for (i = 0; i < this.allTheSlots.size(); ++i)
                            {
                                ((SlotBase)this.allTheSlots.get(i)).putStack((ItemStack)originals.get(i));
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

    protected int[] getArea()
    {
        return new int[] {68, 44, 16, 16};
    }

    public boolean hasGui()
    {
        return true;
    }

    public int getInventorySize()
    {
        return 10;
    }

    public int generateSlots(int slotCount)
    {
        this.slotGlobalStart = slotCount;
        this.slotList = new ArrayList();

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                this.slotList.add(new SlotCartCrafter(this.getCart(), slotCount++, 10 + 18 * x, 15 + 18 * y));
            }
        }

        this.slotList.add(new SlotCartCrafterResult(this.getCart(), slotCount++, 67, this.canUseAdvancedFeatures() ? 20 : 33));
        return slotCount;
    }

    public void onInventoryChanged()
    {
        if (this.getCart().worldObj.isRemote)
        {
            this.dummy.update();
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        super.drawForeground(gui);
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);
    }

    public int guiWidth()
    {
        return this.canUseAdvancedFeatures() ? 120 : 95;
    }

    public int guiHeight()
    {
        return 75;
    }

    protected boolean canUseAdvancedFeatures()
    {
        return false;
    }

    protected int getLimitStartX()
    {
        return 90;
    }

    protected int getLimitStartY()
    {
        return 23;
    }
}
