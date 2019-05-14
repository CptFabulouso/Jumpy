package com.jerry.framework.impl;

import java.io.IOException;

import com.jerry.framework.Audio;
import com.jerry.framework.Music;
import com.jerry.framework.Sound;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio {
	AssetManager assets; // for loading files from assets
	SoundPool soundPool;

	public AndroidAudio(Activity activity) {
		/*
		 * There are two reasons why we pass our game�s Activity in the
		 * constructor: it allows us to set the volume control of the media
		 * stream (we always want to do that), and it gives us an AssetManager
		 * instance, which we will happily store in the corresponding class
		 * member. The SoundPool is configured to play back 20 sound effects in
		 * parallel, which is adequate for our needs.
		 */
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	@Override
	public Music newMusic(String fileName) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return new AndroidMusic(assetDescriptor); //creates new AndoridMusic instance
		} catch (IOException e){
			//We catch it and re-throw it as a RuntimeException
			throw new RuntimeException("Couldn't load music " + fileName + "'");
		}	
	}

	@Override
	public Sound newSound(String fileName) {
		try {
			//loads a sound effect from an asset into the SoundPool
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load sound " + fileName + "'");
		}
		
	}

}
