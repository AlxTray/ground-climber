package com.alx.groundclimber.listeners;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class ContactListenerImpl implements ContactListener {

    Array<ContactListener> contactListeners = new Array<>();

    public void addContactListener(ContactListener contactListener) {
        contactListeners.add(contactListener);
    }

    public Array<Body> getBodiesToDestroy() {
        Array<Body> bodiesToDestroy = new Array<>();
        for (ContactListener contactListener : contactListeners) {
            if (contactListener.getClass().getSimpleName().equals("CrackedPlatformContactListener")) {
                CrackedPlatformContactListener crackedPlatformContactListener = (CrackedPlatformContactListener) contactListener;
                bodiesToDestroy.addAll(crackedPlatformContactListener.getPlatformsToDestroy());
                crackedPlatformContactListener.clearPlatformsToDestroy();
            }
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
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    }

}
