package com.jerry.framework.gl;

import android.util.Log;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class FPSCounter {

    long startTime = System.nanoTime();
    int frames = 0;

    public void logFrame(){
        frames++;
        if(System.nanoTime() - startTime >= 1000000000){
            Log.e("OpenGLApp", "fps: " + frames);
            frames =0;
            startTime = System.nanoTime();
        }
    }


}
