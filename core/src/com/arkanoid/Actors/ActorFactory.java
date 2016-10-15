/*
 * This file is part of Jump Don't Die
 * Copyright (C) 2015 Dani Rodríguez <danirod@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.arkanoid.Actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import static com.arkanoid.Screens.CuasiConstantes.BOARD_HEIGHT;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_X;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_Y;
import static com.arkanoid.Screens.CuasiConstantes.TILE_SIZE;

/**
 * This class creates entities using Factory Methods.
 */
public class ActorFactory {

    private AssetManager manager;

    /**
     * Create a new entity factory using the provided asset manager.
     * @param manager   the asset manager used to generate things.
     */
    public ActorFactory(AssetManager manager) {
        this.manager = manager;
    }

    /**
     * Create a player using the default texture.
     * @param world     world where the player will have to live in.
     * @param position  initial position ofr the player in the world (meters,meters).
     * @return          a player.
     */
    public PlayerActor createPlayer(World world, Vector2 position) {
        //Texture playerTexture = manager.get("player.png");
        TextureAtlas atlas = manager.get("sprites_player.txt", TextureAtlas.class);
        HashMap<PlayerActor.PlayerAnimations, Animation> animations = new HashMap<PlayerActor.PlayerAnimations, Animation>();

        Array<TextureAtlas.AtlasRegion> playerRegions = atlas.findRegions("red_medium");
        Array<TextureAtlas.AtlasRegion> playerRegionsReverse = atlas.findRegions("red_medium");
        playerRegionsReverse.reverse();
        playerRegions.addAll(playerRegionsReverse);
        animations.put(PlayerActor.PlayerAnimations.RED_MEDIUM, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("red_large");
        playerRegionsReverse = atlas.findRegions("red_large");
        playerRegionsReverse.reverse();
        playerRegions.addAll(playerRegionsReverse);
        animations.put(PlayerActor.PlayerAnimations.RED_LARGE, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("red_medium_create");
        animations.put(PlayerActor.PlayerAnimations.RED_MEDIUM_CREATE, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("red_medium_destoy");
        animations.put(PlayerActor.PlayerAnimations.RED_MEDIUM_DESTROY, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("blue_medium");
        playerRegionsReverse = atlas.findRegions("blue_medium");
        playerRegionsReverse.reverse();
        playerRegions.addAll(playerRegionsReverse);
        animations.put(PlayerActor.PlayerAnimations.BLUE_MEDIUM, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("blue_large");
        playerRegionsReverse = atlas.findRegions("blue_large");
        playerRegionsReverse.reverse();
        playerRegions.addAll(playerRegionsReverse);
        animations.put(PlayerActor.PlayerAnimations.BLUE_LARGE, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("blue_medium_create");
        animations.put(PlayerActor.PlayerAnimations.BLUE_MEDIUM_CREATE, new Animation(1f/(float)playerRegions.size, playerRegions));

        playerRegions = atlas.findRegions("blue_medium_destoy");
        animations.put(PlayerActor.PlayerAnimations.BLUE_MEDIUM_DESTROY, new Animation(1f/(float)playerRegions.size, playerRegions));

        return new PlayerActor(world, animations, position);
    }

    /**
     * Create wall using the default texture set.
     * @param world     world where the wall will live in.
     * @return          a board.
     */
    public BoardActor createBoard(World world) {
        TextureAtlas atlas = manager.get("sprites_board.txt", TextureAtlas.class);
//        HashMap<BoardActor.BoardAnimations, Animation> animations = new HashMap<PlayerActor.PlayerAnimations, Animation>();
        HashMap<BoardActor.BoardTextures, TextureRegion> textures = new HashMap<BoardActor.BoardTextures, TextureRegion>();
        textures.put(BoardActor.BoardTextures.CORNER_LEFT, atlas.findRegion("corner_left"));
        textures.put(BoardActor.BoardTextures.CORNER_RIGHT, atlas.findRegion("corner_right"));
        textures.put(BoardActor.BoardTextures.TUBE, atlas.findRegion("tube"));
        textures.put(BoardActor.BoardTextures.DOOR, atlas.findRegion("door"));
        textures.put(BoardActor.BoardTextures.DOOR_TOP, atlas.findRegion("door_top"));

        Array<TextureRegion> grounds = new Array<TextureRegion>(4);
        grounds.add(atlas.findRegion("ground_1_light"));
        grounds.add(atlas.findRegion("ground_2_light"));
        grounds.add(atlas.findRegion("ground_3_light"));
        grounds.add(atlas.findRegion("ground_4_light"));
        return new BoardActor(world, textures, grounds);
    }

    public BallActor createBall(World world) {

        return new BallActor(world);
    }

    public BlockActor createBlock(World world, Vector2 position, String color) {

        TextureAtlas atlas = manager.get("sprites_board.txt", TextureAtlas.class);
        TextureRegion texture = atlas.findRegion("block_" + color);

        int hardness = 1;
        if(color.equals("plate"))
        {
            hardness = 2;
        }else if(color.equals("golden"))
        {
            hardness = 99;
        }

        return new BlockActor(world, position, texture, hardness);
    }
}
