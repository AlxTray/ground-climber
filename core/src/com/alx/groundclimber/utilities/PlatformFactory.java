package com.alx.groundclimber.utilities;

import com.alx.groundclimber.bodies.CrackedPlatform;
import com.alx.groundclimber.bodies.NormalPlatform;
import com.alx.groundclimber.bodies.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

public class PlatformFactory {

    World world;

    public PlatformFactory(World world) {
        this.world = world;
    }

    public Platform createPlatform(String type, float x, float y, float height, float width) {
        switch (type) {
            case "normal":
                Gdx.app.debug("PlatformFactory - DEBUG", "Generated normal platform");
                return new NormalPlatform(world, x, y, height, width);
            case "cracked":
                Gdx.app.debug("PlatformFactory - DEBUG", "Generated cracked platform");
                return new CrackedPlatform(world, x, y, height, width);
            default:
                throw new IllegalArgumentException("Invalid platform type " + type);
        }
    }

}
