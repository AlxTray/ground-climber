package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class NormalPlatform extends Platform {
    public NormalPlatform(World world, float x, float y, float height, float width) {
        super(world, x, y, height, width);
        body.setUserData(this);
    }

}
