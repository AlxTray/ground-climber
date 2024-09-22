package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class BackgroundObjectRenderer {
    private final Texture backgroundImage;

    public BackgroundObjectRenderer() {
        backgroundImage = AssetLibrary.getInstance().getAsset("game_background", Texture.class);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.disableBlending();
        batch.draw(
            backgroundImage,
            camera.position.x - (camera.viewportWidth / 2),
            camera.position.y - (camera.viewportHeight / 2),
            camera.viewportWidth,
            camera.viewportHeight);
        batch.enableBlending();
    }

}
