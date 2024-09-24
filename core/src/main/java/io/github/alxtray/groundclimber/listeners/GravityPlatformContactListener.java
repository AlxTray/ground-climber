package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.alxtray.groundclimber.bodies.BouncyPlatform;
import io.github.alxtray.groundclimber.bodies.GravityPlatform;
import io.github.alxtray.groundclimber.bodies.Player;

public class GravityPlatformContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        Object bodyAUserData = bodyA.getUserData();
        Object bodyBUserData = bodyB.getUserData();
        if (!(bodyAUserData instanceof GravityPlatform) && !(bodyBUserData instanceof GravityPlatform)) {
            return;
        }

        Body playerBody = (bodyAUserData instanceof Player) ? bodyA : bodyB;
        World world = playerBody.getWorld();
        Vector2 currentGravity = world.getGravity();
        world.setGravity(new Vector2(currentGravity.x, -currentGravity.y));
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
