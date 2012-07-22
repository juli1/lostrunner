package org.gunnm.lostrunner.model;

import java.util.Calendar;

import org.gunnm.lostrunner.maps.Map1;
import org.gunnm.lostrunner.maps.Map2;
import org.gunnm.lostrunner.maps.MapInterface;

import android.util.Log;

public class Game {

	private Cube[] cubes;
	private Hero hero;
	private MapInterface currentMap;
	private static float CUBE_SPEED = 1;
	private static float HERO_SPEED = 2.5f;
	private static float CUBE_ROTATION_SPEED = 60;
	private long lastTime = 0;
	private int currentMapIndex;
	private final static int NB_MAPS = 2;
	private final static Class[] maps = {Map1.class, Map2.class};
	
	
	private void loadMap (MapInterface map)
	{
		currentMap = map;
		cubes = new Cube[currentMap.getNbCubes()];
		hero.setX(currentMap.getHeroPositionX());
		hero.setZ(currentMap.getHeroPositionZ());
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cubes[i] = new Cube (map.getCubePositionX(i), map.getCubePositionZ(i));
		}
		lastTime = Calendar.getInstance().getTimeInMillis();
	}
	
	
	public Game()
	{

		MapInterface firstMap = null;
		try {
			firstMap = (MapInterface) maps[0].newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		hero = new Hero ();
		loadMap (firstMap);
		currentMapIndex = 0;	
	}
	
	public Hero getHero ()
	{
		return this.hero;
	}
	
	public boolean detectCollision ()
	{
		Cube cube;
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cube = cubes[i];
			if ((hero.getX() > cube.getX()) &&
			    (hero.getX() < cube.getX()+1))
			{
				//Log.i ("Game", "Potential Collision with cube " + i); 
				
				if ((hero.getZ() > cube.getZ() ) &&
				   (hero.getZ() < cube.getZ() + 1.5f))
				{  
					Log.i ("Game", "HeroX="+ hero.getX() + ";heroZ="+hero.getZ() + ";cubeX="+cube.getX() + ";cubeZ=" +cube.getZ());
					Log.i ("Game", "Collision");
					return true;
				}
			}
		}
		return false;
	}
	
	 
	public boolean detectEndOfLevel ()
	{
		//Log.i ("Game" , "HeroX="+hero.getX()+";heroZ="+hero.getZ()+";exitX"+currentMap.getExitPositionX()+";exitZ="+currentMap.getExitPositionZ());
		if (currentMap.getExitPositionZ() == -currentMap.getMapDepth())
		{
			if ( (hero.getX() > currentMap.getExitPositionX()) &&
				 (hero.getX() < currentMap.getExitPositionX() + 1) &&
				 (hero.getZ() <= currentMap.getExitPositionZ()))
			{
//				Log.i ("Game" , "End of gane");
				return true;
			}
		}
		else
		{
			if ( (hero.getZ() > currentMap.getExitPositionZ() - 1) &&
					 (hero.getX() < currentMap.getExitPositionZ()) &&
					 (hero.getX() <= 0))
				{
//				Log.i ("Game" , "End of gane");
				}
			return true;
		}
			
		return false;
	}
	
	
	public void update ()
	{
		long currentTime;
		long period;
		boolean endLevel;
		boolean collision;
		
		endLevel = false;
		collision = false;
		
		if (hero.getNbLifes() <= 0)
		{
			return;
		}
		
		currentTime  = Calendar.getInstance().getTimeInMillis();
		period = currentTime - lastTime;
		
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			if (cubes[i].isVisible())
			{
				cubes[i].setZ(cubes[i].getZ() + ( ((float)((float)period / 1000)) * CUBE_SPEED) );
				cubes[i].setRotation(cubes[i].getRotation() + ( ((float)((float)period / 1000)) * CUBE_ROTATION_SPEED) );
				
				if (cubes[i].getZ() >= 0)
				{
					cubes[i].setVisible(false);
				}
			}
		}
		
		
		switch (hero.getDirection())
		{
			case Hero.DIRECTION_LEFT:
			{
				if (hero.getX() >= 0)
				{
					hero.setX(hero.getX() - ( ((float)((float)period / 1000)) * HERO_SPEED) );
					hero.setDirection(Hero.DIRECTION_NONE); 
					
				}
				break;
			}
			case Hero.DIRECTION_RIGHT:
			{
				if (hero.getX() < currentMap.getMapWidth() - 1 )
				{
					hero.setX(hero.getX() +   ( ((float)((float)period / 1000)) * HERO_SPEED) );
					hero.setDirection(Hero.DIRECTION_NONE); 
					
				}
				break;
			}
			case Hero.DIRECTION_UP:
			{
				if (hero.getZ() > - currentMap.getMapDepth() )
					{
					hero.setZ(hero.getZ() -  ( ((float)((float)period / 1000)) * HERO_SPEED) );
					hero.setDirection(Hero.DIRECTION_NONE); 
					
				}
				break;
			}
			case Hero.DIRECTION_DOWN:
			{
				if (hero.getZ() < 0)
				{
					hero.setZ(hero.getZ() +  ( ((float)((float)period / 1000)) * HERO_SPEED) );
					hero.setDirection(Hero.DIRECTION_NONE); 
					
				
				}
				break;
			}
		}
		
		
		endLevel = detectEndOfLevel ();
		
		if (endLevel)
		{
			if (currentMapIndex < NB_MAPS)
			{
				currentMapIndex = currentMapIndex + 1;
				try {
					loadMap((MapInterface)maps[currentMapIndex].newInstance());
				} catch (IllegalAccessException e) 
				{
					e.printStackTrace();
				}
				catch (InstantiationException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		collision = detectCollision ();
		if (collision)
		{
			hero.setNbLifes(hero.getNbLifes() - 1); 
			loadMap (currentMap);
		}
	
		lastTime = currentTime;
		
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
