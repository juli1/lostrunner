package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map4 implements MapInterface {

	private int NB_CUBES = 36;
	
	private int MAP_WIDTH = 10;
	private int MAP_DEPTH = 10;
	private int cubesPositions[][];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map4()
	{
		int cubeId = 0;
		cubesPositions = new int[NB_CUBES][2];
		for ( int i = 0 ; i < 4 ; i++)
		{
			cubesPositions[cubeId][0] = 0;
			cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
			cubeId = cubeId + 1;
			
			cubesPositions[cubeId][0] = 1;
			cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
			cubeId = cubeId + 1;
			
			cubesPositions[cubeId][0] = 2;
			cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
			cubeId = cubeId + 1;
		
			if (i != 3)
			{
				cubesPositions[cubeId][0] = 3;
				cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
				cubeId = cubeId + 1;
			}
			
			if (i != 2)
			{
				cubesPositions[cubeId][0] = 4;
				cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
				cubeId = cubeId + 1;
			}
			
			if (i != 1)
			{
				cubesPositions[cubeId][0] = 5;
				cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
				cubeId = cubeId + 1;
			}
			
			if (i != 0)
			{
				cubesPositions[cubeId][0] = 6;
				cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
				cubeId = cubeId + 1;
			}
			
			cubesPositions[cubeId][0] = 7;
			cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
			cubeId = cubeId + 1;
			
			cubesPositions[cubeId][0] = 8;
			cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
			cubeId = cubeId + 1;
			
			cubesPositions[cubeId][0] = 9;
			cubesPositions[cubeId][1] = -MAP_DEPTH + i * 2;
			cubeId = cubeId + 1;
		}		
		
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
		return Cube.DIRECTION_NORTH_TO_SOUTH;
	}
	
	public int getCubeType (int cubeId)
	{
		return Cube.TYPE_VERTICAL;
	}
	public int getNbWarps()
	{
		return 0;
	}
	public int getWarpPositionX(int warpId)
	{
		return 0;
	}
	public int getWarpPositionY(int warpId)	
	{
		return 0;
	}
	
	public int getWarpPositionZ(int warpId)
	{
		return 0;
	}
	public int getWarpDirection (int warpId)
	{
		return Warp.WARP_TYPE_HORIZONTAL;
	}
	public float getCubeSpeed (int cubeId)
	{
		return Cube.DEFAULT_SPEED;
	}
	
	public float getCubeRotationSpeed (int cubeId)
	{
		return Cube.DEFAULT_ROTATION_SPEED;
	}
	public int getWarpConnection(int warpId)
	{
		return 0;
	}
}
