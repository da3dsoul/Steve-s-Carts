package vswe.stevescarts.Blocks;

import net.minecraft.block.BlockRailBase;

public class BlockSpecialRailBase extends BlockRailBase implements IBlockBase
{
    private String unlocalizedName;

    protected BlockSpecialRailBase(boolean p_i45389_1_)
    {
        super(p_i45389_1_);
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
