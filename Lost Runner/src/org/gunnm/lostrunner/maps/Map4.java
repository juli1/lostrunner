package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map4 implements MapInterface {

	private int NB_CUBES = 64;
	
	private int MAP_WIDTH = 10;
	private int MAP_DEPTH = 14;
	private int cubesPositions[][];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map4()
	{
		cubesPositions = new int[NB_CUBES][2];
		for ( int i = 0 ; i < 8 ; i++)
		{
			cubesPositions[(i * 8)][0] = 0;
			cubesPositions[(i * 8)][1] = -MAP_DEPTH + i;
		
			cubesPositions[1 + (i * 8)][0] = 1;
			cubesPositions[1 + (i * 8)][1] = -MAP_DEPTH + i;
		
			cubesPositions[2 + (i * 8)][0] = 2;
			cubesPositions[2 + (i * 8)][1] = -MAP_DEPTH + i;
		
			cubesPositions[3 + (i * 8)][0] = 3;
			cubesPositions[3 + (i * 8)][1] = -MAP_DEPTH + i;
			
			cubesPositions[4 + (i * 8)][0] = 6;
			cubesPositions[4 + (i * 8)][1] = -MAP_DEPTH + i;
			
			cubesPositions[5 + (i * 8)][0] = 7;
			cubesPositions[5 + (i * 8)][1] = -MAP_DEPTH + i;
			
			cubesPositions[6 + (i * 8)][0] = 8;
			cubesPositions[6 + (i * 8)][1] = -MAP_DEPTH + i;
			
			cubesPositions[7 + (i * 8)][0] = 9;
			cubesPositions[7 + (i * 8)][1] = -MAP_DEPTH + i;
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
		return false;
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
	public int getWarpConnection(int warpId)
	{
		return 0;
	}
}
