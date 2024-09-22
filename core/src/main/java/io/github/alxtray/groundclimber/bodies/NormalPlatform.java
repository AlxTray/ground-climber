package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.physics.box2d.World;

public class NormalPlatform extends Platform {
    public NormalPlatform(final World world, final float x, final float y, final float height, final float width) {
        super(world, x, y, height, width);
        body.setUserData(this);
    }

}
