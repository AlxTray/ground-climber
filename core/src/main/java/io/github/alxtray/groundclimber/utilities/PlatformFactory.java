package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.bodies.platforms.*;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import text.formic.Stringf;

public class PlatformFactory {
    public static Platform createPlatform(World world, String type, String orientation, float x, float y, float height, float width) {
        PlatformOrientation platformOrientation = PlatformOrientation.valueOf(orientation);
        Logger.log(
            "PlatformFactory",
            Stringf.format("Generated %s platform", type),
            LogLevel.DEBUG);
        switch (type) {
            case "normal":
                return new NormalPlatform(world, platformOrientation, x, y, height, width);
            case "cracked":
                return new CrackedPlatform(world, platformOrientation, x, y, height, width);
            case "bouncy":
                return new BouncyPlatform(world, platformOrientation, x, y, height, width);
            case "gravity":
                return new GravityPlatform(world, platformOrientation, x, y, height, width);
            default:
                throw new IllegalArgumentException("Invalid platform type " + type);
        }
    }

}
