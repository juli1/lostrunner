package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map3 implements MapInterface {

	private int NB_CUBES = 16;
	
	private int MAP_WIDTH = 10;
	private int MAP_DEPTH = 20;
	private int cubesPositions[][];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map3()
	{
		cubesPositions = new int[NB_CUBES][2];

		cubesPositions[0][0] = 0;
		cubesPositions[0][1] = -MAP_DEPTH + 1;
		
		cubesPositions[1][0] = 1;
		cubesPositions[1][1] = -MAP_DEPTH + 2;
		
		cubesPositions[2][0] = 2;
		cubesPositions[2][1] = -MAP_DEPTH + 3;
		
		cubesPositions[3][0] = 3;
		cubesPositions[3][1] = -MAP_DEPTH + 4;
		
		cubesPositions[4][0] = 6;
		cubesPositions[4][1] = -MAP_DEPTH + 4;
		
		cubesPositions[5][0] = 7;
		cubesPositions[5][1] = -MAP_DEPTH + 3;
		
		cubesPositions[6][0] = 8;
		cubesPositions[6][1] = -MAP_DEPTH + 2;
		
		cubesPositions[7][0] = 9;
		cubesPositions[7][1] = -MAP_DEPTH + 1;
		
		cubesPositions[8][0] = 0;
		cubesPositions[8][1] = -MAP_DEPTH + 8;
		
		cubesPositions[9][0] = 1;
		cubesPositions[9][1] = -MAP_DEPTH + 7;
		
		cubesPositions[10][0] = 2;
		cubesPositions[10][1] = -MAP_DEPTH + 6;
		
		cubesPositions[11][0] = 3;
		cubesPositions[11][1] = -MAP_DEPTH + 5;
		
		cubesPositions[12][0] = 6;
		cubesPositions[12][1] = -MAP_DEPTH + 5;
		
		cubesPositions[13][0] = 7;
		cubesPositions[13][1] = -MAP_DEPTH + 6;
		
		cubesPositions[14][0] = 8;
		cubesPositions[14][1] = -MAP_DEPTH + 7;
		
		cubesPositions[15][0] = 9;
		cubesPositions[15][1] = -MAP_DEPTH + 8;
		
		
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
