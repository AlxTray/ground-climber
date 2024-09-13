package com.alx.groundclimber.listeners;

import com.alx.groundclimber.enums.LogLevel;
import com.alx.groundclimber.utilities.Logger;
import com.badlogic.gdx.physics.box2d.*;

public class DebugContactListener implements ContactListener {

  @Override
  public void beginContact(Contact contact) {
    Body bodyA = contact.getFixtureA().getBody();
    Body bodyB = contact.getFixtureB().getBody();
    Object bodyAUserData = bodyA.getUserData();
    Object bodyBUserData = bodyB.getUserData();

    Logger.log(
        "Contact",
        String.format(
            "Object %s collided with %s",
            bodyBUserData.getClass().getSimpleName(),
            bodyAUserData.getClass().getSimpleName()),
        LogLevel.DEBUG);
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
