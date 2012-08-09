package org.gunnm.lostrunner.utils;

import android.app.Activity;
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
	private int  				scoreSubmitStatus;
	private boolean				initialized         = false;
	public final static int    	SHOW_RESULT 		= 2;
	public final static int    	POST_SCORE 			= 3;
	
	public Score ()
	{
		initialized = false;
	}
	
	
	public void setInitialized (boolean b)
	{
		this.initialized = b;
	}
	
	public boolean getInitialized ()
	{
		return this.initialized;
	}
	
	public static Score getInstance ()
	{
		if (instance == null)
		{
			instance = new Score();
		}
		return instance;
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

    	preferences = PreferenceManager.getDefaultSharedPreferences(relatedActivity);
    	useScoreloop = preferences.getBoolean("use_scoreloop", false);
    	
    	if (useScoreloop && this.initialized)
    	{
    		checkTermsOfService();
    		relatedActivity.startActivityForResult(new Intent(relatedActivity, ShowResultOverlayActivity.class), SHOW_RESULT);
    	}
    }
    
    private boolean useScoreLoop ()
    {
    	final SharedPreferences preferences;
	    boolean use;
	    
    	if (relatedActivity == null)
    	{
    		return false;
    	}
    	
		preferences = PreferenceManager.getDefaultSharedPreferences(relatedActivity);
	    	
	    use = preferences.getBoolean("use_scoreloop", false);
	    
	    return use;
    }
    
    private void disableScoreLoop ()
    {

	    
    	if (relatedActivity == null)
    	{
    		return;
    	}
    	
		PreferenceManager.getDefaultSharedPreferences(relatedActivity).edit().putBoolean("use_scoreloop", false);
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
