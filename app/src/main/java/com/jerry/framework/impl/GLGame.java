package com.jerry.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.jerry.framework.Audio;
import com.jerry.framework.FileIO;
import com.jerry.framework.Game;
import com.jerry.framework.Graphics;
import com.jerry.framework.Input;
import com.jerry.framework.Screen;

public abstract class GLGame extends Activity implements Game, Renderer {

	enum GLGameState {
		Initialized, Running, Paused, Finished, Idle
	}

	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;

	// object we�ll use to synchronize the UI and rendering threads.
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);

		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		/*
		 * The scale values are both 1, so we will get the real touch
		 * coordinates
		 */
		input = new AndroidInput(this, glView, 1, 1);
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"GLGame");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);

		/*
		 * The synchronization is necessary, since the members we manipulate
		 * within the synchronized block could be manipulated in the onPause()
		 * method on the UI thread
		 */
		synchronized (stateChanged) {
			/*
			 * If the application is started for the first time, state will be
			 * GLGameState.Initialized. In this case, we call the
			 * getStartScreen() method to return the starting screen of the
			 * game. If the game is not in an initialized state but has already
			 * been running, we know that we have just resumed from a paused
			 * state
			 */
			if (state == GLGameState.Initialized) {
				screen = getStartScreen();
			}
			state = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;

		synchronized (stateChanged) {
			state = this.state;
		}

		if (state == GLGameState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();

			screen.update(deltaTime);
			screen.present(deltaTime);
		}

		if (state == GLGameState.Paused) {
			screen.pause();
			screen.dispose();

			/*
			 * GLGameState.Idle, indicating that we have received the pause
			 * request from the UI thread. Since we wait for this to happen in
			 * the onPause() method in the UI thread, we notify the UI thread
			 * that it can now truly pause the application. This notification is
			 * necessary, as we have to make sure that the rendering thread is
			 * paused/shut down properly in case our Activity is paused or
			 * closed on the UI thread.
			 */
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}

		if (state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();

			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}

		}

	}

	@Override
	protected void onPause() {

		synchronized (stateChanged) {
			if (isFinishing()) {
				state = GLGameState.Finished;
			} else {
				state = GLGameState.Paused;
			}
			/*
			 * Depending on whether the application is closed or paused, we set
			 * state accordingly and wait for the rendering thread to process
			 * the new state. This is achieved with the standard Java
			 * wait/notify mechanism.
			 */
			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		wakeLock.release();
		glView.onPause();
		super.onPause();

	}

	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		throw new IllegalStateException("We are using OpenGL!");
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen newScreen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		this.screen.pause();
		this.screen.dispose();
		newScreen.resume();
		newScreen.update(0);
		this.screen = newScreen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}
}
