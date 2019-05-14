package com.jerry.framework;

public interface Graphics {

	public static enum PixmapFormat {
		ARGB8888, ARGB4444, RGB565
	}

	/*
	 * method will load an image given in either JPEG or PNG format. We specify
	 * a desired format for the resulting Pixmap, which is a hint for the
	 * loading mechanism. The resulting Pixmap might have a different format. We
	 * do this so that we can somewhat control the memory footprint of our
	 * loaded images (for example, by loading RGB888 or ARGB8888 images as
	 * RGB565 or ARGB4444 images). The filename specifies an asset in our
	 * application�s APK file.
	 */
	public Pixmap newPixmap(String fileName, PixmapFormat format);

	/*
	 * method clears the complete framebuffer with the given color. All colors
	 * in our little framework will be specified as 32-bit ARGB8888 values
	 * (Pixmaps might of course have a different format).
	 */
	public void clear(int color);

	/*
	 * The Graphics.drawPixel() method will set the pixel at (x,y) in the
	 * framebuffer to the given color. Coordinates outside the screen will be
	 * ignored. This is called clipping.
	 */
	public void drawPixel(int x, int y, int color);

	/*
	 * The Graphics.drawLine() method is analogous to the Graphics.drawPixel()
	 * method. We specify the start point and endpoint of the line, along with a
	 * color. Any portion of the line that is outside the framebuffer�s raster
	 * will be ignored.
	 */
	public void drawLine(int x, int y, int x2, int y2, int color);

	/*
	 * The Graphics.drawRect() method draws a rectangle to the framebuffer. The
	 * (x,y) specifies the position of the rectangle's top-left corner in the
	 * framebuffer. The arguments width and height specify the number of pixels
	 * in x and y, and the rectangle will fill starting from (x,y). We fill
	 * downward in y. The color argument is the color that is used to fill the
	 * rectangle.
	 */
	public void drawRect(int x, int y, int width, int height, int color);
	
	public void drawCircle(int x, int y, int radius,int color);

	public void drawText(String text, int x, int y, int color, int textSize);
	
	/*
	 * The Graphics.drawPixmap() method draws rectangular portions of a Pixmap
	 * to the framebuffer. The (x,y) coordinates specify the top-left corner�s
	 * position of the Pixmap's target location in the framebuffer. The
	 * arguments srcX and srcY specify the corresponding top-left corner of the
	 * rectangular region that is used from the Pixmap, given in the Pixmap's
	 * own coordinate system. Finally, srcWidth and srcHeight specify the size
	 * of the portion that we take from the Pixmap.
	 */
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight);

	public void drawPixmap(Pixmap pixmap, int x, int y);

	/*
	 * Graphics.getWidth() and Graphics.getHeight() methods return the width and
	 * height of the framebuffer in pixels
	 */
	public int getWidth();

	public int getHeight();

}
