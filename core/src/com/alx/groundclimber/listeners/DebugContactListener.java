package com.alx.groundclimber.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class DebugContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        Object bodyAUserData = bodyA.getUserData();
        Object bodyBUserData = bodyB.getUserData();

        Gdx.app.debug("Contact DEBUG", "Object " + bodyAUserData.getClass().getSimpleName() + " collided with " + bodyBUserData.getClass().getSimpleName());
    }


    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    }

}