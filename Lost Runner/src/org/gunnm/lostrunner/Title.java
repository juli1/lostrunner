package org.gunnm.lostrunner;

import org.gunnm.lostrunner.controller.Key;
import org.gunnm.lostrunner.controller.Touch;
import org.gunnm.lostrunner.graphics.LostRenderer;
import org.gunnm.lostrunner.graphics.TitleRenderer;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.sounds.Sound;
import org.gunnm.lostrunner.utils.Score;

import com.scoreloop.client.android.core.model.Continuation;
import com.scoreloop.client.android.ui.LeaderboardsScreenActivity;
import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.scoreloop.client.android.ui.PostScoreOverlayActivity;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
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
	private Score scores;

	AlertDialog.Builder builder;
	private boolean scoreLoopInitialized = false;
	
	public Title ()
	{
        
	}
	
	public void onCreate(Bundle savedInstanceState) {
		WindowManager wm;
		Display display;
		
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        if (scoreLoopInitialized == false)
        {
        	try
        	{
        		Log.i("Title", "initialize scoreloop");
        		ScoreloopManagerSingleton.init(this, org.gunnm.lostrunner.configuration.ScoreLoop.scoreLoopSecret);
        		scoreLoopInitialized = true;
        	}
        	catch (IllegalStateException e)
        	{
        		Log.i("Title", "error when initializng scoreloop" + e.toString());
        	}
        }
        Score.getInstance().setInitialized(scoreLoopInitialized);
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
		
		builder = new AlertDialog.Builder(this);

		sound.startTrack();
		Log.i("Title", "onCreate");
    }
		 
	public boolean onTouch(View v, MotionEvent event)
	{
		int posX;
		int posY;
		int divider;
		int partSize;
		final Activity currentActivity;
		
		currentActivity = this;
		
		divider = 6;
		partSize = screenHeight / divider;
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			posX = (int) event.getX();
			posY = (int) event.getY();
			
			
			if ( (posY < 3 * partSize) &&  (posY > 2 * partSize))
			{
				sound.playSound(Sound.SELECTION);
				sound.startTrack();
				Game.getInstance(this).reset();
	        	Intent intent = new Intent(this, org.gunnm.lostrunner.Main.class);
	        	startActivity(intent);
			}
			if ( (posY < 4 * partSize) &&  (posY > 3 * partSize))
			{
				
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			    boolean useScoreloop;

			    useScoreloop = preferences.getBoolean("use_scoreloop", false);
			    
			    if (useScoreloop)
			    {
			    	ScoreloopManagerSingleton.get().askUserToAcceptTermsOfService( this, new Continuation<Boolean>() {
			    		public void withValue(final Boolean value, final Exception error) {
			    			if (value != null && value) {
								Intent intent = new Intent(currentActivity, LeaderboardsScreenActivity.class);
								startActivity(intent);
			    			}
			    		}
			    	});
			    }
			    else
			    {
			    	  builder.setMessage("You must use Score Loop to get access to the leaderboard. Check game settings in the menu.");  
			          builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {  
			               public void onClick(DialogInterface dialog, int which) {  
			                    
			               }  
			          });  
			          AlertDialog alert = builder.create();  
			          alert.show();
			    }
			}
			if ( (posY < 5 * partSize) &&  (posY > 4 * partSize))
			{
				sound.playSound(Sound.SELECTION);
				if ( (this.screenWidth < 400) || (this.screenHeight < 800))
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://julien.gunnm.org/data/misc/lost-runner-instructions.html")));
						
				}
				else
				{
					Intent intent = new Intent(this, org.gunnm.lostrunner.Instructions.class);
					startActivity(intent);
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
		surface.onPause();
		Log.i("Title", "onPause");
	}

	protected void onResume() {
		Game currentGame;
		Score score;
		
		super.onResume();
		Log.i("Title", "onResume");
		surface.onResume();
		sound.startTrack();
		scores = Score.getInstance();
		scores.setActivity(this);
		scores.checkTermsOfService();
		
		currentGame = Game.getInstance(this);
		score = Score.getInstance();
		score.setActivity(this);
		if (currentGame.isCompleted() && ( ! currentGame.getScoreSubmitted()))
		{
			Log.i("Title", "onResume score not submitted");
			score.registerScore(currentGame.getElapsedSec());
			currentGame.setScoreSubmitted(true);
		}
		
	}
		
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_settings:
	        {
	        	startActivity(new Intent(this, org.gunnm.lostrunner.Preferences.class));
	            return true;
	        }
	        default:
	        {
	            return super.onOptionsItemSelected(item);
	        }
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
			 }

			 break;

		 case Score.POST_SCORE:

			 break;
		 default:
			 break;
		 }
	 }
}


