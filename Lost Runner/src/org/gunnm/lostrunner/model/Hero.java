package org.gunnm.lostrunner.model;

import android.util.Log;

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
	public final static int ANGLE_INCREASE = 1;
	public final static int ANGLE_DECREASE = 2;
	private int nbLifes;
	private int nbBombs;
	private int nbBigBombs;
	private int nbBullets;
	private int angleArms;
	private int angleLegs;
	private int angleArmsStatus;
	private int angleLegsStatus;
	
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
		posX 				= x;
		posY 				= y;
		posZ 				= z;
		currentDirection 	= DIRECTION_NONE;
		nbLifes 			= 3;
		nbBullets			= 0;
		nbBigBombs			= 0;
		nbBombs				= 0;
		angleArms 			= 25;
		angleLegs			= 0;
		angleArmsStatus		= ANGLE_INCREASE;
		angleLegsStatus		= ANGLE_INCREASE;
	}
	
	
	public int getNbBombs ()
	{
		return this.nbBombs;
	}
	
	public int getNbBigBombs ()
	{
		return this.nbBigBombs;
	}
	
	public int getNbBullets ()
	{
		return this.nbBullets;
	}
	
	public void setNbBombs  (int n)
	{
		this.nbBombs = n;
	}
	
	public void setNbBigBombs (int n)
	{
		this.nbBigBombs = n;
	}
	
	public void setNbBullets (int n)
	{
		this.nbBullets = n;
	}
	
	public int getNbLifes ()
	{
		return this.nbLifes;
	}
	
	public void setNbLifes (int n)
	{
		this.nbLifes = n;
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
//		Log.i ("HERO", "Set X to: " + x);
		this.posX = x;
	}
	
	public void setY (float y)
	{
		this.posY = y;
	}
	
	public void setZ (float z)
	{
//		Log.i ("HERO", "Set Z to: " + z);
		this.posZ = z;
	}
	
	public int getLegsAngles ()
	{
		return this.angleLegs;
	}
	
	public int getArmsAngles ()
	{
		return this.angleArms;
	}
	
	public void updateAngles ()
	{
		if (this.currentDirection == DIRECTION_NONE)
		{
			this.angleArms = 25;
			this.angleLegs = 0;
			return;
		}
		
		if (this.angleArmsStatus == ANGLE_INCREASE)
		{
			this.angleArms = this.angleArms + 15;
			if (this.angleArms > 35)
			{
				this.angleArmsStatus = ANGLE_DECREASE;
				this.angleArms = 35;
			}
		}
		
		if (this.angleArmsStatus == ANGLE_DECREASE)
		{
			this.angleArms = this.angleArms - 15;
			if (this.angleArms < -35)
			{
				this.angleArmsStatus = ANGLE_INCREASE;
				this.angleArms = -35;
			}
		}
		
		
		if (this.angleLegsStatus == ANGLE_INCREASE)
		{
			this.angleLegs = this.angleLegs + 20;
			if (this.angleLegs > 45)
			{
				this.angleLegsStatus = ANGLE_DECREASE;
				this.angleLegs = 45;
			}
		}
		
		if (this.angleLegsStatus == ANGLE_DECREASE)
		{
			this.angleLegs = this.angleLegs - 20;
			if (this.angleLegs < -45)
			{
				this.angleLegsStatus = ANGLE_INCREASE;
				this.angleLegs = -45;
			}
		}
		
	}
}
