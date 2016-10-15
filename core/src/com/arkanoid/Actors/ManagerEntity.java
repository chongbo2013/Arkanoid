package com.arkanoid.Actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebas on 11/10/2016.
 */

public class ManagerEntity {
    private final List<Entity> entities;
    private final List<Entity> entitiesToRemove;

    public ManagerEntity(){
        entities = new ArrayList<Entity>();
        entitiesToRemove = new ArrayList<Entity>();
    }

    public void add(List<Entity> blocks) {
        entities.addAll(blocks);
    }

    /**
     * Deleting entities dead
     *
     */
    public void update(){
        for (Entity entity : entitiesToRemove)
        {
            entity.remove();
            entities.remove(entity);
        }
        entitiesToRemove.clear();

        for (Entity entity :
                entities) {
            if (!entity.isAlive()) {
                entitiesToRemove.add(entity);
            }
        }
    }
}
