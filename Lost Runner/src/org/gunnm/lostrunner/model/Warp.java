package org.gunnm.lostrunner.model;

public class Warp {
	public final static int WARP_TYPE_VERTICAL = 1;
	public final static int WARP_TYPE_HORIZONTAL = 1;
	private float posX;
	private float posY;
	private float posZ;
	private int direction;
	private Warp connection;
	
	public Warp ()
	{
		this (0,0,0,WARP_TYPE_HORIZONTAL, null);
	}
	
	public void setConnection (Warp c)
	{
		this.connection = c;
	}
	
	public void setX (float x)
	{
		this.posX = x;
	}
	
	public float getX ()
	{
		return (this.posX);
	}
	
	
	public void setZ (float z)
	{
		this.posZ = z;
	}
	
	public float getZ ()
	{
		return (this.posZ);
	}

	public void setY (float y)
	{
		this.posY = y;
	}
	
	public float getY ()
	{
		return (this.posY);
	}
	
	public int getDirection ()
	{
		return (this.direction);
	}
	
	public void setDirection (int d)
	{
		this.direction = d;
	}
	
	
	public Warp (float x, float y, float z, int d, Warp c)
	{
		this.posX 			= x;
		this.posY 			= y;
		this.posZ 			= z;
		this.direction 		= d;
		this.connection 	= c;
	}

}
