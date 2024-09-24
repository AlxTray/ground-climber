package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class GravityPlatform extends Platform {
    public GravityPlatform(World world, PlatformOrientation orientation, float x, float y, float height, float width) {
        super(world, orientation, x, y, height, width);
        body.setUserData(this);
    }

    public Texture getOverlayTexture() {
        return AssetLibrary.getInstance().getAsset("gravity_platform_overlay", Texture.class);
    }
}
