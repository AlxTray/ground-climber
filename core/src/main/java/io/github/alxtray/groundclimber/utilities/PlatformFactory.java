package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.bodies.BouncyPlatform;
import io.github.alxtray.groundclimber.bodies.CrackedPlatform;
import io.github.alxtray.groundclimber.bodies.NormalPlatform;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;

public class PlatformFactory {
    public static Platform createPlatform(World world, String type, String orientation, float x, float y, float height, float width) {
        PlatformOrientation platformOrientation = PlatformOrientation.valueOf(orientation);
        switch (type) {
            case "normal":
                Logger.log(
                    "PlatformFactory",
                    "Generated normal platform",
                    LogLevel.DEBUG);
                return new NormalPlatform(world, platformOrientation, x, y, height, width);
            case "cracked":
                Logger.log(
                    "PlatformFactory",
                    "Generated cracked platform",
                    LogLevel.DEBUG);
                return new CrackedPlatform(world, platformOrientation, x, y, height, width);
            case "bouncy":
                Logger.log(
                    "PlatformFactory",
                    "Generated bouncy platform",
                    LogLevel.DEBUG);
                return new BouncyPlatform(world, platformOrientation, x, y, height, width);
            default:
                throw new IllegalArgumentException("Invalid platform type " + type);
        }
    }

}
