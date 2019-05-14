package com.jerry.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;

import com.jerry.framework.Graphics;
import com.jerry.framework.Pixmap;

public class AndroidGraphics implements Graphics {
	AssetManager assets; // to load Bitmap instances
	Bitmap frameBuffer; // our artificial framebuffer
	Canvas canvas; // this we use to draw to the artificial framebuffer,
	Paint paint; // Paint we need for drawing
	// Rects for implementing the AndroidGraphics.drawPixmap() methods.
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	/*
	 * These last three members are there so we don�t have to create new
	 * instances of these classes on every draw call
	 */

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		// Canvas instance that will draw the artificial framebuffer
		this.canvas = new Canvas(frameBuffer);
		// Paint, which we use for some of the drawing methods
		this.paint = new Paint();
	}

	@Override
	public Pixmap newPixmap(String fileName, PixmapFormat format) {
		/*
		 * translating the PixmapFormat into one of the constants of the Android
		 * Config class
		 */
		Config config = null;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		/*
		 * we create a new Options instance and set our preferred color format
		 */
		Options options = new Options();
		options.inPreferredConfig = config;

		InputStream in = null;
		Bitmap bitmap = null;

		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null) {
				throw new RuntimeException("Couldn't load bitmap from asset"
						+ fileName + "'");
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		/*
		 * we check what format the BitmapFactory used to load the Bitmap and
		 * translate it into a PixmapFormat enumeration value. The BitmapFactory
		 * might decide to ignore our desired color format, so we have to check
		 * to determine what it used to decode the image
		 */
		if (bitmap.getConfig() == Config.RGB_565){
			format = PixmapFormat.RGB565;
			Log.e("Bitmap", "Bitmap loaded RGB565");
		}
		else if (bitmap.getConfig() == Config.ARGB_4444){
			format = PixmapFormat.ARGB4444;
			Log.e("Bitmap", "Bitmap loaded ARGB4444");	
		}
		else{
			format = PixmapFormat.ARGB8888;
			Log.e("Bitmap", "Bitmap loaded ARGB8888");		
		}

		/*
		 * Finally, we construct a new AndroidBitmap instance based on the
		 * Bitmap we loaded, as well as its PixmapFormat, and return it to the
		 * caller
		 */
		return new AndroidPixmap(bitmap, format);
	}

	/*
	 * extracts the red, green, and blue components of the specified 32- bit
	 * ARGB color parameter and calls the Canvas.drawRGB() method, which clears
	 * our artificial framebuffer with that color. This method ignores any alpha
	 * value of the specified color, so we don�t have to extract
	 * 
	 * @see framework.Graphics#clear(int)
	 */
	@Override
	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
	}

	@Override
	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);

	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		paint.setStrokeWidth(5);
		canvas.drawLine(x, y, x2, y2, paint);
	}
	
	@Override
	public void drawText(String text, int x, int y, int color, int textSize){
		paint.setColor(color);
		paint.setTextSize(textSize);
		canvas.drawText(text, x, y, paint);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);

	}

	@Override
	public void drawCircle(int x, int y, int radius, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(x, y, radius, paint);
	}
	
	/*
	 * sets up the source and destination of the Rect members that are used in
	 * the actual drawing call
	 * 
	 * @see framework.Graphics#drawPixmap(framework.Pixmap, int, int, int, int,
	 * int, int)
	 */
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {

		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;

		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,
				null);
	}

	/*
	 * draws the complete Pixmap to the artificial framebuffer at the given
	 * coordinates (non-Javadoc)
	 * 
	 * @see framework.Graphics#drawPixmap(framework.Pixmap, int, int)
	 */
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);
	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	/*
	 * return the size of the artificial framebuffer stored by the
	 * AndroidGraphics to which it renders internally (non-Javadoc)
	 * 
	 * @see framework.Graphics#getHeight()
	 */
	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}
}
