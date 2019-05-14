package com.jerry.framework;

import com.jerry.framework.math.Rectangle;
import com.jerry.framework.math.Vector2;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;

    public GameObject(float x, float y, float width, float height ){
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x-width/2,y-height/2,width,height);
    }

}
