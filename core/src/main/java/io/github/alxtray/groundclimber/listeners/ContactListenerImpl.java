package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.enums.PlatformStatus;
import io.github.alxtray.groundclimber.utilities.Logger;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectListenerVisitor;
import text.formic.Stringf;

import java.util.Objects;

public class ContactListenerImpl implements ContactListener {
    private final EnvironmentObjectListenerVisitor environmentObjectListener;
    private final Array<Body> objectsToDestroy = new Array<>();

    public ContactListenerImpl() {
        environmentObjectListener = new EnvironmentObjectListener();
    }

    @Override
    public void beginContact(Contact contact) {
        Body environmentObjectBody = (contact.getFixtureA().getBody().getUserData() instanceof Player) ?
            contact.getFixtureB().getBody() : contact.getFixtureA().getBody();
        Body playerBody = (contact.getFixtureA().getBody().getUserData() instanceof Player) ?
            contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
        PlatformStatus status =
            ((EnvironmentObject) environmentObjectBody.getUserData()).acceptContact(environmentObjectListener, (Player) playerBody.getUserData());

        if (Objects.requireNonNull(status) == PlatformStatus.Remove) {
            objectsToDestroy.add(environmentObjectBody);
        }

        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Logger.log(
                "Contact",
                Stringf.format(
                    "Object %s collided with %s",
                    playerBody.getUserData().getClass().getSimpleName(),
                    environmentObjectBody.getUserData().getClass().getSimpleName()),
                LogLevel.DEBUG);
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

    public Array<Body> getObjectsToDestroy() {
        return objectsToDestroy;
    }

    public void clearObjectsToDestroy() {
        objectsToDestroy.clear();
    }

}
