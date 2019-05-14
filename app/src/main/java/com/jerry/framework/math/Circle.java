package com.jerry.framework.math;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class Circle {
    public final Vector2 center = new Vector2();
    public float radius;

    public Circle(float x, float y, float radius){
        this.center.set(x,y);
        this.radius = radius;
    }


}
