package org.gunnm.lostrunner;

import org.gunnm.lostrunner.sounds.Sound;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity 
{
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	protected void onResume ()
	{
		super.onResume();
		Sound.getInstance(this).startTrack();
	}
	
	protected void onPause ()
	{
		super.onPause();
		Sound.getInstance(this).stopTrack();
	}
}

