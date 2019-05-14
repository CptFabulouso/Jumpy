package com.jerry.jumpy;

import com.jerry.framework.math.OverlapTester;
import com.jerry.framework.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pavel on 14. 12. 2014.
 */
public class World {

    /*
    We could just add invocations of
    Assets.playSound() to the respective simulation classes, but that’s not very clean. Instead, we’ll
    let a user of the World class register a WorldListener, which will be called when Bob jumps from
    a platform, jumps from a spring, gets hit by a squirrel, or collects a coin. We will later register a
    listener that plays back the proper sound effects for each of those events, keeping the simulation
    classes clean from any direct dependencies on rendering and audio playback.
     */
    public interface WorldListener{
        public void jump();
        public void highJump();
        public void hit();
        public void coin();
    }

    /*
    Our view frustum will show a region of 10×15 meters of our world.
    Our world will span 20 view frustums or screens vertically
     */
    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0,-12);

    public final Bob bob;
    public final List<Platform> platforms;
    public final List<Spring> springs;
    public final List<Squirrel> squirrels;
    public final List<Coin> coins;
    public Castle castle;
    public final WorldListener listener;
    public final Random rand;

    public float heightSoFar;
    public int score;
    public int state;

    public World(WorldListener listener){
        this.bob = new Bob(5,1);
        this.platforms = new ArrayList<Platform>();
        this.springs = new ArrayList<Spring>();
        this.squirrels = new ArrayList<Squirrel>();
        this.coins = new ArrayList<Coin>();
        this.listener = listener;
        rand = new Random();
        generateLevel();

        this.heightSoFar =0;
        this.score = 0;
        this.state =WORLD_STATE_RUNNING;
    }

    private void generateLevel(){
        float y = Platform.PLATFORM_HEIGHT/2;
        float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * bob.BOB_JUMP_VELOCITY /(-2*gravity.y);
        while (y < WORLD_HEIGHT - WORLD_WIDTH/2){
            int type = rand.nextFloat() > 0.8?Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
            float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH/2;

            Platform platform = new Platform(x,y, type);
            platforms.add(platform);

            if(rand.nextFloat() > 0.9f && type != Platform.PLATFORM_TYPE_MOVING){
                Spring spring = new Spring(platform.position.x,
                        platform.position.y + Platform.PLATFORM_HEIGHT/2 + Spring.SPRING_HEIGHT/2-0.01f);
                springs.add(spring);
            }

            if(y > WORLD_HEIGHT/3 && rand.nextFloat() >0.8f){
                Squirrel squirrel = new Squirrel(platform.position.x + rand.nextFloat(),
                        platform.position.y + Squirrel.SQUIRREL_HEIGHT + rand.nextFloat()*2);
                squirrels.add(squirrel);
            }

            if(rand.nextFloat() > 0.6f){
                Coin coin = new Coin(platform.position.x + rand.nextFloat(),
                        platform.position.y + Coin.COIN_HEIGHT + rand.nextFloat()/3);
                coins.add(coin);
            }

            /*
            Increase y by the maximum normal jump height of Bob, decrease it a tiny bit
            randomly—but only so far that it doesn’t fall below the last y value
             */
            y += (maxJumpHeight - 0.5f);
            y -= rand.nextFloat() * (maxJumpHeight/3);
        }

        castle = new Castle(WORLD_WIDTH / 2, y);

    }

    public void update(float deltaTime, float accelX){
        updateBob(deltaTime, accelX);
        updatePlatforms(deltaTime);
        updateSquirrels(deltaTime);
        updateCoins(deltaTime);
        if(bob.state != Bob.BOB_STATE_HIT){
            checkCollisions();
        }
        checkGameOver();
    }

    private void updateBob(float deltaTime, float accelX){
        //check if bob is hitting bottom of the world
        if(bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f)
            bob.hitPlatform();
        if(bob.state != Bob.BOB_STATE_HIT){
            bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
        }
        bob.update(deltaTime);
        heightSoFar = Math.max(bob.position.y, heightSoFar);
    }

    private void updatePlatforms(float deltaTime) {
        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            platform.update(deltaTime);
            if(platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime >Platform.PLATFORM_PULVERIZE_TIME){
                platforms.remove(platform);
                len = platforms.size();
            }
        }
    }

    private void updateSquirrels(float deltaTime) {
        int len = squirrels.size();
        for (int i = 0; i < len; i++) {
            Squirrel squirrel = squirrels.get(i);
            squirrel.update(deltaTime);
        }
    }

    private void updateCoins(float deltaTime) {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            coin.update(deltaTime);
        }
    }

    private void checkCollisions() {
        checkPlatformCollisions();
        checkSquirrelCollisions();
        checkItemCollisions();
        checkCastleCollisions();
    }

    private void checkPlatformCollisions() {
        if(bob.velocity.y > 0)
            return;

        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            if (bob.position.y > platform.position.y) {
                if(OverlapTester.overlapRectangles(bob.bounds, platform.bounds)){
                    bob.hitPlatform();
                    listener.jump();
                    //check if there is a spring, if yes, don't pulverize
                    if(rand.nextFloat() > 0.5f){
                        int springLen = springs.size();
                        //search through springs
                        for (int j = 0; j < springLen; j++) {
                            Spring spring = springs.get(j);
                            //in case of found overlap between spring and platfrom, exit for loop
                            if(OverlapTester.overlapRectangles(spring.bounds, platform.bounds)){
                                break;
                            }
                            //if no collision found, pulverize
                            if(j == springLen-1){
                                platform.pulverize();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private void checkSquirrelCollisions() {
        int len = squirrels.size();
        for (int i = 0; i < len; i++) {
            Squirrel squirrel = squirrels.get(i);
            if(OverlapTester.overlapRectangles(bob.bounds, squirrel.bounds)){
                bob.hitSquirrel();
                listener.hit();
            }
        }
    }

    private void checkItemCollisions() {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            if(OverlapTester.overlapRectangles(bob.bounds, coin.bounds)){
                coins.remove(coin);
                len = coins.size();
                listener.coin();
                score += Coin.COIN_SCORE;
            }
        }

        if(bob.velocity.y > 0)
            return;

        len = springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = springs.get(i);
            if(OverlapTester.overlapRectangles(bob.bounds, spring.bounds)){
                bob.hitString();
                listener.highJump();
            }
        }
    }

    private void checkCastleCollisions() {
        if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds)) {
            state = WORLD_STATE_NEXT_LEVEL;
        }
    }

    private void checkGameOver() {
        /*
        our view frustum will have a height of 15 meters, camera is centered onto bob
         */
        if (heightSoFar - 7.5f > bob.position.y) {
            state = WORLD_STATE_GAME_OVER;
        }
    }

}
