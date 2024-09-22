package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.physics.box2d.*;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.Logger;
import text.formic.Stringf;

public class DebugContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final Object bodyAUserData = bodyA.getUserData();
        final Object bodyBUserData = bodyB.getUserData();

        Logger.log(
            "Contact",
            Stringf.format(
                "Object %s collided with %s",
                bodyBUserData.getClass().getSimpleName(),
                bodyAUserData.getClass().getSimpleName()),
            LogLevel.DEBUG);
    }

    @Override
    public void endContact(final Contact contact) { // Nothing to do once contact has ended
    }

    @Override
    public void preSolve(final Contact contact, final Manifold manifold) { // No logic needed here
    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse contactImpulse) { // No logic needed here
    }

}
