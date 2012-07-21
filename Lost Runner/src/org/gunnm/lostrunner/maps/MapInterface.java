package org.gunnm.lostrunner.maps;

public interface MapInterface {
	
	public int getNbCubes ();
	public int getMapWidth ();
	public int getMapDepth ();
	public int getCubePositionX (int cubeId);
	public int getCubePositionZ (int cubeId);
}
