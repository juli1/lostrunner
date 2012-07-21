package org.gunnm.lostrunner.model;

public class Cube {
	private int posX;
	private int posZ;
	private int posY;
	
	public Cube (int x, int z)
	{
		this (x,0,z);
	}
	
	public Cube (int x, int y, int z)
	{
		this.posX = x;
		this.posZ = z;
		this.posY = y;
	}

	public int getY()
	{
		return this.posY;
	}
	
	public int getZ()
	{
		return this.posZ;
	}
	
	public int getX()
	{
		return this.posX;
	}
	
	public void setX(int x)
	{
		this.posX = x;
	}
	
	public void setZ(int z)
	{
		this.posZ = z;
	}
	
	public void setY(int y)
	{
		this.posY = y;
	}
}
