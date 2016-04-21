package vswe.stevescarts.Items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;

public class ItemBlockDetector extends ItemBlock
{
    public ItemBlockDetector(Block b)
    {
        super(b);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(StevesCarts.tabsSC2Blocks);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack item)
    {
        return item != null ? "item.SC2:BlockDetector" + item.getItemDamage() : "item.unknown";
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int dmg)
    {
        return dmg;
    }
}
