package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.alxtray.groundclimber.bodies.BouncyPlatform;
import io.github.alxtray.groundclimber.bodies.Player;

public class BouncyPlatformContactListener implements ContactListener {
    private static final Vector2 BOUNCE_LINEAR_IMPULSE = new Vector2(0, -20000f);

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        Object bodyAUserData = bodyA.getUserData();
        Object bodyBUserData = bodyB.getUserData();
        if (!(bodyAUserData instanceof BouncyPlatform) && !(bodyBUserData instanceof BouncyPlatform)) {
            return;
        }

        Body playerBody = (bodyAUserData instanceof Player) ? bodyA : bodyB;
        playerBody.applyLinearImpulse(BOUNCE_LINEAR_IMPULSE, playerBody.getWorldCenter(), true);
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
