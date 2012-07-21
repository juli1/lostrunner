package org.gunnm.lostrunner.model;

public class Cube {
	private float posX;
	private float posZ;
	private float posY;
	private float rotation;
	
	public Cube (int x, int z)
	{
		this (x,0,z);
	}
	
	public Cube (int x, int y, int z)
	{
		this.posX = x;
		this.posZ = z;
		this.posY = y;
		this.rotation = 0;
	}

	
	public float getRotation ()
	{
		return this.rotation;
	}
	
	public void setRotation (float r)
	{
		this.rotation = r;
	}
	
	public float getY()
	{
		return this.posY;
	}
	
	public float getZ()
	{
		return this.posZ;
	}
	
	public float getX()
	{
		return this.posX;
	}
	
	public void setX(int x)
	{
		this.posX = x;
	}
	
	public void setZ(float z)
	{
		this.posZ = z;
	}
	
	public void setY(float y)
	{
		this.posY = y;
	}
}
