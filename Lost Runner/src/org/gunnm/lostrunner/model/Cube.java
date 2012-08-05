package org.gunnm.lostrunner.model;

import org.gunnm.lostrunner.maps.MapInterface;

import android.util.Log;

public class Cube {
	private float posX;
	private float posZ;
	private float posY;
	private float rotation;
	private boolean visible;
	private boolean active;
	private int type;
	private int direction;
	private float speed;
	private float rotationSpeed;
	private static float DEFAULT_ROTATION_SPEED = 60;
	public final static int TYPE_HORIZONTAL 			= 1;
	public final static int TYPE_VERTICAL 			= 2;
	public final static int DIRECTION_WEST_TO_EAST 	= 4;
	public final static int DIRECTION_EAST_TO_WEST 	= 5;
	public final static int DIRECTION_NORTH_TO_SOUTH 	= 6;
	public final static int DIRECTION_SOUTH_TO_NORTH 	= 7;
	private static float DEFAULT_SPEED = 1;
	private boolean bounce;
	private MapInterface currentMap;
	
	
	public void setDirection (int d)
	{
		this.direction = d;
	}
	
	public void setBounce (boolean b)
	{
		bounce = b;
	}
	
	public void setMap (MapInterface m)
	{
		this.currentMap = m;
	}
	
	public void update (long period)
	{
		if (this.isVisible() == false)
		{
			return;
		}
		
		switch (this.type)
		{
			case TYPE_VERTICAL:
			{
				switch (this.direction)
				{
					case DIRECTION_NORTH_TO_SOUTH:
					{
						this.posZ = this.posZ + ( ((float)((float)period / 1000)) * speed);
						this.rotation = rotation + ( ((float)((float)period / 1000)) * rotationSpeed);
						
						if (posZ >= 0)
						{
							if (bounce == false)
							{
								visible = false;
							}
							else
							{
								this.direction = DIRECTION_SOUTH_TO_NORTH;
							}
						}
						break;
					}
					case DIRECTION_SOUTH_TO_NORTH:
					{
						this.posZ = this.posZ - ( ((float)((float)period / 1000)) * speed);
						this.rotation = rotation - ( ((float)((float)period / 1000)) * rotationSpeed);
						
						if (posZ <= ( (-1) * currentMap.getMapDepth()))
						{
							if (bounce == false)
							{
								visible = false;
							}
							else
							{
								this.direction = DIRECTION_NORTH_TO_SOUTH;
							}
						}
						break;
					}
					default:
					{
						Log.i ("CUBE", "invalid direction");
						break;
					}
				}
				break;
			}
			case TYPE_HORIZONTAL:
			{
				switch (this.direction)
				{
					case DIRECTION_WEST_TO_EAST:
					{
						this.posX = this.posX + ( ((float)((float)period / 1000)) * speed);
						this.rotation = rotation - ( ((float)((float)period / 1000)) * rotationSpeed);
						
						if (posX >= (currentMap.getMapWidth() - 1))
						{
							if (bounce == false)
							{
								visible = false;
							}
							else
							{
								this.direction = DIRECTION_EAST_TO_WEST;
							}
						}
						break;
					}
					case DIRECTION_EAST_TO_WEST:
					{
						this.posX = this.posX - ( ((float)((float)period / 1000)) * speed);
						this.rotation = rotation + ( ((float)((float)period / 1000)) * rotationSpeed);
						
						if (posX <= 0)
						{
							if (bounce == false)
							{
								visible = false;
							}
							else
							{
								this.direction = DIRECTION_WEST_TO_EAST;
							}
						}
						break;
					}
					default:
					{
						Log.i ("CUBE", "invalid direction");
						break;
					}
				}
				
				break;
			}
			default:
			{
				Log.i ("CUBE", "invalid direction");
				break;
			}
		}		
	}
	
	
	
	public Cube (int x, int z)
	{
		this (x,0,z);
	}
	
	public void setType (int t)
	{
		this.type = t;
	}
	
	public int getType ()
	{
		return this.type;
	}
	
	public Cube (int x, int y, int z)
	{
		this (x, y, z, TYPE_VERTICAL, DIRECTION_NORTH_TO_SOUTH, DEFAULT_SPEED, DEFAULT_ROTATION_SPEED, false);
	}
	
	public Cube (int x, int y, int z, int t, int d, float s, float rs, boolean b)
	{
		this.posX 			= x;
		this.posZ 			= z;
		this.posY 			= y;
		this.rotation 		= 0;
		this.visible 		= true;
		this.active 		= true;
		this.direction 		= d;
		this.type 			= t;
		this.speed 			= s;
		this.rotationSpeed 	= rs;
		this.bounce 		= b;
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
	
	public void setVisible (boolean b)
	{
		this.visible = b;
	}
	
	public boolean isVisible ()
	{
		return this.visible;
	}
	
	public void setActive (boolean b)
	{
		this.active = b;
	}
	
	public boolean getActive ()
	{
		return (this.active);
	}
}
