package com.jerry.jumpy;

import com.jerry.framework.DynamicGameObject;

/**
 * Created by Pavel on 14. 12. 2014.
 */
public class Squirrel extends DynamicGameObject {
    public static final float SQUIRREL_WIDTH = 1;
    public static final float SQUIRREL_HEIGHT = 0.6f;
    public static final float SQUIRREL_VELOCITY = 3f;
    float stateTime = 0;


    public Squirrel(float x, float y) {
        super(x, y, SQUIRREL_WIDTH, SQUIRREL_HEIGHT);
        velocity.set(SQUIRREL_VELOCITY,0);
    }

    public void update(float deltaTime){
        //update position
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        //update bounds position - set them as position and substract to fit lower left corner
        bounds.lowerLeft.set(position).sub(SQUIRREL_WIDTH/2, SQUIRREL_HEIGHT/2);

        if(position.x < SQUIRREL_WIDTH/2){ //squirrel hits left side of screen
            position.x = SQUIRREL_WIDTH/2;
            velocity.x = SQUIRREL_VELOCITY;
        }
        //squirrel hits right side of scrren
        if(position.x > World.WORLD_WIDTH - SQUIRREL_WIDTH/2){
            position.x =World.WORLD_WIDTH - SQUIRREL_WIDTH/2;
            velocity.x = -SQUIRREL_VELOCITY;
        }
        stateTime += deltaTime;
    }

}
