package org.gunnm.lostrunner.model;

public class Hero {
	private float posX = 0;
	private float posY = 0;
	private float posZ = 0;
	private int currentDirection = DIRECTION_NONE;
	public final static int DIRECTION_NONE = 0;
	public final static int DIRECTION_LEFT = 1;
	public final static int DIRECTION_RIGHT = 2;
	public final static int DIRECTION_UP = 3;
	public final static int DIRECTION_DOWN = 4;
	
	public Hero()
	{
		this (0, 0, 0);	
	}
	
	public Hero (float x, float z)
	{
		this (x, 0, z);
	}
	
	public Hero (float x, float y, float z)
	{
		posX = x;
		posY = y;
		posZ = z;
		currentDirection = DIRECTION_NONE;
	}
	
	public void setDirection (int d)
	{
		this.currentDirection = d;
	}
	
	public int getDirection ()
	{
		return this.currentDirection;
	}
	
	public float getX ()
	{
		return posX;
	}
	
	public float getY()
	{
		return posY;
	}
	
	public float getZ()
	{
		return posZ;
	}
	
	public void setX (float x)
	{
		this.posX = x;
	}
	
	public void setY (float y)
	{
		this.posY = y;
	}
	
	public void setZ (float z)
	{
		this.posZ = z;
	}
	
}