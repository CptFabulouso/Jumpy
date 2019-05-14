package com.jerry.framework.impl;

import com.jerry.framework.Pool;
import com.jerry.framework.Pool.PoolObjectFactory;
import com.jerry.framework.Input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

/*
 * The KeyboardHandler class implements the OnKeyListener interface so that it can
 receive key events from a View
 */
public class KeyboardHandler implements OnKeyListener {
	/*
	 * We store the current state (pressed or not) of each key in this array.
	 * The android.view.KeyEvent.KEYCODE_XXX constants (which encode the key
	 * codes) are all between 0 and 127, so we can store them in a garbage
	 * collector�friendly form
	 */
	boolean[] pressedKeys = new boolean[128];
	/*
	 * keyEventPool holds the instances of our KeyEvent class
	 */
	Pool<KeyEvent> keyEventPool;
	/*
	 * keyEventBuffer stores the KeyEvents that have not yet been consumed by
	 * our game. Each time we get a new key event on the UI thread, we add it to
	 * this list
	 */
	List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>(); 
	/*
	 * stores the KeyEvents that we return by calling the
	 * KeyboardHandler.getKeyEvents().
	 */
    List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
    
 // View from which we want to receive key events
    public KeyboardHandler(View view) {
    	/*
		 * We create the Pool instance with a proper PoolObjectFactory, register
		 * the handler as an OnKeyListener with the View, and finally, make sure
		 * that the View will receive key events by making it the focused View.
		 */
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool<KeyEvent>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
            return false;
        /*
		 * the events are received on the UI thread and read on the main loop
		 * thread, so we have to make sure that none of our members are accessed
		 * in parallel.
		 */
        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    /*
	 * we pass in an integer that specifies the key code (one of the Android
	 * KeyEvent.KEYCODE_XXX constants) and returns whether that key is pressed
	 * or not
	 */
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            /*
			 * we loop through the keyEvents array, and insert all of its
			 * KeyEvents into our Pool. We fetch instances from the Pool in the
			 * onKey() method on the UI thread. Here, we reinsert them into the
			 * Pool
			 */
            for (int i = 0; i < len; i++) {
                keyEventPool.free(keyEvents.get(i));
            }
            /*
			 * events in our keyEventsBuffer list. Finally, we clear the
			 * keyEventsBuffer list and return the newly�filled keyEvents list
			 * to the caller
			 */
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
            /*
			 *For more explanation see page 224, beginning android 4 games dev
			 */
        }
    }
}