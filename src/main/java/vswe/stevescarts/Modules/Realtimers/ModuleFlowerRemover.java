package vswe.stevescarts.Modules.Realtimers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IShearable;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleFlowerRemover extends ModuleBase
{
    private int tick;
    private float bladeangle;
    private float bladespeed = 0.0F;

    public ModuleFlowerRemover(MinecartModular cart)
    {
        super(cart);
    }

    public void update()
    {
        super.update();

        if (this.getCart().worldObj.isRemote)
        {
            this.bladeangle += this.getBladeSpindSpeed();

            if (this.getCart().hasFuel())
            {
                this.bladespeed = Math.min(1.0F, this.bladespeed + 0.005F);
            }
            else
            {
                this.bladespeed = Math.max(0.0F, this.bladespeed - 0.005F);
            }
        }
        else
        {
            if (this.getCart().hasFuel())
            {
                if (this.tick >= this.getInterval())
                {
                    this.tick = 0;
                    this.mownTheLawn();
                    this.shearEntities();
                }
                else
                {
                    ++this.tick;
                }
            }
        }
    }

    protected int getInterval()
    {
        return 70;
    }

    protected int getBlocksOnSide()
    {
        return 7;
    }

    protected int getBlocksFromLevel()
    {
        return 1;
    }

    private void mownTheLawn()
    {
        for (int x = -this.getBlocksOnSide(); x <= this.getBlocksOnSide(); ++x)
        {
            for (int z = -this.getBlocksOnSide(); z <= this.getBlocksOnSide(); ++z)
            {
                for (int y = -this.getBlocksFromLevel(); y <= this.getBlocksFromLevel(); ++y)
                {
                    int x1 = x + this.getCart().x();
                    int y1 = y + this.getCart().y();
                    int z1 = z + this.getCart().z();

                    if (this.isFlower(x1, y1, z1))
                    {
                        Block block = this.getCart().worldObj.getBlock(x1, y1, z1);
                        int m = this.getCart().worldObj.getBlockMetadata(x1, y1, z1);

                        if (block != null)
                        {
                            this.addStuff(block.getDrops(this.getCart().worldObj, x1, y1, z1, m, 0));
                            this.getCart().worldObj.setBlockToAir(x1, y1, z1);
                        }
                    }
                }
            }
        }
    }

    private void shearEntities()
    {
        List entities = this.getCart().worldObj.getEntitiesWithinAABB(EntityLiving.class, this.getCart().boundingBox.expand((double)this.getBlocksOnSide(), (double)((float)this.getBlocksFromLevel() + 2.0F), (double)this.getBlocksOnSide()));
        Iterator itt = entities.iterator();

        while (itt.hasNext())
        {
            EntityLiving target = (EntityLiving)itt.next();

            if (target instanceof IShearable)
            {
                IShearable shearable = (IShearable)target;
                int var10003 = (int)target.posX;
                int var10004 = (int)target.posY;

                if (shearable.isShearable((ItemStack)null, this.getCart().worldObj, var10003, var10004, (int)target.posZ))
                {
                    var10004 = (int)target.posX;
                    int var10005 = (int)target.posY;
                    int var10006 = (int)target.posZ;
                    this.addStuff(shearable.onSheared((ItemStack)null, this.getCart().worldObj, var10004, var10005, var10006, 0));
                }
            }
        }
    }

    private boolean isFlower(int x, int y, int z)
    {
        Block block = this.getCart().worldObj.getBlock(x, y, z);
        return block != null && (block instanceof BlockFlower || block instanceof BlockTallGrass || block instanceof BlockDoublePlant || block instanceof BlockDeadBush);
    }

    private void addStuff(ArrayList<ItemStack> stuff)
    {
        Iterator i$ = stuff.iterator();

        while (i$.hasNext())
        {
            ItemStack iStack = (ItemStack)i$.next();
            this.getCart().addItemToChest(iStack);

            if (iStack.stackSize != 0)
            {
                EntityItem entityitem = new EntityItem(this.getCart().worldObj, this.getCart().posX, this.getCart().posY, this.getCart().posZ, iStack);
                entityitem.motionX = 0.0D;
                entityitem.motionY = 0.15000000596046448D;
                entityitem.motionZ = 0.0D;
                this.getCart().worldObj.spawnEntityInWorld(entityitem);
            }
        }
    }

    public float getBladeAngle()
    {
        return this.bladeangle;
    }

    public float getBladeSpindSpeed()
    {
        return this.bladespeed;
    }
}
