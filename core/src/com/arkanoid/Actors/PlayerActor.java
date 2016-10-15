package com.arkanoid.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

import static com.arkanoid.Screens.CuasiConstantes.TILE_SIZE;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_Y;
import static com.arkanoid.Screens.CuasiConstantes.PPM;
/**
 * Created by Sebastian Oscar Lopez on 03/07/2016.
 */
public class PlayerActor extends Actor {

    Vector2 left;
    Vector2 right;
    Vector2 direction = Vector2.Zero;

    public enum PlayerAnimations{
        RED_MEDIUM,
        RED_LARGE,
        RED_MEDIUM_CREATE,
        RED_MEDIUM_DESTROY,
        BLUE_MEDIUM,
        BLUE_LARGE,
        BLUE_MEDIUM_CREATE,
        BLUE_MEDIUM_DESTROY
    }

    PlayerAnimations PlayerAnimationStatus = PlayerAnimations.RED_MEDIUM_CREATE;

    private HashMap<PlayerAnimations, Animation> animations;

    private World world;
    private Body body;
    private Fixture fixture;

    /**
     * Is the player alive? If he touches a spike, is not alive. The player will only move and
     * jump if it's alive. Otherwise it is said that the user has lost and the game is over.
     */
    private boolean alive = true;

    /**
     * Left is pressed?
     */
    private boolean isLeft = false;
    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
        directionUpdate();
    }

    /**
     * Right is pressed?
     */
    private boolean isRight= false;

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
        directionUpdate();
    }

    /**
     * Direction update
     */
    private void directionUpdate(){
        direction = Vector2.Zero;
        if(isLeft && !isRight)
        {
            direction = left;
        }

        if(isRight && !isLeft)
        {
            direction = right;
        }
        body.setLinearVelocity(direction);
    }

    /**
     * Speed left/right
     */
    public void setSpeed(float speed) {
        left = new Vector2(speed * -1, 0f);
        right = new Vector2(speed, 0f);
    }

    /**
     * Time for animation
     */
    private float elapsedTime = 0;

    public PlayerActor(World world, HashMap<PlayerAnimations, Animation> animations, Vector2 position) {
        this.world = world;
        this.animations = animations;

        // Create the player body.
        BodyDef def = new BodyDef();                // (1) Create the body definition.
        def.position.set(position);                 // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.
        def.gravityScale = 0;
        body = world.createBody(def);               // (4) Now create the body.

        // Give it some shape.
        PolygonShape box = new PolygonShape();      // (1) Create the shape.
        box.setAsBox(TILE_SIZE * 3, TILE_SIZE);                 // (2) 1x0.25 meter box.
        fixture = body.createFixture(box, 3);       // (3) Create the fixture.
        fixture.setUserData("player");              // (4) Set the user data.
        fixture.setDensity(1f);
        fixture.setRestitution(0f);
        fixture.setFriction(1f);
        box.dispose();                              // (5) Destroy the shape.

        setSpeed(TILE_SIZE * 20f);

        // Arkanoid size is 1 x 0.25 meters
        setSize(TILE_SIZE * 6 * PPM, TILE_SIZE * 2 * PPM);

        // Graph position
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
    }

    Vector2 position = null;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Always update the position of the actor when you are going to draw it, so that the
        // position of the actor on the screen is as accurate as possible to the current position
        // of the Box2D body.
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);

        batch.draw(animations.get(PlayerAnimationStatus).getKeyFrame(elapsedTime += Gdx.graphics.getDeltaTime(), true), getX(), getY(), getWidth(), getHeight());

        if(PlayerAnimationStatus == PlayerAnimations.RED_MEDIUM_CREATE && elapsedTime >= 1f){
            PlayerAnimationStatus = PlayerAnimations.RED_MEDIUM;
            elapsedTime = 0;
        }
    }

    @Override
    public void act(float delta) {

        position = body.getPosition();
        position.y = OFFSET_Y + TILE_SIZE * 2f;
        body.setTransform(position, 0f);
        if (alive) {
        }

    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    // Getter and setter festival below here.

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
