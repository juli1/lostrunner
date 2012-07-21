package org.gunnm.lostrunner.model;

import org.gunnm.lostrunner.maps.MapInterface;

public class Game {

	private Cube[] cubes;
	private MapInterface currentMap;
	public Game(MapInterface map)
	{
		currentMap = map;
		cubes = new Cube[currentMap.getNbCubes()];
		
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cubes[i] = new Cube (map.getCubePositionX(i), map.getCubePositionZ(i));
		}
	}
	
	public void update (float delay)
	{
		
	}
	
	public Cube getCube (int id)
	{
		return cubes[id];
	}
	
	public MapInterface getCurrentMap ()
	{
		return this.currentMap;
	}
}
