package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.alxtray.groundclimber.Core;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;

public class MenuScreen implements Screen {
    private static final float TITLE_MOVE_AMOUNT = 1.4f;
    protected final Core game;
    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final OrthographicCamera camera;
    protected final Skin skin;
    protected final float viewportTop;
    protected final float buttonHeight;
    protected final float buttonWidth;
    private final Texture title;
    private final float titleWidth;
    private final float titleHeight;
    private final float titleX;
    private float currentTitleY;
    private final float finalTitleY;
    private final Texture backgroundImage;

    public MenuScreen(final Core game, String titleImageName, String backgroundImageName, int buttonHeightFactor, int buttonWidthFactor) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        skin = AssetLibrary.getInstance().getAsset("skin", Skin.class);
        backgroundImage = AssetLibrary.getInstance().getAsset(backgroundImageName, Texture.class);
        title = AssetLibrary.getInstance().getAsset(titleImageName, Texture.class);
        titleWidth = camera.viewportWidth * 0.4f;
        titleHeight = camera.viewportHeight * 0.2f;
        titleX = (camera.viewportWidth - titleWidth) / 2;
        currentTitleY = camera.viewportHeight;
        finalTitleY = (camera.viewportHeight - titleHeight) / 1.2f;

        viewportTop = camera.position.y + camera.viewportHeight / 2;
        buttonHeight = (float) Gdx.graphics.getHeight() / buttonHeightFactor;
        buttonWidth = (float) Gdx.graphics.getWidth() / buttonWidthFactor;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(
            backgroundImage,
            camera.position.x - (camera.viewportWidth / 2),
            camera.position.y - (camera.viewportHeight / 2),
            camera.viewportWidth,
            camera.viewportHeight);
        if (currentTitleY > finalTitleY) {
            currentTitleY -= TITLE_MOVE_AMOUNT;
        }
        batch.draw(
            title,
            titleX,
            currentTitleY,
            titleWidth,
            titleHeight);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() { // No logic needed
    }

    @Override
    public void resize(int i, int i1) { // No logic needed for resize currently
    }

    @Override
    public void pause() { // No logic needed
    }

    @Override
    public void resume() { // No logic needed
    }

    @Override
    public void hide() { // No logic needed
    }

    @Override
    public void dispose() {
        batch.dispose();
        Logger.log(
            "MenuScreen",
            "Disposed objects",
            LogLevel.DEBUG);
    }

}
