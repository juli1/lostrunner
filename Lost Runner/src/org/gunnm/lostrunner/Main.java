package org.gunnm.lostrunner;

import org.gunnm.lostrunner.controller.Key;
import org.gunnm.lostrunner.controller.Touch;
import org.gunnm.lostrunner.graphics.LostRenderer;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.utils.Score;

import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.scoreloop.client.android.ui.PostScoreOverlayActivity;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
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
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		scores 				= Score.getInstance ();
		scores.setActivity(this);
		ScoreloopManagerSingleton.get().setOnScoreSubmitObserver(scores);
        
        currentGame = new Game (this);
        gestureDetector = new GestureDetector(this, new GlAppGestureListener(this));
        
        surface = new GLSurfaceView(this);
       
        renderer = new LostRenderer(this, currentGame);
        surface.setRenderer(renderer);
        surface.setOnKeyListener(new Key(renderer, currentGame));
        surface.setOnTouchListener(new Touch(this, renderer, currentGame));
        surface.setFocusable(true);
        setContentView(surface);
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
		if (currentGame.isActive() == false)
		{
			currentGame.reset ();
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
	
	
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) 
	 {

		 scores = Score.getInstance();
		 scores.setActivity(this);
		 switch (requestCode) {

		 case Score.SHOW_RESULT:
			 if (scores.getScoreSubmitStatus() != OnScoreSubmitObserver.STATUS_ERROR_NETWORK) {
				 // Show the post-score activity unless there has been a network error.
				 startActivityForResult(new Intent(this, PostScoreOverlayActivity.class), Score.POST_SCORE);
			 } else { 

				 finish();
			 }

			 break;

		 case Score.POST_SCORE:

			 // Here we get notified that the PostScoreOverlay has finished.
			 // in this example this simply means that we're ready to return to the main activity
			 finish();
			 break;
		 default:
			 break;
		 }
	 }
	
}


