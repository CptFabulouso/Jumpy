package com.jerry.framework.impl;

import com.jerry.framework.Game;
import com.jerry.framework.Screen;

/**
 * Just a little helper class
 */
public abstract class GLScreen extends Screen{
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;

    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = glGame.getGLGraphics();
    }

}
