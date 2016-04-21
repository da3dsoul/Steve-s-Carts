package vswe.stevescarts.Helpers.astar;

import net.minecraft.util.ChunkCoordinates;

import java.util.ArrayList;

public class AStar_Node implements Comparable<AStar_Node> {
    /* Nodes that this is connected to */
    float distanceFromStart;
    private int x;
    private int y;
    private int z;

    private static final int[] xOffset = { 0, -1, 0, 1, 0, -1,-1, 1, 1, -1, 0, 1, 0, -1,-1, 1, 1,  0, -1, 0, 1, 0, -1,-1, 1, 1 };
    private static final int[] yOffset = {-1, -1,-1,-1,-1, -1,-1,-1,-1,  0, 0, 0, 0,  0, 0, 0, 0,  1,  1, 1, 1, 1,  1, 1, 1, 1 };
    private static final int[] zOffset = { 0,  0, 1, 0,-1, -1, 1, 1,-1,  0, 1, 0,-1, -1, 1, 1,-1,  0,  0, 1, 0,-1, -1, 1, 1,-1 };

    AStar_Node(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.distanceFromStart = 0;
    }

    AStar_Node(ChunkCoordinates c) {
        this.x = c.posX;
        this.y = c.posY;
        this.z = c.posZ;
        this.distanceFromStart = 0;
    }

    AStar_Node (int x, int y, int z, int distanceFromStart) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.distanceFromStart = distanceFromStart;
    }

    public ArrayList<AStar_Node> getNeighborList() {
        ArrayList<AStar_Node> neighborList = new ArrayList<AStar_Node>();
        for(int i = 0; i < 25; i++) {
            neighborList.add(new AStar_Node(x + xOffset[i], y + yOffset[i], z + zOffset[i]));
        }
        return neighborList;
    }

    public float getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(float f) {
        this.distanceFromStart = f;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public ChunkCoordinates getPoint() {
        return new ChunkCoordinates(x,y,z);
    }

    public boolean equals(Object obj) {
        if(obj instanceof AStar_Node)
        {
            AStar_Node aStar_Node = (AStar_Node)obj;
            if(aStar_Node.x != this.x) return false;
            if(aStar_Node.y != this.y) return false;
            if(aStar_Node.z != this.z) return false;
            return true;
        }else if(obj instanceof ChunkCoordinates)
        {
            ChunkCoordinates c = (ChunkCoordinates)obj;
            if(c.posX != this.x) return false;
            if(c.posY != this.y) return false;
            if(c.posZ != this.z) return false;
            return true;
        }else
        {
            return false;
        }
    }

    public int compareTo(AStar_Node otherNode) {

        if (this.distanceFromStart > otherNode.getDistanceFromStart()) {
            return -1;
        } else if (this.distanceFromStart < otherNode.getDistanceFromStart()) {
            return 1;
        } else {
            if(this.y > otherNode.getY()) return -1;
            if(this.y < otherNode.getY()) return 1;
            if(this.getX() > otherNode.getX()) return -1;
            if(this.getX() < otherNode.getX()) return 1;
            if(this.getZ() > otherNode.getZ()) return -1;
            if(this.getZ() < otherNode.getZ()) return 1;
            return 0;
        }
    }



    @Override
    public int hashCode() {
        return this.x + this.z << 8 + this.y << 16;
    }
}