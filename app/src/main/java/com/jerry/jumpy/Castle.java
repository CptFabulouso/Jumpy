package com.jerry.jumpy;

import com.jerry.framework.GameObject;

/**
 * Created by Pavel on 14. 12. 2014.
 */
public class Castle extends GameObject {
    public static float CASTLE_WIDTH = 1.7f;
    public static float CASTLE_HEIGHT = 1.7f;


    public Castle(float x, float y) {
        super(x, y, CASTLE_WIDTH, CASTLE_HEIGHT);
    }
}
