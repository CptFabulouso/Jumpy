package com.jerry.framework.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jerry.framework.Audio;
import com.jerry.framework.FileIO;
import com.jerry.framework.Game;
import com.jerry.framework.Graphics;
import com.jerry.framework.Input;
import com.jerry.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
	// to which we�ll draw, and will manage our main loop thread for us
	AndroidFastRenderView renderView;
	/*
	 * set the Graphics, Audio, Input, and FileIO members to instances of
	 * AndroidGraphics, AndroidAudio, AndroidInput, and AndroidFileIO
	 */
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	// holds the currently active Screen
	Screen screen;
	// to keep the screen from dimming
	WakeLock wakeLock;
	View mDecorView;
	private float screenHeight;
	private float screenWidth;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//mDecorView = getWindow().getDecorView();
		//hideSystemUI();
		
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 1280: 800;
		int frameBufferHeight = isLandscape ? 800 : 1280;
		/*
		 * The Bitmap instance has an RGB565 color format. This way, we don�t
		 * waste memory, and our drawing is completed a little faster
		 */
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		screenHeight =getWindowManager().getDefaultDisplay().getHeight();
		screenWidth =getWindowManager().getDefaultDisplay().getWidth();

		float scaleX = (float) frameBufferWidth
				/ screenWidth;
		float scaleY = (float) frameBufferHeight
				/ screenHeight ;

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"GLGame");

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void hideSystemUI() {
		mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LOW_PROFILE
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
		/*
		if(LoadingScreen.loaded && Settings.soundEnabled){
			Assets.music.play();
		}
		*/
	}

	@Override
	public void onPause() {
		super.onPause();
		//Assets.music.pause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		if (isFinishing()) {
			screen.dispose();
			//Assets.music.dispose();
		}
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen screen) {
		if (screen == null) {
			throw new IllegalArgumentException("Screen must not be null");
		}
		/*
		 * we tell the current Screen to pause and dispose of itself so that it
		 * can make room for the new Screen
		 */
		this.screen.pause();
		this.screen.dispose();
		/*
		 * The new Screen is asked to resume itself and update itself once with
		 * a delta time of zero
		 */
		screen.resume();
		screen.update(0);
		/*
		 * Finally, we set the Screen member to the new Screen.
		 */
		this.screen = screen;

	}

	public Screen getCurrentScreen() {
		return screen;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public float getScreenWidth() {
		return screenWidth;
	}

}
