package com.jerry.framework.math;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class Rectangle {
    public final Vector2 lowerLeft = new Vector2();
    public float width, height;

    public Rectangle(float x, float y, float width, float height){
        lowerLeft.set(x,y);
        this.width = width;
        this.height = height;
    }

    public void set(float x, float y, float width, float height){

    }



}
