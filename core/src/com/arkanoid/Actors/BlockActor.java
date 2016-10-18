package com.arkanoid.Actors;

import com.arkanoid.Block;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.arkanoid.Screens.CuasiConstantes.PPM;
import static com.arkanoid.Screens.CuasiConstantes.TILE_SIZE;

/**
 * Created by Sebas on 10/10/2016.
 */

public class BlockActor extends Entity{

    private boolean startAnimation = false;
    private float elapsedTime = 0;

    public interface BlockListener{
        void onDeadBlock(BlockActor blockActor);
    }
    private BlockListener blockListener = null;
    public void setBlockListener(BlockListener blockListener){
        this.blockListener = blockListener;
    }

    private TextureRegion texture;

    private World world;
    private final Vector2 position;
    private Body body;
    private Fixture fixture;

    private Animation animation;
    private int hardness;
    public int getHardness() {
        return hardness;
    }

    /**
     * Create a single block
     *
     * @param world
     * @param animation
     */
    public BlockActor(World world, Vector2 position, TextureRegion texture, Animation animation, int hardness) {
        this.world = world;
        this.position = position;
        this.texture = texture;
        this.animation = animation;
        this.hardness = hardness;

        BodyDef def = new BodyDef();
        def.position.set(position.x, position.y);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);
        PolygonShape box = new PolygonShape();
        box.setAsBox(TILE_SIZE, TILE_SIZE / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);               // User data must be block type
        fixture.setDensity(0f);
        fixture.setRestitution(0f);
        fixture.setFriction(0f);
        box.dispose();

        // Graph size
        setSize(TILE_SIZE * 2f * PPM, TILE_SIZE * PPM);
        // Graph position
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
    }

    /**
     * Ball or Shoot impact in block
     */
    public void hit(Object impact){
        if(impact.getClass() == BallActor.class){
            if(hardness < 99) {
                hardness--;
            }
        }

        if(hardness == 0) {
            setAlive(false);

            if(blockListener != null) {
                blockListener.onDeadBlock(this);
            }
        } else {
            startAnimation = true;
        }
    }

    public void StartAnimation() {
        if(!startAnimation) {
            startAnimation = true;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(startAnimation) {
            batch.draw(animation.getKeyFrame(elapsedTime += Gdx.graphics.getDeltaTime(), false), getX(), getY(), getWidth(), getHeight());
            if(animation.isAnimationFinished(elapsedTime)) {
                elapsedTime = 0;
                startAnimation = false;
            }
        } else {
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public boolean remove() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
        return super.remove();
    }
}
