package com.jerry.jumpy;

import com.jerry.framework.GameObject;

/**
 * Created by Pavel on 14. 12. 2014.
 */
public class Coin extends GameObject{
    public static final float COIN_WIDTH = 0.5f;
    public static final float COIN_HEIGHT = 0.5f;
    public static final int COIN_SCORE = 10;
    private float stateTime;

    public Coin(float x, float y) {
        super(x, y, COIN_WIDTH, COIN_HEIGHT);
    }

    public void update(float deltaTime){
        stateTime += deltaTime;
    }

    public float getStateTime(){
        return  stateTime;
    }
}
