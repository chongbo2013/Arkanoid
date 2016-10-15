package com.arkanoid.Screens;

import com.arkanoid.Actors.BallActor;
import com.arkanoid.Actors.BlockActor;
import com.arkanoid.Actors.BoardActor;
import com.arkanoid.Actors.Entity;
import com.arkanoid.Actors.ManagerEntity;
import com.arkanoid.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.arkanoid.Actors.ActorFactory;
import com.arkanoid.Actors.PlayerActor;
import com.arkanoid.ArkanoidGame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.arkanoid.Screens.CuasiConstantes.BOARD_WIDTH;
import static com.arkanoid.Screens.CuasiConstantes.MPP;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_X;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_Y;
import static com.arkanoid.Screens.CuasiConstantes.PPM;
import static com.arkanoid.Screens.CuasiConstantes.SCREEN_HEIGHT;
import static com.arkanoid.Screens.CuasiConstantes.SCREEN_WIDTH;

/**
 * Created by Sebastian Oscar Lopez on 03/07/2016.
 */
public class GameScreen extends BaseScreen implements InputProcessor, BlockActor.BlockListener {


    /** Stage instance for Scene2D rendering. */
    private Camera camera;
    private Viewport viewport;
    private Stage stage;

    /** World instance for Box2D engine. */
    private World world;
    private final Box2DDebugRenderer b2dr;
    private Camera physicsDebugCam;

  //  private final Vector3 position;
    private ActorFactory factory;
    private ManagerEntity managerEntity;

    private PlayerActor playerActor;

    private BallActor ball;

    /**
     * Time game
     */
    private float elapsedTime = 0;

    /**
     * Total blocks in level
     */
    private int totalBlocks;
    private int stepsNextLevel = 3;

    /**
     * Create the screen. Since this constructor cannot be invoked before libGDX is fully started,
     * it is safe to do critical code here such as loading assets and setting up the stage.
     * @param game
     */
    public GameScreen(ArkanoidGame game) {
        super(game);

        // Scene' camera
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        // The game's stage
        stage = new Stage(viewport);

        // Create a new Box2D world for managing things.
        world = new World(new Vector2(0, -10), true);
//        b2dr = new Box2DDebugRenderer(true, true, true, true, true, true);

        // Debug Box2D
        b2dr = new Box2DDebugRenderer();
        // Debug's camera
        physicsDebugCam = new OrthographicCamera(SCREEN_WIDTH / PPM, SCREEN_HEIGHT / PPM);
        //physicsDebugCam.unproject(camera.position);
        Vector3 position = camera.position.cpy();
        physicsDebugCam.position.set(position.scl(MPP));

        world.setContactListener(new GameContactListener());

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Stage is about to start
     */
    @Override
    public void show() {
        // the cameras start
        physicsDebugCam.update();
        camera.update();
        
        // The things's factory
        factory = new ActorFactory(game.getManager());

        // ManagerEntity. Very useful for blocks delete
        managerEntity = new ManagerEntity();

        // Add the board
        BoardActor board = factory.createBoard(world);
        board.setLevel(1);
        stage.addActor(board);

        // Add player (the Arkanoid)
        playerActor = factory.createPlayer(world, new Vector2(BOARD_WIDTH / 2 + OFFSET_X, OFFSET_Y + 5f));
        stage.addActor(playerActor);

        // Add the ball
        ball = factory.createBall(world);
        stage.addActor(ball);
    }


    boolean start = false;

    int idxLevel = 0;
    private void generateLevel() {
        start = false;

        ball.start();

        // Add the blocks
        List<BlockActor> blocks = levels.get(idxLevel - 1).build(factory, world);
        totalBlocks = blocks.size();
        managerEntity.add((List)blocks);
        for (BlockActor block :
                blocks) {
            block.setBlockListener(this);
            stage.addActor(block);
        }
    }

    /**
     * This method is executed whenever the game requires this screen to be rendered. This will
     * display things on the screen. This method is also used to update the game.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(delta, 6, 2);

        elapsedTime += Gdx.graphics.getDeltaTime();
        if(!start && elapsedTime >= 1f){
            start = true;
            ball.start();
        }

        stage.act();
        stage.draw();

        b2dr.render(world, physicsDebugCam.combined);
        camera.update();

        ball.refreshSpeed();

        if(stepsNextLevel > 0)
        {
            stepsNextLevel--;
            if(stepsNextLevel == 0) {
                idxLevel++;
                generateLevel();
            }
        }

        managerEntity.update();
    }

    private ArrayList<Level> levels;
    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }

    @Override
    public void onDeadBlock(BlockActor blockActor) {
        totalBlocks--;
        if(totalBlocks == 0) {
            stepsNextLevel = 3;
        }
    }

    /**
     * This method will be executed when this screen is no more the active screen.
     * I use this method to destroy all the things that have been used in the stage.
     */
    @Override
    public void hide() {
        // Clear the stage. This will remove ALL actors from the stage and it is faster than
        // removing every single actor one by one. This is not shown in the video but it is
        // an improvement.
        stage.clear();

        playerActor.detach();
    }

    /**
     * This method is executed when the screen can be safely disposed.
     * I use this method to dispose things that have to be manually disposed.
     */
    @Override
    public void dispose() {
        // Dispose the stage to remove the Batch references in the graphics card.
        stage.dispose();

        // Dispose the world to remove the Box2D native data (C++ backend, invoked by Java).
        world.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.LEFT:
                playerActor.setIsLeft(true);
                break;
            case Input.Keys.RIGHT:
                playerActor.setIsRight(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                playerActor.setIsLeft(false);
                break;
            case Input.Keys.RIGHT:
                playerActor.setIsRight(false);
                break;
            case Input.Keys.ENTER:
                ball.start();
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(screenX > SCREEN_WIDTH / 2f) {
            playerActor.setIsLeft(false);
            playerActor.setIsRight(true);
        }else{
            playerActor.setIsLeft(true);
            playerActor.setIsRight(false);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        playerActor.setIsLeft(false);
        playerActor.setIsRight(false);

        if(ball.getY() < 0)
        {
            ball.start();
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
