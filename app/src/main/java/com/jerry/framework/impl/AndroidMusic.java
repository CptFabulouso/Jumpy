package com.jerry.framework.impl;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import com.jerry.framework.Music;

public class AndroidMusic implements Music, OnCompletionListener {
	MediaPlayer mediaPlayer;
	boolean isPrepared = false;

	// we can only call MediaPlayer.start()/stop()/pause() when the
	// MediaPlayer is prepared.

	public AndroidMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}

	@Override
	public void play() {
		if (mediaPlayer.isPlaying()) {
			// If we are already playing, we simply return from the function.
			return;
		}
		try {
			/*
			 * This is conducted in a synchronized block, since we are using the
			 * isPrepared flag, which might get set on a separate thread because
			 * we are implementing the OnCompletionListener interface
			 */
			synchronized (this) {
				// check to see if the MediaPlayer is already prepared based on
				// our flag;
				if (!isPrepared) {
					mediaPlayer.prepare();
				}
				mediaPlayer.start();
			}
		} catch (IllegalStateException e) {
			Log.w("LOG", "printStackTrace");
			e.printStackTrace();
		} catch (IOException e) {
			Log.w("LOG", "IOException");
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		mediaPlayer.stop();
		synchronized (this) {
			isPrepared = false;
		}

	}

	@Override
	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}

	}

	@Override
	public void setLooping(boolean isLooping) {
		mediaPlayer.setLooping(isLooping);
	}

	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);

	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !isPrepared;
	}

	@Override
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	@Override
	public void dispose() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
	}

	
	@Override
	public void onCompletion(MediaPlayer player) {
		/*
		 * in a synchronized block so that the other methods don�t start
		 * throwing exceptions out of the blue.
		 */
		synchronized (this) {
			isPrepared = false;
		}
	}

}
