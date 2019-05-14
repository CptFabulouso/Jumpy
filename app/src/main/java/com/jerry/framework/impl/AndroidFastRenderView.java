package com.jerry.framework.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(AndroidGame game, Bitmap frameBuffer) {
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		this.holder = getHolder();
	}

	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

	@Override
	public void run() {
		Rect dstRect = new Rect();
		/*
		 * returns the current time in nanoseconds as a long
		 */
		long startTime = System.nanoTime();
		while (running) {
			if (!holder.getSurface().isValid())
				continue;

			/*
			 * To make it easier to work with that delta, we convert it into
			 * seconds
			 */
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();

			/*
			 * time. With the delta time at hand, we call the current Screen�s
			 * update() and present() methods, which will update the game logic
			 * and render things to the artificial framebuffer
			 */
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);

			Canvas canvas = holder.lockCanvas();
			/*
			 * Note that we�ve used a shortcut here to get a destination
			 * rectangle that stretches over the whole SurfaceView via the
			 * Canvas.getClipBounds() method.It will set the top and left
			 * members of dstRect to 0 and 0, respectively, and the bottom and
			 * right members to the actual screen dimensions
			 */
			canvas.getClipBounds(dstRect);
			/*
			 * The scaling is performed automatically in case the destination
			 * rectangle we pass to the Canvas.drawBitmap() method is smaller or
			 * bigger than the framebuffer
			 */
			canvas.drawBitmap(frameBuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);

		}
	}

	/*
	 * terminates the rendering/main loop thread and waits for it to die
	 * completely before returning.
	 */
	public void pause() {
		running = false;
		while (true) {
			try {
				renderThread.join();
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
