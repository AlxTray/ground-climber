package com.alx.groundclimber.utilities;

import com.alx.groundclimber.bodies.CrackedPlatform;
import com.alx.groundclimber.bodies.NormalPlatform;
import com.alx.groundclimber.bodies.Platform;
import com.alx.groundclimber.enums.LogLevel;
import com.badlogic.gdx.physics.box2d.World;

public class PlatformFactory {

  final World world;

  public PlatformFactory(World world) {
    this.world = world;
  }

  public Platform createPlatform(String type, float x, float y, float height, float width) {
    switch (type) {
      case "normal":
        Logger.log(
            "PlatformFactory",
            "Generated normal platform",
            LogLevel.DEBUG);
        return new NormalPlatform(world, x, y, height, width);
      case "cracked":
        Logger.log(
            "PlatformFactory",
            "Generated cracked platform",
            LogLevel.DEBUG);
        return new CrackedPlatform(world, x, y, height, width);
      default:
        throw new IllegalArgumentException("Invalid platform type " + type);
    }
  }

}
