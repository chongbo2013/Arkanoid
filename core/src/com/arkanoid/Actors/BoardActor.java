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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import static com.arkanoid.Screens.CuasiConstantes.TILE_SIZE;
import static com.arkanoid.Screens.CuasiConstantes.BOARD_HEIGHT;
import static com.arkanoid.Screens.CuasiConstantes.BOARD_WIDTH;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_X;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_Y;
import static com.arkanoid.Screens.CuasiConstantes.PPM;

/**
 * This entity represents the floor. The player and the spikes are above the floor. You cannot go
 * below the floor. And if you hit the left border of a floor when you are supposed to jump,
 * you lose.
 */
public class BoardActor extends Actor {

    private Texture board;
    private World world;
    private HashMap<BoardTextures, TextureRegion> textures;
    private Array<TextureRegion> grounds;
    private Body body;
    private Fixture fixture;
    private TextureRegion ground;

    /**
     * Create a new level
     *
     * @param world
     * @param textures
     */
    public BoardActor(World world, HashMap<BoardTextures, TextureRegion> textures, Array<TextureRegion> grounds) {
        this.world = world;
        this.textures = textures;
        this.grounds = grounds;

        // left border
        BodyDef def = new BodyDef();
        def.position.set(OFFSET_X - TILE_SIZE / 2f, OFFSET_Y + BOARD_HEIGHT / 2f);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);
        PolygonShape box = new PolygonShape();
        box.setAsBox(TILE_SIZE / 2, BOARD_HEIGHT / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("board_left");
        fixture.setDensity(0f);
        fixture.setRestitution(0f);
        fixture.setFriction(0f);
        box.dispose();

        // right border
        def = new BodyDef();
        def.position.set(OFFSET_X + BOARD_WIDTH + TILE_SIZE / 2f, OFFSET_Y + BOARD_HEIGHT / 2f);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);
        box = new PolygonShape();
        box.setAsBox(TILE_SIZE / 2, BOARD_HEIGHT / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("board_right");
        fixture.setDensity(0f);
        fixture.setRestitution(0f);
        fixture.setFriction(0f);
        box.dispose();

        // top border
        def = new BodyDef();
        def.position.set(OFFSET_X + BOARD_WIDTH / 2f, OFFSET_Y + BOARD_HEIGHT + TILE_SIZE / 2f);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);
        box = new PolygonShape();
        box.setAsBox(BOARD_WIDTH / 2, TILE_SIZE / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("board_top");
        fixture.setDensity(0f);
        fixture.setRestitution(0f);
        fixture.setFriction(0f);
        box.dispose();
        //        def.position.set(OFFSET_X + width / 2f - border / 2f, OFFSET_Y + height / 2f + border / 2f);

        // Graph size
        setSize(BOARD_WIDTH * PPM, BOARD_HEIGHT * PPM);
        // Graph position
        setPosition(body.getPosition().x * PPM - getWidth() / 2f, body.getPosition().y * PPM - getHeight() / 2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // tile ground
        for(float posAuxY = OFFSET_Y + BOARD_HEIGHT - TILE_SIZE * 3f; posAuxY >= OFFSET_Y - TILE_SIZE; posAuxY -= TILE_SIZE * 4f) {
            for (float posAuxX = OFFSET_X + BOARD_WIDTH - TILE_SIZE * 3f; posAuxX >= OFFSET_X - TILE_SIZE; posAuxX -= TILE_SIZE * 4f) {
                batch.draw(ground, posAuxX * PPM, posAuxY * PPM
                        , TILE_SIZE * 4f * PPM, TILE_SIZE * 4f * PPM
                );
            }
        }

        // Render both textures.
        TextureRegion tube = textures.get(BoardTextures.TUBE);
        TextureRegion door = textures.get(BoardTextures.DOOR);
        TextureRegion doorTop = textures.get(BoardTextures.DOOR_TOP);
        TextureRegion cornerLeft = textures.get(BoardTextures.CORNER_LEFT);
        TextureRegion cornerRight = textures.get(BoardTextures.CORNER_RIGHT);

        // Corner top left
        batch.draw(cornerLeft, (OFFSET_X - TILE_SIZE) * PPM, (OFFSET_Y + BOARD_HEIGHT) * PPM
                , TILE_SIZE * PPM, TILE_SIZE * PPM
        );

        // Corner top right
        batch.draw(cornerRight, (OFFSET_X + BOARD_WIDTH) * PPM, (OFFSET_Y + BOARD_HEIGHT) * PPM
                , TILE_SIZE * PPM, TILE_SIZE * PPM
        );

        // Sides
        float posAux = OFFSET_Y;
        for(int idx = 0; idx < 6; idx++){
            // Left side
            batch.draw(door, (OFFSET_X - TILE_SIZE) * PPM, posAux * PPM
                    , TILE_SIZE * PPM, TILE_SIZE * 5 * PPM
            );


            // Right side
            batch.draw(door, (OFFSET_X + BOARD_WIDTH) * PPM, posAux * PPM
                    , TILE_SIZE * PPM, TILE_SIZE * 5 * PPM
            );

            posAux += TILE_SIZE * 5f;
        }

        // top
        for(int idx = 0; idx < 26; idx++) {

            if(idx == 4 || idx == 22) {
                batch.draw(doorTop, (OFFSET_X + TILE_SIZE * idx) * PPM, (OFFSET_Y + BOARD_HEIGHT) * PPM
                        , TILE_SIZE * 4 * PPM, TILE_SIZE * PPM
                );
                idx += 3;
            } else {
                batch.draw(tube, (OFFSET_X + TILE_SIZE * idx) * PPM, (OFFSET_Y + BOARD_HEIGHT ) * PPM
                        , TILE_SIZE * PPM, TILE_SIZE * PPM
                );
            }
        }

        // right side
        batch.draw(tube, (OFFSET_X + BOARD_WIDTH) * PPM, OFFSET_Y * PPM
                , 0f, 0f
                , TILE_SIZE * PPM, TILE_SIZE * PPM
                , 1f, 1f, 0.25f, false);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public enum BoardTextures {
        CORNER_LEFT,
        CORNER_RIGHT,
        TUBE,
        DOOR,
        DOOR_TOP
    }

    public void setLevel(int level){
        ground = grounds.get(level - 1);
    }
}
