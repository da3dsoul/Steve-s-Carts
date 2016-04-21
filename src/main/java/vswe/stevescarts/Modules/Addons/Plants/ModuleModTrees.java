package vswe.stevescarts.Modules.Addons.Plants;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ITreeModule;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

import java.util.ArrayList;

public class ModuleModTrees extends ModuleAddon implements ITreeModule
{
    public ModuleModTrees(MinecartModular cart)
    {
        super(cart);
    }

    public boolean isLeaves(Block b, int x, int y, int z)
    {
        try {
            if (b == null) return false;
            if (b.getMaterial() == Material.air) return false;
            if (b instanceof BlockLeavesBase) return true;
            if (b.isLeaves(this.getCart().worldObj, x, y, z)) return true;

            String blockDisplayName = b.getLocalizedName();

            if (!(blockDisplayName.startsWith("tile.") || blockDisplayName.endsWith(".name"))) {
                if (blockDisplayName.endsWith("Leaves")) return true;
            }

            String blockNameUnlocalized = b.getUnlocalizedName() + ".name";
            if (blockNameUnlocalized.endsWith(".name.name")) {
                blockNameUnlocalized = blockNameUnlocalized.replaceAll(".name.name", ".name");
            }
            String blockName = StatCollector.translateToFallback(blockNameUnlocalized).trim();
            if (blockName.endsWith("Leaves")) return true;

            ItemStack stack = null;

            try {
                stack = b.getPickBlock(null, this.getCart().worldObj, x, y, z);
            } catch (Throwable t) {
            }

            if (stack != null && stack.getItem() != null) {
                ArrayList<ItemStack> oreIds = OreDictionary.getOres("treeLeaves");
                for (ItemStack oreID : oreIds) {
                    if (oreID != null && oreID.getItem() != null) {
                        if (stack.isItemEqual(oreID)) return true;
                    }
                }

                String itemDisplayName = null;
                try {
                    itemDisplayName = stack.getDisplayName();
                } catch (Throwable t) {
                }

                if (itemDisplayName != null && !(itemDisplayName.startsWith("item.") || itemDisplayName.endsWith(".name"))) {
                    if (itemDisplayName.endsWith("Leaves")) return true;
                }

                String itemNameUnlocalized = null;
                try {
                    itemNameUnlocalized = stack.getUnlocalizedName() + ".name";
                } catch (Throwable t) {
                }

                if (itemNameUnlocalized != null) {
                    if (itemNameUnlocalized.endsWith(".name.name")) {
                        itemNameUnlocalized = itemNameUnlocalized.replaceAll(".name.name", ".name");
                    }
                    String itemName = StatCollector.translateToFallback(itemNameUnlocalized).trim();
                    if (itemName.endsWith("Leaves")) return true;
                }
            } else {
                stack = new ItemStack(b, 1, b.damageDropped(this.getCart().worldObj.getBlockMetadata(x, y, z)));
                if (stack != null && stack.getItem() != null) {
                    ArrayList<ItemStack> oreIds = OreDictionary.getOres("treeLeaves");
                    for (ItemStack oreID : oreIds) {
                        if (oreID != null && oreID.getItem() != null) {
                            if (stack.isItemEqual(oreID)) return true;
                        }
                    }

                    String itemDisplayName = null;
                    try {
                        itemDisplayName = stack.getDisplayName();
                    } catch (Throwable t) {
                    }

                    if (itemDisplayName != null && !(itemDisplayName.startsWith("item.") || itemDisplayName.endsWith(".name"))) {
                        if (itemDisplayName.endsWith("Leaves")) return true;
                    }

                    String itemNameUnlocalized = null;
                    try {
                        itemNameUnlocalized = stack.getUnlocalizedName() + ".name";
                    } catch (Throwable t) {
                    }

                    if (itemNameUnlocalized != null) {
                        String itemName = StatCollector.translateToFallback(itemNameUnlocalized).trim();
                        if (itemName.endsWith("Leaves")) return true;
                    }
                }
            }
        } catch (Throwable t) {}

        return false;
    }

    public boolean isWood(Block b, int x, int y, int z)
    {
        try {
            if (b == null) return false;
            if (b.getMaterial() == Material.air) return false;
            if (b instanceof BlockLog) return true;
            if (b.isWood(this.getCart().worldObj, x, y, z)) return true;

            String blockDisplayName = b.getLocalizedName();

            if (!(blockDisplayName.startsWith("tile.") || blockDisplayName.endsWith(".name"))) {
                if (blockDisplayName.endsWith("Wood")) return true;
                if (blockDisplayName.endsWith("Log")) return true;
            }

            String blockNameUnlocalized = b.getUnlocalizedName() + ".name";
            if (blockNameUnlocalized.endsWith(".name.name")) {
                blockNameUnlocalized = blockNameUnlocalized.replaceAll(".name.name", ".name");
            }
            String blockName = StatCollector.translateToFallback(blockNameUnlocalized).trim();
            if (blockName.endsWith("Wood")) return true;
            if (blockName.endsWith("Log")) return true;

            ItemStack stack = null;

            try {
                stack = b.getPickBlock(null, this.getCart().worldObj, x, y, z);
            } catch (Throwable t) {
            }

            if (stack != null && stack.getItem() != null) {
                ArrayList<ItemStack> oreIds = OreDictionary.getOres("logWood");
                for (ItemStack oreID : oreIds) {
                    if (oreID != null && oreID.getItem() != null) {
                        if (stack.isItemEqual(oreID)) return true;
                    }
                }

                String itemDisplayName = null;
                try {
                    itemDisplayName = stack.getDisplayName();
                } catch (Throwable t) {
                }

                if (itemDisplayName != null && !(itemDisplayName.startsWith("item.") || itemDisplayName.endsWith(".name"))) {
                    if (itemDisplayName.endsWith("logWood")) return true;
                }

                String itemNameUnlocalized = null;
                try {
                    itemNameUnlocalized = stack.getUnlocalizedName() + ".name";
                } catch (Throwable t) {
                }

                if (itemNameUnlocalized != null) {
                    if (itemNameUnlocalized.endsWith(".name.name")) {
                        itemNameUnlocalized = itemNameUnlocalized.replaceAll(".name.name", ".name");
                    }
                    String itemName = StatCollector.translateToFallback(itemNameUnlocalized).trim();
                    if (itemName.endsWith("Wood")) return true;
                    if (itemName.endsWith("Log")) return true;
                }
            } else {
                stack = new ItemStack(b, 1, b.damageDropped(this.getCart().worldObj.getBlockMetadata(x, y, z)));
                if (stack != null && stack.getItem() != null) {
                    ArrayList<ItemStack> oreIds = OreDictionary.getOres("logWood");
                    for (ItemStack oreID : oreIds) {
                        if (oreID != null && oreID.getItem() != null) {
                            if (stack.isItemEqual(oreID)) return true;
                        }
                    }

                    String itemDisplayName = null;
                    try {
                        itemDisplayName = stack.getDisplayName();
                    } catch (Throwable t) {
                    }

                    if (itemDisplayName != null && !(itemDisplayName.startsWith("item.") || itemDisplayName.endsWith(".name"))) {
                        if (itemDisplayName.endsWith("logWood")) return true;
                    }

                    String itemNameUnlocalized = null;
                    try {
                        itemNameUnlocalized = stack.getUnlocalizedName() + ".name";
                    } catch (Throwable t) {
                    }

                    if (itemNameUnlocalized != null) {
                        if (itemNameUnlocalized.endsWith(".name.name")) {
                            itemNameUnlocalized = itemNameUnlocalized.replaceAll(".name.name", ".name");
                        }
                        String itemName = StatCollector.translateToFallback(itemNameUnlocalized).trim();
                        if (itemName.endsWith("Wood")) return true;
                        if (itemName.endsWith("Log")) return true;
                    }
                }
            }
        } catch (Throwable t) {}

        return false;
    }

    public boolean isSapling(ItemStack sapling)
    {
        if (sapling != null)
        {
            if (this.isStackSapling(sapling))
            {
                return true;
            }

            if (sapling.getItem() instanceof ItemBlock)
            {
                Block b = Block.getBlockFromItem(sapling.getItem());

                if (b instanceof BlockSapling)
                {
                    return true;
                }

                if(b != null && this.isStackSapling(new ItemStack(b, 1, 32767)))
                    return true;
            }

            Block block1 = Block.getBlockFromItem(sapling.getItem());
            if(block1 != null) {
                if(block1 instanceof IGrowable) return true;
            }
        }

        return false;
    }

    private boolean isStackSapling(ItemStack sapling)
    {
        int[] ids = OreDictionary.getOreIDs(sapling);
        for(int id : ids) {
            String name = OreDictionary.getOreName(id);
            return name != null && name.startsWith("treeSapling");
        }
        return false;
    }
}
