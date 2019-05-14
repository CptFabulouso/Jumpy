package com.jerry.jumpy;

import com.jerry.framework.DynamicGameObject;

/**
 * Created by Pavel on 14. 12. 2014.
 */
public class Platform extends DynamicGameObject {
    public static final float PLATFORM_WIDTH = 2;
    public static final float PLATFORM_HEIGHT = 0.5f;
    //platform can be static or moving
    public static final int PLATFORM_TYPE_STATIC = 0;
    public static final int PLATFORM_TYPE_MOVING = 1;
    //state of platform
    public static final int PLATFORM_STATE_NORMAL = 0;
    public static final int PLATFORM_STATE_PULVERIZING = 1;
    //time is connected with animation time declared in assets
    public static final float PLATFORM_PULVERIZE_TIME = 0.2f * 4;
    public static final float PLATFORM_VELOCITY = 2;

    int type;
    int state;
    float stateTime;

    public Platform(float x, float y, int type) {
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        this.type = type;
        this.state = PLATFORM_STATE_NORMAL;
        this.stateTime = 0;
        if(type == PLATFORM_TYPE_MOVING){
            velocity.x = PLATFORM_VELOCITY;
        }
    }

    public void update(float deltaTime){
        if(type == PLATFORM_TYPE_MOVING){
            position.add(velocity.x * deltaTime,0);
            bounds.lowerLeft.set(position).sub(PLATFORM_WIDTH/2, PLATFORM_HEIGHT/2);
            if(position.x < PLATFORM_WIDTH / 2) {
                velocity.x = -velocity.x;
                position.x = PLATFORM_WIDTH / 2;
            }
            if(position.x > World.WORLD_WIDTH - PLATFORM_WIDTH / 2) {
                velocity.x = -velocity.x;
                position.x = World.WORLD_WIDTH - PLATFORM_WIDTH / 2;
            }
        }
        stateTime += deltaTime;
    }

    public void pulverize() {
        state = PLATFORM_STATE_PULVERIZING;
        stateTime = 0;
        velocity.x = 0;
    }

}
