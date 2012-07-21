package org.gunnm.lostrunner.controller;

import org.gunnm.lostrunner.graphics.LostRenderer;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class Key implements OnKeyListener
{
	private static final String TAG = "Key";
	private LostRenderer renderer;
	
	public Key (LostRenderer r)
	{
		this.renderer = r;
	
	}

 
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
	
		if (event.getAction() == KeyEvent.ACTION_DOWN)
		{
			Log.i("KEY", "Keydown event");

			if (event.getKeyCode()  == KeyEvent.KEYCODE_SHIFT_RIGHT)
			{
				Log.i("KEY", "Going left");
				LostRenderer.camX -= 0.2;
			}
			if (event.getKeyCode()  == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				Log.i("KEY", "Going left");
				LostRenderer.camX -= 0.2;
			}
			 
			if (event.getKeyCode()  == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				Log.i("KEY", "Going right");
				LostRenderer.camX += 0.2;
			}

			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
			{
				Log.i("KEY", "Going up");
				LostRenderer.camZ -= 0.2;
			}
			
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN)
			{
				Log.i("KEY", "Going down");
				LostRenderer.camZ += 0.2;
			}
		}
		return true;
	}
}
