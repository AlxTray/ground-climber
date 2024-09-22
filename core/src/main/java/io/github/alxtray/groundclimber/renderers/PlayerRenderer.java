package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class PlayerRenderer {
    private final Texture playerImage;
    private final Texture playerFaceImage;

    public PlayerRenderer() {
        AssetLibrary assetLibrary = AssetLibrary.getInstance();
        playerImage = assetLibrary.getAsset("player", Texture.class);
        playerFaceImage = assetLibrary.getAsset("playerFace", Texture.class);
    }

    public void render(SpriteBatch batch, Player player) {
        batch.draw(
            playerImage,
            player.getPosition().x - (playerFaceImage.getWidth() / 2f),
            player.getPosition().y - (playerFaceImage.getHeight() / 2f));
        batch.draw(
            playerFaceImage,
            player.getPosition().x - (playerFaceImage.getWidth() / 2f),
            player.getPosition().y - (playerFaceImage.getHeight() / 2f),
            playerFaceImage.getWidth() / 2f,
            playerFaceImage.getWidth() / 2f,
            playerFaceImage.getWidth(),
            playerFaceImage.getHeight(),
            1f,
            1f,
            (float) Math.toDegrees(player.getBody().getAngle()),
            0,
            0,
            playerFaceImage.getWidth(),
            playerFaceImage.getHeight(),
            false,
            false);
    }

}
