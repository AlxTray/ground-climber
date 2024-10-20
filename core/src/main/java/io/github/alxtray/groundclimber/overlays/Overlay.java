package io.github.alxtray.groundclimber.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.ButtonBuilder;

public class Overlay {
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;
    private final Texture overlayTitle;

    public Overlay(String overlayTitleName, ClickListener resetListener, ClickListener quitListener) {
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        overlayTitle = AssetLibrary.getInstance().getAsset(overlayTitleName, Texture.class);

        Skin skin = AssetLibrary.getInstance().getAsset("skin", Skin.class);
        new ButtonBuilder("Restart", skin, stage)
            .setSize(buttonWidth, buttonHeight)
            .setPosition(camera.position.x - (buttonWidth / 2), viewportTop - levelsButtonTopMargin)
            .setClickListener()
            .build();
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
