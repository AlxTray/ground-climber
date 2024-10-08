package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import io.github.alxtray.groundclimber.enums.PlatformStatus;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectListenerVisitor;

public class BouncyPlatform extends Platform {
    public BouncyPlatform(World world, PlatformOrientation orientation, float x, float y, float height, float width) {
        super(world, orientation, x, y, height, width);
        body.setUserData(this);
    }

    public Texture getOverlayTexture() {
        return AssetLibrary.getInstance().getAsset("bouncy_platform_overlay", Texture.class);
    }

    @Override
    public PlatformStatus acceptContact(EnvironmentObjectListenerVisitor visitor, Player player) {
        visitor.visitBouncyPlatform(player);
        return PlatformStatus.NoChange;
    }

}
