package com.arkanoid.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion texture;

    private Sound release;
    private Music bounce;
    private Music bounce_golden;

//    static final float startX = OFFSET_X + BOARD_WIDTH / 2f, startY = OFFSET_Y + TILE_SIZE * 10f;
    static final float startX = OFFSET_X + TILE_SIZE, startY = OFFSET_Y + TILE_SIZE * 10f;
    private float masterVolume = 0.05f;
    private int idx = 0;

    /**
     * Create the ball
     *  @param world
     * @param release
     * @param bounce
     * @param bounce_golden
     */
    public BallActor(World world, TextureRegion texture, Sound release, Music bounce, Music bounce_golden) {
        this.world = world;
        this.texture = texture;
        this.release = release;
        this.bounce = bounce;
        this.bounce_golden = bounce_golden;

        // left border
        BodyDef def = new BodyDef();
        def.position.set(startX, startY);
        def.type = BodyDef.BodyType.DynamicBody;
        def.bullet = true;
        def.gravityScale = 0f;
        body = world.createBody(def);
        CircleShape box = new CircleShape();
        box.setRadius(TILE_SIZE / 3f);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        fixture.setDensity(1f);
        fixture.setRestitution(1f);
        fixture.setFriction(1f);
        box.dispose();

        setSize(TILE_SIZE /1.5f * PPM, TILE_SIZE / 1.5f * PPM);
        // Graph position
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
    }

    boolean start = false;
    public void start(){
        body.setTransform(startX, startY, body.getAngle());
        body.setLinearVelocity(new Vector2(1, 1));
        refreshSpeed();
        start = true;
    }

    /**
     * The collisions slow the ball, thus every collision must be call this method
     */
    public void refreshSpeed(){
        float LIMIT_ANGLE = 45;
        float speed = 80f;
        Vector2 v = body.getLinearVelocity();
        Vector2 normal = v.nor();

        // Limits angle. Prevents horizontal bounces.
        float angle = normal.angle();
        if(angle < LIMIT_ANGLE){
            normal.setAngle(LIMIT_ANGLE);
        } else if(angle <= 180 && angle > 180 - LIMIT_ANGLE){
            normal.setAngle(180 - LIMIT_ANGLE);
        } else if (angle > 180f && angle <= 180f + LIMIT_ANGLE){
            normal.setAngle(180f + LIMIT_ANGLE);
        } else if (angle > 360f - LIMIT_ANGLE) {
            normal.setAngle(360f - LIMIT_ANGLE);
        }
        body.setLinearVelocity(normal.scl(speed));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Ball impact
     */
    public void hit(Object impact) {
        refreshSpeed();
        Gdx.app.log("Arkanoid:BallActor", "impact" + idx++);
        if (impact.getClass() == BlockActor.class) {
            BlockActor ba = (BlockActor) impact;
            if (ba.getHardness() > 0) {
                bounce.stop();
                release.stop();
                bounce_golden.setVolume(masterVolume);
                bounce_golden.play();
            } else {
                if (!bounce.isPlaying()) {
                    bounce_golden.stop();
                    release.stop();
                    bounce.setVolume(masterVolume);
                    bounce.play();
                }
            }
        } else if (impact.getClass() == PlayerActor.class) {
            bounce.stop();
            bounce_golden.stop();
            release.play(masterVolume);
            Gdx.app.log("Arkanoid:BallActor", "play release");
        }
    }
}
