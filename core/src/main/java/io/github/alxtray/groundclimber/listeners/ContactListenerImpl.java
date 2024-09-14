package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class ContactListenerImpl implements ContactListener {

    private final Array<ContactListener> contactListeners = new Array<>();

    public void addContactListener(ContactListener contactListener) {
        contactListeners.add(contactListener);
    }

    public Array<Body> getBodiesToDestroy() {
        Array<Body> bodiesToDestroy = new Array<>();
        for (ContactListener contactListener : contactListeners) {
            if (!(contactListener instanceof CrackedPlatformContactListener crackedPlatformContactListener)) {
                continue;
            }
            bodiesToDestroy.addAll(crackedPlatformContactListener.getPlatformsToDestroy());
            crackedPlatformContactListener.clearPlatformsToDestroy();
        }

        return bodiesToDestroy;
    }

    @Override
    public void beginContact(Contact contact) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.beginContact(contact);
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
