package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map6 implements MapInterface {

	private int NB_CUBES = 3;
	
	private int MAP_WIDTH = 6;
	private int MAP_DEPTH = 8;
	private int cubesPositions[][];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map6()
	{
		cubesPositions = new int[NB_CUBES][2];
		int cubid;
		cubid = 0;
		for ( int i = 0 ; i < NB_CUBES ; i++)
		{
			cubesPositions[cubid][0] = 3;
			cubesPositions[cubid][1] = -MAP_DEPTH + (i * 2) + 1;
			cubid++;
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
	public int getWarpConnection(int warpId)
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
}
