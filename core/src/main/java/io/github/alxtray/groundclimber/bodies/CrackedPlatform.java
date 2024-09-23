package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;
import text.formic.Stringf;

public class CrackedPlatform extends Platform {
    private int crackLevel;

    public CrackedPlatform(World world, float x, float y, float height, float width) {
        super(world, x, y, height, width);
        crackLevel = 0;
        body.setUserData(this);
    }

    public Texture getOverlayTexture() {
        return AssetLibrary.getInstance().getAsset("cracked_platform_overlay", Texture.class);
    }

    public void incrementCrackLevel() {
        crackLevel++;
        Logger.log(
            "CrackedPlatform",
            Stringf.format(
                "Cracked level for platform at (%s, %s) is now %s",
                body.getPosition().x,
                body.getPosition().y,
                crackLevel),
            LogLevel.DEBUG);
    }

    public int getCrackLevel() {
        return crackLevel;
    }

}
