package org.gunnm.lostrunner;

import org.gunnm.lostrunner.graphics.InstructionsRenderer;
import org.gunnm.lostrunner.sounds.Sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class Instructions extends Activity implements OnTouchListener
{
	
	private GLSurfaceView 			surface;
	private InstructionsRenderer	renderer;
	private int screenWidth;
	private int screenHeight;
	private int oldPositionX;
	private int oldPositionY;
	
	public void onCreate(Bundle savedInstanceState) {
		WindowManager wm;
		Display display;
		
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        surface = new GLSurfaceView(this);
       
        renderer = new InstructionsRenderer(this);
        surface.setRenderer(renderer);
        surface.setOnTouchListener (this);
        surface.setFocusable (true);
        setContentView(surface);
        
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		this.screenWidth 	= display.getWidth();
		this.screenHeight 	= display.getHeight();
		this.oldPositionX   = 0;
		this.oldPositionY   = 0;
    }
	public void movePreviousPage ()
	{
		if (renderer.getPage() == InstructionsRenderer.PAGE_MOVE)
		{
			Intent intent = new Intent(this, org.gunnm.lostrunner.Title.class);
			startActivity(intent);
		}
		else
		{
			renderer.setPage(renderer.getPage() - 1);
		}
	}
	
	public void moveNextPage  ()
	{
		
		if (renderer.getPage() == InstructionsRenderer.PAGE_UTILS)
		{
        	Intent intent = new Intent(this, org.gunnm.lostrunner.Title.class);
        	startActivity(intent);
		}
		else
		{
			renderer.setPage(renderer.getPage() + 1);
		}
	}
	
	public boolean onTouch(View v, MotionEvent event)
	{
		int posX;
		int posY;
		int divider;
		int partSize;
		
		divider = 6;
		partSize = screenHeight / divider;
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			oldPositionX = (int) event.getX();
			oldPositionY = (int) event.getY();
			posX = (int) event.getX();
			posY = (int) event.getY();
			
			if ( (posX < screenWidth / 2) && (posY > screenHeight * 0.8))
			{
				movePreviousPage();
			}
			if ( (posX > screenWidth / 2) && (posY > screenHeight * 0.8))
			{
				moveNextPage();
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			posX = (int) event.getX();
			posY = (int) event.getY();
			
			
			if ( Math.abs(posX - oldPositionX) > (screenWidth / 3))
			{
				if (posX > oldPositionX)
				{
					movePreviousPage ();
					//Log.i("Instructions", "Going right");

				}
				else
				{
					//Log.i("Instructions", "Going left");
					moveNextPage();
				}
			}
		}
		return true;
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	protected void onPause() {
		super.onPause();

		Sound.getInstance(this).stopTrack();
		
		surface.onPause();
	}

	protected void onResume() {
		super.onResume();

		Sound.getInstance(this).startTrack();
		
		surface.onResume();
	}
		
	
}


