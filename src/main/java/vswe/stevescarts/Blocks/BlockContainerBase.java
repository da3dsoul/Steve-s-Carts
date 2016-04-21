package vswe.stevescarts.Blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockContainerBase extends BlockContainer implements IBlockBase
{
    private String unlocalizedName;

    protected BlockContainerBase(Material p_i45386_1_)
    {
        super(p_i45386_1_);
    }

    /**
     * Returns the unlocalized name of the block with "tile." appended to the front.
     */
    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public void setUnlocalizedName(String name)
    {
        this.unlocalizedName = name;
    }
}
