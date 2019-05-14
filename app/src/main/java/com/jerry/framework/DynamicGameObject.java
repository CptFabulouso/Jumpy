package com.jerry.framework;

import com.jerry.framework.math.Vector2;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class DynamicGameObject extends GameObject{
    public final Vector2 velocity, accel;

    public DynamicGameObject(float x, float y, float width, float height){
        super(x,y,width,height);
        velocity = new Vector2();
        accel = new Vector2();
    }

}
