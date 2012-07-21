package org.gunnm.lostrunner.model;

import java.util.Calendar;

import org.gunnm.lostrunner.maps.MapInterface;

public class Game {

	private Cube[] cubes;
	private Hero hero;
	private MapInterface currentMap;
	private static float CUBE_SPEED = 1;
	private static float HERO_SPEED = 2;
	private static float CUBE_ROTATION_SPEED = 60;
	private long lastTime = 0;
	
	public Game(MapInterface map)
	{
		currentMap = map;
		cubes = new Cube[currentMap.getNbCubes()];
		
		hero = new Hero (currentMap.getHeroPositionX(), currentMap.getHeroPositionZ());
		
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cubes[i] = new Cube (map.getCubePositionX(i), map.getCubePositionZ(i));
		}
		lastTime = Calendar.getInstance().getTimeInMillis();
	}
	
	public Hero getHero ()
	{
		return this.hero;
	}
	
	public void update ()
	{
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long period = currentTime - lastTime;
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cubes[i].setZ(cubes[i].getZ() + ( ((float)((float)period / 1000)) * CUBE_SPEED) );
			cubes[i].setRotation(cubes[i].getRotation() + ( ((float)((float)period / 1000)) * CUBE_ROTATION_SPEED) );
		}
		lastTime = currentTime;
		switch (hero.getDirection())
		{
			case Hero.DIRECTION_LEFT:
				hero.setX(hero.getX() - period / ( ((float)((float)period / 1000)) * HERO_SPEED) );
				break;
		}
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
