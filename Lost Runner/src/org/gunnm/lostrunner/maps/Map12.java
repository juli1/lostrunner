package org.gunnm.lostrunner.maps;

import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Warp;

public class Map12 implements MapInterface {

	private int NB_CUBES = 20;
	private int NB_WARPS = 4;
	private int MAP_WIDTH = 8;
	private int MAP_DEPTH = 7;
	private int cubesPositions[][];
	private int warpsPositions[][];
	private int warpsTypes[];
	private int warpsConnections[];
	private int heroPositionX;
	private int heroPositionZ;
	private int exitPositionX;
	private int exitPositionZ;
	
	public Map12()
	{
		warpsPositions = new int[NB_WARPS][2];
		warpsTypes = new int[NB_WARPS];
		warpsConnections = new int[NB_WARPS];
		cubesPositions = new int[NB_CUBES][2];
		
		for ( int i = 0 ; i < 6 ; i++)
		{
			cubesPositions[i * 2][0] = 2;
			cubesPositions[i * 2][1] = -i;
			cubesPositions[i * 2 + 1][0] = 5;
			cubesPositions[i * 2 + 1][1] = -MAP_DEPTH + i;	
		}
		
		cubesPositions[12][0] = 4;
		cubesPositions[12][1] = -MAP_DEPTH / 2 - 1;
		
		cubesPositions[13][0] = 4;
		cubesPositions[13][1] = -MAP_DEPTH / 2 + 1;
		
		cubesPositions[14][0] = MAP_WIDTH - 1;
		cubesPositions[14][1] = -1;
		
		
		cubesPositions[15][0] = 1;
		cubesPositions[15][1] = -MAP_DEPTH / 2 - 1;
		
		cubesPositions[16][0] = 1;
		cubesPositions[16][1] = -MAP_DEPTH / 2 + 1;
		
		cubesPositions[17][0] = 4;
		cubesPositions[17][1] = -1;
		
		
		cubesPositions[18][0] = MAP_WIDTH - 1;
		cubesPositions[18][1] = -MAP_DEPTH / 2 - 1;
		
		cubesPositions[19][0] = MAP_WIDTH - 1;
		cubesPositions[19][1] = -MAP_DEPTH / 2 + 1;
		
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
		
		
		this.heroPositionX = 1;
		this.heroPositionZ = -1;
		

		this.exitPositionX = MAP_WIDTH-1;
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
		
		return Cube.DIRECTION_WEST_TO_EAST;

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
		if (cubeId == 13)
		{
			return Cube.DEFAULT_SPEED / 4;
		}
		if (cubeId == 14)
		{
			return Cube.DEFAULT_SPEED /6;
		}
		
		if (cubeId == 15)
		{
			return Cube.DEFAULT_SPEED / 2;
		}
		if (cubeId == 16)
		{
			return Cube.DEFAULT_SPEED / 4;
		}
		if (cubeId == 17)
		{
			return Cube.DEFAULT_SPEED /6;
		}
		if (cubeId == 18)
		{
			return Cube.DEFAULT_SPEED / 4;
		}
		if (cubeId == 19)
		{
			return Cube.DEFAULT_SPEED / 4;
		}
		
		return 0;
	}
	
	public float getCubeRotationSpeed (int cubeId)
	{
		if (cubeId == 12)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 2;
		}
		if (cubeId == 13)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 2;
		}
		if (cubeId == 14)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 3;
		}
		
		if (cubeId == 15)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 2;
		}
		if (cubeId == 16)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 2;
		}
		if (cubeId == 17)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 3;
		}
		if (cubeId == 18)
		{
			return Cube.DEFAULT_ROTATION_SPEED / 2;
		}
		if (cubeId == 19)
		{
			return Cube.DEFAULT_ROTATION_SPEED;
		}
		return 0;
	}
	
}
