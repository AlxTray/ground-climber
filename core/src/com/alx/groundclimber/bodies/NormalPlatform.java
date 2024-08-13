package com.alx.groundclimber.bodies;

import com.badlogic.gdx.physics.box2d.World;

public class NormalPlatform extends Platform {

  public NormalPlatform(World world, float x, float y, float height, float width) {
    super(world, x, y, height, width);
    this.body.setUserData(this);
  }

}
