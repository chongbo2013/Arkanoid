package com.arkanoid.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.arkanoid.Screens.CuasiConstantes.TILE_SIZE;
import static com.arkanoid.Screens.CuasiConstantes.BOARD_WIDTH;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_X;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_Y;
import static com.arkanoid.Screens.CuasiConstantes.PPM;

/**
 * Created by Sebas on 09/10/2016.
 */

public class BallActor extends Actor {

    private Texture board;
    private World world;
    private Body body;
    private Fixture fixture;

//    static final float startX = OFFSET_X + BOARD_WIDTH / 2f, startY = OFFSET_Y + TILE_SIZE * 10f;
    static final float startX = OFFSET_X + TILE_SIZE, startY = OFFSET_Y + TILE_SIZE * 10f;

    /**
     * Create the ball
     *
     * @param world
     */
    public BallActor(World world) {
        this.world = world;

        // left border
        BodyDef def = new BodyDef();
        def.position.set(startX, startY);
        def.type = BodyDef.BodyType.DynamicBody;
        def.bullet = true;
        def.gravityScale = 0f;
        body = world.createBody(def);
        CircleShape box = new CircleShape();
        box.setRadius(TILE_SIZE / 2f);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        fixture.setDensity(1f);
        fixture.setRestitution(1f);
        fixture.setFriction(1f);
        box.dispose();

        setSize(TILE_SIZE * PPM, TILE_SIZE * PPM);
        // Graph position
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
    }

    boolean start = false;
    public void start(){
        body.setTransform(startX, startY, body.getAngle());
//        body.setLinearVelocity(new Vector2(BOARD_WIDTH - 1, BOARD_WIDTH - 1));
        body.setLinearVelocity(new Vector2(1, 1));
        refreshSpeed();
        start = true;

    }

    /**
     * The collisions slow the ball, thus every collision must be call this method
     */
    public void refreshSpeed(){
        float speed = 100f;
        Vector2 v = body.getLinearVelocity();
        Vector2 normal = v.nor();
        body.setLinearVelocity(normal.scl(speed));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
//        body.setAngularVelocity(7f);
//        body.setLinearVelocity(new Vector2( 0f, -5f));
        // Render both textures.
//        batch.draw(floor, getX()/2, getY()/2, getWidth(), getHeight());
//        batch.draw(overfloor, getX(), getY() + 0.9f * getHeight(), getWidth(), 0.1f * getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
