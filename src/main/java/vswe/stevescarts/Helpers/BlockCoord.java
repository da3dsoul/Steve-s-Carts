package vswe.stevescarts.Helpers;

import vswe.stevescarts.Carts.MinecartModular;

public class BlockCoord implements Comparable
{
    private int x;
    private int y;
    private int z;
    private int dist;
    private MinecartModular cart;

    public BlockCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockCoord(int x, int y, int z, MinecartModular minecartModular, int dist){
        this(x, y, z);
        this.cart = minecartModular;
        this.dist = dist;
    }

    @Override
    public int compareTo(Object o) {
        if(equals(o)) return 0;
        if(!(o instanceof BlockCoord)) return -1;
        if(cart == null) return 1;
        if(((BlockCoord) o).cart == null) return -1;
        if(this.getDistToCartSquared() > ((BlockCoord) o).getDistToCartSquared()) return 1;
        return -1;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof BlockCoord))
        {
            return false;
        }
        else
        {
            BlockCoord coord = (BlockCoord)obj;
            return this.x == coord.x && this.y == coord.y && this.z == coord.z;
        }
    }

    public double getDistToCartSquared(MinecartModular cart)
    {
        int xDif = this.x - cart.x();
        int yDif = this.y - cart.y();
        int zDif = this.z - cart.z();
        return Math.pow((double)xDif, 2.0D) + Math.pow((double)yDif, 2.0D) + Math.pow((double)zDif, 2.0D);
    }

    public int getDist() {
        return dist;
    }

    public double getDistToCartSquared()
    {
        int xDif = this.x - cart.x();
        int yDif = this.y - cart.y();
        int zDif = this.z - cart.z();
        return Math.pow((double)xDif, 2.0D) + Math.pow((double)yDif, 2.0D) + Math.pow((double)zDif, 2.0D);
    }

    public double getHorizontalDistToCartSquared(MinecartModular cart)
    {
        int xDif = this.x - cart.x();
        int zDif = this.z - cart.z();
        return Math.pow((double)xDif, 2.0D) + Math.pow((double)zDif, 2.0D);
    }
}
