package com.alx.groundclimber.bodies;

import com.alx.groundclimber.enums.LogLevel;
import com.alx.groundclimber.utilities.Logger;
import com.badlogic.gdx.physics.box2d.World;

public class CrackedPlatform extends Platform {

  int crackLevel = 0;

  public CrackedPlatform(World world, float x, float y, float height, float width) {
    super(world, x, y, height, width);
    this.body.setUserData(this);
  }

  public void incrementCrackLevel() {
    crackLevel++;
    Logger.log(
        "CrackedPlatform",
        String.format(
            "Cracked level for platform at (%s, %s) is now %s",
            this.body.getPosition().x,
            this.body.getPosition().y,
            this.crackLevel),
        LogLevel.DEBUG);
  }

  public int getCrackLevel() {
    return crackLevel;
  }

}
