package io.github.alxtray.groundclimber.utilities;

import io.github.alxtray.groundclimber.bodies.CrackedPlatform;
import io.github.alxtray.groundclimber.bodies.NormalPlatform;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.LogLevel;
import com.badlogic.gdx.physics.box2d.World;

public class PlatformFactory {

    private final World world;

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
