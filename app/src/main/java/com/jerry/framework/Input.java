package com.jerry.framework;

import java.util.List;

public interface Input {

	public static class KeyEvent {
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;

		public int type;
		public int keyCode;
		public char keyChar;

		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (type == KEY_DOWN)
				builder.append("key down, ");
			else
				builder.append("key up, ");
			builder.append(keyCode);
			builder.append(",");
			builder.append(keyChar);
			return builder.toString();
		}
	}

	public static class TouchEvent {
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;

		public int type; // touch events type
		public int x, y; // position of the finger relative to the UI
							// component�s origin
		public int pointer;/*
							 * The pointer ID for a finger will stay the same
							 * for as long as that finger is on the screen. If
							 * two fingers are down and finger 0 is lifted, then
							 * finger 1 keeps its ID for as long as it is
							 * touching the screen. A new finger will get the
							 * first free ID, which would be 0 in this example
							 */

		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (type == TOUCH_DOWN)
				builder.append("touch down, ");
			else if (type == TOUCH_DRAGGED)
				builder.append("touch dragged, ");
			else
				builder.append("touch up, ");
			builder.append(pointer);
			builder.append(",");
			builder.append(x);
			builder.append(",");
			builder.append(y);
			return builder.toString();
		}
	}

	public boolean isKeyPressed(int keyCode);

	// takes a pointer ID and returns whether a given pointer is down, as well
	// as its current x- and y-coordinates
	public boolean isTouchDown(int pointer);

	public int getTouchX(int pointer);

	public int getTouchY(int pointer);

	// return the respective acceleration values of each accelerometer axis
	public float getAccelX();

	public float getAccelY();

	public float getAccelZ();

	/*
	 * used for event-based handling. They return the KeyEvent and TouchEvent
	 * instances that got recorded since the last time we called these methods.
	 * The events are ordered according to when they occurred, with the newest
	 * event being at the end of the list.
	 */
	public List<KeyEvent> getKeyEvents();

	public List<TouchEvent> getTouchEvents();

}
