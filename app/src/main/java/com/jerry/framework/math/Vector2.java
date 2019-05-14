package com.jerry.framework.math;


import android.util.FloatMath;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class Vector2 {
    public static float TO_RADIANS = (1/180f)*(float)Math.PI;
    public static float TO_DEGREES = (1/(float)Math.PI)* 180;
    public float x,y;

    public Vector2(){
    }

    public Vector2(float x, float y){
        this.x=x;
        this.y=y;
    }

    public Vector2(Vector2 vector){
        this.x = vector.x;
        this.y = vector.y;
    }

    //copy a vector
    public Vector2 cpy(){
        return new Vector2(x,y);
    }

    public Vector2 set(float x, float y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
        return this;
    }

    public Vector2 add(float x, float y){
        this.x +=x;
        this.y +=y;
        return this;
    }

    public Vector2 add(Vector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public Vector2 sub(float x, float y){
        this.x -=x;
        this.y -=y;
        return this;
    }

    public Vector2 sub(Vector2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    //multiply vectro with scalar
    public Vector2 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    public Vector2 mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    //length of vector
    public float len(){
        return FloatMath.sqrt(x*x + y*y);
    }

    //normalize vector(its' length = 1)
    public Vector2 nom(){
        float len = len();
        if(len !=0){
            this.x /=len;
            this.y /=len;
        }
        return this;
    }

    //angle of vector
    public float angle(){
        float angle = (float) Math.atan2(y,x)*TO_DEGREES;
        if(angle <0){
            angle +=360;
        }
        return angle;
    }

    //rotate vector
    public Vector2 rotate(float angle){
        float rad = angle * TO_RADIANS;
        float cos = FloatMath.cos(rad);
        float sin = FloatMath.sin(rad);

        float newX = this.x * cos - this.y*sin;
        float newY = this.x*sin + this.y*cos;

        this.x = newX;
        this.y= newY;

        return this;
    }

    //distance between this and other vector
    public float dist(Vector2 other){
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return FloatMath.sqrt(distX * distX + distY * distY);
    }

    public float dist(float x, float y) {
        float distX = this.x - x;
        float distY = this.y - y;
        return FloatMath.sqrt(distX * distX + distY * distY);
    }

    public float distSquared(Vector2 other){
        /*
        when we check for circle collision, calculating sqrt root is computationally expensive,
        so we rather multiply, condition changes from
        sqrt (dist.x × dist.x + dist.y × dist.y) <= radius1 + radius2
        to
        dist.x × dist.x + dist.y × dist.y <= (radius1 + radius2) × (radius1 + radius2)
         */
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return distX * distX - distY * distY;
    }

    public float distSquared(float x , float y){
        float distX = this.x - x;
        float distY = this.y - y;
        return distX * distX - distY * distY;
    }
}
