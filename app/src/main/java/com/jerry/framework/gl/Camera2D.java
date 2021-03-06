package com.jerry.framework.gl;

import com.jerry.framework.impl.GLGraphics;
import com.jerry.framework.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 10. 12. 2014.
 */
public class Camera2D {
    public final Vector2 position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    final GLGraphics glGraphics;

    public Camera2D(GLGraphics glGraphics, float frustumWidth, float frustumHeight){
        this.glGraphics = glGraphics;
        this.frustumHeight = frustumHeight;
        this.frustumWidth = frustumWidth;
        this.position = new Vector2(frustumWidth/2, frustumHeight/2);
        this.zoom = 1.0f;
    }

    public void setViewportAndMatrices(){
        GL10 gl = glGraphics.getGL();
        //set the viewport to span the whole screen
        gl.glViewport(0,0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(position.x - frustumWidth * zoom / 2,
                position.x + frustumWidth * zoom / 2,
                position.y - frustumHeight * zoom / 2,
                position.y + frustumHeight * zoom / 2,
                1, -1);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    //transform touch coordinates vector to world space

    public void touchToWorld(Vector2 touch){
        touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float) glGraphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }

}
