package com.arkanoid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.arkanoid.Screens.BaseScreen;
import com.arkanoid.Screens.GameScreen;
import com.arkanoid.Screens.LoadingScreen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArkanoidGame extends Game {
/*
    SpriteBatch batch;
    Texture img;
    */
    private AssetManager manager;

    public BaseScreen loadingScreen;
	private GameScreen gameScreen;

	@Override
    public void create () {
        manager = new AssetManager();

        manager.load("player.png", Texture.class);
        manager.load("sprites_player.txt", TextureAtlas.class);
        manager.load("sprites_board.txt", TextureAtlas.class);

        // Enter the loading screen to load the assets.
        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public AssetManager getManager() {
        return manager;
    }

    public void finishLoading() {
        gameScreen = new GameScreen(this);

        FileHandle file = Gdx.files.internal("levels.json");
        String bufferString = file.readString();
        Type listType = new TypeToken<ArrayList<Level>>(){}.getType();
        gameScreen.setLevels((ArrayList<Level>)new GsonBuilder().create().fromJson(bufferString, listType));

        setScreen(gameScreen);
    }

    @Override
    public void render () {
        super.render();
    }
/*
    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
    */
}
