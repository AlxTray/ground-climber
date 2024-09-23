package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class BouncyPlatform extends Platform {
    public BouncyPlatform(World world, float x, float y, float height, float width) {
        super(world, x, y, height, width);
        body.setUserData(this);
    }

    public Texture getOverlayTexture() {
        return AssetLibrary.getInstance().getAsset("bouncy_platform_overlay", Texture.class);
    }

}
