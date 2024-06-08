package com.alx.groundclimber.listeners;

import com.alx.groundclimber.bodies.CrackedPlatform;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class CrackedPlatformContactListener implements ContactListener {

    Array<Body> platformsToDestroy = new Array<>();

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        Object bodyAUserData = bodyA.getUserData();
        Object bodyBUserData = bodyB.getUserData();

        CrackedPlatform crackedPlatform;
        if (bodyAUserData instanceof CrackedPlatform) {
            crackedPlatform = (CrackedPlatform) bodyAUserData;
            crackedPlatform.incrementCrackLevel();
            if (crackedPlatform.getCrackLevel() >= 3) {
                platformsToDestroy.add(bodyA);
            }
        } else if (bodyBUserData instanceof CrackedPlatform) {
            crackedPlatform = (CrackedPlatform) bodyBUserData;
            crackedPlatform.incrementCrackLevel();
            if (crackedPlatform.getCrackLevel() >= 3) {
                platformsToDestroy.add(bodyB);
            }
        }
    }

    public Array<Body> getPlatformsToDestroy() {
        return platformsToDestroy;
    }

    public void clearPlatformsToDestroy() {
        platformsToDestroy.clear();
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