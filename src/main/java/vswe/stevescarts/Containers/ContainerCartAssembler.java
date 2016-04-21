package vswe.stevescarts.Containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Slots.SlotAssembler;
import vswe.stevescarts.Slots.SlotHull;
import vswe.stevescarts.TileEntities.TileEntityBase;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class ContainerCartAssembler extends ContainerBase
{
    private TileEntityCartAssembler assembler;
    public int lastMaxAssemblingTime;
    public int lastCurrentAssemblingTime;
    public boolean lastIsAssembling;
    public int lastFuelLevel;

    public IInventory getMyInventory()
    {
        return this.assembler;
    }

    public TileEntityBase getTileEntity()
    {
        return this.assembler;
    }

    public ContainerCartAssembler(IInventory invPlayer, TileEntityCartAssembler assembler)
    {
        this.assembler = assembler;
        ArrayList slots = assembler.getSlots();
        Iterator j = slots.iterator();

        while (j.hasNext())
        {
            SlotAssembler k = (SlotAssembler)j.next();
            this.addSlotToContainer(k);
        }

        int var6;

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (int var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(invPlayer, var7 + var6 * 9 + 9, this.offsetX() + var7 * 18, var6 * 18 + this.offsetY()));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(invPlayer, var6, this.offsetX() + var6 * 18, 58 + this.offsetY()));
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.assembler.isUseableByPlayer(entityplayer);
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        this.assembler.initGuiData(this, par1ICrafting);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        par2 &= 65535;
        this.assembler.receiveGuiData(par1, (short)par2);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        Iterator var1 = this.crafters.iterator();

        while (var1.hasNext())
        {
            ICrafting var2 = (ICrafting)var1.next();
            this.assembler.checkGuiData(this, var2);
        }
    }

    protected int offsetX()
    {
        return 176;
    }

    protected int offsetY()
    {
        return 174;
    }

    public ItemStack slotClick(int slotID, int button, int keyflag, EntityPlayer player)
    {
        if (slotID >= 0 && slotID < this.inventorySlots.size())
        {
            Slot hullSlot = (Slot)this.inventorySlots.get(slotID);

            if (hullSlot != null && hullSlot instanceof SlotHull)
            {
                InventoryPlayer playerInventory = player.inventory;
                ItemStack playerItem = playerInventory.getItemStack();
                ItemStack slotItem = hullSlot.getStack();
                ArrayList newSlots = this.assembler.getValidSlotFromHullItem(playerItem);
                ArrayList oldSlots = this.assembler.getValidSlotFromHullItem(slotItem);

                if (oldSlots != null)
                {
                    Iterator i$;
                    SlotAssembler slot;

                    if (newSlots != null)
                    {
                        i$ = newSlots.iterator();

                        while (i$.hasNext())
                        {
                            slot = (SlotAssembler)i$.next();
                            int index = oldSlots.indexOf(slot);

                            if (index != -1)
                            {
                                oldSlots.remove(index);
                            }
                        }
                    }

                    i$ = oldSlots.iterator();

                    while (i$.hasNext())
                    {
                        slot = (SlotAssembler)i$.next();

                        if (slot.getHasStack())
                        {
                            return null;
                        }
                    }
                }
            }
        }

        return super.slotClick(slotID, button, keyflag, player);
    }
}
