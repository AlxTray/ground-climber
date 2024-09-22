package io.github.alxtray.groundclimber.listeners;

import io.github.alxtray.groundclimber.bodies.CrackedPlatform;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import io.github.alxtray.groundclimber.bodies.NormalPlatform;
import io.github.alxtray.groundclimber.bodies.Player;

public class CrackedPlatformContactListener implements ContactListener {

    private final Array<Body> platformsToDestroy = new Array<>();

    public Array<Body> getPlatformsToDestroy() {
        return platformsToDestroy;
    }

    public void clearPlatformsToDestroy() {
        platformsToDestroy.clear();
    }

    @Override
    public void beginContact(final Contact contact) {
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final Object bodyAUserData = bodyA.getUserData();
        final Object bodyBUserData = bodyB.getUserData();
        if (!(bodyAUserData instanceof CrackedPlatform) && !(bodyBUserData instanceof CrackedPlatform)) {
            return;
        }

        CrackedPlatform crackedPlatform;
        Body bodyToDestroy;
        if (bodyAUserData instanceof Player) {
            crackedPlatform = (CrackedPlatform) bodyBUserData;
            bodyToDestroy = bodyB;
        } else {
            crackedPlatform = (CrackedPlatform) bodyAUserData;
            bodyToDestroy = bodyA;
        }
        crackedPlatform.incrementCrackLevel();
        if (crackedPlatform.getCrackLevel() >= 3) {
            platformsToDestroy.add(bodyToDestroy);
        }
    }

    @Override
    public void endContact(Contact contact) { // Nothing to do once contact has ended
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) { // No logic needed here
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) { // No logic needed here
    }

}
