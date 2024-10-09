package io.github.alxtray.groundclimber.bodies.platforms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import io.github.alxtray.groundclimber.enums.ObjectStatus;
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
    public ObjectStatus acceptContact(EnvironmentObjectListenerVisitor visitor, Player player) {
        visitor.visitBouncyPlatform(player);
        return ObjectStatus.NoChange;
    }

}
