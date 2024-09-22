package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.bodies.CrackedPlatform;
import io.github.alxtray.groundclimber.bodies.NormalPlatform;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.LogLevel;

public class PlatformFactory {
    public PlatformFactory() {
    }

    public Platform createPlatform(World world, final String type, final float x, final float y, final float height, final float width) {
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
