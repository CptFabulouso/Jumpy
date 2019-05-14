package com.jerry.framework;

import com.jerry.framework.Graphics.PixmapFormat;

public interface Pixmap {

	/*
	 * The Pixmap.getWidth() and Pixmap.getHeight() methods return the width and
	 * the height of the Pixmap in pixels.
	 */
	public int getWidth();

	public int getHeight();

	/*
	 * The Pixmap.getFormat() method returns the PixelFormat that the Pixmap is
	 * stored with in RAM.
	 */
	public PixmapFormat getFormat();

	/*
	 * Finally, there�s the Pixmap.dispose() method. Pixmap instances use up
	 * memory and potentially other system resources. If we no longer need them,
	 * we should dispose of them with this method.
	 */
	public void dispose();

}
