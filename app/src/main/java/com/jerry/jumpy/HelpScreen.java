package com.jerry.jumpy;

import com.jerry.framework.Game;
import com.jerry.framework.Input;
import com.jerry.framework.gl.Camera2D;
import com.jerry.framework.gl.SpriteBatcher;
import com.jerry.framework.gl.Texture;
import com.jerry.framework.gl.TextureRegion;
import com.jerry.framework.impl.GLScreen;
import com.jerry.framework.math.OverlapTester;
import com.jerry.framework.math.Rectangle;
import com.jerry.framework.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 13. 12. 2014.
 */
public class HelpScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle nextBounds;
    Vector2 touchPoint;
    Texture helpImage;
    TextureRegion helpRegion;



    public HelpScreen(Game game) {
        super(game);

        guiCam = new Camera2D(glGraphics, 320, 480);
        nextBounds = new Rectangle(320-64,0,64,64);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics,1);

    }


    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if(event.type == Input.TouchEvent.TOUCH_UP){
                if(OverlapTester.pointInRectangle(nextBounds,touchPoint)){
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new MainMenuScreen(glGame));
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
        batcher.beginBatch(helpImage);

        batcher.drawSprite(160, 240, 320, 480, helpRegion);
        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(320 - 32, 32, -64, 64, Assets.arrow);
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
        helpImage = new Texture(glGame,"help1.png");
        helpRegion = new TextureRegion(helpImage, 0,0,320,480);
    }

    @Override
    public void dispose() {
        helpImage.dispose();
    }
}
