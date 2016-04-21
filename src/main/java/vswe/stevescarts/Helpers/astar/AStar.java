package vswe.stevescarts.Helpers.astar;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class AStar {
	private World worldObj;

	private SortedNodeList blockList;
    private HashSet<AStar_Node> obstacleList;
    private HashSet<AStar_Node> closedList;

	public ChunkCoordinates currentStart = null;
	public int range;

    private ModuleWoodcutter module = null;

	public AStar(World world, ModuleWoodcutter cutter) {
		worldObj = world;
		blockList = new SortedNodeList();
        obstacleList = new HashSet();
        closedList = new HashSet();

        module = cutter;
        range = module.getMaxHorizontalDistanceWithModule();
	}

	public ChunkCoordinates calcFarthestBlock(ChunkCoordinates start) {
        AStar_Node startnode = new AStar_Node(start);
        if(blockList.isEmpty() || currentStart == null || !currentStart.equals(start)) {
            currentStart = new ChunkCoordinates(start);
            blockList = new SortedNodeList();
            blockList.add(startnode);
            obstacleList = new HashSet<AStar_Node>();
            closedList = new HashSet<AStar_Node>();
        }

        if(isObstacle(blockList.getFirst().getPoint())) blockList.remove(blockList.getFirst());

        recursiveSearch(start, blockList.getFirst());

		return blockList.getFirst().getPoint();
	}

    private void recursiveSearch(ChunkCoordinates start, AStar_Node current) {
        closedList.add(current);
        for (AStar_Node neighbor : current.getNeighborList()) {
            if (closedList.contains(neighbor))
                continue;
            if (obstacleList.contains(neighbor))
                continue;
            if (distHorizontalSquared(neighbor, start) > range) {
                obstacleList.add(neighbor);
                continue;
            }

            float neighborDistanceFromStart = (current.getDistanceFromStart() + getDistanceBetween(current, neighbor));
            neighbor.setDistanceFromStart(neighborDistanceFromStart);

            if(blockList.contains(neighbor)) {
                recursiveSearch(start, neighbor);
                continue;
            }

            if (!isObstacle(neighbor.getPoint())) {

                if(!blockList.contains(neighbor)) blockList.add(neighbor);
                recursiveSearch(start,neighbor);
            } else {
                if(!obstacleList.contains(neighbor)) obstacleList.add(neighbor);
            }
        }
    }
	
	public boolean isObstacle(ChunkCoordinates c)
	{
		Block id = worldObj.getBlock(c.posX, c.posY, c.posZ);
		if(id == null) return true;
		if(module.isLeavesHandler(id, c.posX, c.posY, c.posZ)) return false;
        if(module.isWoodHandler(id, c.posX, c.posY, c.posZ)) return false;
		return true;
	}
	
	public float getDistanceBetween(AStar_Node node1, AStar_Node node2) {
		//if the nodes are on top or next to each other, return 1
		if (node1.getX() == node2.getX() || node1.getY() == node2.getY() || node1.getZ() == node2.getZ()){
			return 1;//*(mapHeight+mapWith);
		} else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
            return 1.9F;//*(mapHeight+mapWith);
        }
    }

    private double dist(AStar_Node node, ChunkCoordinates c)
	{
		return Math.sqrt((node.getX() - c.posX) * (node.getX() - c.posX) + (node.getY() - c.posY) * (node.getY() - c.posY) + (node.getX() - c.posX) * (node.getZ() - c.posZ));
	}

    private double distHorizontalSquared(AStar_Node node, ChunkCoordinates c)
    {
        return (node.getX() - c.posX) * (node.getX() - c.posX) + (node.getZ() - c.posZ) * (node.getZ() - c.posZ);
    }

	private class SortedNodeList {

		private ArrayList<AStar_Node> list = new ArrayList<AStar_Node>();

		public AStar_Node getFirst() {
            if(list == null || list.isEmpty()) return null;
            //Collections.sort(list);
            return list.get(list.size()-1);
		}

		public void clear() {
			list.clear();
		}

		public void add(AStar_Node aStar_Node) {
			if(list.contains(aStar_Node)) return;
			list.add(aStar_Node);
		}

		public void remove(AStar_Node n) {
			list.remove(n);
		}

		public int size() {
			return list.size();
		}

		public boolean contains(AStar_Node n) {
			return list.contains(n);
		}

        public boolean isEmpty() { return list.isEmpty(); }
	}

}
