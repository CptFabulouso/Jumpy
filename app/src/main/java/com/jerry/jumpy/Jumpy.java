package com.jerry.jumpy;

import com.jerry.framework.Screen;
import com.jerry.framework.impl.GLGame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 13. 12. 2014.
 */
public class Jumpy extends GLGame{
    boolean firstTimeCreate = true;

    @Override
    public Screen getStartScreen() {
        return new MainMenuScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Settings.soundEnabled){
            Assets.music.pause();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        getCurrentScreen().backButton();
    }
}
