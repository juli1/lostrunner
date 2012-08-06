package org.gunnm.lostrunner;

import org.gunnm.lostrunner.controller.Key;
import org.gunnm.lostrunner.controller.Touch;
import org.gunnm.lostrunner.graphics.LostRenderer;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.sounds.Sound;
import org.gunnm.lostrunner.utils.Score;

import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.scoreloop.client.android.ui.PostScoreOverlayActivity;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
	
	private boolean 		fullScreen = false;
	private GLSurfaceView 	surface;
	private LostRenderer	renderer;
	private Game			currentGame;
	private GestureDetector gestureDetector;
	private Score			scores;
	private Handler			handler;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        handler = new Handler();
        
		scores 				= Score.getInstance ();
		scores.setActivity(this);
		ScoreloopManagerSingleton.get().setOnScoreSubmitObserver(scores);
        
        currentGame = Game.getInstance(this);
        currentGame.start();
        
        gestureDetector = new GestureDetector(this, new GlAppGestureListener(this));
        
        surface = new GLSurfaceView(this);
       
        renderer = new LostRenderer(this, currentGame);
        surface.setRenderer(renderer);
        surface.setOnKeyListener(new Key(renderer, currentGame));
        surface.setOnTouchListener(new Touch(this, renderer, currentGame));
        surface.setFocusable(true);
        setContentView(surface);
		//Log.i("Main", "onCreate");

    }
	
	
	public void notifyScore ()
	{
		Looper.prepare ();
		handler.post (new Runnable()
		{
			public void run()
			{
				Score.getInstance().registerScore(currentGame.getElapsedSec());
			}
		}
		);
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	protected void onPause() {
		super.onPause();
		surface.onPause();
		Sound.getInstance(this).stopTrack();
	}

	protected void onResume() {
		super.onResume();
		surface.onResume();
		//Log.i("Main", "onResume");
		Sound.getInstance(this).startTrack();
		if (currentGame.isActive() == false)
		{
			currentGame.reset ();
			currentGame.start ();
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	
	
	private class GlAppGestureListener extends GestureDetector.SimpleOnGestureListener
    {
    	private Main currentApp;
    	
    	public GlAppGestureListener(Main app) {
    		this.currentApp = app;
    	}


    }
	
	
	
}


