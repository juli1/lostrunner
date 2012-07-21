package org.gunnm.lostrunner.maps;

public class Map1 implements MapInterface {

	private int NB_CUBES = 5;
	
	private int MAP_WIDTH = 5;
	private int MAP_DEPTH = 10;
	private int cubesPositions[][];
	
	
	public Map1()
	{
		cubesPositions = new int[NB_CUBES][2];
		for (int i = 0 ; i < NB_CUBES ; i++)
		{
			cubesPositions[i][0] = i;
			cubesPositions[i][1] = -i;
		}
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
}
