package org.gunnm.lostrunner.controller;

import org.gunnm.lostrunner.graphics.LostRenderer;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.model.Hero;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class Key implements OnKeyListener
{
	private static final String TAG = "Key";
	private LostRenderer renderer;
	private Game game;
	
	public Key (LostRenderer r, Game g)
	{
		this.renderer = r;
		this.game = g;
	}

 
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
	
		if (event.getAction() == KeyEvent.ACTION_DOWN)
		{
			Log.i("KEY", "Keydown event");

			if (event.getKeyCode()  == KeyEvent.KEYCODE_SHIFT_RIGHT)
			{
				Log.i("KEY", "Going left");
				//LostRenderer.camX -= 0.2;
				game.getHero().setDirection(Hero.DIRECTION_RIGHT);
			}
			if (event.getKeyCode()  == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				Log.i("KEY", "Going left");
				//LostRenderer.camX -= 0.2;
				game.getHero().setDirection(Hero.DIRECTION_LEFT);

			}
			 
			if (event.getKeyCode()  == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				//Log.i("KEY", "Going right");
				//LostRenderer.camX += 0.2;
				game.getHero().setDirection(Hero.DIRECTION_RIGHT);
			}

			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
			{
				//Log.i("KEY", "Going up");
				//LostRenderer.camZ -= 0.2;
				game.getHero().setDirection(Hero.DIRECTION_UP);
			}
			
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN)
			{
				//Log.i("KEY", "Going down");
				//LostRenderer.camZ += 0.2;
				game.getHero().setDirection(Hero.DIRECTION_DOWN);
			}
		}
		return true;
	}
}
