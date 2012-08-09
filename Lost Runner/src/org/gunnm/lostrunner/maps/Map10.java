package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map10 implements MapInterface {

	private int NB_CUBES = 13;
	private int NB_WARPS = 4;
	private int MAP_WIDTH = 6;
	private int MAP_DEPTH = 8;
	private int cubesPositions[][];
	private int warpsPositions[][];
	private int warpsTypes[];
	private int warpsConnections[];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map10()
	{
		warpsPositions = new int[NB_WARPS][2];
		warpsTypes = new int[NB_WARPS];
		warpsConnections = new int[NB_WARPS];
		cubesPositions = new int[NB_CUBES][2];
		
		for ( int i = 0 ; i < 6 ; i++)
		{
			cubesPositions[i * 2][0] = MAP_WIDTH / 3 - 1;
			cubesPositions[i * 2][1] = -MAP_DEPTH + i;
			cubesPositions[i * 2 + 1][0] = 2 * (MAP_WIDTH / 3);
			cubesPositions[i * 2 + 1][1] = -i;	
		}
		
		cubesPositions[12][0] = 1;
		cubesPositions[12][1] = -MAP_DEPTH / 2;
		
		this.warpsConnections[0] = 1;
		this.warpsConnections[1] = 0;
		

		this.warpsConnections[2] = 3;
		this.warpsConnections[3] = 2;
		
		this.warpsTypes[0] = Warp.WARP_TYPE_HORIZONTAL;
		this.warpsTypes[1] = Warp.WARP_TYPE_VERTICAL;

		this.warpsTypes[2] = Warp.WARP_TYPE_HORIZONTAL;
		this.warpsTypes[3] = Warp.WARP_TYPE_HORIZONTAL;
		
		
		this.warpsPositions[0][0] = MAP_WIDTH;
		this.warpsPositions[0][1] = -MAP_DEPTH / 2;
		
		this.warpsPositions[1][0] = 3;
		this.warpsPositions[1][1] = -MAP_DEPTH;
		

		this.warpsPositions[2][0] = 0;
		this.warpsPositions[2][1] = -MAP_DEPTH / 2;
		

		this.warpsPositions[3][0] = MAP_WIDTH;
		this.warpsPositions[3][1] = -1;
		
		
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
		if (cubeId == 12)
		{
			return Cube.DEFAULT_SPEED / 2;
		}
		return 0;
	}
	
	public float getCubeRotationSpeed (int cubeId)
	{
		if (cubeId == 12)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 2;
		}
		return 0;
	}
	
}
