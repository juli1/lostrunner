package org.gunnm.lostrunner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.scoreloop.client.android.core.model.Continuation;
import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;
import com.scoreloop.client.android.ui.ShowResultOverlayActivity;

public class Score implements OnScoreSubmitObserver {
	private static Score 		instance 			= null;
	private Activity 			relatedActivity;
	private Context				context;
	private int  				scoreSubmitStatus;
	private boolean				initialized         = false;
	public final static int    	SHOW_RESULT 		= 2;
	public final static int    	POST_SCORE 			= 3;
	
	public Score (Context c)
	{
		try
		{
			//	    		Log.i("Title", "initialize scoreloop");
			ScoreloopManagerSingleton.init(c, org.gunnm.lostrunner.configuration.ScoreLoop.scoreLoopSecret);
			ScoreloopManagerSingleton.get().setOnScoreSubmitObserver(this);
			initialized = true;
		}
		catch (IllegalStateException e)
		{
			//	      		Log.i("Title", "error when initializng scoreloop" + e.toString());
		}
	}
	

	public void setInitialized (boolean b)
	{
		this.initialized = b;
	}
	
	public boolean getInitialized ()
	{
		return this.initialized;
	}
	

    
	
	public static Score getInstance (Context c)
	{
		if (instance == null)
		{
			instance = new Score(c);
		}
		instance.setContext (c);
		return instance;
	}
	
	public void setContext (Context c)
	{

		this.context = c;
		if (this.initialized == false)
		{
			try
			{
				//	    		Log.i("Title", "initialize scoreloop");
				ScoreloopManagerSingleton.init(c, org.gunnm.lostrunner.configuration.ScoreLoop.scoreLoopSecret);
				initialized = true;
			}
			catch (IllegalStateException e)
			{
				//	      		Log.i("Title", "error when initializng scoreloop" + e.toString());
			}
		}
	}
	


    public void registerScore (int score)
    {
    	Double result = new Double (score);
    	
    	if(this.useScoreLoop())
    	{
    		ScoreloopManagerSingleton.get().onGamePlayEnded(result, null);
    	}
    	return;
    }
    
    public void setActivity (Activity a)
    {
    	this.relatedActivity = a;
    }



    public void onScoreSubmit(final int status, final Exception error) {
    	SharedPreferences 	preferences;
    	boolean 			useScoreloop;
    	
    	scoreSubmitStatus = status;

    	if(relatedActivity == null)
    	{
    		return;
    	}

    	preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    	useScoreloop = preferences.getBoolean("use_scoreloop", false);
    	
    	if (useScoreloop && this.initialized)
    	{
    		checkTermsOfService();
    		relatedActivity.startActivityForResult(new Intent(this.context, ShowResultOverlayActivity.class), SHOW_RESULT);
    	}
    }
    
    private boolean useScoreLoop ()
    {
    	final SharedPreferences preferences;
	    boolean use;
	    
		preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
	    	
	    use = preferences.getBoolean("use_scoreloop", false);
	    
	    return use;
    }
    
    private void disableScoreLoop ()
    {
		PreferenceManager.getDefaultSharedPreferences(this.context).edit().putBoolean("use_scoreloop", false);
    }
    
    
    public int getScoreSubmitStatus ()
    {
    	return this.scoreSubmitStatus;
    }
    
    public void checkTermsOfService ()
    {

	    if (this.useScoreLoop())
	    {
	    	if (! this.initialized)
	    	{
	    		return;
	    	}
	    	
	    	ScoreloopManagerSingleton.get().askUserToAcceptTermsOfService(relatedActivity, new Continuation<Boolean>() {
	    		public void withValue(final Boolean value, final Exception error) {
	    			if (value != null && value) {
	    				
	    			}
	    			else
	    			{
	    				disableScoreLoop();
	    			}
	    		}
	    	});
	    }
	    else
	    {
	    	disableScoreLoop();
	    }
    }
}
