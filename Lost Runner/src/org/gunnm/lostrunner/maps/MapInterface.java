package org.gunnm.lostrunner.maps;

public interface MapInterface {
	
	public int getNbCubes ();
	public int getMapWidth ();
	public int getMapDepth ();
	public int getExitPositionX ();
	public int getExitPositionZ ();
	public int getHeroPositionX ();
	public int getHeroPositionZ ();
	public int getCubePositionX (int cubeId);
	public int getCubePositionZ (int cubeId);
	public int getCubeDirection (int cubeId);
	public int getCubeType (int cubeId);
	public boolean getCubeBouncing (int cubeId);
	public int getNbWarps();
	public int getWarpPositionX(int warpId);
	public int getWarpPositionY(int warpId);
	public int getWarpPositionZ(int warpId);
	public int getWarpConnection(int warpId);
	public int getWarpDirection (int warpId);
}
