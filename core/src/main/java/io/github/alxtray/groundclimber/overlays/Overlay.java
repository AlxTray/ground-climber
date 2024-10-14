package io.github.alxtray.groundclimber.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class Overlay {
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;
    private final Texture overlayTitle;

    public Overlay(String overlayTitleName) {
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        overlayTitle = AssetLibrary.getInstance().getAsset(overlayTitleName, Texture.class);
    }

    public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(
            camera.position.x - camera.viewportWidth / 2,
            camera.position.y - camera.viewportHeight / 2,
            camera.viewportWidth,
            camera.viewportHeight
        );
        shapeRenderer.end();

        batch.draw(overlayTitle, 0f, 0f, overlayTitle.getWidth(), overlayTitle.getHeight());

        stage.act(delta);
        stage.draw();
    }
}
