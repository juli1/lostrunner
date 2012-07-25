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
	private boolean[][] hasBomb;
	private boolean[][] hasBigBomb;
	private boolean[][] destroyed;
	private boolean completed;
	
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
		
		hasBomb    = new boolean[map.getMapWidth()][map.getMapDepth()];
		hasBigBomb = new boolean[map.getMapWidth()][map.getMapDepth()];
		destroyed  = new boolean[map.getMapWidth()][map.getMapDepth()];
		
		for (int i = 0 ; i < map.getMapWidth() ; i++)
		{
			for (int j = 0 ; j < map.getMapDepth() ; j++)
			{
				hasBomb[i][j] 		= false;
				hasBigBomb[i][j] 	= false;
				destroyed[i][j] 	= false;
			}	
		}
		
		lastTime = Calendar.getInstance().getTimeInMillis();
	}
	
	
	public Game()
	{
		this.reset ();
	}
	
	public void reset ()
	{

		MapInterface firstMap = null;
		try {
			firstMap = (MapInterface) maps[0].newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		this.completed = false;
		hero = new Hero ();
		hero.setNbBullets(1);
		hero.setNbBombs(1);
		hero.setNbBigBombs(1);
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
		int cubeCoarsePosX;
		int cubeCoarsePosZ;
		int heroCoarsePosX;
		int heroCoarsePosZ;
		
		heroCoarsePosX = (int)Math.floor( (double)hero.getX());
		heroCoarsePosZ = (int)Math.floor( (double) ( -1 * hero.getZ()));
		
		if (heroCoarsePosX >= currentMap.getMapWidth())
		{
			heroCoarsePosX = currentMap.getMapWidth() - 1;
		}
		if (heroCoarsePosX < 0)
		{
			heroCoarsePosX = 0;
		}
		
		if (heroCoarsePosZ >= currentMap.getMapDepth())
		{
			heroCoarsePosZ = currentMap.getMapDepth() - 1;
		}

		if (heroCoarsePosZ < 0)
		{
			heroCoarsePosZ = 0;
		}
		
		//Log.i ("Game", "HeroX="+ hero.getX() + ";heroZ="+hero.getZ() + ";heroCoarseX="+ heroCoarsePosX + ";heroCoarseZ=" + heroCoarsePosZ);
		if (destroyed[heroCoarsePosX][heroCoarsePosZ])
		{
			return true;
		}
		
		//Log.i("GAME", "try to detect collision, heroX = "  + hero.getX() + "heroZ=" + hero.getZ());
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cube = cubes[i];
			if (cube.isVisible() == false)
				continue;
			
			if ((hero.getX() > cube.getX()) &&
			    (hero.getX() <= cube.getX()+1))
			{
				//Log.i ("Game", "Potential Collision with cube " + i); 
				
				if ((hero.getZ() > cube.getZ() ) &&
				   (hero.getZ() < cube.getZ() + 1.5f))
				{  
					//Log.i ("Game", "HeroX="+ hero.getX() + ";heroZ="+hero.getZ() + ";cubeX="+cube.getX() + ";cubeZ=" +cube.getZ());
					//Log.i ("Game", "Collision");
					return true;
				}
			}
			cubeCoarsePosX = (int)Math.floor( (double)cube.getX());
			cubeCoarsePosZ = (int)Math.floor( (double) ( -1 * cube.getZ()));
			//Log.i ("Game", "CubeCoarseX="+ cubeCoarsePosX + ";cubeCoarseZ="+cubeCoarsePosZ + ";cubeX="+cube.getX() + ";cubeZ=" +cube.getZ());
			if ((hasBomb(cubeCoarsePosX, cubeCoarsePosZ)) || (hasBigBomb(cubeCoarsePosX, cubeCoarsePosZ)))
			{
				cube.setActive(false);
				cube.setVisible(false);
				if (hasBigBomb(cubeCoarsePosX, cubeCoarsePosZ))
				{
					destroyed[cubeCoarsePosX][cubeCoarsePosZ] = true;
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
	
	public boolean isActive ()
	{
		if (hero.getNbLifes() <= 0)
		{
			return false;
		}
		if (completed)
		{
			return false;
		}
		return true;
	}
	
	public boolean isCompleted ()
	{
		return completed;
	}
	
	public void update ()
	{
		long currentTime;
		long period;
		boolean endLevel;
		boolean collision;
		
		
		
		endLevel = false;
		collision = false;
		
		if (this.isActive() == false)
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
					//hero.setDirection(Hero.DIRECTION_NONE); 
					
				}
				break;
			}
			case Hero.DIRECTION_RIGHT:
			{
				if (hero.getX() < currentMap.getMapWidth() - 1 )
				{
					hero.setX(hero.getX() +   ( ((float)((float)period / 1000)) * HERO_SPEED) );
					//hero.setDirection(Hero.DIRECTION_NONE); 
					
				}
				break;
			}
			case Hero.DIRECTION_UP:
			{
				if (hero.getZ() > - currentMap.getMapDepth() )
					{
					hero.setZ(hero.getZ() -  ( ((float)((float)period / 1000)) * HERO_SPEED) );
					//hero.setDirection(Hero.DIRECTION_NONE); 
					
				}
				break;
			}
			case Hero.DIRECTION_DOWN:
			{
				if (hero.getZ() < 0)
				{
					hero.setZ(hero.getZ() +  ( ((float)((float)period / 1000)) * HERO_SPEED) );
					//hero.setDirection(Hero.DIRECTION_NONE); 
					
				
				}
				break;
			}
		}
		
		
		endLevel = detectEndOfLevel ();
		
		if (endLevel)
		{
			if (currentMapIndex < NB_MAPS - 1)
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
	
	public void enableBomb ()
	{
		int x;
		int z;
		
		if (hero.getNbBombs() <= 0)
		{
			return;
		}
		
		x = (int)Math.floor((double)hero.getX());
		z = (int)Math.floor((double) ( -1 * hero.getZ()));
		Log.i("GAME", "try to put bomb at x=" + x + ";z="+z);
		try 
		{
			if ( ! hasBigBomb[x][z])
			{
				hero.setNbBombs(hero.getNbBombs() - 1);
				hasBomb[x][z] = true;
			}
		}
		catch (Exception e)
		{
			
		}
	}
	
	public void enableBigBomb ()
	{
		int x;
		int z;
		
		if (hero.getNbBigBombs() <= 0)
		{
			return;
		}
		
		x = (int)Math.floor((double)hero.getX());
		z = (int)Math.floor((double) ( -1 * hero.getZ()));
		Log.i("GAME", "try to put big bomb at x=" + x + ";z="+z);
		try {
			if ( ! hasBomb[x][z])
			{
				hero.setNbBigBombs(hero.getNbBigBombs() - 1);
				hasBigBomb[x][z] = true;
			}
		}
		catch (Exception e)
		{
			
		}
	}
	
	public void enableShoot ()
	{
		Cube cube;
		Cube toDestroy;
		
		toDestroy = null;
		
		if (hero.getNbBullets() <= 0)
		{
			return;
		}
		
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cube = cubes[i];
			if (cube.isVisible() == false)
				continue;
			

			
			if ((hero.getX() > cube.getX()) &&
			    (hero.getX() <= cube.getX()+1) &&
			    (cube.getZ() < hero.getZ())) 
			    
			{
				//Log.i("Game", "Cube at posX" + cube.getX() + ";posZ=" + cube.getZ());
				
				if (toDestroy == null)
				{
					toDestroy = cubes[i];
				} 
				else
				{
					if (toDestroy.getZ() < cubes[i].getZ())
					{
						toDestroy = cubes[i];
					}
				} 
			}		
		}
		
		if (toDestroy != null)
		{
			//Log.i("Game", "Destroy cube at posX" + toDestroy.getX() + ";posZ=" + toDestroy.getZ());
			toDestroy.setVisible(false);
			toDestroy.setActive(false);
			hero.setNbBullets (hero.getNbBullets() - 1);

		}
	}
	
	public boolean hasBomb (int x, int z)
	{
		try
		{
			return hasBomb[x][z];
		}
		catch (IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public boolean hasBigBomb (int x, int z)
	{
		try
		{
			return hasBigBomb[x][z];
		}
		catch (IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public boolean isDestroyed (int x, int z)
	{
		try
		{
			return destroyed[x][z];
		}
		catch (IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
}
