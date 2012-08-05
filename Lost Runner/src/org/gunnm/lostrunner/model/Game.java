package org.gunnm.lostrunner.model;

import java.util.Calendar;

import org.gunnm.lostrunner.maps.*;
import org.gunnm.lostrunner.sounds.Sound;

import android.content.Context;
import android.util.Log;

public class Game {

	private Cube[] cubes;
	private Warp[] warps;
	private Hero hero;
	private MapInterface currentMap;
	private static float HERO_SPEED = 2.5f;

	
	private long elapsed;
	private long lastTime = 0;
	private int currentMapIndex;
	private long completedTime;
	private final static int NB_MAPS = 1;
	private final static Class[] maps = {Map7.class, Map2.class};
	private boolean[][] hasBomb;
	private boolean[][] hasBigBomb;
	private boolean[][] destroyed;
	private boolean 	completed;
	private Sound 		sound;
	private static Game instance;
	private boolean scoreSubmitted;
	private boolean isStarted;
	
	
	public static Game getInstance (Context c)
	{
		if (instance == null)
		{
			instance = new Game(c);
		}
		return instance;
	}
	
	public long getCompletedTime ()
	{
		return this.completedTime;
	}
	
	public boolean isStarted ()
	{
		return this.isStarted;
	}
	
	public void start()
	{
		if (isStarted == false)
		{
			isStarted 				= true;
			this.elapsed 			= 0;
		}
	}
	
	private void loadMap (MapInterface map)
	{
		currentMap = map;
		cubes = new Cube[currentMap.getNbCubes()];
		warps = new Warp[currentMap.getNbWarps()];
		hero.setX(currentMap.getHeroPositionX());
		hero.setZ(currentMap.getHeroPositionZ());
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cubes[i] = new Cube (map.getCubePositionX(i), map.getCubePositionZ(i));
			cubes[i].setBounce(map.getCubeBouncing (i));
			cubes[i].setType(map.getCubeType (i));
			cubes[i].setDirection(map.getCubeDirection (i));
			cubes[i].setMap (map);
		}
		
		for (int i = 0 ; i < currentMap.getNbWarps() ; i++)
		{
			warps[i] = new Warp ();
			warps[i].setX(currentMap.getWarpPositionX(i));
			warps[i].setZ(currentMap.getWarpPositionZ(i));
			warps[i].setDirection (currentMap.getWarpDirection(i));
		}
		for (int i = 0 ; i < currentMap.getNbWarps() ; i++)
		{
			warps[i].setConnection(warps[currentMap.getWarpConnection(i)]);
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
	
	
	public Game(Context c)
	{
		sound = Sound.getInstance (c);
		
		this.reset ();
	}
	
	public void setScoreSubmitted (boolean b)
	{
		this.scoreSubmitted = b;
	}
	
	public boolean getScoreSubmitted ()
	{
		return this.scoreSubmitted;
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
		this.elapsed 			= 0;
		this.scoreSubmitted 	= false;
		this.completed 			= false;
		this.isStarted 			= false;
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
		float heroFineLeft;
		float heroFineRight;
		
		heroFineLeft 	= hero.getX() - 0.2f;
		heroFineRight 	= hero.getX() + 0.2f;
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
			sound.playSound(Sound.DEATH);
			return true;
		}
		
		//Log.i("GAME", "try to detect collision, heroX = "  + hero.getX() + "heroZ=" + hero.getZ());
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cube = cubes[i];
			if (cube.isVisible() == false)
				continue;
			
			if (((heroFineLeft > cube.getX()) &&
			     (heroFineLeft <= cube.getX()+1)) ||
			     ((heroFineRight > cube.getX()) &&
			     (heroFineRight <= cube.getX()+1))
			   )
			{
				//Log.i ("Game", "Potential Collision with cube " + i); 
				
				if ((hero.getZ() > cube.getZ() ) &&
				   (hero.getZ() < cube.getZ() + 1.5f))
				{  
					Log.i ("Game", "HeroX="+ hero.getX() + ";heroZ="+hero.getZ() + ";cubeX="+cube.getX() + ";cubeZ=" +cube.getZ());
					Log.i ("Game", "Collision with cube" + i);
					sound.playSound(Sound.DEATH);
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
				else
				{
					hasBomb[cubeCoarsePosX][cubeCoarsePosZ] = false;
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
				if (currentMapIndex >= NB_MAPS - 1)
				{
					sound.playSound(Sound.FINISHED);
				}
				else
				{
					sound.playSound(Sound.LEVEL_COMPLETED);
				}
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
	
	public void warpSwitch (Warp warp)
	{
		float newX = 0;
		float newZ = 0;
		if (warp.getDirection() == Warp.WARP_TYPE_HORIZONTAL)
		{
			if (warp.getX() == currentMap.getMapWidth())
			{
				newX = currentMap.getMapWidth() - 1;
				newZ = (-1) * warp.getZ();
			}
			if (warp.getX() == 0)
			{
				newX = currentMap.getMapWidth() + 1;
				newZ = (-1) * warp.getZ();

			}	
		}
		
		if (warp.getDirection() == Warp.WARP_TYPE_VERTICAL)
		{
			if (warp.getZ() == ( ( -1 ) * currentMap.getMapDepth()))
			{
				newX = warp.getX();
				newZ = currentMap.getMapWidth() + 1;
			}
			if (warp.getZ() == 0)
			{
				newX = warp.getX();
				newZ = -1;
			}	
		}
//		Log.i ("Game", "New position: warp Z = " + warp.getZ() + "hero coarse z="+newZ);
//		Log.i ("Game", "New position warp X = " + warp.getX() + "hero coarse z="+newX);
		hero.setX(newX);
		hero.setZ( ( - 1 ) * newZ);
	}
	
	public void detectWarp()
	{
		float heroFineLeft;
		float heroFineRight;
		float heroCoarsePosX;
		float heroCoarsePosZ;
		heroFineLeft 	= hero.getX() - 0.2f;
		heroFineRight 	= hero.getX() + 0.2f;
		heroCoarsePosX = (int)Math.floor( (double)hero.getX());
		heroCoarsePosZ = (int)Math.floor( (double) ( -1 * hero.getZ()));
		
		for (int i = 0 ; i < currentMap.getNbWarps() ; i++)
		{
			Warp warp = warps[i];
			switch (warp.getDirection())
			{
				case Warp.WARP_TYPE_HORIZONTAL:
				{
					if ( (( -1 ) * heroCoarsePosZ) != warp.getZ())
					{
						continue;
					}
//					Log.i ("Game", "hero on the same Z as the warp");
//					Log.i ("Game", "warp Z = " + warp.getZ() + "hero coarse z="+heroCoarsePosZ);
//					Log.i ("Game", "warp X = " + warp.getX() + "hero coarse z="+heroCoarsePosX);

					if (warp.getX() == currentMap.getMapWidth())
					{
						if (hero.getX() >= warp.getX())
						{
							warpSwitch (warp.getConnection ());
							return;
						}
					}
					
					if (warp.getX() == 0)
					{
						if (hero.getX() <= 0)
						{
							warpSwitch (warp.getConnection ());
							return;
						}
					}
					break;
				}
				
				case Warp.WARP_TYPE_VERTICAL:
				{
					if (heroCoarsePosX != warp.getX())
					{
						continue;
					}
//					Log.i ("Game", "hero on the same X as the warp");
//					Log.i ("Game", "warp X = " + warp.getX() + "hero coarse x="+heroCoarsePosX);
//					Log.i ("Game", "warp Z = " + warp.getZ() + "hero coarse z="+heroCoarsePosZ);
//					Log.i ("Game", "map depth="+currentMap.getMapDepth());

					if (Math.abs(warp.getZ()) == currentMap.getMapDepth())
					{
						if ((heroCoarsePosZ * (-1)) <= warp.getZ())
						{
							warpSwitch (warp.getConnection ());
							return;
						}
					}
					
					if (warp.getZ() == 0)
					{
						if (heroCoarsePosZ >= 0)
						{
							warpSwitch (warp.getConnection ());
							return;
						}
					}
					break;
					
				}
			}
		}
	}
	
	public boolean isActive ()
	{
		if (isStarted == false)
		{
			return false;
		}
		
		if (hero.getNbLifes() <= 0)
		{
			sound.stopTrack();
			return false;
		}
		if (completed)
		{
			sound.stopTrack();
			return false;
		}
		return true;
	}
	
	public boolean isCompleted ()
	{
		return completed;
	}
	
	public int getElapsedSec ()
	{
		return (int)(elapsed / 1000);
	}
	
	public long getElapsedMsec ()
	{
		return elapsed;
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
		
		currentTime  	= Calendar.getInstance().getTimeInMillis();
		period 			= currentTime - lastTime;
		elapsed 		= elapsed + period;
		
		for (int i = 0 ; i < currentMap.getNbCubes() ; i++)
		{
			cubes[i].update (period);

		}
		
		hero.updateAngles();
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
				if (hero.getX() < currentMap.getMapWidth() )
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
		
		
		detectWarp();
		
		
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
			else
			{
				completedTime = Calendar.getInstance().getTimeInMillis();
				this.completed = true;
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
	
	public Warp getWarp (int id)
	{
		return warps[id];
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
		sound.playSound(Sound.GUN);
		if (hero.getNbBullets() <= 0)
		{
			return;
		}
		hero.setNbBullets (hero.getNbBullets() - 1);
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
