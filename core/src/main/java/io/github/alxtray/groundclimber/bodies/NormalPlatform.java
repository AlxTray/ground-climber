package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;

public class NormalPlatform extends Platform {
    public NormalPlatform(World world, PlatformOrientation orientation, float x, float y, float height, float width) {
        super(world, orientation, x, y, height, width);
        body.setUserData(this);
    }

}
