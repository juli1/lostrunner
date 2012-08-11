package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map7 implements MapInterface {

	private int NB_CUBES = 10;
	private int NB_WARPS = 2;
	private int MAP_WIDTH = 6;
	private int MAP_DEPTH = 10;
	private int cubesPositions[][];
	private int warpsPositions[][];
	private int warpsTypes[];
	private int warpsConnections[];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map7()
	{
		warpsPositions = new int[NB_WARPS][2];
		warpsTypes = new int[NB_WARPS];
		warpsConnections = new int[NB_WARPS];
		cubesPositions = new int[NB_CUBES][2];
		int cubid;
		cubid = 0;
		for ( int i = 0 ; i < NB_CUBES ; i++)
		{
			if (( i % 2 ) == 0)
			{
				cubesPositions[cubid][0] = (int) Math.floor(((double)i/2));
			}
			else
			{
				cubesPositions[cubid][0] = MAP_WIDTH - (int) Math.floor(((double)i/2));
			}
			cubesPositions[cubid][1] = -MAP_DEPTH + (int) Math.floor(((double)i/2)) + 3;
			cubid++;
		}	
		
		this.warpsConnections[0] = 1;
		this.warpsConnections[1] = 0;
		
		this.warpsTypes[0] = Warp.WARP_TYPE_HORIZONTAL;
		this.warpsTypes[1] = Warp.WARP_TYPE_VERTICAL;
		
		this.warpsPositions[0][0] = MAP_WIDTH;
		this.warpsPositions[0][1] = -1;
		
		this.warpsPositions[1][0] = 1;
		this.warpsPositions[1][1] = -MAP_DEPTH;
		
		
		this.heroPositionX = 2;
		this.heroPositionZ = -1;
		

		this.exitPositionX = 4;
		this.exitPositionZ = -MAP_DEPTH;
	}	

	public int getHeroPositionX ()
	{
		return this.heroPositionX;
	}
	
	public int getHeroPositionZ ()
	{
		return this.heroPositionZ;
	}
	
	public int getExitPositionX ()
	{
		return this.exitPositionX;
	}
	
	public int getExitPositionZ ()
	{
		return this.exitPositionZ;
	}
	
	public int getNbCubes ()
	{
		return NB_CUBES;
	}
	
	public int getMapWidth ()
	{
		return MAP_WIDTH;
	}
	
	public int getMapDepth ()
	{
		return MAP_DEPTH;
	}
	
	public int getCubePositionX (int cubeId)
	{
		return cubesPositions[cubeId][0];
	}
	
	public int getCubePositionZ (int cubeId)
	{
		return cubesPositions[cubeId][1];
	}
	
	public boolean getCubeBouncing(int cubeId)
	{
		return true;
	}
	
	public int getCubeDirection(int cubeId)
	{
		
		if ( (cubeId % 2) == 0)
		{
			return Cube.DIRECTION_WEST_TO_EAST;
		}
		else
		{
			return Cube.DIRECTION_EAST_TO_WEST;
		}
	}
	
	public int getCubeType (int cubeId)
	{
		return Cube.TYPE_HORIZONTAL;
	}
	
	public int getNbWarps()
	{
		return this.NB_WARPS;
	}
	public int getWarpPositionX(int warpId)
	{
		return warpsPositions[warpId][0];
	}
	public int getWarpPositionY(int warpId)	
	{
		return 0;
	}
	
	public int getWarpPositionZ(int warpId)
	{
		return warpsPositions[warpId][1];
	}
	public int getWarpConnection(int warpId)
	{
		return warpsConnections[warpId];
	}
	
	public int getWarpDirection (int warpId)
	{
		return warpsTypes[warpId];
	}
	
	public float getCubeSpeed (int cubeId)
	{
		return 2 * Cube.DEFAULT_SPEED;
	}
	
	public float getCubeRotationSpeed (int cubeId)
	{
		return Cube.DEFAULT_ROTATION_SPEED;
	}
	
}
