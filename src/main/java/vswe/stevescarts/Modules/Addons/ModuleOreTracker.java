package vswe.stevescarts.Modules.Addons;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.oredict.OreDictionary;
import scala.reflect.internal.Trees;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ModuleOreTracker extends ModuleAddon
{
    public ModuleOreTracker(MinecartModular cart)
    {
        super(cart);
    }

    public BlockCoord findBlockToMine(ModuleDrill drill, BlockCoord start)
    {
        return this.findBlockToMine(drill, new ArrayList(), start, true);
    }

    private BlockCoord findBlockToMine(ModuleDrill drill, ArrayList<BlockCoord> checked, BlockCoord current, boolean first)
    {
        if (current != null && !checked.contains(current) && (first || this.isOre(current)))
        {
            checked.add(current);

            if (checked.size() < 200)
            {
                for (int x = -1; x <= 1; ++x)
                {
                    for (int y = -1; y <= 1; ++y)
                    {
                        for (int z = -1; z <= 1; ++z)
                        {
                            if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1)
                            {
                                BlockCoord ret = this.findBlockToMine(drill, checked, new BlockCoord(current.getX() + x, current.getY() + y, current.getZ() + z), false);

                                if (ret != null)
                                {
                                    return ret;
                                }
                            }
                        }
                    }
                }
            }

            return first && !this.isOre(current) ? null : (drill.isValidBlock(current.getX(), current.getY(), current.getZ(), 0, 1, true) == null ? null : current);
        }
        else
        {
            return null;
        }
    }

    private boolean isOre(BlockCoord coord)
    {
        Block b = this.getCart().worldObj.getBlock(coord.getX(), coord.getY(), coord.getZ());

        if (b != null && b.getMaterial() != Material.air)
        {
            if (b instanceof BlockOre)
            {
                return true;
            }

            if(b.getClass().getSimpleName().equalsIgnoreCase("BlockOreTile")) return true;

            String blockDisplayName = b.getLocalizedName();

            if(!(blockDisplayName.startsWith("tile.") || blockDisplayName.endsWith(".name"))) {
                if(blockDisplayName.endsWith("Ore")) return true;
                if(inInclusionFilter(blockDisplayName)) return true;
            }

            String blockNameUnlocalized = b.getUnlocalizedName()+".name";
            if(blockNameUnlocalized.endsWith(".name.name")) {
                blockNameUnlocalized = blockNameUnlocalized.replaceAll(".name.name", ".name");
            }
            String blockName = StatCollector.translateToFallback(blockNameUnlocalized).trim();
            if(blockName.endsWith("Ore")) return true;
            if(inInclusionFilter(blockName)) return true;

            ItemStack stack = null;

            try {
                stack = b.getPickBlock(null, this.getCart().worldObj,coord.getX(),coord.getY(),coord.getZ());
            } catch (Throwable t) {}

            if(stack != null) {
                int[] oreIds = OreDictionary.getOreIDs(stack);
                for(int oreID : oreIds) {
                    String oreName = OreDictionary.getOreName(oreID);
                    if(oreName != null && oreName.startsWith("ore")) return true;
                }

                String itemDisplayName = null;
                try {
                    itemDisplayName = stack.getDisplayName();
                } catch (Throwable t) {}

                if(itemDisplayName != null && !(itemDisplayName.startsWith("item.") || itemDisplayName.endsWith(".name"))) {
                    if(itemDisplayName.endsWith("Ore")) return true;
                    if(inInclusionFilter(itemDisplayName)) return true;
                }

                String itemNameUnlocalized = null;
                try {
                    itemNameUnlocalized = stack.getUnlocalizedName() + ".name";
                } catch (Throwable t) {}

                if(itemNameUnlocalized != null) {
                    if (itemNameUnlocalized.endsWith(".name.name")) {
                        itemNameUnlocalized = itemNameUnlocalized.replaceAll(".name.name", ".name");
                    }
                    String itemName = StatCollector.translateToFallback(itemNameUnlocalized).trim();
                    if (inInclusionFilter(itemName)) return true;
                    if (itemName.endsWith("Ore")) return true;
                }
            } else {
                stack = new ItemStack(b, 1, b.damageDropped(this.getCart().worldObj.getBlockMetadata(coord.getX(),coord.getY(),coord.getZ())));
                if(stack != null) {
                    int[] oreIds = OreDictionary.getOreIDs(stack);
                    for (int oreID : oreIds) {
                        String oreName = OreDictionary.getOreName(oreID);
                        if (oreName != null && oreName.startsWith("ore")) return true;
                    }

                    String itemDisplayName = null;
                    try {
                        itemDisplayName = stack.getDisplayName();
                    } catch (Throwable t) {}

                    if(itemDisplayName != null && !(itemDisplayName.startsWith("item.") || itemDisplayName.endsWith(".name"))) {
                        if(itemDisplayName.endsWith("Ore")) return true;
                        if(inInclusionFilter(itemDisplayName)) return true;
                    }

                    String itemNameUnlocalized = null;
                    try {
                        itemNameUnlocalized = stack.getUnlocalizedName() + ".name";
                    } catch (Throwable t) {}

                    if (itemNameUnlocalized != null) {
                        String itemName = StatCollector.translateToFallback(itemNameUnlocalized).trim();
                        if (inInclusionFilter(itemName)) return true;
                        if (itemName.endsWith("Ore")) return true;
                    }
                }
            }


        }
        return false;
    }
    
    private boolean inInclusionFilter(String s)
    {
    	for(String s1 : StevesCarts.instance.oreExtractorInclusions)
    	{
    		if(s1.equalsIgnoreCase(s)) return true;
    	}
    	return false;
    }
}
