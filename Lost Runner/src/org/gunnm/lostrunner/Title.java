package org.gunnm.lostrunner;

import org.gunnm.lostrunner.controller.Key;
import org.gunnm.lostrunner.controller.Touch;
import org.gunnm.lostrunner.graphics.LostRenderer;
import org.gunnm.lostrunner.graphics.TitleRenderer;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.sounds.Sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class Title extends Activity implements OnTouchListener
{
	
	private GLSurfaceView 	surface;
	private TitleRenderer	renderer;
	private static boolean 	fullscreen;
	private int screenWidth;
	private int screenHeight;
	private Sound sound;
	
	public void onCreate(Bundle savedInstanceState) {
		WindowManager wm;
		Display display;
		
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        sound = Sound.getInstance(this);
        surface = new GLSurfaceView(this);
        renderer = new TitleRenderer(this);
        surface.setRenderer(renderer);
        surface.setOnTouchListener (this);
        surface.setFocusable (true);
        setContentView(surface);
        
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		this.screenWidth 	= display.getWidth();
		this.screenHeight 	= display.getHeight();
		sound.startTrack();
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
			posX = (int) event.getX();
			posY = (int) event.getY();
			//Log.i("Title", "screenWidth=" + screenWidth + ";screenHeight=" + this.screenHeight + ";posX=" + posX + ";posY=" + posY);
			
			
			if ( (posY < 3 * partSize) &&  (posY > 2 * partSize))
			{
				//Log.i("Title","Start or continue");
				sound.playSound(Sound.SELECTION);
				sound.startTrack();

	        	Intent intent = new Intent(this, org.gunnm.lostrunner.Main.class);
	        	startActivity(intent);
			}
			if ( (posY < 4 * partSize) &&  (posY > 3 * partSize))
			{
				//Log.i("Title","scores");
			}
			if ( (posY < 5 * partSize) &&  (posY > 4 * partSize))
			{
				sound.playSound(Sound.SELECTION);
				//Log.i("Title","instructions");
	        	Intent intent = new Intent(this, org.gunnm.lostrunner.Instructions.class);
	        	startActivity(intent);
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
		surface.onPause();
	}

	protected void onResume() {
		super.onResume();
		surface.onResume();
		sound.startTrack();
	}
		
	
}


