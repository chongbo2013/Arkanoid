package com.arkanoid;

import com.arkanoid.Actors.ActorFactory;
import com.arkanoid.Actors.BlockActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;
import java.util.List;

import static com.arkanoid.Screens.CuasiConstantes.BOARD_HEIGHT;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_X;
import static com.arkanoid.Screens.CuasiConstantes.OFFSET_Y;
import static com.arkanoid.Screens.CuasiConstantes.PPM;
import static com.arkanoid.Screens.CuasiConstantes.TILE_SIZE;

public class Level {
    private List<Block> blocks;

    public Level(List<Block> blocks)
    {
        this.blocks = blocks;
    }
    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<BlockActor> build(ActorFactory factory, World world){

        LinkedList<BlockActor> blocksActors = new LinkedList<BlockActor>();

        Vector2 position;
        for(Block block:
                blocks){

            for(int idx = 0; idx < block.getQuantity(); idx++) {
                position = new Vector2(OFFSET_X + (block.getX() + (float)(block.getRepeatX() * idx) + 0.5f)* TILE_SIZE * 2f, OFFSET_Y + BOARD_HEIGHT - (block.getY() + (float)(block.getRepeatY() * idx) + 0.5f) * TILE_SIZE);
                blocksActors.add(factory.createBlock(world, position, block.getColor()));
            }
        }

        return blocksActors;
    }
}
