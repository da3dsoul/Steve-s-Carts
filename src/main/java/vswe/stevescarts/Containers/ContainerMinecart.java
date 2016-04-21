package vswe.stevescarts.Containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.TileEntities.TileEntityBase;

public class ContainerMinecart extends ContainerBase
{
    private IInventory player;
    public HashMap<Short, Short> cache;
    public MinecartModular cart;

    public ContainerMinecart(IInventory player, MinecartModular cart)
    {
        this.cartInv(cart);
        this.playerInv(player);
    }

    public IInventory getMyInventory()
    {
        return this.cart;
    }

    public TileEntityBase getTileEntity()
    {
        return null;
    }

    protected void cartInv(MinecartModular cart)
    {
        this.cart = cart;

        if (cart.getModules() != null)
        {
            Iterator i = cart.getModules().iterator();

            while (i.hasNext())
            {
                ModuleBase module = (ModuleBase)i.next();

                if (module.hasSlots())
                {
                    ArrayList slotsList = module.getSlots();
                    Iterator i$ = slotsList.iterator();

                    while (i$.hasNext())
                    {
                        SlotBase slot = (SlotBase)i$.next();
                        slot.xDisplayPosition = slot.getX() + module.getX() + 1;
                        slot.yDisplayPosition = slot.getY() + module.getY() + 1;
                        this.addSlotToContainer(slot);
                    }
                }
            }
        }
        else
        {
            for (int var7 = 0; var7 < 100; ++var7)
            {
                this.addSlotToContainer(new Slot(cart, var7, -1000, -1000));
            }
        }
    }

    protected void playerInv(IInventory player)
    {
        this.player = player;
        int j;

        for (j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(player, k + j * 9 + 9, this.offsetX() + k * 18, j * 18 + this.offsetY()));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(player, j, this.offsetX() + j * 18, 58 + this.offsetY()));
        }
    }

    protected int offsetX()
    {
        return 159;
    }

    protected int offsetY()
    {
        return 174;
    }

    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.cart.isUseableByPlayer(entityplayer);
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.cart.closeInventory();
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        ModuleBase module;

        if (this.cart.getModules() != null)
        {
            for (Iterator i$ = this.cart.getModules().iterator(); i$.hasNext(); module = (ModuleBase)i$.next())
            {
                ;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        par2 &= 65535;

        if (this.cart.getModules() != null)
        {
            Iterator i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();

                if (par1 >= module.getGuiDataStart() && par1 < module.getGuiDataStart() + module.numberOfGuiData())
                {
                    module.receiveGuiData(par1 - module.getGuiDataStart(), (short)par2);
                    break;
                }
            }
        }
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (this.cart.getModules() != null && this.crafters.size() > 0)
        {
            Iterator i$ = this.cart.getModules().iterator();

            while (i$.hasNext())
            {
                ModuleBase module = (ModuleBase)i$.next();
                module.checkGuiData(this, this.crafters, false);
            }
        }
    }
}
