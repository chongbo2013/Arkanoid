package com.arkanoid.Screens;

import com.arkanoid.Actors.BallActor;
import com.arkanoid.Actors.BlockActor;
import com.arkanoid.Actors.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Sebas on 10/10/2016.
 */
public class GameContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object userDataA = fixtureA.getUserData();
        Object userDataB = fixtureB.getUserData();

        if(userDataA != null && userDataB != null){
            // userDataA ever is ball or fire
            if(userDataB.getClass() == BallActor.class) {
                Object userDataAux = userDataA;
                userDataA = userDataB;
                userDataB = userDataAux;
            }

            if(userDataA.getClass() == BallActor.class) {
                if(userDataB.getClass() == BlockActor.class)
                {
                    ((BlockActor)userDataB).hit(userDataA);
                }
                BallActor ball = (BallActor)userDataA;
                ball.refreshSpeed();
            }
        }
    }
}
