package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class ContactListenerImpl implements ContactListener {
    private final Array<ContactListener> contactListeners = new Array<>();

    public void addContactListener(final ContactListener contactListener) {
        contactListeners.add(contactListener);
    }

    public Array<Body> getBodiesToDestroy() {
        final Array<Body> bodiesToDestroy = new Array<>();
        for (final ContactListener contactListener : contactListeners) {
            if (!(contactListener instanceof CrackedPlatformContactListener)) {
                continue;
            }
            final CrackedPlatformContactListener crackedPlatformListener =
                (CrackedPlatformContactListener) contactListener;
            bodiesToDestroy.addAll(crackedPlatformListener.getPlatformsToDestroy());
            crackedPlatformListener.clearPlatformsToDestroy();
        }

        return bodiesToDestroy;
    }

    @Override
    public void beginContact(final Contact contact) {
        for (final ContactListener contactListener : contactListeners) {
            contactListener.beginContact(contact);
        }
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
