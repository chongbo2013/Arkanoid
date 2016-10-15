package com.arkanoid.Actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Sebas on 10/10/2016.
 */

public abstract class Entity extends Actor {
    private boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
