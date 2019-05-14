package com.jerry.jumpy;

import com.jerry.framework.Game;
import com.jerry.framework.Input.TouchEvent;
import com.jerry.framework.gl.Camera2D;
import com.jerry.framework.gl.SpriteBatcher;
import com.jerry.framework.impl.GLScreen;
import com.jerry.framework.math.OverlapTester;
import com.jerry.framework.math.Rectangle;
import com.jerry.framework.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 14. 12. 2014.
 */
public class HighscoreScreen extends GLScreen{
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Vector2 touchPoint;
    String[] highscores;
    float xOffset = 0;

    public HighscoreScreen(Game game) {
        super(game);

        guiCam = new Camera2D(glGraphics,320,480);
        backBounds = new Rectangle(0,0,64,64);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics,100);
        highscores = new String[5];
        for(int i = 0; i < 5;i++){
            highscores[i] = (i+1) + ". " + Settings.highscores[i];
            xOffset = Math.max(highscores[i].length() * Assets.font.glyphWidth, xOffset);
        }
        xOffset = 160-xOffset/2;
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if (event.type == TouchEvent.TOUCH_UP) {
                if (OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.background);
        batcher.drawSprite(160,240,320,480,Assets.backgroundRegion);
        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(160,360,300,33,Assets.highScoresRegion);

        float y = 240;
        for(int i = 4; i>=0; i--){
            Assets.font.drawText(batcher, highscores[i], xOffset, y);
            y+= Assets.font.glyphHeight;
        }

        batcher.drawSprite(32,32,64,64, Assets.arrow);
        batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {

    }

    @Override
    public void backButton() {
        game.setScreen(new MainMenuScreen(glGame));
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
