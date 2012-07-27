package org.gunnm.lostrunner.controller;



import org.gunnm.lostrunner.graphics.LostRenderer;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.model.Hero;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class Touch implements OnTouchListener {

	private int screenWidth;
	private int screenHeight;
	private static final String TAG = "Touch";
	private Context context;
	private final static int ORIENTATION_PORTRAIT  = 1;
	private final static int ORIENTATION_LANDSCAPE = 2;
	private int orientation;
	private LostRenderer renderer;
	private Game currentGame;
	
	// The table is organized with left, right, top, down
	private int[] zoneMoveLeft;
	private int[] zoneMoveRight;
	private int[] zoneMoveUp;
	private int[] zoneMoveDown;
	private int[] zoneBullet;
	private int[] zoneBomb;
	private int[] zoneBigBomb;
	private int[] zoneZoomIn;
	private int[] zoneZoomOut;
	private int[] zoneCamLeft;
	private int[] zoneCamRight;
	
	public Touch (Context c, LostRenderer r, Game g)
	{
		WindowManager wm;
		Display display;
		
		context = c;
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		orientation = ORIENTATION_PORTRAIT;
		
		this.renderer = r;
		this.currentGame = g;
		
		zoneMoveLeft 	= new int[]{0,0,0,0};
		zoneMoveRight 	= new int[]{0,0,0,0};
		zoneMoveUp 		= new int[]{0,0,0,0};
		zoneMoveDown 	= new int[]{0,0,0,0};
		zoneBullet 		= new int[]{0,0,0,0};
		zoneBomb 		= new int[]{0,0,0,0};
		zoneBigBomb 	= new int[]{0,0,0,0};
		zoneZoomIn 		= new int[]{0,0,0,0};
		zoneZoomOut 	= new int[]{0,0,0,0};
		zoneCamLeft 	= new int[]{0,0,0,0};
		zoneCamRight 	= new int[]{0,0,0,0};
		updateOrientation();
	}

	public void setScreenWidth (int v)
	{
		screenWidth = v;
	}
	
	public void setScreenHeight (int v)
	{
		screenHeight = v;
	}
	
	private void updateOrientation()
	{
		
		int screenWidthPart;
		int screenHeightPart;
		int dividerWidth;
		int dividerHeight;
		
		
		
		if (screenWidth < screenHeight)
		{
			dividerWidth = 6;
			dividerHeight = 8;

			this.orientation = ORIENTATION_PORTRAIT;

		}
		else
		{
			dividerWidth = 6;
			dividerHeight = 8;
			this.orientation = ORIENTATION_LANDSCAPE;
		}
		
		screenWidthPart = screenWidth / dividerWidth;
		screenHeightPart = screenHeight / dividerHeight;
		zoneMoveLeft 	= new int[]{0                    ,screenWidthPart       ,  screenHeightPart  * (dividerHeight - 2) ,screenHeightPart * (dividerHeight - 1) };
		zoneMoveRight 	= new int[]{screenWidthPart*2    ,screenWidthPart* 3 , screenHeightPart * (dividerHeight - 2) ,screenHeightPart * (dividerHeight - 1)};
		zoneMoveUp 		= new int[]{0                    ,screenWidthPart*2     ,  screenHeightPart  * (dividerHeight - 3) , (int)(screenHeightPart * (dividerHeight - 1.5))};
		zoneMoveDown 	= new int[]{0                    ,screenWidthPart*2     ,(int)( screenHeightPart   * (dividerHeight - 1.5)) ,screenHeightPart * dividerHeight};
		zoneBullet 		= new int[]{screenWidthPart* (dividerWidth - 3) ,screenWidthPart* (dividerWidth - 2), screenHeightPart * (dividerHeight - 2) ,screenHeightPart * dividerHeight};
		zoneBomb 		= new int[]{screenWidthPart* (dividerWidth - 2) ,screenWidthPart* (dividerWidth - 1), screenHeightPart * (dividerHeight - 2) ,screenHeightPart * dividerHeight};
		zoneBigBomb 	= new int[]{screenWidthPart* (dividerWidth - 1) ,screenWidthPart* (dividerWidth - 0), screenHeightPart * (dividerHeight - 2) ,screenHeightPart * dividerHeight};
		zoneZoomIn 		= new int[]{screenWidthPart* (dividerWidth - 1) ,screenWidthPart* (dividerWidth - 0), 0 ,screenHeightPart};
		zoneZoomOut 	= new int[]{screenWidthPart* (dividerWidth - 2) ,screenWidthPart* (dividerWidth - 1), 0 ,screenHeightPart};
		zoneCamLeft 	= new int[]{0 ,screenWidthPart, 0 ,screenHeightPart};
		zoneCamRight 	= new int[]{screenWidthPart , 2 * screenWidthPart, 0 ,screenHeightPart};
	}
	
	public boolean onTouch(View v, MotionEvent event) 
	{
		float posX;
		float posY;
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			currentGame.getHero().setDirection(Hero.DIRECTION_NONE);
			renderer.disableCameraZoom();
			renderer.disableCameraMove();
		}
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
			posX = (int) event.getX();
			posY = (int) event.getY();
			//Log.i(TAG, "Screen Width = " + screenWidth + ";screen height=" + screenHeight + ";posx=" + posX + ";posy=" + posY);
			if ( (posX > zoneMoveLeft[0]) && (posX < zoneMoveLeft[1]) && (posY > zoneMoveLeft[2]) && (posY < zoneMoveLeft[3]) )
			{
				currentGame.getHero().setDirection(Hero.DIRECTION_LEFT);
				Log.i(TAG, "Move left");
				return true;
			}
			
			if ( (posX > zoneMoveRight[0]) && (posX < zoneMoveRight[1]) && (posY > zoneMoveRight[2]) && (posY < zoneMoveRight[3]) )
			{
				currentGame.getHero().setDirection(Hero.DIRECTION_RIGHT);
				Log.i(TAG, "Move right");
				return true;
			}

			if ( (posX > zoneMoveUp[0]) && (posX < zoneMoveUp[1]) && (posY > zoneMoveUp[2]) && (posY < zoneMoveUp[3]) )
			{
				currentGame.getHero().setDirection(Hero.DIRECTION_UP);
				Log.i(TAG, "Move up");
				return true;
			}
			if ( (posX > zoneMoveDown[0]) && (posX < zoneMoveDown[1]) && (posY > zoneMoveDown[2]) && (posY < zoneMoveDown[3]) )
			{
				currentGame.getHero().setDirection(Hero.DIRECTION_DOWN);
				Log.i(TAG, "Move down");
				return true;
			}
			
			if ( (posX > zoneBullet[0]) && (posX < zoneBullet[1]) && (posY > zoneBullet[2]) && (posY < zoneBullet[3]) )
			{
				Log.i(TAG, "Bullet");
				currentGame.enableShoot();
				return true;
			}
			if ( (posX > zoneBomb[0]) && (posX < zoneBomb[1]) && (posY > zoneBomb[2]) && (posY < zoneBomb[3]) )
			{
				Log.i(TAG, "Bomb");
				currentGame.enableBomb();
				return true;
			}
			if ( (posX > zoneBigBomb[0]) && (posX < zoneBigBomb[1]) && (posY > zoneBigBomb[2]) && (posY < zoneBigBomb[3]) )
			{
				Log.i(TAG, "BigBomb");
				currentGame.enableBigBomb();
				return true;
			}
			
			if ( (posX > zoneZoomIn[0]) && (posX < zoneZoomIn[1]) && (posY > zoneZoomIn[2]) && (posY < zoneZoomIn[3]) )
			{
				renderer.zoomIn ();
				
				Log.i(TAG, "ZoomIn");
				return true;
			}
			
			if ( (posX > zoneZoomOut[0]) && (posX < zoneZoomOut[1]) && (posY > zoneZoomOut[2]) && (posY < zoneZoomOut[3]) )
			{
				renderer.zoomOut ();
				Log.i(TAG, "Zoom Out");
				return true;
			}
			 
			if ( (posX > zoneCamLeft[0]) && (posX < zoneCamLeft[1]) && (posY > zoneCamLeft[2]) && (posY < zoneCamLeft[3]) )
			{
				renderer.moveLeft ();
				Log.i(TAG, "Cam left");
				return true;
			}
			
			if ( (posX > zoneCamRight[0]) && (posX < zoneCamRight[1]) && (posY > zoneCamRight[2]) && (posY < zoneCamRight[3]) )
			{
				renderer.moveRight ();
				Log.i(TAG, "Cam right");
				return true;
			}
		}
		
		
		
		return true;
	}
	

}
