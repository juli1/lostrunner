package org.gunnm.lostrunner.sounds;

import java.io.IOException;
import java.util.HashMap;

import android.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class Sound {

	private Context context;
	private String levelCompleted = "levelcompleted.wav";
	private String selection      = "selection.wav";
	private String death      = "death.wav";
	private String finished      = "finished.wav";
	private String gun      = "gun.wav";
	private String bomb      = "bomb.wav";
	private String bigBomb      = "bigbomb.wav";
	private SoundPool soundPool;
	private HashMap  poolMap;
	private AudioManager audioManager;
	public static final int SELECTION = 1;
	public static final int LEVEL_COMPLETED = 2;
	public static final int DEATH = 3;
	public static final int FINISHED = 4;
	public static final int GUN = 5;
	public static final int BOMB = 6;
	public static final int BIG_BOMB = 7;
	
	
	public static final int TRACK1 = 10;
	public static final int TRACK2 = 11;
	public static final int TRACK3 = 12;
	
	private MediaPlayer mediaPlayer;
	
	private static Sound instance = null;
	
	public Sound (Context c)
	{
		this.context = c;
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    poolMap = new HashMap();
	    audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	    mediaPlayer = MediaPlayer.create(c, org.gunnm.lostrunner.R.raw.track1);
	    mediaPlayer.setLooping(true);
		mediaPlayer.start();
		mediaPlayer.pause();
	    /*
	    mediaPlayer = new MediaPlayer();
	    try {
	        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	        Log.d("Sound", "setAudiotStreamType");
			mediaPlayer.setDataSource(c.getAssets().openFd("track1.mp3").getFileDescriptor());

	        Log.d("Sound", "setDataSource");
	        mediaPlayer.prepareAsync();
	        Log.d("Sound", "prepareAsync");
		} catch (Exception e)
		{
			Log.i("Sound", "error when loading sound" + e.toString());
		}*/
	}
	
	
	public static Sound getInstance (Context c)
	{
		if (instance == null)
		{
			instance = new Sound (c);
			instance.initSounds();
		}
		return instance;
	}
	
	
	public void initSounds ()
	{

	    try {
			poolMap.put(SELECTION, soundPool.load(context.getAssets().openFd(selection), 0));
			poolMap.put(LEVEL_COMPLETED, soundPool.load(context.getAssets().openFd(levelCompleted), 0));
			/*
			poolMap.put(BOMB, soundPool.load(context.getAssets().openFd(bomb), 0));
			poolMap.put(BIG_BOMB, soundPool.load(context.getAssets().openFd(bigBomb), 0));
			poolMap.put(GUN, soundPool.load(context.getAssets().openFd(gun), 0));
			*/
			poolMap.put(FINISHED, soundPool.load(context.getAssets().openFd(finished), 0));
			poolMap.put(DEATH, soundPool.load(context.getAssets().openFd(death), 0));
			
		} catch (IOException e) {
			
			Log.i("Sound", "Error when trying to load sound");
		}
	}
	
	public void startTrack ()
	{
		mediaPlayer.start();
	}
	
	public void stopTrack ()
	{
		mediaPlayer.pause();
	}
	
	public void playSound(int index)
	{
		float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    soundPool.play( (Integer) poolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
	
	
}
